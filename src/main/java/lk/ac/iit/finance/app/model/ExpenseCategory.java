package lk.ac.iit.finance.app.model;

public class ExpenseCategory extends AbstractCategory {

    private Budget budget;

    public ExpenseCategory(String name, String userId) {

        super(name, userId);
    }

    public void setBudget(Budget budget) {

        this.budget = budget;
    }

    public Budget getBudget() {

        return this.budget;
    }
}
