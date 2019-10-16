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

import java.io.IOException;
import java.util.ArrayList;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.transfer.Sigmoid;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.learning.RBFLearning;

/**
 *This class is to analyze datasets by machine learnings.
 * @author aslanpour
 */
public class MachineLearning {
    
    /**
     * Based on the temperature and relative humidity, create a dataset for training
     * constituting 80% of the entire data and a dataset for testing the neural network performance
     * constituting the rest of the data (20%). Then, examine different kinds of configurations
     * for the neural network (RBF) to find the most accurate.
     * @param temperatureDataList
     * @param humidityDataList
     * @throws IOException 
     */
    public static void neuralNetworkPreparation(ArrayList temperatureDataList, ArrayList humidityDataList) throws IOException{
        //Merge files and remove unnecesssary fields like key and year
        //input is : month [11,12], day [1, 31], hour [0, 23], and temperature [-3.6, 20.4]
        //output is humidity[15, 96]
        double [][] inputTrain = new double[1170][4]; // 1170 equals 80% of the data
        double [] outputTrain = new double[1170];
        
        double [][] inputTest = new double [temperatureDataList.size() - 1170][4];
        double [] outputTest = new double [temperatureDataList.size() - 1170];
        
        DataSet trainSet = new DataSet(4, 1); // 4 inputs and 1 output
        DataSet testSet = new DataSet(4, 1); // 4 inputs and 1 output

                
        for (int i = 0; i < temperatureDataList.size(); i++){
            // Temperature row: 0:Key, 1: Year, 2: Month, 3:Day, 4: Hour, 5: Temperature
            ArrayList<Double> rowTemperature = (ArrayList<Double>)temperatureDataList.get(i);
            //Humidity row: 0:Key, 1: Year, 2: Month, 3:Day, 4: Hour, 5: Humidity
            ArrayList<Double> rowHumidity = (ArrayList<Double>)humidityDataList.get(i);
            
            if (i < 1170){ // fill in train set
                inputTrain[i][0] = normalizedValue(rowTemperature.get(2), 11, 12); // month
                inputTrain[i][1] = normalizedValue(rowTemperature.get(3), 1, 31); // day
                inputTrain[i][2] = normalizedValue(rowTemperature.get(4), 0, 23); //hour
                inputTrain[i][3] = normalizedValue(rowTemperature.get(5), -4, 21); // temperature
                
                outputTrain[i] = normalizedValue(rowHumidity.get(5),0, 100); // humidity
                
                // Add normalized values to train dataset
                
                DataSetRow dataSetRow = new DataSetRow(inputTrain[i], new double[] {outputTrain[i]});
                trainSet.addRow(dataSetRow);
                
                        
            }else{ // fill in test set
                inputTest[i - 1170][0] = normalizedValue(rowTemperature.get(2), 11, 12); // month
                inputTest[i - 1170][1] = normalizedValue(rowTemperature.get(3), 1, 31); // day
                inputTest[i - 1170][2] = normalizedValue(rowTemperature.get(4), 0, 23); //hour
                inputTest[i - 1170][3] = normalizedValue(rowTemperature.get(5), -4, 21); // temperature
                
                outputTest[i - 1170] = normalizedValue(rowHumidity.get(5),0, 100); // humidity
                
                // Add normalized values to train dataset
                DataSetRow dataSetRow = new DataSetRow(inputTest[i - 1170], new double[] {outputTest[i - 1170]});
                testSet.addRow(dataSetRow);
            }
        }
        
        // Save DataSets
        trainSet.save("src/files/trainSet.tset");
        testSet.save("src/files/testSet.tset");
        
        // Configure the Neural Networks
        ArrayList networkList = new ArrayList<Double>();
        int networkNo = 1;
        int failedNetwork = 0; // error = NAN
        
        int inputs = 4;  // C
        int outputs = 1;   // D
        
        int rbfNeuronCountBegin = 2; // E
        int rbfNeuronCountEnd = 10;   // F
        
        double learningRateBegin = 0.1; // G
        double learningRateEnd = 0.6;  // H
        
        int maxIterateStart = 100;  // I
        int maxIterateEnd = 2000; // J
        
        boolean avoidDivideByZeroByOne = true; // k
        
        InputFunction inputFunction = new WeightedSum(); // K      /* For Output Layer */
        TransferFunction transferFunction = new Sigmoid(); // L    /* For Output Layer */
        
        
        //Stress the networks
        for (int rbfNeuronCount = rbfNeuronCountBegin; rbfNeuronCount <= rbfNeuronCountEnd; rbfNeuronCount++){ 
            for(double learningRate = learningRateBegin; learningRate <= learningRateEnd; learningRate += 0.1){ 
                for(int maxIterate = maxIterateStart; maxIterate <= maxIterateEnd; maxIterate+= 100){ 
                    ArrayList<Double> networkInfo = new ArrayList<>();
                    // Create a Network
                    NeuralNetwork neuralNetwork = new RBFNetwork(inputs, rbfNeuronCount, outputs);

                    // Set more efficient functions for output layer
                    for(int i = 0; i < neuralNetwork.getOutputsCount(); i++){
                        neuralNetwork.getLayerAt(neuralNetwork.getLayersCount() - 1)
                                                            .getNeuronAt(i).setInputFunction(inputFunction);
                        neuralNetwork.getLayerAt(neuralNetwork.getLayersCount() - 1)
                                                            .getNeuronAt(i).setTransferFunction(transferFunction);
                    }
                    
                    // Set Learning Parameters
                    RBFLearning learningRule = new RBFLearning();
                    learningRule.setLearningRate(learningRate);
                    learningRule.setMaxError(0.0);
                    learningRule.setMaxIterations(maxIterate);
                    learningRule.setNeuralNetwork(neuralNetwork);
                    neuralNetwork.setLearningRule(learningRule);
                    
                    // Training
//                    RangeRandomizer NquyenWidrowRandomizer = new NguyenWidrowRandomizer(-0.5, 0.5);
                    neuralNetwork.randomizeWeights(-0.5, 0.5);

                    System.out.println("Neural Network is Learning . . .");

                    neuralNetwork.learn(trainSet);

                    System.out.println(" Neural Network Learned");
                                    
                    System.out.println("**************************");
                    System.out.println("Network No. " + networkNo + "-");
                    System.out.println("RBF Neuron Count = " + rbfNeuronCount);
                    System.out.println("Learning Rule = " + neuralNetwork.getLearningRule().toString());
                    System.out.println("     Learning Rate = " + learningRate);
                    System.out.println("     Max Iterate = " + maxIterate);
                    double totalError = ((RBFLearning)neuralNetwork.getLearningRule()).getTotalNetworkError();
                    System.out.println("Total Error (train) =" + totalError);
                    System.out.println("..................................");
                    
                    if(Double.isNaN(totalError)){
                        failedNetwork++;
                        break;
                    }
                    
                    // save network
                    String netPath = "src/files/nn"
                                                 + "/RBF_In" + inputs
                                                 + "_H" + rbfNeuronCount 
                                                 + "_Out" + outputs
                                                 + "_LR" + learningRate 
                                                 + "_Iter" + maxIterate + ".nnet";
                    
                    neuralNetwork.save(netPath);
                     // Test Phase
                     
                    System.out.println(networkNo + " Neural Network (" + networkNo +")" + " is Testing . . .");
                            
                    double sumAE = 0; double sumSE = 0; double sumAPE = 0; double sumPRED = 0;

                    for(DataSetRow dataSetRow: testSet.getRows()){
                        
                        neuralNetwork.setInput(dataSetRow.getInput());
                        // Test
                        neuralNetwork.calculate();

                        //desire output (from test set)
                        double sumDesireOutput = 0; double sumPredictedOutput = 0;
                        double error = 0; double aE = 0; double aPE = 0; double sE = 0;
                        
                        for(double desireOutPut: dataSetRow.getDesiredOutput()){
                            //desire humidity
                            double value = deNormalizedValue(desireOutPut, 0, 100);
                            sumDesireOutput += value;
                        }
                        
                        if(sumDesireOutput == 0 && avoidDivideByZeroByOne)
                            sumDesireOutput = 1;
                        else
                            sumDesireOutput /= outputs;
                        
                        //predicted output
                        for(int i = 0; i < neuralNetwork.getOutputsCount(); i++){
                            double value = deNormalizedValue(neuralNetwork.getOutput()[i], 0, 100);
                            sumPredictedOutput += value;
                        }
                        sumPredictedOutput /= outputs;

                        // calculate error metrics
                        error = sumDesireOutput - sumPredictedOutput;
                        aE = Math.abs(error);  // Absolute Error
                        aPE = aE / sumDesireOutput; // Absolute Percentale Error ??
                        sE = error * error;//pow 2 // Square Error

                        if(aE <= ((sumDesireOutput * 25) / 100)){
                            sumPRED++; // 1
                        }
                        sumAE += aE; // 2
                        sumSE +=sE; // 3
                        sumAPE +=aPE; // 4
                    }
                    //prediction quality indicator which is  the percentage of estimates that
                    // are within m% of the actual value
                    //  reveals what proportion of estimates are within a tolerance of 25%
                    double pRED = (sumPRED / testSet.getRows().size()) * 100; // 1
                    //The mean absolute error uses the same scale as the data being measured
                    double mAE = sumAE / testSet.getRows().size(); // 2
                    // the smaller the RMSE, the better???
                    double rMSE = Math.sqrt(sumSE / testSet.getRows().size()); // 3
                    // percentage. the smaller the MAPE, the better
                    double mAPE = sumAPE / testSet.getRows().size(); // 4
                    System.out.println("Total Error = " + totalError);
                    System.out.println("MAE = " + mAE);
                    System.out.println("RMSE = " + rMSE);
                    System.out.println("MAPE = " + mAPE);
                    System.out.println("PRED(25) = " + pRED);
                    
                    // Save neural network info and its error metrics 
                    networkInfo.add((double)rbfNeuronCount);
                    networkInfo.add(learningRate);
                    networkInfo.add((double)maxIterate);
                    
                    networkInfo.add(totalError);
                    networkInfo.add(mAE);
                    networkInfo.add(rMSE);
                    networkInfo.add(mAPE);
                    networkInfo.add(pRED);
                            
                    networkList.add(networkInfo);
                            
                    networkNo++;
                }
            }
            //write to CSV file
            ReadWriteCSV.writeCSV(networkList, "src/files/nn/", "nn.csv");
        }
        
        System.out.println("Failed Network = " + failedNetwork);
    }
   
    /**
     * Test the neural network and report the error in predictions.
     * @param testSetPath
     * @param testSetFileName
     * @param nnPath
     * @param nnFileName
     * @throws IOException 
     */
    public static void predict (String testSetPath, String testSetFileName,
                            String nnPath, String nnFileName) throws IOException{
        
        ArrayList dataList = new ArrayList();
        
        System.out.println(" Neural Network is Testing . . .");
        boolean avoidDivideByZeroByOne = true;
        DataSet testSet = DataSet.load(testSetPath + testSetFileName);
        
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(nnPath + nnFileName);
        
        double sumAE = 0; double sumSE = 0; double sumAPE = 0; double sumPRED = 0;

        for(DataSetRow dataSetRow: testSet.getRows()){

            neuralNetwork.setInput(dataSetRow.getInput());
            // Test
            neuralNetwork.calculate();

            //desire output (from test set)
            double sumDesireOutput = 0; double sumPredictedOutput = 0;
            double error = 0; double aE = 0; double aPE = 0; double sE = 0;

            for(double desireOutPut: dataSetRow.getDesiredOutput()){
                //desire humidity
                double value = deNormalizedValue(desireOutPut, 0, 100);
                sumDesireOutput += value;
            }

            if(sumDesireOutput == 0 && avoidDivideByZeroByOne)
                sumDesireOutput = 1;
            else
                sumDesireOutput /= neuralNetwork.getOutputsCount();

            //predicted output
            for(int i = 0; i < neuralNetwork.getOutputsCount(); i++){
                double value = deNormalizedValue(neuralNetwork.getOutput()[i], 0, 100);
                sumPredictedOutput += value;
            }
            sumPredictedOutput /= neuralNetwork.getOutputsCount();

            // calculate error metrics
            error = sumDesireOutput - sumPredictedOutput;
            aE = Math.abs(error);  // Absolute Error
            aPE = aE / sumDesireOutput; // Absolute Percentale Error ??
            sE = error * error;//pow 2 // Square Error

            if(aE <= ((sumDesireOutput * 25) / 100)){
                sumPRED++; // 1
            }
            sumAE += aE; // 2
            sumSE +=sE; // 3
            sumAPE +=aPE; // 4
            
            // copy desire and predicted values in a list
            ArrayList<Double> row = new ArrayList<Double>();
            row.add(sumDesireOutput);
            row.add(sumPredictedOutput);
            
            dataList.add(row);
        }
        //prediction quality indicator which is  the percentage of estimates that
        // are within m% of the actual value
        //  reveals what proportion of estimates are within a tolerance of 25%
        double pRED = (sumPRED / testSet.getRows().size()) * 100; // 1
        //The mean absolute error uses the same scale as the data being measured
        double mAE = sumAE / testSet.getRows().size(); // 2
        // the smaller the RMSE, the better???
        double rMSE = Math.sqrt(sumSE / testSet.getRows().size()); // 3
        // percentage. the smaller the MAPE, the better
        double mAPE = sumAPE / testSet.getRows().size(); // 4
        double totalError = ((RBFLearning)neuralNetwork.getLearningRule()).getTotalNetworkError();
        System.out.println("Total Error = " + totalError);
        System.out.println("MAE = " + mAE);
        System.out.println("RMSE = " + rMSE);
        System.out.println("MAPE = " + mAPE);
        System.out.println("PRED(25) = " + pRED);
        
        // write to a CSV file
        ReadWriteCSV.writeCSV(dataList, "src/files/", "predictionResult.csv");
        
    }
    
    private static double normalizedValue(double input, double minInput, double maxInput) {
            return (input - minInput) / (maxInput - minInput) * 0.8 + 0.1;
    }

    private static double deNormalizedValue(double input, double minInput, double maxInput) {
            return (input - 0.1) * ( maxInput - minInput) / 0.8 + minInput;
    }
}
