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

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *This class is to find a linear relationship between two parameter using Regression.
 * @author aslanpour
 */
public class Regression {
    
    /**
     * Find the linear relationship between the two parameters.
     * @param x 
     * @param y 
     */
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
}
