package lk.menara.smartcity360.model.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ComparisonOperator {
    GREATER_THAN(">"),
    GREATER_OR_EQUAL(">="),
    LESS_THAN("<"),
    LESS_OR_EQUAL("<="),
    EQUAL("=="),
    NOT_EQUAL("!=");

    private static final double EPSILON = 1e-9;

    private final String symbol;

    ComparisonOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean test(double left, double right) {
        return switch (this) {
            case GREATER_THAN -> left > right;
            case GREATER_OR_EQUAL -> left >= right;
            case LESS_THAN -> left < right;
            case LESS_OR_EQUAL -> left <= right;
            case EQUAL -> Math.abs(left - right) < EPSILON;
            case NOT_EQUAL -> Math.abs(left - right) >= EPSILON;
        };
    }

    public static Optional<ComparisonOperator> fromSymbol(String symbol) {
        return Arrays.stream(values()).filter(operator -> operator.symbol.equals(symbol)).findFirst();
    }
}
