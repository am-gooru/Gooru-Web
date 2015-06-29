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
package org.ednovo.gooru.client.mvp.gshelf.righttabs;
import org.ednovo.gooru.application.client.gin.AppClientFactory;
import org.ednovo.gooru.application.shared.model.folder.FolderDo;
import org.ednovo.gooru.client.mvp.gshelf.coursedetails.CourseInfoPresenter;
import org.ednovo.gooru.client.mvp.gshelf.courselist.MyCollectionsListPresenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
public class MyCollectionsRightClusterPresenter extends PresenterWidget<IsMyCollectionsRightClusterView> implements MyCollectionsRightClusterUiHandlers{
	
	public static final  Object INNER_SLOT = new Object();
	CourseInfoPresenter courseInfoPresenter;
	/**
	 * Constructor
	 * @param eventBus
	 * @param view
	 */
	@Inject
	public MyCollectionsRightClusterPresenter(EventBus eventBus, IsMyCollectionsRightClusterView view,CourseInfoPresenter courseInfoPresenter) {
		super(eventBus, view);
		this.courseInfoPresenter=courseInfoPresenter;
		getView().setUiHandlers(this);
	}
	@Override
	public void setTabItems(int index,String type,HTMLPanel slotPanel,FolderDo folderObj) {
		clearSlot(INNER_SLOT);
		getView().setSlotPanel(slotPanel,folderObj);
		if(index==1){
			courseInfoPresenter.callTaxonomyService();
			setInSlot(INNER_SLOT, courseInfoPresenter);
		}else if(index==2){
			MyCollectionsListPresenter myCollectionsListPresenter=AppClientFactory.getInjector().getMyCollectionsListPresenter();
			myCollectionsListPresenter.setDataInContentSlot(type, slotPanel,folderObj.getGooruOid(),false);
			setInSlot(INNER_SLOT, myCollectionsListPresenter);
		}else if(index==3){
			
		}
	}
	@Override
	public void setDefaultActiveTab(){
		getView().resetHilightStyles();
		getView().setDefaultActiveTab();
	}
}