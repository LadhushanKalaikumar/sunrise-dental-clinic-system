package com.mycompany.sunrisedentalclinic.dao;

import com.mycompany.sunrisedentalclinic.database.DatabaseConnection;
import com.mycompany.sunrisedentalclinic.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User authenticate(String username, String password)
            throws SQLException {

        String sql = "SELECT user_id, username, password, full_name, role "
                   + "FROM users "
                   + "WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new User(
                            resultSet.getInt("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("full_name"),
                            resultSet.getString("role")
                    );
                }
            }
        }

        return null;
    }
}
