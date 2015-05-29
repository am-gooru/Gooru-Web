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
package org.ednovo.gooru.client.mvp.gsearch.collection;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.AppPlaceKeeper;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SearchAsyncCallbackForSearch;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.gsearch.GooruSearchUiHandlers;
import org.ednovo.gooru.client.mvp.gsearch.SearchAbstractPresenter;
import org.ednovo.gooru.client.mvp.gsearch.SearchMainPresenter;
import org.ednovo.gooru.client.mvp.gsearch.ViewMorePopup.ViewMorePeoplePresenter;
import org.ednovo.gooru.client.mvp.gsearch.addResourcePopup.SearchAddResourceToCollectionPresenter;
import org.ednovo.gooru.client.mvp.gsearch.util.GooruGradesPresenter;
import org.ednovo.gooru.client.mvp.search.IsSearchView;
import org.ednovo.gooru.client.mvp.search.CenturySkills.AddCenturyPresenter;
import org.ednovo.gooru.client.mvp.search.collection.RefreshDisclosurePanelForFoldersEventHandler;
import org.ednovo.gooru.client.mvp.search.standards.AddStandardsPresenter;
import org.ednovo.gooru.client.mvp.search.util.CollectionResourceWidget;
import org.ednovo.gooru.client.mvp.search.util.CollectionSearchWidget;
import org.ednovo.gooru.client.service.SearchServiceAsync;
import org.ednovo.gooru.shared.model.search.CollectionSearchResultDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
/**
 * @fileName : SearchCollectionPresenter.java
 *
 * @description : 
 *
 * @version : 1.3
 *
 * @date: 10-04-2015
 *
 * @Author Gooru Team
 *
 * @Reviewer: 
 */
public class SearchCollectionPresenter extends SearchAbstractPresenter<CollectionSearchResultDo, ResourceSearchResultDo, IsSearchCollectionView, SearchCollectionPresenter.IsSearchCollectionProxy> implements GooruSearchUiHandlers,RefreshDisclosurePanelForFoldersEventHandler {

	@Inject
	private SearchServiceAsync searchService;
	SearchDo<CollectionSearchResultDo> searchDo=new SearchDo<CollectionSearchResultDo>();
	Map<String, String> filterMap = new HashMap<String, String>();
	
	AddStandardsPresenter addStandardsPresenter = null;

	AddCenturyPresenter addCenturyPresenter;
	
	GooruGradesPresenter gooruGradesPresenter;
	
	SearchAddResourceToCollectionPresenter searchAddResourceToCollectionPresenter=null;
	
	ViewMorePeoplePresenter viewmorePeoplePresenter=null;
	
	@ProxyCodeSplit
	@NameToken(PlaceTokens.SEARCH_COLLECTION)
	@UseGatekeeper(AppPlaceKeeper.class)
	public interface IsSearchCollectionProxy extends ProxyPlace<SearchCollectionPresenter> {
	}

	@Inject
	public SearchCollectionPresenter(IsSearchCollectionView view, IsSearchCollectionProxy proxy,SignUpPresenter signUpViewPresenter,AddStandardsPresenter addStandardsPresenter,AddCenturyPresenter addCenturyPresenter,GooruGradesPresenter gooruGradesPresenter,SearchAddResourceToCollectionPresenter searchAddResourceToCollectionPresenter,ViewMorePeoplePresenter viewmorePeoplePresenter) {
		super(view, proxy, signUpViewPresenter,addStandardsPresenter,addCenturyPresenter,gooruGradesPresenter,searchAddResourceToCollectionPresenter,viewmorePeoplePresenter);
		this.addStandardsPresenter = addStandardsPresenter;
		this.addCenturyPresenter=addCenturyPresenter;
		this.gooruGradesPresenter=gooruGradesPresenter;
		this.searchAddResourceToCollectionPresenter=searchAddResourceToCollectionPresenter;
		this.viewmorePeoplePresenter=viewmorePeoplePresenter;
		getView().setUiHandlers(this);
	}

	@Override
	public String getViewToken() {
		return PlaceTokens.SEARCH_COLLECTION;
	}
	
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, SearchMainPresenter.TYPE_VIEW, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
	}
	@Override
	protected void onReset() {
		super.onReset();
	}
	
	@Override
	public void onBind() {
		super.onBind();
	}

	public SearchServiceAsync getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchServiceAsync searchService) {
		this.searchService = searchService;
	}

	
	@Override
	public void refreshDisclosurePanelForFoldersinSearch(String collectionId) {
		
	}

	@Override
	protected void requestSearch(final SearchDo<CollectionSearchResultDo> searchDo,final SearchAsyncCallbackForSearch<SearchDo<CollectionSearchResultDo>> searchAsyncCallback) {
		getSearchService().getCollectionSearchResultsJson(searchDo, getSearchResultsJsonAsyncCallbackFirstLoad());
	}
	@Override
	protected void requestSearchLoad(SearchDo<CollectionSearchResultDo> searchDo,SearchAsyncCallbackForSearch<SearchDo<CollectionSearchResultDo>> searchResultsJsonAsyncCallback,boolean isBackTotop) {
		if(isBackTotop){
			getSearchService().getCollectionSearchResultsJson(searchDo, getSearchResultsBackToTop());
		}else{
			getSearchService().getCollectionSearchResultsJson(searchDo, getSearchResultsJsonAsyncCallbackLoadInStore());
		}
	}
	@Override
	protected void requestSearchFormJson(String result,SearchDo<CollectionSearchResultDo> searchDo2) {
		getSearchService().descralizeCollectionSearchResults(result, searchDo2, getSearchAsyncCallback());
	}

	@Override
	public void getCollectionSearchResultsOnPageWise(String query,int pageNumber, int pageSize) {
		getSearchDo().setPageNum(pageNumber);
		getSearchDo().setPageSize(pageSize);
		getSearchService().getCollectionSearchResultsJson(getSearchDo(), getSearchResultsJsonAsyncCallbackLoadInStore());
	}
	
	/**
	 * @return search filters as Map value
	 */
	@Override
	protected Map<String, String> getSearchFilters() {
		Map<String, String> filters = super.getSearchFilters();
		String author = getPlaceManager().getRequestParameter(IsSearchView.OWNER_FLT);
		if (author != null) {
			filters.put(IsSearchView.OWNER_FLT, author);
		}
		return filters;
	}

	@Override
	public void displayRemixForCollectionsPoup(CollectionSearchResultDo collectionsearchResultDo,CollectionSearchWidget collectionSearchWidget) {
		searchAddResourceToCollectionPresenter.getUserShelfCollectionsData(collectionsearchResultDo, "collection",collectionSearchWidget);
		addToPopupSlot(searchAddResourceToCollectionPresenter);
	}
	@Override
	public void showRatingAndReviewPopup(ResourceSearchResultDo searchResultDo) {
		
	}

	@Override
	public void displayUsersList(ResourceSearchResultDo resourceSearchResultDo) {
		
	}

	@Override
	public void displayAddResourcePoup(ResourceSearchResultDo resourceSearchResultDo,CollectionResourceWidget displayAddResourcePoup) {
		
	}
}
