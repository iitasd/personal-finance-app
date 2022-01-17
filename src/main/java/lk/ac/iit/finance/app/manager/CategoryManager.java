package lk.ac.iit.finance.app.manager;

import lk.ac.iit.finance.app.model.Budget;
import lk.ac.iit.finance.app.model.ExpenseCategory;
import lk.ac.iit.finance.app.model.IncomeCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryManager {

    public List<IncomeCategory> incomeCategoryList;
    public List<ExpenseCategory> expenseCategoryList;
    private static CategoryManager categoryManager;

    private CategoryManager() {

        this.incomeCategoryList = new ArrayList<>();
        this.expenseCategoryList = new ArrayList<>();

    }

    public List<IncomeCategory> getIncomeCategoryList(String userId) {
        return incomeCategoryList.stream().filter(e -> e.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public void setIncomeCategoryList(List<IncomeCategory> incomeCategoryList) {

        this.incomeCategoryList = incomeCategoryList;
    }

    public List<ExpenseCategory> getExpenseCategoryList(String userId) {

        return expenseCategoryList.stream().filter(e -> e.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public void setExpenseCategoryList(List<ExpenseCategory> expenseCategoryList) {

        this.expenseCategoryList = expenseCategoryList;
    }

    public static CategoryManager getInstance() {

        if (categoryManager == null) {
            synchronized (CategoryManager.class) {
                if (categoryManager == null) {
                    categoryManager = new CategoryManager();
                }
            }
        }
        return categoryManager;
    }

    public IncomeCategory addIncomeCategory(String categoryName, String description, String userId, boolean isSystem) {

        IncomeCategory incomeCategory = new IncomeCategory(categoryName, userId);
        incomeCategory.setDescription(description);
        incomeCategory.setSystem(isSystem);
        this.incomeCategoryList.add(incomeCategory);
        return incomeCategory;
    }

    public ExpenseCategory addExpenseCategory(String categoryName, String description, String userId,
                                              boolean isSystem, Budget budget) {

        ExpenseCategory expenseCategory = new ExpenseCategory(categoryName, userId);
        expenseCategory.setDescription(description);
        expenseCategory.setSystem(isSystem);
        expenseCategory.setBudget(budget);
        this.expenseCategoryList.add(expenseCategory);
        return expenseCategory;
    }

    public ExpenseCategory getExpenseCategory(String id) {

        for (ExpenseCategory expenseCategory : expenseCategoryList) {
            if (expenseCategory.getCategoryId().equalsIgnoreCase(id)) {
                return expenseCategory;
            }
        }
        return null;
    }

    public IncomeCategory getIncomeCategory(String id) {

        for (IncomeCategory incomeCategory : incomeCategoryList) {
            if (incomeCategory.getCategoryId().equalsIgnoreCase(id)) {
                return incomeCategory;
            }
        }
        return null;
    }

    public IncomeCategory updateIncomeCategory(String incomeId, String categoryName, String description,
                                               boolean isSystem) {

        IncomeCategory incomeCategory = this.getIncomeCategory(incomeId);
        if (incomeCategory != null) {
            incomeCategory.setCategoryName(categoryName);
            incomeCategory.setDescription(description);
            incomeCategory.setSystem(isSystem);
            return incomeCategory;
        } else {
            System.out.println("No income category found with given ID");
            return null;
        }

    }

    public ExpenseCategory updateExpenseCategory(String expenseId, String categoryName, String description,
                                                 boolean isSystem, Budget budget) {

        ExpenseCategory expenseCategory = this.getExpenseCategory(expenseId);
        if (expenseCategory != null) {
            expenseCategory.setCategoryName(categoryName);
            expenseCategory.setDescription(description);
            expenseCategory.setSystem(isSystem);
            expenseCategory.setBudget(budget);
            return expenseCategory;
        } else {
            System.out.println("No expense category found with given ID");
            return null;
        }

    }
}
