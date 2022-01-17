package lk.ac.iit.finance.app.model;

import java.util.UUID;

public abstract class AbstractCategory implements TransactionCategory {

    private String categoryName;
    private String description;
    private boolean isSystem;
    private String categoryId;
    private String userId;
    private CategoryType categoryType;

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

    public AbstractCategory(String name, String userId, CategoryType categoryType) {

        this.categoryName = name;
        this.userId = userId;
        this.categoryType = categoryType;
        this.categoryId = UUID.randomUUID().toString();
    }

    public boolean isSystem() {

        return isSystem;
    }

    public void setSystem(boolean system) {

        isSystem = system;
    }

    public String getCategoryName() {

        return categoryName;
    }

    public void setCategoryName(String categoryName) {

        this.categoryName = categoryName;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public CategoryType getCategoryType() {

        return categoryType;
    }
}
