package lk.ac.iit.finance.app.model;

/**
 * The income category model
 */
public class IncomeCategory extends AbstractCategory {

    public IncomeCategory(String name, String userId) {

        super(name, userId, CategoryType.INCOME);
    }
}
