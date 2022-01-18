package lk.ac.iit.finance.app.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class AbstractTransaction implements Transaction {

    private Double amount;
    private LocalDate date;
    private TransactionCategory category;
    private String note;
    private String transactionId;
    private String userId;
    private TransactionStatus status = TransactionStatus.SUCCESS;

    public void setAmount(Double amount) {

        this.amount = amount;
    }

    public void setTransactionId(String transactionId) {

        this.transactionId = transactionId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public String getNote() {

        return note;
    }

    public void setNote(String note) {

        this.note = note;
    }

    @Override
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public TransactionStatus getStatus() {
        return status;
    }

    public AbstractTransaction(double amount, LocalDate date, String userId) {

        this.amount = amount;
        this.date = date;
        this.userId = userId;
        this.transactionId = UUID.randomUUID().toString();
    }

    @Override
    public String getTransactionId() {

        return this.transactionId;
    }

    @Override
    public String getUserId() {

        return this.userId;
    }

    @Override
    public void setAmount(double amount) {

        this.amount = amount;
    }

    @Override
    public double getAmount() {

        return amount;
    }

    @Override
    public TransactionCategory getCategory() {

        return category;
    }

    @Override
    public void setCategory(TransactionCategory category) {

        this.category = category;
    }

    @Override
    public void setDate(LocalDate date) {

        this.date = date;
    }

    @Override
    public LocalDate getDate() {

        return this.date;
    }
}
