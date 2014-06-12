/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports;

import org.appdynamics.utilreports.conf.*;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashSet;

/**
 *
 * @author gilbert.solorzano
 */
public class TestXMLRead {
    public static void main(String[] args){
        System.out.println("Starting");
        BufferedInputStream configFile = null;
        String filePath="/Users/gilbert.solorzano/BaseCode/AppDUtilReports/src/org/appdynamics/utilreports/conf/HCExample.xml";
        try {

            
            System.out.println("Calling the configinfo...");
            ReadConfig cfg = new ReadConfig(filePath);
            System.out.println(cfg.getReport().toString());

        }
        catch (Exception e) {
            System.out.println("Exception occurred " + e.getMessage());
        }
        finally {
           try{ configFile.close();}catch(Exception e){}
        }
        
        
        
    }
}
