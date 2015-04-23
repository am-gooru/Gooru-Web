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
package org.ednovo.gooru.client.mvp.gsearch.addResourcePopup;



import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.folder.FolderListDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;

/**
 * 
 * @fileName : SearchAddResourceToCollectionPresenter.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: 22-APR-2015
 *
 * @Author : Gooru Team
 *
 * @Reviewer: Gooru Team
 */
public class SearchAddResourceToCollectionPresenter extends PresenterWidget<IsSearchAddResourceToCollectionView> implements SearchAddResourceToCollectionUiHandlers{


	ResourceSearchResultDo searchResultDo =null;
	
	@Inject
	public SearchAddResourceToCollectionPresenter(EventBus eventBus, IsSearchAddResourceToCollectionView view) {
		super(eventBus, view);
		getView().setUiHandlers(this);	
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	@Override
	public void getUserShelfData(ResourceSearchResultDo searchResultDo,String searchType) {
		this.searchResultDo =searchResultDo;
		getWorkspaceData(0,20,true,searchType);
	}
	public void getWorkspaceData(int offset,int limit, final boolean clearShelfPanel,final String searchType){
		AppClientFactory.getInjector().getResourceService().getFolderWorkspace(offset, limit,"public,anyonewithlink", null,true, new SimpleAsyncCallback<FolderListDo>() {
			@Override
			public void onSuccess(FolderListDo folderListDo) {
				if(folderListDo.getCount()==0){
					getView().displayNoCollectionsMsg();
				}else{
					getView().displayWorkspaceData(folderListDo,clearShelfPanel,searchType);
				}
			}
		});
	}
	@Override
	public void getFolderItems(final TreeItem item,String parentId) {
		AppClientFactory.getInjector().getfolderService().getChildFolders(0, 20, parentId,"public,anyonewithlink", null,true, new SimpleAsyncCallback<FolderListDo>() {
			@Override
			public void onSuccess(FolderListDo folderListDo) {
				getView().setFolderItems(item,folderListDo);
			}
		});
	}
	@Override
	public void addResourceToCollection(final String selectedFolderOrCollectionid,String searchType,final String title) {
		if(selectedFolderOrCollectionid!=null){
			//This will check the resource count
			AppClientFactory.getInjector().getfolderService().getCollectionResources(selectedFolderOrCollectionid,null, null, new SimpleAsyncCallback<FolderListDo>(){
				@Override
				public void onSuccess(FolderListDo result) {
					if (result.getCount()<25){
						//If the resource length is less than 25 then we need to create the collection item
						AppClientFactory.getInjector().getResourceService().createCollectionItem(selectedFolderOrCollectionid, searchResultDo.getGooruOid(), new SimpleAsyncCallback<CollectionItemDo>() {
							@Override
							public void onSuccess(CollectionItemDo result) {
								
							}
						});
					}
				}
			});
		}
	}
	@Override
	public Button getAddButton() {
		return getView().getAddButton();
	}

	@Override
	public void hidePopup() {
		getView().hidePopup();
	}
}
