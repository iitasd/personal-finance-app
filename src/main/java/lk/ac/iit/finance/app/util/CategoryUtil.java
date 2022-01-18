package lk.ac.iit.finance.app.util;

import lk.ac.iit.finance.app.manager.CategoryManager;

public class CategoryUtil {

    public static void addDefaultCategories(String userId) {
        CategoryManager.getInstance().addIncomeCategory("Salary",
                "Monthly Salary", userId, true);
        CategoryManager.getInstance().addIncomeCategory("Bank Interest",
                "Bank Interest", userId, true);
        CategoryManager.getInstance().addExpenseCategory("Education",
                "Education expenses", userId, true, null);
        CategoryManager.getInstance().addExpenseCategory("Groceries",
                "Groceries expenses", userId, true, null);
        CategoryManager.getInstance().addExpenseCategory("Health",
                "Health expenses", userId, true, null);
        CategoryManager.getInstance().addExpenseCategory("House Rent",
                "House Rent", userId, true, null);
        CategoryManager.getInstance().addExpenseCategory("Fuel",
                "Fuel expenses", userId, true, null);
        CategoryManager.getInstance().addExpenseCategory("Shopping",
                "Shopping expenses", userId, true, null);
    }
}
