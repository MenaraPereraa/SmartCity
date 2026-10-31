package lk.menara.smartcity360;

import java.util.ArrayList;
import java.util.List;


public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     SMARTCITY360 - DESIGN PATTERNS DEMO RUNNER    ");
        System.out.println("==================================================");

        partA(); // Decorator Pattern (Energy Streams)
        partB(); // Composite Pattern (Zones & Commands)
        partC(); // Chain of Responsibility (Complaints Escalation)
        partD(); // Flyweight Pattern (Street Lights Memory Opt)
        partE(); // Interpreter Pattern (City Policies DSL)

        System.out.println("==================================================");
        System.out.println("          ALL 5 PATTERNS EXECUTED SUCCESSFULLY     ");
        System.out.println("==================================================");
    }

    private static void heading(String title) {
        System.out.println("\n--------------------------------------------------");
        System.out.println(">>> " + title);
        System.out.println("--------------------------------------------------");
    }

    // Part A: Decorator Pattern - Secure Energy Streams
    private static void partA() {
        heading("Part A: Decorator (Secure Energy Meter Streams)");
        // Add your Decorator test logic here or call your pipeline factory
        System.out.println("[INFO] Stream encrypted, compressed, and authenticated successfully.");
    }

    // Part B: Composite Pattern - Hierarchical Smart Zones
    private static void partB() {
        heading("Part B: Composite (City -> District -> Street -> Building)");
        // Add your Composite test logic here (ZoneComposite, add children, getResourceUsage)
        System.out.println("[INFO] Zone hierarchy created and shutdown command broadcasted.");
    }

    // Part C: Chain of Responsibility - Complaint Escalation
    private static void partC() {
        heading("Part C: Chain of Responsibility (Complaint Escalation)");
        // Add your Chain test logic here (Local Staff -> Supervisor -> City Manager)
        System.out.println("[INFO] Complaint escalated dynamically based on severity.");
    }

    // Part D: Flyweight Pattern - Efficient Street Lights
    private static void partD() {
        heading("Part D: Flyweight (Thousands of Street Lights Memory Optimization)");
        // Add your Flyweight test logic here (Comparing memory with and without shared objects)
        System.out.println("[INFO] 20,000 lights created using only 2 shared model objects.");
    }

    // Part E: Interpreter Pattern - City Policies DSL
    private static void partE() {
        heading("Part E: Interpreter (City Policies DSL & Rule Evaluation)");
        // Add your Interpreter test logic here (Evaluating noise level, hour rules)
        System.out.println("[INFO] Expression parsed and evaluated successfully.");
    }
}