package lk.ac.iit.finance.app.manager;

import lk.ac.iit.finance.app.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private final List<Transaction> transactions;
    private final List<Transaction> recursiveTransactions;
    private static TransactionManager transactionManager;

    private TransactionManager() {

        this.transactions = new ArrayList<>();
        this.recursiveTransactions = new ArrayList<>();
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

    public Transaction addIncome(double amount, LocalDate date, String note, String userId, IncomeCategory category,
                                 RecurringState recurringState) {

        if (recurringState.isRecurring()) {
            RecurringIncome recurringIncome = new RecurringIncome(amount, date, userId, recurringState);
            recurringIncome.setCategory(category);
            recurringIncome.setNote(note);
            recursiveTransactions.add(recurringIncome);
            processRecursiveTransactions(recurringIncome);
            return recurringIncome;
        } else {
            Income income = new Income(amount, date, userId);
            income.setCategory(category);
            income.setNote(note);
            transactions.add(income);
            return income;
        }
    }

    public Transaction addExpense(double amount, LocalDate date, String note, String userId, ExpenseCategory category,
                                  RecurringState recurringState) {

        if (recurringState.isRecurring()) {
            RecurringExpense recurringExpense = new RecurringExpense(amount, date, userId, recurringState);
            recurringExpense.setCategory(category);
            recurringExpense.setNote(note);
            recursiveTransactions.add(recurringExpense);
            processRecursiveTransactions(recurringExpense);
            return recurringExpense;
        } else {
            Expense expense = new Expense(amount, date, userId);
            expense.setCategory(category);
            expense.setNote(note);
            transactions.add(expense);
            return expense;
        }
    }

    public Transaction getTransaction(String id) {

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equalsIgnoreCase(id)) {
                return transaction;
            }
        }
        return null;
    }

//    public Transaction editTransaction(String transactionId, double amount, Date date, String note,
//                                       ExpenseCategory category, RecurringState recurringState) {

//        Transaction transaction = this.getTransaction(transactionId);
//        if (transaction != null) {
//            transaction.setAmount(amount);
//            transaction.setDate(date);
//            transaction.setNote(note);
//            transaction.setCategory(category);
//            transaction.setRecurringState(recurringState);
//            return transaction;
//        } else {
//            System.out.println("No transaction found with given ID: " + transactionId);
//            return null;
//        }
//    }

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

    public List<Transaction> getRecursiveTransactions() {
        return recursiveTransactions;
    }

    private void processRecursiveTransactions(AbstractRecursiveTransaction recurringTransaction) {
        LocalDate startDate = recurringTransaction.getDate();
        if (recurringTransaction.getCategory().getCategoryType().equals(CategoryType.INCOME)) {
            if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.DAILY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusDays(1)) {
                    Income income = new Income(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    income.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    income.setNote(note);
                    transactions.add(income);
                    if (i == recurringTransaction.getRecurringPeriod().getOccurrenceCount()) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.WEEKLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusWeeks(1)) {
                    Income income = new Income(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    income.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    income.setNote(note);
                    transactions.add(income);
                    if (i == recurringTransaction.getRecurringPeriod().getOccurrenceCount()) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.MONTHLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusMonths(1)) {
                    Income income = new Income(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    income.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    income.setNote(note);
                    transactions.add(income);
                    if (i == recurringTransaction.getRecurringPeriod().getOccurrenceCount()) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.YEARLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusYears(1)) {
                    Income income = new Income(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    income.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    income.setNote(note);
                    transactions.add(income);
                    if (i == recurringTransaction.getRecurringPeriod().getOccurrenceCount()) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                }
            }
        } else if (recurringTransaction.getCategory().getCategoryType().equals(CategoryType.EXPENSE)) {
            if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.DAILY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusDays(1)) {
                    Expense expense = new Expense(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    expense.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    expense.setNote(note);
                    transactions.add(expense);
                    if (i == recurringTransaction.getRecurringPeriod().getOccurrenceCount()) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.WEEKLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusWeeks(1)) {
                    Expense expense = new Expense(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    expense.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    expense.setNote(note);
                    transactions.add(expense);
                    if (i == recurringTransaction.getRecurringPeriod().getOccurrenceCount()) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.MONTHLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusMonths(1)) {
                    Expense expense = new Expense(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    expense.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    expense.setNote(note);
                    transactions.add(expense);
                    if (i == recurringTransaction.getRecurringPeriod().getOccurrenceCount()) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.YEARLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusYears(1)) {
                    Expense expense = new Expense(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    expense.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    expense.setNote(note);
                    transactions.add(expense);
                    if (i == recurringTransaction.getRecurringPeriod().getOccurrenceCount()) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                }
            }
        }

    }
}
