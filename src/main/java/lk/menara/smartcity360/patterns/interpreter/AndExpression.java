package lk.menara.smartcity360.patterns.interpreter;

import java.util.Map;
import java.util.Objects;

public class AndExpression implements Expression {
    private final Expression left;
    private final Expression right;

    public AndExpression(Expression left, Expression right) {
        this.left = Objects.requireNonNull(left, "Left expression is required");
        this.right = Objects.requireNonNull(right, "Right expression is required");
    }

    @Override
    public boolean interpret(Map<String, Double> context) {
        return left.interpret(context) && right.interpret(context);
    }

    @Override
    public String toString() {
        return "(" + left + " AND " + right + ")";
    }
}
