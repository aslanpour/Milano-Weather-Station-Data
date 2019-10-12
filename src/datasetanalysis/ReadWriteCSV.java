/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasetanalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author aslanpour
 */
public class ReadWriteCSV {
   
    private static String FILE_PATH = "src/files/mi_meteo_2001.csv";

    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        ArrayList dataList = new ArrayList<>();
        BufferedReader csvReader;
        try {
            csvReader = new BufferedReader(new FileReader(FILE_PATH));
            try {
                String line;
                int i=0;
                while ( (line = csvReader.readLine()) != null ) {
                    String[] items = line.split(",");
                    
                    //Modify the read data
                    ArrayList newRow = new ArrayList();
                    double year =0, month =0, day =0, hour = 0, temprature = 0;
                    
                    String[] dateSplitted = items[1].split("/");
                    //E.g.: 11/28/2013 1:00

                    month = Double.valueOf(dateSplitted[0]);
                    day = Double.valueOf(dateSplitted[1]);
                    
                    year = Double.valueOf(dateSplitted[2].substring(0, 4));
                    hour = Double.valueOf(dateSplitted[2].substring(4, 6).trim());
                            
                    temprature = Double.valueOf(items[2]);
                    newRow.add(year);
                    newRow.add(month);
                    newRow.add(day);
                    newRow.add(hour);
                    newRow.add(temprature);
                    i++;
                    if (i==410)
                        i=402;
                    dataList.add(newRow);
                } 
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
