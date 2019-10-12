/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasetanalysis.filemanipulating;
import java.io.InputStream;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import java.util.ArrayList;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.random.NguyenWidrowRandomizer;
import org.neuroph.util.random.RangeRandomizer;
import org.neuroph.nnet.UnsupervisedHebbianNetwork;
import org.neuroph.core.learning.UnsupervisedLearning;
import org.neuroph.core.transfer.Ramp;
import org.neuroph.nnet.learning.UnsupervisedHebbianLearning;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.learning.RBFLearning;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.core.transfer.Linear;
import org.neuroph.core.transfer.Sigmoid;
import org.neuroph.util.Neuroph;
import org.neuroph.core.Layer;
/**
 *
 * @author Sadegh Aslanpour
 */
public class TestRBF {
    public static void main(String[] args) {
        String trainDatasetPath = "D:/Dropbox/Workflow Paper/Workload Prediction/trainDS_d3456-10min-predictF-1H.tset";
        String testDatasetPath = "D:/Dropbox/Workflow Paper/Workload Prediction/testDS_d7-10min-predictF-1H.tset";
//        trainSaveTestReport(trainDatasetPath, testDatasetPath);
        test(testDatasetPath);
//        exampleCreateNeuralNetwork();
//        exampleTrainNeuralNetwork();
//        exampleTestNeuralNetwork();
    }
    
    public static void trainTestReport( String trainSetPath, String testSetPath){
         ArrayList networkList = new ArrayList<Double>();
        int networkNo = 1;
        int failedNetwork = 0; // error = NAN
        
        double minRequests = 0; // ???
        double maxRequests = 200; // ???
        int inputValues = 4;  // C
        int outputValues = 6;   // D
        
        int rbfNeuronCountStart = 3; // E
        int rbfNeuronCountEnd = 15;   // F
        
        double learningRateStart = 0.1; // G
        double learningRateEnd = 0.5;  // H
        
        int maxIterateStart = 1700;  // I
        int maxIterateEnd = 2000; // J
        
        InputFunction inputFunction = new WeightedSum(); // K      /* For Output Layer */
        TransferFunction transferFunction = new Sigmoid(); // L    /* For Output Layer */
        
        Log.printLine("Training Set is Loading . . .");
        DataSet trainSet = DataSet.load(trainSetPath); 
        
        Log.printLine("Testing Set is Loading . . .");
        DataSet testDataSet = DataSet.load(testSetPath);
        
        for (int rbfNeuronCount = rbfNeuronCountStart; rbfNeuronCount <= rbfNeuronCountEnd; rbfNeuronCount++){ 
            for(double learningRate = learningRateStart; learningRate <= learningRateEnd; learningRate += 0.1){ 
                for(int maxIterate = maxIterateStart; maxIterate <= maxIterateEnd; maxIterate+= 100){ 
                    ArrayList<Double> networkInfo = new ArrayList<>();
                    // Create a Network
                    NeuralNetwork neuralNetwork = new RBFNetwork(inputValues, rbfNeuronCount, outputValues);

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
                    RangeRandomizer NquyenWidrowRandomizer = new NguyenWidrowRandomizer(-0.5, 0.5);
                    neuralNetwork.randomizeWeights(NquyenWidrowRandomizer);

                    Log.printLine("Neural Network is Learning . . .");

                    neuralNetwork.learn(trainSet);

                    Log.printLine(" Neural Network Learned");
                                    
                    Log.printLine("**************************");
                    Log.printLine("Network No. " + networkNo + "-");
                    Log.printLine("RBF Neuron Count = " + rbfNeuronCount);
                    Log.printLine("Learning Rule = " + neuralNetwork.getLearningRule().toString());
                    Log.printLine("     Learning Rate = " + learningRate);
                    Log.printLine("     Max Iterate = " + maxIterate);
                    double totalError = ((RBFLearning)neuralNetwork.getLearningRule()).getTotalNetworkError();
                    Log.printLine("Total Error (train) =" + totalError);
                    Log.printLine("..................................");
                    
                    if(Double.isNaN(totalError)){
                        failedNetwork++;
                        break;
                    }
                    
                     // Test Phase
                    Log.printLine(networkNo + " Neural Network (" + networkNo +")" + " is Testing . . .");
                            
                    double sumAE = 0; double sumSE = 0; double sumAPE = 0; double sumPRED = 0;

                    for(DataSetRow dataSetRow: testDataSet.getRows()){

                        double sumDesireOutput = 0; double sumPredictedOutput = 0;
                        double error = 0; double aE = 0; double aPE = 0; double sE = 0;
                        ArrayList<Double> data = new ArrayList<>();
                        
                        for(double desireOutPut: dataSetRow.getDesiredOutput()){
                            
                            double value = MLP.deNormalizedValue(desireOutPut, minRequests, maxRequests);
                            sumDesireOutput += value;
//                                data.add(value);
                        }
                        sumDesireOutput /= outputValues;
                        
                        neuralNetwork.setInput(dataSetRow.getInput());
                        // Test
                        neuralNetwork.calculate();

                        
                        for(int i = 0; i < neuralNetwork.getOutputsCount(); i++){
                            double value = MLP.deNormalizedValue(neuralNetwork.getOutput()[i], minRequests, maxRequests);
                            sumPredictedOutput += value;
//                                data.add(value);
                        }
                        sumPredictedOutput /= outputValues;

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

//                            dataList.add(data);
                    }

                    double pRED = sumPRED / testDataSet.getRows().size(); // 1
                    double mAE = sumAE / testDataSet.getRows().size(); // 2
                    double rMSE = Math.sqrt(sumSE / testDataSet.getRows().size()); // 3
                    double mAPE = sumAPE / testDataSet.getRows().size(); // 4
                    Log.printLine("Total Error = " + totalError);
                    Log.printLine("MAE = " + mAE);
                    Log.printLine("RMSE = " + rMSE);
                    Log.printLine("MAPE = " + mAPE);
                    Log.printLine("PRED(25) = " + pRED);
                    
                    // Save Information of This Network
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
            
        }
        // Write to Excel
        Log.printLine("Failde Network = " + failedNetwork);
        ReadWriteExcel.writeDataList(networkList);
    }
    
    public static void trainSaveTestReport( String trainSetPath, String testSetPath){
         ArrayList networkList = new ArrayList<Double>();
        int networkNo = 1;
        int failedNetwork = 0; // error = NAN
        
        double minRequests = 0; // ???
        double maxRequests = 200; // ???
        int inputValues = 5;  // C
        int outputValues = 1;   // D
        
        int rbfNeuronCountStart = 2; // E
        int rbfNeuronCountEnd = 2;   // F
        
        double learningRateStart = 0.1; // G
        double learningRateEnd = 0.1;  // H
        
        int maxIterateStart = 100;  // I
        int maxIterateEnd = 200; // J
        
        boolean avoidDivideByZeroByOne = true; // k
        
        InputFunction inputFunction = new WeightedSum(); // K      /* For Output Layer */
        TransferFunction transferFunction = new Sigmoid(); // L    /* For Output Layer */
        
        Log.printLine("Training Set is Loading . . .");
        DataSet trainSet = DataSet.load(trainSetPath); 
        
        Log.printLine("Testing Set is Loading . . .");
        DataSet testDataSet = DataSet.load(testSetPath);
        
        for (int rbfNeuronCount = rbfNeuronCountStart; rbfNeuronCount <= rbfNeuronCountEnd; rbfNeuronCount++){ 
            for(double learningRate = learningRateStart; learningRate <= learningRateEnd; learningRate += 0.1){ 
                for(int maxIterate = maxIterateStart; maxIterate <= maxIterateEnd; maxIterate+= 100){ 
                    ArrayList<Double> networkInfo = new ArrayList<>();
                    // Create a Network
                    NeuralNetwork neuralNetwork = new RBFNetwork(inputValues, rbfNeuronCount, outputValues);

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
                    RangeRandomizer NquyenWidrowRandomizer = new NguyenWidrowRandomizer(-0.5, 0.5);
                    neuralNetwork.randomizeWeights(NquyenWidrowRandomizer);

                    Log.printLine("Neural Network is Learning . . .");

                    neuralNetwork.learn(trainSet);

                    Log.printLine(" Neural Network Learned");
                                    
                    Log.printLine("**************************");
                    Log.printLine("Network No. " + networkNo + "-");
                    Log.printLine("RBF Neuron Count = " + rbfNeuronCount);
                    Log.printLine("Learning Rule = " + neuralNetwork.getLearningRule().toString());
                    Log.printLine("     Learning Rate = " + learningRate);
                    Log.printLine("     Max Iterate = " + maxIterate);
                    double totalError = ((RBFLearning)neuralNetwork.getLearningRule()).getTotalNetworkError();
                    Log.printLine("Total Error (train) =" + totalError);
                    Log.printLine("..................................");
                    
                    if(Double.isNaN(totalError)){
                        failedNetwork++;
                        break;
                    }
                    
                    // save network
                    String netPath = "D:/Dropbox/Workflow Paper/Workload Prediction/NeuralNetworks/RBF"
                                                 + "/RBF_In" + inputValues 
                                                 + "_H" + rbfNeuronCount 
                                                 + "_Out" + outputValues
                                                 + "_LR" + learningRate 
                                                 + "_Iter" + maxIterate + ".nnet";
                    
                    neuralNetwork.save(netPath);
                     // Test Phase
                    Log.printLine(networkNo + " Neural Network (" + networkNo +")" + " is Testing . . .");
                            
                    double sumAE = 0; double sumSE = 0; double sumAPE = 0; double sumPRED = 0;

                    for(DataSetRow dataSetRow: testDataSet.getRows()){
                        
                        neuralNetwork.setInput(dataSetRow.getInput());
                        // Test
                        neuralNetwork.calculate();

                        //desire output (from test set)
                        double sumDesireOutput = 0; double sumPredictedOutput = 0;
                        double error = 0; double aE = 0; double aPE = 0; double sE = 0;
                        ArrayList<Double> data = new ArrayList<>();
                        
                        for(double desireOutPut: dataSetRow.getDesiredOutput()){
                            
                            double value = MLP.deNormalizedValue(desireOutPut, minRequests, maxRequests);
                            sumDesireOutput += value;
//                                data.add(value);
                        }
                        
                        if(sumDesireOutput == 0 && avoidDivideByZeroByOne)
                            sumDesireOutput = 1;
                        else
                            sumDesireOutput /= outputValues;
                        
                        //predicted output
                        for(int i = 0; i < neuralNetwork.getOutputsCount(); i++){
                            double value = MLP.deNormalizedValue(neuralNetwork.getOutput()[i], minRequests, maxRequests);
                            sumPredictedOutput += value;
//                                data.add(value);
                        }
                        sumPredictedOutput /= outputValues;

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

//                            dataList.add(data);
                    }

                    double pRED = sumPRED / testDataSet.getRows().size(); // 1
                    double mAE = sumAE / testDataSet.getRows().size(); // 2
                    double rMSE = Math.sqrt(sumSE / testDataSet.getRows().size()); // 3
                    double mAPE = sumAPE / testDataSet.getRows().size(); // 4
                    Log.printLine("Total Error = " + totalError);
                    Log.printLine("MAE = " + mAE);
                    Log.printLine("RMSE = " + rMSE);
                    Log.printLine("MAPE = " + mAPE);
                    Log.printLine("PRED(25) = " + pRED);
                    
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
            
        }
        // Write to Excel
        Log.printLine("Failde Network = " + failedNetwork);
        ReadWriteExcel.writeDataList(networkList);
    }
    
    public static void trainTestWriteCreate( String trainSetPath, String testSetPath){
        ArrayList dataList = new ArrayList<Double>();

        int inputValues = 4;  // C
        int outputValues = 6;   // D
        int rbfNeuron = 7; // E
        double learningRate = 0.1; // F
        int maxIterate = 1800;  // G
        InputFunction inputFunction = new WeightedSum(); // K      /* For Output Layer */
        TransferFunction transferFunction = new Sigmoid(); // L    /* For Output Layer */

        String netPath = "C:/AutoScaleSimFiles/NeuralNetwork/"
                        + "RBF_In" + inputValues + "_H" + rbfNeuron + "_Out" + outputValues + "_LR" + learningRate + 
                        "_Iter" + maxIterate + ".nnet";
        
        Log.printLine("Training Set is Loading . . .");
        DataSet trainSet = DataSet.load(trainSetPath); 
        
        Log.printLine("Testing Set is Loading . . .");
        DataSet testSet = DataSet.load(testSetPath);
        
        RBFNetwork rbfNetwork = new RBFNetwork(inputValues, rbfNeuron, outputValues);
        
        for(int i = 0; i < rbfNetwork.getOutputsCount(); i++){
                rbfNetwork.getLayerAt(rbfNetwork.getLayersCount() - 1).getNeuronAt(i)
                                                            .setInputFunction(inputFunction);
                rbfNetwork.getLayerAt(rbfNetwork.getLayersCount() - 1)
                                                            .getNeuronAt(i).setTransferFunction(transferFunction);
        }
        
        RBFLearning learningRule = new RBFLearning();
        learningRule.setLearningRate(learningRate);
        learningRule.setMaxIterations(maxIterate);
        learningRule.setMaxError(0.0);
        learningRule.setNeuralNetwork(rbfNetwork);
        
        rbfNetwork.setLearningRule(learningRule);
        
        RangeRandomizer NquyenWidrowRandomizer = new NguyenWidrowRandomizer(-0.5, 0.5);
        rbfNetwork.randomizeWeights(NquyenWidrowRandomizer);
        // Train
        rbfNetwork.learn(trainSet);
        Log.printLine("Total Error = " + ((RBFLearning)rbfNetwork.getLearningRule()).getTotalNetworkError());
        
        rbfNetwork.save(netPath);
        
        // Test
        for(DataSetRow dataSetRow: testSet.getRows()){
            ArrayList<Double> data = new ArrayList<>();
            
            for(double input: dataSetRow.getInput()){
                data.add(input);
            }
            
            double networkTotalDesireOutput = 0;
            for(double desireOutPut: dataSetRow.getDesiredOutput()){
                networkTotalDesireOutput += desireOutPut;
//                data.add(desireOutPut);
            }
            data.add(networkTotalDesireOutput / dataSetRow.getDesiredOutput().length);
            
            rbfNetwork.setInput(dataSetRow.getInput());

            rbfNetwork.calculate();
            
            double networkTotalOutput = 0;
            for(int i = 0; i < rbfNetwork.getOutputsCount(); i++){
                networkTotalOutput += rbfNetwork.getOutput()[i];
//                data.add(neuralNetwork.getOutput()[i]);
            }
            data.add(networkTotalOutput / rbfNetwork.getOutputsCount());
            
            dataList.add(data);
        }

        Log.printLine("Neural Network Tested");
        ReadWriteExcel.writeDataList(dataList);
    }
    
    public static void test (String testSetPath){
        ArrayList dataList = new ArrayList<Double>();

        double minRequests = 0; // ???
        double maxRequests = 200; //???
        int inputValues = 5;  // C
        int outputValues = 1;   // D
        int rbfNeuron = 2; // E
        double learningRate = 0.1; // F
        int maxIterate = 100;  // G
        
        String netPath = "D:/Dropbox/Workflow Paper/Workload Prediction/NeuralNetworks/RBF/"
                        + "RBF_In" + inputValues + "_H" + rbfNeuron + "_Out" + outputValues + "_LR" + learningRate + 
                            "_Iter" + maxIterate + ".nnet";
         Log.printLine("Testing Set is Loading . . .");
        DataSet testSet = DataSet.load(testSetPath);
        
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(netPath);
        
        // Test
        double sumAE = 0; double sumSE = 0; double sumAPE = 0; double sumPRED = 0;

        for(DataSetRow dataSetRow: testSet.getRows()){

                        double sumDesireOutput = 0; double sumPredictedOutput = 0;
                        double error = 0; double aE = 0; double aPE = 0; double sE = 0;
                        
                        for(double desireOutPut: dataSetRow.getDesiredOutput()){
                            
                            double value = MLP.deNormalizedValue(desireOutPut, minRequests, maxRequests);
                            sumDesireOutput += value;
//                                data.add(value);
                        }
                        sumDesireOutput /= dataSetRow.getDesiredOutput().length;

                        neuralNetwork.setInput(dataSetRow.getInput());
                        // Test
                        neuralNetwork.calculate();

                        
                        for(int i = 0; i < neuralNetwork.getOutputsCount(); i++){
                            double value = MLP.deNormalizedValue(neuralNetwork.getOutput()[i], minRequests, maxRequests);
                            sumPredictedOutput += value;
//                                data.add(value);
                        }
                        sumPredictedOutput /= neuralNetwork.getOutputsCount();

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

//                            dataList.add(data);
                    }

                    double pRED = sumPRED / testSet.getRows().size(); // 1
                    double mAE = sumAE / testSet.getRows().size(); // 2
                    double rMSE = Math.sqrt(sumSE / testSet.getRows().size()); // 3
                    double mAPE = sumAPE / testSet.getRows().size(); // 4
                    Log.printLine("Total Error = " + ((RBFLearning)neuralNetwork.getLearningRule()).getTotalNetworkError());
                    Log.printLine("MAE = " + mAE);
                    Log.printLine("RMSE = " + rMSE);
                    Log.printLine("MAPE = " + mAPE);
                    Log.printLine("PRED(25) = " + pRED);

                    Log.printLine("Out layer" + neuralNetwork.getLayerAt(2).getNeuronAt(0).getTransferFunction().toString());
                    Log.printLine(neuralNetwork.getLayerAt(2).getNeuronAt(1).getInputFunction());
        Log.printLine("Neural Network Tested");
        ReadWriteExcel.writeDataList(dataList);
    }
    
    public static void exampleCreateNeuralNetwork(){
    /*(1) Create Neural Network */
        // Set Neural Network Configuration 
        int neuronInInputLayer = 4;    // input neurons
        int neuronsInRBFLayer = 8;     // rbf neurons
        int neuronInOutputLayer = 6;   // output neurons
        
        // Create a Network object based on defined configuration
        NeuralNetwork myNeuralNetwork = new RBFNetwork(neuronInInputLayer, neuronsInRBFLayer, neuronInOutputLayer);
        
        // Save Neural Network in Hard disk
        String netPath = "C:/AutoScaleSimFiles/myNeuralNetwork.nnet";
        myNeuralNetwork.save(netPath);
        Log.printLine("My Neural Network created in: ");
        Log.printLine(Const.oneTab + netPath);
        Log.printLine("Configuration is: ");
        Log.printLine("Input Neurons: "         + myNeuralNetwork.getInputsCount() 
                    + " Hidden(RBF) Neurons: " + myNeuralNetwork.getLayerAt(1).getNeuronsCount()
                    + " Output Neurons: "       + myNeuralNetwork.getLayerAt(2).getNeuronsCount());
        
    }
    
    /**
     * 
     */
    public static void exampleTrainNeuralNetwork(){
    /*(2)Load my neural network */
        String netPath = "C:/AutoScaleSimFiles/myNeuralNetwork.nnet";
        NeuralNetwork myNeuralNetwork = NeuralNetwork.createFromFile(netPath);
        // Learning Rule */
            // Set Learning Parameters
        RBFLearning learningRule = new RBFLearning(); // create a learning objcet
        
        learningRule.setLearningRate(0.2);  // between 0 to 1
        learningRule.setMaxError(0.0);  // the learning phase would stop if max error in each epoch reach this value
        learningRule.setMaxIterations(100); // the learning phase would stop if max iterations (epoch) reach this value
        // Apply learning rule to my neural network
        learningRule.setNeuralNetwork(myNeuralNetwork);
        myNeuralNetwork.setLearningRule(learningRule);

        // Training */
            // Training (the goal of training a neural network is that we want to set the weights
                    // There is a weight parameter Between every related neuron.
        
        // Load train dataset
        Log.printLine("Train Set is Loading . . .");
        String trainSetPath = "C:/AutoScaleSimFiles/trainSet10Min_345.tset";
        DataSet trainSet = DataSet.load(trainSetPath); 
        // Set primary weights
        RangeRandomizer NquyenWidrowRandomizer = new NguyenWidrowRandomizer(-0.5, 0.5);
        myNeuralNetwork.randomizeWeights(NquyenWidrowRandomizer);

        // Start training
        myNeuralNetwork.learn(trainSet);

        Log.printLine("Neural Network is Trained");

        // Save Neural Network in Hard disk
        myNeuralNetwork.save(netPath);
        
        // Print Logs */
        Log.printLine("**************************");
        Log.printLine("RBF Configuration = "
                + "Input Neurons: "         + myNeuralNetwork.getInputsCount() 
                + " Hidden(RBF) Neurons: " + myNeuralNetwork.getLayerAt(1).getNeuronsCount()
                + " Output Neurons: "       + myNeuralNetwork.getLayerAt(2).getNeuronsCount());
        
        Log.printLine("Learning Rule = " + myNeuralNetwork.getLearningRule().toString());
        Log.printLine("     Learning Rate = " + ((RBFLearning)myNeuralNetwork.getLearningRule()).getLearningRate());
        Log.printLine("     Max Iterate = " + ((RBFLearning)myNeuralNetwork.getLearningRule()).getMaxIterations());
        Log.printLine("Total Error (train) =" + ((RBFLearning)myNeuralNetwork.getLearningRule()).getTotalNetworkError());

    }
    
    public static void exampleTestNeuralNetwork(){
    /*(3) Load my neural network */
        String netPath = "C:/AutoScaleSimFiles/myNeuralNetwork.nnet";
        NeuralNetwork myNeuralNetwork = NeuralNetwork.createFromFile(netPath);
        // Load test file */
        Log.printLine("Test set is Loading . . .");
        // Load dataset file in ordet to test my neural network
        String testSetPath = "C:/AutoScaleSimFiles/testSet10Min_6.tset";
        DataSet testDataSet = DataSet.load(testSetPath);
        
        // Test 
        // some parameters for error calculation in prediction
        double sumAE = 0; double sumSE = 0; double sumAPE = 0; double sumPRED = 0;
        // Test neural network accuarancy by data set
        // Each Row in dataset contains input series and desire output series:
        // Input series are actual inputs (day of week, hour of day, minute of hour, and arrived user requests)
        // Desire output series are actual values (arrival user requests in 10, 20, 30, 40, 50, and 60 minutes later)
        //
        for(DataSetRow dataSetRow: testDataSet.getRows()){
            // assign values to input neuron of neural network
            myNeuralNetwork.setInput(dataSetRow.getInput());
            // prediction future user requests
            myNeuralNetwork.calculate();

            //desire output (from test set)
            double sumDesireOutput = 0; double sumPredictedOutput = 0;
            double error = 0; double aE = 0; double aPE = 0; double sE = 0;

            // error calculation
            // sum desire (actual) outputs
            for(double desireOutPut: dataSetRow.getDesiredOutput()){
                sumDesireOutput += desireOutPut;
            }

            if(sumDesireOutput == 0)
                sumDesireOutput = 1;
            else
                sumDesireOutput /= dataSetRow.getDesiredOutput().length;

            //some predicted outputs
            for(int i = 0; i < myNeuralNetwork.getOutputsCount(); i++){
                sumPredictedOutput += myNeuralNetwork.getOutput()[i];
            }
            sumPredictedOutput /= myNeuralNetwork.getLayerAt(2).getNeuronsCount();

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

        double pRED = sumPRED / testDataSet.getRows().size(); // 1
        double mAE = sumAE / testDataSet.getRows().size(); // 2
        double rMSE = Math.sqrt(sumSE / testDataSet.getRows().size()); // 3
        double mAPE = sumAPE / testDataSet.getRows().size(); // 4
        
        Log.printLine("Neural Network is Trained");
        
        Log.printLine("Total Error = " + ((RBFLearning)myNeuralNetwork.getLearningRule()).getTotalNetworkError());
        Log.printLine("MAE = " + mAE);
        Log.printLine("RMSE = " + rMSE);
        Log.printLine("MAPE = " + mAPE);
        Log.printLine("PRED(25) = " + pRED);
    }
}
