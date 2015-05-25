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

package org.ednovo.gooru.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.AppInjector;
import org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual.CollectionSummaryIndividualCBundle;
import org.ednovo.gooru.client.mvp.analytics.util.AnalyticsTabCBundle;
import org.ednovo.gooru.client.mvp.folder.toc.FolderContainerCBundle;
import org.ednovo.gooru.client.mvp.home.HomeCBundle;
import org.ednovo.gooru.client.mvp.home.LoginPopUpCBundle;
import org.ednovo.gooru.client.mvp.play.collection.end.CollectionPlaySummaryCBundle;
import org.ednovo.gooru.client.mvp.play.resource.ResourcePlayerCBundle;
import org.ednovo.gooru.client.mvp.play.resource.style.PlayerSmallMobileBundle;
import org.ednovo.gooru.client.mvp.play.resource.style.PlayerStyleBundle;
import org.ednovo.gooru.client.mvp.search.SearchCBundle;
import org.ednovo.gooru.client.mvp.search.event.DisplayNoCollectionEvent;
import org.ednovo.gooru.client.uc.BrowserAgent;
import org.ednovo.gooru.client.uc.UcCBundle;
import org.ednovo.gooru.shared.model.user.UserDo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
/**
 * 
 * @fileName : GooruEntry.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: 06-Dec-2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class GooruEntry implements EntryPoint {

	private final AppInjector appInjector = GWT.create(AppInjector.class);
	
	private HandlerRegistration nativePreviewHandlerRegistration;
	
	private static final String GOORU_USER_INACTIVE = "in-active";

	
	public void onModuleLoad() {
		
		/**
		 * Capturing all uncaught exception on client side.
		 */
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			
			@Override
			public void onUncaughtException(Throwable e) {
				Throwable unwrapped = getExceptionToDisplay(e);
				AppClientFactory.printSevereLogger("Exception Caught !! "+unwrapped.getMessage());
			}
		});
		
		DelayedBindRegistry.bind(appInjector);
		AppClientFactory.setAppGinjector(appInjector);
		  ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
		    loadLibraries.add(LoadLibrary.ADSENSE);
		    loadLibraries.add(LoadLibrary.DRAWING);
		    loadLibraries.add(LoadLibrary.GEOMETRY);
		    loadLibraries.add(LoadLibrary.PANORAMIO);
		    loadLibraries.add(LoadLibrary.PLACES);
		    loadLibraries.add(LoadLibrary.WEATHER);
		    loadLibraries.add(LoadLibrary.VISUALIZATION);
		     
		    
		String device = BrowserAgent.returnFormFactorWithSizeView();
		String size[] = device.split("-");

			appInjector.getAppService().getLoggedInUser(new SimpleAsyncCallback<UserDo>() {
				@Override
				public void onSuccess(UserDo loggedInUser) {
					AppClientFactory.setLoggedInUser(loggedInUser);
					UcCBundle.INSTANCE.css().ensureInjected();
					HomeCBundle.INSTANCE.css().ensureInjected();
					AppClientFactory.getInjector().getWrapPresenter().get().setLoginData(loggedInUser);
					appInjector.getPlaceManager().revealCurrentPlace();
					AppClientFactory.setProtocol(getHttpOrHttpsProtocol());
					registerWindowEvents();
				}
			});
			AppClientFactory.setAppGinjector(appInjector);

		getloggersStatus();

		StyleInjector.injectAtEnd("@media (min-width: 240px) and (max-width: 767px) {" + PlayerStyleBundle.INSTANCE.getPlayerMobileStyle().getText() + "}");
		StyleInjector.injectAtEnd("@media (min-width: 768px) and (max-width: 991px) {" + PlayerStyleBundle.INSTANCE.getPlayerTabletStyle().getText() + "}");
		StyleInjector.injectAtEnd("@media (min-width: 240px) and (max-width: 550px) {" + PlayerSmallMobileBundle.INSTANCE.getPlayerSmallMobile().getText() + "}");
		PlayerStyleBundle.INSTANCE.getPlayerStyleResource().ensureInjected();
		
		StyleInjector.injectAtEnd("@media (max-width: 767px){"+SearchCBundle.INSTANCE.getResponsiveStyle().getText()+"}");
		StyleInjector.injectAtEnd("@media (max-width: 767px) and (orientation:portrait){"+SearchCBundle.INSTANCE.getResponsive1Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (max-width: 767px) and (orientation:landscape){"+SearchCBundle.INSTANCE.getResponsive2Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 480px) and (max-width: 767px){"+SearchCBundle.INSTANCE.getResponsive3Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 240px) and (max-width: 319px){"+SearchCBundle.INSTANCE.getResponsive4Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 320px) and (max-width: 479px){"+SearchCBundle.INSTANCE.getResponsive5Style().getText()+"}");
		StyleInjector.injectAtEnd("@media screen and (min-width: 768px){"+SearchCBundle.INSTANCE.getResponsive6Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 1200px){"+SearchCBundle.INSTANCE.getResponsive7Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 768px) and (max-width: 991px) {"+SearchCBundle.INSTANCE.getResponsive8Style().getText()+"}");

		SearchCBundle.INSTANCE.css().ensureInjected();
		
		
		
		StyleInjector.injectAtEnd("@media (min-width: 240px) and (max-width: 319px){"+LoginPopUpCBundle.INSTANCE.getResponsiveStyle().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 320px) and (max-width: 479px){"+LoginPopUpCBundle.INSTANCE.getResponsive1Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 480px) and (max-width: 767px){"+LoginPopUpCBundle.INSTANCE.getResponsive2Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 768px) and (max-width: 991px){"+LoginPopUpCBundle.INSTANCE.getResponsive3Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 1200px){"+LoginPopUpCBundle.INSTANCE.getResponsive4Style().getText()+"}");
		StyleInjector.injectAtEnd("@media screen and (max-width: 767px) {"+LoginPopUpCBundle.INSTANCE.getResponsive5Style().getText()+"}");
		StyleInjector.injectAtEnd("@media screen and (min-width: 768px) {"+LoginPopUpCBundle.INSTANCE.getResponsive6Style().getText()+"}");

		LoginPopUpCBundle.INSTANCE.css().ensureInjected();
		
		StyleInjector.injectAtEnd("@media (max-width: 767px) {"+AnalyticsTabCBundle.INSTANCE.getResponsiveStyle().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 768px) and (max-width: 991px) {"+AnalyticsTabCBundle.INSTANCE.getResponsive1Style().getText()+"}");
		
		AnalyticsTabCBundle.INSTANCE.css().ensureInjected();
		
		StyleInjector.injectAtEnd("@media (min-width: 240px) and (max-width: 319px){"+ResourcePlayerCBundle.INSTANCE.getResponsive1Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 320px) and (max-width: 479px){"+ResourcePlayerCBundle.INSTANCE.getResponsive2Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 480px) and (max-width: 767px){"+ResourcePlayerCBundle.INSTANCE.getResponsive3Style().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 768px) and (max-width: 992px){"+ResourcePlayerCBundle.INSTANCE.getResponsive4Style().getText()+"}");

		ResourcePlayerCBundle.INSTANCE.css().ensureInjected();
	

		StyleInjector.injectAtEnd("@media (max-width: 767px) {"+CollectionSummaryIndividualCBundle.INSTANCE.getResponsiveStyle().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 768px) and (max-width: 991px) {"+CollectionSummaryIndividualCBundle.INSTANCE.getResponsive1Style().getText()+"}");
		
		CollectionSummaryIndividualCBundle.INSTANCE.css().ensureInjected();
		
		StyleInjector.injectAtEnd("@media (max-width: 767px) {"+CollectionPlaySummaryCBundle.INSTANCE.getResponsiveStyle().getText()+"}");
		StyleInjector.injectAtEnd("@media (min-width: 768px) and (max-width: 991px) {"+CollectionPlaySummaryCBundle.INSTANCE.getResponsive1Style().getText()+"}");
		
		CollectionPlaySummaryCBundle.INSTANCE.css().ensureInjected();
		
		StyleInjector.injectAtEnd("@media (min-width: 480px) and (max-width: 767px){"+FolderContainerCBundle.INSTANCE.getResponsiveStyle().getText()+"}");
		FolderContainerCBundle.INSTANCE.css().ensureInjected();
	}
	
	
	/**
	 * Checks the status of loggers from property file whether it is enabled or not.
	 */
	private void getloggersStatus() { 
		AppClientFactory.getInjector().getSearchService().isClientSideLoggersEnabled(new SimpleAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				AppClientFactory.setClientLoggersEnabled(result);
			}
			
		});
	}

	private void registerWindowEvents(){
		nativePreviewHandlerRegistration = Event.addNativePreviewHandler(new NativePreviewHandler() {
			
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				if(event.getTypeInt()==Event.ONMOUSEOVER){
					if(AppClientFactory.getUserStatus()!=null){
						Cookies.setCookie("gooru-active-user", AppClientFactory.getUserStatus(),getCurrentDateAndTime()); 
					}
					if((AppClientFactory.getUserStatus()==null || AppClientFactory.getUserStatus().trim().equals(GOORU_USER_INACTIVE)) &&  AppClientFactory.isUserflag() && (AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PREVIEW_PLAY))){
					}
					else if((AppClientFactory.getUserStatus()==null || AppClientFactory.getUserStatus().trim().equals(GOORU_USER_INACTIVE)) &&  AppClientFactory.isUserflag() && AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_PLAY)){
						
					}
					else if((AppClientFactory.getUserStatus()==null || AppClientFactory.getUserStatus().trim().equals(GOORU_USER_INACTIVE)) && AppClientFactory.isUserflag()){
						AppClientFactory.setUserflag(false)	;
						if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.HOME) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SEARCH_RESOURCE) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.FOLDER_TOC)|| AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SEARCH_COLLECTION) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.STUDY)){
							userLoggedOutheader();
						}
						else{
							redirectToLandingPage();
						}
						
					}
					
				}
			}
		});
	}
	
	/* 
	 * If user logged out by staying on Discover or Study page on any one of the tab,
	 * this method will be called and header will reset on other tabs. 
	 * 
	 * */
	
	protected Date getCurrentDateAndTime() {
		Date date = new Date();
		Date updatedDate = new Date((date.getTime() + (1000 * 60 * 60 * 24)));//(1000 * 60 * 60 * 24)=24 hrs. **** 120060 = 2 mins
		return updatedDate;
	}

	private void userLoggedOutheader(){
		appInjector.getAppService().getLoggedInUser(new SimpleAsyncCallback<UserDo>() {
			@Override
			public void onSuccess(UserDo loggedInUser) {
				AppClientFactory.setLoggedInUser(loggedInUser);
				UcCBundle.INSTANCE.css().ensureInjected();
				HomeCBundle.INSTANCE.css().ensureInjected();
				AppClientFactory.getInjector().getWrapPresenter().get().setLoginData(loggedInUser);
			}
		});
	}
	
	
	/* 
	 * If user logged out on any one of the tab,
	 * this method will be called to redirect to landing page on other tabs 
	 *  and Log-in pop up will be invoked.
	 *  
	 * */
	
	private void redirectToLandingPage(){
		appInjector.getAppService().getLoggedInUser(new SimpleAsyncCallback<UserDo>() {
			@Override
			public void onSuccess(UserDo loggedInUser) {
				AppClientFactory.setLoggedInUser(loggedInUser);
				UcCBundle.INSTANCE.css().ensureInjected();
				HomeCBundle.INSTANCE.css().ensureInjected();
				AppClientFactory.getInjector().getWrapPresenter().get().setLoginData(loggedInUser);
				if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.STUDENT)){
					
				}else if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.SHELF)){
					AppClientFactory.fireEvent(new DisplayNoCollectionEvent());
				}else{
					Map<String, String> params = new HashMap<String,String>();
					params.put("loginEvent", "true");
					appInjector.getPlaceManager().revealPlace(PlaceTokens.HOME, params);
				}
			}
		});
	}
	
	public String getHttpOrHttpsProtocol() {
		return Window.Location.getProtocol();
	}
	
	/**
	 *  Method used to unwrap GWT umbrella exception.
	 *  
	 * @param e {@link Throwable}
	 * @return {@link Throwable}
	 */
	public Throwable getExceptionToDisplay(Throwable e) {
	    if(e instanceof UmbrellaException) {
	      UmbrellaException ue = (UmbrellaException) e;
	      if(ue.getCauses().size() == 1) {
	        return getExceptionToDisplay(ue.getCauses().iterator().next());
	      }
	    }
	    return e;
	  }
}
