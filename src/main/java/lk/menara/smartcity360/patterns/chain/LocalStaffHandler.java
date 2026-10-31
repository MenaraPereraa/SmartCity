package lk.menara.smartcity360.patterns.chain;

import lk.menara.smartcity360.model.Complaint;
import lk.menara.smartcity360.model.enums.HandlerLevel;

public class LocalStaffHandler extends ComplaintHandler {
    public LocalStaffHandler() {
        super(HandlerLevel.LOCAL_STAFF);
    }

    @Override
    protected String resolution(Complaint complaint) {
        return "Resolved on site by local staff";
    }
}
