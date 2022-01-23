package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

/**
 * Transaction interface.
 */
public interface Transaction {

    String getTransactionId();

    String getUserId();

    void setAmount(double amount);

    double getAmount();

    TransactionCategory getCategory();

    void setCategory(TransactionCategory category);

    void setDate(LocalDate date);

    LocalDate getDate();

    String getNote();

    void setNote(String note) ;

    void setStatus(TransactionStatus status);

    TransactionStatus getStatus();
}
