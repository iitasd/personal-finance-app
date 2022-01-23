package lk.ac.iit.finance.app.model;

/**
 * The budget usage model
 */
public class BudgetUsage {

    private double budgetedAmount;
    private double currentUsageAmount;
    private String categoryId;
    private String userId;

    public BudgetUsage() {
    }

    public BudgetUsage(double budgetedAmount, double currentUsageAmount, String categoryId, String userId) {
        this.budgetedAmount = budgetedAmount;
        this.currentUsageAmount = currentUsageAmount;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public double getBudgetedAmount() {
        return budgetedAmount;
    }

    public void setBudgetedAmount(double budgetedAmount) {
        this.budgetedAmount = budgetedAmount;
    }

    public double getCurrentUsageAmount() {
        return currentUsageAmount;
    }

    public void setCurrentUsageAmount(double currentUsageAmount) {
        this.currentUsageAmount = currentUsageAmount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getUsagePercentage() {
        return currentUsageAmount * 100 / budgetedAmount;
    }
}
