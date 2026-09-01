package com.mycompany.sunrisedentalclinic.controller;

import com.mycompany.sunrisedentalclinic.dao.UserDAO;
import com.mycompany.sunrisedentalclinic.model.User;
import java.sql.SQLException;

public class LoginController {

    private final UserDAO userDAO;

    public LoginController() {
        userDAO = new UserDAO();
    }

    public User login(String username, String password)
            throws SQLException {

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        return userDAO.authenticate(
                username.trim(),
                password
        );
    }
}
