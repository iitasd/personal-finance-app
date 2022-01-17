package lk.ac.iit.finance.app.model;

public class RecurringState {

    private boolean isRecurring;
    private RecurringPeriod period;
    private int occurrenceCount;

    public RecurringState(boolean isRecurring, RecurringPeriod period, int occurrenceCount) {
        this.isRecurring = isRecurring;
        this.period = period;
        this.occurrenceCount = occurrenceCount;
    }
    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public RecurringPeriod getPeriod() {
        return period;
    }

    public void setPeriod(RecurringPeriod period) {
        this.period = period;
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public void setOccurrenceCount(int occurrenceCount) {
        this.occurrenceCount = occurrenceCount;
    }
}
