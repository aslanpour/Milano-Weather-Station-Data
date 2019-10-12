package datasetanalysis.filemanipulating;

import autoscalesim.container.history.ExecutorHistory;
import autoscalesim.container.history.AutoScalingHistory;
import autoscalesim.container.history.EndUserHistory;
import autoscalesim.container.history.NNHistory;
import autoscalesim.container.history.VmHistory;
import autoscalesim.container.history.SlaHistory;
import autoscalesim.container.history.AnalyzerHistory;
import autoscalesim.container.history.ThroughputHistory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Created by Mohammad Sadegh Aslanpour on 11/10/2015.
 */
//workBook = WorkbookFactory.create(input)
public class ReadWriteExcel {
    
    public ReadWriteExcel(){
        
    }
    private static String FILE_PATH = "src/others/SimulationResult.xls";
    //We are making use of a single instance to prevent multiple write access to same file.
    private static final ReadWriteExcel INSTANCE = new ReadWriteExcel();
    
    public static ReadWriteExcel getInstance() {
        return INSTANCE;
    }

    /**
     * Write a dataList to a sheet(sheet name is DataList) in Default Path
     * FILE_PATH = "src/others/SimulationResult.xls"
     * @param dataList 
     */
     public static void writeDataList(ArrayList dataList){
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("DataList") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("DataList"));
                
                Sheet dataListSheet = workbook.createSheet("DataList");
                int rowIndex = 0;
                
                for(int i = 0; i < dataList.size(); i++){
                    ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
                    Row row = dataListSheet.createRow(rowIndex++);

                    for(int j = 0; j < data.size(); j++){
                        row.createCell(j).setCellValue(data.get(j));
                    }
                }

                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.println(FILE_PATH + " is successfully written");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }
    
    /**
     * write a DataList to an special sheet in a Default Path
     * FILE_PATH = "src/others/SimulationResult.xls"
     * @param dataList
     * @param sheetName 
     */
    public static void writeDataList(ArrayList dataList, String sheetName){
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet(sheetName) != null)
                    workbook.removeSheetAt(workbook.getSheetIndex(sheetName));
                
                Sheet dataListSheet = workbook.createSheet(sheetName);
                int rowIndex = 0;
                
                for(int i = 0; i < dataList.size(); i++){
                    ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
                    Row row = dataListSheet.createRow(rowIndex++);

                    for(int j = 0; j < data.size(); j++){
                        row.createCell(j).setCellValue(data.get(j));
                    }
                }

                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.println(FILE_PATH + " is successfully written");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
                System.out.println("File is Not In Default Path");
        }
    }
     
    /**
     * Write a list of Clients Behavior
     * @param clientsBehaviorList 
     */
    public static void writeClientHistoryList(ArrayList<EndUserHistory> clientsHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("Clients") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("Clients"));
                
                Sheet clientsBehaviorSheet = workbook.createSheet("Clients");
                int rowIndex = 0;
                
                for(EndUserHistory clientHistory : clientsHistoryList){
                    Row row = clientsBehaviorSheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is Tick
//                    row.createCell(cellIndex++).setCellValue(clientHistory.getTick());
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(clientHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(clientHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(clientHistory.getMinute());

                    // sixth place in row is requests number
                    row.createCell(cellIndex++).setCellValue(clientHistory.getRequestsPerTier()[0]);

                    //seventh place in row is requests length
                    row.createCell(cellIndex++).setCellValue(clientHistory.getRequestsLengthPerTier()[0]);
                    //
                    row.createCell(cellIndex++).setCellValue(clientHistory.getRequestsPerTier()[0]); // web
                    //
                    row.createCell(cellIndex++).setCellValue(clientHistory.getRequestsPerTier()[1]); // app
                    //
                    row.createCell(cellIndex++).setCellValue(clientHistory.getRequestsPerTier()[2]); // db
                    
                    //eighth place in row is day part
//                    row.createCell(cellIndex++).setCellValue(clientHistory.getDayPart());

                }

                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.print(FILE_PATH + " is successfully written   ---   ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }
    
    /**
     * Write a list of Sla Behavior
     * @param slaBehaviorList 
     */
    public static void writeSLAHistoryList(ArrayList<SlaHistory> slaHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);
                
                if(workbook.getSheet("Sla") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("Sla"));
                
                Sheet slaBehaviorSheet = workbook.createSheet("Sla");
                int rowIndex = 0;

                for(SlaHistory slaHistory : slaHistoryList){
                    Row row = slaBehaviorSheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is Tick
//                    row.createCell(cellIndex++).setCellValue(slaHistory.getTick());
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(slaHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(slaHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(slaHistory.getMinute());

                    //
                    row.createCell(cellIndex++).setCellValue(slaHistory.getAvgResponseTime()[0]);
                    
                    // sixth place in row is delay time
                    row.createCell(cellIndex++).setCellValue(slaHistory.getAvgDelayTime()[0]);
                    
                    //
                    row.createCell(cellIndex++).setCellValue(slaHistory.getSLAVNumbersByTier()[0]);
                    
                    // 
                    row.createCell(cellIndex++).setCellValue(slaHistory.getSLAVSecondsByTier()[0]);
                    
                    //
                    row.createCell(cellIndex++).setCellValue(slaHistory.getCloudletsCancelled()[0]);
                    
                    //
                    row.createCell(cellIndex++).setCellValue(slaHistory.getCloudletFailedCounter()[0]);
                    
                }
                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.print(FILE_PATH + " is successfully written   ---   ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }

    public static void writeVmHistoryList(ArrayList<VmHistory> vmHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("Vm") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("Vm"));
                
                Sheet vmsBehaviorSheet = workbook.createSheet("Vm");
                int rowIndex = 0;

                for(VmHistory vmHistory : vmHistoryList){
                    Row row = vmsBehaviorSheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is Tick
//                    row.createCell(cellIndex++).setCellValue(vmHistory.getTick());
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(vmHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(vmHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(vmHistory.getMinute());

                    //
                    row.createCell(cellIndex++).setCellValue(vmHistory.getCpuUtilization()[0]);
                    // sixth place in row is Vms number
                    row.createCell(cellIndex++).setCellValue(vmHistory.getVms()[0]);
                    // 
                    row.createCell(cellIndex++).setCellValue(vmHistory.getInitialingVms()[0]);
                    //
                    row.createCell(cellIndex++).setCellValue(vmHistory.getRunningVms()[0]);
                    //
                    row.createCell(cellIndex++).setCellValue(vmHistory.getQuarantinedVms()[0]);
                    
                    row.createCell(cellIndex++).setCellValue(vmHistory.getRunningCloudlet()[0]);
                    //
//                    row.createCell(cellIndex++).setCellValue(vmHistory.getVmsConfig()[0]);
//                    row.createCell(cellIndex++).setCellValue(vmHistory.getVmsConfig()[1]);
//                    row.createCell(cellIndex++).setCellValue(vmHistory.getVmsConfig()[2]);
//                    row.createCell(cellIndex++).setCellValue(vmHistory.getVmsConfig()[3]);
                }
                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.print(FILE_PATH + " is successfully written   ---   ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }

    public static void writeThroughputHistoryList(ArrayList<ThroughputHistory> throughputHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("Throughput") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("Throughput"));
                
                Sheet throughputHistorySheet = workbook.createSheet("Throughput");
                int rowIndex = 0;

                for(ThroughputHistory throughputHistory : throughputHistoryList){
                    Row row = throughputHistorySheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is Tick
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(throughputHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(throughputHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(throughputHistory.getMinute());

                    //
                    row.createCell(cellIndex++).setCellValue(throughputHistory.getThroughputFinishedCloudlets()[0]);
                    
                    //
                    row.createCell(cellIndex++).setCellValue(throughputHistory.getThroughputFinishedCloudlets()[1]);
                    
                    //
                    row.createCell(cellIndex++).setCellValue(throughputHistory.getThroughputFinishedCloudlets()[2]);
                    //
                    row.createCell(cellIndex++).setCellValue(throughputHistory.getThroughputFinishedCloudletsAllTiers());
                    
                }
                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.print(FILE_PATH + " is successfully written   ---   ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }
    /**
     * 
     * @param analyzerHistoryList 
     */
    public static void writeAnalyzerHistoryList(ArrayList<AnalyzerHistory> analyzerHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("Analyzer") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("Analyzer"));
                
                Sheet analyzerSheet = workbook.createSheet("Analyzer");
                int rowIndex = 0;

                for(AnalyzerHistory analyzerHistory : analyzerHistoryList){
                    Row row = analyzerSheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is day number
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getDayNumber());
                    //first place in row is weekend
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getWeekend());
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getMinute());

                    //
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getCpuUtilization()[0]);
                    
                    //vms
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getVms()[0]);
                    
                    // response time
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getResponseTime()[0]);
                    // sixth place in row is Vms number
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getDelayTime()[0]);
                    
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getSLAViolation()[0]);
                    // sla violation percentage
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getSLAVPercentage()[0]);
                    
                    // sla violation time
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getSLAVTime()[0]);
                    
                    // failed cloudlet
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getFailedCloudlet()[0]);
                    
                    //current user request
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getCurrentUserRequest()[0]);
                    // 
                    for(int j = 0; j < getNeuralNetwork().getOutputsCount(); j++){
                        row.createCell(cellIndex++).setCellValue(analyzerHistory.getFutureUserRequest()[0][j]);
                    }
                    
                    //current user request
                    row.createCell(cellIndex++).setCellValue(analyzerHistory.getThroughput()[0]);
                }
                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.print(FILE_PATH + " is successfully written   ---   ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }
    
    
    public static void writePlannerHistoryList(ArrayList<PlannerHistory> plannerHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("Planner") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("Planner"));
                
                Sheet plannerSheet = workbook.createSheet("Planner");
                int rowIndex = 0;

                for(PlannerHistory plannerHistory : plannerHistoryList){
                    Row row = plannerSheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is Tick
//                    row.createCell(cellIndex++).setCellValue(executorHistory.getTick());
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(plannerHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(plannerHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(plannerHistory.getMinute());

                    //
                    row.createCell(cellIndex++).setCellValue(plannerHistory.getDecision()[0]);
                    // sixth place in row is Vms number
                }
                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.print(FILE_PATH + " is successfully written   ---   ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }
    
    public static void writeExecutorHistoryList(ArrayList<ExecutorHistory> executorHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("Executor") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("Executor"));
                
                Sheet executorSheet = workbook.createSheet("Executor");
                int rowIndex = 0;

                for(ExecutorHistory executorHistory : executorHistoryList){
                    Row row = executorSheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is Tick
//                    row.createCell(cellIndex++).setCellValue(executorHistory.getTick());
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(executorHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(executorHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(executorHistory.getMinute());

                    //
                    row.createCell(cellIndex++).setCellValue(executorHistory.getAction()[0]);
                    // sixth place in row is Vms number
                    row.createCell(cellIndex++).setCellValue(executorHistory.getProvisioning()[0]);
                    // 
                    row.createCell(cellIndex++).setCellValue(executorHistory.getDeProvisioning()[0]);
                }
                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.print(FILE_PATH + " is successfully written   ---   ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }
    
     public static void writeAutoScalingHistory(ArrayList<AutoScalingHistory> autoScalingHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("AutoScaling") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("AutoScaling"));
                
                Sheet resourceManagerBehaviorSheet = workbook.createSheet("AutoScaling");
                int rowIndex = 0;

                for(AutoScalingHistory autoScalingHistory : autoScalingHistoryList){
                    Row row = resourceManagerBehaviorSheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is Tick
//                    row.createCell(cellIndex++).setCellValue(autoScalingHistory.getTick());
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(autoScalingHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(autoScalingHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(autoScalingHistory.getMinute());

                    // sixth place in row is Vms number
                    row.createCell(cellIndex++).setCellValue(autoScalingHistory.getDecision());

                }
                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.println(FILE_PATH + " is successfully written");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }
     
     
    public static void writeNNBehaviorHistorySixOutput(ArrayList<NNHistory> nnHistoryList){
        
        try{
            FileInputStream fis = new FileInputStream(FILE_PATH);
            try{
                Workbook workbook = new HSSFWorkbook(fis);

                if(workbook.getSheet("NN") != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("NN"));
                
                Sheet nnBehaviorSheet = workbook.createSheet("NN");
                int rowIndex = 0;

                for(NNHistory nnHistory : nnHistoryList){
                    Row row = nnBehaviorSheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    //first place in row is Tick
//                    row.createCell(cellIndex++).setCellValue(nnHistory.getTick());
                    //second place in row is Day Number
                    row.createCell(cellIndex++).setCellValue(nnHistory.getDayOfWeek());

                    //third place in row is Hour
                    row.createCell(cellIndex++).setCellValue(nnHistory.getHour());

                    //fourth place in row is Minute
                    row.createCell(cellIndex++).setCellValue(nnHistory.getMinute());

                    // sixth place in row is Vms number
                    row.createCell(cellIndex++).setCellValue(nnHistory.getCurrentLoad());

                    //seventh place in row is Total Computing Power
                    row.createCell(cellIndex++).setCellValue(nnHistory.getAvgPredictedLoad());
                    
                    //eighth place in row is Cpu Utilization 
                    row.createCell(cellIndex++).setCellValue(nnHistory.getPredictedLoad10());
                    
                    row.createCell(cellIndex++).setCellValue(nnHistory.getPredictedLoad20());
                    
                    row.createCell(cellIndex++).setCellValue(nnHistory.getPredictedLoad30());
                    
                    row.createCell(cellIndex++).setCellValue(nnHistory.getPredictedLoad40());
                    
                    row.createCell(cellIndex++).setCellValue(nnHistory.getPredictedLoad50());
                    
                    row.createCell(cellIndex++).setCellValue(nnHistory.getPredictedLoad60());
                    // ninth place in row is Ram Usage
//                    row.createCell(cellIndex++).setCellValue(nnHistory.getRamUsage());
                }
                //write this workbook in excel file.
                try {
                        FileOutputStream fos = new FileOutputStream(FILE_PATH);
                        workbook.write(fos);
                        fos.close();

                        System.out.println(FILE_PATH + " is successfully written");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }catch(FileNotFoundException ex){
                ex.printStackTrace();
        }
    }

    /**
     * Read a Sheet from Default Path
     * @param sheetName
     * @return 
     */
    public static ArrayList readASheet(String sheetName){
        ArrayList dataList =  new ArrayList();
        FileInputStream fis;
        Workbook workbook;
        try {
            fis = new FileInputStream(FILE_PATH);
            workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                ArrayList data = new ArrayList();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    data.add(cell.getNumericCellValue());
                }
                dataList.add(data);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    
    /**
     * Read a Sheet from an special workbook
     * @param sheetName
     * @param filePath
     * @return 
     */
    public static ArrayList readASheet(String sheetName, String filePath){
        ArrayList dataList =  new ArrayList();
        FileInputStream fis;
        Workbook workbook;
        try {
            fis = new FileInputStream(filePath);
            workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                ArrayList data = new ArrayList();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    data.add(cell.getNumericCellValue());
                }
                dataList.add(data);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    
    /**
     * Read a sheet, Normalization, write new Numbers in new sheet and new text file
     * @param sheetName 
     */
    public static void normalizationClientsBehavior(String sheetName){
        double x;
        double minInput;
        double maxInput;
        double minOutput = 0.1;
        double maxOutput = 0.8; // 0.8 + 0.1 ---> Max Output is 0.9
        double normalizedValue;
        
        // Read
        ArrayList dataList =  new ArrayList();
        FileInputStream fis;
        try {
             fis = new FileInputStream(FILE_PATH);
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellIndex = 0;
                ArrayList<Double> data = new ArrayList<Double>();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    x = cell.getNumericCellValue();
                    switch(cellIndex){
                        case 0: // Day Number
                            minInput = 1; maxInput = 7; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 1: // Hour
                            minInput = 0; maxInput = 23; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 2: // Minute
                            minInput = 0; maxInput = 59; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 3: // Requests Count
                            minInput = 12; maxInput = 198; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 4: // Current Requests Length
                            minInput = 77500; maxInput = 1580000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        default: // Future Requests Length
                            minInput = 77500; maxInput = 1580000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                        break;
                    }
                    cellIndex++;
                }
                dataList.add(data);
            }
            System.out.println("Rows: " + dataList.size());
            // Write to Excel
            if(workbook.getSheet("N_" + sheetName) != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("N_" + sheetName));
                
            Sheet normalizedSheet = workbook.createSheet("N_" + sheetName);
            for(int i = 0; i < dataList.size() ; i++){
                Row row = normalizedSheet.createRow(i);
                int cellIndex = 0;
                ArrayList<Double> data = (ArrayList<Double>) dataList.get(i);
                //second place in row is Day Number
                row.createCell(cellIndex++).setCellValue(data.get(0));

                //third place in row is Hour
                row.createCell(cellIndex++).setCellValue(data.get(1));

                //fourth place in row is Minute
                row.createCell(cellIndex++).setCellValue(data.get(2));

                // sixth place in row is requests number
                row.createCell(cellIndex++).setCellValue(data.get(3));

                //seventh place in row is current requests length
                row.createCell(cellIndex++).setCellValue(data.get(4));

                //eighth place in row is future requests length
                row.createCell(cellIndex++).setCellValue(data.get(5));
            }
            //write this workbook in excel file.
            try {
                    FileOutputStream fos = new FileOutputStream(FILE_PATH);
                    workbook.write(fos);
                    fos.close();

                    System.out.println(FILE_PATH + " is successfully written");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write to a txt file
            String txtfileName = "C:/AutoScaleSimFiles/" + "N_" + sheetName + ".txt";
            File file = new File(txtfileName);
            if(file.exists())
                file.delete();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for(int i = 0; i < dataList.size(); i++){
                ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
                printWriter.println(data.get(0) + "," + data.get(1) + "," + data.get(2) + ","
                                    + data.get(3) + "," + data.get(4) + "," + data.get(5));
            }
            printWriter.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read a sheet, Normalization, write new Numbers in new sheet and new text file
     * Input[4]: Hour, Minute, Requests Count, Load
     * OuPut[6]: The Load of 10, 20, 30, 40, 50, 60 Minute Later
     * @param sheetName 
     */
    public static void normalizationClientsBehavior4in_6out(String sheetName){
        double x;
        double minInput;
        double maxInput;
        double minOutput = 0.1;
        double maxOutput = 0.8; // 0.8 + 0.1 ---> Max Output is 0.9
        double normalizedValue;
        
        // Read
        ArrayList dataList =  new ArrayList();
        FileInputStream fis;
        try {
             fis = new FileInputStream(FILE_PATH);
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellIndex = 0;
                ArrayList<Double> data = new ArrayList<Double>();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    x = cell.getNumericCellValue();
                    switch(cellIndex){
                        case 0: // Hour
                            minInput = 0; maxInput = 23; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 1: // Minute
                            minInput = 0; maxInput = 59; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 2: // Requests Coun
                            minInput = 5; maxInput = 200; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 3: // Load
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 4: // Future Load (10 Min)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 5: // Future Load (20 Min)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 6: // Future Load (30 Min)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 7: // Future Load (40 Min)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        default: // Future Load (50 Min)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                    }
                    cellIndex++;
                }
                dataList.add(data);
            }
            System.out.println("Rows of Sheet: " + dataList.size());
            System.out.println("Cells" + ((ArrayList)dataList.get(0)).size());
            // Write to Excel
            if(workbook.getSheet("N_" + sheetName) != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("N_" + sheetName));
                
            Sheet normalizedSheet = workbook.createSheet("N_" + sheetName);
            for(int i = 0; i < dataList.size() ; i++){
                Row row = normalizedSheet.createRow(i);
                int cellIndex = 0;
                ArrayList<Double> data = (ArrayList<Double>) dataList.get(i);
                //Hour
                row.createCell(cellIndex++).setCellValue(data.get(0));

                //Minute
                row.createCell(cellIndex++).setCellValue(data.get(1));

                //Request Count
                row.createCell(cellIndex++).setCellValue(data.get(2));

                // Load
                row.createCell(cellIndex++).setCellValue(data.get(3));

                //Future Load (10 Min)
                row.createCell(cellIndex++).setCellValue(data.get(4));

                //Future Load (20 Min)
                row.createCell(cellIndex++).setCellValue(data.get(5));
                
                //Future Load (30 Min)
                row.createCell(cellIndex++).setCellValue(data.get(6));
                
                //Future Load (40 Min)
                row.createCell(cellIndex++).setCellValue(data.get(7));
                
                //Future Load (50 Min)
                row.createCell(cellIndex++).setCellValue(data.get(8));
                
                //Future Load (60 Min)
                row.createCell(cellIndex++).setCellValue(data.get(9));
            }
            //write this workbook in excel file.
            try {
                    FileOutputStream fos = new FileOutputStream(FILE_PATH);
                    workbook.write(fos);
                    fos.close();

                    System.out.println(FILE_PATH + " is successfully written");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write to a txt file
            String txtfileName = "C:/AutoScaleSimFiles/" + "N_" + sheetName + ".txt";
            DataSet dataSet = new DataSet(4, 6);  //???
            
            File file = new File(txtfileName);
            if(file.exists())
                file.delete();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            for(int i = 0; i < dataList.size(); i++){
                ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
                printWriter.println(data.get(0) + "," + data.get(1) + "," + data.get(2) + ","
                                    + data.get(3) + "," + data.get(4) + "," + data.get(5)
                                    + data.get(6) + "," + data.get(7) + "," + data.get(8) + ","
                                    + data.get(9));
                double[] input = new double[]{data.get(0), data.get(1), data.get(2), data.get(3)}; //???
                double[] output = new double[]{data.get(4), data.get(5), data.get(6), data.get(7), data.get(8), data.get(9)};
                dataSet.addRow(input, output);
            }
            printWriter.close();
            
            dataSet.save("C:/AutoScaleSimFiles/" + sheetName + ".tset");
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
     /**
     * Read a sheet, Normalization, write new Numbers in new sheet and new text file
     * @param sheetName 
     */
    public static void normalizationClientsBehavior5in_6out(String sheetName){
        double x;
        double minInput;
        double maxInput;
        double minOutput = 0.1;
        double maxOutput = 0.8; // 0.8 + 0.1 ---> Max Output is 0.9
        double normalizedValue;
        
        // Read
        ArrayList dataList =  new ArrayList();
        FileInputStream fis;
        try {
             fis = new FileInputStream(FILE_PATH);
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellIndex = 0;
                ArrayList<Double> data = new ArrayList<Double>();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    x = cell.getNumericCellValue();
                    switch(cellIndex){
                        case 0: // Day of Week
                            minInput = 1; maxInput = 7; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 1: // Hour
                            minInput = 0; maxInput = 23; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 2: // Minute
                            minInput = 0; maxInput = 59; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 3: // Requests Count
                            minInput = 5; maxInput = 200; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 4: // Current Requests Length
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 5: // Future Requests Length (Output 10)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 6: // Future Requests Length (Output 20)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 7: // Future Requests Length (Output 30)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 8: // Future Requests Length (Output 40)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        case 9: // Future Requests Length (Output 50)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                        default: // Future Requests Length (Output 60)
                            minInput = 50000; maxInput = 1600000; 
                            normalizedValue = (x - minInput) / (maxInput - minInput) * maxOutput + minOutput;
                            data.add(normalizedValue);
                            break;
                    }
                    cellIndex++;
                }
                dataList.add(data);
            }
            System.out.println("Rows: " + dataList.size());
            System.out.println(((ArrayList)dataList.get(0)).size());
            // Write to Excel
            if(workbook.getSheet("N_" + sheetName) != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("N_" + sheetName));
                
            Sheet normalizedSheet = workbook.createSheet("N_" + sheetName);
            for(int i = 0; i < dataList.size() ; i++){
                Row row = normalizedSheet.createRow(i);
                int cellIndex = 0;
                ArrayList<Double> data = (ArrayList<Double>) dataList.get(i);
                //second place in row is Day of Week
                row.createCell(cellIndex++).setCellValue(data.get(0));

                //third place in row is Hour
                row.createCell(cellIndex++).setCellValue(data.get(1));

                //fourth place in row is Minute
                row.createCell(cellIndex++).setCellValue(data.get(2));

                // sixth place in row is requests number
                row.createCell(cellIndex++).setCellValue(data.get(3));

                //seventh place in row is current requests length
                row.createCell(cellIndex++).setCellValue(data.get(4));

                //eighth place in row is future requests length (Output 10)
                row.createCell(cellIndex++).setCellValue(data.get(5));
                
                //eighth place in row is future requests length (Output 20)
                row.createCell(cellIndex++).setCellValue(data.get(6));
                
                //eighth place in row is future requests length (Output 30)
                row.createCell(cellIndex++).setCellValue(data.get(7));
                
                //eighth place in row is future requests length (Output 40)
                row.createCell(cellIndex++).setCellValue(data.get(8));
                
                //eighth place in row is future requests length (Output 50)
                row.createCell(cellIndex++).setCellValue(data.get(9));
                
                //eighth place in row is future requests length (Output 60)
                row.createCell(cellIndex++).setCellValue(data.get(10));
            }
            //write this workbook in excel file.
            try {
                    FileOutputStream fos = new FileOutputStream(FILE_PATH);
                    workbook.write(fos);
                    fos.close();

                    System.out.println(FILE_PATH + " is successfully written");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write to a txt file
            String txtfileName = "C:/AutoScaleSimFiles/" + "N_" + sheetName + ".txt";
            DataSet dataSet = new DataSet(5, 6);
            
            File file = new File(txtfileName);
            if(file.exists())
                file.delete();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            for(int i = 0; i < dataList.size(); i++){
                ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
                printWriter.println(data.get(0) + "," + data.get(1) + "," + data.get(2) + ","
                                    + data.get(3) + "," + data.get(4) + "," + data.get(5)
                                    + data.get(6) + "," + data.get(7) + "," + data.get(8) + ","
                                    + data.get(9) + "," + data.get(10));
                double[] input = new double[]{data.get(0), data.get(1), data.get(2), data.get(3), data.get(4)};
                double[] output = new double[]{data.get(5), data.get(6), data.get(7), data.get(8), data.get(9), data.get(10)};
                dataSet.addRow(input, output);
            }
            printWriter.close();
            
            dataSet.save("C:/AutoScaleSimFiles/" + sheetName + ".tset");
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read a sheet, DeNormalization, write new Numbers in new sheet and new text file
     * @param sheetName 
     */
    public static void deNormalizationClientsBehavior(String sheetName){
        double y;
        double minInput;
        double maxInput;
        double minOutput = 0.1;
        double maxOutput = 0.8; // 0.8 + 0.1 ---> Max Output is 0.9
        double deNormalizedValue;
        
        // Read
        ArrayList dataList =  new ArrayList();
        FileInputStream fis;
        try {
             fis = new FileInputStream(FILE_PATH);
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellIndex = 0;
                ArrayList<Double> data = new ArrayList<Double>();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    y = cell.getNumericCellValue();
                    switch(cellIndex){
                        case 0: // Day Number
                            minInput = 1; maxInput = 28; 
                            deNormalizedValue = (y - minOutput) * ( maxInput - minInput) / maxOutput + minInput;
                            data.add(deNormalizedValue);
                            break;
                        case 1: // Hour
                            minInput = 0; maxInput = 23; 
                            deNormalizedValue = (y - minOutput) * ( maxInput - minInput) / maxOutput + minInput;
                            data.add(deNormalizedValue);
                            break;
                        case 2: // Minute
                            minInput = 0; maxInput = 50; 
                            deNormalizedValue = (y - minOutput) * ( maxInput - minInput) / maxOutput + minInput;
                            data.add(deNormalizedValue);
                            break;
                        case 3: // Requests Count
                            minInput = 6; maxInput = 99; 
                            deNormalizedValue = (y - minOutput) * ( maxInput - minInput) / maxOutput + minInput;
                            data.add(deNormalizedValue);
                            break;
                        case 4: // Current Requests Length
                            minInput = 32500; maxInput = 795000; 
                            deNormalizedValue = (y - minOutput) * ( maxInput - minInput) / maxOutput + minInput;
                            data.add(deNormalizedValue);
                            break;
                        default: // Future Requests Length
                            minInput = 32500; maxInput = 795000; 
                            deNormalizedValue = (y - minOutput) * ( maxInput - minInput) / maxOutput + minInput;
                            data.add(deNormalizedValue);
                        break;
                    }
                    cellIndex++;
                }
                dataList.add(data);
            }
            System.out.println("Rows: " + dataList.size());
            // Write to Excel
            if(workbook.getSheet("DN_" + sheetName) != null)
                    workbook.removeSheetAt(workbook.getSheetIndex("DN_" + sheetName));
                
            Sheet normalizedSheet = workbook.createSheet("DN_" + sheetName);
            for(int i = 0; i < dataList.size() ; i++){
                Row row = normalizedSheet.createRow(i);
                int cellIndex = 0;
                ArrayList<Double> data = (ArrayList<Double>) dataList.get(i);
                //second place in row is Day Number
                row.createCell(cellIndex++).setCellValue(data.get(0));

                //third place in row is Hour
                row.createCell(cellIndex++).setCellValue(data.get(1));

                //fourth place in row is Minute
                row.createCell(cellIndex++).setCellValue(data.get(2));

                // sixth place in row is requests number
                row.createCell(cellIndex++).setCellValue(data.get(3));

                //seventh place in row is current requests length
                row.createCell(cellIndex++).setCellValue(data.get(4));

                //eighth place in row is future requests length
                row.createCell(cellIndex++).setCellValue(data.get(5));
            }
            //write this workbook in excel file.
            try {
                    FileOutputStream fos = new FileOutputStream(FILE_PATH);
                    workbook.write(fos);
                    fos.close();

                    System.out.println(FILE_PATH + " is successfully written");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write to a txt file
            String txtfileName = "C:/AutoScaleSimFiles/" + "DN_" + sheetName + ".txt";
            File file = new File(txtfileName);
            if(file.exists())
                file.delete();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for(int i = 0; i < dataList.size(); i++){
                ArrayList<Double> data = (ArrayList<Double>)dataList.get(i);
                printWriter.println(data.get(0) + "," + data.get(1) + "," + data.get(2) + ","
                                    + data.get(3) + "," + data.get(4) + "," + data.get(5));
            }
            printWriter.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * File path for saving
     * @param filePath for example: C:/AutoScaleSimFiles/example.xls
     */
    public static void setFilePath(String filePath){
        FILE_PATH = filePath;
    }
  
}
