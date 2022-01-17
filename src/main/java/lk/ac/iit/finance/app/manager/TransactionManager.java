package lk.ac.iit.finance.app.manager;

import lk.ac.iit.finance.app.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionManager {

    private final List<Transaction> transactions;
    private static TransactionManager transactionManager;

    private TransactionManager() {

        this.transactions = new ArrayList<>();
    }

    public static TransactionManager getInstance() {

        if (transactionManager == null) {
            synchronized (TransactionManager.class) {
                if (transactionManager == null) {
                    transactionManager = new TransactionManager();
                }
            }
        }
        return transactionManager;
    }

    public Income addIncome(double amount, Date date, String note, String userId, IncomeCategory category,
                            RecurringState recurringState) {

        Income income = new Income(amount, date, userId);
        income.setCategory(category);
        income.setNote(note);
        income.setRecurringState(recurringState);
        transactions.add(income);
        return income;
    }

    public Expense addExpense(double amount, Date date, String note, String userId, ExpenseCategory category,
                              RecurringState recurringState) {

        Expense expense = new Expense(amount, date, userId);
        expense.setCategory(category);
        expense.setNote(note);
        expense.setRecurringState(recurringState);
        transactions.add(expense);
        return expense;
    }

    public Transaction getTransaction(String id) {

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equalsIgnoreCase(id)) {
                return transaction;
            }
        }
        return null;
    }

    public Transaction editTransaction(String transactionId, double amount, Date date, String note,
                                       ExpenseCategory category, RecurringState recurringState) {

        Transaction transaction = this.getTransaction(transactionId);
        if (transaction != null) {
            transaction.setAmount(amount);
            transaction.setDate(date);
            transaction.setNote(note);
            transaction.setCategory(category);
            transaction.setRecurringState(recurringState);
            return transaction;
        } else {
            System.out.println("No transaction found with given ID: " + transactionId);
            return null;
        }
    }

    public void deleteTransaction(String transactionId) {

        Transaction transaction = this.getTransaction(transactionId);
        if (transaction != null) {
            transactions.remove(transaction);
        } else {
            System.out.println("No transaction found with given ID: " + transactionId);
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
