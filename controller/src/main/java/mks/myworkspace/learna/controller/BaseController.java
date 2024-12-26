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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.authz.impl.BaseRole;
import org.sakaiproject.component.cover.ComponentManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

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

    @Value("${loginUrl}")
    String loginUrl;

    @Value("${logoutUrl}")
    String logoutUrl;

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
            httpSession.setAttribute("roles", sakaiProxy.getRoles());
            log.debug("Roles of user {}: {}", sakaiProxy.getCurrentUserEid(), sakaiProxy.getRoles());
        } else {
            // Demo for Web App
            String username = getCurrentUserEid();
            List<Role> userRoles = getUserRolesBySpring();

            httpSession.setAttribute("currentSiteId", "DefaultSite");
            httpSession.setAttribute("userDisplayName", "Lê Ngọc Thạch");
            httpSession.setAttribute("userEid", username);
            httpSession.setAttribute("userEmail", "ThachLN@mgail.com");
            httpSession.setAttribute("userFirstName", "Thạch");
            httpSession.setAttribute("userLastName", "Lê");
            httpSession.setAttribute("roles", userRoles);
            httpSession.setAttribute("loginUrl", loginUrl);
            httpSession.setAttribute("logoutUrl", logoutUrl);

            log.debug("Roles of user {}: {}", username, userRoles);
        }
    }


    public String getCurrentUserEid() {
        if (sakaiProxy != null) {
            return sakaiProxy.getCurrentUserEid();
        } else {
            String username = getUserIdentifierBySpring();
            return (username != null) ? username : "demouser";
        }
    }

    public List<Role> getCurrentUserRoles() {
        List<Role> roles;

        if (sakaiProxy != null) {
            roles = sakaiProxy.getRoles();
        } else {
            roles = getUserRolesBySpring();
            
            if (roles == null) {
                roles = new ArrayList<Role>(1);
                roles.add(new BaseRole("ROLE_USER"));
            }
        }
        
        return roles;
    }

    public String getCurrentSiteId() {
        return (sakaiProxy != null) ? sakaiProxy.getCurrentSiteId(): "DefaultSite";
    }
    
    public String getCurrentUserEmail() {
        return (sakaiProxy != null) ? sakaiProxy.getCurrentUserEmail(): "ThachLN@gmail.com";
    }
    
    public String getCurrentUserDisplayName() {
        return (sakaiProxy != null) ? sakaiProxy.getCurrentUserDisplayName(): "Lê Ngọc Thạch";
    }

    /**
     * Get authenticated identifier by spring.
     * @return account name.
     */
    public String getUserIdentifierBySpring() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            
            if (principal instanceof User) {
                User user = (User) principal;
                return user.getUsername();
            } else if (principal instanceof AttributePrincipal) {
                AttributePrincipal attrPrincipal = (AttributePrincipal) principal;
                return attrPrincipal.getName();
            }
        }

        return null;
    }

    /**
     * Get user roles by Spring.
     * @return
     */
    public List<Role> getUserRolesBySpring() {
        List<Role> roles = new ArrayList<Role>();
        // Get the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Ensure the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Retrieve roles (authorities)
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Print roles
            BaseRole role;
            log.debug("User Roles:");
            for (GrantedAuthority authority : authorities) {
                role = new BaseRole(authority.getAuthority());
                roles.add(role);
                log.debug(authority.getAuthority());
            }
            
            return roles;
        } else {
            log.debug("No authenticated user.");
        }
        
        return null;
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
