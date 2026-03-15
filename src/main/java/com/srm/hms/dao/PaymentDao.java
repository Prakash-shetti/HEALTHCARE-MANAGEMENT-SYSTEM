package com.srm.hms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.srm.hms.model.Payments;
import com.srm.hms.util.JdbcUtils;

public class PaymentDao {
	public int insertPayment(Payments payment) throws ClassNotFoundException {
		String INSERT_PAYMENTS = "INSERT INTO payments"
				+ "  (amount,payment_date, payment_mode, card_number,expire_date,cvv_number,transaction_id,user_id) VALUES "
				+ " (?, ?, ?, ?,?,?,?,?);";
 
		int result = 0;
		try (Connection connection = JdbcUtils.getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PAYMENTS)){
			preparedStatement.setFloat(1,payment.getAmount());
			preparedStatement.setDate(2,JdbcUtils.getSQLDate(payment.getPayment_date()));
			preparedStatement.setString(3, payment.getPayment_mode());
			preparedStatement.setLong(4, payment.getCard_number());
			preparedStatement.setString(5, payment.getExpire_date());
			preparedStatement.setInt(6, payment.getCvv_number());
			preparedStatement.setString(7, payment.getTransaction_id());
			preparedStatement.setInt(8, payment.getUser_id());
 
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			result = preparedStatement.executeUpdate();
 
		} catch (SQLException e) {
			// process sql exception
			JdbcUtils.printSQLException(e);
		}
		return result;
	}
	
	public List<Payments> getAllPayments() throws SQLException {
        List<Payments> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments";
        try (Connection conn =JdbcUtils. getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Payments payment = new Payments(
                    rs.getInt("user_id"),
                    rs.getFloat("amount"),
                    rs.getDate("payment_date").toLocalDate(),
                    rs.getString("payment_mode"),
                    rs.getLong("card_number"),
                    rs.getString("expire_date"),
                    rs.getInt("cvv_number"),
                    rs.getString("transaction_id")
                );
                payments.add(payment);
            }
        }
        return payments;
    }

}
