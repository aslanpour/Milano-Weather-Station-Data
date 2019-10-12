/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasetanalysis.filemanipulating;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.stop.MaxErrorStop;
import java.util.Arrays;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.InputStream;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.core.data.BufferedDataSet;
import org.neuroph.core.events.NeuralNetworkEventType;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.error.ErrorFunction;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.core.transfer.Sigmoid;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.comp.layer.InputLayer;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.nnet.comp.neuron.InputOutputNeuron;
import org.neuroph.nnet.learning.kmeans.KMeansClustering;
import org.neuroph.nnet.learning.RBFLearning;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.TrainingSetImport;
import org.neuroph.util.benchmark.Stopwatch;
import org.neuroph.util.data.norm.DecimalScaleNormalizer;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.norm.RangeNormalizer;
import org.neuroph.util.random.RangeRandomizer;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.NguyenWidrowRandomizer;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.data.BufferedDataSet;
/**
 *
 * @author Sadegh Aslanpour
 */
public class MLP {

    public static void main(String[] args) {
        int inputNeuron;
        int hiddenNeroun;
        int outputNeuron;
        LearningRule learningRule;
        int learningRate;
        int maxError;
        int maxIterations;
        String filePatthTrainSet; // .tset
        String filePathTestSet; // .txt
//        Create a Neural network (Multi Layer Perceptron)
        NeuralNetwork neuralNetwork = 
                new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 5, 8, 1);// khodash bias ezaf mikonad
        // Learning Rule
        BackPropagation backPropagation = new BackPropagation();
//        MomentumBackpropagation momentumBackPropagation = new MomentumBackpropagation();
        // Sets Leaning Parameters
            // Learning Rate
        backPropagation.setLearningRate(0.2);
//        momentumBackPropagation.setLearningRate(0.4);
            // Stopping Criteria
        backPropagation.setMaxError(0.0);
        backPropagation.setMaxIterations(300);

//        momentumBackPropagation.setMaxError(0.0);
//        momentumBackPropagation.setMaxIterations(10);
//        momentumBackPropagation.setMomentum(0.2);
        // Sets Learning Rule of Neural Network
        neuralNetwork.setLearningRule(backPropagation);
//        neuralNetwork.setLearningRule(momentumBackPropagation);
        
        // Sets training set
        String filePath = "C:/AutoScaleSimFiles/TrainSet1Min.tset";
        DataSet trainingSet = DataSet.load(filePath);
        RangeRandomizer NquyenWidrowRandomizer = new NguyenWidrowRandomizer(-0.5, 0.5);
        neuralNetwork.randomizeWeights(NquyenWidrowRandomizer);
        // Learning
        neuralNetwork.learn(trainingSet);
        
        // Save network
        filePath = "C:/AutoScaleSimFiles/NeuralNetwork/NN581.nnet";
        neuralNetwork.save(filePath);
        
        // Load network
        NeuralNetwork newNeuralNetwork = NeuralNetwork.createFromFile(filePath);
        
        // Test Network
//        filePath = "C:/AutoScaleSimFiles/TestSet1Min.txt";
        filePath = "C:/AutoScaleSimFiles/TestSet1Min.tset";
//        DataSet testingSet = DataSet.createFromFile(filePath, 5, 1, ",", false);
        DataSet testingSet = DataSet.load(filePath);
        ArrayList outputList = testNeuralNetwork(newNeuralNetwork, testingSet);
        ReadWriteExcel.writeDataList(outputList, "TestResult1Min");
    }
    
    public static ArrayList testNeuralNetwork(NeuralNetwork nnet, DataSet testingSet){
        ArrayList dataList = new ArrayList<Double>();
        
        for(DataSetRow dataSetRow: testingSet.getRows()){
            ArrayList<Double> data = new ArrayList<>();
            
            nnet.setInput(dataSetRow.getInput());
            
            nnet.calculate();
            
            double networkOutput =((double[]) nnet.getOutput())[0];
            //temporary
            Double[] weight2 = nnet.getWeights();
            data.add(dataSetRow.getDesiredOutput()[0]);
            data.add(networkOutput);
            dataList.add(data);
        }
        return dataList;
                
    }
    
    public double getNormalizedValue(double x, double minInput, double maxInput, double minOutput, double maxOutput){
        double normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
        return normalizedValue;
    }
    
    public double getDenormalizedValue(double y, double minInput, double maxInput, double minOutput, double maxOutput){
        double denormalizedValue = ((y - minOutput) * (maxInput - minInput)) / maxOutput + minInput;
        return denormalizedValue;
    }
    
      
    public static double normalizedValue(double input, double minInput, double maxInput) {
            return (input - minInput) / (maxInput - minInput) * 0.8 + 0.1;
    }

    public static double deNormalizedValue(double input, double minInput, double maxInput) {
            return (input - 0.1) * ( maxInput - minInput) / 0.8 + minInput;
    }
    
    public static MultiLayerPerceptron learnNetwork(NeuralNetwork neuralNetwork){
        int inputNeuron;
        int hiddenNeroun;
        int outputNeuron;
        LearningRule learningRule;
        int learningRate;
        int maxError;
        int maxIterations;
        String filePatthTrainSet; 
        String filePathTestSet; 
        // Implement Learning Rule
        BackPropagation backPropagation = new BackPropagation();
//        MomentumBackpropagation momentumBackPropagation = new MomentumBackpropagation();
        // Sets Leaning Parameters
            // Learning Rate
        backPropagation.setLearningRate(0.2);
//        momentumBackPropagation.setLearningRate(0.4);
            // Stopping Criteria
        backPropagation.setMaxError(0.0);
        backPropagation.setMaxIterations(200);

//        momentumBackPropagation.setMaxError(0.0);
//        momentumBackPropagation.setMaxIterations(10);
//        momentumBackPropagation.setMomentum(0.2);
        // Sets Learning Rule of Neural Network
        neuralNetwork.setLearningRule(backPropagation);
//        neuralNetwork.setLearningRule(momentumBackPropagation);
        
        // Sets training set
        String filePath = "C:/AutoScaleSimFiles/TrainSet1Min.tset";
        DataSet trainingSet = DataSet.load(filePath);
        RangeRandomizer NquyenWidrowRandomizer = new NguyenWidrowRandomizer(-0.5, 0.5);
        neuralNetwork.randomizeWeights(NquyenWidrowRandomizer);
        // Learning
        neuralNetwork.learn(trainingSet);
        return (MultiLayerPerceptron)neuralNetwork;
    }
}
