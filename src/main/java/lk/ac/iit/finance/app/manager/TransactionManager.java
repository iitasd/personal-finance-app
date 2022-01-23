package lk.ac.iit.finance.app.manager;

import lk.ac.iit.finance.app.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is used to manage the transaction related operations.
 */
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

    /**
     * Add an income
     *
     * @param amount         Amount
     * @param date           Effective date
     * @param note           Note
     * @param userId         User ID
     * @param category       Category type
     * @param recurringState Recurring state
     * @return
     */
    public Transaction addIncome(double amount, LocalDate date, String note, String userId, IncomeCategory category,
                                 RecurringState recurringState) {

        AbstractTransactionFactory abstractTransactionFactory = TransactionFactoryProducer.getFactory(recurringState);
        if (recurringState.isRecurring()) {
            Transaction recurringIncome = abstractTransactionFactory.getTransaction(false, amount, date, userId,
                    category, note);
            recursiveTransactions.add(recurringIncome);
            processRecursiveTransactions((AbstractRecursiveTransaction) recurringIncome);
            return recurringIncome;
        } else {
            Transaction income = abstractTransactionFactory.getTransaction(false, amount, date, userId, category, note);
            transactions.add(income);
            return income;
        }
    }

    /**
     * Add an expense
     *
     * @param amount         Amount
     * @param date           Effective date
     * @param note           Note
     * @param userId         User ID
     * @param category       Category type
     * @param recurringState Recurring state
     * @return
     */
    public Transaction addExpense(double amount, LocalDate date, String note, String userId, ExpenseCategory category,
                                  RecurringState recurringState) {

        AbstractTransactionFactory abstractTransactionFactory = TransactionFactoryProducer.getFactory(recurringState);

        if (recurringState.isRecurring()) {
            Transaction recurringExpense = abstractTransactionFactory.getTransaction(true, amount, date, userId,
                    category, note);
            recursiveTransactions.add(recurringExpense);
            processRecursiveTransactions((AbstractRecursiveTransaction) recurringExpense);
            return recurringExpense;
        } else {
            Transaction expense = abstractTransactionFactory.getTransaction(true, amount, date, userId,
                    category, note);
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
    private boolean validateBudget(Transaction expense) {
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

    /**
     * Get current month usage of a category
     *
     * @param userId     userId
     * @param categoryId category id
     * @return
     */
    public double getCurrentMonthUsage(String userId, String categoryId) {
        double amount = 0;
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        List<Transaction> userTransactions = new UserTransactionFilter(userId).filterTransactions(transactions);
        for (Transaction transaction : userTransactions) {
            if (transaction.getUserId().equals(userId)
                    && transaction.getCategory() != null
                    && transaction.getCategory().getCategoryId() != null
                    && transaction.getCategory().getCategoryId().equals(categoryId)
                    && firstDayOfMonth.isBefore(transaction.getDate())) {
                amount = amount + transaction.getAmount();
            }
        }
        return amount;
    }

    /**
     * Get current month total income
     *
     * @param userId User ID
     * @return
     */
    public double getCurrentMonthIncome(String userId) {
        double amount = 0;
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        List<Transaction> userTransactions = new UserTransactionFilter(userId).filterTransactions(transactions);
        for (Transaction transaction : userTransactions) {
            if (transaction instanceof Income && transaction.getUserId().equals(userId)
                    && (firstDayOfMonth.isBefore(transaction.getDate()) || firstDayOfMonth.isEqual(transaction.getDate()))) {
                amount = amount + transaction.getAmount();
            }
        }
        return amount;
    }

    /**
     * Get current month total expense
     *
     * @param userId user id
     * @return
     */
    public double getCurrentMonthExpense(String userId) {
        double amount = 0;
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        List<Transaction> userTransactions = new UserTransactionFilter(userId).filterTransactions(transactions);

        for (Transaction transaction : userTransactions) {
            if (transaction instanceof Expense && transaction.getUserId().equals(userId)
                    && (firstDayOfMonth.isBefore(transaction.getDate()) || firstDayOfMonth.isEqual(transaction.getDate()))) {
                amount = amount + transaction.getAmount();
            }
        }
        return amount;
    }

    /**
     * GEt monthly expense to a selected given date.
     *
     * @param userId user id
     * @param date   Date
     * @return
     */
    public double getMonthlyExpenseToDate(String userId, LocalDate date) {
        double amount = 0;
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        List<Transaction> userTransactions = new UserTransactionFilter(userId).filterTransactions(transactions);

        for (Transaction transaction : userTransactions) {
            if (transaction instanceof Expense && transaction.getUserId().equals(userId)
                    && date.isAfter(transaction.getDate())
                    && (firstDayOfMonth.isBefore(transaction.getDate()) || firstDayOfMonth.isEqual(transaction.getDate()))) {
                amount = amount + transaction.getAmount();
            }
        }
        return amount;
    }

    /**
     * GEt monthly income to a selected given date.
     *
     * @param userId user id
     * @param date   Date
     * @return
     */
    public double getMonthlyIncomeToDate(String userId, LocalDate date) {
        double amount = 0;
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        List<Transaction> userTransactions = new UserTransactionFilter(userId).filterTransactions(transactions);

        for (Transaction transaction : userTransactions) {
            if (transaction instanceof Income && transaction.getUserId().equals(userId)
                    && date.isAfter(transaction.getDate())
                    && (firstDayOfMonth.isBefore(transaction.getDate()) || firstDayOfMonth.isEqual(transaction.getDate()))) {
                amount = amount + transaction.getAmount();
            }
        }
        return amount;
    }

    /**
     * Get the current month overall budget usage as a percentage.
     *
     * @param userId user Id.
     * @return
     */
    public double getCurrentMonthBudgetStatus(String userId) {
        double amount = 0;
        double totalBudgetAmount = 0;
        List<ExpenseCategory> allBudgetedCategories = BudgetManager.getInstance().getAllBudgetedCategories(userId);
        for (ExpenseCategory expenseCategory : allBudgetedCategories) {
            totalBudgetAmount = totalBudgetAmount + expenseCategory.getBudget().getMaxSpending();
            amount = amount + getCurrentMonthUsage(userId, expenseCategory.getCategoryId());
        }
        if (totalBudgetAmount == 0) {
            return 0;
        }
        double amountToRound = amount * 100 / totalBudgetAmount;
        return Math.round(amountToRound * 100.0) / 100.0;
    }

    /**
     * Get a transaction
     *
     * @param id Id
     * @return
     */
    public Transaction getTransaction(String id) {

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equalsIgnoreCase(id)) {
                return transaction;
            }
        }
        return null;
    }

    /**
     * GEt a recurring transaction.
     *
     * @param id Id.
     * @return
     */
    public Transaction getRecurringTransaction(String id) {

        for (Transaction recursiveTransaction : recursiveTransactions) {
            if (recursiveTransaction.getTransactionId().equalsIgnoreCase(id)) {
                return recursiveTransaction;
            }
        }
        return null;
    }

    /**
     * Edit a recurring transaction.
     *
     * @param transactionId  Transaction Id
     * @param amount         Amount
     * @param note           Note
     * @param recurringState Reccuring State
     * @return
     */
    public Transaction editRecurringTransaction(String transactionId, double amount, String note,
                                                RecurringState recurringState) {

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

    /**
     * Edit a transaction.
     *
     * @param transactionId Transaction Id
     * @param amount        Amount
     * @param date          Date
     * @param note          Note
     * @return
     */
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

    /**
     * Delte a transaction
     *
     * @param transactionId transaction id
     */
    public void deleteTransaction(String transactionId) {

        Transaction transaction = this.getTransaction(transactionId);
        if (transaction != null) {
            transactions.remove(transaction);
        } else {
            System.out.println("No transaction found with given ID: " + transactionId);
        }
    }

    /**
     * Delte a recurring transaction.
     *
     * @param transactionId
     */
    public void deleteRecurringTransaction(String transactionId) {

        Transaction transaction = this.getRecurringTransaction(transactionId);
        if (transaction != null) {
            recursiveTransactions.remove(transaction);
        } else {
            System.out.println("No transaction found with given ID: " + transactionId);
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Transaction> getTransactions(String userId) {
        return transactions.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public List<Transaction> getRecursiveTransactions() {
        return recursiveTransactions;
    }

    public List<Transaction> getRecursiveTransactions(String userId) {
        return recursiveTransactions.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList());
    }

    /**
     * This is used to excecute the future configured recurring transactions.
     */
    public void executeFutureRecursiveTransaction() {
        LocalDate today = LocalDate.now();
        for (Transaction recursiveTransaction : recursiveTransactions) {
            if (recursiveTransaction instanceof RecursiveTransaction) {
                AbstractTransactionFactory abstractTransactionFactory = TransactionFactoryProducer
                        .getFactory(null);

                RecurringState recurringPeriod = ((RecursiveTransaction) recursiveTransaction).getRecurringPeriod();
                if (recurringPeriod.getNextExecutionDate().isEqual(today)) {
                    // If an income
                    if (recursiveTransaction.getCategory().getCategoryType().equals(CategoryType.INCOME)) {
                        String note = "req_" + recursiveTransaction.getTransactionId();

                        Transaction income = abstractTransactionFactory.getTransaction(false,
                                recursiveTransaction.getAmount(), today, recursiveTransaction.getUserId(),
                                recursiveTransaction.getCategory(), note);

                        income.setCategory(recursiveTransaction.getCategory());
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
                        // if an expense
                        String note = "req_" + recursiveTransaction.getTransactionId();
                        Transaction expense = abstractTransactionFactory.getTransaction(true,
                                recursiveTransaction.getAmount(), today, recursiveTransaction.getUserId(),
                                recursiveTransaction.getCategory(), note);

                        expense.setCategory(recursiveTransaction.getCategory());
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
        AbstractTransactionFactory abstractTransactionFactory = TransactionFactoryProducer
                .getFactory(null);
        if (recurringTransaction.getCategory().getCategoryType().equals(CategoryType.INCOME)) {
            if (recurringTransaction.getRecurringPeriod().getPeriod().equals(RecurringPeriod.DAILY)) {
                // if a daily income
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusDays(1)) {
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    Transaction income = abstractTransactionFactory.getTransaction(false,
                            recurringTransaction.getAmount(), date, recurringTransaction.getUserId(),
                            recurringTransaction.getCategory(), note);
                    income.setCategory(recurringTransaction.getCategory());
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
                //if a weekly income
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusWeeks(1)) {
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    Transaction income = abstractTransactionFactory.getTransaction(false,
                            recurringTransaction.getAmount(), date, recurringTransaction.getUserId(),
                            recurringTransaction.getCategory(), note);
                    income.setCategory(recurringTransaction.getCategory());
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
                // if a monthly income
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusMonths(1)) {
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    Transaction income = abstractTransactionFactory.getTransaction(false,
                            recurringTransaction.getAmount(), date, recurringTransaction.getUserId(),
                            recurringTransaction.getCategory(), note);
                    income.setCategory(recurringTransaction.getCategory());
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
                //if a yearly income
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusYears(1)) {
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    Transaction income = abstractTransactionFactory.getTransaction(false,
                            recurringTransaction.getAmount(), date, recurringTransaction.getUserId(),
                            recurringTransaction.getCategory(), note);
                    income.setCategory(recurringTransaction.getCategory());
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
                //if a daily expense
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusDays(1)) {
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    Transaction expense = abstractTransactionFactory.getTransaction(true,
                            recurringTransaction.getAmount(), date, recurringTransaction.getUserId(),
                            recurringTransaction.getCategory(), note);
                    expense.setCategory(recurringTransaction.getCategory());
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
                //if a weekly expense
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusWeeks(1)) {
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    Transaction expense = abstractTransactionFactory.getTransaction(true,
                            recurringTransaction.getAmount(), date, recurringTransaction.getUserId(),
                            recurringTransaction.getCategory(), note);
                    expense.setCategory(recurringTransaction.getCategory());
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
                //if a monthly expense
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusMonths(1)) {
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    Transaction expense = abstractTransactionFactory.getTransaction(true,
                            recurringTransaction.getAmount(), date, recurringTransaction.getUserId(),
                            recurringTransaction.getCategory(), note);
                    expense.setCategory(recurringTransaction.getCategory());
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
                // if a yearly expense
                int i = 1;
                for (LocalDate date = startDate; date.isBefore(LocalDate.now()); date = date.plusYears(1)) {
                    String note = "req_" + recurringTransaction.getTransactionId() + "_" + i;
                    Transaction expense = abstractTransactionFactory.getTransaction(true,
                            recurringTransaction.getAmount(), date, recurringTransaction.getUserId(),
                            recurringTransaction.getCategory(), note);
                    expense.setCategory(recurringTransaction.getCategory());
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
