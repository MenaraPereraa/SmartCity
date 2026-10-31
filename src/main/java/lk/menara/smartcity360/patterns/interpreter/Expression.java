package lk.menara.smartcity360.patterns.interpreter;

import java.util.Map;

/**
 * Abstract expression of the policy language. Every node of the parsed tree
 * (comparison, AND, OR, NOT) implements this one method.
 */
public interface Expression {
    boolean interpret(Map<String, Double> context);
}
