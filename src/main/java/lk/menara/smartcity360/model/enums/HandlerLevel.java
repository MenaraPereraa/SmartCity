package lk.menara.smartcity360.model.enums;

import lk.menara.smartcity360.config.AppConstants;

public enum HandlerLevel {
    LOCAL_STAFF(3),
    SUPERVISOR(7),
    CITY_MANAGER(AppConstants.MAX_SEVERITY);

    private final int maxSeverity;

    HandlerLevel(int maxSeverity) {
        this.maxSeverity = maxSeverity;
    }

    public int getMaxSeverity() {
        return maxSeverity;
    }

    public boolean canHandle(int severity) {
        return severity <= maxSeverity;
    }
}
