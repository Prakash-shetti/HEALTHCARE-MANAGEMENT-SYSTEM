package com.srm.hms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.srm.hms.model.Users;
import com.srm.hms.util.JdbcUtils;

public class UserDao {
	public int registerUser(Users user) {
		String INSERT_USERS_SQL = "INSERT INTO users"
				+ "  (user_first_name, user_last_name, user_role,user_email, user_password,user_mobile_number) VALUES "
				+ " (?, ?, ?, ?,?,?);";
 
		int result = 0;
		try (Connection connection = JdbcUtils.getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){
			preparedStatement.setString(1, user.getUser_firstName());
			preparedStatement.setString(2, user.getUser_lastName());
			preparedStatement.setString(3, user.getUser_role());
			preparedStatement.setString(4, user.getUser_email());
			preparedStatement.setString(5, user.getUser_password());
			preparedStatement.setString(6, user.getUser_mobile_number());
 
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			result = preparedStatement.executeUpdate();
 
		} catch (SQLException e) {
			// process sql exception
			JdbcUtils.printSQLException(e);
		}
		return result;
	}
	
	public Users selectUserById(int user_id) {
		String SELECT_BY_ID="select * from users where user_id=?;";
		Users user = null;
		Connection conn  = null;
		PreparedStatement pstmt = null;
		try{
			conn = JdbcUtils.getConnection();
			pstmt = conn.prepareStatement(SELECT_BY_ID);
			pstmt.setInt(1, user_id);
			System.out.println(pstmt);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				int id =  rs.getInt("user_id");
				String first_name = rs.getString("user_first_name");
				String last_name = rs.getString("user_last_name");
				String role = rs.getString("user_role");
				String email = rs.getString("user_email");
				String password = rs.getString("user_password");
				String mobile = rs.getString("user_mobile_number");
				user =  new Users(id,first_name,last_name,role,email,password,mobile);
			}
		}catch(SQLException exception){
			JdbcUtils.printSQLException(exception);
		}finally{
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return user;
	}
	
	public boolean updateUser(Users user) {
        String sql = "UPDATE users SET user_first_name = ?, user_last_name = ?, user_mobile_number = ? , user_password=? WHERE user_id = ?";
        try (Connection connection= JdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUser_firstName());
            preparedStatement.setString(2, user.getUser_lastName());
            preparedStatement.setString(3, user.getUser_mobile_number());
            preparedStatement.setString(4, user.getUser_password());
            preparedStatement.setInt(5, user.getUser_id());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean userExists(String email) throws SQLException {
        // Implement the logic to check if the user exists
        String query = "SELECT COUNT(*) FROM users WHERE user_email = ?";
        try (Connection connection= JdbcUtils.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        	preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
	
	public List<Users> getAllDoctors() {
	    List<Users> doctors = new ArrayList<>();
	    String sql = "SELECT * FROM users WHERE user_role = 'Doctor'";

	    try (Connection connection = JdbcUtils.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        ResultSet rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            Users user = new Users(
	                rs.getInt("user_id"),
	                rs.getString("user_first_name"),
	                rs.getString("user_last_name"),
	                rs.getString("user_role"),
	                rs.getString("user_email"),
	                rs.getString("user_password"),
	                rs.getString("user_mobile_number")
	            );
	            doctors.add(user);
	        }
	    } catch (SQLException e) {
	        JdbcUtils.printSQLException(e);
	    }
	    return doctors;
	}
	
	public List<Users> getAllPatients() {
	    List<Users> patients = new ArrayList<>();
	    String sql = "SELECT * FROM users WHERE user_role = 'Customer'";

	    try (Connection connection = JdbcUtils.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        ResultSet rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            Users user = new Users(
	                rs.getInt("user_id"),
	                rs.getString("user_first_name"),
	                rs.getString("user_last_name"),
	                rs.getString("user_role"),
	                rs.getString("user_email"),
	                rs.getString("user_password"),
	                rs.getString("user_mobile_number")
	            );
	            patients.add(user);
	        }
	    } catch (SQLException e) {
	        JdbcUtils.printSQLException(e);
	    }
	    return patients;
	}
}