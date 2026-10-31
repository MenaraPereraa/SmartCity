package lk.menara.smartcity360.patterns.chain;

import lk.menara.smartcity360.model.Complaint;
import lk.menara.smartcity360.model.ComplaintEvent;
import lk.menara.smartcity360.model.enums.ComplaintOutcome;
import lk.menara.smartcity360.model.enums.ComplaintStatus;
import lk.menara.smartcity360.model.enums.HandlerLevel;

public abstract class ComplaintHandler {
    private final HandlerLevel level;
    private ComplaintHandler nextHandler;

    protected ComplaintHandler(HandlerLevel level) {
        this.level = level;
    }

    public ComplaintHandler setNextHandler(ComplaintHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public ComplaintHandler getNextHandler() {
        return nextHandler;
    }

    public final void handle(Complaint complaint) {
        if (level.canHandle(complaint.getSeverity())) {
            complaint.recordEvent(
                    ComplaintEvent.of(level, ComplaintOutcome.HANDLED, resolution(complaint)),
                    ComplaintStatus.RESOLVED);
            return;
        }
        if (nextHandler == null) {
            complaint.recordEvent(
                    ComplaintEvent.of(level, ComplaintOutcome.UNRESOLVED, "No higher handler available"),
                    ComplaintStatus.UNRESOLVED);
            return;
        }
        complaint.recordEvent(
                ComplaintEvent.of(level, ComplaintOutcome.ESCALATED,
                        "Severity " + complaint.getSeverity() + " exceeds limit " + level.getMaxSeverity()),
                ComplaintStatus.ESCALATED);
        nextHandler.handle(complaint);
    }

    protected abstract String resolution(Complaint complaint);
}
