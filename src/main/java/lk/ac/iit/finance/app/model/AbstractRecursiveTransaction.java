package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

/**
 * The Abstract recurring transaction model
 */
public class AbstractRecursiveTransaction extends AbstractTransaction implements RecursiveTransaction {


    private RecurringState recurringState;

    public AbstractRecursiveTransaction(double amount, LocalDate date, String userId, RecurringState recurringState,
                                        TransactionCategory transactionCategory, String note) {
        super(amount, date, userId, transactionCategory, note);
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
