package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

public class RecurringIncome extends AbstractRecursiveTransaction {

    public RecurringIncome(double amount, LocalDate date, String userId, RecurringState recurringState,
                           TransactionCategory transactionCategory) {

        super(amount, date, userId, recurringState,transactionCategory );
    }

    public void setCategory(TransactionCategory category) {

        if (category instanceof ExpenseCategory) {
            throw new IllegalArgumentException("Expected to have an income category for income");
        }
        super.setCategory(category);
    }
}
