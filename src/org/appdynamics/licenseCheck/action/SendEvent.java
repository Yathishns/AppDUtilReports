/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.action;

import org.appdynamics.appdrestapi.*;
import org.appdynamics.appdrestapi.util.PostEvent;
import org.appdynamics.licenseCheck.resources.LicenseCheckS;

/**
 *
 * @author gilbert.solorzano
 */
public class SendEvent {
    
    public static void send(RESTAccess access, String application, String summary){
        PostEvent post=new PostEvent(summary, LicenseCheckS.REST_COMMENT,LicenseCheckS.REST_TYPE);
        access.postRESTCustomEvent(application, post);
    }
}
