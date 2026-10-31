package lk.menara.smartcity360.patterns.interpreter;

import java.util.Map;
import lk.menara.smartcity360.exception.PolicyException;
import lk.menara.smartcity360.exception.ValidationException;
import lk.menara.smartcity360.model.enums.ComparisonOperator;

/** Terminal expression: compares one context variable with a number, e.g. {@code noise_level > 70}. */
public class ComparisonExpression implements Expression {
    private final String variable;
    private final ComparisonOperator operator;
    private final double value;

    public ComparisonExpression(String variable, ComparisonOperator operator, double value) {
        if (variable == null || variable.isBlank()) {
            throw new ValidationException("Variable name is required");
        }
        if (operator == null) {
            throw new ValidationException("Comparison operator is required");
        }
        if (!Double.isFinite(value)) {
            throw new ValidationException("Comparison value must be a finite number");
        }
        this.variable = variable;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean interpret(Map<String, Double> context) {
        Double actual = context.get(variable);
        if (actual == null) {
            throw new PolicyException("No value supplied for variable '" + variable + "'");
        }
        return operator.test(actual, value);
    }

    @Override
    public String toString() {
        return variable + " " + operator.getSymbol() + " " + formatNumber(value);
    }

    private static String formatNumber(double number) {
        if (number == Math.rint(number) && Math.abs(number) < 1e15) {
            return String.valueOf((long) number);
        }
        return String.valueOf(number);
    }
}
