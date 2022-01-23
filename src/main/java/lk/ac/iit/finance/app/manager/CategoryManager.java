package lk.ac.iit.finance.app.manager;

import lk.ac.iit.finance.app.model.Budget;
import lk.ac.iit.finance.app.model.ExpenseCategory;
import lk.ac.iit.finance.app.model.IncomeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is used to manager the category related operations.
 */
public class CategoryManager {

    private static CategoryManager categoryManager;
    private List<IncomeCategory> incomeCategoryList;
    private List<ExpenseCategory> expenseCategoryList;

    private CategoryManager() {

        this.incomeCategoryList = new ArrayList<>();
        this.expenseCategoryList = new ArrayList<>();

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

    /**
     * Get income category list
     *
     * @param userId user id
     * @return
     */
    public List<IncomeCategory> getIncomeCategoryList(String userId) {
        return incomeCategoryList.stream().filter(e -> e.getUserId().equals(userId)).collect(Collectors.toList());
    }

    /**
     * Get expense category list
     *
     * @param userId userid
     * @return
     */
    public List<ExpenseCategory> getExpenseCategoryList(String userId) {

        return expenseCategoryList.stream().filter(e -> e.getUserId().equals(userId)).collect(Collectors.toList());
    }

    /**
     * Add an income category
     *
     * @param categoryName category name
     * @param description  description
     * @param userId       userId
     * @param isSystem     isSystem category
     * @return
     */
    public IncomeCategory addIncomeCategory(String categoryName, String description, String userId, boolean isSystem) {

        IncomeCategory incomeCategory = new IncomeCategory(categoryName, userId);
        incomeCategory.setDescription(description);
        incomeCategory.setSystem(isSystem);
        this.incomeCategoryList.add(incomeCategory);
        return incomeCategory;
    }

    /**
     * Add an expense category
     *
     * @param categoryName category name
     * @param description  description
     * @param userId       userId
     * @param isSystem     isSystem category
     * @return
     */
    public ExpenseCategory addExpenseCategory(String categoryName, String description, String userId, boolean isSystem,
                                              Budget budget) {

        ExpenseCategory expenseCategory = new ExpenseCategory(categoryName, userId);
        expenseCategory.setDescription(description);
        expenseCategory.setSystem(isSystem);
        expenseCategory.setBudget(budget);
        this.expenseCategoryList.add(expenseCategory);
        return expenseCategory;
    }

    /**
     * get an expense category
     *
     * @param id category id
     * @return
     */
    public ExpenseCategory getExpenseCategory(String id) {

        for (ExpenseCategory expenseCategory : expenseCategoryList) {
            if (expenseCategory.getCategoryId().equalsIgnoreCase(id)) {
                return expenseCategory;
            }
        }
        return null;
    }

    /**
     * get an income category
     *
     * @param id income id
     * @return
     */
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

    /**
     * Update expense category
     *
     * @param expenseId    expense id
     * @param categoryName category name
     * @param description  description
     * @param isSystem     is a system category
     * @param budget       budget
     * @return
     */
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

    /**
     * Delete a category
     *
     * @param categoryId category id
     * @param userId     user id
     */
    public void deleteCategory(String categoryId, String userId) {

        Optional<IncomeCategory> incomeCategory = incomeCategoryList.stream()
                .filter(c -> c.getCategoryId().equals(categoryId) && c.getUserId().equals(userId)).findFirst();

        if (incomeCategory.isPresent()) {
            incomeCategoryList.remove(incomeCategory.get());
            return;
        }

        Optional<ExpenseCategory> expenseCategory = expenseCategoryList.stream()
                .filter(c -> c.getCategoryId().equals(categoryId) && c.getUserId().equals(userId)).findFirst();

        if (expenseCategory.isPresent()) {
            expenseCategoryList.remove(expenseCategory.get());
        }
    }
}
