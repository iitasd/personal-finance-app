package lk.ac.iit.finance.app.model;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String firstName;

    private String lastName;

    private String username;

    private char[] password;

    private User(UserBuilder userBuilder) {

        this.userId = userBuilder.userId;
        this.firstName = userBuilder.firstName;
        this.lastName = userBuilder.lastName;
        this.username = userBuilder.username;
        this.password = userBuilder.password;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
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

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public char[] getPassword() {

        return password;
    }

    public void setPassword(char[] password) {

        this.password = password;
    }

    public static class UserBuilder {

        private String userId;

        private String firstName;

        private String lastName;

        private String username;

        private char[] password;

        public void userId(String userId) {

            this.userId = userId;
        }

        public UserBuilder firstName(String firstName) {

            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {

            this.lastName = lastName;
            return this;
        }

        public UserBuilder username(String username) {

            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {

            this.password = password.toCharArray();
            return this;
        }

        public User build() {

            User user = new User(this);
            return user;
        }
    }
}
