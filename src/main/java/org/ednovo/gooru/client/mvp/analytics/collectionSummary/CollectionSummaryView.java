package org.ednovo.gooru.client.mvp.analytics.collectionSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.SimpleRunAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.analytics.util.AnalyticsUtil;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryUsersDataDo;
import org.ednovo.gooru.shared.model.analytics.PrintUserDataDO;
import org.ednovo.gooru.shared.model.analytics.UserDataDo;
import org.ednovo.gooru.shared.util.ClientConstants;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleRadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CollectionSummaryView  extends BaseViewWithHandlers<CollectionSummaryUiHandlers> implements IsCollectionSummaryView,ClientConstants {

	private static CollectionSummaryViewUiBinder uiBinder = GWT
			.create(CollectionSummaryViewUiBinder.class);

	interface CollectionSummaryViewUiBinder extends
			UiBinder<Widget, CollectionSummaryView> {
	}

	CollectionSummaryCBundle res;
	
	@UiField ListBox studentsListDropDown,sessionsDropDown;
	@UiField Image exportImage,sessionsTooltip,imgQuestionMark;
	@UiField InlineLabel lastModifiedTime;
	@UiField HTMLPanel collectionSummaryDetails,sessionspnl,loadingImageLabel1;
	@UiField VerticalPanel pnlSummary;
	@UiField Frame downloadFile;
	@UiField Label subText,errorMessage;
	
	Map<String, String> sessionData=new HashMap<String, String>();
	ToolTip toolTip;
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	String collectionId=null,pathwayId=null,classpageId=null;
	CollectionSummaryWidget collectionSummaryWidget=new CollectionSummaryWidget();
	PrintUserDataDO printUserDataDO=new PrintUserDataDO();
	
	/**
	 * Constructor
	 */
	public CollectionSummaryView() {
		this.res = CollectionSummaryCBundle.INSTANCE;
		res.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		setData();
		setStaticData();
		downloadFile.setVisible(false);
		errorMessage.setVisible(false);
	}
	/**
	 * This method is used to set static data.
	 */
	void setStaticData(){
		StringUtil.setAttributes(collectionSummaryDetails.getElement(), "pnlCollectionSummaryDetails", null, null);
		StringUtil.setAttributes(sessionspnl.getElement(), "pnlSessionspnl", null, null);
		StringUtil.setAttributes(loadingImageLabel1.getElement(), "pnlLoadingImage", null, null);
		StringUtil.setAttributes(pnlSummary.getElement(), "pnlSummary", null, null);
		
		StringUtil.setAttributes(studentsListDropDown.getElement(), "ddlStudentsListDropDown", null, null);
		StringUtil.setAttributes(sessionsDropDown.getElement(), "ddlSessionsDropDown", null, null);
		
		StringUtil.setAttributes(exportImage.getElement(), "imgExportImage", null, null);
		StringUtil.setAttributes(sessionsTooltip.getElement(), "imgSessionsTooltip", null, null);
		
		StringUtil.setAttributes(lastModifiedTime.getElement(), "lblLastModifiedTime", null, null);
	}
	/**
	 * This method is used to set default data.
	 */
	void setData(){
		sessionspnl.setVisible(false);
		
		studentsListDropDown.addChangeHandler(new StudentsListChangeHandler());
		sessionsDropDown.addChangeHandler(new StudentsSessionsChangeHandler());

		sessionsTooltip.addMouseOverHandler(new QuestionMouseToolTip(ONE, sessionsTooltip));
		imgQuestionMark.addMouseOverHandler(new QuestionMouseToolTip(TWO, imgQuestionMark));
		
		sessionsTooltip.addMouseOutHandler(new QuestionMouseOutToolTip());
		imgQuestionMark.addMouseOutHandler(new QuestionMouseOutToolTip());
	}
	/**
	 * This inner class is used to handle the mouse over events.
	 */
	public class QuestionMouseToolTip implements MouseOverHandler{
		String fromString="";
		Image image;
		QuestionMouseToolTip(String fromString,Image image){
			this.fromString=fromString;
			this.image=image;
		}
		@Override
		public void onMouseOver(MouseOverEvent event) {
			GWT.runAsync(new SimpleRunAsyncCallback() {
				
				@Override
				public void onSuccess() {

					String setText="";
					if(ONE.equalsIgnoreCase(fromString)){
						setText=i18n.GL3091();
					}else if(TWO.equalsIgnoreCase(fromString)){
						setText=i18n.GL3088();
					}
					toolTip = new ToolTip(setText,"");
					toolTip.getTootltipContent().getElement().setAttribute("style", "width: 258px;");
					toolTip.getElement().getStyle().setBackgroundColor("transparent");
					toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
					toolTip.setPopupPosition(image.getAbsoluteLeft()-(50+22), image.getAbsoluteTop()+22);
					toolTip.show();
				
				}
			});
		}
	}
	/**
	 * This class is used to hide the popup
	 */
	public class QuestionMouseOutToolTip implements MouseOutHandler{
		@Override
		public void onMouseOut(final MouseOutEvent event) {
			GWT.runAsync(new SimpleRunAsyncCallback() {
				
				@Override
				public void onSuccess() {

					EventTarget target = event.getRelatedTarget();
					  if (Element.is(target)) {
						  if (!toolTip.getElement().isOrHasChild(Element.as(target))){
							  toolTip.hide();
						  }
					  }
				
				}
			});
		}
	}
    public class StudentsListChangeHandler implements ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			GWT.runAsync(new SimpleRunAsyncCallback() {
				
				@Override
				public void onSuccess() {

					errorMessage.setVisible(false);
					int selectedIndex=studentsListDropDown.getSelectedIndex();
					String classpageId=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
					if(selectedIndex==0){
						sessionspnl.setVisible(false);
						collectionSummaryWidget.getCollectionLastAccessPnl().setVisible(true);
						getUiHandlers().setTeacherData(collectionId,classpageId,pathwayId);
					}else{
						printUserDataDO.setUserName(studentsListDropDown.getItemText(studentsListDropDown.getSelectedIndex()));
						getUiHandlers().loadUserSessions(collectionId, classpageId, studentsListDropDown.getValue(selectedIndex),pathwayId,printUserDataDO);
						sessionspnl.setVisible(true);
						collectionSummaryWidget.getCollectionLastAccessPnl().setVisible(false);
					}
				
				}
			});
		}
    }
    public class StudentsSessionsChangeHandler implements ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			GWT.runAsync(new SimpleRunAsyncCallback() {
				
				@Override
				public void onSuccess() {

					int selectedSessionIndex=sessionsDropDown.getSelectedIndex();
					int selectedStudentIndex=studentsListDropDown.getSelectedIndex();
					String classpageId=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
	                setSessionStartTime(selectedSessionIndex);
	                printUserDataDO.setUserName(studentsListDropDown.getItemText(selectedStudentIndex));
	                printUserDataDO.setSession(sessionsDropDown.getItemText(selectedSessionIndex));
					getUiHandlers().setIndividualData(collectionId, classpageId, studentsListDropDown.getValue(selectedStudentIndex),sessionsDropDown.getValue(selectedSessionIndex),pathwayId,printUserDataDO);
			
				}
			});
		}
    }
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.IsCollectionSummaryView#setUsersData(java.util.ArrayList)
	 */
	@Override
	public void setUsersData(final ArrayList<CollectionSummaryUsersDataDo> result) {
		GWT.runAsync(new SimpleRunAsyncCallback() {
			
			@Override
			public void onSuccess() {

				errorMessage.setVisible(false);
				studentsListDropDown.clear();
				studentsListDropDown.addItem(ALL_STUDENTS);
				for (CollectionSummaryUsersDataDo collectionSummaryUsersDataDo : result) {
					if(!StringUtil.isEmpty(collectionSummaryUsersDataDo.getUserName()) && !StringUtil.isEmpty(collectionSummaryUsersDataDo.getGooruUId())){
						studentsListDropDown.addItem(collectionSummaryUsersDataDo.getUserName(),collectionSummaryUsersDataDo.getGooruUId());
					}
				}
				sessionspnl.setVisible(false);
				String tabReports=AppClientFactory.getPlaceManager().getRequestParameter("tab", null);
				if(REPORTS.equalsIgnoreCase(tabReports)){ 
					exportImage.setVisible(true);
					subText.setVisible(true);
				}else{
					exportImage.setVisible(false);
					subText.setVisible(false);
				}
				collectionSummaryWidget.getCollectionLastAccessPnl().setVisible(true);
			
			}
		} );
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.IsCollectionSummaryView#setCollectionMetaData(org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo, java.lang.String)
	 */
	@Override
	public void setCollectionMetaData(
			final CollectionSummaryMetaDataDo result,String pathwayId,String classpageId) {
		this.classpageId=classpageId;
		this.pathwayId=pathwayId;
		GWT.runAsync(new SimpleRunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				if(!StringUtil.checkNull(result)){
					collectionId=result.getGooruOId();
					collectionSummaryWidget.setData(result);
					collectionSummaryDetails.add(collectionSummaryWidget);
				}
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.IsCollectionSummaryView#setCollectionResourcesData(java.util.ArrayList)
	 */
	@Override
	public void setCollectionResourcesData(ArrayList<UserDataDo> result) {
		
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.IsCollectionSummaryView#setUserSessionsData(java.util.ArrayList)
	 */
	@Override
	public void setUserSessionsData(final 
			ArrayList<CollectionSummaryUsersDataDo> result) {
		GWT.runAsync(new SimpleRunAsyncCallback() {
			
			@Override
			public void onSuccess() {

				sessionsDropDown.clear();
				sessionData.clear();
				for (CollectionSummaryUsersDataDo collectionSummaryUsersDataDo : result) {
					sessionData.put(collectionSummaryUsersDataDo.getSessionId(), AnalyticsUtil.getSessionsCreatedTime(Long.toString(collectionSummaryUsersDataDo.getTimeStamp())));
					int day=collectionSummaryUsersDataDo.getFrequency();
					sessionsDropDown.addItem(day+AnalyticsUtil.getOrdinalSuffix(day)+" Session",collectionSummaryUsersDataDo.getSessionId());
				}
				setSessionStartTime(0);
			
			}
		});
	}
	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.ViewImpl#setInSlot(java.lang.Object, com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void setInSlot(Object slot, Widget content) {
		pnlSummary.clear();
		if (content != null) {
			 if(slot==CollectionSummaryPresenter.TEACHER_STUDENT_SLOT){
				 pnlSummary.setVisible(true);
				 pnlSummary.add(content);
			}else{
				pnlSummary.setVisible(false);
			}
		}else{
			pnlSummary.setVisible(false);
		}
	}
	/**
	 * This method is used to set session start time.
	 * @param selectedIndex
	 */
	public void setSessionStartTime(int selectedIndex) {
		if(sessionData.size()!=0){
			  lastModifiedTime.setText(sessionData.get(sessionsDropDown.getValue(selectedIndex)).toString());
			  printUserDataDO.setSessionStartTime(sessionData.get(sessionsDropDown.getValue(selectedIndex)).toString());
		}
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.IsCollectionSummaryView#getLoadinImage()
	 */
	@Override
	public HTMLPanel getLoadinImage() {
		return loadingImageLabel1;
	}
	
	@UiHandler("exportImage")
	public void clickedOnExport(ClickEvent e){
		GWT.runAsync(new SimpleRunAsyncCallback() {
			
			@Override
			public void onSuccess() {

				getUiHandlers().exportCollectionSummary(collectionId, classpageId, "", "", pathwayId, AnalyticsUtil.getTimeZone());
			
			}
		});
	}
	@Override
	public Frame getFrame() {
		return downloadFile;
	}
	@Override
	public void resetDataIfNoSessions() {
		GWT.runAsync(new SimpleRunAsyncCallback() {
			
			@Override
			public void onSuccess() {

				lastModifiedTime.setText("");
				sessionspnl.setVisible(false);
				errorMessage.setVisible(true);
				collectionSummaryWidget.getCollectionLastAccessPnl().setVisible(true);
			
			}
		});
	}
}
