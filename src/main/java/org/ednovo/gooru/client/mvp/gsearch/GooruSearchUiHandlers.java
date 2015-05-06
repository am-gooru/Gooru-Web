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

import org.ednovo.gooru.client.gin.BaseUiHandlers;
import org.ednovo.gooru.client.mvp.gsearch.util.GooruGradesPresenter;
import org.ednovo.gooru.client.mvp.search.event.AggregatorSuggestionEventHandler;
import org.ednovo.gooru.client.mvp.search.event.RefreshSearchHandler;
import org.ednovo.gooru.client.mvp.search.event.SearchHandler;
import org.ednovo.gooru.client.mvp.search.event.SourceSuggestionHandler;
import org.ednovo.gooru.client.mvp.search.event.StandardsSuggestionHandler;
import org.ednovo.gooru.client.mvp.search.event.SwitchSearchHandler;
import org.ednovo.gooru.shared.model.search.CollectionSearchResultDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;

/**
 * @author Search Team
 * 
 */
public interface GooruSearchUiHandlers extends BaseUiHandlers,RefreshSearchHandler, StandardsSuggestionHandler, SourceSuggestionHandler,SwitchSearchHandler,SearchHandler,
    AggregatorSuggestionEventHandler{
	
	void getCollectionSearchResultsOnPageWise(String query,int pageNumber,int pageSize);
	/**
	 * To add Grades Presenter widget
	 */
	void getGradesWidget();
	/**
	 * @return the gooruGradesPresenter
	 */
	GooruGradesPresenter getGooruGradesPresenter();
	void getAddStandards();
	void setUpdatedStandards();
	void closeStandardsPopup();
	/**
	 * This method will set the type of search
	 * @param isCollectionSearch
	 * @param query 
	 */
	void setSearchType(boolean isCollectionSearch, String query);
	
	void displayAddResourcePoup(ResourceSearchResultDo resourceSearchResultDo);
	void displayUsersList(ResourceSearchResultDo resourceSearchResultDo);
	
	void displayRemixForCollectionsPoup(CollectionSearchResultDo collectionsearchResultDo);
	
	void requestSourceSuggestions(SearchDo<String> searchDo);
	
	void requestAggregatorSuggestions(SearchDo<String> searchDo);
	
	void showRatingAndReviewPopup(ResourceSearchResultDo resourceSearchResultDo);
	
	void setDataReterivedFromStorage(String data,boolean isApiCalled);
}
