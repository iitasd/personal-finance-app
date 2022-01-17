package lk.ac.iit.finance.app.model;

import java.time.LocalDate;
import java.util.Date;

public class AbstractRecursiveTransaction extends AbstractTransaction implements RecursiveTransaction {


    private RecurringState recurringState;

    public AbstractRecursiveTransaction(double amount, LocalDate date, String userId, RecurringState recurringState) {
        super(amount, date, userId);
        this.recurringState = recurringState;
    }

    @Override
    public RecurringState getRecurringPeriod() {

        return recurringState;
    }

    @Override
    public void setRecurringState(RecurringState recurringState) {

        this.recurringState = recurringState;
    }
}
