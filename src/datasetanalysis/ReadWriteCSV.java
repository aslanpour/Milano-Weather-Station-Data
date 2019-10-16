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
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author aslanpour
 */
public class ReadWriteCSV {
   
    /**
     * 
     * @param args 
     */
    
    public static final String FILE_PATH = "src/files/";
    
    public static void main(String[] args) throws IOException {
        // Step 1: preprocessing the data
        
        String fileNameTemperature = "mi_meteo_2001.csv";
        String fileNameHumidity = "mi_meteo_2002.csv";
       
        String dateFormatTemperature = "MM/DD/YYYY HH:MM";
        String dateFormatHumidity = "YYYY/MM/DD HH:MM";
       
//        preprocessing(fileNameTemperature, dateFormatTemperature);
//        preprocessing(fileNameHumidity, dateFormatHumidity);
        
        
        //Step 2: Read both preprocessed datasets
        //fields: 0:Key, 1:Year, 2:Month, 3:Day, 4:Hour, 5:Target Parameter
        String fileName_Temperature = "preprocessed_mi_meteo_2001.csv";
        String fileName_Humidity = "preprocessed_mi_meteo_2002.csv";
        ArrayList temperatureDataSet = readCSV(fileName_Temperature, false);
        ArrayList humidityDataSet = readCSV(fileName_Humidity, false);
        
        // Step 3:  Descriptive Statistics
//        double[] temperature = pickAnItemList(temperatureDataSet, 5);
//        double[] humidity = pickAnItemList(humidityDataSet, 5);
//        descriptiveStatistics(temperature, "Temperature");
//        descriptiveStatistics(humidity, "Humidity");
        
        // Step 4: Inferrential Statistics
//        inferentialStatistics(temperature, humidity);
        
        // Step 5: Regression
//        regression(temperature, humidity);
        
        // Step 6: Machine Learning
//        TestRBF testRBF = new TestRBF(temperatureDataSet, humidityDataSet);
        TestRBF.test("src/files/", "testSet.tset", "src/files/", "RBF_In4_H6_Out1_LR0.5_Iter200.nnet");
        
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
    
    /**
     *
     * @param fileName
     * @throws IOException
     */
    public static void preprocessing (String fileName, String dateFormat) throws IOException{
        // Extract the date, time and target parameter and send to a new CSV file
        
        ArrayList dataset = timeStampFixing_mi_meteo_dataset(FILE_PATH, 
                                    fileName, 
                                    dateFormat);
        //Result: A list of Year, Month, Day, Hour, TargetParameter
        
        // sort
        ArrayList sortedList = sortByMultipleFields(dataset);
        //Result: A sorted list of Key, Year, Month, Day, Hour, TargetParameter
        
        //wrtie to a new file
        writeCSV(sortedList, FILE_PATH, "preprocessed_" + fileName);
    }
    
    public static double[] sort(double[] values){
        DescriptiveStatistics descriptiveStat = new DescriptiveStatistics(values);
        return descriptiveStat.getSortedValues();
    }
    
    public static ArrayList sortByMultipleFields (ArrayList dataList){
        ArrayList dataListWithKey = new ArrayList();
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
            dataListWithKey.add(row);
        }
        
        //bubble sorting
        for (int i = 0; i < dataListWithKey.size(); i++){
            
            for (int j = 1; j < (dataListWithKey.size()- i) ; j ++){
                ArrayList<Double> rowJ = (ArrayList<Double>)dataListWithKey.get(j);
                double keyJ = rowJ.get(0);
                
                ArrayList<Double> rowJMinusOne = (ArrayList<Double>)dataListWithKey.get(j - 1);
                double keyJMinusOne = rowJMinusOne.get(0);
                
                if (keyJMinusOne > keyJ){
                    ArrayList<Double> rowTmp = rowJMinusOne;
                    dataListWithKey.set(j - 1, rowJ);
                    dataListWithKey.set(j, rowTmp);
                }
            }
        }
        
        return dataListWithKey;
    }
    
    public static void descriptiveStatistics(double[] values, String parameterName){
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
        
        
        
        System.out.println(parameterName + "\n" +
                            "Count: " + count +
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
        
    
    public static void inferentialStatistics(double[] x, double[] y){
        Covariance covariance = new Covariance();
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
        
        double covarianceDbl = covariance.covariance(x, y);
        double pearsonsCorrelationDbl = pearsonsCorrelation.correlation(x, y);
        double spearmansCorrelationDbl = spearmansCorrelation.correlation(x, y);
        
        System.out.println("\n" + "Inferential Statistics:" +
                            "\nCovariance: " + covarianceDbl + 
                            "\nPearsonsCorrelation: " + pearsonsCorrelationDbl +
                            "\nSpearmansCorrelation: " + spearmansCorrelationDbl);
    }
    
    public static void regression (double[] x, double[] y){
        SimpleRegression simpleRegression = new SimpleRegression();
        for (int i =0; i < x.length; i++){
            simpleRegression.addData(x[i], y[i]);
        }
        
        double yIntercept = simpleRegression.getIntercept();
        double slope = simpleRegression.getSlope();
        double rSquare = simpleRegression.getRSquare();
        System.out.println("Regression: \n" + 
                            "Y-Intercep: " + yIntercept + 
                            "\nSlope: " + slope + 
                            "\nRSquare: " + rSquare);
    }
    
    public static void machineLearning (ArrayList dataSetTemperature, ArrayList dataSetHumidity){
       // Merge the temperature and humidity files
       
    }
    public static ArrayList timeStampFixing_mi_meteo_dataset(String filePath, 
                                                        String fileName, 
                                                        String dateFormat) throws IOException{
        ArrayList dataList = new ArrayList<>();
        BufferedReader csvReader;
        try {
            csvReader = new BufferedReader(new FileReader(filePath + fileName));
            try {
                String line;
                // Line e.g. 2002,11/14/2013 19:00,84
                while ( (line = csvReader.readLine()) != null ) {
                    String[] items = line.split(",");
                    
                    //[0] The first item, e.g. items[0] is useless.
                    
                    //[1] Modify the date and time
                    ArrayList<Double> newRow = new ArrayList<Double>();
                    
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
                    
                    newRow.add(year);
                    newRow.add(month);
                    newRow.add(day);
                    newRow.add(hour);
                    newRow.add(targetParameter);
                    
                    dataList.add(newRow);
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
    
    public static ArrayList readCSV(String file, boolean labeled){
        ArrayList dataList = new ArrayList<>();
        BufferedReader csvReader;
        try {
            csvReader = new BufferedReader(new FileReader(FILE_PATH + file));
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
        FileWriter csvWriter = new FileWriter(FILE_PATH + fileName);
                
        for (int i = 0; i < data.length; i++) {
           csvWriter.append(data[i] + "\n");
        }
        
        csvWriter.flush();
        csvWriter.close();
    }
    
    public static void writeCSV(ArrayList dataList, String filePath, String fileName) throws IOException{
        FileWriter csvWriter = new FileWriter(filePath + fileName);
                
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
        FileWriter csvWriter = new FileWriter(FILE_PATH + fileName);
        csvWriter.append(lable + "\n");
        
        for (int i = 0; i < data.length; i++) {
           csvWriter.append(data[i] + "\n");
        }
        
        csvWriter.flush();
        csvWriter.close();
    }
}
