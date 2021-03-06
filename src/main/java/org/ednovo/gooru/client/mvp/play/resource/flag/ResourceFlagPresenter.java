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
package org.ednovo.gooru.client.mvp.play.resource.flag;


import java.util.ArrayList;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.play.collection.CollectionPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.resource.ResourcePlayerPresenter;
import org.ednovo.gooru.client.service.PlayerAppServiceAsync;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ContentReportDo;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;



public class ResourceFlagPresenter extends PresenterWidget<IsResourceFlag> implements ResourceFlagUiHandler{
	
	@Inject
	private PlayerAppServiceAsync playerAppService;
	private PreviewPlayerPresenter previewPlayerPresenter;
	private CollectionPlayerPresenter collectionPlayerPresenter;
	private ResourcePlayerPresenter resourcePlayerPresenter;
	private boolean isPreviewPlayer=false,isCollectionPlayer=false,isResourcePlayer=false;
	
	@Inject
	public ResourceFlagPresenter(EventBus eventBus, IsResourceFlag view) {
		super(eventBus, view);
		getView().setUiHandlers(this);
	}
	public void setFlagView(CollectionItemDo collectionItemDo){
		getView().setFlagView(collectionItemDo);
	}
	public void setContentDeleteIds(String gooruFlagId){
		getView().setContentDeleteIds(gooruFlagId);
	}
	public void setPreviewPlayerPresenter(PreviewPlayerPresenter previewPlayerPresenter){
		this.previewPlayerPresenter=previewPlayerPresenter;
		isPreviewPlayer=true;
	}
	public void setCollectionPlayerPresenter(CollectionPlayerPresenter collectionPlayerPresenter){
		this.collectionPlayerPresenter=collectionPlayerPresenter;
		isCollectionPlayer=true;
	}
	public void setResourcePlayerPresenter(ResourcePlayerPresenter resourcePlayerPresenter) {
		this.resourcePlayerPresenter = resourcePlayerPresenter;
		isResourcePlayer=true;
	}
	@Override
	public void createReport(final String associatedGooruOid, String freeText,
			ArrayList<String> contentReportList,
			String deleteContentReportGooruOids,String collectionItemId) {
			ItemFlagDataLogEvent( associatedGooruOid, freeText, contentReportList,collectionItemId);
			playerAppService.createContentReport(associatedGooruOid, freeText, contentReportList, deleteContentReportGooruOids, new SimpleAsyncCallback<ContentReportDo>() {
			@Override
			public void onSuccess(ContentReportDo result) {
				getView().showSuccessPopup();
				if(previewPlayerPresenter!=null&&isPreviewPlayer){
					previewPlayerPresenter.isResourceContentReported(associatedGooruOid);
				}else if(collectionPlayerPresenter!=null&&isCollectionPlayer){
					collectionPlayerPresenter.getReportData(associatedGooruOid);
				}else if(resourcePlayerPresenter!=null&&isResourcePlayer){
					resourcePlayerPresenter.getContentReport(associatedGooruOid);
				}
			}
		});	
		
	}
	@Override
	public void getContentReport(String associatedGooruOid) {
		playerAppService.getContentReport(associatedGooruOid, AppClientFactory.getGooruUid(), new SimpleAsyncCallback<ArrayList<ContentReportDo>>() {
			@Override
			public void onSuccess(ArrayList<ContentReportDo> result) {
				String gooruFlagId="";
				if(result!=null &result.size()>0){
					for(int i =0;i<result.size();i++){
						 if(result.get(i).getDeleteContentGooruOid()!=null){
						  gooruFlagId = gooruFlagId+result.get(i).getDeleteContentGooruOid();
						  if(result.size()!=(i+1)){
								gooruFlagId=gooruFlagId+",";
								getView().getreportData(result.get(0), gooruFlagId);
							}
						 }
					}
				}else{
				}
			}
		});
	}
	
	public void ItemFlagDataLogEvent(String associatedGooruOid,String freeText,ArrayList<String> contentReportList,String collectionItemId){
	   if(isPreviewPlayer){
		   previewPlayerPresenter.triggerItemFlagDataLogEvent(PlayerDataLogEvents.getUnixTime(),PlayerDataLogEvents.RESOURCE,freeText,contentReportList,associatedGooruOid, collectionItemId);
	   }else if(isCollectionPlayer){
		   collectionPlayerPresenter.triggerItemFlagDataLogEvent(PlayerDataLogEvents.getUnixTime(),PlayerDataLogEvents.RESOURCE,freeText,contentReportList,associatedGooruOid, collectionItemId);
	   }else if(isResourcePlayer){
		   resourcePlayerPresenter.triggerItemFlagDataLogEvent(PlayerDataLogEvents.getUnixTime(),PlayerDataLogEvents.RESOURCE,freeText,contentReportList,associatedGooruOid, ""); 
	   }
	}
	public HTMLEventPanel getResourceFlagCloseButton()
	{
		return getView().resourceCloseButton();
	}
	public Button getSubmitButton()
	{
		return getView().getSubmitButton();
	}
	
}
