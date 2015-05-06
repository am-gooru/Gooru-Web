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
package org.ednovo.gooru.client.mvp.gsearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SearchAsyncCallback;
import org.ednovo.gooru.client.SearchAsyncCallbackForSearch;
import org.ednovo.gooru.client.SearchAsyncCallbackForString;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.event.RegisterTabDndEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.gsearch.ViewMorePopup.ViewMorePeoplePresenter;
import org.ednovo.gooru.client.mvp.gsearch.addResourcePopup.SearchAddResourceToCollectionPresenter;
import org.ednovo.gooru.client.mvp.gsearch.util.GooruGradesPresenter;
import org.ednovo.gooru.client.mvp.home.AlmostDoneUc;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingEvent;
import org.ednovo.gooru.client.mvp.search.CenturySkills.AddCenturyPresenter;
import org.ednovo.gooru.client.mvp.search.event.AggregatorSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.RefreshSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.SearchEvent;
import org.ednovo.gooru.client.mvp.search.event.SetFooterEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.search.event.SourceSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.StandardsSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.SwitchSearchEvent;
import org.ednovo.gooru.client.mvp.search.standards.AddStandardsPresenter;
import org.ednovo.gooru.client.service.SearchServiceAsync;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * @author Search Team
 * 
 * @param <T>
 * @param <C>
 * @param <V>
 * @param <P>
 */
public abstract class SearchAbstractPresenter<T extends ResourceSearchResultDo, C extends ResourceSearchResultDo, V extends IsGooruSearchView<T>, P extends Proxy<?>>
		extends BasePlacePresenter<IsGooruSearchView<T>, P> implements
		GooruSearchUiHandlers {

	@Inject
	private SearchServiceAsync searchService;

	private final String QUERY = "query";

	private SearchDo<T> searchDo = new SearchDo<T>();

	private SearchAsyncCallbackForString<SearchDo<T>> searchAsyncCallback;

	private SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionAsyncCallback;

	private SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionInfoAsyncCallback;

	private SearchAsyncCallback<SearchDo<String>> sourceSuggestionAsyncCallback;

	private SearchAsyncCallback<SearchDo<String>> aggregatorSuggestionAsyncCallback;

	private SearchAsyncCallbackForSearch<SearchDo<T>> searchResultsJsonAsyncCallbackFirstLoad;
	
	private SearchAsyncCallbackForSearch<SearchDo<T>> searchResultsJsonAsyncCallbackLoadInStore;
	
	private SearchAsyncCallbackForString<SearchDo<T>> searchAsyncCallbackLoadInStore;
	
	protected static final String ALL = "*";

	SignUpPresenter signUpViewPresenter = null;

	AddStandardsPresenter addStandardsPresenter = null;

	AddCenturyPresenter addCenturyPresenter = null;
	
	GooruGradesPresenter gooruGradesPresenter = null;
	
	SearchAddResourceToCollectionPresenter searchAddResourceToCollectionPresenter=null;
	
	ViewMorePeoplePresenter viewMorePeoplePresenter=null;
	
	private boolean isCCSSAvailable =false;
	private boolean isNGSSAvailable =false;
	private boolean isTEKSAvailable =false;
	private boolean isCAAvailable =false;
	
	private static final String USER_META_ACTIVE_FLAG = "0";

	boolean setFilter=true;
	/**
	 * Class constructor
	 * 
	 * @param view
	 *            {@link View}
	 * @param proxy
	 *            {@link Proxy}
	 */
	public SearchAbstractPresenter(V view, P proxy,
			SignUpPresenter signUpViewPresenter,
			AddStandardsPresenter addStandardsPresenterObj,
			AddCenturyPresenter addCenturyPresenter, GooruGradesPresenter gooruGradesPresenter,SearchAddResourceToCollectionPresenter searchAddResourceToCollectionPresenter,ViewMorePeoplePresenter viewMorePeoplePresenter) {
		super(view, proxy);
		this.signUpViewPresenter = signUpViewPresenter;
		this.addStandardsPresenter = addStandardsPresenterObj;
		this.addCenturyPresenter = addCenturyPresenter;
		this.gooruGradesPresenter = gooruGradesPresenter;
		this.searchAddResourceToCollectionPresenter=searchAddResourceToCollectionPresenter;
		this.viewMorePeoplePresenter=viewMorePeoplePresenter;
		addRegisteredHandler(RefreshSearchEvent.TYPE, this);
		addRegisteredHandler(SwitchSearchEvent.TYPE, this);
		//addRegisteredHandler(DisableSpellSearchEvent.TYPE, this);
		addRegisteredHandler(SearchEvent.TYPE, this);
		addRegisteredHandler(StandardsSuggestionEvent.TYPE, this);
		//addRegisteredHandler(StandardsSuggestionInfoEvent.TYPE, this);
		
		if (getViewToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
			addRegisteredHandler(SourceSuggestionEvent.TYPE, this);
			addRegisteredHandler(AggregatorSuggestionEvent.TYPE, this);
		}
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, SearchMainPresenter.TYPE_VIEW, this);
	}

	@Override
	public void onBind() {
		super.onBind();
		//first time data store
		setSearchAsyncCallback(new SearchAsyncCallbackForString<SearchDo<T>>() {
			@Override
			protected void run(boolean isApiCalled,String representation, SearchDo<T> searchDo) {
				getView().setJsonResponseInStorage(representation,isApiCalled);
				requestSearchFormJson(representation,searchDo);
			}
			@Override
			public void onCallSuccess(SearchDo<T> result,boolean isApiCalled) {
				setSearchDo(result);
				getView().postSearch(result,isApiCalled);
			}
		});
		setSearchResultsJsonAsyncCallbackFirstLoad(new SearchAsyncCallbackForSearch<SearchDo<T>>() {
			@Override
			protected void run(SearchDo<T> searchDo) {
				requestSearch(searchDo, this);
			}
			@Override
			public void onCallSuccess(String result) {
				getSearchAsyncCallback().execute(false,result,getSearchDo());
			}
		});
		//Next time it will add to local store
		setSearchAsyncCallbackLoadInStore(new SearchAsyncCallbackForString<SearchDo<T>>() {
			@Override
			protected void run(boolean isApiCalled, String representation,SearchDo<T> searchDo) {
				getView().setJsonResponseInStorage(representation,isApiCalled);
			}

			@Override
			public void onCallSuccess(SearchDo<T> result, boolean isApiCalled) {
				setSearchDo(result);
			}
		});
		setSearchResultsJsonAsyncCallbackLoadInStore(new SearchAsyncCallbackForSearch<SearchDo<T>>() {
			@Override
			protected void run(SearchDo<T> searchDo) {
				requestSearchLoad(searchDo, this);
			}

			@Override
			public void onCallSuccess(String result) {
				getSearchAsyncCallbackLoadInStore().execute(false,result,getSearchDo());
			}
		});
		getView().getGradePanel().add(gooruGradesPresenter.getWidget());
		
		if (getViewToken().equals(PlaceTokens.SEARCH_RESOURCE)) {
			setSourceSuggestionAsyncCallback(new SearchAsyncCallback<SearchDo<String>>() {

				@Override
				protected void run(SearchDo<String> searchDo) {
					getSearchService().getSuggestSource(searchDo, this);

				}

				@Override
				public void onCallSuccess(SearchDo<String> result) {
					getView().setSourceSuggestions(result);
				}
			});
			setAggregatorSuggestionAsyncCallback(new SearchAsyncCallback<SearchDo<String>>() {

				@Override
				protected void run(SearchDo<String> searchDo) {
					getSearchService().getSuggestedAggregator(searchDo, this);

				}

				@Override
				public void onCallSuccess(SearchDo<String> result) {
					getView().setAggregatorSuggestions(result);

				}

			});
		}
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().resetData();
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory
				.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
		if (getSearchDo().getSearchQuery() != null
				&& getSearchDo().getSearchQuery().trim().length() >= 0) {
			getSearchDo().setPageNum(1);
			getSearchResultsJsonAsyncCallbackFirstLoad().execute(getSearchDo());
			getSearchDo().setPageNum(2);
			getSearchResultsJsonAsyncCallbackLoadInStore().execute(getSearchDo());
		}

		AppClientFactory.fireEvent(new RegisterTabDndEvent());
		AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(true));
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.NONE));
	}

	@Override
	protected void onReset() {
		super.onReset();
		//getView().resetData();
		/*
		String count = Cookies.getCookie("MyCookie");
		if (count != null && Integer.parseInt(count) == 7) {
			Window.enableScrolling(false);
			Cookies.setCookie("MyCookie", "8");
		} else {
			Window.enableScrolling(false);
			// Window.enableScrolling(true);
		}*/

		if(AppClientFactory.getPlaceManager().refreshPlace()) {
			if (setFilter) {
				searchDo.setPageNum(1);
				getSearchService().getSearchFilters(getCurrentPlaceToken(),
						new SimpleAsyncCallback<SearchFilterDo>() {

					@Override
					public void onSuccess(SearchFilterDo searchFilterDo) {
						getView().setSearchFilter(searchFilterDo);
					}
				});
				setFilter = false;
			} else {
				initiateSearch();
			}
		}
		if (getPlaceManager().getRequestParameter("callback") != null
				&& getPlaceManager().getRequestParameter("callback")
				.equalsIgnoreCase("signup")) {
			// To show SignUp (Registration popup)
			Window.enableScrolling(false);
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
			String type = getPlaceManager().getRequestParameter("type");
			int displayScreen = getPlaceManager().getRequestParameter("type") != null ? Integer
					.parseInt(type) : 1;
					signUpViewPresenter.displayPopup(displayScreen);
					addToPopupSlot(signUpViewPresenter);
		}

		int flag = AppClientFactory.getLoggedInUser().getViewFlag();
		final String loginType = AppClientFactory.getLoggedInUser()
				.getLoginType() != null ? AppClientFactory.getLoggedInUser()
				.getLoginType() : "";
		if (!AppClientFactory.isAnonymous() && flag == 0
				&& !loginType.equalsIgnoreCase("Credential")) {
			AlmostDoneUc update = new AlmostDoneUc(AppClientFactory
					.getLoggedInUser().getEmailId(),
					AppClientFactory.getLoggedInUser());
			update.setGlassEnabled(true);
			update.show();
			update.center();
		}

		Document doc = Document.get();
		doc.getElementById("uvTab").getStyle().setDisplay(Display.BLOCK);
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.NONE));
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		if (AppClientFactory.getPlaceManager().refreshPlace()) {
			initParam();
		}
	}

	public void setPageTitle(String searchQuery) {
		String pageToken = AppClientFactory.getPlaceManager()
				.getCurrentPlaceRequest().getNameToken();
		if (pageToken.equals(PlaceTokens.COLLECTION_SEARCH)) {
			AppClientFactory
					.setBrowserWindowTitle(SeoTokens.COLLECTION_SEARCH_TITLE
							+ searchQuery);
		} else if (pageToken.equals(PlaceTokens.RESOURCE_SEARCH)) {
			AppClientFactory
					.setBrowserWindowTitle(SeoTokens.RESOURCE_SEARCH_TITLE
							+ searchQuery);
		}
	}
	
	
	public void initiateSearch() {
		setPageTitle(getSearchDo().getSearchQuery());
		AppClientFactory
				.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
		getView().resetData();
		if (getSearchDo().getSearchQuery() != null
				&& getSearchDo().getSearchQuery().trim().length() >= 0) {
			getSearchDo().setPageNum(1);
			getSearchResultsJsonAsyncCallbackFirstLoad().execute(getSearchDo());
			getSearchDo().setPageNum(2);
			getSearchResultsJsonAsyncCallbackLoadInStore().execute(getSearchDo());
		}
	}

	/**
	 * @return search filters such as category, subject, grade, etc..
	 */
	protected Map<String, String> getSearchFilters() {
		Map<String, String> filters = new HashMap<String, String>();
		String subject = getPlaceManager().getRequestParameter(
				IsGooruSearchView.SUBJECT_FLT);
		if (subject != null) {
			filters.put(IsGooruSearchView.SUBJECT_FLT, subject);
		}
		String grade = getPlaceManager().getRequestParameter(
				IsGooruSearchView.GRADE_FLT);
		if (grade != null) {
			filters.put(IsGooruSearchView.GRADE_FLT, grade);
		}
		String standard = getPlaceManager().getRequestParameter(
				IsGooruSearchView.STANDARD_FLT);
		if (standard != null) {
			filters.put(IsGooruSearchView.STANDARD_FLT, standard);
		}
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest()
				.getNameToken().equalsIgnoreCase(PlaceTokens.SEARCH_RESOURCE)) {
			String notFriendly = getPlaceManager().getRequestParameter(
					IsGooruSearchView.MEDIATYPE_FLT);
			String category = getPlaceManager().getRequestParameter(
					IsGooruSearchView.CATEGORY_FLT);
			if (category != null) {
				filters.put(IsGooruSearchView.CATEGORY_FLT, category);
			}
			if (notFriendly != null
					&& notFriendly.equalsIgnoreCase("not_ipad_friendly")) {
				filters.put(IsGooruSearchView.MEDIATYPE_FLT, notFriendly);
			}
			String oer = getPlaceManager().getRequestParameter(
					IsGooruSearchView.OER_FLT);
			String ratings= getPlaceManager().getRequestParameter(
					IsGooruSearchView.RATINGS_FLT);
			String accessMode = getPlaceManager().getRequestParameter(
					IsGooruSearchView.ACCESS_MODE_FLT);
			String publisher = getPlaceManager().getRequestParameter(
					IsGooruSearchView.PUBLISHER_FLT);
			String aggregator = getPlaceManager().getRequestParameter(
					IsGooruSearchView.AGGREGATOR_FLT);
			String reviews = getPlaceManager().getRequestParameter(
					IsGooruSearchView.REVIEWS_FLT);
			if(!StringUtil.isEmpty(publisher)){
				filters.put(IsGooruSearchView.PUBLISHER_FLT, publisher);
			}
			if(!StringUtil.isEmpty(reviews) && reviews.equalsIgnoreCase("1"))
			{
				filters.put(IsGooruSearchView.REVIEWS_FLT, reviews);
			}

			if(!StringUtil.isEmpty(aggregator)){
				filters.put(IsGooruSearchView.AGGREGATOR_FLT, aggregator);
			}
			if (oer != null && oer.equalsIgnoreCase("1")) {
				filters.put(IsGooruSearchView.OER_FLT, oer);
			}
			if (accessMode != null) {
				filters.put(IsGooruSearchView.ACCESS_MODE_FLT, accessMode);
			}
			if(ratings != null){
				filters.put(IsGooruSearchView.RATINGS_FLT, ratings);
			}else{
				filters.put(IsGooruSearchView.RATINGS_FLT, "5,4,3,2,1,0");
			}

		}else{
			String collectionType = getPlaceManager().getRequestParameter(
					IsGooruSearchView.COLLECTIONTYPE_FLT);
			if (collectionType != null) {
				filters.put(IsGooruSearchView.COLLECTIONTYPE_FLT, collectionType);
			}
		}
		return filters;
	}

	/**
	 * @return type of {@link SearchDo}
	 */
	public SearchDo<T> getSearchDo() {
		return searchDo;
	}

	/**
	 * @param searchDo
	 *            , assign searchDo
	 */
	public void setSearchDo(SearchDo<T> searchDo) {
		this.searchDo = searchDo;
	}

	public SearchServiceAsync getSearchService() {
		return searchService;
	}
	
	@Override
	public void onSearch(String query) {
		getSearchDo().setQuery(query);
		onSearchRequest(null);
	}
	
	@Override
	public void switchSearch(String viewToken, String searchQuery) {
		getSearchDo().setNotFriendly(null);
		getSearchDo().setQuery(searchQuery);
		Map<String, String> params = new HashMap<String, String>();
		params.put(IsGooruSearchView.RATINGS_FLT, "5,4,3,2,1,0");
		getSearchDo().setFilters(params);
		onSearchRequest(viewToken);
	}
	
	@Override
	public void refreshSearch(String query) {
		if(query!=null){
			getSearchDo().setQuery(query);
			onSearchRequest(null);
		}
	}

	/**
	 * Assign query and filters for search
	 */
	private void initParam() {
		getSearchDo().setSearchResults(null);
		String queryVal = "";
		if (getPlaceManager().getRequestParameter(QUERY) != null) {
			queryVal = getPlaceManager().getRequestParameter(QUERY);
			/* queryVal = queryVal.replaceAll("%5C1", "&"); */
		}
		try {
			queryVal = URL.decodeQueryString(queryVal);
		} catch (Exception ex) {
			AppClientFactory.printSevereLogger(ex.getMessage());
		}
		getSearchDo().setQuery(queryVal);
		getSearchDo().setFilters(getSearchFilters());
	}
	
	/**
	 * Set search view token ,assign search query, page number and page size
	 * 
	 * @param viewToken
	 *            is a page view url
	 */
	public void onSearchRequest(String viewToken) {
		if (viewToken == null) {
			viewToken = getCurrentPlaceToken();
		}
		if (getViewToken().equalsIgnoreCase(viewToken)) {
			Map<String, String> params = getView().getSearchFilters(viewToken);
			params.put(QUERY, getSearchDo().getUrlQuery());
			getPlaceManager().revealPlace(viewToken, params, true);
		}
	}
	
	@Override
	public void getGradesWidget(){
		//getView().getGradePanel().clear();
		
	}
	
	@Override
	public void requestStandardsSuggestion(SearchDo<CodeDo> searchDo) {
		if (isCurrentView()) {
			getStandardSuggestionAsyncCallback().execute(searchDo);
		}
	}

	@Override
	public void requestSourceSuggestion(SearchDo<String> searchDo) {
		if (isCurrentView()) {
			getSourceSuggestionAsyncCallback().execute(searchDo);
		}
		
	}
	@Override
	public void requestSourceSuggestions(SearchDo<String> searchDo){
		getSourceSuggestionAsyncCallback().execute(searchDo);
	}

	@Override
	public void requestAggregatorSuggestion(SearchDo<String> searchDo) {
		if (isCurrentView()) {
			getAggregatorSuggestionAsyncCallback().execute(searchDo);
		}
	}
	
	@Override
	public void requestAggregatorSuggestions(SearchDo<String> searchDo){
		getAggregatorSuggestionAsyncCallback().execute(searchDo);
	}

	public SearchAsyncCallback<SearchDo<CodeDo>> getStandardSuggestionAsyncCallback() {
		return standardSuggestionAsyncCallback;
	}

	public void setStandardSuggestionAsyncCallback(
			SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionAsyncCallback) {
		this.standardSuggestionAsyncCallback = standardSuggestionAsyncCallback;
	}

	public SearchAsyncCallback<SearchDo<String>> getSourceSuggestionAsyncCallback() {
		return sourceSuggestionAsyncCallback;
	}

	public void setSourceSuggestionAsyncCallback(
			SearchAsyncCallback<SearchDo<String>> sourceSuggestionAsyncCallback) {
		this.sourceSuggestionAsyncCallback = sourceSuggestionAsyncCallback;
	}

	public SearchAsyncCallback<SearchDo<String>> getAggregatorSuggestionAsyncCallback() {
		return aggregatorSuggestionAsyncCallback;
	}

	public void setAggregatorSuggestionAsyncCallback(
			SearchAsyncCallback<SearchDo<String>> aggregatorSuggestionAsyncCallback) {
		this.aggregatorSuggestionAsyncCallback = aggregatorSuggestionAsyncCallback;
	}

	protected abstract void requestSearch(SearchDo<T> searchDo,SearchAsyncCallbackForSearch<SearchDo<T>> searchResultsJsonAsyncCallback);
	
	protected abstract void requestSearchLoad(SearchDo<T> searchDo,SearchAsyncCallbackForSearch<SearchDo<T>> searchResultsJsonAsyncCallback);

	protected abstract void requestSearchFormJson(String result,SearchDo<T> searchDo2);

	/**
	 * @return the standardSuggestionInfoAsyncCallback
	 */
	public SearchAsyncCallback<SearchDo<CodeDo>> getStandardSuggestionInfoAsyncCallback() {
		return standardSuggestionInfoAsyncCallback;
	}

	/**
	 * @param standardSuggestionInfoAsyncCallback
	 *            the standardSuggestionInfoAsyncCallback to set
	 */
	public void setStandardSuggestionInfoAsyncCallback(
			SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionInfoAsyncCallback) {
		this.standardSuggestionInfoAsyncCallback = standardSuggestionInfoAsyncCallback;
	}

	/**
	 * @return the gooruGradesPresenter
	 */
	@Override
	public GooruGradesPresenter getGooruGradesPresenter() {
		return gooruGradesPresenter;
	}
	
	@Override
	public void setUpdatedStandards() {
		getView().setUpdatedStandards(addStandardsPresenter.setStandardsVal());
	}
	
	@Override
	public void closeStandardsPopup() {
		addStandardsPresenter.hidePopup();
	}
	
	@Override
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
									addToPopupSlot(addStandardsPresenter);
									getView().OnStandardsClickEvent(addStandardsPresenter.getAddBtn());
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
				addToPopupSlot(addStandardsPresenter);
				getView().OnStandardsClickEvent(addStandardsPresenter.getAddBtn());
			}
		}

	}
	@Override
	public void setSearchType(boolean isCollectionSearch, String query) {
		String viewToken;
		if(isCollectionSearch){
			viewToken=PlaceTokens.SEARCH_COLLECTION;
		}else{
			viewToken=PlaceTokens.SEARCH_RESOURCE;
		}
		Map<String, String> params = getView().getSearchFilters(viewToken);
        //onSearchRequest(viewToken);
		getSearchDo().setPageNum(1);
		getSearchDo().setQuery(query);
		params.put(QUERY, getSearchDo().getUrlQuery());
		getPlaceManager().revealPlace(viewToken, params, true);
	}

	public SearchAsyncCallbackForSearch<SearchDo<T>> getSearchResultsJsonAsyncCallbackFirstLoad() {
		return searchResultsJsonAsyncCallbackFirstLoad;
	}

	public void setSearchResultsJsonAsyncCallbackFirstLoad(
			SearchAsyncCallbackForSearch<SearchDo<T>> searchResultsJsonAsyncCallbackFirstLoad) {
		this.searchResultsJsonAsyncCallbackFirstLoad = searchResultsJsonAsyncCallbackFirstLoad;
	}

	public SearchAsyncCallbackForString<SearchDo<T>> getSearchAsyncCallback() {
		return searchAsyncCallback;
	}

	public void setSearchAsyncCallback(
			SearchAsyncCallbackForString<SearchDo<T>> searchAsyncCallback) {
		this.searchAsyncCallback = searchAsyncCallback;
	}

	public SearchAsyncCallbackForSearch<SearchDo<T>> getSearchResultsJsonAsyncCallbackLoadInStore() {
		return searchResultsJsonAsyncCallbackLoadInStore;
	}

	public void setSearchResultsJsonAsyncCallbackLoadInStore(
			SearchAsyncCallbackForSearch<SearchDo<T>> searchResultsJsonAsyncCallbackLoadInStore) {
		this.searchResultsJsonAsyncCallbackLoadInStore = searchResultsJsonAsyncCallbackLoadInStore;
	}

	public SearchAsyncCallbackForString<SearchDo<T>> getSearchAsyncCallbackLoadInStore() {
		return searchAsyncCallbackLoadInStore;
	}

	public void setSearchAsyncCallbackLoadInStore(
			SearchAsyncCallbackForString<SearchDo<T>> searchAsyncCallbackLoadInStore) {
		this.searchAsyncCallbackLoadInStore = searchAsyncCallbackLoadInStore;
	}

	@Override
	public void setDataReterivedFromStorage(String data,boolean isApiCalled) {
		getSearchAsyncCallback().execute(isApiCalled,data, getSearchDo());
	}
}
