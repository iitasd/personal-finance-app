package lk.ac.iit.finance.app.model;

public class RecurringState {

    public boolean isRecurring;
    public RecurringPeriod period;
    public int occurrenceCount;

    public RecurringState(boolean isRecurring, RecurringPeriod period, int occurrenceCount) {
        this.isRecurring = isRecurring;
        this.period = period;
        this.occurrenceCount = occurrenceCount;
    }
}
