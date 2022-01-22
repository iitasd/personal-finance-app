package lk.ac.iit.finance.app.manager;

import lk.ac.iit.finance.app.model.Budget;
import lk.ac.iit.finance.app.model.BudgetUsage;
import lk.ac.iit.finance.app.model.ExpenseCategory;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager {
    private static BudgetManager budgetManager;

    private BudgetManager() {

    }

    public static BudgetManager getInstance() {

        if (budgetManager == null) {
            synchronized (BudgetManager.class) {
                if (budgetManager == null) {
                    budgetManager = new BudgetManager();
                }
            }
        }
        return budgetManager;
    }

    public ExpenseCategory addBudget(String categoryId, double maxSpending, String userId) {
        ExpenseCategory expenseCategory = CategoryManager.getInstance().getExpenseCategory(categoryId);
        if (!expenseCategory.getUserId().equals(userId)) {
            System.out.println("Invalid user is trying. ");
            return null;
        }
        expenseCategory.setBudget(new Budget(maxSpending));
        return expenseCategory;
    }

    public ExpenseCategory updateBudget(String categoryId, double maxSpending) {
        ExpenseCategory expenseCategory = CategoryManager.getInstance().getExpenseCategory(categoryId);
        expenseCategory.setBudget(new Budget(maxSpending));
        return expenseCategory;
    }

    public ExpenseCategory getBudget(String categoryId) {
        return CategoryManager.getInstance().getExpenseCategory(categoryId);
    }

    public ExpenseCategory deleteBudget(String categoryId) {
        ExpenseCategory expenseCategory = CategoryManager.getInstance().getExpenseCategory(categoryId);
        expenseCategory.setBudget(null);
        return expenseCategory;
    }

    public List<ExpenseCategory> getAllBudgetedCategories(String userId) {
        List<ExpenseCategory> budgetList = new ArrayList<>();
        List<ExpenseCategory> expenseCategoryList = CategoryManager.getInstance().getExpenseCategoryList(userId);
        for (ExpenseCategory expenseCategory : expenseCategoryList) {
            if (expenseCategory.getBudget() != null && expenseCategory.getUserId().equals(userId)) {
                budgetList.add(expenseCategory);
            }
        }
        return budgetList;
    }

    public List<ExpenseCategory> getAllNotBudgetedCategories(String userId) {
        List<ExpenseCategory> budgetList = new ArrayList<>();
        List<ExpenseCategory> expenseCategoryList = CategoryManager.getInstance().getExpenseCategoryList(userId);
        for (ExpenseCategory expenseCategory : expenseCategoryList) {
            if (expenseCategory.getBudget() == null && expenseCategory.getUserId().equals(userId)) {
                budgetList.add(expenseCategory);
            }
        }
        return budgetList;
    }

    public BudgetUsage getBudgetUsage(String categoryId, String userId) {
        ExpenseCategory budget = getBudget(categoryId);
        if (budget != null && budget.getBudget() != null && budget.getUserId().equals(userId)) {
            double currentUsage = TransactionManager.getInstance().getCurrentMonthUsage(userId, categoryId);
            return new BudgetUsage(budget.getBudget().getMaxSpending(), currentUsage, categoryId, userId);
        }
        return null;
    }

    public List<BudgetUsage> listBudgetUsages(String userId) {
        List<BudgetUsage> budgetUsages = new ArrayList<>();
        List<ExpenseCategory> allBudgetedCategories = getAllBudgetedCategories(userId);
        for (ExpenseCategory expenseCategory : allBudgetedCategories) {
            BudgetUsage budgetUsage = getBudgetUsage(expenseCategory.getCategoryId(), userId);
            if(budgetUsage != null) {
                budgetUsages.add(budgetUsage);
            }
        }
        return budgetUsages;
    }
}
