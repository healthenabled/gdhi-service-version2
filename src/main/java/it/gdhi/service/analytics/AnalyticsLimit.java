package it.gdhi.service.analytics;

public final class AnalyticsLimit {

    public static final int DEFAULT = 25;

    private AnalyticsLimit() {
    }

    public static int resolve(Integer limit) {
        return limit == null || limit < 1 ? DEFAULT : limit;
    }
}
