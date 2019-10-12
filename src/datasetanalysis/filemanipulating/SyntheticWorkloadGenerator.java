/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasetanalysis.filemanipulating;

import java.util.Random;
import java.util.ArrayList;

/**
 * This class produces synthetic workload
 * @author Mohammad Sadegh Aslanpour
 */
public class SyntheticWorkloadGenerator {

    public static double i;
    //WeekDays: Monday, Tuesday, Wednesday, Thursday and Friday
    //Weekend: Saturday and Sunday
    // Normal Request Count is between 10 and 99
    
    public static void main(String[] args) {

        ArrayList dataList = new ArrayList();
        int minLength = 2500;  // ???
        int maxLength = 7500;  // ???
        int vmMips = 2500;      // ???
        int requestNumberLevel = 2; // ???
        int weeks = 1 * Const.aWeek; // ???
        boolean initialSmoothing = true; // ???
        int ininitialSmoothingWeek = 2; // ???
        
        int randomLengthBound = (maxLength - minLength) / vmMips + 1;
        
        int requestNumber;
        int maxRequest;
        int minRequest;
        for(i = 0; i < weeks; i += 60){
            ArrayList<Double> data = new ArrayList<Double>();
        //Sets the Bounds(max, min) of random number, with Regard To Day Stage 
            switch (stageOfTheDay(getHour())) {
                // Chooses 1 number between 10 and 99
                case Const.DayBreak:                                            // hours: 0,  1,  2,  3 ,  4
                    minRequest = 30 - ((getHour() / 2) * 10)                            // 30, 30, 20,  20,  10
                                    + (((getHour() + 1) % 2) * 5)                       //+ 5,+ 0,+ 5,+  0,+ 5
                                    + ((getHour() / 4) * ((((getMinute() / 30) + 1) % 2)*5))//+ 0,+ 0,+ 0,+  0,+ 5
                                    - ((getHour() / 4 ) * 5);                           // if hour ==5 -->hour-5

                    maxRequest = 40 - ((getHour() / 2) * 10)                            // 40, 40, 30,  30,  20 
                                    - ((getHour() % 2) * 5)                             //- 0, -5, -0,  -5,  -0
                                    - ((getHour() / 4) * ((getMinute() / 30) * 5));     // -0, -0, -0,  -0, (0,-5)
                    break;
                case Const.MorningBefor7:                                       // hours: 5 ,  6
                    minRequest = 10 + ((getHour() % 5) * 5);                             //   10, 15
                    maxRequest = 20 - ((((getHour() % 5) + 1) % 2) * 5);                 //   15, 20
                    break;
                case Const.MorningAfter7:                                       // hours: 7,  8,  9,  10,  11
                    minRequest = (((getHour() % 6) + 1) * 10)                            //  20, 30, 40,  50,  60
                                    + ((getMinute() / 30) * 5);                          // (0,+5)(0,+5)(0,+5)(0,+5)(0,+5)

                    maxRequest = (((getHour() % 6) + 2) * 10)                            //  30, 40, 50,  60,  70
                                    - ((((getMinute() / 30) + 1) % 2) * 5);              // (-5,0)(-5,0)(-5,0)(-5,0)(-5,0)
                    break;
                case Const.Noon_AfterNoon:                                      // hours: 12, 13, 14, 15, 16 , 17
                    minRequest = 70;                                                     // 70
                    maxRequest = 100;                                                    // 100
                    break;
                case Const.Evening:                                             // hours: 18, 19, 20
                    minRequest = 90 - ((getHour() % 17) * 10)                            //   80, 70, 60
                                    + ((((getMinute() / 30) + 1) % 2) * 5);              //   (+5,0)(+5,0)(+5,0)

                    maxRequest = 90 - (((getHour() % 17) -1) * 10)                       //   70, 60, 50
                                    - ((getMinute() / 30) * 5);                          //   (0,-5)(0,-5)(0,-5)
                    break;
                default: // Night                                                   // hours:  21, 22, 23
                    minRequest = 40 + (((((getHour() % 21) / 2) + 1) % 2) * 10)          //    50, 50, 40
                                    + (((getHour() % 23) % 2) * 5)                       //    +5, 0, 0
                                    + ((getHour() / 23) *  ( (((getMinute() / 30) + 1) % 2) * 5));//0, 0, (+5,0)

                    maxRequest = 60 - (((getHour() % 21) / 2) * 10)                      //    60, 60, 50
                                    - ((( getHour() % 21) % 2) * 5)                      //     0, -5, 0
                                    - ((getHour() / 23) * ((getMinute() / 30) * 5));     //     0,  0, (0,-5)
                    break;
            }

            Random random = new Random();
            
            int bound = maxRequest - minRequest;
            // First Number
            requestNumber = random.nextInt(bound) + minRequest;
            if (requestNumber == maxRequest) Log.print("mosavi");
            // Set Level Number
            requestNumber = requestNumber * requestNumberLevel;
            // Initial Smoothing
//            if(initialSmoothing){
//                if(getDayNumber() == (((ininitialSmoothingWeek -1) * 7) + 1) && getHour() < 5 ){
//                    requestNumber = initialSmoothing(requestNumber);
//                }
//            }
            if(initialSmoothing){
                if(getDayOfWeek() == 1 && getHour() < 5){
                    requestNumber = initialSmoothing(requestNumber);
                }
            }
            // Third Number
            // End of Friday,Start of Saturday, End of Sunday and Start of Monday shoule be smooth,Because of
            // Lower Load on Weekends than WeekDays
            requestNumber = smoothing(requestNumber);
            // Fourth Number
            // The requests in weekend is 2/3 of other days
            if (weekend()){
                requestNumber = Math.round(requestNumber * 2 / 3);
            }
            
            // Save request number of this minute
            data.add((double)getDayNumber());
            
            data.add((double)getDayOfWeek());
            data.add((double)getHour());
            data.add((double)getMinute());
            data.add((double)requestNumber);
            // length
            int totalRequestsLength = 0;
            for(int j = 0; j<requestNumber; j++){   /////////Min and Max length should be Set
                int length = minLength;
                length += random.nextInt(randomLengthBound) * vmMips;
                totalRequestsLength += length; // 5000, 7500 or 10000
            }

            data.add((double)totalRequestsLength);
            
            dataList.add(data);
        }
        ReadWriteExcel.writeDataList(dataList, "W_W1to" + weeks / Const.aWeek + "_" + minLength + "-" + maxLength +"_level" + requestNumberLevel);
    }
    
    private static int initialSmoothing(int requestNumber){
        if(getHour() == 0)
            requestNumber = (int)Math.round(requestNumber * 0.4);
        else if(getHour() == 1)
            requestNumber = (int)Math.round(requestNumber * 0.5);
        else if(getHour() == 2)
            requestNumber = (int)Math.round(requestNumber * 0.6);
        else if(getHour() == 3)
            requestNumber = (int)Math.round(requestNumber * 0.6);
        else if(getHour() == 4)
            requestNumber = (int)Math.round(requestNumber * 0.6);
        
        return requestNumber;
    }
    /**
     * The requests count for weekend should be smooth, because of its sudden downfall and intensity
     * On Friday at night
     * On Saturday at Daybreak
     * on Sunday at night
     * on Monday at Daybreak
     * @param requestNumber
     * @return 
     */
    private static int smoothing(int requestNumber){
        // On Friday at night
        int day =((int)Math.floor(i / Const.aDay)) % 7;
        if(day == Const.Friday){
            if(stageOfTheDay(getHour()) == Const.Night){
                switch(getHour()){
                    case 21:
                        requestNumber -= Math.round(requestNumber * 1 / 20); // - 5%
                        break;
                    case 22:
                        requestNumber -= Math.round(requestNumber * 2 / 20); // - 10%
                        break;
                    case 23:
                        requestNumber -= Math.round(requestNumber * 3 / 20); // - 15%
                }
            }
            return requestNumber;
        }
        
        // On Saturday at DayBreak
        if(day == Const.Saturday){
            if(stageOfTheDay(getHour()) == Const.DayBreak){
                switch(getHour()){
                    case 0:
                        requestNumber += Math.round(requestNumber * 3 / 20); // + 15%
                        break;
                    case 1:
                        requestNumber += Math.round(requestNumber * 2 / 20); // + 10%
                        break;
                    case 2:
                        requestNumber += Math.round(requestNumber * 1 / 20); // + 5%
                }
            }
            return requestNumber;
        }
        
        // On Sunday at night
        if(day == Const.Sunday){
            if(stageOfTheDay(getHour()) == Const.Night){
                switch(getHour()){
                    case 21:
                        requestNumber += Math.round(requestNumber * 1 / 20); // + 5%
                        break;
                    case 22:
                        requestNumber += Math.round(requestNumber * 2 / 20); // + 10%
                        break;
                    case 23:
                        requestNumber += Math.round(requestNumber * 3 / 20); // + 15%
                }
            }
            return requestNumber;
        }
        // On Monday at DayBreak
        if(day == Const.Monday){
            if(stageOfTheDay(getHour()) == Const.DayBreak){
                switch(getHour()){
                    case 0:
                        requestNumber -= Math.round(requestNumber * 3 / 20); // - 15%
                        break;
                    case 1:
                        requestNumber -= Math.round(requestNumber * 2 / 20); // - 10%
                        break;
                    case 2:
                        requestNumber -= Math.round(requestNumber * 1 / 20); // - 5%
                }
            }
            return requestNumber;
        }

        return requestNumber;
    }
    /**
     * Stage of the Day (DayBreak, Morning Before 7, Morning After 7, Noon and AfterNoon, Evening, Night)
     * @param hour
     * @return Stage
     */
    public static int stageOfTheDay (double hour) {
        int stageOfTheDay;
        
        if (hour >= 0 && hour < 5)          // 0, 1, 2, 3, 4      
            stageOfTheDay = Const.DayBreak;
        else if (hour >= 5 && hour < 7)     // 5, 6
            stageOfTheDay = Const.MorningBefor7;
        else if (hour >= 7 && hour < 12)    // 7, 8, 9, 10, 11
            stageOfTheDay = Const.MorningAfter7;
        else if (hour >=12 && hour < 18)    // 12, 13, 14, 15, 16, 17
            stageOfTheDay = Const.Noon_AfterNoon;
        else if (hour >= 18 && hour < 21)   // 18, 19, 20
            stageOfTheDay = Const.Evening;
        else if (hour >=21 && hour <24)     // 21, 22, 23
            stageOfTheDay = Const.Night;
        else {
            stageOfTheDay = 0;
            Log.printLine("Error");
        }
        
        return stageOfTheDay;
    }
    
      
    /**
     * Day Number (starts with 1)
     * @return 
     */
    public static int getDayNumber() {
        return (int)Math.floor(i / Const.aDay) + 1;
    }
    
    /**
     * Current Hour
     * @return 
     */
    public static int getHour () {
        return (int)Math.floor((i % Const.aDay) / (Const.anHour));
    }
     
    /**
     * Current Minute
     * @return 
     */
    public static int getMinute () {
        return (int)Math.floor((i % Const.anHour) / Const.aMinute);
    }
    
    /**
     * Today Name
     * @return 
     */
    public static int getDayOfWeek() {
         return ((int)Math.floor(i / Const.aDay)) % 7 + 1;
    }
    
    public static String getDayNameOfWeek() {
         int dayOfWeek = ((int)Math.floor(i / Const.aDay)) % 7;
         String dayName;
         switch (dayOfWeek){
             case 0:
                 dayName = "Monday";
                 break;
             case 1:
                 dayName = "Thuseday";
                 break;
             case 2:
                 dayName = "Wednesday";
                 break;
             case 3:
                 dayName = "Thursday";
                 break;
             case 4:
                 dayName = "Friday";
                 break;
             case 5:
                 dayName = "Saturday";
                 break;
             case 6:
                 dayName = "Sunday";
                 break;
             default:
                 dayName = "Error";
                 break;
         }
         return dayName;
    }
    
    public static boolean weekend(){
        int dayOfWeek = ((int)Math.floor(i / Const.aDay)) % 7;
        if(dayOfWeek == Const.Saturday || dayOfWeek == Const.Sunday)
            return true;
        else
            return false;
    }
}
