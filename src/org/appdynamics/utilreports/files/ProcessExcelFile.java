/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.files;

import org.appdynamics.appdrestapi.data.BusinessTransaction;
import org.appdynamics.utilreports.resources.AppDUtilReportS;
import org.appdynamics.utilreports.util.*;


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
    private ArrayList<GatherLoadCheck> loadChecks=new ArrayList<GatherLoadCheck>();
    private String fileName;
    
    public ProcessExcelFile(ArrayList<GatherLoadCheck> loadChecks, String fileName){
        this.loadChecks=loadChecks;
        this.fileName=fileName;
    }
    
    public void init(){
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        for(GatherLoadCheck glc:loadChecks){
            String app=glc.getLc().getApplication();
            //Create a blank sheet
            if(glc.getBt() != null){
                XSSFSheet btSummary = 
                        workbook.createSheet(new StringBuilder().append(AppDUtilReportS.BT_CHECK).append("_").append(app).toString());
                processBT(btSummary,glc.getBt(),app);
            }
            if(glc.getBk() != null){
                XSSFSheet beSummary = 
                        workbook.createSheet(new StringBuilder().append(AppDUtilReportS.BE_CHECK).append("_").append(app).toString());
                processBE(beSummary,glc.getBk(),app);
            }
            for(GatherEUMInfo eum: glc.getEum()){
                XSSFSheet eumSummary = 
                        workbook.createSheet(new StringBuilder().append(eum.getName()).append("_").append(app).toString());
                processEUM(eumSummary,eum,app);
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
    
    private void processBT(XSSFSheet xsf, GatherBTInfo bt, String app){
        //First row is the application
        int rowIndex=0;int cellIndex=0;
        Row headerRow = xsf.createRow(rowIndex);
        Cell cell_0 = headerRow.createCell(cellIndex);cell_0.setCellValue(new StringBuilder().append("Appliation=").append(app).toString());
        cellIndex++;
        Cell cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(new StringBuilder()
                .append("BT query end time ").append(getDate(bt.getEnd())).toString());
        
        rowIndex+=2;
        headerRow = xsf.createRow(rowIndex);
        //Second row is the header
        cellIndex=0;
        cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue("Time Ranage");
        cellIndex++;
        cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue("Business Transaction Name");
        cellIndex++;
        Cell cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue("Request Couns");
        cellIndex++;
        Cell cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue("Tier Name");
        
        cellIndex=0;
        
        rowIndex++;
        //Now we start putting down the data
        String hourRange = "Last 4 Hours";
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
        
        hourRange = "Last 24 Hours";
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
        
        hourRange = "Last 48 Hours";
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
    }
    
    private void processBE(XSSFSheet xsf, GatherBKInfo bk, String app){
        //First row is the application
        int rowIndex=0;int cellIndex=0;
        Row headerRow = xsf.createRow(rowIndex);
        Cell cell_0 = headerRow.createCell(cellIndex);cell_0.setCellValue(new StringBuilder().append("Appliation=").append(app).toString());
        cellIndex++;
        Cell cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(new StringBuilder()
                .append("BE query end time ").append(getDate(bk.getEnd())).toString());
        
        rowIndex+=2;
        headerRow = xsf.createRow(rowIndex);
        //Second row is the header
        cellIndex=0;
        cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue("Time Ranage");
        cellIndex++;
        cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue("Backend Name");
        cellIndex++;
        Cell cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue("Request Couns");
        
        cellIndex=0;
        
        rowIndex++;
        //Now we start putting down the data
        String hourRange = "Last 4 Hours";
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
        
        hourRange = "Last 24 Hours";
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
        
        hourRange = "Last 48 Hours";
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
    }
    
    private void processEUM(XSSFSheet xsf, GatherEUMInfo eum, String app){
        //First row is the application
        int rowIndex=0;int cellIndex=0;
        Row headerRow = xsf.createRow(rowIndex);
        Cell cell_0 = headerRow.createCell(cellIndex);cell_0.setCellValue(new StringBuilder().append("Appliation=").append(app).toString());
        cellIndex++;
        Cell cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(new StringBuilder()
                .append("EUM Query end time ").append(getDate(eum.getEnd())).toString());
        
        rowIndex+=2;
        headerRow = xsf.createRow(rowIndex);
        //Second row is the header
        cellIndex=0;
        cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue("Time Ranage");
        cellIndex++;
        cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(new StringBuilder().append(eum.getName()).append(" Name").toString());
        cellIndex++;
        Cell cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue("Request Couns");
        
        cellIndex=0;
        
        rowIndex++;
        //Now we start putting down the data
        String hourRange = "Last 4 Hours";
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
        
        hourRange = "Last 24 Hours";
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
        
        hourRange = "Last 48 Hours";
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
