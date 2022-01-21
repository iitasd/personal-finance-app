package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

public class RecurringExpense extends AbstractRecursiveTransaction {

    public RecurringExpense(double amount, LocalDate date, String userId, RecurringState recurringState,
                            TransactionCategory transactionCategory) {

        super(amount, date, userId, recurringState, transactionCategory);
    }

    public void setCategory(TransactionCategory category) {

        if (category instanceof IncomeCategory) {
            throw new IllegalArgumentException("Expected to have an expense category for expense");
        }
        super.setCategory(category);
    }

}
