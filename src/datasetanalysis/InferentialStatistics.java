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

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

/**
 *This class is for performing inferential statistics, especifically Covariance and Correlation.
 * @author aslanpour
 */
public class InferentialStatistics {
    
    /**
     * Perform inferential statistics to find covariance and correlation for two parameters.
     * @param x
     * @param y 
     */
    public static void analyze(double[] x, double[] y){
        Covariance covariance = new Covariance();
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
        
        double covarianceDbl = covariance.covariance(x, y);
        double pearsonsCorrelationDbl = pearsonsCorrelation.correlation(x, y);
        double spearmansCorrelationDbl = spearmansCorrelation.correlation(x, y);
        
        System.out.println("Inferential Statistics Results: \n");
        System.out.println("\nCovariance: " + covarianceDbl + 
                            "\nPearsonsCorrelation: " + pearsonsCorrelationDbl +
                            "\nSpearmansCorrelation: " + spearmansCorrelationDbl);
    }
}
