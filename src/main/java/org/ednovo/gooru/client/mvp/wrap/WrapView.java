/*******************************************************************************
 * Copyright 2013 Ednovo d/b/a Gooru. All rights reserved.
 * 
 *  http://www.goorulearning.org/
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.ednovo.gooru.client.mvp.wrap;

import java.util.List;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseView;
import org.ednovo.gooru.client.mvp.home.HeaderUc;
import org.ednovo.gooru.client.mvp.home.PreFilterPopup;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingEvent;
import org.ednovo.gooru.client.mvp.home.presearchstandards.AddStandardsPreSearchPresenter;
import org.ednovo.gooru.client.uc.BrowserAgent;
import org.ednovo.gooru.client.uc.DeviceUc;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.model.user.UserDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class WrapView extends BaseView implements IsWrapView {

	private static WrapViewUiBinder uiBinder = GWT.create(WrapViewUiBinder.class);
	
	MessageProperties i18n = GWT.create(MessageProperties.class);

	interface WrapViewUiBinder extends UiBinder<Widget, WrapView> {
	}

	@UiField
	SimplePanel wrapperPanel;

	@UiField
	HeaderUc headerUc;

	@UiField HTMLPanel panelWrapper, panelDevice,searchPush,menuRight, resorceSearchFilters, collectionSearchFilters,wrapBodyPanel;
	
	AddStandardsPreSearchPresenter addStandardsPresenter = null;
	
	private static final String USER_META_ACTIVE_FLAG = "0";
	
	private boolean isCCSSAvailable =false;
	private boolean isNGSSAvailable =false;
	private boolean isTEKSAvailable =false;
	private boolean isCAAvailable =false;
	
	private boolean isArrowIcon = false;
	
	PreFilterPopup preFilter =	null;
	Boolean isIpad,isAndriod,isWinDskp;
	/**
	 * Class constructor 
	 */
	public WrapView() {
		setWidget(uiBinder.createAndBindUi(this));
		
		panelWrapper.getElement().setId("wrapper");
		wrapBodyPanel.getElement().setId("main");
		headerUc.getElement().setId("homeHeaderUc");
		wrapperPanel.getElement().setId("spnlWrapperPanel");
		searchPush.getElement().setId("searchPush");
		menuRight.getElement().setId("menuRight");
		

		  Boolean isIpad = !!Navigator.getUserAgent().matches("(.*)iPad(.*)");
		  Boolean isAndriod = !!Navigator.getUserAgent().matches("(.*)Android(.*)");
		  Boolean isWinDskp = !!Navigator.getUserAgent().matches("(.*)NT(.*)");
		  if (BrowserAgent.isDevice()){
			  panelDevice.add(new DeviceUc(wrapperPanel, headerUc));
			  panelDevice.setVisible(true);
			  if(isIpad && !StringUtil.IPAD_MESSAGE_Close_Click)
			  {
				  wrapperPanel.getElement().setAttribute("style", "margin-top:0px;");
				  headerUc.getElement().getFirstChildElement().setAttribute("style", "position:relative;");
			  }
			  else if(isAndriod && !StringUtil.IPAD_MESSAGE_Close_Click)
			  {
				  wrapperPanel.getElement().setAttribute("style", "margin-top:0px;");
				  headerUc.getElement().getFirstChildElement().setAttribute("style", "position:relative;");
			  }
		  }else{
			  panelDevice.clear();
			  panelDevice.setVisible(false);
			  wrapperPanel.getElement().setAttribute("style", "margin-top:36px;");
		  }
		  
		  ClickHandler rootClick = new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					if(!isArrowIcon && preFilter!=null){
						preFilter.hide();
					}else{
						isArrowIcon=false;
					}
				}
			};
			
			RootPanel.get().addDomHandler(rootClick, ClickEvent.getType());
			
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.SEARCH_COLLECTION)){
			if(AppClientFactory.getPlaceManager().getRequestParameter("query")!=null)
			{
			if(AppClientFactory.getPlaceManager().getRequestParameter("query").equalsIgnoreCase("*"))
			{
				headerUc.editSearchTxtBox.setText("");	
			}
			else
			{
				headerUc.editSearchTxtBox.setText(AppClientFactory.getPlaceManager().getRequestParameter("query"));
			}
			}
				
		}
		activateSearchBar(true);
		activateClassicButton(false);
		if (slot == WrapPresenter.TYPE_VIEW) {
			if (content != null) {
				String place=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
				if(place!=null&&((!place.equals(PlaceTokens.HOME)||!(place.equals(PlaceTokens.SEARCH_COLLECTION)||!place.equals(PlaceTokens.FOLDER_TOC)||!(place.equals(PlaceTokens.SEARCH_RESOURCE)))))){
					if (place.equals(PlaceTokens.SHELF)
							|| place.equalsIgnoreCase(PlaceTokens.COMMUNITY)
							|| place.equalsIgnoreCase(PlaceTokens.RUSD_LIBRARY)
							|| place.equalsIgnoreCase(PlaceTokens.SAUSD_LIBRARY)
							|| place.equalsIgnoreCase(PlaceTokens.VALVERDE)
							|| place.equalsIgnoreCase(PlaceTokens.LIFEBOARD)
							|| place.equalsIgnoreCase(PlaceTokens.EDIT_CLASSPAGE)
							|| place.equalsIgnoreCase(PlaceTokens.STUDENT)) {
						wrapperPanel.getElement().setAttribute("style", "margin-top:36px;");
					}else{
						/*if(isIpad){
							  wrapperPanel.getElement().setAttribute("style", "margin-top:0px;");
						}else if(isAndriod){
							  wrapperPanel.getElement().setAttribute("style", "margin-top:0px;");
						}else{
							  wrapperPanel.getElement().setAttribute("style", "margin-top:36px;");
						}*/
					}
				}
				wrapperPanel.setWidget(content);
			}
		}
	}

	@Override
	public void activateClassicButton(boolean activate) {
		headerUc.setClassicButtonEnabled(activate);
	}
	
	@Override
	public void invokeLogin() {
		headerUc.onLinkPopupClicked(null);
	}

	@Override
	public void setLoginData(UserDo user) {
		headerUc.setLoggedInUser(user);
	}

	@Override
	public void invokeRegister() {
		headerUc.onRegisterPopupClicked(null);
	}

	@Override
	public void activateSearchBar(boolean activate) {
		headerUc.setSearchBarEnabled(activate);
	}

	@Override
	public void invokeGooruGuideBubble() {
		/*GooruGuideInfoVc gooruGuideInfo = new GooruGuideInfoVc();
		int left = headerUc.getGooruGuideIconLeft() - 195;
		int top = headerUc.getGooruGuideIconTop() + 40;
		gooruGuideInfo.setWidth("224px");
		gooruGuideInfo.setStyleName(GooruCBundle.INSTANCE.css().logoutPopup());
		gooruGuideInfo.setGlassEnabled(true);
		gooruGuideInfo.setPopupPosition(left, top);
		gooruGuideInfo.show();*/
	}

	public void setDotsPanelSelection(HeaderTabType tabType){

		headerUc.manageDotsMenuSelectionFromEvent(tabType);
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.wrap.IsWrapView#setDiscoverLinkFromLibrary(java.lang.String)
	 */
	@Override
	public void setDiscoverLinkFromLibrary(String discoverLink) {
		headerUc.setDiscoverLinkFromLibrary(discoverLink);
	}

	
	@Override
	public HTMLPanel getSearchFiltersPanel(){
		return resorceSearchFilters;
	}
	
	@Override
	public HTMLPanel getCollectionSearchFiltersPanel(){
		
		return collectionSearchFilters;
	}

	@Override
	public void showPrefilter(AddStandardsPreSearchPresenter addStandardsPresenter) {
		this.addStandardsPresenter=addStandardsPresenter;
		headerUc.getArrowLbl().addClickHandler(new showPrefilterPopup());
		
		//This is used for handle the mouse left click event to display the search prefilter popup.
		MouseDownHandler hanlder=new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				displayPreFilterpopup();
			}
		};
		headerUc.getEditSearchTxtBox().addDomHandler(hanlder, MouseDownEvent.getType());
	}
	
	/**
	 * @description This class is used to show the pre-filter search popup
	 * @date 16-Dec-2014
	 */
	public class showPrefilterPopup implements ClickHandler{

		/* (non-Javadoc)
		 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
		 */
		
		@Override
		public void onClick(ClickEvent event) {
			
			displayPreFilterpopup();
		}
	}

	@Override
	public void openPreFilter() {
		displayPreFilterpopup();
	}

	public void displayPreFilterpopup() { 
			
		if(preFilter!=null && preFilter.isShowing()){
			preFilter.hide();
			isArrowIcon=true;
		}else{
			isArrowIcon=true;
			//if(preFilter==null){
				preFilter =	new PreFilterPopup();
				preFilter.getStandardsInfo().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						preFilter.ShowSTandardsPanel().clear();
						getAddStandards();
						preFilter.ShowSTandardsPanel().add(addStandardsPresenter.getWidget());
						
					//	addStandardsPresenter.getView().getAddStandardsPanel().getElement().setAttribute("style", "margin: -45px 4px 4px; border: 0px solid #ccc;");
						addStandardsPresenter.getAddBtn().setVisible(false);
						headerUc.setAddStandardsPresenter(addStandardsPresenter,true);
						
					}
				});
			//}
			headerUc.setPrefilterObj(preFilter);
			//preFilter.setStyleName(GooruCBundle.INSTANCE.css().positionStyle());
			//preFilter.setPopupPosition(headerUc.getEditSearchTxtBox().getElement().getAbsoluteLeft(), headerUc.getEditSearchTxtBox().getElement().getAbsoluteTop()+40);

			preFilter.setFilter();
			preFilter.show();
			preFilter.hidePlanels();
			ClickHandler handler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					preFilter.show();
					isArrowIcon = true;
				}
			};
			preFilter.addDomHandler(handler, ClickEvent.getType());
			
		}
		
	
	}
	
	/**
     * To show particular user standards 
     */
	public void getAddStandards() {
		if(!AppClientFactory.isAnonymous()){
		AppClientFactory.getInjector().getUserService().getUserProfileV2Details(AppClientFactory.getLoggedInUser().getGooruUId(),
				USER_META_ACTIVE_FLAG,
				new SimpleAsyncCallback<ProfileDo>() {
					@Override
					public void onSuccess(final ProfileDo profileObj) {
					AppClientFactory.fireEvent(new StandardPreferenceSettingEvent(profileObj.getUser().getMeta().getTaxonomyPreference().getCode()));
					checkStandarsList(profileObj.getUser().getMeta().getTaxonomyPreference().getCode());
					}
					public void checkStandarsList(List<String> standarsPreferencesList) {
						
					if(standarsPreferencesList!=null){
							if(standarsPreferencesList.contains("CCSS")){
								isCCSSAvailable = true;
							}else{
								isCCSSAvailable = false;
							}
							if(standarsPreferencesList.contains("NGSS")){
								isNGSSAvailable = true;
							}else{
								isNGSSAvailable = false;
							}
							if(standarsPreferencesList.contains("TEKS")){
								isTEKSAvailable = true;
							}else{
								isTEKSAvailable = false;
							}
							if(standarsPreferencesList.contains("CA")){
								isCAAvailable = true;
							}else{
								isCAAvailable = false;
							}
								if(isCCSSAvailable || isNGSSAvailable || isTEKSAvailable || isCAAvailable){
									addStandardsPresenter.enableStandardsData(isCCSSAvailable,isTEKSAvailable,isNGSSAvailable,isCAAvailable);
									addStandardsPresenter.callDefaultStandardsLoad();								
								}
							
					}
						
					}

				});
		}else{
			isCCSSAvailable = true;
			isNGSSAvailable = true;
			isCAAvailable = true;
			isTEKSAvailable = false;
			if(isCCSSAvailable || isNGSSAvailable || isTEKSAvailable || isCAAvailable){
				addStandardsPresenter.enableStandardsData(isCCSSAvailable,isTEKSAvailable,isNGSSAvailable,isCAAvailable);
				addStandardsPresenter.callDefaultStandardsLoad();
			}
		}

	}

	@Override
	public void updateUserHeaderProfileImage(String imageUrl) {
		headerUc.updateHeaderProfileImage(imageUrl);
	}
}
