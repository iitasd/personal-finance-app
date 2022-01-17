package lk.ac.iit.finance.app.model;

import java.util.Date;

public interface Transaction {

    String getTransactionId();

    String getUserId();

    void setAmount(double amount);

    double getAmount();

    TransactionCategory getCategory();

    void setCategory(TransactionCategory category);

    void setDate(Date date);

    Date getDate();

    RecurringState getRecurringPeriod();

    void setRecurringState(RecurringState recurringState);

    String getNote();

    void setNote(String note) ;

}
