package datasetanalysis.filemanipulating;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;
import java.util.ArrayList;
 
public class ReadWriteTextFile {
 
    public ArrayList readFile(String filePath){
        try{
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            String line = null;
            ArrayList dataList = new ArrayList();
            while((line = bufferReader.readLine()) != null){
                char[] ch = line.toCharArray();
                ArrayList<Double> data = new ArrayList<>();
                String x = null;
                for( int i = 0; i < ch.length; i++){
                    
                    if(ch[i] != ','){
                        x += ch[i];
                    }else{
                        data.add(Double.valueOf(x));
                        x = null;
                    }
                }
                dataList.add(data);
            }
            
            bufferReader.close();
            return dataList;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static void writeLines(ArrayList dataList, String filePath, String fileName) {
        try {
            ArrayList<String> tmpList = new ArrayList<>();
            boolean arrived = false;
            
            File file = new File(filePath + fileName + ".txt");
                file.delete();
            file.createNewFile();
            // Create FileReader Object
            FileWriter fileWriter = new FileWriter(file);
            
            // Create Buffered/PrintWriter Objects
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            //PrintWriter outputStream = new PrintWriter(new FileWriter("ReadWriteTextFile.out"));
            for (Object dataList1 : dataList) {
                String line = (String) dataList1;
                if(line.contains("[30:15:51:57]")){
                    arrived = true;
                }
                if(arrived == false){
                    printWriter.println(line);
                }else{
                    tmpList.add(line);
                    if(line.contains("[30:15:51:56]")){
                        printWriter.println(tmpList.get(6));
                        printWriter.println(tmpList.get(0));
                        printWriter.println(tmpList.get(1));
                        printWriter.println(tmpList.get(2));
                        printWriter.println(tmpList.get(3));
                        printWriter.println(tmpList.get(4));
                        printWriter.println(tmpList.get(5));
                        arrived = false;
                    }
                }
                
            }
            printWriter.close();
        } catch (IOException e) {
            System.out.println("IOException:");
            e.printStackTrace();
        }

    }
    
    public static void write(ArrayList dataList, String filePath) {

        try {
            File file = new File(filePath);
            if(file.exists())
                file.delete();
            file.createNewFile();
            // Create FileReader Object
            FileWriter fileWriter = new FileWriter(file);
            
            // Create Buffered/PrintWriter Objects
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            //PrintWriter outputStream = new PrintWriter(new FileWriter("ReadWriteTextFile.out"));
            double input1;double input2; double input3; double input4; double input5; double output1;
            
            for(int i = 0; i <dataList.size(); i++){
                ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
                input1 = data.get(0);
                input2 = data.get(1);
                input3 = data.get(2);
                input4 = data.get(3);
                input5 = data.get(4);
                output1 = data.get(5);
                printWriter.println(input1 + ',' + input2 + ',' + input3 + ',' + input4 + ',' + input5 + ',' + output1);
            }
            
            printWriter.close();

            } catch (IOException e) {

                System.out.println("IOException:");
                e.printStackTrace();
            }

        }

}