package lk.menara.smartcity360.patterns.chain;

public final class ComplaintChainFactory {
    private ComplaintChainFactory() {
    }

    public static ComplaintHandler build() {
        ComplaintHandler localStaff = new LocalStaffHandler();
        localStaff.setNextHandler(new SupervisorHandler()).setNextHandler(new CityManagerHandler());
        return localStaff;
    }
}
