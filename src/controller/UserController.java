package controller;

import dao.UserDAO;
import model.User;
import java.util.List;

public class UserController {

    private final UserDAO userDAO;

    public UserController() {
        userDAO = new UserDAO();
    }

    public boolean addUser(User user) {
        return userDAO.insertUser(user);
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    public boolean insertUser(User user) {
        return userDAO.insertUser(user);
    }
}

