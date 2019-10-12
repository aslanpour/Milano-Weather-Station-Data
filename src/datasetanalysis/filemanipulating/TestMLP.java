/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasetanalysis.filemanipulating;
import autoscalesim.container.ReadWriteExcel;
import org.cloudbus.cloudsim.Log;
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

/**
 *
 * @author Sadegh Aslanpour
 */
public class TestMLP {

    /**
     *
     */
    
    
    public static void main(String[] args) {
        
        trainTestWrite();
//        test();
        int networkNo = 1;
        ArrayList networkList = new ArrayList();
        double[][] topsInLearn = new double[5][4]; // Learn Error
        topsInLearn[0][0] = Double.MAX_VALUE;topsInLearn[0][1] = Double.MAX_VALUE;topsInLearn[0][2] = Double.MAX_VALUE;topsInLearn[0][3] = Double.MAX_VALUE;
        topsInLearn[1][0] = Double.MAX_VALUE;topsInLearn[1][1] = Double.MAX_VALUE;topsInLearn[1][2] = Double.MAX_VALUE;topsInLearn[1][3] = Double.MAX_VALUE;
        topsInLearn[2][0] = Double.MAX_VALUE;topsInLearn[2][1] = Double.MAX_VALUE;topsInLearn[2][2] = Double.MAX_VALUE;topsInLearn[2][3] = Double.MAX_VALUE;
        topsInLearn[3][0] = Double.MAX_VALUE;topsInLearn[3][1] = Double.MAX_VALUE;topsInLearn[3][2] = Double.MAX_VALUE;topsInLearn[3][3] = Double.MAX_VALUE;
        topsInLearn[4][0] = Double.MAX_VALUE;topsInLearn[4][1] = Double.MAX_VALUE;topsInLearn[4][2] = Double.MAX_VALUE;topsInLearn[4][3] = Double.MAX_VALUE;
        double[][] topsInMAPE = new double[5][2];
        topsInMAPE[0][0] = Double.MAX_VALUE;topsInMAPE[0][1] = Double.MAX_VALUE;
        topsInMAPE[1][0] = Double.MAX_VALUE;topsInMAPE[1][1] = Double.MAX_VALUE;
        topsInMAPE[2][0] = Double.MAX_VALUE;topsInMAPE[2][1] = Double.MAX_VALUE;
        topsInMAPE[3][0] = Double.MAX_VALUE;topsInMAPE[3][1] = Double.MAX_VALUE;
        topsInMAPE[4][0] = Double.MAX_VALUE;topsInMAPE[4][1] = Double.MAX_VALUE;
        double[][] topsInRMSE = new double[5][2];
        topsInRMSE[0][0] = Double.MAX_VALUE;topsInRMSE[0][1] = Double.MAX_VALUE;
        topsInRMSE[1][0] = Double.MAX_VALUE;topsInRMSE[1][1] = Double.MAX_VALUE;
        topsInRMSE[2][0] = Double.MAX_VALUE;topsInRMSE[2][1] = Double.MAX_VALUE;
        topsInRMSE[3][0] = Double.MAX_VALUE;topsInRMSE[3][1] = Double.MAX_VALUE;
        topsInRMSE[4][0] = Double.MAX_VALUE;topsInRMSE[4][1] = Double.MAX_VALUE;

        Log.printLine("Training Set is Loading . . .");
        DataSet trainSet = DataSet.load("C:/AutoScaleSimFiles/TrainSet10Min.tset"); // ???
        
        Log.printLine("Testing Set is Loading . . .");
        DataSet testDataSet = DataSet.load("C:/AutoScaleSimFiles/TestSet10Min.tset"); // ???

        for( int hNeuron = 8; hNeuron <= 8; hNeuron++){ // A?
            NeuralNetwork neuralNetwork;
            neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 5, hNeuron, 6); 
            for(int learningRule = 0; learningRule <=0; learningRule+= 3){ // B?
                
                double lRate = 0.2;
                while(lRate <= 0.2){ // C?

                    double maxIter = 600;  // should be 100
                    
                    while(maxIter <=600){ // D?
                        double moment = 0.5; //equal with moment max
                        if(learningRule >= 3)// if Learning Rule is MomentumBP
                            moment =0.2;
                        while(moment <= 0.5){ // E?
                            switch(learningRule){
                                case 0:
                                    BackPropagation backPropagation = new BackPropagation();
                                    backPropagation.setLearningRate(lRate);
                                    backPropagation.setMaxError(0.0);
                                    backPropagation.setMaxIterations((int)maxIter);
                                    backPropagation.setNeuralNetwork(neuralNetwork);
                                    neuralNetwork.setLearningRule(backPropagation);
                                    break;
                                case 1:
                                    ResilientPropagation resilientPropagation = new ResilientPropagation();
                                    resilientPropagation.setLearningRate(lRate);
                                    resilientPropagation.setMaxError(0.0);
                                    resilientPropagation.setMaxIterations((int)maxIter);
                                    resilientPropagation.setNeuralNetwork(neuralNetwork);
                                    neuralNetwork.setLearningRule(resilientPropagation);
                                    break;
                                case 2:
                                    DynamicBackPropagation dynamicBackPropagation = new DynamicBackPropagation();
                                    dynamicBackPropagation.setLearningRate(lRate);
                                    dynamicBackPropagation.setMaxError(0.0);
                                    dynamicBackPropagation.setMaxIterations((int)maxIter);
                                    dynamicBackPropagation.setMaxMomentum(0.2);
                                    dynamicBackPropagation.setMinLearningRate(0.2);
                                    dynamicBackPropagation.setMaxLearningRate(0.6);
                                    dynamicBackPropagation.setNeuralNetwork(neuralNetwork);
                                    neuralNetwork.setLearningRule(dynamicBackPropagation);
                                    break;
                                default:
                                    MomentumBackpropagation momentumBackPropagation = new MomentumBackpropagation();
                                    momentumBackPropagation.setLearningRate(lRate);
                                    momentumBackPropagation.setMomentum(moment);
                                    momentumBackPropagation.setMaxError(0.0);
                                    momentumBackPropagation.setMaxIterations((int)maxIter);
                                    momentumBackPropagation.setNeuralNetwork(neuralNetwork);
                                    neuralNetwork.setLearningRule(momentumBackPropagation);
                                    break;
                            }

                            RangeRandomizer NquyenWidrowRandomizer = new NguyenWidrowRandomizer(-0.5, 0.5);
                            neuralNetwork.randomizeWeights(NquyenWidrowRandomizer);

                            Log.printLine("Neural Network is Learning . . .");

                            neuralNetwork.learn(trainSet);

                            Log.printLine(" Neural Network Learned");
                            double hiddenNeuron; double learningRate; double momentumRate = 0; double iter; double totalError;
                            switch(learningRule){
                                case 0:
                                    hiddenNeuron = neuralNetwork.getLayers()[1].getNeuronsCount();
                                    learningRate = ((BackPropagation)neuralNetwork.getLearningRule()).getLearningRate();
                                    iter = ((BackPropagation)neuralNetwork.getLearningRule()).getCurrentIteration();
                                    totalError = ((BackPropagation)neuralNetwork.getLearningRule()).getTotalNetworkError();
                                    break;
                                case 1:
                                    hiddenNeuron = neuralNetwork.getLayers()[1].getNeuronsCount() + 1;
                                    learningRate = ((ResilientPropagation)neuralNetwork.getLearningRule()).getLearningRate();
                                    iter = ((ResilientPropagation)neuralNetwork.getLearningRule()).getCurrentIteration();
                                    totalError = ((ResilientPropagation)neuralNetwork.getLearningRule()).getTotalNetworkError();
                                    break;
                                case 2:
                                    hiddenNeuron = neuralNetwork.getLayers()[1].getNeuronsCount();
                                    learningRate = ((DynamicBackPropagation)neuralNetwork.getLearningRule()).getLearningRate();
                                    iter = ((DynamicBackPropagation)neuralNetwork.getLearningRule()).getCurrentIteration();
                                    totalError = ((DynamicBackPropagation)neuralNetwork.getLearningRule()).getTotalNetworkError();
                                    break;
                                default:
                                    hiddenNeuron = neuralNetwork.getLayers()[1].getNeuronsCount();
                                    learningRate = ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getLearningRate();
                                    iter = ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getMaxIterations();
                                    totalError = ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getTotalNetworkError();
                                    momentumRate = ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getMomentum();
                            }

                            Log.printLine("**************************");
                            Log.printLine("Network No. " + networkNo + "-");
                            Log.printLine("Hidden Neuron(with Bias) = " + hiddenNeuron);
                            Log.printLine("Learning Rule = " + neuralNetwork.getLearningRule().toString());
                            Log.printLine("     Learning Rate = " + learningRate);
                            if(learningRule == 3)
                                Log.printLine("    Momentum = " + momentumRate);

                            Log.printLine("     Iterate = " + iter);
                            Log.printLine("Total Error = " + totalError);
                            Log.printLine("..................................");

                            boolean replaced = false;
                            for(int index = 0; index < topsInLearn.length;index++){
                                if(totalError < topsInLearn[index][3] && replaced == false){
                                    topsInLearn[index][0] = hNeuron;
                                    topsInLearn[index][1] = lRate;
                                    topsInLearn[index][2] = maxIter;
                                    topsInLearn[index][3] = totalError;
                                    replaced = true;
                                    Log.printLine("The Network added to tops Learning, number  " + (index + 1));
                                }
                            }


                            // Test Phase
                            Log.printLine(networkNo + " Neural Network  is Testing . . .");

                            double sumAE = 0; double sumSE = 0; double sumAPE = 0; double sumPRED = 0;

                            for(DataSetRow dataSetRow: testDataSet.getRows()){

                                double sumDesireOutput = 0; double sumPredictedOutput = 0;
                                double error = 0; double aE = 0; double aPE = 0; double sE = 0;
                                ArrayList<Double> data = new ArrayList<>();

    //                            for(double input: dataSetRow.getInput()){
    //                                double value = MLP.deNormalizedValue(input, 50000, 1600000);
    ////                                data.add(value);
    //                            }
                                for(double desireOutPut: dataSetRow.getDesiredOutput()){
                                    double value = MLP.deNormalizedValue(desireOutPut, 50000, 1600000);
                                    sumDesireOutput += value;
    //                                data.add(value);
                                }
                                
//                                sumDesireOutput /= 6;
                                neuralNetwork.setInput(dataSetRow.getInput());
                                // Test
                                neuralNetwork.calculate();

                                for(int i = 0; i < neuralNetwork.getOutputsCount(); i++){
                                    double value = MLP.deNormalizedValue(neuralNetwork.getOutput()[i], 50000, 1600000);
                                    sumPredictedOutput += value;
    //                                data.add(value);
                                }

//                                sumPredictedOutput /=6;
                                
                                error = sumPredictedOutput - sumDesireOutput;
                                aE = Math.abs(error);
                                aPE = aE / sumDesireOutput;
                                sE = error * error;//pow 2

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

                            Log.printLine("MAE = " + mAE);
                            Log.printLine("RMSE = " + rMSE);
                            Log.printLine("MAPE = " + mAPE);
                            Log.printLine("PRED(25) = " + pRED);

                            boolean replacedMAPE = false;
                            for(int index = 0; index < topsInMAPE.length;index++){
                                if(mAPE < topsInMAPE[index][1] && replacedMAPE == false){
                                    topsInMAPE[index][0] = networkNo;
                                    topsInMAPE[index][1] = mAPE;
                                    replacedMAPE = true;
                                    Log.printLine("The Network added to tops MAPE, number  " + (index + 1));
                                }
                            }

                            boolean replacedRMSE = false;
                            for(int index = 0; index < topsInRMSE.length;index++){
                                if(rMSE < topsInRMSE[index][1] && replacedRMSE == false){
                                    topsInRMSE[index][0] = networkNo;
                                    topsInRMSE[index][1] = rMSE;
                                    replacedRMSE = true;
                                    Log.printLine("The Network added to tops RMSE, number  " + (index + 1));
                                }
                            }

                            ArrayList networkInfo = new ArrayList();
                            networkInfo.add((double)hiddenNeuron);
                            networkInfo.add((double)learningRule);
                            networkInfo.add((double)learningRate);
                            networkInfo.add((double)momentumRate);
                            networkInfo.add((double)iter);
                            networkInfo.add((double)totalError);
                            networkInfo.add((double)mAPE);
                            networkInfo.add((double)rMSE);
                            networkInfo.add((double)mAE);
                            networkInfo.add((double)pRED);
                            
                            networkList.add(networkInfo);

                            
                            networkNo ++;
                            
                            moment += 0.1;
                        }
                        maxIter += 100;
                    }

                    lRate += 0.1;
                }
            }
        }
        Log.printLine("TOPS IN LEARNING");
        for(int index = 0; index < topsInLearn.length; index++){
             Log.printLine("Number " + (index + 1) + " = ");
             Log.printLine("Hidden Neuron = " + topsInLearn[index][0]);
             Log.printLine("Learning Rate = " + topsInLearn[index][1]);
             Log.printLine("Iterate = " + topsInLearn[index][2]);
             Log.printLine("Total Error = " + topsInLearn[index][3]);
             Log.printLine(".............");
         }
        
        Log.printLine("TOPS IN MAPE");
        for(int index = 0; index < topsInMAPE.length; index++){
             Log.printLine("Number " + (index + 1) + " = ");
             Log.printLine("Network No. = " + topsInMAPE[index][0] + "-");
             Log.printLine("MAPE = " + topsInMAPE[index][1]);
             Log.printLine(".............");
         }
        
        Log.printLine("TOPS IN RMSE");
        for(int index = 0; index < topsInRMSE.length; index++){
             Log.printLine("Number " + (index + 1) + " = ");
             Log.printLine("Network No. = " + topsInRMSE[index][0] + "-");
             Log.printLine("RMSE = " + topsInRMSE[index][1]);
             Log.printLine(".............");
         }
        
        ReadWriteExcel.writeDataList(networkList);
        
    }
    
    public static void trainTestWrite(){
    /* Preparing a Neural Network */
        /* Configure a Neural Network */
        int inputNeuron = 2; // Neurons in input layer
        int hiddenNeuron = 4; // Neurons in hidden layer 
        int outputNeuron = 1; // Neurons in output layer
            // Transfer function of each neuron in hidden and output layers. ther is sigmoid, gaussian, tanH, linear or . . .
            // notice: usually transfer function of input layer sets by linear function
        TransferFunctionType transferFunction = TransferFunctionType.SIGMOID;
        
        /* Create a Neural Network (Multi Layer Perceptron, RBFNetwork or . . .) */
        NeuralNetwork myNeuralNetwork;
        myNeuralNetwork = new MultiLayerPerceptron(transferFunction,
                                                inputNeuron,
                                                hiddenNeuron - 1, // A bias neuron automatically will adds
                                                outputNeuron);
        /* Set Learning Parameters */
        double learningRate = 0.2; // Should be between 0 to 1
        double maxError = 0.0; // In train phase, if the network gain this value (error) in an epotch, the training phase
                                // would stop
        int maxIterations = 1000; // the training phase would stop after this epotch
            // Save parameters in an Learning Rule (BackPropagation, MomentumBackPropagation or . . .)
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setLearningRate(learningRate);
        backPropagation.setMaxError(maxError);
        backPropagation.setMaxIterations(maxIterations);
        backPropagation.setNeuralNetwork(myNeuralNetwork);
        myNeuralNetwork.setLearningRule(backPropagation);
        
    /* Train Neural Network by a DataSet (the example is prediction of XOR function) */
        // Create training dataset
        DataSet trainingDataSet = new DataSet(inputNeuron, outputNeuron); // (input, desire output)
        double[] input0 = new double[]{0,0}; double[] input1 = new double[]{0,1};
        double[] input2 = new double[]{1,0}; double[] input3 = new double[]{1,1};
        double[] desireOutput0 = new double[]{0}; double[] desireOutput1 = new double[]{1};
        double[] desireOutput2 = new double[]{1}; double[] desireOutput3 = new double[]{1};
        trainingDataSet.addRow(input0, desireOutput0);
        trainingDataSet.addRow(input1, desireOutput1);
        trainingDataSet.addRow(input2, desireOutput2);
        trainingDataSet.addRow(input3, desireOutput3);
        
        // Start Leaning
        myNeuralNetwork.learn(trainingDataSet);
        System.out.println("Neural Network has been learned");
        System.out.println(" Total Network Error " 
                            + ((BackPropagation)myNeuralNetwork.getLearningRule()).getTotalNetworkError());
        
        // Save Learned Neural Network as a .nnet file
        String filePath = "c:/myNeuralNetwork.nnet";
        myNeuralNetwork.save(filePath);

    /* Test Neural Network */
        // Load neural network for Testing by an unaccustomed dataset
        NeuralNetwork learnedNeuralNetwork = NeuralNetwork.createFromFile(filePath);
        
        // create testing dataSet
        DataSet testingDataSet = new DataSet(inputNeuron, outputNeuron); // (input , desireOutput)
        double[] input = new double[]{1,0}; 
        double[] desireOutput = new double[]{0}; 
        testingDataSet.addRow(input, desireOutput);
        // Give inputs to NeuralNetwork
        learnedNeuralNetwork.setInput(testingDataSet.getRowAt(0).getInput());
        learnedNeuralNetwork.calculate();
        
        // Get Predicted Value
        double predictedValue = learnedNeuralNetwork.getOutput()[0];
        System.out.println("Inputs (" + input[0] + " and " + input[1] + ") -----> predicted value is " + predictedValue);
    }
 
    public static void test(){
        ArrayList dataList = new ArrayList<Double>();
        String filePath = "C:/AutoScaleSimFiles/NeuralNetwork/NN5" + (12) + "6.nnet"; // A?
        String filePathTestSet = "C:/AutoScaleSimFiles/TestSet10Min.tset"; // B?
        
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(filePath);
//        Log.printLine("Learn rate " + ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getLearningRate());
//        Log.printLine("Momentum Rate " + ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getMomentum());
//        Log.printLine("Max Iter " + ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getMaxIterations());
//        Log.printLine("Current Iter " + ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getCurrentIteration());
//        Log.printLine("Total Error "  + ((MomentumBackpropagation)neuralNetwork.getLearningRule()).getTotalNetworkError());
//        Log.printLine("Neural Network is Testing . . .");
        
        DataSet testingSet = DataSet.load(filePathTestSet);
        
        for(DataSetRow dataSetRow: testingSet.getRows()){
            ArrayList<Double> data = new ArrayList<>();
            
            for(double input: dataSetRow.getInput()){
                data.add(input);
            }
            
            double networkTotalDesireOutput = 0;
            for(double desireOutPut: dataSetRow.getDesiredOutput()){
                networkTotalDesireOutput += desireOutPut;
//                data.add(desireOutPut);
            }
            data.add(MLP.deNormalizedValue(networkTotalDesireOutput / 6, 50000, 1600000));
            
            neuralNetwork.setInput(dataSetRow.getInput());

            neuralNetwork.calculate();
            
            double networkTotalOutput = 0;
            for(int i = 0; i < neuralNetwork.getOutputsCount(); i++){
                networkTotalOutput += neuralNetwork.getOutput()[i];
//                data.add(neuralNetwork.getOutput()[i]);
            }
            data.add(MLP.deNormalizedValue(networkTotalOutput / 6, 50000, 1600000));
            
            dataList.add(data);
        }

        Log.printLine("Neural Network Tested");
        ReadWriteExcel.writeDataList(dataList); 
    }
    
    private static void setMAPE(double[] input, double[] desireOutput, double[] predictedOutput){
        double sumDesireOutputs = 0;
        for(int i = 0; i < desireOutput.length; i++){
            sumDesireOutputs += desireOutput[i];
        }
        
        double sumPredictedOutputs = 0;
        for(int i = 0; i < predictedOutput.length; i++){
            sumPredictedOutputs += predictedOutput[i];
        }
        
        
    }
}
