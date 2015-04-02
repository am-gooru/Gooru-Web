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
package org.ednovo.gooru.client.mvp.classpages.tabitem.assignments.collections;

/**
 * 
 */
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.child.ChildPresenter;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.classpages.edit.EditClasspagePresenter;
import org.ednovo.gooru.client.mvp.search.event.ResetProgressEvent;
import org.ednovo.gooru.client.service.ClasspageService;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;


/*
 * 
 * @fileName : CollectionsPresenter.java
 *
 * @description : This is presenter class for CollectionsView.java
 *
 *
 * @version : 1.0
 *
 * @date: Apr 17, 2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class CollectionsPresenter extends ChildPresenter<CollectionsPresenter, IsCollectionsView> implements CollectionsUiHandlers {

	CollectionItemDo collectionItemDo = null;
	ClasspageService classpageService=null;
	
	private EditClasspagePresenter editClasspagePresenter=null;
		
	public CollectionsPresenter(IsCollectionsView childView) {
		super(childView);
	}

	
	/** 
	 * This method is to get the classpageService
	 */
	public ClasspageService getClasspageService() {
		return classpageService;
	}


	/** 
	 * This method is to set the classpageService
	 */
	public void setClasspageService(ClasspageService classpageService) {
		this.classpageService = classpageService;
	}
	
	public void updateClasspageItem(String classpageItemId,final String directionText,final String dueDate,final String readStatus){
		AppClientFactory.getInjector().getClasspageService().updateClasspageItem(classpageItemId, directionText, dueDate,readStatus,new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
//				if(directionText!=null){
//					getView().updateDirection(directionText);
//				}else if(dueDate!=null){
//					getView().updateDueDate(dueDate);
//				}else if(readStatus!=null){
//					getView().updateCollectionStatus(readStatus);
//				}
			}
		});
	}
	
	public void deleteClasspageItem(String classpageItemId){
		/*AppClientFactory.getInjector().getClasspageService().deleteClassPageItem(classpageItemId, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				//getView().removeClasspageItemWidget();
				//AppClientFactory.fireEvent(new ResetProgressEvent());
//				AppClientFactory.fireEvent(new RefreshAssignmentsListEvent());
			}
		});*/
	}


	public EditClasspagePresenter getEditClasspagePresenter() {
		return editClasspagePresenter;
	}


	public void setEditClasspagePresenter(
			EditClasspagePresenter editClasspagePresenter) {
		this.editClasspagePresenter = editClasspagePresenter;
	}
	
	


}
