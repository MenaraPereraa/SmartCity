package lk.menara.smartcity360.patterns.interpreter;

import java.util.Map;
import java.util.Objects;

public class NotExpression implements Expression {
    private final Expression operand;

    public NotExpression(Expression operand) {
        this.operand = Objects.requireNonNull(operand, "Operand expression is required");
    }

    @Override
    public boolean interpret(Map<String, Double> context) {
        return !operand.interpret(context);
    }

    @Override
    public String toString() {
        return operand instanceof ComparisonExpression ? "NOT (" + operand + ")" : "NOT " + operand;
    }
}
