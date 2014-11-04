package com.academysmart.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.academysmart.exception.IncorrectEmailException;
import com.academysmart.exception.ServletException;
import com.academysmart.model.Employee;

public class EmployeeRepositorySingleton {

	private static EmployeeRepositorySingleton instance;
	private static List<Employee> employees = new ArrayList<Employee>();
	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/xe";
	static final String USER = "Yulia";
	static final String PASS = "12345";

	public static EmployeeRepositorySingleton getRepository() {
		// TODO implement method that returns repository instance
		if (instance == null) {
			instance = new EmployeeRepositorySingleton();

		}
		return instance;
	}

	public static void getDataBase() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Employee newEmployee = null;
		Connection connect = null;
		Statement statement = null;
		ResultSet choiseResult = null;
		try {
			connect = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = (Statement) connect.createStatement();
			choiseResult = statement
					.executeQuery("SELECT * FROM STUFF ORDER BY ID");
			while (choiseResult.next()) {
				newEmployee = new Employee();
				newEmployee.setId(choiseResult.getString("ID"));
				newEmployee.setName(choiseResult.getString("NAME"));
				newEmployee.setSurname(choiseResult.getString("SURNAME"));
				newEmployee.setEmail(choiseResult.getString("EMAIL"));
				employees.add(newEmployee);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void addEmployee(String fname, String lname, String email)
			throws IncorrectEmailException, ServletException, SQLException {
		// TODO implement method that adds an employee to repository
		// This method should check that email is not used by other employees
		int id = 1;

		if (fname.equals("") || lname.equals("") || email.equals("")) {
			throw new ServletException("Fill in all the fields");
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		Statement statement = null;
		ResultSet choiseResult = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = (Statement) conn.createStatement();
			choiseResult = statement.executeQuery("SELECT ID FROM STUFF");
			while (choiseResult.next()) {
				id = 1 + Integer.parseInt(choiseResult.getString("ID"));
			}
			stmt = conn.prepareStatement("insert into STUFF "
					+ "values(?, ?, ?, ?)");
			stmt.setInt(1, id);
			stmt.setString(2, fname);
			stmt.setString(3, lname);
			stmt.setString(4, email);
			stmt.executeUpdate();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		Employee employee = new Employee();
		employee.setId(String.valueOf(id));
		employee.setName(fname);
		employee.setSurname(lname);
		employee.setEmail(email);
		for (Employee i : employees) {
			if (i.getEmail().equals(employee.getEmail())) {
				throw new IncorrectEmailException("This email already exists");
			}
		}
		employees.add(employee);

	}

	public List<Employee> getAllEmployees() {
		return employees;
	}

}
