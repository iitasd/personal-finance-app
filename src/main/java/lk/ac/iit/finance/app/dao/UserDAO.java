package lk.ac.iit.finance.app.dao;

import lk.ac.iit.finance.app.model.AuthenticatedUser;
import lk.ac.iit.finance.app.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDAO {

    private static final UserDAO INSTANCE = new UserDAO();

    //TODO: remove this with a DB
    private static List<User> userStore = new ArrayList<>();

    private UserDAO() {

    }

    public static UserDAO getInstance() {

        return INSTANCE;
    }

    public void registerUser(User user) {

        user.setUserId(UUID.randomUUID().toString());
        userStore.add(user);
    }

    public Optional<AuthenticatedUser> authenticate(String username, char[] password) {

        return userStore.stream()
                .filter(user -> user.getUsername().equals(username) && Arrays.equals(user.getPassword(), password))
                .findFirst().map(user -> new AuthenticatedUser(user.getUserId(), user.getUsername()));

    }
}
