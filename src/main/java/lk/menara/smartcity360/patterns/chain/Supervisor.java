package lk.menara.smartcity360.patterns.chain;

public class Supervisor extends ComplaintHandler {
    @Override
    public void handleComplaint(Complaint complaint) {
        if (complaint.getSeverityLevel() == 2) {
            System.out.println("Supervisor handled the complaint: " + complaint.getDescription());
        } else if (nextHandler != null) {
            System.out.println("Supervisor cannot handle. Escalating to City Manager...");
            nextHandler.handleComplaint(complaint);
        }
    }
}