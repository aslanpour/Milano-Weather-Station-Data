/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datasetanalysis.filemanipulating;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Mohammad Sadegh Aslanpour 
 */
public class ReadASheet {

    /**
     * Read a Sheet from an special workbook
     * @param sheetName
     * @param filePath
     * @return 
     */
    public static ArrayList readASheet(String sheetName, String filePath) throws InvalidFormatException{
        ArrayList dataList =  new ArrayList();
        FileInputStream fis;
        Workbook workbook;
        try {
            fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);
            // or --->  workbook = new HSSFWorkbook(fis);
            
            Sheet sheet = workbook.getSheet(sheetName);

            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                ArrayList data = new ArrayList();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    data.add(cell.getNumericCellValue());
                }
                dataList.add(data);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}


