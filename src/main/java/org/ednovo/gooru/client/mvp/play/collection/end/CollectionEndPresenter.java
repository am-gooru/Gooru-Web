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
package org.ednovo.gooru.client.mvp.play.collection.end;



import java.util.ArrayList;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual.CollectionSummaryIndividualPresenter;
import org.ednovo.gooru.client.mvp.play.collection.CollectionPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.end.study.CollectionHomeMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.collection.event.EditCommentChildViewEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.SetPlayerLoginStatusEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdateCommentChildViewEvent;
import org.ednovo.gooru.client.mvp.play.collection.preview.end.PreviewEndPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.PreviewHomePresenter;
import org.ednovo.gooru.client.service.AnalyticsServiceAsync;
import org.ednovo.gooru.client.service.LibraryServiceAsync;
import org.ednovo.gooru.client.service.PlayerAppServiceAsync;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryUsersDataDo;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.ContentReportDo;
import org.ednovo.gooru.shared.model.library.ConceptDo;
import org.ednovo.gooru.shared.model.player.CommentsDo;
import org.ednovo.gooru.shared.model.player.CommentsListDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;

public class CollectionEndPresenter extends PresenterWidget<IsCollectionEndView> implements CollectionEndUiHandlers{

	@Inject
	private PlayerAppServiceAsync playerAppService;
	
	@Inject
	private AnalyticsServiceAsync analyticService;
	
	@Inject
	private LibraryServiceAsync libraryService;

    private SimpleAsyncCallback<CommentsListDo> commentsListDoAsync;
    
	private CollectionDo collectionDo=null;
	
	private CollectionPlayerPresenter collectionPlayerPresenter=null;
	
	private PreviewHomePresenter previewHomePresenter;
	
	private PreviewEndPresenter previewEndPresenter;
	
	private CollectionHomeMetadataPresenter collectionHomeMetadataPresenter;
	
	private CollectionSummaryIndividualPresenter collectionSummaryIndividualPresenter;
	
	public static final  Object METADATA_PRESENTER_SLOT = new Object();
	
	public static final  Object COLLECTION_REPORTS_SLOT=new Object();
	
	private static final String PAGE = "course-page";
	
	private static final String CREATE = "CREATE";
	
	private static final String EDIT = "EDIT";
	
	private static final String FEATCHINGCOMMENT = "FEATCHINGCOMMENT";
	
	private static final String INITIAL_COMMENT_LIMIT = "10";
	
	private static final String INITIAL_OFFSET = "0";
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@Inject
	public CollectionEndPresenter(EventBus eventBus, IsCollectionEndView view,PreviewHomePresenter previewHomePresenter,
			PreviewEndPresenter previewEndPresenter,CollectionHomeMetadataPresenter collectionHomeMetadataPresenter,CollectionSummaryIndividualPresenter collectionSummaryIndividualPresenter) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.previewHomePresenter=previewHomePresenter;
		this.previewEndPresenter=previewEndPresenter;
		this.collectionHomeMetadataPresenter=collectionHomeMetadataPresenter;
		this.collectionSummaryIndividualPresenter=collectionSummaryIndividualPresenter;
		addRegisteredHandler(SetPlayerLoginStatusEvent.TYPE, this);
		addRegisteredHandler(UpdateCommentChildViewEvent.TYPE, this);
		addRegisteredHandler(EditCommentChildViewEvent.TYPE, this);
	}
	
	public void setCollectionMetadata(final CollectionDo collectionDo){
		clearSlot(COLLECTION_REPORTS_SLOT);
		getView().resetCollectionMetaData();
		getView().getLoadingImageLabel().setVisible(true);
		this.collectionDo=collectionDo;
		getView().setCollectionMetadata(collectionDo);
		if(AppClientFactory.isAnonymous()) {
			getView().setPlayerLoginStatus(false);
		} else {
			getView().setPlayerLoginStatus(true);
		}
		getCollectionCommentsList(collectionDo.getGooruOid(), INITIAL_OFFSET, INITIAL_COMMENT_LIMIT);
		setRelatedConcepts(collectionDo);
	}
	
	public void displayAuthorDetails(boolean isDisplayDetails){
		getView().displayAuthorDetails(isDisplayDetails);
	}
	public void setPreviewHomePresenter(){
		previewHomePresenter.setCollectionMetadata(collectionDo);
		previewHomePresenter.removeAssignmentButtons();
		setInSlot(METADATA_PRESENTER_SLOT, previewHomePresenter,false);
	}
	public void setPreviewEndPresenter(){
		previewEndPresenter.setCollectionMetadata(collectionDo);
		setInSlot(METADATA_PRESENTER_SLOT, previewEndPresenter,false);
	}
	
	public void setStudyEndPage(){
		previewHomePresenter.setCollectionMetadata(collectionDo);
		previewHomePresenter.removeAssignmentImagetButtons(); 
		setInSlot(METADATA_PRESENTER_SLOT, previewHomePresenter,false);
	}
	
	public void setCollectionHomeMetadata(){
		collectionHomeMetadataPresenter.setCollectionMetadata(collectionDo);
		setInSlot(METADATA_PRESENTER_SLOT, collectionHomeMetadataPresenter,false);
	}
	
	public void setCollectionSummaryBasedOnClasspageIdSessionId(){
		String classpageId=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
		if(classpageId==null){
			classpageId="";
		}
		getSessionsDataByUser(collectionDo.getGooruOid(),classpageId,AppClientFactory.getLoggedInUser().getGooruUId());
	}
	
	public void setCollectionSummaryData(String collectionId,String classpageId,String userId,String sessionId){
		getView().getLoadingImageLabel().setVisible(true);
		clearSlot(COLLECTION_REPORTS_SLOT);
		collectionSummaryIndividualPresenter.setIndividualData(collectionId, classpageId, userId, sessionId,"",false,getView().getLoadingImageLabel(),null);
		setInSlot(COLLECTION_REPORTS_SLOT,collectionSummaryIndividualPresenter,false);
	}
	public void setViewCount(String viewCount){
		getView().setViewCount(viewCount);
	}
	
	public void updateLikesCount(int likesCount){
		//getView().setLikesCount(likesCount);
	}
	
	public void setUserProfileName(String gooruUid){
		getView().setUserProfileName(gooruUid);
	}
	
	public void resetMetadataFields(){
		getView().resetMetadataFields();
	}

	public PlayerAppServiceAsync getPlayerAppService() {
		return playerAppService;
	}

	public void setPlayerAppService(PlayerAppServiceAsync playerAppService) {
		this.playerAppService = playerAppService;
	}
	
	public SimpleAsyncCallback<CommentsListDo>  getCommentsListDoAsync() {
		return commentsListDoAsync;
	}

	public void setCommentsListDoAsync(SimpleAsyncCallback<CommentsListDo> commentsListDoAsync) {
		this.commentsListDoAsync = commentsListDoAsync;
	}
	

	@Override
	public void setPlayerLoginStatusHandler(boolean isLoggedIn) {
		getView().setPlayerLoginStatus(isLoggedIn);
	}
	
	private void getCollectionCommentsList(String gooruOid, final String offset, String limit) {
		this.playerAppService.getCollectionCommentsList(gooruOid, offset, limit, new SimpleAsyncCallback<CommentsListDo>() {
			@Override
			public void onSuccess(CommentsListDo commentDoList) {
				getView().setCommentsData(commentDoList,collectionDo, Integer.parseInt(offset) >0 ? false : true);
			}
		});
	}

	@Override
	public void createCommentForCollection(String gooruOid, String comment) {
		this.playerAppService.createCommentForCollection(gooruOid, comment, new SimpleAsyncCallback<CommentsDo>() {
			@Override
			public void onSuccess(CommentsDo commentsDo) {
				getView().setCommentsWidget(commentsDo, CREATE);
				getView().displaySuccessMsg(false);
			}
		});
	}

	@Override
	public void updateCommentChildView(String commentUid, String action) {
		getView().updateCommentChildView(commentUid, action);
	}

	@Override
	public void deleteCommentFromCollection(final String gooruOid,String commentUid,final String offset, final String limit) {
		this.playerAppService.deleteCollectionCommentbyCommentUid(commentUid, new SimpleAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void noResult) {
				if(Integer.parseInt(offset)>=9)
				insertCommentAfterDeletion(gooruOid, offset, limit);
			}
		});
	}
	public void insertCommentAfterDeletion(final String gooruOid,final String offset, String limit){
		this.playerAppService.getCollectionCommentsList(gooruOid, offset, limit, new SimpleAsyncCallback<CommentsListDo>() {
			@Override
			public void onSuccess(CommentsListDo commentDoList) {
				getView().setCommentsWidget(commentDoList.getSearchResults().get(0), FEATCHINGCOMMENT);
			}
		});
	}
	@Override
	public void editCommentChildView(String commentUid, String commentText, String action) {
		this.playerAppService.updateCollectionCommentbyCommentUid(commentUid, commentText, new SimpleAsyncCallback<CommentsDo>() {
			@Override
			public void onSuccess(CommentsDo result) {
				getView().updateCommentChildView("", EDIT);
			}
		});
	}

	@Override
	public void getPaginationResults(String gooruOid, String offset, String limit) {
		getCollectionCommentsList(gooruOid, offset, limit);
	}

	
	
	public void getFlagedReport(String gooruOid) {
		playerAppService.getContentReport(collectionDo.getGooruOid(), AppClientFactory.getGooruUid(), new SimpleAsyncCallback<ArrayList<ContentReportDo>>() {
			@Override
			public void onSuccess(ArrayList<ContentReportDo> result) {
				String gooruFlagId="";
				if(result.size()==0){
					getView().getFlagButton().setText(i18n.GL0556());
					getView().getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
					getView().getFlagButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
				}else{
					for(int i =0;i<result.size();i++){
						gooruFlagId = gooruFlagId+result.get(i).getDeleteContentGooruOid();
						getView().getFlagButton().setText(i18n.GL0557());
						getView().getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
						getView().getFlagButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
						
					}
			}
			}
		});
		
	}

	public void setRelatedConcepts(final CollectionDo collectionDo) {
		final String subject = AppClientFactory.getPlaceManager().getRequestParameter("subject");
		final String lessonId = AppClientFactory.getPlaceManager().getRequestParameter("lessonId", "123");
		final String libraryType = AppClientFactory.getPlaceManager().getRequestParameter("library", PlaceTokens.DISCOVER);
		getView().hideNextCollectionContainer(true);
		if(subject!=null) {
			this.libraryService.getLibraryCollections(subject, lessonId, libraryType, new SimpleAsyncCallback<ArrayList<ConceptDo>>() {
				@Override
				public void onSuccess(ArrayList<ConceptDo> conceptDoList) {
					getView().isConceptsContainerVisible(true);
					getView().setRelatedConceptsContent(conceptDoList, PAGE, subject, lessonId, libraryType);
					if(conceptDoList!=null){
						for(int i=0;i<conceptDoList.size();i++){
							if(!conceptDoList.get(i).getGooruOid().equals(collectionDo.getGooruOid())){
								getNextCollectionDetails(conceptDoList.get(i).getGooruOid(),PAGE,subject,lessonId,libraryType);
								return;
							}
						}
					}
				}
			});
		} else {
			getView().isConceptsContainerVisible(false);
		}
	}
	
	public void getNextCollectionDetails(String gooruOid, String page, final String subject,final  String lessonId, final String libraryType){
		getView().hideNextCollectionContainer(false);
		AppClientFactory.getInjector().getPlayerAppService().getSimpleCollectionDetils(null, gooruOid, null, null, null, new SimpleAsyncCallback<CollectionDo>() {
			@Override
			public void onSuccess(CollectionDo result) {
				getView().displayNextCollectionDetails(result,subject, lessonId,libraryType);
			}
		});
	}

	public void setCollectionDoOnRefresh(CollectionDo collectionDo) { 
		this.collectionDo = collectionDo;
		getView().setCollectionMetadata(collectionDo);
		setRelatedConcepts(collectionDo);
	}

	public CollectionPlayerPresenter getCollectionPlayerPresenter() {
		return collectionPlayerPresenter;
	}

	public void setCollectionPlayerPresenter(CollectionPlayerPresenter collectionPlayerPresenter) {
		this.collectionPlayerPresenter = collectionPlayerPresenter;
		previewHomePresenter.setCollectionPlayerPresenter(collectionPlayerPresenter);
	}

	public void setTeacherInfo(ClasspageItemDo classpageItemDo) { 
		getView().setTeacherInfo(classpageItemDo);
	}

	public void setDataInsightsSummaryUrl(String sessionId){
		getView().setDataInsightsSummaryUrl(sessionId);
	}
	
	public void setDataInsightsUrl(){
		getView().setDataInsightsUrl();
	}
	public Button getBackToClassButton(){
		return previewHomePresenter.getBackToClassButton();
	}
	public void clearDashBoardIframe(){
		getView().clearDashBoardIframe();
	}
	public void setClasspageInsightsUrl(String classpageId, String sessionId){
		getView().setClasspageInsightsUrl(classpageId, sessionId);
	}

	@Override
	public void resetCollectionActivityEventId() {
		collectionPlayerPresenter.resetcollectionActivityEventId();
	}
	
	@Override
	public void triggerCollectionShareDataEvent(String collectionId,String itemType, String shareType, boolean confirmStatus) {
		if(collectionPlayerPresenter!=null){
			collectionPlayerPresenter.triggerCollectionShareDataEvent( collectionId, itemType,  shareType,  confirmStatus);
		}
	}
	public void displaySpendTime(Long hours,Long mins, Long secs){
		getView().displaySpendTime( hours,mins,secs);
	}
	public void displayScoreCount(Integer collectionScore,Integer noOfQuestions){
		getView().displayScoreCount(collectionScore,noOfQuestions);
	}

	@Override
	public void getAvgReaction() {
		if(collectionPlayerPresenter!=null){
			Map<String, Integer> reactionTree=collectionPlayerPresenter.getReactionTreeMap();
			Integer maxValue=0;
			String reactiontype=null;
			if(reactionTree.size()>0){
				for(Map.Entry<String,Integer> entry : reactionTree.entrySet()){
					Integer reactionValue=entry.getValue();
					if(reactionValue>maxValue){
						maxValue=reactionValue;
						reactiontype=entry.getKey();
					}
				}
			}
			getView().showAvgReaction(reactiontype);
		}else{
			getView().showAvgReaction(null);
		}
		
	}

	@Override
	public void getCollectionMetaDataByUserAndSession(final String collectionId, final String classId, final String userId, final String sessionId) {
		this.analyticService.getCollectionMetaDataByUserAndSession(collectionId, classId, userId, sessionId, new AsyncCallback<ArrayList<CollectionSummaryMetaDataDo>>() {
			
			@Override
			public void onSuccess(ArrayList<CollectionSummaryMetaDataDo> result) {
				if(result.get(0).getCompletionStatus()!=null){
					if(result.get(0).getCompletionStatus().equalsIgnoreCase("in-progress")){
						getCollectionMetaDataByUserAndSession(collectionId, classId, userId, sessionId);
					}else{
						if(result.size()!=0){
							getView().setCollectionMetaDataByUserAndSession(result);
							setCollectionSummaryData(collectionId, classId, userId, sessionId);
						}
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

	public AnalyticsServiceAsync getAnalyticService() {
		return analyticService;
	}

	public void setAnalyticService(AnalyticsServiceAsync analyticService) {
		this.analyticService = analyticService;
	}

	@Override
	public void getSessionsDataByUser(final String collectionId,final String classId,final String userId) {
		this.analyticService.getSessionsDataByUser(collectionId, classId, userId, new AsyncCallback<ArrayList<CollectionSummaryUsersDataDo>>() {
			
			@Override
			public void onSuccess(ArrayList<CollectionSummaryUsersDataDo> result) {
				if(result.size()!=0){
					getCollectionMetaDataByUserAndSession(collectionId, classId, userId, result.get(result.size()-1).getSessionId());
					getView().setSessionsData(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
}
