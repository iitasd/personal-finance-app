package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

public class Income extends AbstractTransaction {

    public Income(double amount, LocalDate date, String userId, TransactionCategory transactionCategory, String note) {

        super(amount, date, userId, transactionCategory, note);
    }

    public void setCategory(TransactionCategory category) {

        if (category instanceof ExpenseCategory) {
            throw new IllegalArgumentException("Expected to have an income category for income");
        }
        super.setCategory(category);
    }
}
