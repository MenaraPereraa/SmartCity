package lk.menara.smartcity360.model;

import java.time.Instant;
import lk.menara.smartcity360.model.enums.ComplaintOutcome;
import lk.menara.smartcity360.model.enums.HandlerLevel;

public record ComplaintEvent(HandlerLevel handler, ComplaintOutcome outcome, String remarks, Instant occurredAt) {

    public static ComplaintEvent of(HandlerLevel handler, ComplaintOutcome outcome, String remarks) {
        return new ComplaintEvent(handler, outcome, remarks, Instant.now());
    }
}
