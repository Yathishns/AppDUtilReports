/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.logging;

import java.util.Date;
import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author gilbert.solorzano
 */
public final class JLog_Formatter extends Formatter{
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    
    @Override
    public String format(LogRecord record){
        StringBuilder bud=new StringBuilder();
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(record.getMillis());
        
        bud.append(cal.getTime()).append(" ").append(formatMessage(record)).append(LINE_SEPARATOR);
        
        return bud.toString();
    }
}
