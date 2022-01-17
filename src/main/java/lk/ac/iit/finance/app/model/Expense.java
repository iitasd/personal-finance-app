package lk.ac.iit.finance.app.model;

import java.util.Date;

public class Expense extends AbstractTransaction {

    public Expense(double amount, Date date, String userId) {

        super(amount, date, userId);
    }

    public void setCategory(TransactionCategory category) {

        if (category instanceof ExpenseCategory) {
            throw new IllegalArgumentException("Expected to have an expense category for expense");
        }
        super.setCategory(category);
    }

}
