package lk.ac.iit.finance.app.model;

import java.time.LocalDate;
import java.util.Date;

public class RecurringIncome extends AbstractRecursiveTransaction {

    public RecurringIncome(double amount, LocalDate date, String userId, RecurringState recurringState) {

        super(amount, date, userId, recurringState);
    }

    public void setCategory(TransactionCategory category) {

        if (category instanceof ExpenseCategory) {
            throw new IllegalArgumentException("Expected to have an income category for income");
        }
        super.setCategory(category);
    }
}
