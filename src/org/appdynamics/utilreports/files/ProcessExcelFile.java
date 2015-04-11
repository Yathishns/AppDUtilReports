/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.files;

import org.appdynamics.appdrestapi.data.BusinessTransaction;
import org.appdynamics.utilreports.resources.AppDUtilReportS;
import org.appdynamics.utilreports.util.*;
import org.appdynamics.utilreports.conf.*;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author gilbert.solorzano
 */
public class ProcessExcelFile {
    private static Logger logger=Logger.getLogger(ProcessExcelFile.class.getName());
    private ArrayList<GatherLoadCheck> loadChecks=new ArrayList<GatherLoadCheck>();
    private String fileName;
    
    public ProcessExcelFile(ArrayList<GatherLoadCheck> loadChecks, String fileName){
        this.loadChecks=loadChecks;
        this.fileName=fileName;
    }
    
    public void init(){
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        //logger.log(Level.INFO,"Start excel sheet.");
        for(GatherLoadCheck glc:loadChecks){
            //String app=glc.getLc().getApplication();
            //Create a blank sheet
            for(LoadCheck lc:glc.getLoadCheckList()){
                //logger.log(Level.INFO,new StringBuilder().append("Sheet Name ").append(lc.getSheetName()).append(", ").append(lc.getAppName()).toString());
                XSSFSheet _sheet = workbook.createSheet(new StringBuilder().append(lc.getSheetName()).append(AppDUtilReportS._U).append(lc.getAppId()).toString());
                processCheck(_sheet,lc);
            }


        }
        
        try
        {
            //Write the workbook in file system
            //String fileName=new StringBuilder().append("/Users/gilbert.solorzano/Documents/").append(customer.getName()).append("LicenseFile.xlsx").toString();
            
            FileOutputStream out = new FileOutputStream(fileName);
            workbook.write(out);
            out.close();
            

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    private void processCheck(XSSFSheet xsf, LoadCheck lc){
        
        //First row is the application
        int rowIndex=0;int cellIndex=0;
        Row headerRow = xsf.createRow(rowIndex);
        Cell cell_0 = headerRow.createCell(cellIndex);cell_0.setCellValue(new StringBuilder().append(AppDUtilReportS.APPLICATION_EQ).append(lc.getAppName()).append(" (id=").append(lc.getAppId()).append(")").toString());
        cellIndex++;
        Cell cell_1 ;
        
        rowIndex+=2;
        headerRow = xsf.createRow(rowIndex);
        //Second row is the header
        cellIndex=0;
        cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(AppDUtilReportS.TIME_RANGE);
        cellIndex++;
        cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(lc.getHeader());
        cellIndex++;
        Cell cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(AppDUtilReportS.REQUEST_COUNTS);
        Cell cell_3;
        if(lc.getMetricIndex()==5){
            cellIndex++;
            cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue("Tier Name");
        }
        cellIndex=0;
        
        for(CheckAll check:lc.getChecks()){
            for(QInfo info:check.getMyList()){
                rowIndex++;
                headerRow = xsf.createRow(rowIndex);
                cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(check.getName());
                cellIndex++;
                cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(info.getName());
                cellIndex++;
                cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(info.getValue());
                cellIndex++;
                if(lc.getMetricIndex()==5){
                    cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue(info.getTierName());
                }
                cellIndex=0;
            }
            
            
        }
        for(QInfo info:lc.getBase()){
                if(!info.isPassed()){ //this are the badones.
                rowIndex++;
                headerRow = xsf.createRow(rowIndex);
                cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue("Past the Time");
                cellIndex++;
                cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(info.getName());
                cellIndex++;
                cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(info.getValue());
                cellIndex++;
                if(lc.getMetricIndex()==5){
                    cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue(info.getTierName());
                }
                cellIndex=0;
                }
            
        }
        
    }
    private void processBT(XSSFSheet xsf, GatherBTInfo bt, String app){
        //First row is the application
        int rowIndex=0;int cellIndex=0;
        Row headerRow = xsf.createRow(rowIndex);
        Cell cell_0 = headerRow.createCell(cellIndex);cell_0.setCellValue(new StringBuilder().append(AppDUtilReportS.APPLICATION_EQ).append(app).toString());
        cellIndex++;
        Cell cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(new StringBuilder()
                .append("BT query end time ").append(getDate(bt.getEnd())).toString());
        
        rowIndex+=2;
        headerRow = xsf.createRow(rowIndex);
        //Second row is the header
        cellIndex=0;
        cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(AppDUtilReportS.TIME_RANGE);
        cellIndex++;
        cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue("Business Transaction Name");
        cellIndex++;
        Cell cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(AppDUtilReportS.REQUEST_COUNTS);
        cellIndex++;
        Cell cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue("Tier Name");
        
        cellIndex=0;
        
        rowIndex++;
        //Now we start putting down the data
        String hourRange = AppDUtilReportS.LAST_4_HOURS;
        for(BusinessTransaction dBt:bt.get4HourBT().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt.getName());
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(bt.get4HourBT().get(dBt));
            cellIndex++;
            cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue(dBt.getTierName());
            cellIndex=0;
        }
        
        hourRange = AppDUtilReportS.LAST_24_HOURS;
        for(BusinessTransaction dBt:bt.get24HourBT().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt.getName());
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(bt.get24HourBT().get(dBt));
            cellIndex++;
            cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue(dBt.getTierName());
            cellIndex=0;
        }
        
        hourRange = AppDUtilReportS.LAST_48_HOURS;
        for(BusinessTransaction dBt:bt.get48HourBT().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt.getName());
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(bt.get48HourBT().get(dBt));
            cellIndex++;
            cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue(dBt.getTierName());
            cellIndex=0;
        }
        //We are going to create row 0 first 
        hourRange = AppDUtilReportS.NORMAL;
        for(BusinessTransaction dBt:bt.getNormalBT().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt.getName());
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(bt.getNormalBT().get(dBt));
            cellIndex++;
            cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue(dBt.getTierName());
            cellIndex=0;
        }
    }
    
    private void processBE(XSSFSheet xsf, GatherBKInfo bk, String app){
        //First row is the application
        int rowIndex=0;int cellIndex=0;
        Row headerRow = xsf.createRow(rowIndex);
        Cell cell_0 = headerRow.createCell(cellIndex);cell_0.setCellValue(new StringBuilder().append(AppDUtilReportS.APPLICATION_EQ).append(app).toString());
        cellIndex++;
        Cell cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(new StringBuilder()
                .append("BE query end time ").append(getDate(bk.getEnd())).toString());
        
        rowIndex+=2;
        headerRow = xsf.createRow(rowIndex);
        //Second row is the header
        cellIndex=0;
        cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(AppDUtilReportS.TIME_RANGE);
        cellIndex++;
        cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue("Backend Name");
        cellIndex++;
        Cell cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(AppDUtilReportS.REQUEST_COUNTS);
        
        cellIndex=0;
        
        rowIndex++;
        //Now we start putting down the data
        String hourRange = AppDUtilReportS.LAST_4_HOURS;
        for(String dBt:bk.get4HourBK().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt);
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(bk.get4HourBK().get(dBt));
            cellIndex=0;
        }
        
        hourRange = AppDUtilReportS.LAST_24_HOURS;
        for(String dBt:bk.get24HourBK().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt);
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(bk.get24HourBK().get(dBt));
            cellIndex=0;
        }
        
        hourRange = AppDUtilReportS.LAST_48_HOURS;
        for(String dBt:bk.get48HourBK().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt);
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(bk.get48HourBK().get(dBt));
            cellIndex=0;
        }
        //We are going to create row 0 first
        hourRange = AppDUtilReportS.NORMAL;
        for(String dBt:bk.getNormalBK().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt);
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(bk.getNormalBK().get(dBt));
            cellIndex=0;
        }
    }
    
    private void processEUM(XSSFSheet xsf, GatherEUMInfo eum, String app){
        //First row is the application
        int rowIndex=0;int cellIndex=0;
        Row headerRow = xsf.createRow(rowIndex);
        Cell cell_0 = headerRow.createCell(cellIndex);cell_0.setCellValue(new StringBuilder().append(AppDUtilReportS.APPLICATION_EQ).append(app).toString());
        cellIndex++;
        Cell cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(new StringBuilder()
                .append("EUM Query end time ").append(getDate(eum.getEnd())).toString());
        
        rowIndex+=2;
        headerRow = xsf.createRow(rowIndex);
        //Second row is the header
        cellIndex=0;
        cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(AppDUtilReportS.TIME_RANGE);
        cellIndex++;
        cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(new StringBuilder().append(eum.getName()).append(" Name").toString());
        cellIndex++;
        Cell cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(AppDUtilReportS.REQUEST_COUNTS);
        
        cellIndex=0;
        
        rowIndex++;
        //Now we start putting down the data
        String hourRange = AppDUtilReportS.LAST_4_HOURS;
        for(String dBt:eum.get4HourEUM().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt);
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(eum.get4HourEUM().get(dBt));
            cellIndex=0;
        }
        
        hourRange = AppDUtilReportS.LAST_24_HOURS;
        for(String dBt:eum.get24HourEUM().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt);
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(eum.get24HourEUM().get(dBt));
            cellIndex=0;
        }
        
        hourRange = AppDUtilReportS.LAST_48_HOURS;
        for(String dBt:eum.get48HourEUM().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt);
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(eum.get48HourEUM().get(dBt));
            cellIndex=0;
        }
        //We are going to create row 0 first
        hourRange = AppDUtilReportS.NORMAL;
        for(String dBt:eum.getNormalEUM().keySet()){
            rowIndex++;
            headerRow = xsf.createRow(rowIndex);
            cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(hourRange);
            cellIndex++;
            cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(dBt);
            cellIndex++;
            cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(eum.getNormalEUM().get(dBt));
            cellIndex=0;
        }
    }
    
    private String getDate(long end){
        java.util.Calendar cal=java.util.Calendar.getInstance();
        cal.setTimeInMillis(end);
        return cal.getTime().toString();
    }
    
    private String getHourRange(long end, int jump){
        
        java.util.Calendar cal=java.util.Calendar.getInstance();
        cal.setTimeInMillis(end);
        cal.add(java.util.Calendar.HOUR, jump);
        StringBuilder bud = new StringBuilder().append(cal.getTime().toString()).append(" thru ");
        cal.setTimeInMillis(end);
        bud.append(cal.getTime().toString());
        
        return bud.toString();
        
    }
}
