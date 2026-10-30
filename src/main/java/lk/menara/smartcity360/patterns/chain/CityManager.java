package lk.menara.smartcity360.patterns.chain;

public class CityManager extends ComplaintHandler {
    @Override
    public void handleComplaint(Complaint complaint) {
        if (complaint.getSeverityLevel() >= 3) {
            System.out.println("City Manager handled critical complaint: " + complaint.getDescription());
        } else {
            System.out.println("Complaint could not be handled.");
        }
    }
}