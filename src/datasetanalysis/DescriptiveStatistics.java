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

/**
 *This class is to perform descriptive statistical analysis on a parameter.
 * @author aslanpour
 */
public class DescriptiveStatistics {
    
    /**
     * Analyze the parameter in terms of descriptive statistics.
     * @param values
     * @param parameterName 
     */
    public static void analyze(double[] values, String parameterName){
        org.apache.commons.math3.stat.descriptive.DescriptiveStatistics descriptiveStat = new org.apache.commons.math3.stat.descriptive.DescriptiveStatistics(values);
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
        
        
        System.out.println("Descriptive Statistics Results: \n");
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
    
    /**
     * Find Mode value for a list of data
     * @param a
     * @return 
     */
    private static int mode(int a[]) {
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
    
    /**
     * Find Mode value for a list of data
     * @param a
     * @return 
     */
    private static double mode(double a[]) {
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
}
