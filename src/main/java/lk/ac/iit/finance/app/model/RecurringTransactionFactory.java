package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

public class RecurringTransactionFactory extends AbstractTransactionFactory {
    private RecurringState recurringState;

    public RecurringTransactionFactory(RecurringState recurringState) {
        this.recurringState = recurringState;
    }

    @Override
    public Transaction getTransaction(boolean isExpense, double amount, LocalDate date, String userId,
                                      TransactionCategory category, String note) {
        if (isExpense) {
            return new RecurringExpense(amount, date, userId, recurringState, category, note);
        } else {
            return new RecurringIncome(amount, date, userId, recurringState, category, note);
        }
    }
}
