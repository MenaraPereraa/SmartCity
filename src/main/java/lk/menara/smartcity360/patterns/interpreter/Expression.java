package lk.menara.smartcity360.patterns.interpreter;

import java.util.Map;


public interface Expression {
    boolean interpret(Map<String, Double> context);
}
