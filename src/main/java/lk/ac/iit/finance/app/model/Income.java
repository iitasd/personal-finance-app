package lk.ac.iit.finance.app.model;

import java.util.Date;

public class Income extends AbstractTransaction {

    public Income(double amount, Date date, String userId) {

        super(amount, date, userId);
    }

    public void setCategory(TransactionCategory category) {

        if (category instanceof IncomeCategory) {
            throw new IllegalArgumentException("Expected to have an income category for income");
        }
        super.setCategory(category);
    }
}
