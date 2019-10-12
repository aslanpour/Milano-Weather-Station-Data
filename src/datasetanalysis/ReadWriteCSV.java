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
        ArrayList dataset = readCSV("src/files/new.csv", true);
        pickAnItemList(dataset, 4);//get temprature
        
    }
    
    public static double[] pickAnItemList(ArrayList dataList, int index){
        double[] itemList = new double[dataList.size()];
        
        for(int i = 0; i < dataList.size();i++){
            ArrayList<Double> row = (ArrayList<Double>)dataList.get(i);
            itemList[i] = row.get(index);
        }
        
        return itemList;
    }
    
    public static void preprocess_mi_meteo_2001(String file) throws IOException{
        ArrayList dataList = new ArrayList<>();
        BufferedReader csvReader;
        try {
            csvReader = new BufferedReader(new FileReader(file));
            try {
                String line;
                
                while ( (line = csvReader.readLine()) != null ) {
                    String[] items = line.split(",");
                    
                    //Modify the read data
                    double[] newRow = new double[5];
                    double year =0, month =0, day =0, hour = 0, temprature = 0;
                    
                    String[] dateSplitted = items[1].split("/");
                    //E.g.: 11/28/2013 1:00

                    month = Double.valueOf(dateSplitted[0]);
                    day = Double.valueOf(dateSplitted[1]);
                    
                    year = Double.valueOf(dateSplitted[2].substring(0, 4));
                    hour = Double.valueOf(dateSplitted[2].substring(4, 6).trim());
                            
                    temprature = Double.valueOf(items[2]);
                    newRow[0] = year;
                    newRow[1] = month;
                    newRow [2] = day;
                    newRow [3] = hour;
                    newRow [4] = temprature;
                    
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
        FileWriter csvWriter = new FileWriter("src/files/new.csv");
        csvWriter.append("Year");
        csvWriter.append(",");
        csvWriter.append("Month");
        csvWriter.append(",");
        csvWriter.append("Day");
        csvWriter.append(",");
        csvWriter.append("Hour");
        csvWriter.append(",");
        csvWriter.append("Temperature");
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
}
