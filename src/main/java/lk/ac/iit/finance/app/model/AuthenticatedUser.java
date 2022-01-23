package lk.ac.iit.finance.app.model;

import java.io.Serializable;

/**
 * Authentication user model
 */
public class AuthenticatedUser implements Serializable {

    private static final long serialVersionUID = 1747422469571724488L;
    private String userId;

    private String username;

    private String firstName;

    private String lastName;

    public AuthenticatedUser(String userId, String username, String firstName, String lastName) {

        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }
}
