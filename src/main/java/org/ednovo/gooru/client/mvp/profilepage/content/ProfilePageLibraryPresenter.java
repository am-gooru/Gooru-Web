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
/**
 * 
 */
package org.ednovo.gooru.client.mvp.profilepage.content;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.child.ChildPresenter;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.shared.model.library.PartnerFolderListDo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Search Team
 * 
 */
public class ProfilePageLibraryPresenter extends ChildPresenter<ProfilePageLibraryPresenter, IsProfilePageLibraryView> implements ProfilePageLibraryUiHandlers{
	
	private static final String LIBRARY_PAGE = "partner-page";
	
	private static final String SHARING_TYPE = "public";
	
	private static final String COLLECTION_TYPE = "folder";

	/**
	 * Class constructor
	 * 
	 * @param childView 
	 */
	public ProfilePageLibraryPresenter(IsProfilePageLibraryView childView) {
		super(childView);
	}
	
	@Override
	public void getPartnerWorkspaceFolders() {
		String id = AppClientFactory.getPlaceManager().getRequestParameter("id");
		AppClientFactory.getInjector().getLibraryService().getLibraryPartnerWorkspace(id, 20, SHARING_TYPE, COLLECTION_TYPE, new AsyncCallback<PartnerFolderListDo>(){
			@Override
			public void onFailure(Throwable caught) {}
			
			@Override
			public void onSuccess(PartnerFolderListDo result) {
				getView().setUnitList(result.getSearchResult());
			}
		});
	}
	
	@Override
	public void getPartnerChildFolderItems(final String folderId, final int pageNumber) {
		AppClientFactory.getInjector().getLibraryService().getPartnerPaginationWorkspace(folderId,SHARING_TYPE, 14,new AsyncCallback<PartnerFolderListDo>() {
			@Override
			public void onSuccess(PartnerFolderListDo result) {
				getView().setTopicListData(result.getSearchResult(), folderId);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	
	public void setPartnerWidget() {
		if(AppClientFactory.getLoggedInUser()!=null) {
			getView().clearPanels();
			getView().loadingPanel(true);
			getIntoPartnerLibrarypage();
			getPartnerWorkspaceFolders();
		} else {
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.ERROR);
		}
	}
	
	/**
	 * @function getIntoPartnerLibrarypage 
	 * @created_date : 18-Mar-2014
	 * @description
	 * @parm(s) : 
	 * @return : void
	 */
	@Override
	public void getIntoPartnerLibrarypage() {
		getView().loadPartnersPage(LIBRARY_PAGE,getViewToken());
	}
	
	public String getViewToken() {
		return PlaceTokens.HOME;
	}

}