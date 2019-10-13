/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasetanalysis;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 *
 * @author aslanpour
 */
public class ReadWriteCSV {
   
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) throws IOException {
        ArrayList dataset = readCSV("src/files/extracted_mi_meteo_2001.csv", true);
        ArrayList dataList2 = sortByMultipleFields(dataset);
        writeCSV(dataList2, "src/files/www.csv");
//        double [] temprature = pickAnItemList(dataset, 4);//get temprature
//        descriptiveStatistics(temprature);
//        writeCSV(temprature, "src/files/new2.csv");
//        preprocess_mi_meteo_dataset("src/files/mi_meteo_2001.csv", 
//                                    "extracted_mi_meteo_2001", 
//                                    "MM/DD/YYYY HH:MM");
    }
    
    public static double[] pickAnItemList(ArrayList dataList, int index){
        double[] itemList = new double[dataList.size()];
        
        for(int i = 0; i < dataList.size();i++){
            ArrayList<Double> row = (ArrayList<Double>)dataList.get(i);
            itemList[i] = row.get(index);
        }
        
        return itemList;
    }
    
    public static int mode(int a[]) {
    int maxValue = 0, maxCount = 0;

    for (int i = 0; i < a.length; ++i) {
        int count = 0;
        for (int j = 0; j < a.length; ++j) {
            if (a[j] == a[i]) ++count;
        }
        if (count > maxCount) {
            maxCount = count;
            maxValue = a[i];
        }
    }

    return maxValue;
    }
    
    public static double mode(double a[]) {
        double maxValue = 0, maxCount = 0;

        for (int i = 0; i < a.length; ++i) {
            int count = 0;
            for (int j = 0; j < a.length; ++j) {
                if (a[j] == a[i]) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = a[i];
            }
        }
        return maxValue;
    }
    
    public static double[] sort(double[] values){
        DescriptiveStatistics descriptiveStat = new DescriptiveStatistics(values);
        return descriptiveStat.getSortedValues();
    }
    public static ArrayList sortByMultipleFields (ArrayList dataList){
        ArrayList sortedDataList = new ArrayList();
        // Add a key field to each row
        // Key must be made by rows: 1, 2, and 3
        for (int i=0; i < dataList.size(); i++){
            ArrayList<Double> row = (ArrayList<Double>)dataList.get(i);
            
            String keyStr;
            keyStr = String.valueOf(row.get(1).intValue()); // month
            if (row.get(2).intValue() < 10) // day
                keyStr += "0" + String.valueOf(row.get(2).intValue());
            else
                keyStr += String.valueOf(row.get(2).intValue());
            
            if(row.get(3).intValue() < 10) // hour
                keyStr += "0" + String.valueOf(row.get(3).intValue());
            else
                keyStr += String.valueOf(row.get(3).intValue());
            
            double key = Double.valueOf(keyStr);
            row.add(0, key);
            sortedDataList.add(row);
        }
        
        //sorting
        for (int i = 0; i < dataList.size(); i++){
            ArrayList<Double> rowI = (ArrayList<Double>)dataList.get(i);
            double keyI = rowI.get(0);
            for (int j = 0; j < dataList.size() ; j ++){
                ArrayList<Double> rowj = (ArrayList<Double>)dataList.get(j);
                double keyJ = rowj.get(0);
                
                if (keyJ > keyI){
                    ArrayList<Double> rowTmp = (ArrayList<Double>)dataList.get(i);
                    sortedDataList.set(i, rowj);
                    sortedDataList.set(j, rowTmp);
                }
            }
        }
        
        return sortedDataList;
    }
    
    public static void descriptiveStatistics(double[] values){
        DescriptiveStatistics descriptiveStat = new DescriptiveStatistics(values);
        double count = values.length;
        double sum = descriptiveStat.getSum();
        double min = descriptiveStat.getMin();
        double max = descriptiveStat.getMax();
        double mean = descriptiveStat.getMean();
        double mode =  mode(values);
        double standardDeviation = descriptiveStat.getStandardDeviation();
        double skewness = descriptiveStat.getSkewness();
        double kurtosis = descriptiveStat.getKurtosis();
        double median = descriptiveStat.getPercentile(50);
        double percentile99 = descriptiveStat.getPercentile(99);
        
        
        
        System.out.println("Count: " + count +
                            "\nSum: " + sum + 
                            "\nMin: " + min + 
                            "\nMax: " + max + 
                            "\nMean: " + mean + 
                            "\nMode: " + mode +
                            "\nStandard Deviation: " + standardDeviation + 
                            "\nSkewness: " + skewness + 
                            "\nKurtosis: " + kurtosis + 
                            "\nMedian: " + median +
                            "\n90th Percentile: " + percentile99);
    }
        
    
    public static void correlation(double[] x, double[] y){
        
    }
    
    public static void preprocess_mi_meteo_dataset(String filePath, String newFileName, String dateFormat) throws IOException{
        ArrayList dataList = new ArrayList<>();
        BufferedReader csvReader;
        try {
            csvReader = new BufferedReader(new FileReader(filePath));
            try {
                String line;
                // Line e.g. 2002,11/14/2013 19:00,84
                while ( (line = csvReader.readLine()) != null ) {
                    String[] items = line.split(",");
                    
                    //[0] The first item, e.g. items[0] is useless.
                    
                    //[1] Modify the date and time
                    double[] newRow = new double[5];
                    double year =0, month =0, day =0, hour = 0, targetParameter = 0;
                    
                    String[] dateSplitted = items[1].split("/");
                    
                    if(dateFormat == "MM/DD/YYYY HH:MM"){//Temperature
                        // E.g.: 11/28/2013 1:00
                        month = Double.valueOf(dateSplitted[0]);
                        day = Double.valueOf(dateSplitted[1]);
                    
                        year = Double.valueOf(dateSplitted[2].substring(0, 4));
                        
                        String[] st = dateSplitted[2].split(" ");
                        String[] st1 = st[1].split(":");
                        hour = Double.valueOf(st1[0]);
                    }else if (dateFormat == "YYYY/MM/DD HH:MM"){// Humidity
                        //E.g. 2013/11/14 19:00
                        year = Double.valueOf(dateSplitted[0]);
                        month = Double.valueOf(dateSplitted[1]);
                        
                        day = Double.valueOf(dateSplitted[2].substring(0, 2).trim());
                        
                        //remove day from the text and split into two parts (hour and minute).
                        String tmpSplitter = dateSplitted[2].substring(2, dateSplitted[2].length());
                        String tmp[] = tmpSplitter.split(":");
                        hour = Double.valueOf(tmp[0].trim());
                        hour = hour;
                    }
                    
                    //[2]  
                    targetParameter = Double.valueOf(items[2]);
                    
                    newRow[0] = year;
                    newRow[1] = month;
                    newRow [2] = day;
                    newRow [3] = hour;
                    newRow [4] = targetParameter;
                    
                    dataList.add(newRow);
                } 
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace(); 
        }
        
        
        //Write
        FileWriter csvWriter = new FileWriter("src/files/" + newFileName+ ".csv");
        csvWriter.append("Year");
        csvWriter.append(",");
        csvWriter.append("Month");
        csvWriter.append(",");
        csvWriter.append("Day");
        csvWriter.append(",");
        csvWriter.append("Hour");
        csvWriter.append(",");
        if(dateFormat == "MM/DD/YYYY HH:MM")
            csvWriter.append("Temperature");
        else if (dateFormat == "YYYY/MM/DD HH:MM")
            csvWriter.append("Humidity");
        csvWriter.append("\n");
        
        for (int i = 0; i < dataList.size(); i++) {
           double[] row = (double[])dataList.get(i);
           csvWriter.append(row[0] + ",");
           csvWriter.append(row[1] + ",");
           csvWriter.append(row[2] + ",");
           csvWriter.append(row[3] + ",");
           csvWriter.append(row[4] + "\n");
        }
        
        csvWriter.flush();
        csvWriter.close();
    }
    
    public static ArrayList readCSV(String file, boolean labeled){
        ArrayList dataList = new ArrayList<>();
        BufferedReader csvReader;
        try {
            csvReader = new BufferedReader(new FileReader(file));
            try {
                String line;
                if (labeled)
                    csvReader.readLine();
                while ( (line = csvReader.readLine()) != null ) {
                    String[] rowStr = line.split(",");
                    
                    ArrayList<Double> row = new ArrayList<Double>();
                    for (int i =0; i < rowStr.length;i++){
                        row.add(Double.valueOf(rowStr[i]));
                    }
                    
                    dataList.add(row);
                } 
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace(); 
        }
        
        return dataList;

    }
    
    public static void writeCSV(double[] data, String fileName) throws IOException{
        FileWriter csvWriter = new FileWriter(fileName);
                
        for (int i = 0; i < data.length; i++) {
           csvWriter.append(data[i] + "\n");
        }
        
        csvWriter.flush();
        csvWriter.close();
    }
    
    public static void writeCSV(ArrayList dataList, String fileName) throws IOException{
        FileWriter csvWriter = new FileWriter(fileName);
                
        for (int i = 0; i < dataList.size(); i++) {
            ArrayList<Double> row = (ArrayList<Double>)dataList.get(i);
            
           for (int j = 0; j< row.size(); j++){
               if (j <row.size() - 1)
                csvWriter.append(row.get(j) + ",");
               else
                   csvWriter.append(String.valueOf(row.get(j)));
           }
           csvWriter.append("\n");
        }
        
        csvWriter.flush();
        csvWriter.close();
    }
    
    public static void writeCSV(double[] data, String fileName, String lable) throws IOException{
        FileWriter csvWriter = new FileWriter(fileName);
        csvWriter.append(lable + "\n");
        
        for (int i = 0; i < data.length; i++) {
           csvWriter.append(data[i] + "\n");
        }
        
        csvWriter.flush();
        csvWriter.close();
    }
}
