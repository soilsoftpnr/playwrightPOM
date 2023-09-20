package automate.compare.methods;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import automate.db.methods.DbConnection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.*;

public class Compare {

	public boolean compareStrings(String str1, String str2) {
		return str1.equals(str2);
	}

	public boolean compareLists(List<Object> list1, List<Object> list2) {
		return list1.equals(list2);
	}

	public boolean compareExcelFiles(String filePath1, String filePath2) throws Exception {
		Workbook workbook1 = WorkbookFactory.create(new FileInputStream(filePath1));
		Workbook workbook2 = WorkbookFactory.create(new FileInputStream(filePath2));

		return workbook1.equals(workbook2);
	}

	public boolean comparePDFFiles(String filePath1, String filePath2) throws Exception {
		PDDocument doc1 = PDDocument.load(new File(filePath1));
		PDDocument doc2 = PDDocument.load(new File(filePath2));
		PDFTextStripper stripper1 = new PDFTextStripper();
		PDFTextStripper stripper2 = new PDFTextStripper();

		String text1 = stripper1.getText(doc1);
		String text2 = stripper2.getText(doc2);

		return text1.equals(text2);
	}

	public boolean compareWordDocuments(String filePath1, String filePath2) throws Exception {
		XWPFDocument doc1 = new XWPFDocument(new FileInputStream(filePath1));
		XWPFDocument doc2 = new XWPFDocument(new FileInputStream(filePath2));

		return doc1.equals(doc2);
	}

	public boolean TableComparison(String host, String uname, String pwd, String dbprovider, String port,
			String dbservice, String table1, String table2) throws SQLException {

		boolean tablesEqual = false;
		Connection connection = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		String dburl = null;
		try {
			// Connect to the database
			try {
				if (dbprovider.contains("postgresql")) {
					dburl = "jdbc:" + dbprovider + "://" + host + ":" + port + "/" + dbservice;
					try {
						connection = DbConnection.connectPostgreDB(dburl, uname, pwd);
					} catch (Exception e) {
						// TODO: handle exception
					}

				} else if (dbprovider.contains("mssql")) {
					dburl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbservice;
					try {
						connection = DbConnection.connectMsSQL(dburl, uname, pwd);
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else if (dbprovider.contains("mysql")) {
					dburl = "jdbc:mysql://localhost:" + port + "/" + dbservice;
					try {
						connection = DbConnection.connectMySQL(dburl, uname, pwd);
					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {
					dburl = "jdbc:" + dbprovider + ":thin:@" + host + ":" + port + ":" + dbservice;
					try {
						Class.forName("oracle.jdbc.driver.OracleDriver");
					} catch (Exception e) {
						// TODO: handle exception
					}
					connection = DriverManager.getConnection(dburl, uname, pwd);
					System.out.println("Connected");
				}

			} catch (SQLException se) {
				System.out.println("Connection failed!");
				se.printStackTrace();

			}

			// Create statements for both tables
			stmt1 = connection.createStatement();
			stmt2 = connection.createStatement();

			// Execute queries to retrieve data from both tables
			resultSet1 = stmt1.executeQuery("SELECT * FROM " + table1);
			resultSet2 = stmt2.executeQuery("SELECT * FROM " + table2);

			// Compare the data from the two result sets
			tablesEqual = compareResultSets(resultSet1, resultSet2);

			if (tablesEqual) {
				tablesEqual = true;
				System.out.println("The tables are equal.");
			} else {
				tablesEqual = false;
				System.out.println("The tables are not equal.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close result sets, statements, and the connection
			try {
				if (resultSet1 != null)
					resultSet1.close();
				if (resultSet2 != null)
					resultSet2.close();
				if (stmt1 != null)
					stmt1.close();
				if (stmt2 != null)
					stmt2.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tablesEqual;
	}

	// Compare two result sets
	private static boolean compareResultSets(ResultSet rs1, ResultSet rs2) throws SQLException {
		while (rs1.next() && rs2.next()) {
			// Compare each column in the current row
			for (int i = 1; i <= rs1.getMetaData().getColumnCount(); i++) {
				Object value1 = rs1.getObject(i);
				Object value2 = rs2.getObject(i);

				if (!value1.equals(value2)) {
					return false; // Data is not equal
				}
			}
		}

		// Check if both result sets have the same number of rows
		return !rs1.next() && !rs2.next();
	}

	public boolean compareImages(String imagePath1, String imagePath2) {

		try {
			// Load the images
			BufferedImage image1 = ImageIO.read(new File(imagePath1));
			BufferedImage image2 = ImageIO.read(new File(imagePath2));

			// Check if images have the same dimensions
			if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
				return true; // Images have different dimensions, consider them different
			}

			// Compare pixel by pixel
			for (int x = 0; x < image1.getWidth(); x++) {
				for (int y = 0; y < image1.getHeight(); y++) {
					int pixel1 = image1.getRGB(x, y);
					int pixel2 = image2.getRGB(x, y);

					// Compare pixel colors
					if (pixel1 != pixel2) {
						return true; // Visual difference detected
					}
				}
			}

			// No differences found
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false; // Error occurred, consider it a difference
		}
	}

}
