package lk.ac.iit.finance.app.model;

import java.util.UUID;

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
