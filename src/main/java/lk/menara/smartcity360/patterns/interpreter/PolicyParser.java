package lk.menara.smartcity360.patterns.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lk.menara.smartcity360.exception.PolicyException;
import lk.menara.smartcity360.model.enums.ComparisonOperator;

/**
 * Turns policy text into an {@link Expression} tree using recursive descent.
 *
 * <pre>
 * expression := term ( "OR" term )*
 * term       := factor ( "AND" factor )*
 * factor     := "NOT" factor | "(" expression ")" | VARIABLE OPERATOR NUMBER
 * </pre>
 *
 * Keywords are case-insensitive. NOT binds tighter than AND, and AND tighter than OR.
 */
public final class PolicyParser {
    private enum TokenType { NUMBER, IDENTIFIER, OPERATOR, LEFT_PAREN, RIGHT_PAREN, AND, OR, NOT, END }

    private record Token(TokenType type, String text, int position) {
    }

    private final List<Token> tokens;
    private int current;

    private PolicyParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static Expression parse(String text) {
        if (text == null || text.isBlank()) {
            throw new PolicyException("Policy expression is empty");
        }
        PolicyParser parser = new PolicyParser(tokenize(text));
        Expression expression = parser.parseExpression();
        if (parser.peek().type() != TokenType.END) {
            throw parser.unexpected("Expected AND, OR or end of expression");
        }
        return expression;
    }

    private Expression parseExpression() {
        Expression left = parseTerm();
        while (match(TokenType.OR)) {
            left = new OrExpression(left, parseTerm());
        }
        return left;
    }

    private Expression parseTerm() {
        Expression left = parseFactor();
        while (match(TokenType.AND)) {
            left = new AndExpression(left, parseFactor());
        }
        return left;
    }

    private Expression parseFactor() {
        if (match(TokenType.NOT)) {
            return new NotExpression(parseFactor());
        }
        if (match(TokenType.LEFT_PAREN)) {
            Expression inner = parseExpression();
            consume(TokenType.RIGHT_PAREN, "Expected ')'");
            return inner;
        }
        Token variable = consume(TokenType.IDENTIFIER, "Expected a variable name, NOT or '('");
        Token operator = consume(TokenType.OPERATOR, "Expected a comparison operator after '" + variable.text() + "'");
        Token number = consume(TokenType.NUMBER, "Expected a number after '" + operator.text() + "'");
        return new ComparisonExpression(
                variable.text(),
                ComparisonOperator.fromSymbol(operator.text()).orElseThrow(),
                Double.parseDouble(number.text()));
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean match(TokenType type) {
        if (peek().type() == type) {
            current++;
            return true;
        }
        return false;
    }

    private Token consume(TokenType type, String expected) {
        if (peek().type() != type) {
            throw unexpected(expected);
        }
        return tokens.get(current++);
    }

    private PolicyException unexpected(String expected) {
        Token found = peek();
        String what = found.type() == TokenType.END ? "end of expression" : "'" + found.text() + "'";
        return new PolicyException(expected + " but found " + what + " at position " + found.position());
    }

    private static List<Token> tokenize(String text) {
        List<Token> tokens = new ArrayList<>();
        int length = text.length();
        int i = 0;
        while (i < length) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
            } else if (c == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "(", i++));
            } else if (c == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")", i++));
            } else if (c == '>' || c == '<' || c == '=' || c == '!') {
                boolean twoChars = i + 1 < length && text.charAt(i + 1) == '=';
                String symbol = twoChars ? text.substring(i, i + 2) : String.valueOf(c);
                if (ComparisonOperator.fromSymbol(symbol).isEmpty()) {
                    throw new PolicyException("Unknown operator '" + symbol + "' at position " + i);
                }
                tokens.add(new Token(TokenType.OPERATOR, symbol, i));
                i += symbol.length();
            } else if (isDigit(c) || (c == '-' && i + 1 < length && isDigit(text.charAt(i + 1)))) {
                int start = i++;
                while (i < length && (isDigit(text.charAt(i)) || text.charAt(i) == '.')) {
                    i++;
                }
                String number = text.substring(start, i);
                try {
                    if (!Double.isFinite(Double.parseDouble(number))) {
                        throw new PolicyException("Number '" + number + "' is out of range at position " + start);
                    }
                } catch (NumberFormatException e) {
                    throw new PolicyException("Invalid number '" + number + "' at position " + start);
                }
                tokens.add(new Token(TokenType.NUMBER, number, start));
            } else if (Character.isLetter(c) || c == '_') {
                int start = i++;
                while (i < length && (Character.isLetterOrDigit(text.charAt(i)) || text.charAt(i) == '_')) {
                    i++;
                }
                String word = text.substring(start, i);
                TokenType type = switch (word.toUpperCase(Locale.ROOT)) {
                    case "AND" -> TokenType.AND;
                    case "OR" -> TokenType.OR;
                    case "NOT" -> TokenType.NOT;
                    default -> TokenType.IDENTIFIER;
                };
                tokens.add(new Token(type, word, start));
            } else {
                throw new PolicyException("Unexpected character '" + c + "' at position " + i);
            }
        }
        tokens.add(new Token(TokenType.END, "", length));
        return tokens;
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
