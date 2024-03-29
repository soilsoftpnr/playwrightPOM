package automate.db.methods;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class DBCurdMethods {
	 // Method for INSERT operation
	public static boolean insertDataIntoDb(String host, String dataBaseProviderName, String dbName, String port, String uname,
			String pwd, String QUERY, String[] valuesToInsert, String variableToSave) throws Throwable {
		Connection connection = null;
		boolean inserted = false;
		int rowsInserted = 0;
		String dburl = null;
		PreparedStatement preparedStatement = null;
		try {
			if (dataBaseProviderName.contains("postgresql")) {
				dburl = "jdbc:" + dataBaseProviderName + "://" + host + ":" + port + "/" + dbName;
				connection = DbConnection.connectPostgreDB(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mssql")) {
				dburl = "jdbc:sqlserver://" + host + ":" + port + ";dataBaseProviderName=" + dbName;
				connection = DbConnection.connectMsSQL(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mysql")) {
				dburl = "jdbc:mysql://localhost:" + port + "/" + dbName;
				connection = DbConnection.connectMySQL(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mongoDB")) {
				dburl = "mongodb://" + uname + ":" + pwd + "@" + host + ":" + port + "/" + dbName;

				connection = (Connection)DbConnection.connectMangoDBAndExecueQuery(dburl, dbName, pwd);
			} else {
				dburl = "jdbc:" + dataBaseProviderName + ":thin:@" + host + ":" + port + ":" + dbName;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(dburl, uname, pwd);
				System.out.println("Connected");
			}

		} catch (SQLException se) {
			System.out.println("Connection failed!");
			se.printStackTrace();

		}
		try {
			// Open a connection
			preparedStatement = ((java.sql.Connection) connection).prepareStatement(QUERY);

			// example query "INSERT INTO yourtable (column1, column2, column3, column4, ...
			// , columnN) VALUES (?, ?, ?, ?, ... , ?)";
			// Loop through the values and insert them
			for (int i = 0; i < valuesToInsert.length; i++) {

				// examples how to give values
				// preparedStatement.setString(1, "value1");
				// preparedStatement.setString(2, "value2");
				// preparedStatement.setInt(3, 42);

				preparedStatement.setString(i, valuesToInsert[i]);

				// Execute the query for each set of values
				rowsInserted = preparedStatement.executeUpdate();

			}
			if (rowsInserted > 0) {
				inserted = true;
				System.out.println("Data inserted successfully for row ");
			} else {
				System.out.println("Data insertion failed for row ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 6. Close resources in a finally block
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return inserted;
	}
	

    // Method for SELECT operation
	public static String readDataFromDb(String host, String dataBaseProviderName, String dbName, String port, String uname,
			String pwd, String QUERY, String columnName, String variableToSave) throws Throwable {

		
		// String queryCondition=QUERY.split("where")[1].trim();
		Connection connection = null;
	     Statement stmt = null;
		
		String dburl = null;
		String testData = "";

		try {
			if (dataBaseProviderName.contains("postgresql")) {
				dburl = "jdbc:" + dataBaseProviderName + "://" + host + ":" + port + "/" + dbName;
				connection = DbConnection.connectPostgreDB(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mssql")) {
				dburl = "jdbc:sqlserver://" + host + ":" + port + ";dataBaseProviderName=" + dbName;
				connection = DbConnection.connectMsSQL(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mysql")) {
				dburl = "jdbc:mysql://localhost:" + port + "/" + dbName;
				connection = DbConnection.connectMySQL(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mongoDB")) {
				dburl = "mongodb://" + uname + ":" + pwd + "@" + host + ":" + port + "/" + dbName;

				connection = DbConnection.connectMangoDBAndExecueQuery(dburl, dbName, pwd);
			} else {
				dburl = "jdbc:" + dataBaseProviderName + ":thin:@" + host + ":" + port + ":" + dbName;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(dburl, uname, pwd);
				System.out.println("Connected");
			}

		} catch (SQLException se) {
			System.out.println("Connection failed!");
			se.printStackTrace();

		}

		// Open a connection
		stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery(QUERY);

		while (resultSet.next()) {
			// Access test data
			testData = resultSet.getString(columnName);
		}

		// Close the connection
		resultSet.close();
		stmt.close();
		connection.close();

		return testData;

	}



    // Method for UPDATE operation
	public static String updateDataInDb(String host, String dataBaseProviderName, String dbName, String port, String uname,
			String pwd, String QUERY, String columnName, String variableToSave) throws Throwable {
		Connection connection = null;
	     Statement stmt = null;
		// String queryCondition=QUERY.split("where")[1].trim();

		String dburl = null;
		String testData = "";

		try {
			if (dataBaseProviderName.contains("postgresql")) {
				dburl = "jdbc:" + dataBaseProviderName + "://" + host + ":" + port + "/" + dbName;
				connection = DbConnection.connectPostgreDB(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mssql")) {
				dburl = "jdbc:sqlserver://" + host + ":" + port + ";dataBaseProviderName=" + dbName;
				connection = DbConnection.connectMsSQL(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mysql")) {
				dburl = "jdbc:mysql://localhost:" + port + "/" + dbName;
				connection = DbConnection.connectMySQL(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mongoDB")) {
				dburl = "mongodb://" + uname + ":" + pwd + "@" + host + ":" + port + "/" + dbName;

				connection = DbConnection.connectMangoDBAndExecueQuery(dburl, dbName, pwd);
			} else {
				dburl = "jdbc:" + dataBaseProviderName + ":thin:@" + host + ":" + port + ":" + dbName;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(dburl, uname, pwd);
				System.out.println("Connected");
			}

		} catch (SQLException se) {
			System.out.println("Connection failed!");
			se.printStackTrace();

		}

		// Open a connection
		stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery(QUERY);

	

		// Close the connection
		resultSet.close();
		stmt.close();
		connection.close();

		return testData;

	}



    // Method for DELETE operation

   public static String deleteDataInDb(String host, String dataBaseProviderName, String dbName, String port, String uname,
			String pwd, String QUERY, String columnName, String variableToSave) throws Throwable {
		Connection connection = null;
	     Statement stmt = null;
		// String queryCondition=QUERY.split("where")[1].trim();

		String dburl = null;
		String testData = "";

		try {
			if (dataBaseProviderName.contains("postgresql")) {
				dburl = "jdbc:" + dataBaseProviderName + "://" + host + ":" + port + "/" + dbName;
				connection = DbConnection.connectPostgreDB(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mssql")) {
				dburl = "jdbc:sqlserver://" + host + ":" + port + ";dataBaseProviderName=" + dbName;
				connection = DbConnection.connectMsSQL(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mysql")) {
				dburl = "jdbc:mysql://localhost:" + port + "/" + dbName;
				connection = DbConnection.connectMySQL(dburl, uname, pwd);
			} else if (dataBaseProviderName.contains("mongoDB")) {
				dburl = "mongodb://" + uname + ":" + pwd + "@" + host + ":" + port + "/" + dbName;

				connection = DbConnection.connectMangoDBAndExecueQuery(dburl, dbName, pwd);
			} else {
				dburl = "jdbc:" + dataBaseProviderName + ":thin:@" + host + ":" + port + ":" + dbName;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(dburl, uname, pwd);
				System.out.println("Connected");
			}

		} catch (SQLException se) {
			System.out.println("Connection failed!");
			se.printStackTrace();

		}

		// Open a connection
		stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery(QUERY);

	

		// Close the connection
		resultSet.close();
		stmt.close();
		connection.close();

		return testData;

	}
}
