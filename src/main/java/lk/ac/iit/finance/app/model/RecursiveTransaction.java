package lk.ac.iit.finance.app.model;

/**
 * Recurring transaction interface.
 */
public interface RecursiveTransaction extends Transaction{

    RecurringState getRecurringPeriod();

    void setRecurringState(RecurringState recurringState);
}
