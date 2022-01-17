package lk.ac.iit.finance.app.manager;

import lk.ac.iit.finance.app.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionManager {

    private List<Income> incomeList;
    private List<Expense> expenseList;
    private static TransactionManager transactionManager;

    private TransactionManager() {

        this.incomeList = new ArrayList<>();
        this.expenseList = new ArrayList<>();
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
        return income;
    }

    public Expense addIncome(double amount, Date date, String note, String userId, ExpenseCategory category,
                             RecurringState recurringState) {

        Expense expense = new Expense(amount, date, userId);
        expense.setCategory(category);
        expense.setNote(note);
        expense.setRecurringState(recurringState);
        return expense;
    }

    public Income getIncome(String id) {

        for (Income income : incomeList) {
            if (income.getTransactionId().equalsIgnoreCase(id)) {
                return income;
            }
        }
        return null;
    }

    public Expense getExpense(String id) {

        for (Expense expense : expenseList) {
            if (expense.getTransactionId().equalsIgnoreCase(id)) {
                return expense;
            }
        }
        return null;
    }

    public Income editIncome(String incomeId, double amount, Date date, String note,
                             ExpenseCategory category, RecurringState recurringState) {

        Income income = this.getIncome(incomeId);
        if (income != null) {
            income.setAmount(amount);
            income.setDate(date);
            income.setNote(note);
            income.setCategory(category);
            income.setRecurringState(recurringState);
            return income;
        } else {
            System.out.printf("No income found with given ID");
            return null;
        }
    }

    public Expense editExpense(String expenseId, double amount, Date date, String note,
                               ExpenseCategory category, RecurringState recurringState) {

        Expense expense = this.getExpense(expenseId);
        if (expense != null) {
            expense.setAmount(amount);
            expense.setDate(date);
            expense.setNote(note);
            expense.setCategory(category);
            expense.setRecurringState(recurringState);
            return expense;
        } else {
            System.out.printf("No expense found with given ID");
        }
        return null;
    }
}
