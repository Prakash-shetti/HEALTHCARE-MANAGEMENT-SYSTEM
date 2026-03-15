package com.srm.hms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.srm.hms.model.Users;
import com.srm.hms.util.JdbcUtils;

public class LoginDao {

    public boolean validate(Users user) throws SQLException {
        String SELECT_USER_SQL = "SELECT * FROM users WHERE user_email = ? AND user_password = ?";

        try (Connection connection = JdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_SQL)) {

            preparedStatement.setString(1, user.getUser_email());
            preparedStatement.setString(2, user.getUser_password());

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a row is returned, user is valid

        }
    }

    public String getRole(Users user) throws SQLException {
        String SELECT_ROLE_SQL = "SELECT user_role FROM users WHERE user_email = ? AND user_password = ?";

        try (Connection connection = JdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROLE_SQL)) {

            preparedStatement.setString(1, user.getUser_email());
            preparedStatement.setString(2, user.getUser_password());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("user_role");
            }

        }
        return null;
    }

    public Users getUserByEmail(String email) throws SQLException {
        String SELECT_USER_BY_EMAIL_SQL = "SELECT * FROM users WHERE user_email = ?";

        Users user = null;
        try (Connection connection = JdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL_SQL)) {

            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String firstName = resultSet.getString("user_first_name");
                String lastName = resultSet.getString("user_last_name");
                String role = resultSet.getString("user_role");
                String userEmail = resultSet.getString("user_email");
                String password = resultSet.getString("user_password");
                String mobile = resultSet.getString("user_mobile_number");

                user = new Users(id, firstName, lastName, role, userEmail, password, mobile);
            }

        }
        return user;
    }
}
