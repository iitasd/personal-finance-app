package lk.ac.iit.finance.app.model;

import java.util.Date;

public interface Transaction {

    public String getTransactionId();

    public String getUserId();

    public void setAmount(double amount);

    public double getAmount();

    public TransactionCategory getCategory();

    public void setCategory(TransactionCategory category);

    public void setDate(Date date);

    public Date getDate();

    public RecurringState getRecurringPeriod();

    public void setRecurringState(RecurringState recurringState);
}
