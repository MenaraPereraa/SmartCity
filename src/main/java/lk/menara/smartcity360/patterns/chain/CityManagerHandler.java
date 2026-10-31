package lk.menara.smartcity360.patterns.chain;

import lk.menara.smartcity360.model.Complaint;
import lk.menara.smartcity360.model.enums.HandlerLevel;

public class CityManagerHandler extends ComplaintHandler {
    public CityManagerHandler() {
        super(HandlerLevel.CITY_MANAGER);
    }

    @Override
    protected String resolution(Complaint complaint) {
        return "Emergency response opened by city manager";
    }
}
