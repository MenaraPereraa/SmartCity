package lk.menara.smartcity360.patterns.chain;

public class LocalStaff extends ComplaintHandler {
    @Override
    public void handleComplaint(Complaint complaint) {
        if (complaint.getSeverityLevel() == 1) {
            System.out.println("Local Staff handled the complaint: " + complaint.getDescription());
        } else if (nextHandler != null) {
            System.out.println("Local Staff cannot handle. Escalating to Supervisor...");
            nextHandler.handleComplaint(complaint);
        }
    }
}