/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasetanalysis.filemanipulating;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author Aslanpour
 */
public class DataSetExtractor {
    public static final String DEFAULT_FILE_PATH = "C:/AutoScaleSimFiles/";

    public static void main(String[] args) throws IOException {
        final String filePath = "C:/AutoScaleSimFiles/";
        final String fileName = "NASA_DataSetAndDelayList.tset";
        ArrayList dataList;
        //----------------------------------------------------------------------------
        // Getting a list of time stamps, each one as a user request
        
//        dataList = getTimeStampFromTextFileEPAHTTP(traceFilePath, fileName); // EPA
//        dataList = getTimeStampFromTextFileClarkNetHttpAndNASA(filePath, fileName); // ClarkNet and NASA
        // Write to Excel // max row should not be bigger than 65 565
        String sheetName = fileName;
//        ReadWriteExcel.writeDataList(dataList, sheetName); 
        
        // Write to DataSet and Text // .tset and .txt
        String dataSetName = "DS_" + fileName;
//        ReadWriteDataSet.writeUnSupervizedDataSet(dataList, 4, DEFAULT_FILE_PATH, dataSetName, true); 
        //-----------------------------------------------------------
        
        // Grouping Workload in a Minute

//        ArrayList groupedDataList = timeSeriesGroupingInMinute(dataList, 1, 28);
        // write to Excel
//        ReadWriteExcel.writeDataList(groupedDataList, fileName + "Grouped");
        // write to DataSet and Text
//        ReadWriteDataSet.writeUnSupervizedDataSet(groupedDataList, 4, DEFAULT_FILE_PATH, fileName + "Grouped", true);//tset and txt
        
        //---------------------------------------------------------
        // Create Grouped DataSet in minute with Delay list
//        timeSeriesGroupedAndDelays(dataList, 1, 28, 4, 405);
        
        //-----------------------------------------------------------
        // Cutting a DataSet File
        DataSet dataSet = DataSet.load("C:/AutoScaleSimFiles/NASA_DataSetAndDelayList.tset");
        int fromDay = 15;    int toDay = 21; 
        dataSetName = "NASA";
        cuttingDSFile(dataSetName, dataSet, fromDay, toDay);
    }
    
    /**
     * 
     * 
     * @param fullFilePath
     * @param filePath
     * @param fileName
     * @return
     * @throws IOException 
     */
    public static ArrayList getTimeStampFromTextFileEPAHTTP(String filePath, String fileName) throws IOException{
        //read file
        
        ArrayList dataList = new ArrayList<>();
        ArrayList<Double> record = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath + fileName + ".txt"));
            try {
                String line;
                while ( (line = br.readLine()) != null ) {
                    // printing out each line in the file

                    int firstIndex = line.indexOf('[');
                    int lastIndex = line.indexOf(']');
                    String time = line.substring(firstIndex + 1, lastIndex);
                    
                    record = new ArrayList<Double>();
                    String field = new String();
                    
                    for(char ch:time.toCharArray()){
                        if (ch != ':')
                            field += ch;
                        else{
                            record.add(Double.valueOf(field));
                            field = new String();
                        }
                    }
                    record.add(Double.valueOf(field));
                    
                    dataList.add(record);
//                    for(int cell: record){
//                        System.out.print(cell + " ");
//                    }
                    
//                    System.out.print(time);
//                    System.out.println();
                } 
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        
        System.out.println("Time Stamp List is successfully gotten from Text File");
        return dataList;
    }
    /**
     * 
     * 
     * @param fullFilePath
     * @param filePath
     * @param fileName
     * @return
     * @throws IOException 
     */
    public static ArrayList getTimeStampFromTextFileClarkNetHttpAndNASA(String filePath, String fileName) throws IOException{
        //read file
        
        ArrayList dataList = new ArrayList<>();
        ArrayList<Double> record = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath + fileName + ".txt"));
            try {
                boolean firstLine = true;
                String lastTimeSeriesStr = new String();
                int errorCountInTimeOrder = 0;
                int errorCountInLineWithoutTime = 0;
                int correctLines = 0;
                String line;
                while ( (line = br.readLine()) != null ) {
                    // printing out each line in the file
//                    System.out.println(line);
                    if(!line.contains("[") && !line.contains("]")){
                        errorCountInLineWithoutTime++;
                        System.out.println("error - Line without time");
                        System.out.println("Line is: " + line);
                        continue;
                    }
                    int firstIndex = line.indexOf('[');
                    int lastIndex = line.indexOf(']');
                    String time = line.substring(firstIndex + 1, lastIndex);
                        // time value now = in Clarknet [28/Aug/1995:00:00:34 -0400]
                        // time value now = in NASA [01/Jul/1995:00:00:01 -0400]
                        // time value now = in EPA [29:23:53:25]
                    String timeStr;
                    record = new ArrayList<Double>();
                    String field = new String();
                    
                    field = time.substring(0, 2);
                    timeStr = field;
                    record.add(Double.valueOf(field)); // Day
                    
                    time = time.substring(12, 20);
                    field = new String();
                    for(char ch:time.toCharArray()){
                        if (ch != ':')
                            field += ch;
                        else{
                            record.add(Double.valueOf(field)); // Hour, Minute
                            timeStr += field;
                            field = new String();
                        }
                    }
                    record.add(Double.valueOf(field)); // Second
                    timeStr += field;
                    // if this record order is not true it should not save
                    if(firstLine){
                        lastTimeSeriesStr = timeStr;
                        firstLine = false;
                    }else{
                        if(Double.valueOf(timeStr) < Double.valueOf(lastTimeSeriesStr)){
                            errorCountInTimeOrder++;
                            System.out.println("error in time order");
                            System.out.println("Last time:" + lastTimeSeriesStr);
                            System.out.println("this time: " + timeStr);
                            continue;
                        }
                    }
                    lastTimeSeriesStr = timeStr;
                    
                    dataList.add(record);
                    correctLines++;
                    
//                    for(double cell: record){
//                        System.out.print(cell + "--");
//                    }
//                    System.out.println();
                } 
                System.out.println("total error in Times order are : " + errorCountInTimeOrder);
                System.out.println("total error in line without time are : " + errorCountInLineWithoutTime);
                System.out.println("Total correct lines are : " + correctLines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Time Stamp List has successfully gotten from Text File");
        return dataList;
    }
    
    /**
     * Changes a data list by this cells: day, hour, minute and second
     * to
     * day, hour, minute and requests count
     * @param dataList
     * @return 
     */
    public static ArrayList timeSeriesGroupingInMinute(ArrayList timeStampList, int dayStart, int dayFinish){
        ArrayList groupedDataList = new ArrayList();
        ArrayList<Double> groupedList;
        
        int dataListIndex = 0;
        
        for (int dayCounter = dayStart; dayCounter <= dayFinish; dayCounter++){
            for (int hourCounter = 0; hourCounter <= 23; hourCounter++){
                for(int minuteCounter = 0; minuteCounter <= 59; minuteCounter++){
                    int reqCount = 0;

                    while(dataListIndex < timeStampList.size()){
                        ArrayList<Double> record =(ArrayList<Double>) timeStampList.get(dataListIndex);
//                        int day = record.get(0).intValue();
                        int hour = record.get(1).intValue();
                        int minute = record.get(2).intValue();
//                        int second = record.get(3).intValue();
                        
                        if(hour == hourCounter && minute == minuteCounter){
                           reqCount++;
                           dataListIndex++;
                        }else{
                            break;
                        }
                    }
                    
                    groupedList = new ArrayList<>();
                    groupedList.add(Double.valueOf(dayCounter)); 
                    groupedList.add(Double.valueOf(hourCounter));
                    groupedList.add(Double.valueOf(minuteCounter));
                    groupedList.add(Double.valueOf(reqCount));
                    
                    groupedDataList.add(groupedList);
                }
            }
        }
        System.out.println("Time Series is grouped in minute-grouping");
        return groupedDataList;
    }
    
    /**
     * Changes a data list by this cells: day, hour, minute and second
     * to
     * day, hour, minute and requests count
     * @param dataSet
     * @param dayStart
     * @param dayFinish
     * @return 
     */
    public static ArrayList timeSeriesGroupingInMinute(DataSet dataSet, int dayStart, int dayFinish){
        ArrayList groupedDataList = new ArrayList();
        ArrayList<Double> groupedList;
        
        
        int dataListIndex = 0;
        
        for (int dayCounter = dayStart; dayCounter <= dayFinish; dayCounter++){
            for (int hourCounter = 0; hourCounter <= 23; hourCounter++){
                for(int minuteCounter = 0; minuteCounter <= 59; minuteCounter++){
                    int reqCount = 0;
                    if(dayCounter == 30 && hourCounter == 15 && minuteCounter == 52)
                        dayCounter = dayCounter;
                    while(dataListIndex < dataSet.size()){
                        double[] record = dataSet.getRowAt(dataListIndex).getInput();
//                        int day = ((Double)(record[0])).intValue();
                        int hour = ((Double)(record[1])).intValue();
                        int minute = ((Double)(record[2])).intValue();
//                        int second = ((Double)(record[3])).intValue();
                        
                        if(hour == hourCounter && minute == minuteCounter){
                           reqCount++;
                           dataListIndex++;
                        }else{
                            if(dataListIndex !=0){
//                                double[] tmp = dataSet.getRowAt(dataListIndex).getInput();
                            }
                            break;
                        }
                    }
                    
                    groupedList = new ArrayList<>();
                    groupedList.add(Double.valueOf(dayCounter)); 
                    groupedList.add(Double.valueOf(hourCounter));
                    groupedList.add(Double.valueOf(minuteCounter));
                    groupedList.add(Double.valueOf(reqCount));
                    
                    groupedDataList.add(groupedList);
                }
            }
        }
        System.out.println("Time Series is grouped in minute-grouping");
        return groupedDataList;
    }
    
    /**
     * 
     * 
     * @param fullFilePath
     * @param filePath
     * @param fileName
     * @return
     * @throws IOException 
     */
    public static ArrayList getTraceLines(String filePath, String fileName) throws IOException{
        //read file
        
        ArrayList dataList = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath + fileName + ".txt"));
            try {
                String line;
                while ( (line = br.readLine()) != null ) {
                    // printing out each line in the file
//                    System.out.println(line);
                    dataList.add(line);
                } 
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Lines of DataSet is gotten");
       return dataList;
        
    }
    
    public static void editClarkNetHttp(String filePath, String fileName){
        // read
        
        //update
        
        //write
    }
    /**
     * Changes a data list by this cells: day, hour, minute and second
     * to
     * day, hour, minute and requests count
     * @param dataList
     * @return 
     */
    public static void timeSeriesGroupedAndDelays(ArrayList timeStampList, int dayStart, int dayFinish, int inTimeStamp, int outDelayListSize){
        DataSet dataSet = new DataSet(inTimeStamp, outDelayListSize); // in: day, hour, min, sec, reqCount    out: delay list
        
        ArrayList groupedDataList = new ArrayList();
        ArrayList<Double> groupedList;
        
        int timeStampIndex = 0;
        
        for (int dayCounter = dayStart; dayCounter <= dayFinish; dayCounter++){
            for (int hourCounter = 0; hourCounter <= 23; hourCounter++){
                for(int minuteCounter = 0; minuteCounter <= 59; minuteCounter++){
                    double[] input = new double[inTimeStamp];
                    double[] output = new double[outDelayListSize];
                    for(int i =0; i < output.length; i++){
                        output[i] = -1;
                    }
                    
                    int reqCount = 0;
                    
                    while(timeStampIndex < timeStampList.size()){
                        ArrayList<Double> timeStamp =(ArrayList<Double>) timeStampList.get(timeStampIndex);
                        int day = timeStamp.get(0).intValue();
                        int hour = timeStamp.get(1).intValue();
                        int minute = timeStamp.get(2).intValue();
                        int second = timeStamp.get(3).intValue();
                        
                        if(hour == hourCounter && minute == minuteCounter){
                            output[reqCount] = second;
                           reqCount++;
                           timeStampIndex++;
                        }else{
                            break;
                        }
                    }
                    
                    input[0] = dayCounter;
                    input[1] = hourCounter;
                    input[2] = minuteCounter;
                    input[3] = reqCount;
                    
                    dataSet.addRow(input, output);
                }
            }
        }
        dataSet.save(DEFAULT_FILE_PATH + "DataSetAndDelayList" + ".tset");
        dataSet.saveAsTxt(DEFAULT_FILE_PATH + "DataSetAndDelayList" + ".txt", ",");
        System.out.println("DataSet and Delay are Created and Saved from Time Stamp List");
    }
    
    public static void cuttingDSFile(String dataSetName, DataSet dataSetFile, int fromDay, int toDay){
        DataSet cuttedDataSet = new DataSet(dataSetFile.getInputSize(), dataSetFile.getOutputSize());
            for(DataSetRow dataSetRow : dataSetFile.getRows()){
                if(dataSetRow.getInput()[0] >= fromDay && dataSetRow.getInput()[0] <= toDay){
                    cuttedDataSet.addRow(dataSetRow);
                }
            }
        
        cuttedDataSet.save(DEFAULT_FILE_PATH + dataSetName + "CuttedDataSetAndDelayList_" + String.valueOf(fromDay) 
                                + "_" + String.valueOf(toDay) + ".tset");
        cuttedDataSet.saveAsTxt(DEFAULT_FILE_PATH + dataSetName + "CuttedDataSetAndDelayList_" + String.valueOf(fromDay) 
                                + "_" + String.valueOf(toDay) + ".txt", ",");
        System.out.println("Cutted DataSet and Delay are Created and Saved");
    }
}
