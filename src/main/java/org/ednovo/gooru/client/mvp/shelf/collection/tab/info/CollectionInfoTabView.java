/*******************************************************************************
 * 
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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.application.client.PlaceTokens;
import org.ednovo.gooru.application.client.gin.AppClientFactory;
import org.ednovo.gooru.application.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.application.shared.i18n.MessageProperties;
import org.ednovo.gooru.application.shared.model.code.CodeDo;
import org.ednovo.gooru.application.shared.model.code.LibraryCodeDo;
import org.ednovo.gooru.application.shared.model.content.CollectionDo;
import org.ednovo.gooru.application.shared.model.content.StandardFo;
import org.ednovo.gooru.application.shared.model.search.SearchDo;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.assign.CollectionAssignCBundle;
import org.ednovo.gooru.client.mvp.shelf.event.AddCourseEvent;
import org.ednovo.gooru.client.mvp.shelf.event.AddCourseHandler;
import org.ednovo.gooru.client.uc.AlertContentUc;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.CloseLabel;
import org.ednovo.gooru.client.uc.CloseLabelCentury;
import org.ednovo.gooru.client.uc.CourseListUc;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.GradeLabel;
import org.ednovo.gooru.client.uc.StandardsPreferenceOrganizeToolTip;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.tractionsoftware.gwt.user.client.ui.GroupedListBox;

/**
 * @author Search Team
 * 
 */
public class CollectionInfoTabView extends BaseViewWithHandlers<CollectionInfoTabUiHandlers> implements IsCollectionInfoTabView, SelectionHandler<SuggestOracle.Suggestion> {
	@UiField
	Button addCourseBtn, addStandardBtn,removeCourseBtn;

	@UiField
	FlowPanel pnl21CenturySkils,gradeTopList, gradeMiddleList, gradeBottomList, courseData, standardsPanel,centPanel, KinderGarten, higherEducation,standardContainer;

	@UiField
	Label  GradeUpdate, languageObjectiveTitle,standardMaxMsg, courseLabel, standardLabel,centLabel, standardsDefaultText,centDefaultText,gradeLbl,selectGradeLbl,selectCourseLbl,toggleArrowButtonPrimary,toggleArrowButtonSecondary,instructionalMethod,audienceLabel,audienceTitle,instructionalTitle,languageObjectiveHeader,depthOfKnowledgeHeader,depthOfKnowledgeTitle,learningInnovationHeader,learningInnovationTitle;
	
	@UiField Label lblAudiencePlaceHolder,lblAudienceArrow,lblInstructionalPlaceHolder,lblInstructionalArrow,languageObjectiveerrLabel;
	
	@UiField ScrollPanel spanelAudiencePanel,spanelInstructionalPanel;	

	@UiField
	HTMLPanel panelLoading,mainInfoPanel,secondaryContentsContainer,htmlAudienceListContainer,htmlInstructionalListContainer,primaryLabelTag,secondaryHeaderLabel,hPanelMainInfo;
	
	@UiField TextArea textAreaVal;
	
	@UiField CheckBox chkLevelRecall,chkLevelSkillConcept,chkLevelStrategicThinking,chkLevelExtendedThinking,learninglevel1,learninglevel2,learninglevel3;
	@UiField(provided = true)
	AppSuggestBox standardSgstBox;
	
	@UiField(provided = true)	
	AppSuggestBox centurySgstBox;

	@UiField(provided = true)
	CollectionCBundle res;
	
	ToolTip toolTip=null;
	
	@UiField public Button browseBtn,centbrowseBtn;
	
	@UiField Label lblCharLimit,charLimitErrLbl;
	
	String courseCode="";
	
	private static final List<String> gradeList = new ArrayList<String>();
	
	private HandlerRegistration handlerRegistration=null;
	
	private CollectionDo collectionDo = null;

	private AppMultiWordSuggestOracle standardSuggestOracle;
	
	private AppMultiWordSuggestOracle centurySuggestOracle;

	private SearchDo<CodeDo> standardSearchDo = new SearchDo<CodeDo>();
	
	private SearchDo<StandardFo> centurySearchDo = new SearchDo<StandardFo>();

	private Map<String, String> standardCodesMap = new HashMap<String, String>();
	
	private Map<String, String> centuryCodesMap = new HashMap<String, String>();
	
	private GroupedListBox collectionCourseLst;

	private AlertContentUc alertContentUc;
	
	private boolean isClickedOnDropDwn=false;
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	private static final String ADD_COURSE=i18n.GL0847();
	
	private static final String CHANGE_COURSE=i18n.GL1496();
	
	private static final String FLT_CODE_ID = "id";
	
	private static final String FLT_SOURCE_CODE_ID = 	"flt.sourceCodeId";
	
	private static final String NO_MATCH_FOUND = i18n.GL0723();
	
	CourseListUc courseListUc=null;
	
	List<String> standardPreflist=null;
	
	String InstructionalMethodStr = i18n.GL1729();
	
	String AudienceStr = i18n.GL1730();
	
	ArrayList<String> courseDo = new ArrayList<String>();
	
	String newInstructionalVal = "";
	
	final String ASSESSMENT="assessment";
	
	private static CollectionInfoTabViewUiBinder uiBinder = GWT.create(CollectionInfoTabViewUiBinder.class);
	
	Map<Long, String> selectedValuesFromAutoSuggest=new HashMap<Long, String>();
	
	Map<Long, String> hilightSelectedValuesFromAutoSuggest=new HashMap<Long, String>();
	
	interface CollectionInfoTabViewUiBinder extends UiBinder<Widget, CollectionInfoTabView> {
	}
	
	/**
	 * Class constructor
	 */
	public CollectionInfoTabView() {
		standardSuggestOracle = new AppMultiWordSuggestOracle(true);
		centurySuggestOracle= new AppMultiWordSuggestOracle(true);
		
		standardSearchDo.setPageSize(10);
		final StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
		
		standardSgstBox = new AppSuggestBox(standardSuggestOracle) {
			
			@Override
			public void keyAction(String text,KeyUpEvent event) {
				text=text.toUpperCase();
				standardsPreferenceOrganizeToolTip.hide();
				standardSearchDo.setSearchResults(null);
				boolean standardsPrefDisplayPopup = false;
				if(!courseCode.isEmpty()) {
					Map<String,String> filters = new HashMap<String, String>();
					filters.put(FLT_CODE_ID,courseCode);
					filters.put(FLT_SOURCE_CODE_ID,courseCode);
					standardSearchDo.setFilters(filters);
				}
				standardSearchDo.setQuery(text);
				if (text != null && text.trim().length() > 0) {
					standardsPreferenceOrganizeToolTip.hide();
					standardSuggestOracle.clear();
					if(standardPreflist!=null){
						for(int count=0; count<standardPreflist.size();count++) {
							if(text.contains("CCSS") || text.contains("TEKS") || text.contains("CA") ||text.contains("NGSS")||text.contains("CAS612")||text.contains("CASK5")||text.contains("CAELD")||text.contains("CSC")) {
							if(text.contains(standardPreflist.get(count))) {
								standardsPrefDisplayPopup = true;
								break;
							} else {
								standardsPrefDisplayPopup = false;
							}
							}else{
								standardsPrefDisplayPopup = true;
							}
						}
						
					}
						
					if(standardsPrefDisplayPopup){
						standardsPreferenceOrganizeToolTip.hide();
						getUiHandlers().getAutoSuggestedStandardsList(standardSearchDo);
					}
					else{
						standardSgstBox.hideSuggestionList();
						standardSuggestOracle.clear();
						standardsPreferenceOrganizeToolTip.show();
						standardsPreferenceOrganizeToolTip.setPopupPosition(standardSgstBox.getAbsoluteLeft()+3, standardSgstBox.getAbsoluteTop()+33);
					}
					
					}
			}

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}
		};
		
		standardSgstBox.addSelectionHandler(this);
		
		centurySgstBox = new AppSuggestBox(centurySuggestOracle) {
			@Override
			public void keyAction(String text,KeyUpEvent event) {
				text=text.toUpperCase();
				centurySearchDo.setSearchResults(null);
				if (text != null && text.trim().length() > 0) {
						centurySearchDo.setQuery(text);
						getUiHandlers().getAutoSuggestedCenturyList(centurySearchDo);
				}
			}

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}
		};
		
		centurySgstBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String codeId=getCodeIdByStandardDo(centurySgstBox.getValue(),centurySearchDo.getSearchResults());
				selectedValuesFromAutoSuggest.put(Long.parseLong(codeId), centurySgstBox.getValue());
				hilightSelectedValuesFromAutoSuggest.put(Long.parseLong(codeId), centurySgstBox.getValue());
				centPanel.add(create21CenturyLabel(centurySgstBox.getValue(),codeId,""));
				getUiHandlers().updateCentury(collectionDo.getGooruOid(),"add",selectedValuesFromAutoSuggest);
				reset21CenturyCount();
				centurySgstBox.setText("");
				centurySgstBox.getElement().setAttribute("alt","");
				centurySgstBox.getElement().setAttribute("title","");
				centurySuggestOracle.clear();
			}
		});
		
		centurySgstBox.getTextBox().getElement().setAttribute("placeholder", i18n.GL3122_1());
		
		res = CollectionCBundle.INSTANCE;
		CollectionCBundle.INSTANCE.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		GradeUpdate.setText("Grades are updating..");
		GradeUpdate.setVisible(false);
		browseBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().getAddStandards();
				
			}
		});

		centbrowseBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().getAddCentury();
			}
		});
		
		BlurHandler blurhander=new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if(standardsPreferenceOrganizeToolTip!=null && standardsPreferenceOrganizeToolTip.isShowing()){
					standardsPreferenceOrganizeToolTip.hide();
				}
			}
		};
		standardSgstBox.addDomHandler(blurhander, BlurEvent.getType());
		standardSgstBox.getElement().setId("tbautoStandardSgstBox");
		gradeLbl.setText(i18n.GL1076());
		gradeLbl.getElement().setId("lblGradeLbl");
		gradeLbl.getElement().setAttribute("alt",i18n.GL1076());
		gradeLbl.getElement().setAttribute("title",i18n.GL1076());
		
		selectGradeLbl.getElement().setId("lblSelectGradeLbl");
		
		selectCourseLbl.getElement().setId("lblSelectCourseLbl");
		
		
		addCourseBtn.setText(i18n.GL0847());
		addCourseBtn.getElement().setId("btnAddCourseBtn");
		addCourseBtn.getElement().setAttribute("alt",i18n.GL0847());
		addCourseBtn.getElement().setAttribute("title",i18n.GL0847());
		
		removeCourseBtn.setText(i18n.GL0848());
		removeCourseBtn.getElement().setId("btnRemoveCourseBtn");
		removeCourseBtn.getElement().setAttribute("alt",i18n.GL0848());
		removeCourseBtn.getElement().setAttribute("title",i18n.GL0848());
		
		standardLabel.setText(i18n.GL0575());
		standardLabel.getElement().setId("lblStandardLabel");
		standardLabel.getElement().setAttribute("alt",i18n.GL0575());
		standardLabel.getElement().setAttribute("title",i18n.GL0575());
		
		centLabel.setText(i18n.GL3199());
		centLabel.getElement().setId("lblCentLabel");
		centLabel.getElement().setAttribute("alt",i18n.GL3199());
		centLabel.getElement().setAttribute("title",i18n.GL3199());
		
		addStandardBtn.setText(i18n.GL0590());
		addStandardBtn.getElement().setId("btnAddStandardBtn");
		addStandardBtn.getElement().setAttribute("alt",i18n.GL0590());
		addStandardBtn.getElement().setAttribute("title",i18n.GL0590());
		
		standardMaxMsg.setText(i18n.GL0849());
		standardMaxMsg.getElement().setId("lblStandardMaxMsg");
		standardMaxMsg.getElement().setAttribute("alt",i18n.GL0849());
		standardMaxMsg.getElement().setAttribute("title",i18n.GL0849());
		
		instructionalMethod.setText(i18n.GL1637());
		instructionalMethod.getElement().setId("lblInstructionalMethod");
		instructionalMethod.getElement().setAttribute("alt",i18n.GL1637());
		instructionalMethod.getElement().setAttribute("title",i18n.GL1637());
		
		audienceLabel.setText(i18n.GL1638());
		audienceLabel.getElement().setId("lblAudienceLabel");
		audienceLabel.getElement().setAttribute("alt",i18n.GL1638());
		audienceLabel.getElement().setAttribute("title",i18n.GL1638());
		
		instructionalTitle.getElement().setId("lblInstructionalTitle");
		
		
		audienceTitle.getElement().setId("lblAdienceTitle");
		
		
		textAreaVal.setText(i18n.GL1641());
		textAreaVal.getElement().setId("tatTextAreaVal");
		textAreaVal.getElement().setAttribute("alt",i18n.GL1641());
		textAreaVal.getElement().setAttribute("title",i18n.GL1641());
		StringUtil.setAttributes(textAreaVal, true);
		
		languageObjectiveHeader.setText(i18n.GL1642());
		languageObjectiveHeader.getElement().setId("lblLanguageObjectiveHeader");
		languageObjectiveHeader.getElement().setAttribute("alt",i18n.GL1642());
		languageObjectiveHeader.getElement().setAttribute("title",i18n.GL1642());
		
		depthOfKnowledgeHeader.setText(i18n.GL1643());
		depthOfKnowledgeHeader.getElement().setId("lblDepthOfKnowledgeHeader");
		depthOfKnowledgeHeader.getElement().setAttribute("alt",i18n.GL1643());
		depthOfKnowledgeHeader.getElement().setAttribute("title",i18n.GL1643());
		
		depthOfKnowledgeTitle.getElement().setId("lblDepthOfKnowledgeTitle");
		
	
		chkLevelRecall.setText(i18n.GL1645());
		chkLevelRecall.getElement().setId("chkLevelRecall");
		chkLevelRecall.getElement().setAttribute("alt",i18n.GL1645());
		chkLevelRecall.getElement().setAttribute("title",i18n.GL1645());
		
		chkLevelSkillConcept.setText(i18n.GL1646());
		chkLevelSkillConcept.getElement().setId("chkLevelSkillConcept");
		chkLevelSkillConcept.getElement().setAttribute("alt",i18n.GL1646());
		chkLevelSkillConcept.getElement().setAttribute("title",i18n.GL1646());
		
		chkLevelStrategicThinking.setText(i18n.GL1647());
		chkLevelStrategicThinking.getElement().setId("chkLevelStrategicThinking");
		chkLevelStrategicThinking.getElement().setAttribute("alt",i18n.GL1647());
		chkLevelStrategicThinking.getElement().setAttribute("title",i18n.GL1647());
		
		chkLevelExtendedThinking.setText(i18n.GL1648());
		chkLevelExtendedThinking.getElement().setId("chkLevelExtendedThinking");
		chkLevelExtendedThinking.getElement().setAttribute("alt",i18n.GL1648());
		chkLevelExtendedThinking.getElement().setAttribute("title",i18n.GL1648());
		
		learningInnovationHeader.setText(i18n.GL1649());
		learningInnovationHeader.getElement().setId("lblLearningInnovationHeader");
		learningInnovationHeader.getElement().setAttribute("alt",i18n.GL1649());
		learningInnovationHeader.getElement().setAttribute("title",i18n.GL1649());
		
		learningInnovationTitle.getElement().setId("lblLearningInnovationTitle");
		
		
		learninglevel1.setText(i18n.GL1651());
		learninglevel1.getElement().setId("chkLearninglevel1");
		learninglevel1.getElement().setAttribute("alt",i18n.GL1651());
		learninglevel1.getElement().setAttribute("title",i18n.GL1651());
		
		learninglevel2.setText(i18n.GL1652());
		learninglevel2.getElement().setId("chkLearninglevel2");
		learninglevel2.getElement().setAttribute("alt",i18n.GL1652());
		learninglevel2.getElement().setAttribute("title",i18n.GL1652());
		
		learninglevel3.setText(i18n.GL1653());
		learninglevel3.getElement().setId("chkLearninglevel3");
		learninglevel3.getElement().setAttribute("alt",i18n.GL1653());
		learninglevel3.getElement().setAttribute("title",i18n.GL1653());
		
		lblInstructionalPlaceHolder.setText(i18n.GL0105());
		lblInstructionalPlaceHolder.getElement().setId("lblInstructionalPlaceHolder");
		lblInstructionalPlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
		lblInstructionalPlaceHolder.getElement().setAttribute("title",i18n.GL0105());
		
		lblAudiencePlaceHolder.setText(i18n.GL0105());
		lblAudiencePlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
		lblAudiencePlaceHolder.getElement().setAttribute("title",i18n.GL0105());
		
		configureClickEvents();
		
		toggleArrowButtonPrimary.removeStyleName(res.css().primaryToggleArrowBottomrotateRight());
		toggleArrowButtonSecondary.removeStyleName(res.css().primaryToggleArrowBottomrotateRight());
		
		toggleArrowButtonPrimary.getElement().setId("lblToggleArrowButtonPrimary");
		toggleArrowButtonSecondary.getElement().setId("lblToggleArrowButtonSecondary");
		
		primaryLabelTag.getElement().setInnerHTML(i18n.GL1656());
		primaryLabelTag.getElement().setId("pnlPrimaryLabelTag");
		primaryLabelTag.getElement().setAttribute("alt",i18n.GL1656());
		primaryLabelTag.getElement().setAttribute("title",i18n.GL1656());
		
		secondaryHeaderLabel.getElement().setInnerHTML(i18n.GL1657());
		secondaryHeaderLabel.getElement().setId("pnlSecondaryHeaderLabel");
		secondaryHeaderLabel.getElement().setAttribute("alt",i18n.GL1657());
		secondaryHeaderLabel.getElement().setAttribute("title",i18n.GL1657());
		
		lblCharLimit.getElement().getStyle().setMarginRight(0, Unit.PX);
		String value = StringUtil.generateMessage(i18n.GL2103(), "500");
		lblCharLimit.setText(value);
		StringUtil.setAttributes(lblCharLimit.getElement(), "charLimitLbl", value, value);
		
		charLimitErrLbl.setVisible(false);
		charLimitErrLbl.setText(i18n.GL0143());
		StringUtil.setAttributes(charLimitErrLbl.getElement(), "charLimitErrLbl", i18n.GL0143(), i18n.GL0143());
		
		ClickHandler infoRootHandler= new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!isClickedOnDropDwn && (spanelInstructionalPanel.isVisible() || spanelAudiencePanel.isVisible())){
					spanelInstructionalPanel.setVisible(false);
					spanelAudiencePanel.setVisible(false);
				}else if(!isClickedOnDropDwn){
					spanelInstructionalPanel.setVisible(false);
					spanelAudiencePanel.setVisible(false);
				}else{
					isClickedOnDropDwn=false;
				}
				
			}
		};
		
		hPanelMainInfo.addDomHandler(infoRootHandler, ClickEvent.getType());
		
		RootPanel.get().addDomHandler(infoRootHandler, ClickEvent.getType());
		
		textAreaVal.getElement().removeAttribute("style");

		languageObjectiveerrLabel.setVisible(false);

		textAreaVal.getElement().setAttribute("maxlength", "500");
		
		textAreaVal.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				String directionText=textAreaVal.getText();		
				if(directionText.equalsIgnoreCase(i18n.GL1641())){
					textAreaVal.setText("");
					textAreaVal.getElement().setAttribute("alt","");
					textAreaVal.getElement().setAttribute("title","");
				}
				textAreaVal.getElement().getStyle().setColor("black");
			}
		});
		
		textAreaVal.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(textAreaVal.getText().length() >= 500)
				{
					charLimitErrLbl.setVisible(true);
				}else{
					charLimitErrLbl.setVisible(false);
				}
			}
		});
		
		textAreaVal.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				
				if(textAreaVal.getText().length() >= 500)
				{
					charLimitErrLbl.setVisible(true);
				}else{
					charLimitErrLbl.setVisible(false);
				}
				final String learningObjective=textAreaVal.getText();
				if(learningObjective.length() == 0){
					textAreaVal.setText(i18n.GL1641());
					textAreaVal.getElement().setAttribute("alt",i18n.GL1641());
					textAreaVal.getElement().setAttribute("title",i18n.GL1641());
					textAreaVal.getElement().getStyle().setColor("#999");
				}
				//As the learning objective is not mandatory removed the if else condition
				Map<String, String> parms = new HashMap<String, String>();
				parms.put("text", textAreaVal.getText());
				AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean value) {
					//callapi
					if(!value){
						textAreaVal.getElement().removeAttribute("style");
						languageObjectiveerrLabel.setVisible(false);
						if(collectionDo !=null){
							AppClientFactory.getInjector().getResourceService().updateCollectionLanguageObjective(collectionDo,learningObjective, new SimpleAsyncCallback<CollectionDo>() {
								@Override
								public void onSuccess(CollectionDo result) {
											
									}
							});
						}
					}else{
						textAreaVal.getElement().getStyle().setBorderColor("orange");
						languageObjectiveerrLabel.setText(i18n.GL0554());
						languageObjectiveerrLabel.getElement().setAttribute("alt",i18n.GL0554());
						languageObjectiveerrLabel.getElement().setAttribute("title",i18n.GL0554());
						languageObjectiveerrLabel.setVisible(true);
					 }
				}
			});
		}
	});
	
		List<String> instructionalMethodList = Arrays.asList(InstructionalMethodStr.split(","));

		
		for(int k=0; k<instructionalMethodList.size(); k++)
		{

				String instructionalTitle = instructionalMethodList.get(k);
				
				final Label titleLabel = new Label(instructionalTitle);
				titleLabel.setStyleName(CollectionAssignCBundle.INSTANCE.css().classpageTitleText());
				titleLabel.getElement().setAttribute("id", instructionalTitle);
				//Set Click event for title
				titleLabel.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {		
						String optionSelected = titleLabel.getElement().getId();
						lblInstructionalPlaceHolder.setText(optionSelected);
						lblInstructionalPlaceHolder.getElement().setAttribute("alt",optionSelected);
						lblInstructionalPlaceHolder.getElement().setAttribute("title",optionSelected);
						spanelInstructionalPanel.setVisible(false);
						lblInstructionalPlaceHolder.getElement().setId(titleLabel.getElement().getId());
						lblInstructionalPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
						
				
						if(collectionDo.getInstructionalMethod() != null && collectionDo.getInstructionalMethod().size()>0)
						{
							for(int i=0;i<collectionDo.getInstructionalMethod().size();i++)
							{
								if(collectionDo.getInstructionalMethod().get(i).isSelected()){
								AppClientFactory.getInjector().getResourceService().updateCollectionInstructionalMethod(collectionDo, collectionDo.getInstructionalMethod().get(i).getValue(),false, new SimpleAsyncCallback<CollectionDo>() {
									@Override
									public void onSuccess(CollectionDo result) {
										
									}
								});	
								}
							}
						}
						

							AppClientFactory.getInjector().getResourceService().updateCollectionInstructionalMethod(collectionDo, optionSelected,true, new SimpleAsyncCallback<CollectionDo>() {
								@Override
								public void onSuccess(CollectionDo result) {
							}
							});

							lblInstructionalPlaceHolder.setText(optionSelected);
							lblInstructionalPlaceHolder.getElement().setAttribute("alt",optionSelected);
							lblInstructionalPlaceHolder.getElement().setAttribute("title",optionSelected);
				
					}
				});
				htmlInstructionalListContainer.add(titleLabel);
		}
		
	List<String> audienceList = Arrays.asList(AudienceStr.split(","));
		
		for(int n=0; n<audienceList.size(); n++)
		{

				String audienceTitle = audienceList.get(n);
				
				final Label titleLabel = new Label(audienceTitle);
				titleLabel.setStyleName(CollectionAssignCBundle.INSTANCE.css().classpageTitleText());
				titleLabel.getElement().setAttribute("id", audienceTitle);
				//Set Click event for title
				titleLabel.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {		
						String optionSelected = titleLabel.getElement().getId();
						lblAudiencePlaceHolder.setText(optionSelected);
						lblAudiencePlaceHolder.getElement().setAttribute("alt",optionSelected);
						lblAudiencePlaceHolder.getElement().setAttribute("title",optionSelected);
						spanelAudiencePanel.setVisible(false);
						lblAudiencePlaceHolder.getElement().setId(titleLabel.getElement().getId());
						lblAudiencePlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
						
				
						if(collectionDo.getAudience() != null && collectionDo.getAudience().size()>0)
						{
							for(int j=0;j<collectionDo.getAudience().size();j++)
							{
								if(collectionDo.getAudience().get(j).isSelected()){
								AppClientFactory.getInjector().getResourceService().updateCollectionAudience(collectionDo, collectionDo.getAudience().get(j).getValue(),false, new SimpleAsyncCallback<CollectionDo>() {
									@Override
									public void onSuccess(CollectionDo result) {
									
									}
								});
								
								}
							}
						}
						

							AppClientFactory.getInjector().getResourceService().updateCollectionAudience(collectionDo, optionSelected,true, new SimpleAsyncCallback<CollectionDo>() {
								@Override
								public void onSuccess(CollectionDo result) {
									
							
								}
							});

							lblAudiencePlaceHolder.setText(optionSelected);
							lblAudiencePlaceHolder.getElement().setAttribute("alt",optionSelected);
							lblAudiencePlaceHolder.getElement().setAttribute("title",optionSelected);
				
					}
				});
				htmlAudienceListContainer.add(titleLabel);
				
			
		}
		
		
		lblAudiencePlaceHolder.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				OpenAudienceDropdown();
			}

			
		});
		
		lblAudienceArrow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				OpenAudienceDropdown();
			}
		});
		
		lblInstructionalPlaceHolder.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				OpenInstructionalDropdown();
			}
		});
		
		lblInstructionalArrow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				OpenInstructionalDropdown();	
			}
		});
		
		AppClientFactory.getEventBus().addHandler(AddCourseEvent.TYPE, addCourseHandler);
		standardsDefaultText.getElement().setId("lblStandardsDefaultText");
		centDefaultText.getElement().setId("lblCenturyDefaultText");
		panelLoading.getElement().setId("pnlPanelLoading");
		mainInfoPanel.getElement().setId("pnlMainInfoPanel");
		KinderGarten.getElement().setId("fpnlKinderGarten");
		gradeTopList.getElement().setId("fpnlGradeTopList");
		gradeMiddleList.getElement().setId("fpnlGradeMiddleList");
		gradeBottomList.getElement().setId("fpnlGradeBottomList");
		higherEducation.getElement().setId("fpnlHigherEducation");
		courseLabel.getElement().setId("lblCourseLabel");
		courseData.getElement().setId("fpnlCourseData");
		standardsPanel.getElement().setId("fpnlStandardsPanel");
		secondaryContentsContainer.getElement().setId("pnlSecondaryContentsContainer");
		languageObjectiveTitle.getElement().setId("lblLanguageObjectiveTitle");
		languageObjectiveerrLabel.getElement().setId("lblLanguageObjectiveerrLabel");
		lblInstructionalArrow.getElement().setId("lblInstructionalArrow");
		spanelInstructionalPanel.getElement().setId("sbSpanelInstructionalPanel");
		htmlInstructionalListContainer.getElement().setId("pnlHtmlInstructionalListContainer");
		lblAudienceArrow.getElement().setId("lblAudienceArrow");
		spanelAudiencePanel.getElement().setId("sbSpanelAudiencePanel");
		htmlAudienceListContainer.getElement().setId("pnlHtmlAudienceListContainer");
		
		addStandardBtn.setVisible(false);
		panelLoading.setVisible(true);
		mainInfoPanel.setVisible(false);
		removeCourseBtn.setVisible(false);
		addCourseBtn.getElement().getStyle().setMarginRight(10, Unit.PX);
		removeCourseBtn.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		removeCourseBtn.setVisible(false);
		courseData.getElement().getStyle().setDisplay(Display.NONE);
		
		spanelInstructionalPanel.setVisible(false);
		spanelAudiencePanel.setVisible(false);
		pnl21CenturySkils.setVisible(false);
	}
	/**
	 * 
	 * @function modifyStaticText 
	 * 
	 * @created_date : 23-Jan-2015
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param collectionType
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void modifyStaticText(String collectionType){
		
		addAttributesToWidget(selectGradeLbl,collectionType!=null&&ASSESSMENT.equals(collectionType)? i18n.GL3025() : i18n.GL0820());
		addAttributesToWidget(selectCourseLbl,collectionType!=null&&ASSESSMENT.equals(collectionType) ? i18n.GL3026() : i18n.GL0846());
		addAttributesToWidget(standardsDefaultText, collectionType!=null&&ASSESSMENT.equals(collectionType)? i18n.GL3203() : i18n.GL0749());
		addAttributesToWidget(centDefaultText, collectionType!=null&&ASSESSMENT.equals(collectionType) ? i18n.GL3204() : i18n.GL3123_1());
		addAttributesToWidget(depthOfKnowledgeTitle, collectionType!=null&&ASSESSMENT.equals(collectionType)? i18n.GL3028() : i18n.GL1644());
		addAttributesToWidget(learningInnovationTitle, collectionType!=null&&ASSESSMENT.equals(collectionType) ? i18n.GL3029() : i18n.GL1650());
		addAttributesToWidget(instructionalTitle, collectionType!=null&&ASSESSMENT.equals(collectionType) ? i18n.GL3030() : i18n.GL1639());
		addAttributesToWidget(audienceTitle, collectionType!=null&&ASSESSMENT.equals(collectionType)? i18n.GL3031() : i18n.GL1640());

	}
	
	public void addAttributesToWidget(Label labelText, String text){
		labelText.setText(text);
		labelText.getElement().setAttribute("alt",text);
		labelText.getElement().setAttribute("title",text);
	}
	
	public void configureClickEvents()
	{
	chkLevelRecall.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				  CheckBox checkBox = (CheckBox) event.getSource();
			      boolean checked = checkBox.getValue();
			      updateDepthOfKnowledge(collectionDo, i18n.GL1645(),checked);
			}
		});
		
		chkLevelSkillConcept.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				  CheckBox checkBox = (CheckBox) event.getSource();
			      boolean checked = checkBox.getValue();
			      updateDepthOfKnowledge(collectionDo, i18n.GL1646(),checked);
			}
		});
		
		chkLevelStrategicThinking.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				CheckBox checkBox = (CheckBox) event.getSource();
		        boolean checked = checkBox.getValue();
		        updateDepthOfKnowledge(collectionDo, i18n.GL1647(),checked);
			}
		});
		
		chkLevelExtendedThinking.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				  CheckBox checkBox = (CheckBox) event.getSource();
			      boolean checked = checkBox.getValue();
			      updateDepthOfKnowledge(collectionDo, i18n.GL1648(),checked);
			}
		});
		
		learninglevel1.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				  CheckBox checkBox = (CheckBox) event.getSource();
			      boolean checked = checkBox.getValue();
			      updateLearningSkills(collectionDo, i18n.GL1651(),checked);
			}
		});
		
		learninglevel2.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				  CheckBox checkBox = (CheckBox) event.getSource();
			      boolean checked = checkBox.getValue();		
			      updateLearningSkills(collectionDo, i18n.GL1652(),checked);
			}
		});
		
	learninglevel3.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				  CheckBox checkBox = (CheckBox) event.getSource();
			      boolean checked = checkBox.getValue();
			      updateLearningSkills(collectionDo, i18n.GL1653(),checked);
			}
		});
	}

	private void OpenAudienceDropdown() {
		isClickedOnDropDwn=true;
		if (spanelInstructionalPanel.isVisible()){
			spanelInstructionalPanel.setVisible(false);
		}
		
		if (spanelAudiencePanel.isVisible()){
			spanelAudiencePanel.setVisible(false);
		}else{
			spanelAudiencePanel.setVisible(true);
		}
	}
	
	private void OpenInstructionalDropdown() {
		isClickedOnDropDwn=true;
		if (spanelAudiencePanel.isVisible()){
			spanelAudiencePanel.setVisible(false);
		}
		
		if (spanelInstructionalPanel.isVisible()){
			spanelInstructionalPanel.setVisible(false);
		}else{
			spanelInstructionalPanel.setVisible(true);
		}
		
		
	}
	
	@Override
	public void reset() {
		super.reset();
		collectionDo = null;
		gradeTopList.clear();
		gradeMiddleList.clear();
		gradeBottomList.clear();
		standardsPanel.clear();
		centPanel.clear();
		KinderGarten.clear();
		higherEducation.clear();
		standardSuggestOracle.clear();
		standardCodesMap.clear();
		courseDo.clear();
		courseData.clear();
		addCourseBtn.setText(ADD_COURSE);
		addCourseBtn.getElement().setAttribute("alt",ADD_COURSE);
		addCourseBtn.getElement().setAttribute("title",ADD_COURSE);
		addCourseBtn.setVisible(true);
		removeCourseBtn.setVisible(false);
		selectedValuesFromAutoSuggest.clear();
		hilightSelectedValuesFromAutoSuggest.clear();
	}

	@Override
	public void setData(CollectionDo collectionDoVal) {
		reset();
		this.collectionDo = collectionDoVal;
		modifyStaticText(collectionDo.getCollectionType());
			if(collectionDoVal.getLanguageObjective() != null)
			{
				textAreaVal.setText(collectionDo.getLanguageObjective());
				textAreaVal.getElement().setAttribute("alt",collectionDo.getLanguageObjective());
				textAreaVal.getElement().setAttribute("title",collectionDo.getLanguageObjective());
			}
			else
			{
				textAreaVal.setText(i18n.GL1641());
				textAreaVal.getElement().setAttribute("alt",i18n.GL1641());
				textAreaVal.getElement().setAttribute("title",i18n.GL1641());
			}
			if(collectionDoVal.getDepthOfKnowledges()!=null){
				for(int i=0; i<collectionDoVal.getDepthOfKnowledges().size(); i++)
				{
					String compareValueLevel = collectionDoVal.getDepthOfKnowledges().get(i).getValue().replaceAll("\\s+","");
					String compareValueLevelFetched = chkLevelRecall.getText().replaceAll("\\s+","");
					String compareValueLevelCheckbox1 = chkLevelSkillConcept.getText().replaceAll("\\s+","");
					String compareValueLevelCheckbox2 = chkLevelExtendedThinking.getText().replaceAll("\\s+","");
					String compareValueLevelCheckbox3 = chkLevelStrategicThinking.getText().replaceAll("\\s+","");
					if(compareValueLevel.equalsIgnoreCase(compareValueLevelFetched))
					{
						if(collectionDoVal.getDepthOfKnowledges().get(i).isSelected()==true)
						{
						chkLevelRecall.setValue(true);
						}
						else
						{
						chkLevelRecall.setValue(false);
						}
					}
					else if(compareValueLevel.equalsIgnoreCase(compareValueLevelCheckbox1))
					{
						if(collectionDoVal.getDepthOfKnowledges().get(i).isSelected()==true)
						{
							chkLevelSkillConcept.setValue(true);
						}
						else
						{
							chkLevelSkillConcept.setValue(false);
						}
					}
					else if(compareValueLevel.equalsIgnoreCase(compareValueLevelCheckbox2))
					{
						if(collectionDoVal.getDepthOfKnowledges().get(i).isSelected()==true)
						{
							chkLevelExtendedThinking.setValue(true);
						}
						else
						{
							chkLevelExtendedThinking.setValue(false);
						}
					}
					else if(compareValueLevel.equalsIgnoreCase(compareValueLevelCheckbox3))
					{
						if(collectionDoVal.getDepthOfKnowledges().get(i).isSelected()==true)
						{
							chkLevelStrategicThinking.setValue(true);
						}
						else
						{
							chkLevelStrategicThinking.setValue(false);
						}
					}
				}
			}
			
			if(collectionDoVal.getLearningSkills()!=null){
				for(int j=0; j<collectionDoVal.getLearningSkills().size(); j++)
				{
					String compareValueLevel = collectionDoVal.getLearningSkills().get(j).getValue().replaceAll("\\s+","");
					String compareValueLevelFetched = learninglevel1.getText().replaceAll("\\s+","");
					String compareValueLevelCheckbox1 = learninglevel2.getText().replaceAll("\\s+","");
					String compareValueLevelCheckbox2 = learninglevel3.getText().replaceAll("\\s+","");
					if(compareValueLevel.equalsIgnoreCase(compareValueLevelFetched))
					{
						if(collectionDoVal.getLearningSkills().get(j).isSelected()==true)
						{
							learninglevel1.setValue(true);
						}
						else
						{
							learninglevel1.setValue(false);
						}
					}
					else if(compareValueLevel.equalsIgnoreCase(compareValueLevelCheckbox1))
					{
						if(collectionDoVal.getLearningSkills().get(j).isSelected()==true)
						{
							learninglevel2.setValue(true);
						}
						else
						{
							learninglevel2.setValue(false);
						}
					}
					else if(compareValueLevel.equalsIgnoreCase(compareValueLevelCheckbox2))
					{
						if(collectionDoVal.getLearningSkills().get(j).isSelected()==true)
						{
							learninglevel3.setValue(true);
						}
						else
						{
							learninglevel3.setValue(false);
						}
					}

				}
			}
			
			
			if(collectionDoVal.getInstructionalMethod()!=null){
				for(int m=0; m<collectionDoVal.getInstructionalMethod().size(); m++)
				{
					if(collectionDoVal.getInstructionalMethod().get(m).getValue() != null || !collectionDoVal.getInstructionalMethod().get(m).getValue().isEmpty())
					{
						if(collectionDoVal.getInstructionalMethod().get(m).isSelected()==true)
						{
							lblInstructionalPlaceHolder.setText(collectionDoVal.getInstructionalMethod().get(m).getValue());
							lblInstructionalPlaceHolder.getElement().setAttribute("alt",collectionDoVal.getInstructionalMethod().get(m).getValue());
							lblInstructionalPlaceHolder.getElement().setAttribute("title",collectionDoVal.getInstructionalMethod().get(m).getValue());
							break;
						}
						else
						{
						lblInstructionalPlaceHolder.setText(i18n.GL0105());
						lblInstructionalPlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
						lblInstructionalPlaceHolder.getElement().setAttribute("title",i18n.GL0105());
						}
					}
					else
					{
					lblInstructionalPlaceHolder.setText(i18n.GL0105());
					lblInstructionalPlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
					lblInstructionalPlaceHolder.getElement().setAttribute("title",i18n.GL0105());
					}
				}
			}
			
			if(collectionDoVal.getAudience()!=null){
				for(int n=0; n<collectionDoVal.getAudience().size(); n++)
				{
					if(collectionDoVal.getAudience().get(n).getValue() != null || !collectionDoVal.getAudience().get(n).getValue().isEmpty())
					{
						if(collectionDoVal.getAudience().get(n).isSelected()==true)
						{
							lblAudiencePlaceHolder.setText(collectionDoVal.getAudience().get(n).getValue());
							lblAudiencePlaceHolder.getElement().setAttribute("alt",collectionDoVal.getAudience().get(n).getValue());
							lblAudiencePlaceHolder.getElement().setAttribute("title",collectionDoVal.getAudience().get(n).getValue());
							break;
						}
						else
						{
							lblAudiencePlaceHolder.setText(i18n.GL0105());
							lblAudiencePlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
							lblAudiencePlaceHolder.getElement().setAttribute("title",i18n.GL0105());
						}
					}
					else
					{
					lblAudiencePlaceHolder.setText(i18n.GL0105());
					lblAudiencePlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
					lblAudiencePlaceHolder.getElement().setAttribute("title",i18n.GL0105());
					}
				}
			}
			if(collectionDoVal.getTaxonomySet()!=null){
				if(collectionDoVal.getTaxonomySet().size()==0){
					courseData.getElement().getStyle().setDisplay(Display.NONE);
					addCourseBtn.setText(ADD_COURSE);
					addCourseBtn.getElement().setAttribute("alt",ADD_COURSE);
					addCourseBtn.getElement().setAttribute("title",ADD_COURSE);
					removeCourseBtn.setVisible(false);
					courseCode="";
				}else{
					for (CodeDo code : collectionDoVal.getTaxonomySet()) {
						if (code.getDepth() == 2) {
							courseDo.add(code.getLabel());
							courseData.add(createCourseLabel(code.getLabel(), code.getCodeId() + "", code.getLabel()));
							courseData.getElement().getStyle().setDisplay(Display.BLOCK);
							courseCode=Integer.toString(code.getCodeId());
							addCourseBtn.setText(ADD_COURSE);
							addCourseBtn.getElement().setAttribute("alt",CHANGE_COURSE);
							addCourseBtn.getElement().setAttribute("title",CHANGE_COURSE);
							removeCourseBtn.setVisible(false);
						}
					}
				}
			}
			if (collectionDoVal.getMetaInfo() != null && collectionDoVal.getMetaInfo().getStandards() != null) {
				for (StandardFo standard : collectionDoVal.getMetaInfo().getStandards()) {
					standardsPanel.add(createStandardLabel(standard.getCode(), standard.getCodeId() + "", standard.getDescription()));
				}
			}
			//Added this for to display 21 century skills 
			if (collectionDoVal.getMetaInfo() != null && collectionDoVal.getMetaInfo().getSkills() != null) {
				centPanel.clear();
				for (StandardFo standard : collectionDoVal.getMetaInfo().getSkills()) {
					hilightSelectedValuesFromAutoSuggest.put(Long.parseLong(standard.getCodeId()+""), standard.getLabel());
					centPanel.add(create21CenturyLabel(standard.getLabel(),standard.getCodeId()+"",standard.getDescription()));
				}
			}
			resetStandardCount();
			resetCourseCount();
			setGradeList();
			reset21CenturyCount();
	}

	@Override
	public void setCourseList(List<LibraryCodeDo> libraryCode) {
		collectionCourseLst = new GroupedListBox();
		collectionCourseLst.addStyleName(res.css().infoTextBox());
		collectionCourseLst.addItem(" - Select Course(s) - ", "-1");
		if (libraryCode != null) {
			for (LibraryCodeDo libraryCodeDo : libraryCode) {
				for (LibraryCodeDo liCodeDo : libraryCodeDo.getNode()) {
					collectionCourseLst.addItem(libraryCodeDo.getLabel() + "|" + liCodeDo.getLabel(), liCodeDo.getCodeId() + "");
				}
			}
		}
		toggleArrowButtonPrimary.removeStyleName(res.css().primaryToggleArrowBottomrotateRight());
		toggleArrowButtonSecondary.removeStyleName(res.css().primaryToggleArrowBottomrotateRight());
		
		secondaryContentsContainer.setVisible(true);
		
		panelLoading.setVisible(false);
		mainInfoPanel.setVisible(true);
	}

	/**
	 * New course is created for collection
	 * 
	 * @param courseLabel
	 *            the course of the label widget which is used to get all
	 *            courses
	 * @param courseCode
	 *            the course of the course code, an absolute course code will be
	 *            used to create a new coursefinal StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
	
	 * @return the label of all course created for the collection.
	 */
	protected CloseLabel createCourseLabel(final String courseLabel, final String courseCode) {
		return new CloseLabel(courseLabel) {
           
			@Override
			public void onCloseLabelClick(ClickEvent event) {
				this.removeFromParent();
				getUiHandlers().deleteCourseOrStandard(collectionDo.getGooruOid(), courseCode);
				resetCourseCount();
			
			}
		};
	}

	/**
	 * Adding new course for the collection , will check it has more than five
	 * courses and the selected course is repeated or not
	 * 
	 * @param clickEvent
	 *            specifies event type
	 */
	@UiHandler("addCourseBtn")
	public void onAddCourseClick(ClickEvent clickEvent) {final StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
		int left = (Window.getClientWidth() - 542) /2;
		int top = (Window.getClientHeight() - 469) /2;
		if (courseListUc == null){
			courseListUc=new CourseListUc(collectionDo);
		}else{
			courseListUc.setDefaultCourseData(collectionDo);
		}			
		
		courseListUc.center();
		courseListUc.show();
		courseListUc.setPopupPosition(left, top);
		Window.enableScrolling(false);
	}
	
	
	

	/**
	 * to display message if course exceeds more than five
	 */
	public void courseMaxShow() {
		courseData.setStyleName(CollectionCBundle.INSTANCE.css().coursesContainer());
	}

	/**
	 * to hide message if course exceeds less than five
	 */
	public void courseMaxHide() {
		courseData.setStyleName(CollectionCBundle.INSTANCE.css().floatLeft());
		collectionCourseLst.setStyleName(CollectionCBundle.INSTANCE.css().infoTextBox());
		addCourseBtn.setStyleName(CollectionCBundle.INSTANCE.css().infoAddButton());
	}

	/**
	 * to display message if standard exceeds more than fifteen
	 */
	public void standardMaxShow() {
		standardSgstBox.addStyleName(CollectionCBundle.INSTANCE.css().standardTxtBox());
		addStandardBtn.addStyleName(CollectionCBundle.INSTANCE.css().floatLeftNeeded());
		standardMaxMsg.setStyleName(CollectionCBundle.INSTANCE.css().standardMax());
		standardsPanel.addStyleName(CollectionCBundle.INSTANCE.css().floatLeftNeeded());
		new FadeInAndOut(standardMaxMsg.getElement(), 5000, 5000);
	}
	public GradeLabel frameLabel(String label,CollectionDo collectionDoInternal)
	{
		if (collectionDoInternal.getGrade() != null) {
			String genGrade = collectionDoInternal.getGrade();
			if (genGrade.indexOf("-")>0){
				genGrade = generateGrade(genGrade);
			}
			String grade[] = genGrade.split(",");
			for (int i = 0; i < grade.length; i++) {
				if (label.equals(grade[i])) {
					if(!gradeList.contains(grade[i].toString())){
						gradeList.add(grade[i].toString());
					}
				}
			}
		}
		GradeLabel gradeLblKindergarten = new GradeLabel(label, collectionDoInternal) {	
	
			@Override
			public void onClick(ClickEvent event) {
				GradeUpdate.setVisible(true);
				gradeTopList.setVisible(false);
				gradeMiddleList.setVisible(false);
				gradeBottomList.setVisible(false);
				KinderGarten.setVisible(false);
				higherEducation.setVisible(false);
				if(this.getElement().getAttribute("selected") != null)
				{
				if(this.getElement().getAttribute("selected").contains("selected")){
					this.getElement().getStyle().setProperty("background", "");
					this.getElement().getStyle().setColor("#999");
					this.getElement().removeAttribute("selected");
					try
					{
					gradeList.remove(this.getText());	
					}
					catch(Exception ex)
					{
						AppClientFactory.printSevereLogger("grade catch::"+ex.getMessage());
					}
				
				} else {
					this.getElement().getStyle().setProperty("background", "#0F76BB");
					this.getElement().getStyle().setColor("#fff");
					this.getElement().setAttribute("selected", "selected");
					if(!gradeList.contains(this.getText())){
						gradeList.add(this.getText());
						if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.SHELF)){
							MixpanelUtil.mixpanelEvent("Collaborator_edits_collection");
						}
					}
				}
				} else {
					this.getElement().getStyle().setProperty("background", "#0F76BB");
					this.getElement().getStyle().setColor("#fff");
					this.getElement().setAttribute("selected", "selected");
					if(!gradeList.contains(this.getText())){
						gradeList.add(this.getText());
						//updateGrade(gradeList);
						if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.SHELF)){
							MixpanelUtil.mixpanelEvent("Collaborator_edits_collection");
						}
					}
				}
				updateGrade(gradeList);
			}

		};
		//AppClientFactory.printInfoLogger("update grade list1233::::"+gradeList);
		return gradeLblKindergarten;
	}
	private String generateGrade(String gradeTxt){
		String tmpGradeTxt = "";
		if (gradeTxt.indexOf("-") > 0){
			if (gradeTxt.indexOf(",") == -1){
				tmpGradeTxt = generateGradeIfHypen(gradeTxt);
			}else{
				String gradeList[];
				gradeList = gradeTxt.split(",");
				for (int k=0; k<gradeList.length;k++){
					if (gradeList[k].indexOf("-") > 0){
						if (k==gradeList.length){
							tmpGradeTxt = tmpGradeTxt + generateGradeIfHypen(gradeList[k]);
						}else{
							tmpGradeTxt = tmpGradeTxt + generateGradeIfHypen(gradeList[k]) + ",";
						}
					}else{
						if (k==gradeList.length-1){
							tmpGradeTxt = tmpGradeTxt + gradeList[k];
						}else{
							tmpGradeTxt = tmpGradeTxt + gradeList[k] + ",";
						}
					}
				}
			}
		}
		return tmpGradeTxt;
	}
	
	private String generateGradeIfHypen(String grade){
		String gradeList[];
		StringBuilder gradeStr = new StringBuilder();
		gradeList = grade.split("-");
		if (gradeList.length>=2){
			int start = Integer.parseInt(gradeList[0].trim());
			int end = Integer.parseInt(gradeList[1].trim());
			if ( start < end){
				for (int i = start; i<=end; i++){
					if (i==end){
						gradeStr.append(i);
					}else{
						gradeStr.append(i).append(",");
					}
				}
			}
		}else{
			gradeStr.append(Integer.parseInt(gradeList[0]));
		}
		return gradeStr.toString();
	}

	/**
	 * separate the view according to grade level of the collection
	 */
	public void setGradeList() {
		gradeList.clear();
		KinderGarten.add(frameLabel("Kindergarten", collectionDo));
		higherEducation.add(frameLabel("Higher Education", collectionDo));
		for (int i = 1; i <= 12; i++) {
			if (i <= 4) {
				gradeTopList.add(frameLabel(i + "", collectionDo));
			}
			if (i <= 8 && i >= 5) {
				gradeMiddleList.add(frameLabel(i + "", collectionDo));
			}
			if (i <= 12 && i >= 9) {
				gradeBottomList.add(frameLabel(i + "", collectionDo));
			}
		}
	}

	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		addStandard(standardSgstBox.getValue(), getCodeIdByCode(standardSgstBox.getValue(), standardSearchDo.getSearchResults()));
		standardSgstBox.setText("");
		standardSgstBox.getElement().setAttribute("alt","");
		standardSgstBox.getElement().setAttribute("title","");
		standardSuggestOracle.clear();
	}
	@Override
	public void setCenturySuggestions(SearchDo<StandardFo> centurySearchDo) {
		centurySuggestOracle.clear();
		this.centurySearchDo = centurySearchDo;
		if (this.centurySearchDo.getSearchResults() != null) {
			if(centurySearchDo.getSearchResults().size()>0){
				List<String> sources = getAddedcenturys(centPanel);
				for (StandardFo code : centurySearchDo.getSearchResults()) {
					if (!sources.contains(code.getLabel())) {
						centurySuggestOracle.add(code.getLabel());
					}
					centuryCodesMap.put(code.getCodeId() + "", code.getLabel());
				}
			}
			if (centurySuggestOracle.isEmpty()) {
				centurySuggestOracle.add(NO_MATCH_FOUND);
			}
			centurySgstBox.showSuggestionList();		
		}
	}

	public void setStandardSuggestions(SearchDo<CodeDo> standardSearchDo) {
		standardSuggestOracle.clear();
		this.standardSearchDo = standardSearchDo;
		if (this.standardSearchDo.getSearchResults() != null) {
			/*if(standardSearchDo.getSearchResults().size()>0){*/
			List<String> sources = getAddedStandards(standardsPanel);
			for (CodeDo code : standardSearchDo.getSearchResults()) {
				if (!sources.contains(code.getCode())) {
					standardSuggestOracle.add(code.getCode());
				}
				standardCodesMap.put(code.getCodeId() + "", code.getLabel());
			}
		}
		if (standardSuggestOracle.isEmpty()) {
			standardSuggestOracle.add(NO_MATCH_FOUND);
		}
		standardSgstBox.showSuggestionList();		
	}
	
	/**
	 * get the century values are added for collection
	 * 
	 * @param flowPanel
	 *            having all added standards label
	 * @return century text in list which are added for the collection
	 */
	private List<String> getAddedcenturys(FlowPanel flowPanel) {
		List<String> suggestions = new ArrayList<String>();
		for (Widget widget : flowPanel) {
			if (widget instanceof DownToolTipWidgetUc) {
				suggestions.add(((CloseLabelCentury) ((DownToolTipWidgetUc) widget).getWidget()).getSourceText());
			}
		}
		return suggestions;
	}
	/**
	 * get the standards are added for collection
	 * 
	 * @param flowPanel
	 *            having all added standards label
	 * @return standards text in list which are added for the collection
	 */
	private List<String> getAddedStandards(FlowPanel flowPanel) {
		List<String> suggestions = new ArrayList<String>();
		for (Widget widget : flowPanel) {
			if (widget instanceof DownToolTipWidgetUc) {
				suggestions.add(((CloseLabel) ((DownToolTipWidgetUc) widget).getWidget()).getSourceText());
			}
		}
		return suggestions;
	}

	/**
	 * set the standard text with count while adding and removing the standard
	 */
	private void resetStandardCount() {
		if (standardsPanel.getWidgetCount() > 0) {
			standardLabel.setText(i18n.GL0575() + " (" + standardsPanel.getWidgetCount() + ")");
			standardLabel.getElement().setAttribute("alt",i18n.GL0575() + " (" + standardsPanel.getWidgetCount() + ")");
			standardLabel.getElement().setAttribute("title",i18n.GL0575() + " (" + standardsPanel.getWidgetCount() + ")");
		} else {
			standardLabel.setText(i18n.GL0575());
			standardLabel.getElement().setAttribute("alt",i18n.GL0575());
			standardLabel.getElement().setAttribute("title",i18n.GL0575());
		}
	}
	/**
	 * set the 21 century text with count while adding and removing the 21 century value
	 */
	private void reset21CenturyCount() {
		if (centPanel.getWidgetCount() > 0) {
			centLabel.setText(i18n.GL3199() + " (" + centPanel.getWidgetCount() + ")");
			centLabel.getElement().setAttribute("alt",i18n.GL3199() + " (" + centPanel.getWidgetCount() + ")");
			centLabel.getElement().setAttribute("title",i18n.GL3199() + " (" + centPanel.getWidgetCount() + ")");
		} else {
			centLabel.setText(i18n.GL3199());
			centLabel.getElement().setAttribute("alt",i18n.GL3199());
			centLabel.getElement().setAttribute("title",i18n.GL3199());
		}
	}
	/**
	 * set the course text with count while adding and removing the course
	 */
	private void resetCourseCount() {		
		courseLabel.setText(i18n.GL0574());
		courseLabel.getElement().setAttribute("alt",i18n.GL0574());
		courseLabel.getElement().setAttribute("title",i18n.GL0574());
	}
	
	AddCourseHandler addCourseHandler=new AddCourseHandler() {
		@Override
		public void onAddCourse(String courseName, String courseId) {
			courseDo.add(courseName);
			courseData.add(createCourseLabel(courseName, courseId + "", courseName));
			courseLabel.getElement().setAttribute("alt",courseName);
			courseLabel.getElement().setAttribute("title",courseName);
			courseData.setVisible(true);
			addCourseBtn.setText(ADD_COURSE);
			addCourseBtn.getElement().setAttribute("alt",CHANGE_COURSE);
			addCourseBtn.getElement().setAttribute("title",CHANGE_COURSE);
			removeCourseBtn.setVisible(false);
			courseCode=courseId;
		}
	};

	
	@UiHandler("removeCourseBtn")
	public void onClickRemoveCourseBtn(ClickEvent clickEvent){
		courseData.getElement().getStyle().setDisplay(Display.NONE);
		addCourseBtn.setText(ADD_COURSE);
		addCourseBtn.getElement().setAttribute("alt",ADD_COURSE);
		addCourseBtn.getElement().setAttribute("title",ADD_COURSE);
		removeCourseBtn.setVisible(false);
		getUiHandlers().deleteCourseOrStandard(collectionDo.getGooruOid(), courseCode);
		
		courseCode="";
	}
	
	@UiHandler("toggleArrowButtonPrimary")
	public void onClickHidePrimaryContainer(ClickEvent clickEvent){
		if(mainInfoPanel.isVisible())
		{
			mainInfoPanel.setVisible(false);
			toggleArrowButtonPrimary.addStyleName(res.css().primaryToggleArrowBottomrotateRight());
			
		}
		else
		{
			mainInfoPanel.setVisible(true);
			toggleArrowButtonPrimary.removeStyleName(res.css().primaryToggleArrowBottomrotateRight());
		}
	}
	
	@UiHandler("toggleArrowButtonSecondary")
	public void onClickHideSecondaryContainer(ClickEvent clickEvent){
		if(secondaryContentsContainer.isVisible())
		{
			secondaryContentsContainer.setVisible(false);
			toggleArrowButtonSecondary.addStyleName(res.css().primaryToggleArrowBottomrotateRight());
			
		}
		else
		{
			secondaryContentsContainer.setVisible(true);
			toggleArrowButtonSecondary.removeStyleName(res.css().primaryToggleArrowBottomrotateRight());
		}
	}
	/**
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("addStandardBtn")
	public void onAddStandardsClick(ClickEvent clickEvent) {
		addStandard(standardSgstBox.getText(), getCodeIdByCode(standardSgstBox.getText(), standardSearchDo.getSearchResults()));
	}

	private static String getCodeIdByCode(String code, List<CodeDo> codes) {
		if (codes != null) {
			for (CodeDo codeDo : codes) {
				if (code.equals(codeDo.getCode())) {
					return codeDo.getCodeId() + "";
				}
			}
		}
		return null;
	}
	private static String getCodeIdByStandardDo(String code, List<StandardFo> codes) {
		if (codes != null) {
			for (StandardFo codeDo : codes) {
				if (code.equals(codeDo.getLabel())) {
					return codeDo.getCodeId() + "";
				}
			}
		}
		return null;
	}

	/**
	 * Adding new standard for the collection , will check it has more than
	 * fifteen standards
	 * 
	 * @param standard
	 *            which to be added for the collection
	 */
	public void addStandard(String standard, String id) {
		if (standardsPanel.getWidgetCount() < 15) {
			if (standard != null && !standard.isEmpty()) {
				standardsPanel.add(createStandardLabel(standard, id, standardCodesMap.get(id)));
				this.resetStandardCount();
				getUiHandlers().updateStandard(collectionDo.getGooruOid(), id, "add");
			}
		} else {
			standardMaxShow();
			standardSgstBox.setText("");
			standardSgstBox.getElement().setAttribute("alt","");
			standardSgstBox.getElement().setAttribute("title","");
		}
	}

	/**
	 * new label is created for the standard which needs to be added
	 * 
	 * @param standardCode
	 *            update standard code
	 * @return instance of {@link DownToolTipWidgetUc}
	 */
	public DownToolTipWidgetUc createStandardLabel(final String standardCode, final String id, String description) {
		CloseLabel closeLabel = new CloseLabel(standardCode) {

			@Override
			public void onCloseLabelClick(ClickEvent event) {
				this.getParent().removeFromParent();
				resetCourseCount();
				getUiHandlers().deleteCourseOrStandard(collectionDo.getGooruOid(), id);
				resetStandardCount();				
			}
		};
		return new DownToolTipWidgetUc(closeLabel, description);
	}
	/**
	 * new label is created for the 21 century which needs to be added
	 * 
	 * @param standardCode
	 *            update standard code
	 * @return instance of {@link DownToolTipWidgetUc}
	 */
	public DownToolTipWidgetUc create21CenturyLabel(final String centuryCode, final String id, String description) {
		CloseLabelCentury closeLabel = new CloseLabelCentury(centuryCode) {
			@Override
			public void onCloseLabelClick(ClickEvent event) {
				this.getParent().removeFromParent();
				reset21CenturyCount();
				getUiHandlers().deleteCourseOrStandard(collectionDo.getGooruOid(), id);
				if(hilightSelectedValuesFromAutoSuggest.size()> 0){
					hilightSelectedValuesFromAutoSuggest.remove(Long.parseLong(id));
				}
			}
		};
		return new DownToolTipWidgetUc(closeLabel, description);
	}
	public DownToolTipWidgetUc createCourseLabel(final String standardCode, final String id, String description) {
		CloseLabel closeLabel = new CloseLabel(standardCode) {

			@Override
			public void onCloseLabelClick(ClickEvent event) {
				for(int i=0;i<courseDo.size();i++){
					if(courseDo.get(i).toString().equalsIgnoreCase(standardCode)){
						courseDo.remove(courseDo.get(i).toString());
						deleteCourse(collectionDo.getGooruOid(), id,"delete");	
					}
				}
				this.getParent().removeFromParent();
			}
		};
		return new DownToolTipWidgetUc(closeLabel, description);
	}
	
	public void deleteCourse(String collectionId, String courseCode, String action) {
		AppClientFactory.getInjector().getResourceService().deleteTaxonomyResource(collectionId, Integer.parseInt(courseCode), new SimpleAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			}
		});
	}

	@Override
	public void onPostCourseUpdate(CollectionDo collectionDo) {
		this.collectionDo = collectionDo;
	}
	@Override
	public void onPostStandardUpdate(CollectionDo collectionDo) {
		this.collectionDo = collectionDo;
		selectedValuesFromAutoSuggest.clear();
	}

	@Override
	public void closeAllOpenedPopUp() {
		if(alertContentUc!=null){
			alertContentUc.getAlertBox().hide();
		}
	}

	@Override
	public FlowPanel getStandardContainer() {
		return standardContainer;
	}

	@Override
	public void getUserStandardPrefCodeId(List<String> list) {
		if(list!=null){
		standardPreflist=new ArrayList<String>();
		for (String code : list) {
			standardPreflist.add(code);
			standardPreflist.add(code.substring(0, 2));
		 }
		}
	}

	public void OnStandardsClickEvent(Button standardsButtonClicked)
	{
		if(handlerRegistration!=null){
			handlerRegistration.removeHandler();
		}
		handlerRegistration=standardsButtonClicked.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().setUpdatedStandards();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.shelf.collection.tab.info.IsCollectionInfoTabView#OnCenturyClickEvent(com.google.gwt.user.client.ui.Button)
	 */
	public void OnCenturyClickEvent(Button centuryButtonClicked){
		if(handlerRegistration!=null){
			handlerRegistration.removeHandler();
		}
		handlerRegistration=centuryButtonClicked.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().setUpdatedCentury();
			}
		});
	}
	
	public void setUpdatedStandards(String standardsCode, Integer codeId,String descroption)
	{
		standardsPanel.add(createStandardLabel(standardsCode, codeId + "", descroption));
		this.resetStandardCount();
		getUiHandlers().updateStandard(collectionDo.getGooruOid(), codeId.toString(), "add");
		getUiHandlers().closeStandardsPopup();
	}
	
	private void updateGrade(List<String> gradeListInternal){
	AppClientFactory.getInjector().getResourceService().updateCollectionMetadata(collectionDo.getGooruOid(), null, null, join(gradeListInternal, ","), null, null, null,null,null,null, new SimpleAsyncCallback<CollectionDo>(){
			
			@Override
			public void onSuccess(CollectionDo result) {
				GradeUpdate.setVisible(false);
				gradeTopList.setVisible(true);
				gradeMiddleList.setVisible(true);
				gradeBottomList.setVisible(true);
				KinderGarten.setVisible(true);
				higherEducation.setVisible(true);
				collectionDo.setGrade(result.getGrade());
			}
		});
	}
	
	private String join(List<?> list,String separator){
		StringBuilder builder =null;
		if(list != null){
			builder = new StringBuilder();
			for(Object value:list){
				if(builder.length() > 0){
					builder.append(separator);
				}
				builder.append(value);
			}
		}
		return builder.toString();
	}

	@Override
	public Button getBrowseBtn() {
		return browseBtn;
	}
	/**
	 * 
	 * @function updateDepthOfKnowledge 
	 * 
	 * @created_date : 23-Jan-2015
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param collectionDo
	 * @parm(s) : @param depthOfKnowlwedgevalues
	 * @parm(s) : @param selectedVal
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private void updateDepthOfKnowledge(CollectionDo collectionDo, String depthOfKnowlwedgevalues, Boolean selectedVal ){
		AppClientFactory.getInjector().getResourceService().updateCollectionDepthOfKnowledge(collectionDo, depthOfKnowlwedgevalues, selectedVal, new SimpleAsyncCallback<CollectionDo>() {
			@Override
			public void onSuccess(CollectionDo result) {
				
			}
		});
	}
	
	/**
	 * @function updateLearningSkills 
	 * 
	 * @created_date : 23-Jan-2015
	 * 
	 * @description
	 * 
	 * @parm(s) : @param collectionDo
	 * @parm(s) : @param learningSkillsValues
	 * @parm(s) : @param selectedVal
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private void updateLearningSkills(CollectionDo collectionDo, String learningSkillsValues, Boolean selectedVal ){
		AppClientFactory.getInjector().getResourceService().updateCollectionLearningSkills(collectionDo, learningSkillsValues,selectedVal, new SimpleAsyncCallback<CollectionDo>() {
			@Override
			public void onSuccess(CollectionDo result) {
				
			}
		});
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.shelf.collection.tab.info.IsCollectionInfoTabView#setUpdatedCentury(java.util.Map)
	 */
	@Override
	public void setUpdatedCentury(Map<Long, String> selectedValues){
		centPanel.clear();
		int size=selectedValues.size();
		if(size>0){
			for(Iterator<Map.Entry<Long, String>> it = selectedValues.entrySet().iterator(); it.hasNext(); ) {
			      Map.Entry<Long, String> entry = it.next();
			      centPanel.add(create21CenturyLabel(entry.getValue(), entry.getKey()+"",""));
			      if(!hilightSelectedValuesFromAutoSuggest.containsKey(entry.getKey())) {
			    	  hilightSelectedValuesFromAutoSuggest.put(entry.getKey(), entry.getValue());
			      }else{
			    	  it.remove();
			      }
			}
		}
		this.reset21CenturyCount();
		getUiHandlers().updateCentury(collectionDo.getGooruOid(),"add",selectedValues);
		getUiHandlers().closeCenturyPopup();
	}
	@Override
	public Map<Long, String> getSelectedCenturyValuesThroughAutosuggest() {
		return hilightSelectedValuesFromAutoSuggest;
	}
}
