package lk.ac.iit.finance.app.model;

public enum RecurringPeriod {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly");

    private String value;

    RecurringPeriod(String text) {
        this.value = text;
    }

    public String getValue() {
        return this.value;
    }

    public static RecurringPeriod fromString(String value) {
        for (RecurringPeriod b : RecurringPeriod.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        return null;
    }
}
