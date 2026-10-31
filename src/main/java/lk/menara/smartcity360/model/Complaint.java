package lk.menara.smartcity360.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lk.menara.smartcity360.config.AppConstants;
import lk.menara.smartcity360.exception.ValidationException;
import lk.menara.smartcity360.model.enums.ComplaintStatus;

public class Complaint {
    private final String description;
    private final int severity;
    private final List<ComplaintEvent> events = new ArrayList<>();
    private ComplaintStatus status = ComplaintStatus.OPEN;

    public Complaint(String description, int severity) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Complaint description is required");
        }
        if (severity < AppConstants.MIN_SEVERITY || severity > AppConstants.MAX_SEVERITY) {
            throw new ValidationException("Severity must be between "
                    + AppConstants.MIN_SEVERITY + " and " + AppConstants.MAX_SEVERITY);
        }
        this.description = description;
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public int getSeverity() {
        return severity;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public List<ComplaintEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public void recordEvent(ComplaintEvent event, ComplaintStatus newStatus) {
        events.add(event);
        this.status = newStatus;
    }
}
