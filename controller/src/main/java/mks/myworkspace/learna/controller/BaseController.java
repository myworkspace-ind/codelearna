/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.myworkspace.learna.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sakaiproject.component.cover.ComponentManager;
import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.logic.ProjectLogic;
import mks.myworkspace.learna.logic.SakaiProxy;

/**
 * Handles requests for the application home page.
 */
@Slf4j
public class BaseController {

    @Setter
    @Getter
    SakaiProxy sakaiProxy = null;

    @Setter    // Used for Sakai Tool
    @Getter    // Used for Sakai Tool
    ProjectLogic projectLogic;

    @Value("${theme.root}")
    String themeRoot;

    /** Default. */
    final String mimeType = "application/octet-stream";
    final String headerKey = "Content-Disposition";
    
    public String TMP_DIR = System.getProperty("java.io.tmpdir");

    /**
     * Store common data into session.
     * <br/>
     * themeRoot: Root URL of the theme<br/>
     * currentSiteId:<br/>
     * userDisplayName::<br/>
     * userEid::<br/>
     * userEmail:<br/>
     * userFirstName:<br/>
     * userLastName:<br/>
     * @param request Client HTTP request
     * @param httpSession Client HTTP session
     */
    void initSession(HttpServletRequest request, HttpSession httpSession) {
        // Get username when login success
        httpSession.setAttribute("themeRoot", themeRoot);
        
        if (sakaiProxy == null) {
        	sakaiProxy = ComponentManager.get(SakaiProxy.class);
        	log.debug("ComponentManager.get(SakaiProxy.class)=" + sakaiProxy);
        }

        if (sakaiProxy != null) {
            httpSession.setAttribute("currentSiteId", sakaiProxy.getCurrentSiteId());
            httpSession.setAttribute("userDisplayName", sakaiProxy.getCurrentUserDisplayName());
            httpSession.setAttribute("userEid", sakaiProxy.getCurrentUserEid());
            httpSession.setAttribute("userEmail", sakaiProxy.getCurrentUserEmail());
            httpSession.setAttribute("userFirstName", sakaiProxy.getCurrentUserFirstName());
            httpSession.setAttribute("userLastName", sakaiProxy.getCurrentUserLastName());
        } else {
            // Demo for Web App
            httpSession.setAttribute("currentSiteId", "Default");
            httpSession.setAttribute("userDisplayName", "Le Ngoc Thach");
            httpSession.setAttribute("userEid", "ThachLN");
            httpSession.setAttribute("userEmail", "LNThach@gmail.com");
            httpSession.setAttribute("userFirstName", "Thach");
            httpSession.setAttribute("userLastName", "Le Ngoc Thach");            
        }
    }


    public String getCurrentUserEid() {
        return (sakaiProxy != null) ? sakaiProxy.getCurrentUserEid(): "ThachLN";
    }


    public String getCurrentSiteId() {
        return (sakaiProxy != null) ? sakaiProxy.getCurrentSiteId(): "DefaultSite";
    }
    
    public String getCurrentUserEmail() {
        return (sakaiProxy != null) ? sakaiProxy.getCurrentUserEmail(): "lnthach@gmail.com";
    }
    
    public String getCurrentUserDisplayName() {
        return (sakaiProxy != null) ? sakaiProxy.getCurrentUserDisplayName(): "Le Ngoc Thach";
    }

    /**
     * Write the content of the file to HttpServletReponse with file name "fileName".
     * @param file File download
     * @param response some description
     * @param fileName some description
     * @throws IOException If something fails at I/O level.
     */
    protected void writeDownloadContent(File file, HttpServletResponse response, String fileName)
            throws IOException {
        long fileSize = file.length();
        ServletOutputStream outStream = null;
        InputStream fis = null;

        response.setContentType(mimeType);
        response.setContentLength((int) fileSize);
        
        // Set headers for the response.
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);
        
        writeDownloadContent(fis, response, fileName);

    }
    
    protected void writeDownloadContent(InputStream is, HttpServletResponse response, String fileName)
            throws IOException {
        long fileSize = 0;
        ServletOutputStream outStream = null;


        response.setContentType(mimeType);
        
        // Set headers for the response.
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);
        
        // Get output stream of the response
        try {
            outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int length;

            while ((length = is.read(buffer)) > 0) {
                fileSize += length;
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
            
            // response.setContentLength((int) fileSize);
        } catch (IOException ex) {
            log.error("Could not read the attachment content.", ex);
        } finally {
            if (outStream != null) {
                outStream.close();
            } else {
                // Do nothing
            }

            if (is != null) {
                is.close();
            } else {
                // Do nothing
            }
        }
    }
}
