package lk.ac.iit.finance.app.model;

import java.io.Serializable;

public class AuthenticatedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String username;

    public AuthenticatedUser(String userId, String username) {

        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }
}
