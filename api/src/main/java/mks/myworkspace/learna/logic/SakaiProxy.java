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

package mks.myworkspace.learna.logic;

import java.util.List;

import org.sakaiproject.authz.api.Role;

/**
 * An interface to abstract all Sakai related API calls in a central method that can be injected into our app.
 * 
 * @author Mike Jennings (mike_jennings@unc.edu), Thacch Ngoc Le (ThachLN@mks.com.vn)
 *
 */
public interface SakaiProxy {

    /**
     * Get current siteid
     * @return
     */
    String getCurrentSiteId();
    
    /**
     * Get current user id
     * @return
     */
    String getCurrentUserId();
    
    /**
     * Get current user display name
     * @return
     */
    String getCurrentUserDisplayName();
    
    /**
     * Get username of authenticated user.
     * @return
     */
    String getCurrentUserEid();
    
    /**
     * Get email address of authenticated user.
     * @return
     */
    String getCurrentUserEmail();
    
    /**
     * Get first name of authenticated user.
     * @return
     */
    String getCurrentUserFirstName();
    
    /**
     * Get last name of authenticated user.
     * @return
     */
    String getCurrentUserLastName();
    
    /**
     * Is the current user a superUser? (anyone in admin realm)
     * @return
     */
    boolean isSuperUser();
    
    /**
     * Post an event to Sakai
     * 
     * @param event         name of event
     * @param reference     reference
     * @param modify        true if something changed, false if just access
     * 
     */
    void postEvent(String event,String reference,boolean modify);
    
    /**
     * Wrapper for ServerConfigurationService.getString("skin.repo")
     * @return
     */
    String getSkinRepoProperty();
    
    /**
     * Gets the tool skin CSS first by checking the tool, otherwise by using the default property.
     * @param   the location of the skin repo
     * @return
     */
    String getToolSkinCSS(String skinRepo);
    
	/**
	 * @return flag in case of the current user is switching to other roles.
	 */
	boolean isUserRoleSwapped();
	
	List<Role> getRoles();
	String getUserRole();
}
