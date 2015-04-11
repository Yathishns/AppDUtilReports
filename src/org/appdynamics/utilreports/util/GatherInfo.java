/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;

import org.appdynamics.appdrestapi.data.MetricDatas;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author gilbert.solorzano
 */
public class GatherInfo implements Runnable{
    protected int threadNum=2;
    protected int threadNumM=2;
    protected int threadNumN=2;
    protected int run=0;
    
    public GatherInfo(){}
    
    public long getRollUpValue(MetricDatas mds){
        
        if(mds != null && mds.getSingleRollUpMetricValue() != null)
            return mds.getSingleRollUpMetricValue().getValue();
        
        return 0;
    }
    
    @Override
    public void run(){}
    
    public void run2(){}
    
    
}
