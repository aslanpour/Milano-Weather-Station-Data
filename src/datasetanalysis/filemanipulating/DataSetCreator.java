/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasetanalysis.filemanipulating;

import java.util.ArrayList;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author Aslanpour
 */
public class DataSetCreator {
    public static final String FILE_PATH = "src/others/";
    
    public static void main(String[] args) {
        
        int maxRequest = 200;
        String sheetName = "testDS_d7-10min-predictF-1H";
        ArrayList dataList;
        dataList = ReadWriteExcel.readASheet(sheetName, "src/others/SimulationResult.xls");
//        Log.printLine("excel file has read");
        int inputCount = 5; // 1-weekend, 2-day of week, 3-hour of day, 4-minute of hour, and 5-user's request
        int outputCount = 1; // request 10, 20, 30, 40, 50, and "60" minutes future
        double[] inputMinValues = new double[]{0 ,1 , 0, 0, 0}; // for 5 inputs --> {0, 1 ,0, 0, 0};
        double[] inputMaxValues = new double[]{1, 7, 23, 59, maxRequest}; //for 5 inputs-->{1, 1, 7, 23, 59, maxRequest}
//        double[] outputMinValues = new double[] {0, 0, 0, 0, 0, 0};
        double[] outputMinValues = new double[] {0};
//        double[] outputMaxValues = new double[]{maxRequest, maxRequest, maxRequest, maxRequest, maxRequest, maxRequest};
        double[] outputMaxValues = new double[]{maxRequest};
        
        // destination of output file
        String fileName = sheetName;
        String filePath  = "C:/AutoScaleSimFiles/";
        
        createSupervizedDataSetNormalized(dataList
                                        , inputCount, outputCount
                                        , inputMinValues, inputMaxValues
                                        , outputMinValues, outputMaxValues
                                        , filePath, fileName, true);
    }
    /**
     * Unsupervised DataSet
     * @param dataList
     * @param inputs
     * @param filePath
     * @param fileName
     * @param isNeedTextfile 
     */
    public static void writeUnSupervizedDataSet(ArrayList dataList, int inputs, String filePath, String fileName, boolean isNeedTextfile){
        DataSet dataSet = new DataSet(inputs);

        for(int i = 0; i < dataList.size(); i++){
            ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
            double[] row = new double[data.size()];
            for(int j = 0; j < data.size(); j++){
                row[j] = data.get(j);
            }
            
            dataSet.addRow(row);
        }
        dataSet.save(filePath + fileName + ".tset");
        if(isNeedTextfile)
            dataSet.saveAsTxt(filePath + fileName + ".txt", ",");
    }
    
    /**
     * 
     * @param dataList
     * @param inputCount
     * @param outputCount
     * @param filePath
     * @param fileName
     * @param isNeedTextfile 
     */
    public static void writeSupervizedDataSet(ArrayList dataList, int inputCount, int outputCount
                                                        , String filePath, String fileName, boolean isNeedTextfile){
        DataSet dataSet = new DataSet(inputCount, outputCount);

        // Before creation of DataSet, the Excel sheet should be contains inputs and outputs in each row.
        for(int i = 0; i < dataList.size(); i++){
            ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
            
            if(data.size() != (inputCount + outputCount))
//                Log.printLine("error - in excel record size");
            
            double[] input = new double[inputCount];
            double[] output = new double[outputCount];
            
            for(int j = 0; j < data.size(); j++){
                if (j < inputCount)
                    input[j] = data.get(j);
                else
                    output[j - outputCount] = data.get(j);
            }
            
            dataSet.addRow(input, output);
        }
        dataSet.save(filePath + fileName + ".tset");
        if(isNeedTextfile)
            dataSet.saveAsTxt(filePath + fileName + ".txt", ",");
    }
    
    public static void createSupervizedDataSetNormalized(ArrayList dataList, int inputCount, int outputCount
                                                        , double[] inputMinValues, double[] inputMaxValues
                                                        , double[] outputMinValues, double[] outputMaxValues
                                                        , String filePath, String fileName, boolean isNeedTextfile){
        DataSet dataSet = new DataSet(inputCount, outputCount);

        
        for(int i = 0; i < dataList.size(); i++){
            ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
            
            if(data.size() != (inputCount + outputCount))
//                Log.printLine("error - in excel record size");
            
            double[] input = new double[inputCount];
            double[] output = new double[outputCount];
            
            for(int j = 0; j < data.size(); j++){
                if (j < inputCount)
                    input[j] = MLP.normalizedValue(data.get(j), inputMinValues[j], inputMaxValues[j]);
                else
                    output[j - inputCount] = MLP.normalizedValue(data.get(j), outputMinValues[j-inputCount]
                                                                             , outputMaxValues[j-inputCount]);
            }
            
            dataSet.addRow(input, output);
        }
        dataSet.save(filePath + fileName + ".tset");
        Log.printLine("data set has been created in " + filePath + fileName);
        if(isNeedTextfile){
            dataSet.saveAsTxt(filePath + fileName + ".txt", ",");
            Log.printLine("data set (txt) has been created in " + filePath + fileName);
        }
    }
}
