package lk.menara.smartcity360.patterns.chain;

import lk.menara.smartcity360.model.Complaint;
import lk.menara.smartcity360.model.enums.HandlerLevel;

public class SupervisorHandler extends ComplaintHandler {
    public SupervisorHandler() {
        super(HandlerLevel.SUPERVISOR);
    }

    @Override
    protected String resolution(Complaint complaint) {
        return "Repair crew assigned by supervisor";
    }
}
