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

import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.util.SiteParticipantHelper;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link SakaiProxy}
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au), Thach Ngoc Le (ThachLN@mks.com.vn
 *
 */
@Slf4j
public class SakaiProxyImpl implements SakaiProxy {

    /**
    * {@inheritDoc}
    */
    public String getCurrentSiteId(){
        return toolManager.getCurrentPlacement().getContext();
    }
    
    /**
    * {@inheritDoc}
    */
    public String getCurrentUserId() {
        return sessionManager.getCurrentSessionUserId();
    }
    
    /**
    * {@inheritDoc}
    */
    public String getCurrentUserDisplayName() {
       return userDirectoryService.getCurrentUser().getDisplayName();
    }

    /**
    * {@inheritDoc}
    */
    public String getCurrentUserEid() {
        return userDirectoryService.getCurrentUser().getEid();
    }

    /**
    * {@inheritDoc}
    */
    public String getCurrentUserEmail() {
        return userDirectoryService.getCurrentUser().getEmail();
    }

    /**
    * {@inheritDoc}
    */
    public String getCurrentUserFirstName() {
        return userDirectoryService.getCurrentUser().getFirstName();
    }

    /**
    * {@inheritDoc}
    */
    public String getCurrentUserLastName() {
        return userDirectoryService.getCurrentUser().getLastName();
    }

    /**
    * {@inheritDoc}
    */
    public boolean isSuperUser() {
        return securityService.isSuperUser();
    }
    
    /**
    * {@inheritDoc}
    */
    public void postEvent(String event,String reference,boolean modify) {
        eventTrackingService.post(eventTrackingService.newEvent(event,reference,modify));
    }
    
    /**
    * {@inheritDoc}
    */
    public String getSkinRepoProperty(){
        return serverConfigurationService.getString("skin.repo");
    }
    
    /**
    * {@inheritDoc}
    */
    public String getToolSkinCSS(String skinRepo){
        
        String skin = siteService.findTool(sessionManager.getCurrentToolSession().getPlacementId()).getSkin();          
        
        if(skin == null) {
            skin = serverConfigurationService.getString("skin.default");
        }
        
        return skinRepo + "/" + skin + "/tool.css";
    }
    
    /**
     * init - perform any actions required here for when this bean starts up
     */
    public void init() {
        log.info("init");
    }
    
    @Getter @Setter
    private ToolManager toolManager;
    
    @Getter @Setter
    private SessionManager sessionManager;
    
    @Getter @Setter
    private UserDirectoryService userDirectoryService;
    
    @Getter @Setter
    private SecurityService securityService;
    
    @Getter @Setter
    private EventTrackingService eventTrackingService;
    
    @Getter @Setter
    private ServerConfigurationService serverConfigurationService;
    
    @Getter @Setter
    private SiteService siteService;
    

    @Getter @Setter
    public AuthzGroupService authzGroupService = null;
    
	@Override
	public boolean isUserRoleSwapped() {
		return securityService.isUserRoleSwapped();
	}

	/**
	 * {@link org.sakaiproject.site.util.SiteAddParticipantHandler#init}
	 */
	@Override
	public String getUserRole() {

//		String userId = getCurrentUserEid();
		String userId = userDirectoryService.getCurrentUser().getId();
		try {
			String siteId = getCurrentSiteId();
//			Site site = siteService.getSite(siteId);
			AuthzGroup realm = authzGroupService.getAuthzGroup(siteService.siteReference(siteId));
			
			Member member = realm.getMember(userId);

			return member.getRole() != null ? member.getRole().getId() : "";
		} catch (GroupNotDefinedException e) {
			log.error("Could not get user role.", e);
		}

		return null;
	}

	/**
	 * {@link org.sakaiproject.site.util.SiteAddParticipantHandler#init}
	 */
	@Override
	public List<Role> getRoles() {
		List<Role> roles = null;
		try {
			String siteId = getCurrentSiteId();
			Site site = siteService.getSite(siteId);
			AuthzGroup realm = authzGroupService.getAuthzGroup(siteService.siteReference(siteId));

			// SAK-23257
			roles = SiteParticipantHelper.getAllowedRoles(site.getType(), realm.getRoles());

		} catch (IdUnusedException | GroupNotDefinedException e) {
			log.error("The siteId we were given was bogus", e);
		}

		int len = (roles != null) ? roles.size() : 0;
		log.debug("Number of roles:" + len);
		for (Role role : roles) {
			log.debug("Role id:" + role.getId());
		}

		return roles;
	}
}
