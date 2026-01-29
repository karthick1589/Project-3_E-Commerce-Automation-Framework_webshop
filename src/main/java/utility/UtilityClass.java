package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UtilityClass {

	public static String filePath = System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";

	public static Object[][] getTestData(String sheetName) {

		File file = new File(filePath);
		Object[][] data = null;

		if (!file.exists() || file.length() == 0) {
			System.out.println("Excel file not found or empty. Returning empty data.");
			return new Object[0][0];
		}

		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				return new Object[0][0];
			}

			int rowCount = sheet.getLastRowNum();
			int colCount = sheet.getRow(0).getLastCellNum();

			data = new Object[rowCount][colCount];

			for (int i = 0; i < rowCount; i++) {
				Row row = sheet.getRow(i + 1);
				for (int j = 0; j < colCount; j++) {
					data[i][j] = getCellValue(row, j);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading Excel file. Check path: " + filePath);
		}
		return data;
	}

	public static String[] getRandomUser(String sheetName) {
		String[] userData = new String[2];
		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = WorkbookFactory.create(fis)) {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new RuntimeException("Sheet '" + sheetName + "'not found.");
			}

			int rowCount = sheet.getLastRowNum();
			if (rowCount < 1) {
				throw new RuntimeException("Sheet '" + sheetName + "' is empty! Add some users.");
			}

			Random rand = new Random();
			int randomRowIndex = rand.nextInt(rowCount) + 1;

			Row row = sheet.getRow(randomRowIndex);

			if (row == null) {
				System.out.println("Row " + randomRowIndex + " is empty/null.");
				return null;
			}

			userData[0] = getCellValue(row, 0); // Username
			userData[1] = getCellValue(row, 1); // Password

			System.out.println("Selected Random User from Row " + randomRowIndex + ": " + userData[0]);
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return userData;
	}

	private static String getCellValue(Row row, int cellIndex) {
		if (row == null)
			return "";

		Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(cell);
	}

	public static void writeToExcel(String sheetName, String username, String password) {
		File file = new File(filePath);
		Workbook workbook = null;
		Sheet sheet = null;
		FileInputStream fis = null;

		try {

			if (file.exists() && file.length() > 0) {
				fis = new FileInputStream(file);
				workbook = WorkbookFactory.create(fis);
			} else {
				workbook = new XSSFWorkbook();
			}

			sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				sheet = workbook.createSheet(sheetName);
				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("Username");
				header.createCell(1).setCellValue("Password");
			}

			int lastRow = sheet.getLastRowNum();
			Row row = sheet.createRow(lastRow + 1);
			row.createCell(0).setCellValue(username);
			row.createCell(1).setCellValue(password);

			if (fis != null) {
				fis.close();
			}

			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
			fos.close();
			workbook.close();

			System.out.println("Credentials saved for: " + username);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error writing to Excel: " + e.getMessage());
		}
	}
}