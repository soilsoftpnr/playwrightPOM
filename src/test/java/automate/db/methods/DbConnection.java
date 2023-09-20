package automate.db.methods;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;

import java.util.Map;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import automate.bastest.methods.BaseTest;

public class DbConnection extends BaseTest {

	static // public static Properties prop;
	Connection connection = null;
	static Statement stmt = null;
	static ResultSet rs = null;

	public static void connectToDB(String dburl) throws ClassNotFoundException {

		dburl = "jdbc:oracle:thin:@192.168.0.173:1521:mdmdev";
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection connection = DriverManager.getConnection(dburl, "monitor229", "admin");
			System.out.println("Connected");
			connection.close();
		} catch (SQLException se) {
			System.out.println("Connection failed!");
			se.printStackTrace();
		}

	}

	/**
	 * @param navActionParams
	 * @return
	 * @throws Throwable
	 */
	
	public static Connection connectPostgreDB(String url, String username, String password)
			throws ClassNotFoundException {
		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("Connection failed! Check output console");
			e.printStackTrace();
		}
		return connection;
	}

	public static Connection connectMsSQL(String url, String username, String password) throws ClassNotFoundException {
		try {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("Connection failed! Check output console");
			e.printStackTrace();
		}
		return connection;
	}

	public static Connection connectMySQL(String url, String username, String password) throws ClassNotFoundException {
		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("Connection failed! Check output console");
			e.printStackTrace();
		}
		return connection;
	}

	public static Connection connectMangoDBAndExecueQuery(String url, String service, String password)
			throws ClassNotFoundException {
		MongoClientURI uri = new MongoClientURI(url);

		try (MongoClient mongoClient = new MongoClient(uri)) {
			MongoDatabase database = mongoClient.getDatabase(service);
			System.out.println("Connected to MongoDB successfully!");
			// You can interact with the database from here.
			// For example, you can perform CRUD operations on collections.
		} catch (Exception e) {
			System.err.println("Error connecting to MongoDB: " + e.getMessage());
		}
		return connection;
	}

}
