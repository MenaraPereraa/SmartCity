package lk.menara.smartcity360.patterns.chain;

public class TestRun {
    public static void main(String[] args) {
        ComplaintHandler localStaff = new LocalStaff();
        ComplaintHandler supervisor = new Supervisor();
        ComplaintHandler cityManager = new CityManager();

        // Create Chain (Local -> Supervisor -> CityManager)
        localStaff.setNextHandler(supervisor);
        supervisor.setNextHandler(cityManager);

        Complaint c1 = new Complaint("Street light not working", 1);
        Complaint c2 = new Complaint("Water pipe leakage in street", 2);
        Complaint c3 = new Complaint("Major power grid failure in city", 3);

        System.out.println("--- Processing Complaint 1 ---");
        localStaff.handleComplaint(c1);

        System.out.println("\n--- Processing Complaint 2 ---");
        localStaff.handleComplaint(c2);

        System.out.println("\n--- Processing Complaint 3 ---");
        localStaff.handleComplaint(c3);
    }
}