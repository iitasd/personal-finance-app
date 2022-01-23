package lk.ac.iit.finance.app.model;

/**
 * Transaction category mode.
 */
public interface TransactionCategory {
    String getCategoryId();

    void setCategoryId(String categoryId);

    boolean isSystem();

    void setSystem(boolean system);

    String getCategoryName();

    void setCategoryName(String categoryName);

    String getDescription();

    void setDescription(String description);

    CategoryType getCategoryType();
}
