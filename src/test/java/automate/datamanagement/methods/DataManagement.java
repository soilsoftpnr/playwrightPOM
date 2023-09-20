package automate.datamanagement.methods;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import automate.api.methods.ApiMethods;
import automate.bastest.methods.BaseTest;
import automate.db.methods.DbConnection;

public class DataManagement extends BaseTest {

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
	public static String getTestDataFromDB(String host, String dbprovider, String dbservice, String port, String uname,
			String pwd, String QUERY, String columnName, String variableToSave) throws Throwable {

		boolean isMultiDB = false;
		int bugCount = 0;
		int count = 0;
		// String queryCondition=QUERY.split("where")[1].trim();

		String dburl = null;
		String testData = "";

		try {
			if (dbprovider.contains("postgresql")) {
				dburl = "jdbc:" + dbprovider + "://" + host + ":" + port + "/" + dbservice;
				connection = DbConnection.connectPostgreDB(dburl, uname, pwd);
			} else if (dbprovider.contains("mssql")) {
				dburl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbservice;
				connection = DbConnection.connectMsSQL(dburl, uname, pwd);
			} else if (dbprovider.contains("mysql")) {
				dburl = "jdbc:mysql://localhost:" + port + "/" + dbservice;
				connection = DbConnection.connectMySQL(dburl, uname, pwd);
			} else if (dbprovider.contains("mongoDB")) {
				dburl = "mongodb://" + uname + ":" + pwd + "@" + host + ":" + port + "/" + dbservice;

				connection = DbConnection.connectMangoDBAndExecueQuery(dburl, dbservice, pwd);
			} else {
				dburl = "jdbc:" + dbprovider + ":thin:@" + host + ":" + port + ":" + dbservice;
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

	public static boolean saveTestDataToDB(String host, String dbprovider, String dbservice, String port, String uname,
			String pwd, String QUERY, String[] valuesToInsert, String variableToSave) throws Throwable {

		boolean inserted = false;
		int rowsInserted = 0;
		String dburl = null;
		String testData = "";
		PreparedStatement preparedStatement = null;
		try {
			if (dbprovider.contains("postgresql")) {
				dburl = "jdbc:" + dbprovider + "://" + host + ":" + port + "/" + dbservice;
				connection = DbConnection.connectPostgreDB(dburl, uname, pwd);
			} else if (dbprovider.contains("mssql")) {
				dburl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbservice;
				connection = DbConnection.connectMsSQL(dburl, uname, pwd);
			} else if (dbprovider.contains("mysql")) {
				dburl = "jdbc:mysql://localhost:" + port + "/" + dbservice;
				connection = DbConnection.connectMySQL(dburl, uname, pwd);
			} else if (dbprovider.contains("mongoDB")) {
				dburl = "mongodb://" + uname + ":" + pwd + "@" + host + ":" + port + "/" + dbservice;

				connection = DbConnection.connectMangoDBAndExecueQuery(dburl, dbservice, pwd);
			} else {
				dburl = "jdbc:" + dbprovider + ":thin:@" + host + ":" + port + ":" + dbservice;
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
			preparedStatement = connection.prepareStatement(QUERY);

			// example query "INSERT INTO yourtable (column1, column2, column3, column4, ...
			// , columnN) VALUES (?, ?, ?, ?, ... , ?)";
			// Loop through the values and insert them
			for (int i = 0; i < valuesToInsert.length; i++) {

				// examples how to give values
				// preparedStatement.setString(1, "value1");
				// preparedStatement.setString(2, "value2");
				// preparedStatement.setInt(3, 42);

				preparedStatement.setString(1, valuesToInsert[i]);

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

	/**
	 * @param p
	 * @throws IOException example prop.setProperty(getvalue.replace("_", ""),
	 *                     results.toString()); saveProperties(prop);
	 */
	protected static void saveProperties(Properties p) throws IOException {
		String filepath = System.getProperty("user.dir") + "\\resources\\saveData.properties";
		FileOutputStream fr = new FileOutputStream(filepath);
		p.store(fr, "Properties");
		fr.close();
		System.out.println("After saving properties: " + p);
	}

	public static String saveProperties(String variable) throws IOException {
		String filepath = System.getProperty("user.dir") + "\\resources\\testData.properties";
		String savedValue = testData.getProperty(variable);
		return savedValue;
	}
	
	
	
	public static void getTestDatafromAPIResponseJSONfile(String APIMethod,String endPoint,String payload,ArrayList<String> saveDataVariable,String...headers) throws IOException {
	
		
		JsonNode responseNode=null;
		
		if (APIMethod.equalsIgnoreCase("POST")) {
		 responseNode=ApiMethods.POSTAPI(endPoint,payload,headers);	
		}else {
			 responseNode=ApiMethods.GETAPI(endPoint,payload,headers);
		}
		
		
		for (int save = 0; save < saveDataVariable.size(); save++) {
			List<String> results = new ArrayList<String>();
			// List<Object > proceeseddata = new ArrayList<Object >();
			net.minidev.json.JSONArray proceeseddata;
			String sval = saveDataVariable.get(save);
			if (sval.contains(".")) {
				String[] saveSplit = sval.split("\\.");

				String getvalue = saveSplit[saveSplit.length - 1];
				Object document = Configuration.defaultConfiguration().jsonProvider()
						.parse(responseNode.toString());

				// Extract values using JSON path expression with wildcard
				proceeseddata = JsonPath.read(document, saveDataVariable.get(save).toString());

				for (Object res : proceeseddata) {
					results.add(res.toString());
				}
				
				saveData.setProperty(getvalue, results.toString());
				saveProperties(prop);
				results.clear();
			} else {
				
				saveData.setProperty(sval, responseNode.get(sval).toString());
				saveProperties(prop);

			}

		}
		
	}
	
	public static void getTestDatafromXMLfile(String filePath,String xpathExpression,String variableNAme) {
		  try {
	            // Create a DocumentBuilder to parse XML
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document document = builder.parse(filePath);

	            // Create an XPath expression to select the state element
	            XPathFactory xPathFactory = XPathFactory.newInstance();
	            XPath xpath = xPathFactory.newXPath();
	            XPathExpression expr = xpath.compile(xpathExpression);

	            // Evaluate the XPath expression and get the result as a String
	            String result = (String) expr.evaluate(document, XPathConstants.STRING);

	            saveData.setProperty(variableNAme, result);
				saveProperties(prop);
	          
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
      
	}
}
