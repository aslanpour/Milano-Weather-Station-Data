/*
 * This project analyzes the Milano Weather Station Data. 
 * This data contains the information about the temperature and relative humidity gathered 
 * during around 2 months in 2013 in Milano, Lambrate street.
 * 1) We 1) analyze the distribution of the data (temperature and relative humidity) 
 * in terms of its centrality and shape, 
 * 2) try to find a relationship between temperature and relative humidity using regression, 
 * and 3) propose a prediction method for this purpose based on artificial neural networks.
 */
package datasetanalysis;

import static datasetanalysis.DatasetPreProcessing.preprocessing;
import static datasetanalysis.ReadWriteCSV.pickAnItemList;
import static datasetanalysis.ReadWriteCSV.readCSV;
import java.io.IOException;
import java.util.ArrayList;

/**
 *This class is to call and run methods from others to perform analysis on datasets.
 * @author aslanpour
 */
public class Main {
    
    public static final String FILE_PATH = "src/files/";
    
    /**
     * A scenario in which the datasets of temperature and relative humidity are prepared and analyzed.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // Step 1: preprocessing the datasets
        
        String fileNameTemperature = "mi_meteo_2001.csv";
        String fileNameHumidity = "mi_meteo_2002.csv";
       
        String dateFormatTemperature = "MM/DD/YYYY HH:MM";
        String dateFormatHumidity = "YYYY/MM/DD HH:MM";
       
//        preprocessing(FILE_PATH, fileNameTemperature, dateFormatTemperature);
//        preprocessing(FILE_PATH, fileNameHumidity, dateFormatHumidity);
        
        
        //Step 2: Read both preprocessed datasets
        //fields: 0:Key, 1:Year, 2:Month, 3:Day, 4:Hour, 5:Target Parameter
        String fileName_Temperature = "preprocessed_mi_meteo_2001.csv";
        String fileName_Humidity = "preprocessed_mi_meteo_2002.csv";
        ArrayList temperatureDataSet = readCSV(FILE_PATH, fileName_Temperature, false);
        ArrayList humidityDataSet = readCSV(FILE_PATH, fileName_Humidity, false);
        
        // Step 3:  Perform Descriptive Statistics
//        double[] temperature = pickAnItemList(temperatureDataSet, 5);//picks the 5th items which is temperature
//        double[] humidity = pickAnItemList(humidityDataSet, 5);//picks the 5th items which is relative humidity
//        DescriptiveStatistics.analyze(temperature, "Temperature");
//        DescriptiveStatistics.analyze(humidity, "Humidity");
        
        // Step 4: Perform Inferential Statistics
//        InferentialStatistics.analyze(temperature, humidity);
        
        // Step 5: Regression Analysis
//        regression(temperature, humidity);
        
        // Step 6: Machine Learning
        MachineLearning.neuralNetworkPreparation(temperatureDataSet, humidityDataSet);
        MachineLearning.predict(FILE_PATH, "testSet.tset", FILE_PATH, "RBF_In4_H6_Out1_LR0.5_Iter200.nnet");
        
    }
}
