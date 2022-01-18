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
            boolean isBudgetOverUsed = validateBudget(expense);
            transactions.add(expense);
            if (isBudgetOverUsed) {
                expense.setStatus(TransactionStatus.BUDGET_OVER_USED);
            }
            return expense;
        }
    }

    /**
     * Validate the budget is overused. return true, if overused.
     *
     * @param expense expense.
     * @return true if overused.
     */
    private boolean validateBudget(Expense expense) {
        ExpenseCategory budget = BudgetManager.getInstance().getBudget(expense.getCategory().getCategoryId());
        if (budget == null || budget.getBudget() == null) {
            return false;
        }

        double maxSpending = budget.getBudget().getMaxSpending();
        double currentMonthUsage = getCurrentMonthUsage(expense.getUserId(), expense.getCategory().getCategoryId());
        if (maxSpending <= currentMonthUsage) {
            return true;
        }
        return false;
    }

    public double getCurrentMonthUsage(String userId, String categoryId) {
        double amount = 0;
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        for (Transaction transaction : transactions) {
            if (transaction.getUserId().equals(userId)
                    && transaction.getCategory().getCategoryId().equals(categoryId)
                    && firstDayOfMonth.isBefore(transaction.getDate())) {
                amount = amount + transaction.getAmount();
            }
        }
        return amount;
    }

    public Transaction getTransaction(String id) {

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equalsIgnoreCase(id)) {
                return transaction;
            }
        }
        return null;
    }

    public Transaction getRecurringTransaction(String id) {

        for (Transaction recursiveTransaction : recursiveTransactions) {
            if (recursiveTransaction.getTransactionId().equalsIgnoreCase(id)) {
                return recursiveTransaction;
            }
        }
        return null;
    }

    public Transaction editRecurringTransaction(String transactionId, double amount, String note, RecurringState recurringState) {

        Transaction transaction = this.getRecurringTransaction(transactionId);
        if (transaction != null) {
            transaction.setAmount(amount);
            transaction.setNote(note);
            ((RecursiveTransaction) transaction).setRecurringState(recurringState);
            return transaction;
        } else {
            System.out.println("No recurring transaction found with given ID: " + transactionId);
            return null;
        }
    }

    public Transaction editTransaction(String transactionId, double amount, LocalDate date, String note) {

        Transaction transaction = this.getTransaction(transactionId);
        if (transaction != null) {
            transaction.setAmount(amount);
            transaction.setDate(date);
            transaction.setNote(note);
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

    public List<Transaction> getRecursiveTransactions() {
        return recursiveTransactions;
    }

    public void executeFutureRecursiveTransaction() {
        LocalDate today = LocalDate.now();
        for (Transaction recursiveTransaction : recursiveTransactions) {
            if (recursiveTransaction instanceof RecursiveTransaction) {
                RecurringState recurringPeriod = ((RecursiveTransaction) recursiveTransaction).getRecurringPeriod();
                if (recurringPeriod.getNextExecutionDate().isEqual(today)) {
                    if (recursiveTransaction.getCategory().getCategoryType().equals(CategoryType.INCOME)) {
                        Income income = new Income(recursiveTransaction.getAmount(), today, recursiveTransaction.getUserId());
                        income.setCategory(recursiveTransaction.getCategory());
                        String note = "req_" + recursiveTransaction.getTransactionId();
                        income.setNote(note);
                        transactions.add(income);
                        int occurrenceCount = recurringPeriod.getOccurrenceCount();
                        if (occurrenceCount != 0) {
                            recurringPeriod.setOccurrenceCount(occurrenceCount - 1);
                            if (recurringPeriod.getPeriod().equals(RecurringPeriod.DAILY)) {
                                recurringPeriod.setNextExecutionDate(today.plusDays(1));
                            } else if (recurringPeriod.getPeriod().equals(RecurringPeriod.WEEKLY)) {
                                recurringPeriod.setNextExecutionDate(today.plusWeeks(1));
                            } else if (recurringPeriod.getPeriod().equals(RecurringPeriod.MONTHLY)) {
                                recurringPeriod.setNextExecutionDate(today.plusMonths(1));
                            } else if (recurringPeriod.getPeriod().equals(RecurringPeriod.YEARLY)) {
                                recurringPeriod.setNextExecutionDate(today.plusYears(1));
                            }
                        }
                    } else if (recursiveTransaction.getCategory().getCategoryType().equals(CategoryType.EXPENSE)) {
                        Expense expense = new Expense(recursiveTransaction.getAmount(), today, recursiveTransaction.getUserId());
                        expense.setCategory(recursiveTransaction.getCategory());
                        String note = "req_" + recursiveTransaction.getTransactionId();
                        expense.setNote(note);
                        transactions.add(expense);
                        int occurrenceCount = recurringPeriod.getOccurrenceCount();
                        if (occurrenceCount != 0) {
                            recurringPeriod.setOccurrenceCount(occurrenceCount - 1);
                            if (recurringPeriod.getPeriod().equals(RecurringPeriod.DAILY)) {
                                recurringPeriod.setNextExecutionDate(today.plusDays(1));
                            } else if (recurringPeriod.getPeriod().equals(RecurringPeriod.WEEKLY)) {
                                recurringPeriod.setNextExecutionDate(today.plusWeeks(1));
                            } else if (recurringPeriod.getPeriod().equals(RecurringPeriod.MONTHLY)) {
                                recurringPeriod.setNextExecutionDate(today.plusMonths(1));
                            } else if (recurringPeriod.getPeriod().equals(RecurringPeriod.YEARLY)) {
                                recurringPeriod.setNextExecutionDate(today.plusYears(1));
                            }
                        }
                    }
                }
            }
        }
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
                    int occurrenceCount = recurringTransaction.getRecurringPeriod().getOccurrenceCount();
                    if (occurrenceCount == 0) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                    recurringTransaction.getRecurringPeriod().setOccurrenceCount(occurrenceCount - 1);
                    recurringTransaction.getRecurringPeriod().setNextExecutionDate(date.plusDays(1));
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.WEEKLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusWeeks(1)) {
                    Income income = new Income(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    income.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    income.setNote(note);
                    transactions.add(income);
                    int occurrenceCount = recurringTransaction.getRecurringPeriod().getOccurrenceCount();
                    if (occurrenceCount == 0) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                    recurringTransaction.getRecurringPeriod().setOccurrenceCount(occurrenceCount - 1);
                    recurringTransaction.getRecurringPeriod().setNextExecutionDate(date.plusWeeks(1));
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.MONTHLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusMonths(1)) {
                    Income income = new Income(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    income.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    income.setNote(note);
                    transactions.add(income);
                    int occurrenceCount = recurringTransaction.getRecurringPeriod().getOccurrenceCount();
                    if (occurrenceCount == 0) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                    recurringTransaction.getRecurringPeriod().setOccurrenceCount(occurrenceCount - 1);
                    recurringTransaction.getRecurringPeriod().setNextExecutionDate(date.plusMonths(1));
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.YEARLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusYears(1)) {
                    Income income = new Income(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    income.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    income.setNote(note);
                    transactions.add(income);
                    int occurrenceCount = recurringTransaction.getRecurringPeriod().getOccurrenceCount();
                    if (occurrenceCount == 0) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                    recurringTransaction.getRecurringPeriod().setOccurrenceCount(occurrenceCount - 1);
                    recurringTransaction.getRecurringPeriod().setNextExecutionDate(date.plusYears(1));
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
                    int occurrenceCount = recurringTransaction.getRecurringPeriod().getOccurrenceCount();
                    if (occurrenceCount == 0) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                    recurringTransaction.getRecurringPeriod().setOccurrenceCount(occurrenceCount - 1);
                    recurringTransaction.getRecurringPeriod().setNextExecutionDate(date.plusDays(1));
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.WEEKLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusWeeks(1)) {
                    Expense expense = new Expense(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    expense.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    expense.setNote(note);
                    transactions.add(expense);
                    int occurrenceCount = recurringTransaction.getRecurringPeriod().getOccurrenceCount();
                    if (occurrenceCount == 0) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                    recurringTransaction.getRecurringPeriod().setOccurrenceCount(occurrenceCount - 1);
                    recurringTransaction.getRecurringPeriod().setNextExecutionDate(date.plusWeeks(1));
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.MONTHLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusMonths(1)) {
                    Expense expense = new Expense(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    expense.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    expense.setNote(note);
                    transactions.add(expense);
                    int occurrenceCount = recurringTransaction.getRecurringPeriod().getOccurrenceCount();
                    if (occurrenceCount == 0) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                    recurringTransaction.getRecurringPeriod().setOccurrenceCount(occurrenceCount - 1);
                    recurringTransaction.getRecurringPeriod().setNextExecutionDate(date.plusMonths(1));
                }
            } else if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.YEARLY)) {
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusYears(1)) {
                    Expense expense = new Expense(recurringTransaction.getAmount(), date, recurringTransaction.getUserId());
                    expense.setCategory(recurringTransaction.getCategory());
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    expense.setNote(note);
                    transactions.add(expense);
                    int occurrenceCount = recurringTransaction.getRecurringPeriod().getOccurrenceCount();
                    if (occurrenceCount == 0) {
                        //No need to further add transactions. Occurrence count is competed.
                        break;
                    }
                    i++;
                    recurringTransaction.getRecurringPeriod().setOccurrenceCount(occurrenceCount - 1);
                    recurringTransaction.getRecurringPeriod().setNextExecutionDate(date.plusYears(1));
                }
            }
        }
    }
}
