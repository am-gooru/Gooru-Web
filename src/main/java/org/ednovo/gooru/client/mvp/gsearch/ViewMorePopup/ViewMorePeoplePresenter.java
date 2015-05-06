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
package org.ednovo.gooru.client.mvp.gsearch.ViewMorePopup;



import java.util.ArrayList;
import java.util.HashMap;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.shared.model.content.ResourceCollDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.util.ClientConstants;

import com.google.gwt.event.shared.EventBus;
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
public class ViewMorePeoplePresenter extends PresenterWidget<IsViewMorePeopleView> implements ViewMorePeopleUiHandlers,ClientConstants{

	HashMap<String,String> successparams = new HashMap<String, String>();
	ResourceSearchResultDo searchResultDo =null;
	
	@Inject
	public ViewMorePeoplePresenter(EventBus eventBus, IsViewMorePeopleView view) {
		super(eventBus, view);
		getView().setUiHandlers(this);	

	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	public void getResourceDataByResource(ResourceSearchResultDo searchResultDo,String searchType) {
		this.searchResultDo =searchResultDo;		
		getWorkspaceData(0,20,searchResultDo.getGooruOid());
	}
	
	public void getWorkspaceData(int offset,int limit, String resourceId){
		AppClientFactory.getInjector().getResourceService().getResourceBasedUsersDetails(resourceId, offset, limit, new SimpleAsyncCallback<ArrayList<ResourceCollDo>>() {
			@Override
			public void onSuccess(ArrayList<ResourceCollDo> userCollectionsList) {
					getView().displayContents(userCollectionsList,searchResultDo);
			}
		});
	}



	@Override
	public void hidePopup() {
		getView().hidePopup();
	}
}
