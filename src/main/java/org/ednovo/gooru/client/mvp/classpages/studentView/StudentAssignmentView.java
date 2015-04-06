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
package org.ednovo.gooru.client.mvp.classpages.studentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.classpages.edit.AssignmentProgressVc;
import org.ednovo.gooru.client.mvp.classpages.edit.EditClasspageCBundle;
import org.ednovo.gooru.client.mvp.classpages.event.OpenJoinClassPopupEvent;
import org.ednovo.gooru.client.mvp.classpages.event.OpenJoinClassPopupHandler;
import org.ednovo.gooru.client.mvp.classpages.event.SetSelectedClasspageListEvent;
import org.ednovo.gooru.client.mvp.home.LoginPopupUc;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.DeletePopupViewVc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.SuccessPopupViewVc;
import org.ednovo.gooru.client.mvp.socialshare.SentEmailSuccessVc;
import org.ednovo.gooru.client.uc.PaginationButtonUc;
import org.ednovo.gooru.client.uc.tooltip.LibraryTopicCollectionToolTip;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClasspageDo;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
/**
 * 
 * @fileName : StudentAssignmentView.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: Nov 22, 2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class StudentAssignmentView extends BaseViewWithHandlers<StudentAssignmentUiHandlers> implements IsStudentAssignmentView ,ClickHandler{
	
	private static StudentAssignmentViewUiBinder uiBinder = GWT.create(StudentAssignmentViewUiBinder.class);
	

	interface StudentAssignmentViewUiBinder extends UiBinder<Widget, StudentAssignmentView> {    

	}
	
	@UiField Label mainTitleLbl,lblUserName;
	
	@UiField FlowPanel studentsetupContainer;
	
	@UiField
	static HTMLPanel mainContainer,lineSeparation,memberContainer;
	
	@UiField Button backToEditPanel;

	@UiField
	static Button btnJoinClass;

	@UiField
	static
	Button btnWithDraw;
	
//	@UiField HTMLEventPanel backToTeacherView;
	
	@UiField Image studentViewImage,imgProfileImage;

//	@UiField
//	static Image userImage;
	
	@UiField
	static Label lblWebHelp;
	
//	@UiField ScrollPanel spanelSutdentsList;
	
//	@UiField HTMLPanel panelSutdentsList,teacherViewPanel;

	@UiField
	static
	Label LblMember;
	
	static StudentJoinClassPopup joinPopupPrivate;
	
	static StudentJoinClassPopup joinPopupPublic;
	
	static StudentJoinClassPopup joinPopupButtonClick;
	
	static ClasspageDo classpageDo;
	
	String defaultProfileImage = "images/settings/setting-user-image.png";
	
	private static boolean isJoinPopupPublic = false;
	
	private static boolean isJoinPopupPrivate = false;
	
	private static boolean isJoinPopupPrivateStatic = false;
	
	private static boolean isJoinPopupPublicStatic = false;
	
	private static boolean isJoinPopupButtonclick = false;
	
	private static final String PREVIOUS = "PREVIOUS";

	private static final String NEXT = "NEXT";
	
	private String DEFAULT_CLASSPAGE_IMAGE = "images/Classpage/default-classpage.png";
	private int totalHitCount=0;
	private int limit=5;
	private int pageNumber=1;
	private PopupPanel toolTipPopupPanel = new PopupPanel();
	
	public static boolean islogin = false;
	public static boolean isloginPrivate = false;
	public static boolean isloginButtonClick = false;
	
	private Map<String,AssignmentProgressVc> assignmentsDotsMap=new HashMap<String,AssignmentProgressVc>();
	
	List<String> sortingOptionsList=new ArrayList<String>();
	
	private Integer defaultOffsetForPath=0;
	private Integer defaultLimitForPath=20;
	
	public static final MessageProperties i18n = GWT.create(MessageProperties.class); 
	
	@Inject
	public StudentAssignmentView() {
		setWidget(uiBinder.createAndBindUi(this));
		EditClasspageCBundle.INSTANCE.css().ensureInjected();
		setStaticData();
//		teacherViewPanel.setVisible(false);
		lblWebHelp.addMouseOverHandler(new OnMouseOver());
		lblWebHelp.addMouseOutHandler(new OnMouseOut());
		OpenJoinClassPopupHandler openJoinClassPopupHandler=new OpenJoinClassPopupHandler() {
			
			@Override
			public void openJoinClassPopup() {
				openJoinClassEvent();
			}
		};
		
		AppClientFactory.getEventBus().addHandler(OpenJoinClassPopupEvent.TYPE,openJoinClassPopupHandler);

		//addSortingOptionsToList();
		//addSortEventToText();
/*		dropdownPlaceHolder.setText(i18n.GL1946());
		dropdownPlaceHolder.getElement().setId("lblDropdownPlaceHolder");
		dropdownPlaceHolder.getElement().setAttribute("alt",i18n.GL1946());
		dropdownPlaceHolder.getElement().setAttribute("title",i18n.GL1946());
		
		dropDownListContainer.setVisible(false);
		
		lblAssignmentProgress.setText(i18n.GL1971());
		lblAssignmentProgress.getElement().setId("lblAssignmentProgress");
		lblAssignmentProgress.getElement().setAttribute("alt",i18n.GL1971());
		lblAssignmentProgress.getElement().setAttribute("title",i18n.GL1971());
		assignmentOrderPanel.getElement().setInnerHTML(i18n.GL2006());
		dropdownPlaceHolder.addClickHandler(new SortDropDownEvent());*/
/*		
		lblNext.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {				
				defaultOffsetForPath = defaultOffsetForPath +defaultLimitForPath;
				
				callAssignmentAPI(AppClientFactory.getPlaceManager().getRequestParameter("id"), defaultOffsetForPath.toString(), defaultLimitForPath.toString());
			}
		});*/
	
/*		lblPrevious.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (defaultOffsetForPath <=0){
					defaultOffsetForPath =0;
				}else{
					defaultOffsetForPath = defaultOffsetForPath - defaultLimitForPath;
				}
				
				callAssignmentAPI(AppClientFactory.getPlaceManager().getRequestParameter("id"), defaultOffsetForPath.toString(), defaultLimitForPath.toString());
				
			}
		});*/
		
		//lblNext.setVisible(false);
		//lblPrevious.setVisible(false);
		
/*		Event.addNativePreviewHandler(new NativePreviewHandler() {
	        public void onPreviewNativeEvent(NativePreviewEvent event) {
	        	hideDropDown(event);
	          }
	    });*/
	}
	
/*	private void addSortEventToText(){
		if(sortingOptionsList.size()>0){
			for(int i=0;i < sortingOptionsList.size();i++){
				String sortType=sortingOptionsList.get(i);
				Label sortingLabel=new Label(sortType);
				sortingLabel.setStyleName(EditClasspageCBundle.INSTANCE.css().dropdownTextLabel());
				//dropDownListContainer.add(sortingLabel);
				sortingLabel.addClickHandler(new SortAssignmentEvents(sortType));
			}
		}
		
	}*/
/*	public class SortDropDownEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			new CustomAnimation(dropDownListContainer).run(300);
		}
	}
	*/
	/*public class SortAssignmentEvents implements ClickHandler{
		private String sortType=null;
		public SortAssignmentEvents(){}
		public SortAssignmentEvents(String sortType){
			this.sortType=sortType;
		}
		@Override
		public void onClick(ClickEvent event) {
			if(!dropdownPlaceHolder.getText().equals(sortType)){
				dropdownPlaceHolder.setText(sortType);
				dropdownPlaceHolder.getElement().setAttribute("alt",sortType);
				dropdownPlaceHolder.getElement().setAttribute("title",sortType);
				String sortingStringValue="";
				if(sortType.equals(i18n.GL1946())){
					sortingStringValue="all";
				}else if(sortType.equals(i18n.GL1952())){
					sortingStringValue="completed";
				}else if(sortType.equals(i18n.GL1953())){
					sortingStringValue="todo";
				}
				contentpanel.clear();
				noAssignmentMsg.setVisible(false);
				contentpanel.add(setLoadingPanel());
				dropDownListContainer.setVisible(false);
				
				Map<String,String> params = new HashMap<String,String>();
				params = StringUtil.splitQuery(Window.Location.getHref());
				params.put("order", sortingStringValue);
				params.put("pageNum", "1");
				
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.STUDENT, params);
				AppClientFactory.getPlaceManager().revealPlace(true, placeRequest, false);
			}else{
				dropDownListContainer.setVisible(false);
			}

		}
	}*/
	
	
	/*public void hideDropDown(NativePreviewEvent event){
    	if(event.getTypeInt()==Event.ONCLICK){
    		Event nativeEvent = Event.as(event.getNativeEvent());
        	boolean target=eventTargetsPopup(nativeEvent);
        	if(!target){
        		//dropDownListContainer.setVisible(false);
        	}
    	}
     }*/
/*	private boolean eventTargetsPopup(NativeEvent event) {
		EventTarget target = event.getEventTarget();
		if (Element.is(target)) {
			return dropDownListContainer.getElement().isOrHasChild(Element.as(target))||dropdownPlaceHolder.getElement().isOrHasChild(Element.as(target));
		}
		return false;
	}*/

	private void setStaticData() {
		backToEditPanel.setText(i18n.GL1130());
		backToEditPanel.getElement().setAttribute("alt",i18n.GL1130());
		backToEditPanel.getElement().setAttribute("title",i18n.GL1130());
		backToEditPanel.getElement().setId("btnBackToEdit");
/*		
		noAssignmentMsg.setText(i18n.GL1131());
		noAssignmentMsg.getElement().setId("lblNoAssignmentMsg");
		noAssignmentMsg.getElement().setAttribute("alt",i18n.GL1131());
		noAssignmentMsg.getElement().setAttribute("title",i18n.GL1131());
		*/
		btnJoinClass.setText(i18n.GL1536());
		btnJoinClass.getElement().setAttribute("alt",i18n.GL1536());
		btnJoinClass.getElement().setAttribute("title",i18n.GL1536());
		
		btnWithDraw.setText(i18n.GL1537());
		btnWithDraw.getElement().setId("lblNoAssignmentMsg");
		btnJoinClass.getElement().setAttribute("alt",i18n.GL1537());
		btnJoinClass.getElement().setAttribute("title",i18n.GL1537());
		
		LblMember.setText(i18n.GL1549());
		LblMember.getElement().setId("lblMember");
		LblMember.getElement().setAttribute("alt",i18n.GL1549());
		LblMember.getElement().setAttribute("title",i18n.GL1549());
		
	
		//noAssignmentMsg.setVisible(false);
		btnJoinClass.setVisible(false);
		lblWebHelp.setVisible(false);
		lblWebHelp.getElement().setId("lblWebHelp");
		btnWithDraw.setVisible(false);
		LblMember.setVisible(false);
//		userImage.setVisible(false);
//		userImage.getElement().setId("imgUserImage");
		mainContainer.getElement().setId("pnlMainContainer");
		studentViewImage.getElement().setId("imgStudentView");
		mainTitleLbl.getElement().setId("lblMainTitle");
		imgProfileImage.getElement().setId("imgProfileImage");
		lblUserName.getElement().setId("lblUserName");
		btnJoinClass.getElement().setId("btnJoinClass");
/*		panelProgressContainer.getElement().setId("pnlProgressContainer");
		panelPrevious.getElement().setId("pnlPrevious");
		lblPrevious.getElement().setId("lblPrevious");
		panelAssignmentPath.getElement().setId("pnlAssignmentPath");
		panelAssignmentProgress.getElement().setId("pnlAssignmentProgress");*/
	//	panelNext.getElement().setId("pnlNext");
		//lblNext.getElement().setId("lblNext");
/*		dropDownListContainer.getElement().setId("fpnlDropDownListContainer");
		paginationFocPanel.getElement().setId("fpnlPaginationFoc");
		paginationFocPanel1.getElement().setId("fpnlPaginationFoc1");
		contentpanel.getElement().setId("pnlContent");*/
	}

	@Override
	public void setClasspageData(ClasspageDo classpageDo) {
		this.classpageDo=classpageDo;
		mainTitleLbl.setText(classpageDo.getTitle() !=null ? classpageDo.getTitle() : "");
		mainTitleLbl.getElement().setAttribute("alt",classpageDo.getTitle() !=null ? classpageDo.getTitle() : "");
		mainTitleLbl.getElement().setAttribute("title",classpageDo.getTitle() !=null ? classpageDo.getTitle() : "");
		studentViewImage.setAltText(classpageDo.getTitle() !=null ? classpageDo.getTitle() : "");
		studentViewImage.setTitle(classpageDo.getTitle() !=null ? classpageDo.getTitle() : "");
		studentViewImage.setUrl(classpageDo.getThumbnailUrl() == "" ? DEFAULT_CLASSPAGE_IMAGE : classpageDo.getThumbnailUrl());
		AppClientFactory.fireEvent(new SetSelectedClasspageListEvent(classpageDo.getClasspageId()));
		imgProfileImage.setUrl(classpageDo.getCreatorProfileImage());
		lblUserName.setText(classpageDo.getCreatorUsername() + "'s " + i18n.GL0102().toLowerCase());
		lblUserName.getElement().setAttribute("alt",classpageDo.getCreatorUsername() + "'s " + i18n.GL0102().toLowerCase());
		lblUserName.getElement().setAttribute("title",classpageDo.getCreatorUsername() + "'s " + i18n.GL0102().toLowerCase());
		imgProfileImage.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				imgProfileImage.setUrl(defaultProfileImage);
			}
		});
		
		if(classpageDo.getCreatorId().equalsIgnoreCase(AppClientFactory.getGooruUid())){
			System.out.println("teacher view");
//			teacherViewPanel.setVisible(true);
		}
		
		studentViewImage.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				studentViewImage.setUrl(DEFAULT_CLASSPAGE_IMAGE);
			}
		});

		
		if(classpageDo.getSharing().equalsIgnoreCase("public"))
		{
			if(classpageDo.getCreatorId().equalsIgnoreCase(AppClientFactory.getGooruUid()))
			{
				btnJoinClass.setVisible(false);
//				userImage.setVisible(true);
				lblWebHelp.setVisible(false);
				btnWithDraw.setVisible(false);
				memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().techerStyle());
				lineSeparation.setVisible(false);
				LblMember.setVisible(true);
//				userImage.setVisible(true);
				LblMember.setText(i18n.GL1551());
				mainContainer.setVisible(true);
			}
			else if(classpageDo.getStatus().equalsIgnoreCase("active"))
			{
				btnJoinClass.setVisible(false);
//				userImage.setVisible(true);
				lblWebHelp.setVisible(false);
				btnWithDraw.setVisible(true);
				lineSeparation.setVisible(true);
				memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().studentStyle());
				LblMember.setVisible(true);
//				userImage.setVisible(true);
				LblMember.setText(i18n.GL1549());
				mainContainer.setVisible(true);
			}
			else 
			{

				
				btnJoinClass.setVisible(true);
				memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().techerStyle());
				lineSeparation.setVisible(false);
//				userImage.setVisible(false);
				lblWebHelp.setVisible(true);
				btnWithDraw.setVisible(false);
				LblMember.setVisible(false);
//				userImage.setVisible(false);
				mainContainer.setVisible(true);
				
				
				
				if(!AppClientFactory.isAnonymous())
				{

				if(!isJoinPopupPublic){
					isJoinPopupPublic=true;
					joinPopupPublic =  new StudentJoinClassPopup(classpageDo) {
					
					@Override
					void joinIntoClass() {
							String emailId=AppClientFactory.getLoggedInUser().getEmailId();
							AppClientFactory.getInjector().getClasspageService().studentJoinIntoClass(classpageDo.getClasspageCode(),emailId, new SimpleAsyncCallback<ClasspageDo>() {

								@Override
								public void onSuccess(ClasspageDo result) {
									joinPopupPublic.hide();
									mainContainer.setVisible(true);
									SuccessPopupViewVc success=new SuccessPopupViewVc(){

										@Override
										public void onClickPositiveButton(
												ClickEvent event) {
											if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
												Window.enableScrolling(false);
											}else{
												Window.enableScrolling(true);
											}
											btnJoinClass.setVisible(false);
//											userImage.setVisible(true);
											lblWebHelp.setVisible(false);
											btnWithDraw.setVisible(true);
											lineSeparation.setVisible(true);
											memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().studentStyle());
											LblMember.setVisible(true);
											LblMember.setText(StudentAssignmentView.i18n.GL1549());
											mainContainer.setVisible(true);
											this.hide();
											isJoinPopupPublic=false;
											
										}
										
									};
									success.setHeight("248px");
                                    success.setWidth("450px");
                                    success.setPopupTitle(StudentAssignmentView.i18n.GL1553());
                                    success.setDescText(StudentAssignmentView.i18n.GL1554()+classpageDo.getTitle()+StudentAssignmentView.i18n.GL_SPL_EXCLAMATION()+'\n'+StudentAssignmentView.i18n.GL1552());
                                    success.setPositiveButtonText(StudentAssignmentView.i18n.GL0190());
                                    success.center();
                                    success.show();
						
								}
							});
					}

					@Override
					public void closePoup() {
						hide();
						Window.enableScrolling(true);
				
					}
				};
				}
				int windowHeight=Window.getClientHeight()/2; //I subtract 10 from the client height so the window isn't maximized.
				int windowWidth=Window.getClientWidth()/2;
				joinPopupPublic.setPopupPosition(windowWidth-253, windowHeight-70);
				joinPopupPublic.setPixelSize(506, 261);		
				//joinPopup.center();
				joinPopupPublic.show();
				
			}

				
			}	
		}
		else
		{
			if(AppClientFactory.isAnonymous()){


				mainContainer.setVisible(false);
				LoginPopupUc loginPopupUc=new LoginPopupUc();
				
			}else{
			if(classpageDo.getCreatorId().equalsIgnoreCase(AppClientFactory.getGooruUid()))
			{
				btnJoinClass.setVisible(false);
//				userImage.setVisible(true);
				lblWebHelp.setVisible(false);
				btnWithDraw.setVisible(false);
				LblMember.setVisible(true);
				LblMember.setText(i18n.GL1551());
//				userImage.setVisible(true);
				mainContainer.setVisible(true);
			}
			else if(classpageDo.getStatus().equalsIgnoreCase("active")){
					btnJoinClass.setVisible(false);
//					userImage.setVisible(true);
					lblWebHelp.setVisible(false);
					btnWithDraw.setVisible(false);
					LblMember.setVisible(true);
//					userImage.setVisible(true);
					mainContainer.setVisible(true);
				}
			else if(classpageDo.getStatus().equalsIgnoreCase("pending")) 
			{
					btnJoinClass.setVisible(true);
					memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().techerStyle());
					lineSeparation.setVisible(false);
//					userImage.setVisible(false);
					lblWebHelp.setVisible(true);
					btnWithDraw.setVisible(false);
					LblMember.setVisible(false);
//					userImage.setVisible(false);
					mainContainer.setVisible(false);


						if(!isJoinPopupPrivateStatic){
							isJoinPopupPrivateStatic=true;
							joinPopupPrivate =  new StudentJoinClassPopup(classpageDo) {
							
							@Override
							void joinIntoClass() {
									String emailId=AppClientFactory.getLoggedInUser().getEmailId();
									AppClientFactory.getInjector().getClasspageService().studentJoinIntoClass(classpageDo.getClasspageCode(),emailId, new SimpleAsyncCallback<ClasspageDo>() {

										@Override
										public void onSuccess(ClasspageDo result) {
											joinPopupPrivate.hide();
											mainContainer.setVisible(true);
											SuccessPopupViewVc success=new SuccessPopupViewVc(){

												@Override
												public void onClickPositiveButton(
														ClickEvent event) {
													if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
														Window.enableScrolling(false);
													}else{
														Window.enableScrolling(true);
													}
													btnJoinClass.setVisible(false);
//													userImage.setVisible(true);
													lblWebHelp.setVisible(false);
													btnWithDraw.setVisible(false);
													LblMember.setVisible(true);
													LblMember.setText(StudentAssignmentView.i18n.GL1549());
													mainContainer.setVisible(true);
													this.hide();
													isJoinPopupPrivateStatic=false;
													
												}
												
											};
											success.setHeight("248px");
                                            success.setWidth("450px");
                                            success.setPopupTitle(StudentAssignmentView.i18n.GL1553());
                                            success.setDescText(StudentAssignmentView.i18n.GL1554()+classpageDo.getTitle()+StudentAssignmentView.i18n.GL_SPL_EXCLAMATION()+'\n'+StudentAssignmentView.i18n.GL1552());
                                            success.setPositiveButtonText(StudentAssignmentView.i18n.GL0190());
                                            success.center();
                                            success.show();
								
										}
									});
							}

							@Override
							public void closePoup() {
								hide();
								Window.enableScrolling(true);
								AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
							}
						};
						}
						int windowHeight=Window.getClientHeight()/2; //I subtract 10 from the client height so the window isn't maximized.
						int windowWidth=Window.getClientWidth()/2;
						joinPopupPrivate.setPopupPosition(windowWidth-253, windowHeight-70);
						joinPopupPrivate.setPixelSize(506, 261);		
						//joinPopup.center();
						joinPopupPrivate.show();
					
					
				}
				else 
				{
					try
					{
					mainContainer.setVisible(false);
					}
					catch(Exception ex)
					{
						AppClientFactory.printSevereLogger(ex.getMessage());
					}
				       if(AppClientFactory.isAnonymous()){
				    	   new SentEmailSuccessVc(i18n.GL1177(), i18n.GL1535());
				       }else{
				    	   new SentEmailSuccessVc(i18n.GL1177(), i18n.GL1535_1());
				       }
				}
			
		}
		}
		/*}
		else
		{
			if(classpageDo.getStatus().equalsIgnoreCase("active")){
				btnJoinClass.setVisible(false);
				lblWebHelp.setVisible(false);
				btnWithDraw.setVisible(true);
				LblMember.setVisible(true);
			}else if(classpageDo.getStatus().equalsIgnoreCase("pending")) {
				btnJoinClass.setVisible(true);
				lblWebHelp.setVisible(true);
				btnWithDraw.setVisible(false);
				LblMember.setVisible(false);
			}else {
		//dont have access popup
			}
		}*/

		
		//here we need to check for http://collab.ednovo.org/jira/browse/CORE-516 this 
		//api response and display the button text and functionality.
		//DataLogEvents.classpageView(GwtUUIDGenerator.uuid(), "classpage-view", classpageDo.getClasspageId(), AppClientFactory.getLoggedInUser().getGooruUId(), System.currentTimeMillis(), System.currentTimeMillis(),"",0L, AppClientFactory.getLoggedInUser().getToken()	,"start");
	}
	
	@Override
	public void showClasspageItems(ArrayList<ClasspageItemDo> classpageItemsList1, String sortOrder){
		//removeLoadingPanel();
		//contentpanel.clear();
		ArrayList<ClasspageItemDo> classpageItemsList = new ArrayList<ClasspageItemDo>();
		classpageItemsList.clear();
		classpageItemsList.addAll(classpageItemsList1);
		
		if(classpageItemsList!=null&&classpageItemsList.size()>0){
			//noAssignmentMsg.setVisible(false);
			for(int itemIndex=0;itemIndex<classpageItemsList.size();itemIndex++){
				ClasspageItemDo classpageItemDo=classpageItemsList.get(itemIndex);
//				CollectionsView collectionsView = new CollectionsView(classpageItemDo,true,(itemIndex+1)){
//					public void resetPagination(){
//						setPagination();
//						contentpanel.add(setLoadingPanel());
//						getUiHandlers().getNextClasspageItems(((pageNumber*limit)-1),1);
//					}
//					public void updateAssignmentCircleColor(String collectionItemId,String readStatus){
//						updateCircleColors(collectionItemId,readStatus);
//					}
//				};
				this.totalHitCount=classpageItemDo.getTotalHitCount();
			//	contentpanel.add(collectionsView);
			}
			//setPagination();
		}else{
			this.totalHitCount=0;
			//setPagination();
			//noAssignmentMsg.setVisible(true);
		}
	}
	public void resetAll(){
/*		contentpanel.clear();
		contentpanel.add(setLoadingPanel());*/
		this.totalHitCount=0;
		//setPagination();
		mainTitleLbl.setText("");
		studentViewImage.setAltText("");
		studentViewImage.setTitle("");
		studentViewImage.setUrl("");
		imgProfileImage.setUrl("");
		lblUserName.setText("");
		//panelAssignmentProgress.clear();
		assignmentsDotsMap.clear();
	}
/*	public void setPagination(){
		if(this.totalHitCount>5){
			showPaginationButton();
		}else{
			clearPaginationButton();
		}
	}*/
	/*public void showPaginationButton(){
		paginationFocPanel.clear();
		paginationFocPanel1.clear();
		String pageNum=AppClientFactory.getPlaceManager().getRequestParameter("pageNum", "1");
		try{
			pageNumber=Integer.parseInt(pageNum);
			pageNumber=pageNumber==0?1:pageNumber;
		}catch(NumberFormatException e){
			AppClientFactory.printSevereLogger(e.getMessage());
		}
		int totalPages = (this.totalHitCount / 5)
				+ ((this.totalHitCount % 5) > 0 ? 1 : 0);
		//int pageNumCount = pageNum + 1;
		if (totalPages > 1) {
			if (pageNumber > 1) {
				paginationFocPanel.add(new PaginationButtonUc(pageNumber - 1, PREVIOUS, this));
				paginationFocPanel1.add(new PaginationButtonUc(pageNumber - 1, PREVIOUS, this));
			}
		
			int page = pageNumber < 5 ? 1 : pageNumber - 3;

			for (int count = 1; count < 5 && page <= totalPages; page++, ++count) 
			{
				paginationFocPanel.add(new PaginationButtonUc(page, page == pageNumber, this));
				paginationFocPanel1.add(new PaginationButtonUc(page, page == pageNumber, this));
			}
			if (pageNumber < totalPages) {
				paginationFocPanel.add(new PaginationButtonUc(pageNumber + 1, NEXT, this));
				paginationFocPanel1.add(new PaginationButtonUc(pageNumber + 1, NEXT, this));
			}
		}
	}*/
/*	public void clearPaginationButton(){
		paginationFocPanel.clear();
		paginationFocPanel1.clear();
	}
	private class PaginationEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			pageNumber++;
			setPagination();
			contentpanel.add(setLoadingPanel());
			getUiHandlers().getNextClasspageItems(((pageNumber-1)*limit),limit);
		}
	}*/
	/*public void resetEditClasspageView(){
		paginationFocPanel.clear();
		paginationFocPanel1.clear();
		contentpanel.clear();
		contentpanel.add(setLoadingPanel());
		limit=5;
		pageNumber=1;
	}*/
/*	public Label setLoadingPanel(){
		Label loadingImage=new Label();
		loadingImage.setStyleName(EditClasspageCBundle.INSTANCE.css().loadingpanelImage());
		return loadingImage;
	}*/
/*	public void removeLoadingPanel(){
		if(contentpanel.getWidgetCount()>0){
			Widget loadingPanel=contentpanel.getWidget(contentpanel.getWidgetCount()-1);
			if(loadingPanel!=null&&loadingPanel instanceof Label){
				loadingPanel.removeFromParent();
			}
		}
	}*/
	
	@Override
	public void showClasspageItemsForAssignmentPath(ArrayList<ClasspageItemDo> classpageItemsList) {
		//TODO 
	//	panelAssignmentProgress.clear();
		assignmentsDotsMap.clear(); // TODO dont forget to clear when panelAssignmentProgress clear
/*		if(classpageItemsList!=null&&classpageItemsList.size()>0){
			//hide/show the next and previous buttons
			if (classpageItemsList.get(0).getTotalHitCount() > defaultLimitForPath && classpageItemsList.size() == defaultLimitForPath){
				lblNext.setVisible(true);
			}else{
				lblNext.setVisible(false);
			}
			
			if (defaultOffsetForPath <= 0){
				lblPrevious.setVisible(false);
			}else{
				lblPrevious.setVisible(true);
			}
			if (classpageItemsList.size() > 0)
				panelAssignmentProgress.clear();
			
			for(int itemIndex=0; itemIndex<classpageItemsList.size(); itemIndex++){
				ClasspageItemDo classpageItemDo=classpageItemsList.get(itemIndex);
				AssignmentProgressVc assignmentProgressVc =new AssignmentProgressVc((itemIndex == classpageItemsList.size() - 1) ? true : false, classpageItemDo, classpageItemDo.getSequenceNumber());
				assignmentsDotsMap.put(classpageItemDo.getCollectionItemId(), assignmentProgressVc);
				panelAssignmentProgress.add(assignmentProgressVc);
				this.totalHitCount=classpageItemDo.getTotalHitCount();
			}
		}*/
	}
	
	public void updateCircleColors(String collectionItemId,String readStatus){
		AssignmentProgressVc assignmentProgressVc=assignmentsDotsMap.get(collectionItemId);
		if(assignmentProgressVc!=null){
			assignmentProgressVc.updateDotsCircle(readStatus);
		}
	}
	
	
	@UiHandler("backToEditPanel")
	public void onClickHandler(ClickEvent event){
		//getPreviousPage();
		String pageSize=Cookies.getCookie("pageSize");
		String classpageid=Cookies.getCookie("classpageid");
		String pageNum=Cookies.getCookie("pageNum");
		String pos=Cookies.getCookie("pos");
		
		if(AppClientFactory.getPlaceManager().getRequestParameter("source").equalsIgnoreCase("T")){
//			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.TEACH);
		}
		else if(AppClientFactory.getPlaceManager().getRequestParameter("source").equalsIgnoreCase("E")){
			
			Map<String, String> params=new HashMap<String, String>();
			//params.put("pageSize", pageSize);
			params.put("classpageid", classpageid);
			params.put("pageNum", pageNum);
			//params.put("pos", pos);
			
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.EDIT_CLASSPAGE,params,true);
		}
		backToEditPanel.setVisible(false);
	}

	@Override
	public void onClick(ClickEvent event) {
	if (event.getSource() instanceof PaginationButtonUc) {
			int pagenumber = ((PaginationButtonUc) event.getSource()).getPage();
			pageNumber = pagenumber;
/*			setPagination();
			contentpanel.clear();
			contentpanel.add(setLoadingPanel());*/
			Map<String,String> params = new HashMap<String,String>();
			String classpageid=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
			String order=AppClientFactory.getPlaceManager().getRequestParameter("order", null);
			String source=AppClientFactory.getPlaceManager().getRequestParameter("source", null);
			String backButtonStatus=AppClientFactory.getPlaceManager().getRequestParameter("b", null);
			params.put("order", order);
			params.put("id", classpageid);
			if(source!=null){
				params.put("source", source);
			}
			if(backButtonStatus!=null){
				params.put("b", "true");
			}
			params.put("pageNum", pagenumber+"");
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.STUDENT, params);
			AppClientFactory.getPlaceManager().revealPlace(true, placeRequest, false);
		}
	}
	
	@UiHandler("btnJoinClass")
	public void joinClassPopup(ClickEvent clickEvent){

		if(AppClientFactory.isAnonymous()){
	
			LoginPopupUc loginPopupUc=new LoginPopupUc();
			
			
			
			
		}else{
			if(!isJoinPopupButtonclick){
				isJoinPopupButtonclick=true;
				joinPopupButtonClick =  new StudentJoinClassPopup(classpageDo) {
				
				@Override
				void joinIntoClass() {
						String emailId=AppClientFactory.getLoggedInUser().getEmailId();
						AppClientFactory.getInjector().getClasspageService().studentJoinIntoClass(classpageDo.getClasspageCode(),emailId, new SimpleAsyncCallback<ClasspageDo>() {

							@Override
							public void onSuccess(ClasspageDo result) {
								joinPopupButtonClick.hide();
								SuccessPopupViewVc success=new SuccessPopupViewVc(){

									@Override
									public void onClickPositiveButton(
											ClickEvent event) {
										if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
											Window.enableScrolling(false);
										}else{
											Window.enableScrolling(true);
										}
										btnJoinClass.setVisible(false);
//										userImage.setVisible(true);
										lblWebHelp.setVisible(false);
										btnWithDraw.setVisible(true);
										memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().studentStyle());
										lineSeparation.setVisible(true);
										LblMember.setVisible(true);
										LblMember.setText(StudentAssignmentView.i18n.GL1549());
										mainContainer.setVisible(true);
										this.hide();
										isJoinPopupButtonclick=false;
									}
									
								};
								success.setHeight("248px");
                                success.setWidth("450px");
                                success.setPopupTitle(StudentAssignmentView.i18n.GL1553());
                                success.setDescText(StudentAssignmentView.i18n.GL1554()+classpageDo.getTitle()+StudentAssignmentView.i18n.GL_SPL_EXCLAMATION()+'\n'+StudentAssignmentView.i18n.GL1552());
                                success.setPositiveButtonText(StudentAssignmentView.i18n.GL0190());
                                success.center();
                                success.show();
								
							}
						});
				}

				@Override
				public void closePoup() {
					hide();
					Window.enableScrolling(true);
				}
			};
			}
			joinPopupButtonClick.setPopupPosition(btnJoinClass.getElement().getAbsoluteLeft() - 509, btnJoinClass.getElement().getAbsoluteTop());
			joinPopupButtonClick.setPixelSize(506, 261);		
			//joinPopup.center();
			joinPopupButtonClick.show();

		}
	    
	}
	
	@UiHandler("btnWithDraw")
	public void onWithdrawButtonClick(ClickEvent clickEvent){
		Window.enableScrolling(false);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
		DeletePopupViewVc delete = new DeletePopupViewVc() {

			@Override
			public void onClickPositiveButton(ClickEvent event) 
			{
				final ArrayList<String> arrayEmailId = new ArrayList<String>();
				arrayEmailId.add('"'+AppClientFactory.getLoggedInUser().getEmailId()+'"');
				getUiHandlers().removeUserFromClass(classpageDo, arrayEmailId.toString());
				hide();
				
			}

			@Override
			public void onClickNegitiveButton(ClickEvent event) {
				Window.enableScrolling(true);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
				hide();
				
			}
			
		};
		
		delete.setPopupTitle(i18n.GL1618());
		delete.setNotes(i18n.GL0748());
		delete.setDescText(i18n.GL1555());
		delete.setPositiveButtonText(i18n.GL_GRR_YES());							
		delete.setNegitiveButtonText(i18n.GL0142());
//		delete.setDeleteValidate("delete");
		delete.setPixelSize(450, 294);		
		delete.center();
		delete.show();
	    
	}
	

	@Override
	public void clearAll() {
		/*paginationFocPanel.clear();
		paginationFocPanel1.clear();
		contentpanel.clear();
		contentpanel.add(setLoadingPanel());
		limit=5;
		pageNumber=1;*/

	}
	
	@Override
	public void hidePanel() {
		/*paginationFocPanel.clear();
		paginationFocPanel1.clear();
		contentpanel.clear();
		contentpanel.add(setLoadingPanel());
		limit=5;
		pageNumber=1;*/
		mainContainer.setVisible(false);
	}

	@Override
	public Button getBackToEditPanel() {
		return backToEditPanel;
	}
	
	public static void setPrivatePage()
	{
		btnJoinClass.setVisible(false);
//		userImage.setVisible(true);
		lblWebHelp.setVisible(false);
		btnWithDraw.setVisible(false);
		LblMember.setVisible(true);
		LblMember.setText(i18n.GL1551());
		mainContainer.setVisible(true);
	}
	public static void setPrivatePageActive()
	{	
	btnJoinClass.setVisible(false);
//	userImage.setVisible(true);
	lblWebHelp.setVisible(false);
	btnWithDraw.setVisible(false);
	LblMember.setVisible(true);
	LblMember.setText(i18n.GL1549());
	mainContainer.setVisible(true);
	}
	
	public static void setPrivatePagePending()
	{
	btnJoinClass.setVisible(true);
	memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().techerStyle());
	lineSeparation.setVisible(false);
//	userImage.setVisible(false);
	lblWebHelp.setVisible(true);
	btnWithDraw.setVisible(false);
	LblMember.setVisible(false);
	LblMember.setText(i18n.GL1549());
	mainContainer.setVisible(false);
	
	if(AppClientFactory.isAnonymous()){

		LoginPopupUc loginPopupUc=new LoginPopupUc();
		
		
	}else{

		if(!isJoinPopupPrivate){
			isJoinPopupPrivate=true;
		joinPopupPrivate =  new StudentJoinClassPopup(classpageDo) {
			
			@Override
			void joinIntoClass() {
					String emailId=AppClientFactory.getLoggedInUser().getEmailId();
					AppClientFactory.getInjector().getClasspageService().studentJoinIntoClass(classpageDo.getClasspageCode(),emailId, new SimpleAsyncCallback<ClasspageDo>() {

						@Override
						public void onSuccess(ClasspageDo result) {
							joinPopupPrivate.hide();
							mainContainer.setVisible(true);
							SuccessPopupViewVc success=new SuccessPopupViewVc(){

								@Override
								public void onClickPositiveButton(
										ClickEvent event) {
									if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
										Window.enableScrolling(false);
									}else{
										Window.enableScrolling(true);
									}
									btnJoinClass.setVisible(false);
//									userImage.setVisible(true);
									lblWebHelp.setVisible(false);
									btnWithDraw.setVisible(false);
									LblMember.setVisible(true);
									LblMember.setText(StudentAssignmentView.i18n.GL1549());
									mainContainer.setVisible(true);
									this.hide();
									isJoinPopupPrivate=false;
									
								}
								
							};
							success.setHeight("248px");
                            success.setWidth("450px");
                            success.setPopupTitle(StudentAssignmentView.i18n.GL1553());
                            success.setDescText(StudentAssignmentView.i18n.GL1554()+classpageDo.getTitle()+StudentAssignmentView.i18n.GL_SPL_EXCLAMATION()+'\n'+StudentAssignmentView.i18n.GL1552());
                            success.setPositiveButtonText(StudentAssignmentView.i18n.GL0190());
                            success.center();
                            success.show();
				
						}
					});
			}

			@Override
			public void closePoup() {
				hide();
				Window.enableScrolling(true);
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
			}
		};
		}
		int windowHeight=Window.getClientHeight()/2; //I subtract 10 from the client height so the window isn't maximized.
		int windowWidth=Window.getClientWidth()/2;
		joinPopupPrivate.setPopupPosition(windowWidth-253, windowHeight-70);
		joinPopupPrivate.setPixelSize(506, 261);		
		//joinPopup.center();
		joinPopupPrivate.show();
	}
	
	}
	
	public static void setPublicPage()
	{
		btnJoinClass.setVisible(false);
//		userImage.setVisible(true);
		lblWebHelp.setVisible(false);
		btnWithDraw.setVisible(true);
		memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().studentStyle());
		lineSeparation.setVisible(true);
		LblMember.setVisible(true);
		LblMember.setText(i18n.GL1551());
		mainContainer.setVisible(true);
	}
	public static void setPublicPageActive()
	{	
		btnJoinClass.setVisible(false);
//		userImage.setVisible(true);
		lblWebHelp.setVisible(false);
		btnWithDraw.setVisible(true);
		memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().studentStyle());
		lineSeparation.setVisible(true);
		LblMember.setVisible(true);
		LblMember.setText(i18n.GL1549());
		mainContainer.setVisible(true);
	}
	
	public static void setPublicPagePending()
	{
		btnJoinClass.setVisible(true);
		memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().techerStyle());
		lineSeparation.setVisible(false);
//		userImage.setVisible(false);
		lblWebHelp.setVisible(true);
		btnWithDraw.setVisible(false);
		LblMember.setVisible(false);
		LblMember.setText(i18n.GL1549());
		mainContainer.setVisible(true);
		
		if(!AppClientFactory.isAnonymous()){
			String classpageid=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
			AppClientFactory.getInjector().getClasspageService().getClasspage(classpageid, new SimpleAsyncCallback<ClasspageDo>() {
				@Override
				public void onSuccess(ClasspageDo classpageDoResp) {
					classpageDo = classpageDoResp;
					if(!isJoinPopupPublic){
						isJoinPopupPublic=true;
						joinPopupPublic =  new StudentJoinClassPopup(classpageDo) {
						
						@Override
						void joinIntoClass() {
								String emailId=AppClientFactory.getLoggedInUser().getEmailId();
								AppClientFactory.getInjector().getClasspageService().studentJoinIntoClass(classpageDo.getClasspageCode(),emailId, new SimpleAsyncCallback<ClasspageDo>() {

									@Override
									public void onSuccess(ClasspageDo result) {
										joinPopupPublic.hide();
										mainContainer.setVisible(true);
										SuccessPopupViewVc success=new SuccessPopupViewVc(){

											@Override
											public void onClickPositiveButton(
													ClickEvent event) {
												if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
													Window.enableScrolling(false);
												}else{
													Window.enableScrolling(true);
												}
												btnJoinClass.setVisible(false);
//												userImage.setVisible(true);
												lblWebHelp.setVisible(false);
												btnWithDraw.setVisible(true);
												memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().studentStyle());
												lineSeparation.setVisible(true);
												LblMember.setVisible(true);
												LblMember.setText(StudentAssignmentView.i18n.GL1549());
												mainContainer.setVisible(true);
												this.hide();
												isJoinPopupPublic=false;
												
											}
											
										};
										success.setHeight("248px");
		                                success.setWidth("450px");
		                                success.setPopupTitle(StudentAssignmentView.i18n.GL1553());
		                                success.setDescText(StudentAssignmentView.i18n.GL1554()+classpageDo.getTitle()+StudentAssignmentView.i18n.GL_SPL_EXCLAMATION()+'\n'+StudentAssignmentView.i18n.GL1552());
		                                success.setPositiveButtonText(StudentAssignmentView.i18n.GL0190());
		                                success.center();
		                                success.show();
							
									}
								});
						}

						@Override
						public void closePoup() {
							hide();
							Window.enableScrolling(true);
					
						}
					};
					}
					int windowHeight=Window.getClientHeight()/2; //I subtract 10 from the client height so the window isn't maximized.
					int windowWidth=Window.getClientWidth()/2;
					joinPopupPublic.setPopupPosition(windowWidth-253, windowHeight-70);
					joinPopupPublic.setPixelSize(506, 261);		
					//joinPopup.center();
					joinPopupPublic.show();
				}
				});

			
		}
		
	}
	private class OnMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.clear();
			toolTipPopupPanel
					.setWidget(new LibraryTopicCollectionToolTip(i18n.GL1563(),"studyView"));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(lblWebHelp.getElement()
					.getAbsoluteLeft() - 204, lblWebHelp.getElement()
					.getAbsoluteTop() + 19);
			toolTipPopupPanel.show();
		
			
		}
		
	}
	private class OnMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
			
		}
		
		
	}
	
	
	public void openJoinClassEvent()
	{
		String classpageid=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
		AppClientFactory.getInjector().getClasspageService().getClasspage(classpageid, new SimpleAsyncCallback<ClasspageDo>() {
			@Override
			public void onSuccess(ClasspageDo classpageDoService) {
				classpageDo = classpageDoService;
				if(classpageDo.getSharing().equalsIgnoreCase("public"))
				{
					if(classpageDo.getCreatorId().equalsIgnoreCase(AppClientFactory.getGooruUid()))
					{
						btnJoinClass.setVisible(false);
//						userImage.setVisible(true);
						lblWebHelp.setVisible(false);
						btnWithDraw.setVisible(false);
						LblMember.setVisible(true);
//						userImage.setVisible(true);
						LblMember.setText(i18n.GL1551());
						mainContainer.setVisible(true);
					}
					else if(classpageDo.getStatus().equalsIgnoreCase("active"))
					{
						btnJoinClass.setVisible(false);
//						userImage.setVisible(true);
						lblWebHelp.setVisible(false);
						btnWithDraw.setVisible(true);
						memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().studentStyle());
						lineSeparation.setVisible(true);
						LblMember.setVisible(true);
//						userImage.setVisible(true);
						LblMember.setText(i18n.GL1549());
						mainContainer.setVisible(true);
					}
					else 
					{

						
						btnJoinClass.setVisible(true);
						memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().techerStyle());
						lineSeparation.setVisible(false);
//						userImage.setVisible(false);
						lblWebHelp.setVisible(true);
						btnWithDraw.setVisible(false);
						LblMember.setVisible(false);
//						userImage.setVisible(false);
						mainContainer.setVisible(true);
						
			
						
						if(!AppClientFactory.isAnonymous())
						{

						if(!isJoinPopupPublic){
							isJoinPopupPublic=true;
							joinPopupPublic =  new StudentJoinClassPopup(classpageDo) {
							
							@Override
							void joinIntoClass() {
									String emailId=AppClientFactory.getLoggedInUser().getEmailId();
									AppClientFactory.getInjector().getClasspageService().studentJoinIntoClass(classpageDo.getClasspageCode(),emailId, new SimpleAsyncCallback<ClasspageDo>() {

										@Override
										public void onSuccess(ClasspageDo result) {
											joinPopupPublic.hide();
											mainContainer.setVisible(true);
											SuccessPopupViewVc success=new SuccessPopupViewVc(){

												@Override
												public void onClickPositiveButton(
														ClickEvent event) {
													if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
														Window.enableScrolling(false);
													}else{
														Window.enableScrolling(true);
													}
													btnJoinClass.setVisible(false);
//													userImage.setVisible(true);
													lblWebHelp.setVisible(false);
													btnWithDraw.setVisible(true);
													memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().studentStyle());
													lineSeparation.setVisible(true);
													LblMember.setVisible(true);
													LblMember.setText(StudentAssignmentView.i18n.GL1549());
													mainContainer.setVisible(true);
													this.hide();
													isJoinPopupPublic=false;
													
												}
												
											};
											success.setHeight("248px");
		                                    success.setWidth("450px");
		                                    success.setPopupTitle(StudentAssignmentView.i18n.GL1553());
		                                    success.setDescText(StudentAssignmentView.i18n.GL1554()+classpageDo.getTitle()+StudentAssignmentView.i18n.GL_SPL_EXCLAMATION()+'\n'+StudentAssignmentView.i18n.GL1552());
		                                    success.setPositiveButtonText(StudentAssignmentView.i18n.GL0190());
		                                    success.center();
		                                    success.show();
								
										}
									});
							}

							@Override
							public void closePoup() {
								hide();
								Window.enableScrolling(true);
						
							}
						};
						}
						int windowHeight=Window.getClientHeight()/2; //I subtract 10 from the client height so the window isn't maximized.
						int windowWidth=Window.getClientWidth()/2;
						joinPopupPublic.setPopupPosition(windowWidth-253, windowHeight-70);
						joinPopupPublic.setPixelSize(506, 261);		
						//joinPopup.center();
						joinPopupPublic.show();
						
					}

						
					}	
				}
				else
				{
					
					if(AppClientFactory.isAnonymous()){


						mainContainer.setVisible(false);
						LoginPopupUc loginPopupUc=new LoginPopupUc();
						
					}else{
					if(classpageDo.getCreatorId().equalsIgnoreCase(AppClientFactory.getGooruUid()))
					{
						btnJoinClass.setVisible(false);
//						userImage.setVisible(true);
						lblWebHelp.setVisible(false);
						btnWithDraw.setVisible(false);
						LblMember.setVisible(true);
						LblMember.setText(i18n.GL1551());
//						userImage.setVisible(true);
						mainContainer.setVisible(true);
					}
					else if(classpageDo.getStatus().equalsIgnoreCase("active")){
							btnJoinClass.setVisible(false);
//							userImage.setVisible(true);
							lblWebHelp.setVisible(false);
							btnWithDraw.setVisible(false);
							LblMember.setVisible(true);
//							userImage.setVisible(true);
							mainContainer.setVisible(true);
						}
					else if(classpageDo.getStatus().equalsIgnoreCase("pending")) 
					{
							btnJoinClass.setVisible(true);
							memberContainer.setStyleName(EditClasspageCBundle.INSTANCE.css().techerStyle());
							lineSeparation.setVisible(false);
//							userImage.setVisible(false);
							lblWebHelp.setVisible(true);
							btnWithDraw.setVisible(false);
							LblMember.setVisible(false);
//							userImage.setVisible(false);
							mainContainer.setVisible(false);
							

								if(!isJoinPopupPrivateStatic){
									isJoinPopupPrivateStatic=true;
									joinPopupPrivate =  new StudentJoinClassPopup(classpageDo) {
									
									@Override
									void joinIntoClass() {
											String emailId=AppClientFactory.getLoggedInUser().getEmailId();
											AppClientFactory.getInjector().getClasspageService().studentJoinIntoClass(classpageDo.getClasspageCode(),emailId, new SimpleAsyncCallback<ClasspageDo>() {

												@Override
												public void onSuccess(ClasspageDo result) {
													joinPopupPrivate.hide();
													mainContainer.setVisible(true);
													SuccessPopupViewVc success=new SuccessPopupViewVc(){

														@Override
														public void onClickPositiveButton(
																ClickEvent event) {
															if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
																Window.enableScrolling(false);
															}else{
																Window.enableScrolling(true);
															}
															btnJoinClass.setVisible(false);
//															userImage.setVisible(true);
															lblWebHelp.setVisible(false);
															btnWithDraw.setVisible(false);
															LblMember.setVisible(true);
															LblMember.setText(StudentAssignmentView.i18n.GL1549());
															mainContainer.setVisible(true);
															this.hide();
															isJoinPopupPrivateStatic=false;
															
														}
														
													};
													success.setHeight("248px");
		                                            success.setWidth("450px");
		                                            success.setPopupTitle(StudentAssignmentView.i18n.GL1553());
		                                            success.setDescText(StudentAssignmentView.i18n.GL1554()+classpageDo.getTitle()+StudentAssignmentView.i18n.GL_SPL_EXCLAMATION()+'\n'+StudentAssignmentView.i18n.GL1552());
		                                            success.setPositiveButtonText(StudentAssignmentView.i18n.GL0190());
		                                            success.center();
		                                            success.show();
										
												}
											});
									}

									@Override
									public void closePoup() {
										hide();
										Window.enableScrolling(true);
										AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
									}
								};
								}
								int windowHeight=Window.getClientHeight()/2; //I subtract 10 from the client height so the window isn't maximized.
								int windowWidth=Window.getClientWidth()/2;
								joinPopupPrivate.setPopupPosition(windowWidth-253, windowHeight-70);
								joinPopupPrivate.setPixelSize(506, 261);		
								//joinPopup.center();
								joinPopupPrivate.show();
							
							
						}
						else 
						{
							try
							{
							mainContainer.setVisible(false);
							}
							catch(Exception ex)
							{
								AppClientFactory.printSevereLogger(ex.getMessage());
							}
						       if(AppClientFactory.isAnonymous()){
						    	   new SentEmailSuccessVc(i18n.GL1177(), i18n.GL1535());
						       }else{
						    	   new SentEmailSuccessVc(i18n.GL1177(), i18n.GL1535_1());
						       }
						}
					
				}
				}
			}
		});


	}
	
	public static boolean getMainContainerStatus()
	{
		Boolean mainContainerStatus = false;
		try
		{
			mainContainerStatus = mainContainer.isVisible();		
		}
		catch(Exception ex)
		{
			AppClientFactory.printSevereLogger(ex.getMessage());
		}
		return mainContainerStatus;		
	}
	
	public void addSortingOptionsToList(){
		sortingOptionsList.clear();
		sortingOptionsList.add(i18n.GL1946());
		sortingOptionsList.add(i18n.GL1952());
		sortingOptionsList.add(i18n.GL1953());

	}

	/**
	 * 
	 * @function callAssignmentAPI 
	 * 
	 * @created_date : Jun 11, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param classpageId
	 * @param pageSize
	 * @param pageNum
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	@Override
	public void callAssignmentAPI(String classpageId, String offsetProgress, String limitProgress){
		getUiHandlers().getAssignmentsProgress(classpageId, offsetProgress.toString(), limitProgress.toString()); // this will call showClasspageItemsForAssignmentPath
	}
	


	public FlowPanel getStudentsetupContainer() {
		return studentsetupContainer;
	}

	public void setStudentsetupContainer(FlowPanel studentsetupContainer) {
		this.studentsetupContainer = studentsetupContainer;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (content != null) {
			 if(slot==StudentAssignmentPresenter.STUDY_SLOT){
				getStudentsetupContainer().clear();
				getStudentsetupContainer().add(content);
			}else{
				//getClassContainer().setVisible(false);
			}
		}
	}
	
	/*@UiHandler("backToTeacherView")
	public void backToTeacherviewEvent(ClickEvent clickEvent){
		Map<String,String> params = new HashMap<String,String>();
		String classpageid=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
		String pageNum=AppClientFactory.getPlaceManager().getRequestParameter("pageNum", null);
		String pos=AppClientFactory.getPlaceManager().getRequestParameter("pos", null);
		String pageSize=AppClientFactory.getPlaceManager().getRequestParameter("pageSize", null);
		params.put("pos", pos);
		params.put("classpageid", classpageid);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.EDIT_CLASSPAGE, params);
		AppClientFactory.getPlaceManager().revealPlace(true, placeRequest, false);
	}*/
	
	
}