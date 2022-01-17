package lk.ac.iit.finance.app.model;

import java.util.Date;
import java.util.UUID;

public class AbstractTransaction implements Transaction {

    private Double amount;
    private Date date;
    private TransactionCategory category;
    private String note;
    private String transactionId;
    private String userId;
    private RecurringState recurringState;

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

    public AbstractTransaction(double amount, Date date, String userId) {

        this.amount = amount;
        this.date = date;
        this.transactionId = getTransactionId();
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
    public void setDate(Date date) {

        this.date = date;
    }

    @Override
    public Date getDate() {

        return this.date;
    }

    @Override
    public RecurringState getRecurringPeriod() {

        return this.recurringState;
    }

    @Override
    public void setRecurringState(RecurringState recurringState) {

        this.recurringState = recurringState;
    }
}
