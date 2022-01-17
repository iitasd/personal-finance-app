package lk.ac.iit.finance.app.model;

public interface RecursiveTransaction extends Transaction{

    RecurringState getRecurringPeriod();

    void setRecurringState(RecurringState recurringState);
}
