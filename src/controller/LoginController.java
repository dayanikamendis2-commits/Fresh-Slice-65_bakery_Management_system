package controller;

import dao.UserDAO;
import model.User;

public class LoginController {

    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }
    //Category - Demostrates ENCAPSULATION and TYPE SAFETY
    // returns the authenticated user or null
    public User authenticate(String username, String password) {
        try {
            return userDAO.findByUsernameAndPassword(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
