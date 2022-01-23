package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

/**
 * Non recurring transaction factory class.
 */
public class NonRecurringTransactionFactory extends AbstractTransactionFactory {

    @Override
    public Transaction getTransaction(boolean isExpense, double amount, LocalDate date, String userId,
                                      TransactionCategory category, String note) {
        if (isExpense) {
            return new Expense(amount, date, userId, category, note);
        } else {
            return new Income(amount, date, userId, category, note);
        }
    }
}
