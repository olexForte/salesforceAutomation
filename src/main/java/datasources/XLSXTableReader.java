package datasources;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

/**
 * XLSX files reader
 */
public  class XLSXTableReader {

//    private static XLSXTableReader instance;
//    public static XLSXTableReader getInstance(String filePath){
//        return instance != null ? instance : new XLSXTableReader(filePath);
//    }
//    public static XLSXTableReader clearInstance(String filePath){
//        return instance != null ? instance : new XLSXTableReader(filePath);
//    }

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    public String fileName;

//    public static void main(String[] args) throws IOException {
//        XLSXTableReader tr = new XLSXTableReader(FileManager.TEST_AUTOMATION_RESOURCES + "/Test Cases.xlsx", "QuizAnswers");
//        tr.getCellValueAsString(3,3);
//        tr.close();
//    }


    public XLSXTableReader(String  fileName) {
        this.fileName = fileName;
        InputStream xlsxFileToRead;
        try {
            xlsxFileToRead = new FileInputStream(fileName);
            //Getting the workbook instance for xlsx file
            workbook = new XSSFWorkbook(xlsxFileToRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = workbook.getSheetAt(0);
    }

    public XLSXTableReader(String  fileName, String sheetName) {
        this.fileName = fileName;
        InputStream xlsxFileToRead;
        try {
            xlsxFileToRead = new FileInputStream(fileName);
            //Getting the workbook instance for xlsx file
            workbook = new XSSFWorkbook(xlsxFileToRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Integer index = Integer.parseInt(sheetName);
            sheet = workbook.getSheetAt(index);
        }catch (Exception e) {
            sheet = workbook.getSheet(sheetName);
        }
    }

    public XLSXTableReader(File file, String sheetName) {
        this.fileName = file.getPath();
        InputStream xlsxFileToRead;
        try {
            xlsxFileToRead = new FileInputStream(file);
            //Getting the workbook instance for xlsx file
            workbook = new XSSFWorkbook(xlsxFileToRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Integer index = Integer.parseInt(sheetName);
            sheet = workbook.getSheetAt(index);
        }catch (Exception e) {
            sheet = workbook.getSheet(sheetName);
        }
    }

    /**
     * get number of cells in row
     * @param row
     * @return
     */
    public int getColumnCount(XSSFRow row){
        return (row.getPhysicalNumberOfCells());
    }


    /**
     * Get Cell value as String
     * @param row
     * @param column
     * @return
     */
    public String getCellValueAsString(int row, int column) {
        return getCellValue(sheet.getRow(row).getCell(column));
    }

    /**
     * get value from cell of .xlsx file
     * @param cell  cell with value
     * @return      String value of cell or "" if cell is empty
     */
    public static String getCellValue(Cell cell) {
        String value;
        if(cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue() != "") {
            value = cell.getStringCellValue();
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            value = String.format("%.2f", cell.getNumericCellValue()).trim();
        } else {
            value = "";                    //value for empty cell
        }

        return value.trim();
    }

    /**
     * get value from cell of .xlsx file
     * @param cell  cell with value
     * @return      float value of cell or 0
     */
    public static double getDoubleCellValue(Cell cell) {
        double value;
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            value = cell.getNumericCellValue();
        } else {
            value = 0;                    //value for empty cell
        }
        return value;
    }


    /**
     * get value from first column of sheet
     * @param filename     name of file
     * @return             array of String values of cells
     */
    public String[] getFirstColumnValues(String filename) {

        List<String> cellValues = new ArrayList<>();
        for (Row row : sheet) {
            if(row.getCell(0) == null)
                break;
            String cellValue = row.getCell(0).getStringCellValue();
            if (cellValue != "") {
                cellValues.add(cellValue);
            } else {
                break;
            }
        }
        String[] values = new String[cellValues.size()];
        values = cellValues.toArray(values);

        return values;
    }


    /**
     * get array of values from column
     * @param columnNumber
     * @return
     */
    public List<String> getColumnValues(int columnNumber) {

        List<String> cellValues = new LinkedList<>();
        Iterator<Row> iterator = sheet.rowIterator();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if(row.getCell(columnNumber) == null)
                break;;
            String cellValue = row.getCell(columnNumber).getStringCellValue();
            cellValues.add(cellValue);
        }

        return cellValues;
    }

    /**
     * get number of sheets in excel
     * @return
     */
    public ArrayList<String> getListOfSheets() {
        ArrayList<String> listOfSheets = new ArrayList<String>();

        for(int i =0; i < workbook.getNumberOfSheets(); i++){
            listOfSheets.add(workbook.getSheetName(i));
        }

        return listOfSheets;
    }

    /**
     * get data from column starting with row number
     * @param index
     * @param startRow
     * @return
     */
    public ArrayList<String> getColumnData(int index, int startRow) {

        ArrayList<String> result = new ArrayList<String>();
        int rowNumber = 0;

        Iterator rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            rowNumber++;
            XSSFRow row = (XSSFRow) rowIterator.next();
            if(startRow < rowNumber){
                if(row.getCell(index).getCellTypeEnum() == CellType.NUMERIC){
                    result.add((String.valueOf(row.getCell(index).getNumericCellValue())));
                } else {
                    result.add(row.getCell(index).getStringCellValue());
                }
            }
        }

        return result;
    }

    /**
     * Get table names
     * @return array of string
     */
    public String[] getTableNames() {
        String[] result = new String[workbook.getNumberOfSheets()-1];
        for(int i = 0; i < result.length; i++){
            result[i] = workbook.getSheetName(i);
        }
        return result;
    }

    /**
     * Get row count
     * @return
     */
    public int getRowCount() {
        return sheet.getLastRowNum();
    }

    /**
     * Get columns count for row
     * @param rowNumber
     * @return
     */
    public int getColumnCount(int rowNumber) {
        return sheet.getRow(rowNumber).getLastCellNum();
    }

    /**
     * Get plain cell value
     * @param iRowNum
     * @param iColumnNum
     * @return
     */
    public String getCellValue(int iRowNum, int iColumnNum) {
        String val ="";

        try {
            val = sheet.getRow(iRowNum).getCell(iColumnNum).getRawValue();
        } catch (Exception e){
            //disregard exception
        }
        return val == null? "" : val;
    }

    /**
     * Convert sheet to CSV
     * @param sheet
     */
    public String sheetToCSV(Sheet sheet) {
        String result = "";
        Row row = null;
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            String rowRepresentation = "";
            for (int j = 0; j < row.getLastCellNum(); j++) {
                rowRepresentation = rowRepresentation + ("\"" + row.getCell(j) + "\";");
            }
            result = result + "\n" + (rowRepresentation);
        }
        return result;
    }

    /**
     * Convert all sheets to CSV
     */
    public void XLSXToCSV(String fileName) throws IOException {
        for(int sheetNumber = 0; sheetNumber < workbook.getNumberOfSheets(); sheetNumber++ ){
            FileManager.appendToFile(fileName, sheetToCSV(workbook.getSheetAt(sheetNumber)));
        }
    }

    /**
     * Convert sheet to List
     * @param sheet
     */
    public ArrayList<ArrayList<String>> sheetToList(Sheet sheet) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        Row row = null;
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            ArrayList<String> rowRepresentation = new ArrayList<>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                rowRepresentation.add(getCellValue(row.getCell(j)));
            }
            result.add(rowRepresentation);
        }
        return result;
    }

    /**
     * Convert all sheets to array list
     */
    public ArrayList<ArrayList<String>> XLSXToList(String fileName) throws IOException {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for(int sheetNumber = 0; sheetNumber < workbook.getNumberOfSheets(); sheetNumber++ ){
            result.addAll(sheetToList(workbook.getSheetAt(sheetNumber)));
        }
        return result;
    }

    /**
     * Close workbook
     * @throws IOException possible exception
     */
    public void close() throws IOException {
        workbook.close();
    }

    /**
     * Unmarge all cells and save table
     */
    public void unmergeAllCells() {
        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<String> ofSheetNames = getListOfSheets();

        for(int sheetNumber = 0; sheetNumber < ofSheetNames.size(); sheetNumber++){
            sheet = workbook.getSheet(ofSheetNames.get(sheetNumber));

            int numOfRegions = sheet.getNumMergedRegions();
            for(int i = 0; i < numOfRegions; i++) {
                sheet.removeMergedRegion(0);
            }
        }

        try {
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendValue(String columnValue, String value) {

    }

    /**
     * Create workbook with sheet
     * @return
     */
    public XSSFSheet createXLSXSheet(){
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet();
        return sheet;
    }

    /**
     * Create sheet in workbook
     * @return
     */
    public XSSFSheet createXLSXSheet(XSSFWorkbook workBook){
        XSSFSheet sheet = workBook.createSheet();
        return sheet;
    }

    /**
     * add row to sheet
     * @param rownumber
     * @param sheet
     * @return
     */
    public XSSFRow addRowToSheet(int rownumber, XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(rownumber);
        return row;
    }

    /**
     * Set value to cell
     * @param cellIndex
     * @param row
     * @param value
     */
    public void setValueToCell(int cellIndex, XSSFRow row, String value){
        setValueToCell(cellIndex, row, value, (short)0);
    }

    public void setValueToCell(int cellIndex, int row, String value){
        setValueToCell(cellIndex, sheet.getRow(row) == null ? sheet.createRow(row) : sheet.getRow(row), value, (short)0);
    }

    public void setValueToCell(int cellIndex, XSSFRow row, String value, short color){
        XSSFCell cell=row.createCell(cellIndex);
        try{
            cell.setCellValue(Double.parseDouble(value));
        } catch (NumberFormatException e){
            cell.setCellValue(value);
        }


        if (color != 0) {
            XSSFCellStyle style1 = workbook.createCellStyle();
            style1.setFillBackgroundColor(color);
            style1.setFillForegroundColor(color);
            style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cell.setCellStyle(style1);
        }
    }


    /**
     * write workbook to file
     * @param outFile
     * @param workBook
     */
    void writeWorkbookToFile(File outFile, XSSFWorkbook workBook){
        FileOutputStream outPutStream = null;
        try {
            outPutStream = new FileOutputStream(outFile);
            workBook.write(outPutStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outPutStream != null) {
                try {
                    outPutStream.flush();
                    outPutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveWorkbookToFile(File xlsxfile){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(xlsxfile);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addHashMapAsRow(HashMap<Integer,String> map){
        addHashMapAsRow( map, false);
    }

    public void addHashMapAsRow(HashMap<Integer,String> map, boolean markAsFailure) {
        int rowNumber = getNumberOfRows();
        addHashMapAsRow( map, rowNumber,  markAsFailure);

    }

    public void addHashMapAsRow(HashMap<Integer,String> map, int rowNumber, boolean markAsFailure){
        XSSFRow row= workbook.getSheetAt(workbook.getActiveSheetIndex()).createRow(rowNumber);
        for(HashMap.Entry item : map.entrySet()){
            if(markAsFailure)
                setValueToCell((Integer)item.getKey(), row , (String) item.getValue(), IndexedColors.CORAL.getIndex());
            else
                setValueToCell((Integer)item.getKey(), row , (String) item.getValue());
        }
    }

    public void addFilters() {
        XSSFSheet sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        int numberOfColumns = getNumberOfColumns(sheet);
        int numberOfRows = getNumberOfRows(sheet);
        for (int i = 0; i < numberOfColumns; i++) {
            //Cell firstCell = sheet.getRow(0).getCell(i);
            //Cell lastCell = sheet.getRow(numberOfRows).getCell(i);
            sheet.setAutoFilter(new CellRangeAddress(0, numberOfRows, i, i));
        }
    }

    public boolean isEmptyTable() {
        XSSFSheet sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        if (getNumberOfRows() == 1 && getNumberOfColumns(sheet) == 1 && getNumberOfColumns(sheet, 2) == 1)
            return true;
        else
            return false;
    }

    /**
     * Get number of rows starting with base_row
     * @param sheet
     * @param base_row row starts counting from (default 0)
     * @return
     */
    public int getNumberOfRows(XSSFSheet sheet, int base_row) {
        int curRow = 1;
        while(true){
            if (isCellBlank(sheet,base_row + curRow, 0) && isCellBlank(sheet,base_row + curRow, 1) && (isCellBlank(sheet,base_row + curRow, 2))) break;
            curRow++;
        }
        return curRow;
    }

    public int getNumberOfRows(XSSFSheet sheet){
        return getNumberOfRows(sheet, 0);
    }

    public int getNumberOfRows(){
        return getNumberOfRows(workbook.getSheetAt(workbook.getActiveSheetIndex()), 0);
    }

    public int getNumberOfColumns(XSSFSheet sheet, int base_column) {
        int curCol = 1;
        while(true){
            if (isCellBlank(sheet, 0, base_column + curCol)) break;
            curCol++;
        }
        return curCol;
    }

    public int getNumberOfColumns(XSSFSheet sheet){
        return getNumberOfColumns(sheet, 0);
    }

    public boolean isCellBlank(XSSFSheet sheet, int row, int col) {
        return sheet.getRow(row) == null ||
                sheet.getRow(row).getCell(col) == null ||
                sheet.getRow(row).getCell(col) .toString().equals("");
    }

    public XSSFRow getNewRow() {
        return sheet.getRow(sheet.getLastRowNum()+1);
    }
    public XSSFRow getLastRow() {
        return sheet.getRow(sheet.getLastRowNum());
    }

    public XSSFRow getRow(int row) {
        return sheet.getRow(row);
    }
}
