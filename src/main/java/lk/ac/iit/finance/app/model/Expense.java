package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

public class Expense extends AbstractTransaction {

    public Expense(double amount, LocalDate date, String userId, TransactionCategory transactionCategory) {

        super(amount, date, userId, transactionCategory);
    }

    public void setCategory(TransactionCategory category) {

        if (category instanceof IncomeCategory) {
            throw new IllegalArgumentException("Expected to have an expense category for expense");
        }
        super.setCategory(category);
    }

}
