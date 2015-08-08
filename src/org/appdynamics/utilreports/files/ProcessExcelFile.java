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
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.CellStyle;


import java.util.logging.Logger;
import java.util.logging.Level;
import org.apache.poi.ss.usermodel.IndexedColors;


/**
 *
 * @author gilbert.solorzano
 */
public class ProcessExcelFile {
    private static Logger logger=Logger.getLogger(ProcessExcelFile.class.getName());
    private ArrayList<GatherLoadCheck> loadChecks=new ArrayList<GatherLoadCheck>();
    private String fileName;
    private CellStyle hyperLinkStyle;
    
    public ProcessExcelFile(ArrayList<GatherLoadCheck> loadChecks, String fileName){
        this.loadChecks=loadChecks;
        this.fileName=fileName;
    }
    
    public void init(){
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet tableContents = workbook.createSheet("Table Of Contents");
        //logger.log(Level.INFO,"Start excel sheet.");
        int count=0;
        
        for(GatherLoadCheck glc:loadChecks){
            //String app=glc.getLc().getApplication();
            //Create a blank sheet
            
            for(LoadCheck lc:glc.getLoadCheckList()){
                //logger.log(Level.INFO,new StringBuilder().append("Sheet Name ").append(lc.getSheetName()).append(", ").append(lc.getAppName()).toString());
                XSSFSheet _sheet = workbook.createSheet(new StringBuilder().append(lc.getSheetName()).append(AppDUtilReportS._U).append(lc.getAppId()).toString());
                processCheck(_sheet,lc);
                //
                addToTableOfContents(tableContents,lc,count);
                count++;
            }


        }
        tableContents.autoSizeColumn(1);
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
    
    private void setUpHyperLink(XSSFWorkbook wb){
        Font hFont=wb.createFont();
        hFont.setUnderline(Font.U_SINGLE);
        hFont.setColor(IndexedColors.BLUE.getIndex());
        hyperLinkStyle=wb.createCellStyle();
        hyperLinkStyle.setFont(hFont);
    }
    private void addToTableOfContents(XSSFSheet xsf, LoadCheck lc, int row){
      
        if(row == 0){
            //Create the header
            Row headerRow = xsf.createRow(0);
            Cell cell_ =headerRow.createCell(1);cell_.setCellValue("Application Name - [Check Type]");
            setUpHyperLink(xsf.getWorkbook());
        }
        int newRow = row + 1;
        
        Row headerRow = xsf.createRow(newRow);
        //Create the number
        headerRow.createCell(0).setCellValue(newRow);
        
        Cell cell_=headerRow.createCell(1);
        
        CreationHelper createHelper = xsf.getWorkbook().getCreationHelper();
        Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
        link.setAddress(new StringBuilder().append("'").append(lc.getSheetName()).append(AppDUtilReportS._U).append(lc.getAppId()).append("'!A1").toString());
        //link.setLabel(new StringBuilder().append(lc.getSheetName()).append(AppDUtilReportS._U).append(lc.getAppName()).toString());
        cell_.setCellValue(new StringBuilder().append(lc.getAppName()).append(" - [").append(lc.getSheetName()).append("]").toString());
        cell_.setHyperlink(link);
        cell_.setCellStyle(hyperLinkStyle);
    }
    
    private void processCheck(XSSFSheet xsf, LoadCheck lc){
        
        //First row is the application
        int rowIndex=0;int cellIndex=0;
        Row headerRow = xsf.createRow(rowIndex);
        Cell cell_0 = headerRow.createCell(cellIndex);
        cell_0.setCellValue(new StringBuilder().append(AppDUtilReportS.APPLICATION_EQ).append(lc.getAppName()).append(" (id=").append(lc.getAppId()).append(")").toString());
        cellIndex++;
        Cell cell_1 ;
        
        rowIndex+=1;
        headerRow = xsf.createRow(rowIndex);
        //Second row is the header
        cellIndex=0;
        cell_0=headerRow.createCell(cellIndex);cell_0.setCellValue(AppDUtilReportS.TIME_RANGE);
        cellIndex++;
        cell_1 = headerRow.createCell(cellIndex);cell_1.setCellValue(lc.getHeader());
        cellIndex++;
        Cell cell_2 = headerRow.createCell(cellIndex);cell_2.setCellValue(AppDUtilReportS.REQUEST_COUNTS);
        Cell cell_3;
        Cell cell_4;
        if(lc.getMetricIndex()==5){
            cellIndex++;
            cell_3 = headerRow.createCell(cellIndex);cell_3.setCellValue("Tier Name");
            cellIndex++;
            cell_4 = headerRow.createCell(cellIndex);cell_4.setCellValue("Type");
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
                    cellIndex++;
                    cell_4 = headerRow.createCell(cellIndex);cell_4.setCellValue(info.getType());
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
                    cellIndex++;
                    cell_4 = headerRow.createCell(cellIndex);cell_3.setCellValue(info.getType());
                }
                cellIndex=0;
                }
            
        }
        
        for(int i =0; i < 4;i++)
            xsf.autoSizeColumn(i);
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
