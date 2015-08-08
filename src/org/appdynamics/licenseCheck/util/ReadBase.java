/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author gilbert.solorzano
 */
public class ReadBase {
    
    public static ArrayList<BaseEntry> getBaseObjects(String filePath) throws Exception{
        ArrayList<BaseEntry> entries = new ArrayList<BaseEntry>();
     
        java.io.BufferedReader br=new java.io.BufferedReader(new FileReader(filePath));

        String s;
        String[] parsed;
        while((s= br.readLine()) != null){
            parsed = s.split(",");//System.out.println("Worked with " + parsed[0] + " " + parsed[1] + "  " + parsed[2] + ", size " +parsed.length);
            if(parsed.length == 4) {
                //System.out.println("Creating entry.");
                //entries.add(new BaseEntry(parsed[0],parsed[1],Integer.getInteger(parsed[2])));
                entries.add(new BaseEntry(parsed[0],parsed[1],new Integer(parsed[2]), new Integer(parsed[3]) ));
            }
        }
            
   
        return entries;
    } 
    
    /* 
      This is going to create a new object by parsing the string, if we find that the format of the string
     is not correct we are going to throw a new exception because the format is not correct.
    
    */
    private static BaseEntry createBaseEntryFromString(String row) throws Exception{
        BaseEntry base = new BaseEntry();
        
        
        return base;
    }
    
}
