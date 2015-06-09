package org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.addquestion;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.search.CenturySkills.AddCenturyPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddAnswerChoice;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddAnswerImg;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddHintsView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddHotSpotQuestionAnswerChoice;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddQuestionAnswerChoice;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddQuestionImg;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.BlueButtonUc;
import org.ednovo.gooru.client.uc.CloseLabel;
import org.ednovo.gooru.client.uc.CloseLabelCentury;
import org.ednovo.gooru.client.uc.ConfirmationPopupVc;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.RemoveToolTipUc;
import org.ednovo.gooru.client.uc.StandardsPreferenceOrganizeToolTip;
import org.ednovo.gooru.client.uc.tooltip.BrowseStandardsTooltip;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.ui.TinyMCE;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.content.CollectionHTQuestionItemDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.CollectionQuestionItemDo;
import org.ednovo.gooru.shared.model.content.ProfanityCheckDo;
import org.ednovo.gooru.shared.model.content.QuestionAnswerDo;
import org.ednovo.gooru.shared.model.content.QuestionHintsDo;
import org.ednovo.gooru.shared.model.content.StandardFo;
import org.ednovo.gooru.shared.model.content.checkboxSelectedDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

public class QuestionTypeView extends BaseViewWithHandlers<QuestionTypeUiHandlers>
implements IsQuestionTypeView,SelectionHandler<SuggestOracle.Suggestion> {

	@UiField QuestionTypeCBundle res;

	private static QuestionTypeViewUiBinder uiBinder = GWT	.create(QuestionTypeViewUiBinder.class);

	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface QuestionTypeViewUiBinder extends UiBinder<Widget, QuestionTypeView> {
	}

	@UiField Label questionTypeHeader,questionTypeText,charLimitLbl,questionNameErrorLbl,setUpAdvancedLbl,advancedLbl,
	explanationLabel,charLimitExplanation,explainationErrorLbl,errorMessageForExplanation,errorMessageForHintsCheck,
	depthOfKnowledgeHeader,depthOfKnowledgeTitle,standardsDefaultText,standardMaxMsg,centuryDefaultText,rightsLbl,
	loadingTextLbl,ansChoiceErrMsg;
	@UiField Anchor addQuestionImg,addExplanationAnc,addHintsAnc,addDepthOfKnowledgeAnc,addStandardsAnc,addCenturyAnc;
	@UiField HTMLPanel questionText,addQuestImgContainer,questionHotSpotAnswerChoiceContainer,advancedContainer,hintsContainer,
	errorContainer,panelContentRights,rightsContent,buttonContainer,questionNameTextAreaContainer,explainationTextAreaContainer;
	TinyMCE questionNameTextArea,explainationTextArea;
	@UiField FlowPanel explanationContainer,depthOfKnowledgeContainer,standardContainer,standardsPanel,centuryContainer,
	centuryPanel;
	@UiField HTMLEventPanel eHearderIconExplanation,eHearderIconDepthOfKnowledge,eHearderIconStandards,eHearderIconCentury,
	lblContentRights,addQuestionResourceButton;
	@UiField Image depthOfKnoweldgeToolTip;
	@UiField CheckBox chkLevelRecall,chkLevelSkillConcept,chkLevelStrategicThinking,chkLevelExtendedThinking,rightsChkBox;
	@UiField Button browseStandards,browseCentury,cancelButton;
	@UiField InlineLabel agreeText,andText,additionalText,commuGuideLinesAnr, termsAndPolicyAnr,privacyAnr,copyRightAnr;
	@UiField(provided = true)
	AppSuggestBox standardSgstBox,centurySgstBox;
	@UiField BlueButtonUc addbutton;
	@UiField public static Label errorMessageForQuestion;

	private static final String MESSAGE_HEADER = i18n.GL0748();
	private static final String MESSAGE_CONTENT = i18n.GL0891();
	private static final String ERROR_MSG_ANSWER = i18n.GL0311();
	private static final String ERROR_MSG_ANSWER_LENGTH =i18n.GL0878();
	private static final String ERROR_MSG_ANSWER_SELECTED =i18n.GL0312(); 
	String[] anserChoiceNumArray=new String[]{"1","2","3","4","5"};

	private DeleteConfirmationPopupVc deleteConfirmationPopup;
	private CollectionItemDo collectionItemDo=null;
	RemoveToolTipUc removeToolTip=null; 
	ToolTip toolTip=null;
	private boolean isBrowseTooltip =false;
	BrowseStandardsTooltip browseStandardsTooltip;
	private boolean isBrowseStandardsToolTip = false;
	private static final String USER_META_ACTIVE_FLAG = "0";
	List<String> standardPreflist;
	private Map<String, String> standardCodesMap = new HashMap<String, String>();
	private Map<String, String> centuryCodesMap = new HashMap<String, String>();
	ArrayList<CodeDo> standardsDo=new ArrayList<CodeDo>();
	Set<CodeDo> deletedStandardsDo=new HashSet<CodeDo>();
	private AppMultiWordSuggestOracle standardSuggestOracle;
	private AppMultiWordSuggestOracle centurySuggestOracle;
	private SearchDo<CodeDo> standardSearchDo = new SearchDo<CodeDo>();
	private SearchDo<StandardFo> centurySearchDo = new SearchDo<StandardFo>();
	final StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
	String courseCode="";
	private static final String FLT_CODE_ID = "id";
	boolean isEditResource=false;

	PopupPanel centuryPopup=new PopupPanel();
	Map<Long, String> centurySelectedValues = new HashMap<Long, String>();
	AddCenturyPresenter centuryPresenterWidget=AppClientFactory.getInjector().getAddCenturyPresenterWidget();

	ArrayList<checkboxSelectedDo> depthOfKnowledges= new ArrayList<checkboxSelectedDo>();
	private HandlerRegistration handlerRegistration=null;

	boolean isAddBtnClicked=true,isRightsClicked=false,validationValue=false,isSaveButtonClicked=false;


	private static final int QUESTION_TEXT_LENGTH =500;
	private static final int EXPLAINATION_TEXT_LENGTH =500;
	private static final int ANSWER_CHOICE_HINTS_TEXT_LENGTH =150;
	private static final String ERROR_MSG_QUESTION_LENGTH =i18n.GL0880();
	private static final String ERROR_MSG_EXPLAINATION_LENGTH =i18n.GL0879();
	private static final String ERROR_MSG_QUESTION = i18n.GL0310();
	private static final String ERROR_MSG_HINTS_LENGTH = i18n.GL0877();
	private static final String ERROR_MSG_HINTS = i18n.GL2201();

	private String questionType="HS";
	private String hsType=i18n.GL3228_1();

	List<ProfanityCheckDo> profanityList,hintsListForProfanity;

	ArrayList<String> isValidHintsList = new ArrayList<String>();

	public QuestionTypeView() {
		initializeAutoSuggestedBox();
		setWidget(uiBinder.createAndBindUi(this));
		setHeaderAndBodyText("HS");
		questionTypeHeader.getElement().setId("lblQuestionTypeHeader");
		questionTypeText.getElement().setId("lblQuestionTypeText");
		questionText.getElement().setId("pnlQuestionText");
		questionText.getElement().setInnerHTML(" "+i18n.GL0863());
		questionText.getElement().setAttribute("alt", i18n.GL0863());
		questionText.getElement().setAttribute("title", i18n.GL0863());
		/*questionNameTextArea.getElement().setId("tinyMCEQuestionNameTextArea");
		questionNameTextArea.getElement().setAttribute("maxlength", "500");
		questionNameTextArea.markAsBlankPanel.setVisible(false);*/
		questionNameTextAreaContainer.getElement().setId("questionNameTextAreaContainer");
		explainationTextAreaContainer.getElement().setId("explainationTextAreaContainer");
		questionNameErrorLbl.getElement().setId("errlblQuestionNameErrorLbl");
		errorMessageForQuestion.getElement().setId("errlblErrorMessageForQuestion");
		addQuestionImg.setText(i18n.GL0860());
		addQuestionImg.getElement().setAttribute("alt", i18n.GL0860());
		addQuestionImg.getElement().setAttribute("title", i18n.GL0860());
		addQuestionImg.getElement().setId("lnkAddQuestionImg");
		addQuestImgContainer.getElement().setId("pnlAddQuestImgContainer");
		questionHotSpotAnswerChoiceContainer.getElement().setId("pnlQuestionHotSpotAnswerChoiceContainer");
		setTextForTheFields();
		setHotSpotAnswerFields();

		advancedContainer.getElement().setId("pnladvancedContainer");
		addHintsAnc.setText(i18n.GL3210_1() +i18n.GL_SPL_OPEN_SMALL_BRACKET()+5+i18n.GL3207_1()+i18n.GL_SPL_CLOSE_SMALL_BRACKET());
		addHintsAnc.getElement().setAttribute("alt", i18n.GL3210_1());
		addHintsAnc.getElement().setAttribute("title", i18n.GL3210_1());
		addExplanationAnc.setText( i18n.GL3208_1());
		addExplanationAnc.getElement().setAttribute("alt",  i18n.GL3208_1());
		addExplanationAnc.getElement().setAttribute("title", i18n.GL3208_1());
		addDepthOfKnowledgeAnc.setText(i18n.GL3209_1());
		addDepthOfKnowledgeAnc.getElement().setAttribute("alt", i18n.GL3209_1());
		addDepthOfKnowledgeAnc.getElement().setAttribute("title", i18n.GL3209_1());
		addStandardsAnc.setText(i18n.GL0575());
		addStandardsAnc.getElement().setAttribute("alt", i18n.GL0575());
		addStandardsAnc.getElement().setAttribute("title", i18n.GL0575());
		addCenturyAnc.setText(i18n.GL3199());
		addCenturyAnc.getElement().setAttribute("alt", i18n.GL3199());
		addCenturyAnc.getElement().setAttribute("title", i18n.GL3199());
		advancedLbl.setText(i18n.GL3096());
		advancedLbl.getElement().setAttribute("alt", i18n.GL3096());
		advancedLbl.getElement().setAttribute("title", i18n.GL3096());
		setUpAdvancedLbl.setText(i18n.GL3211_1());
		setUpAdvancedLbl.getElement().setAttribute("alt", i18n.GL3211_1());
		setUpAdvancedLbl.getElement().setAttribute("title", i18n.GL3211_1());


		/**
		 * Explanation
		 */
		explanationContainer.getElement().setId("fpnlExplanationContainer");
		explainationErrorLbl.getElement().setId("errlblExplainationErrorLbl");
		eHearderIconExplanation.getElement().setId("eHearderIconExplanation");
		eHearderIconExplanation.addClickHandler(new MinimizePanelsClickHandler());

		explanationContainer.getElement().setId("fpnlExplanationContainer");

		explanationLabel.setText(" "+i18n.GL0867());
		explanationLabel.getElement().setId("lblExplanationLabel");
		explanationLabel.getElement().setAttribute("alt", i18n.GL0867());
		explanationLabel.getElement().setAttribute("title", i18n.GL0867());
		explainationErrorLbl.getElement().setId("errlblExplainationErrorLbl");
		/**
		 * hints
		 */
		hintsContainer.getElement().setId("pnlHintsContainer");
		errorMessageForHintsCheck.getElement().setId("errlblErrorMessageForHintsCheck");

		/**
		 * Depth of Knowledge
		 */
		eHearderIconDepthOfKnowledge.getElement().setId("eHearderIconDepthOfKnowledge");
		eHearderIconDepthOfKnowledge.addClickHandler(new MinimizePanelsClickHandler());

		depthOfKnowledgeHeader.getElement().setId("lblDepthOfKnowledgeHeader");
		depthOfKnoweldgeToolTip.getElement().setId("imgDepthOfKnoweldgeToolTip");
		depthOfKnowledgeTitle.getElement().setId("lblDepthOfKnowledgeTitle");


		depthOfKnowledgeHeader.setText(i18n.GL1693());
		depthOfKnowledgeHeader.getElement().setAttribute("alt", i18n.GL1693());
		depthOfKnowledgeHeader.getElement().setAttribute("title", i18n.GL1693());

		chkLevelRecall.getElement().setId("chkChkLevelRecall");
		chkLevelSkillConcept.getElement().setId("chkChkLevelSkillConcept");
		chkLevelStrategicThinking.getElement().setId("chkChkLevelStrategicThinking");
		chkLevelExtendedThinking.getElement().setId("chkChkLevelExtendedThinking");

		chkLevelRecall.setText(i18n.GL1645());
		chkLevelRecall.getElement().setAttribute("alt", i18n.GL1645());
		chkLevelRecall.getElement().setAttribute("title", i18n.GL1645());
		chkLevelSkillConcept.setText(i18n.GL1646());
		chkLevelSkillConcept.getElement().setAttribute("alt", i18n.GL1646());
		chkLevelSkillConcept.getElement().setAttribute("title", i18n.GL1646());
		chkLevelStrategicThinking.setText(i18n.GL1647());
		chkLevelStrategicThinking.getElement().setAttribute("alt", i18n.GL1647());
		chkLevelStrategicThinking.getElement().setAttribute("title", i18n.GL1647());
		chkLevelExtendedThinking.setText(i18n.GL1648());
		chkLevelExtendedThinking.getElement().setAttribute("alt", i18n.GL1648());
		chkLevelExtendedThinking.getElement().setAttribute("title", i18n.GL1648());
		depthOfKnoweldgeToolTip.setUrl("images/mos/questionmark.png");
		depthOfKnoweldgeToolTip.setTitle("Question Mark");
		depthOfKnoweldgeToolTip.getElement().setAttribute("alt", "Question Mark");
		depthOfKnoweldgeToolTip.getElement().setAttribute("title", "Question Mark");

		/**
		 * Standards
		 */
		standardContainer.getElement().setId("fpnlStandardContainer");
		eHearderIconStandards.getElement().setId("eHearderIconStandards");
		eHearderIconStandards.addClickHandler(new MinimizePanelsClickHandler());
		standardsDefaultText.getElement().setId("lblStandardsDefaultText");
		standardSgstBox.getElement().setId("appSuggestBoxStandardSgstBox");
		standardSgstBox.getElement().setAttribute("style", "box-sizing:content-box;width:85%;height:19px");

		standardsDefaultText.setText(i18n.GL1682());
		standardsDefaultText.getElement().setAttribute("alt", i18n.GL1682());
		standardsDefaultText.getElement().setAttribute("title", i18n.GL1682());
		browseStandards.addClickHandler(new onBrowseStandardsClick());

		errorContainer.add(standardsPreferenceOrganizeToolTip);
		standardMaxMsg.getElement().setId("lblStandardMaxMsg");
		standardsPanel.getElement().setId("fpnlStandardsPanel");

		/**
		 * century
		 */
		eHearderIconCentury.getElement().setId("eHearderIconCentury");
		eHearderIconCentury.addClickHandler(new MinimizePanelsClickHandler());
		centuryDefaultText.setText(i18n.GL3199());
		centuryDefaultText.getElement().setAttribute("alt", i18n.GL3199());
		centuryDefaultText.getElement().setAttribute("title", i18n.GL3199());
		centurySgstBox.getElement().setAttribute("style", "box-sizing:content-box;width:85%;height:19px");

		setCenturyData();

		/**
		 * Terms Policy
		 */
		rightsChkBox.addClickHandler(new rightsChecked());
		rightsChkBox.getElement().setId("chkRights");
		rightsLbl.setText(i18n.GL0869());
		rightsLbl.getElement().setId("lblRightsLbl");
		rightsLbl.getElement().setAttribute("alt", i18n.GL0869());
		rightsLbl.getElement().setAttribute("title", i18n.GL0869());
		lblContentRights.getElement().setId("epnlLblContentRights");
		panelContentRights.getElement().setId("pnlPanelContentRights");
		rightsContent.getElement().setId("pnlRightsContent");
		agreeText.setText(i18n.GL0870());
		agreeText.getElement().setId("lblAgreeText");
		agreeText.getElement().setAttribute("alt", i18n.GL0870());
		agreeText.getElement().setAttribute("title", i18n.GL0870());
		commuGuideLinesAnr.setText(i18n.GL0871()+i18n.GL_GRR_COMMA());
		commuGuideLinesAnr.getElement().setId("lnkCommuGuideLinesAnr");
		commuGuideLinesAnr.getElement().setAttribute("alt", i18n.GL0871());
		commuGuideLinesAnr.getElement().setAttribute("title", i18n.GL0871());
		termsAndPolicyAnr.setText(i18n.GL0872()+i18n.GL_GRR_COMMA());
		termsAndPolicyAnr.getElement().setId("lnkTermsAndPolicyAnr");
		termsAndPolicyAnr.getElement().setAttribute("alt", i18n.GL0872());
		termsAndPolicyAnr.getElement().setAttribute("title", i18n.GL0872());
		privacyAnr.setText(" "+i18n.GL0873());
		privacyAnr.getElement().setId("lnkPrivacyAnr");
		privacyAnr.getElement().setAttribute("alt", i18n.GL0873());
		privacyAnr.getElement().setAttribute("title", i18n.GL0873());
		andText.setText(" "+i18n.GL_GRR_AND().trim()+" ");
		andText.getElement().setId("lblAndText");
		andText.getElement().setAttribute("alt", i18n.GL_GRR_AND());
		andText.getElement().setAttribute("title", i18n.GL_GRR_AND());
		copyRightAnr.setText(" "+i18n.GL0875().trim());
		copyRightAnr.getElement().setId("lnkCopyRightAnr");
		copyRightAnr.getElement().setAttribute("alt", i18n.GL0875());
		copyRightAnr.getElement().setAttribute("title", i18n.GL0875());
		additionalText.setText(i18n.GL0874());
		additionalText.getElement().setId("lblAdditionalText");
		additionalText.getElement().setAttribute("alt", i18n.GL0874());
		additionalText.getElement().setAttribute("title", i18n.GL0874());

		/**
		 * add
		 */
		buttonContainer.getElement().setId("pnlButtonContainer");
		addQuestionResourceButton.getElement().setId("epnlAddQuestionResourceButton");
		loadingTextLbl.setText(i18n.GL0591().toLowerCase());
		loadingTextLbl.getElement().setId("lblLoadingTextLbl");
		loadingTextLbl.getElement().setAttribute("alt", i18n.GL0591().toLowerCase());
		loadingTextLbl.getElement().setAttribute("title", i18n.GL0591().toLowerCase());
		loadingTextLbl.getElement().getStyle().setFontStyle(FontStyle.ITALIC);

		addbutton.setText(i18n.GL0590());
		addbutton.getElement().setAttribute("alt", i18n.GL0590());
		addbutton.getElement().setAttribute("title", i18n.GL0590());
		addbutton.getElement().setId("btnAdd");
		cancelButton.setText(i18n.GL0142());
		cancelButton.getElement().setAttribute("alt", i18n.GL0142());
		cancelButton.getElement().setAttribute("title", i18n.GL0142());
		cancelButton.getElement().setAttribute("style", "margin-left:10px;float: none;display: inline-block;");
		cancelButton.getElement().setId("btnCancel");

		setTextAndStyle();

		ansChoiceErrMsg.getElement().setId("lblAnsChoiceErrMsg");
	}


	public void setTextAndStyle(){
		/**
		 * Explanation
		 */
		explanationContainer.setVisible(false);
		addExplanationAnc.addStyleName("advancedOptionsTabs");
		addExplanationAnc.removeStyleName("advancedOptionsTabActive");
		explanationLabel.getElement().getStyle().setDisplay(Display.INLINE);

		/**
		 * Depth of Knowledge
		 */
		depthOfKnowledgeContainer.setVisible(false);
		addDepthOfKnowledgeAnc.addStyleName("advancedOptionsTabs");
		addDepthOfKnowledgeAnc.removeStyleName("advancedOptionsTabActive");

		/**
		 * Standards
		 */
		standardContainer.setVisible(false);
		addStandardsAnc.addStyleName("advancedOptionsTabs");
		addStandardsAnc.removeStyleName("advancedOptionsTabActive");
		errorContainer.setVisible(false);
		addStandardsAnc.setVisible(true);

		/**
		 * century
		 */
		centuryContainer.setVisible(false);
		addCenturyAnc.addStyleName("advancedOptionsTabs");
		addCenturyAnc.removeStyleName("advancedOptionsTabActive");
		addCenturyAnc.setVisible(true);

		/**
		 * Hints
		 */
		hintsContainer.setVisible(false);
		addHintsAnc.addStyleName("advancedOptionsTabs");
		addHintsAnc.removeStyleName("advancedOptionsTabActive");
		addHintsAnc.setText(i18n.GL3210_1() +i18n.GL_SPL_OPEN_SMALL_BRACKET()+5+i18n.GL3207_1()+i18n.GL_SPL_CLOSE_SMALL_BRACKET());

		/**
		 * Add
		 */
		loadingTextLbl.setVisible(false);
	}




	public void setTextForTheFields(){
		String value = StringUtil.generateMessage(i18n.GL2103(), "500");
		charLimitLbl.setText(value);
		StringUtil.setAttributes(charLimitLbl.getElement(), "charLimitLbl", value, value);

		charLimitExplanation.setText(value);
		StringUtil.setAttributes(charLimitExplanation.getElement(), "charLimitExplanation", value, value);

		addClickEventsForCheckBox();

		AppClientFactory.getInjector().getUserService().getUserProfileV2Details(AppClientFactory.getGooruUid(),USER_META_ACTIVE_FLAG,new SimpleAsyncCallback<ProfileDo>() {

			@Override
			public void onSuccess(ProfileDo profileObj) {
				if(profileObj.getUser().getMeta().getTaxonomyPreference().getCodeId()!=null){
					if(profileObj.getUser().getMeta().getTaxonomyPreference().getCodeId().size()==0){
						isBrowseTooltip = true;
						DisableStandars();
					}else
					{
						isBrowseTooltip = false;
						enableStandards();
						standardPreflist=new ArrayList<String>();
						for (String code : profileObj.getUser().getMeta().getTaxonomyPreference().getCode()) {
							standardPreflist.add(code);
							standardPreflist.add(code.substring(0, 2));
						}

					}
				}else{
					isBrowseTooltip = true;
					DisableStandars();
				}
			}

		});
	}


	public void setHeaderAndBodyText(String tabType){
		if(tabType.equals("HS")){
			questionTypeHeader.setText(i18n.GL3226_1());
			questionTypeHeader.getElement().setAttribute("alt", i18n.GL3226_1());
			questionTypeHeader.getElement().setAttribute("title", i18n.GL3226_1());
			questionTypeText.setText(i18n.GL0350());
			questionTypeText.getElement().setAttribute("alt", i18n.GL0350());
			questionTypeText.getElement().setAttribute("title", i18n.GL0350());
		}
	}


	@UiHandler("addQuestionImg")
	public void clickOnAddQuestImg(ClickEvent event){

		if(addQuestImgContainer.getElement().hasChildNodes()){
			addQuestImgContainer.setVisible(true);
			addQuestionImg.setVisible(false);
			setImageContainer();

		}else{
			uploadQuestionImage();
		}


	}

	public void setImageContainer(){

		if(addQuestImgContainer.getElement().hasChildNodes()){
			addQuestionImg.removeStyleName("ancTab");
			addQuestionImg.addStyleName("ancTabActive");
		}else{
			addQuestionImg.addStyleName("ancTab");
			addQuestionImg.removeStyleName("ancTabActive");
		}
	}

	public void uploadQuestionImage(){
		getUiHandlers().questionImageUpload();
	}


	public void uploadAnswerImage(){
		getUiHandlers().answerImageUpload();
	}


	@Override
	public void getRevealType() {
		getHideRightsToolTip();
	}
	
	public void clearTinyMce(){
		questionNameTextArea=new TinyMCE(500);
		explainationTextArea= new TinyMCE();
		questionNameTextAreaContainer.clear();
		explainationTextAreaContainer.clear();
		questionNameTextArea.setCharacterLimit(500);
		questionNameTextArea.getElement().setId("tinyMCEQuestionNameTextArea");
		questionNameTextArea.getElement().setAttribute("maxlength", "500");
		questionNameTextArea.markAsBlankPanel.setVisible(false);
		questionNameTextAreaContainer.add(questionNameTextArea);
		explainationTextAreaContainer.add(explainationTextArea);
	}


	@Override
	public void setImageUrl(String fileName,String fileNameWithoutRepository,boolean isQuestionImage,boolean isUserOwnResourceImage){

		double randNumber = Math.random();

		if(isQuestionImage){
			AddQuestionImg addQuestionImage = new AddQuestionImg();
			addQuestionImage.setQuestionImage(fileName+"?"+randNumber);
			addQuestionImage.setFileName(fileNameWithoutRepository);
			addQuestImgContainer.clear();
			addQuestionImg.getElement().getStyle().setDisplay(Display.NONE);
			addQuestImgContainer.add(addQuestionImage);
			setImageHandler();


			addQuestionImage.getChangeImgLbl().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(getQuestionEditMode()){
						getUiHandlers().questionImageUpload(collectionItemDo.getCollectionItemId());
					}else{
						getUiHandlers().questionImageUpload();
					}
				}
			});
			addQuestionImage.getRemoveImgLbl().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					boolean isQuestionEditMode=getQuestionEditMode();
					if(isQuestionEditMode){
						deleteConfirmationPopup=new DeleteConfirmationPopupVc(MESSAGE_HEADER,MESSAGE_CONTENT);
					}else{
						addQuestImgContainer.clear();
						addQuestionImg.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
					}
				}
			});
		}
		else{
		}
	}


	public void setImageHandler(){
		if(addQuestImgContainer.getElement().hasChildNodes()){
			AddQuestionImg addQuestionImage=(AddQuestionImg)addQuestImgContainer.getWidget(0);
			addQuestionImage.geteHearderIconImage().addClickHandler(new MinimizePanelsClickHandler());
		}
	}

	private class MinimizePanelsClickHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(event.getRelativeElement().getId().equalsIgnoreCase("eHearderIconImage")){
				addQuestImgContainer.setVisible(false);
				addQuestionImg.setVisible(true);
				setImageContainer();
			}else if(event.getRelativeElement().getId().equalsIgnoreCase("eHearderIconExplanation")){
				explanationContainer.setVisible(false);
				addExplanationAnc.setVisible(true);
				addExplanationAnc.setText(i18n.GL3208_1());
				setExplanationContainer();
			}else if(event.getRelativeElement().getId().equalsIgnoreCase("eHearderIconHint")){
				hintsContainer.setVisible(false);
				int widgetsCount=hintsContainer.getWidgetCount();
				for(int i=0;i<widgetsCount;){
					AddHintsView addHintsView =(AddHintsView) hintsContainer.getWidget(i);
					if(addHintsView.hintTextBox.getText().equalsIgnoreCase("")){
						addHintsView.removeFromParent();
						widgetsCount=hintsContainer.getWidgetCount();
						i--;
					}else{
						addHintsView.showHintsMessage(i+1);
						addHintsAnc.removeStyleName("advancedOptionsTabs");
						addHintsAnc.addStyleName("advancedOptionsTabActive");
					}

					i++;
				}
				int count=hintsContainer.getWidgetCount();
				addHintsAnc.setText(i18n.GL3210_1()+i18n.GL_SPL_OPEN_SMALL_BRACKET()+(5-count)+i18n.GL3207_1()+i18n.GL_SPL_CLOSE_SMALL_BRACKET());

			}else if(event.getRelativeElement().getId().equalsIgnoreCase("eHearderIconDepthOfKnowledge")){
				depthOfKnowledgeContainer.setVisible(false);
				addDepthOfKnowledgeAnc.setVisible(true);
				addDepthOfKnowledgeAnc.setText(i18n.GL3209_1());
				setDepthOfKnowledgeContainer();
			}else if(event.getRelativeElement().getId().equalsIgnoreCase("eHearderIconStandards")){
				standardContainer.setVisible(false);
				addStandardsAnc.setVisible(true);
				addStandardsAnc.setText(i18n.GL0575());
				setStandardsContainer();
			}else if(event.getRelativeElement().getId().equalsIgnoreCase("eHearderIconCentury")){
				centuryContainer.setVisible(false);
				addCenturyAnc.setVisible(true);
				addCenturyAnc.setText(i18n.GL3199());
				setCenturyContainer();
			}
		}
	}

	public boolean getQuestionEditMode(){
		return collectionItemDo!=null?true:false;
	}
	private class DeleteConfirmationPopupVc extends ConfirmationPopupVc{
		public DeleteConfirmationPopupVc(String messageHeader,String messageContent){
			super(messageHeader, messageContent);
			setPopupZindex(9999);
			setGlassZindex(9998);
			setScrollDisable();
		}
		@Override
		public void onDelete(ClickEvent clickEvent) {
			setImageContainer();
			getUiHandlers().removeQuestionImage(collectionItemDo.getCollectionItemId());
		}
		public void hide() {
			super.hide();

		}
	}

	public void setHotSpotAnswerFields(){
		questionHotSpotAnswerChoiceContainer.clear();
		final AddHotSpotQuestionAnswerChoice addQuestionAnswer=new AddHotSpotQuestionAnswerChoice(this);

		questionHotSpotAnswerChoiceContainer.add(addQuestionAnswer);
	}

	public void setExplanationContainer(){
		if(explainationTextArea.getText().isEmpty() || explainationTextArea.getText().trim().length()==0){
			addExplanationAnc.addStyleName("advancedOptionsTabs");
			addExplanationAnc.removeStyleName("advancedOptionsTabActive");
		}else{
			addExplanationAnc.removeStyleName("advancedOptionsTabs");
			addExplanationAnc.addStyleName("advancedOptionsTabActive");
		}

	}

	public void setHintsContainer(){
		if(hintsContainer.getWidgetCount()>0){
			addHintsAnc.removeStyleName("advancedOptionsTabs");
			addHintsAnc.addStyleName("advancedOptionsTabActive");
		}else{
			addHintsAnc.addStyleName("advancedOptionsTabs");
			addHintsAnc.removeStyleName("advancedOptionsTabActive");
		}
	}

	public void setDepthOfKnowledgeContainer(){

		boolean isSelected=false;
		if(chkLevelRecall.isChecked() || chkLevelSkillConcept.isChecked() || chkLevelStrategicThinking.isChecked() || chkLevelExtendedThinking.isChecked()){
			isSelected=true;
		}

		if(isSelected){
			addDepthOfKnowledgeAnc.removeStyleName("advancedOptionsTabs");
			addDepthOfKnowledgeAnc.addStyleName("advancedOptionsTabActive");
		}else{
			addDepthOfKnowledgeAnc.addStyleName("advancedOptionsTabs");
			addDepthOfKnowledgeAnc.removeStyleName("advancedOptionsTabActive");
		}

	}


	@UiHandler("addExplanationAnc")
	public void clickOnExplanationAnc(ClickEvent event){
		addExplanationAnc.setVisible(false);
		addExplanationAnc.addStyleName("advancedOptionsTab");
		addExplanationAnc.removeStyleName("advancedOptionsTabActive");
		explanationContainer.setVisible(true);
	}

	public void resetToHints(){
		int widgetsCount=hintsContainer.getWidgetCount();
		for(int i=0;i<widgetsCount;){
			AddHintsView addHintsView =(AddHintsView) hintsContainer.getWidget(i);
			if(i<0){			
				if(i==0){
					addHintsView.hintsTextLblVal.setText("");
				}else{
					addHintsView.hintsTextLblVal.setText("");
				}
				i++;
			}else{
				addHintsView.removeFromParent();
				widgetsCount=hintsContainer.getWidgetCount();
			}

		}
	}

	private void addHintsTextArea(final AddHintsView addHints){

		hintsContainer.add(addHints); 
		addHints.hintDelLbl.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) 
			{    
				addHints.removeFromParent();
				removeToolTip.hide();
				refreshHintNumber();
				isAddBtnClicked=true;
			}
		});

		addHints.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				addHints.hintDelLbl.getElement().getStyle().setDisplay(Display.BLOCK);

			}
		});
		addHints.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				addHints.hintDelLbl.getElement().getStyle().setDisplay(Display.NONE);
			}
		});
		showRemoveToolTip(addHints.hintDelLbl);

		if(hintsContainer.getWidgetCount()>=5){
			addHintsAnc.getElement().getStyle().setDisplay(Display.NONE);
		}

		addHints.eHearderIconHint.addClickHandler(new MinimizePanelsClickHandler());

	}



	protected void refreshHintNumber() 
	{
		int hintWidgetsCount=hintsContainer.getWidgetCount();
		for(int i=0 ; i<hintWidgetsCount;i++)
		{
			Widget childWidget=hintsContainer.getWidget(i);
			AddHintsView addHints=(AddHintsView)childWidget;
			addHints.hintNumLbl.setText(""+(i+1));
			if(i==0){
				addHints.eHearderIconHint.setVisible(true);
				addHints.hintsTextLblVal.setText(i18n.GL0859());
			}

		}
		if(hintsContainer.getWidgetCount()<5){
			addHintsAnc.addStyleName("advancedOptionsTabs");
			addHintsAnc.removeStyleName("advancedOptionsTabActive");
			addHintsAnc.setText(i18n.GL3210_1()+i18n.GL_SPL_OPEN_SMALL_BRACKET()+(5-hintWidgetsCount)+i18n.GL3207_1()+i18n.GL_SPL_CLOSE_SMALL_BRACKET());
			addHintsAnc.getElement().getStyle().setDisplay(Display.BLOCK);
		}

	}

	public void showRemoveToolTip(final Label deleteButton){
		deleteButton.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				removeToolTip=new RemoveToolTipUc();	                
				int left=event.getRelativeElement().getAbsoluteLeft()-16;
				int top=event.getRelativeElement().getAbsoluteTop()+27;
				removeToolTip.setPopupPosition(left, top);
				removeToolTip.getElement().getStyle().setZIndex(999);
				removeToolTip.show();
			}
		});
		deleteButton.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				removeToolTip.hide();
			}
		});
	}

	@UiHandler("addHintsAnc")
	public void clickOnHintsLabel(ClickEvent event){
		Window.enableScrolling(false);
		hintsContainer.setVisible(true);
		int widgetCount=hintsContainer.getWidgetCount();
		addHintsAnc.addStyleName("advancedOptionsTabs");
		addHintsAnc.removeStyleName("advancedOptionsTabActive");
		addHintsAnc.setText(i18n.GL3210_1()+i18n.GL_SPL_OPEN_SMALL_BRACKET()+(4-widgetCount)+i18n.GL3207_1()+i18n.GL_SPL_CLOSE_SMALL_BRACKET());
		final AddHintsView addHints = new AddHintsView(widgetCount+1);
		addHintsTextArea(addHints);
	}

	public void addClickEventsForCheckBox(){
		chkLevelRecall.addClickHandler(new AddCheckBoxClickHandler());
		chkLevelSkillConcept.addClickHandler(new AddCheckBoxClickHandler());
		chkLevelStrategicThinking.addClickHandler(new AddCheckBoxClickHandler());
		chkLevelExtendedThinking.addClickHandler(new AddCheckBoxClickHandler());
		depthOfKnoweldgeToolTip.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				toolTip = new ToolTip(i18n.GL1734());
				toolTip.getLblLink().setVisible(false);
				toolTip.getElement().getStyle().setBackgroundColor("transparent");
				toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
				toolTip.setPopupPosition(depthOfKnoweldgeToolTip.getAbsoluteLeft()-(50+22), depthOfKnoweldgeToolTip.getAbsoluteTop()+22);
				toolTip.getElement().getStyle().setZIndex(1111);
				toolTip.show();
			}
		});
		depthOfKnoweldgeToolTip.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {

				EventTarget target = event.getRelatedTarget();
				if (Element.is(target)) {
					if (!toolTip.getElement().isOrHasChild(Element.as(target))){
						toolTip.hide();
					}
				}
			}
		});
	}
	public class AddCheckBoxClickHandler implements ClickHandler  {

		@Override
		public void onClick(ClickEvent event) {
			CheckBox checkBox = (CheckBox) event.getSource();
			boolean checked = checkBox.getValue();		

			depthOfKnowledges.clear();

			if(chkLevelRecall.isChecked())
			{
				checkboxSelectedDo depthObj=new checkboxSelectedDo();
				depthObj.setSelected(true);
				depthObj.setValue(chkLevelRecall.getText());
				depthOfKnowledges.add(depthObj); 
			}
			else
			{
				checkboxSelectedDo depthObj=new checkboxSelectedDo();
				depthObj.setSelected(false);
				depthObj.setValue(chkLevelRecall.getText());
				depthOfKnowledges.add(depthObj);  
			}

			if(chkLevelSkillConcept.isChecked())
			{
				checkboxSelectedDo depthObj=new checkboxSelectedDo();
				depthObj.setSelected(true);
				depthObj.setValue(chkLevelSkillConcept.getText());
				depthOfKnowledges.add(depthObj); 
			}
			else
			{
				checkboxSelectedDo depthObj=new checkboxSelectedDo();
				depthObj.setSelected(false);
				depthObj.setValue(chkLevelSkillConcept.getText());
				depthOfKnowledges.add(depthObj);  
			}

			if(chkLevelStrategicThinking.isChecked())
			{
				checkboxSelectedDo depthObj=new checkboxSelectedDo();
				depthObj.setSelected(true);
				depthObj.setValue(chkLevelStrategicThinking.getText());
				depthOfKnowledges.add(depthObj); 
			}
			else
			{
				checkboxSelectedDo depthObj=new checkboxSelectedDo();
				depthObj.setSelected(false);
				depthObj.setValue(chkLevelStrategicThinking.getText());
				depthOfKnowledges.add(depthObj);  
			}

			if(chkLevelExtendedThinking.isChecked())
			{
				checkboxSelectedDo depthObj=new checkboxSelectedDo();
				depthObj.setSelected(true);
				depthObj.setValue(chkLevelExtendedThinking.getText());
				depthOfKnowledges.add(depthObj); 
			}
			else
			{
				checkboxSelectedDo depthObj=new checkboxSelectedDo();
				depthObj.setSelected(false);
				depthObj.setValue(chkLevelExtendedThinking.getText());
				depthOfKnowledges.add(depthObj);  
			}


		}
	}
	@UiHandler("addDepthOfKnowledgeAnc")
	public void clickOnaddDepthOfKnowledgeAnc(ClickEvent event){
		addDepthOfKnowledgeAnc.setVisible(false);
		addDepthOfKnowledgeAnc.addStyleName("advancedOptionsTabs");
		addDepthOfKnowledgeAnc.removeStyleName("advancedOptionsTabActive");
		depthOfKnowledgeContainer.setVisible(true);
	}


	public void setStandardsContainer(){
		if(standardsPanel.getWidgetCount()>0){
			addStandardsAnc.removeStyleName("advancedOptionsTabs");
			addStandardsAnc.addStyleName("advancedOptionsTabActive");
		}else{
			addStandardsAnc.addStyleName("advancedOptionsTabs");
			addStandardsAnc.removeStyleName("advancedOptionsTabActive");
		}
	}


	public void initializeAutoSuggestedBox(){
		standardSuggestOracle = new AppMultiWordSuggestOracle(true);
		centurySuggestOracle= new AppMultiWordSuggestOracle(true);
		standardSearchDo.setPageSize(10);
		standardSgstBox = new AppSuggestBox(standardSuggestOracle) {
			@Override
			public void keyAction(String text,KeyUpEvent event) {
				text=text.toUpperCase();
				errorContainer.setVisible(false);
				standardSearchDo.setSearchResults(null);
				boolean standardsPrefDisplayPopup = false;
				standardSgstBox.hideSuggestionList();
				if(!courseCode.isEmpty()) {
					Map<String,String> filters = new HashMap<String, String>();
					filters.put(FLT_CODE_ID,courseCode);
					standardSearchDo.setFilters(filters);
				}
				standardSearchDo.setQuery(text);
				if (text != null && text.trim().length() > 0) {
					errorContainer.setVisible(false);
					if(standardPreflist!=null){
						for(int count=0; count<standardPreflist.size();count++) {
							if(text.contains(standardPreflist.get(count))) {
								standardsPrefDisplayPopup = true;
								break;
							} else {
								standardsPrefDisplayPopup = false;
							}
						}						
					}
					if(standardsPrefDisplayPopup){
						errorContainer.setVisible(false);
						AppClientFactory.getInjector().getSearchService().getSuggestStandardByFilterCourseId(standardSearchDo, new SimpleAsyncCallback<SearchDo<CodeDo>>() {
							@Override
							public void onSuccess(SearchDo<CodeDo> result) {
								setStandardSuggestions(result);

							}
						});

						standardSgstBox.showSuggestionList();
					}
					else{
						errorContainer.setVisible(true);
						standardSgstBox.hideSuggestionList();
						standardSuggestOracle.clear();
					}
				}
			}
			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}
		};
		standardSgstBox.addSelectionHandler(this);
		BlurHandler blurHandler=new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if(standardsPreferenceOrganizeToolTip.isShowing()){
					errorContainer.setVisible(false);
				}
			}
		};
		standardSgstBox.addDomHandler(blurHandler, BlurEvent.getType());
		centurySgstBox=new AppSuggestBox(centurySuggestOracle) {

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}

			@Override
			public void keyAction(String text, KeyUpEvent event) {
				text=text.toUpperCase();
				centurySearchDo.setSearchResults(null);
				centurySearchDo.setQuery(text);
				if (text != null && text.trim().length() > 0) {
					AppClientFactory.getInjector().getSearchService().getSuggestCenturyByQuery(centurySearchDo, new AsyncCallback<SearchDo<StandardFo>>() {

						@Override
						public void onSuccess(SearchDo<StandardFo> result) {
							setCenturySuggestions(result);

						}

						@Override
						public void onFailure(Throwable caught) {

						}							
					});
					centurySgstBox.showSuggestionList();
				}
			}
		};
		centurySgstBox.getElement().getStyle().setFontSize(12, Unit.PX);
		centurySgstBox.getTextBox().getElement().setAttribute("placeholder", i18n.GL3122_1());

		BlurHandler blurHandlerCentury=new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if(standardsPreferenceOrganizeToolTip.isShowing()){
					errorContainer.setVisible(false);
				}
			}
		};

		centurySgstBox.addDomHandler(blurHandlerCentury, BlurEvent.getType());
		centurySgstBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				addCentury(centurySgstBox.getValue(), getCodeIdByCodeCentury(centurySgstBox.getValue(), centurySearchDo.getSearchResults()));
				centurySgstBox.setText("");
				centurySuggestOracle.clear();
			}
		});
	}

	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		addStandard(standardSgstBox.getValue(), getCodeIdByCode(standardSgstBox.getValue(), standardSearchDo.getSearchResults()));
		standardSgstBox.setText("");
		standardSuggestOracle.clear();
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
	private static String getCodeIdByCodeCentury(String code, List<StandardFo> codes) {
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
		if (standardsPanel.getWidgetCount() <5) {
			if (standard != null && !standard.isEmpty()) {
				CodeDo codeObj=new CodeDo();
				codeObj.setCodeId(Integer.parseInt(id));
				codeObj.setCode(standard);
				standardsDo.add(codeObj);
				standardsPanel.add(createStandardLabel(standard, id, standardCodesMap.get(id)));
			}
		} else {
			standardMaxShow();
			standardSgstBox.setText("");
		}
	}
	public void addCentury(String centuryTag, String id) {
		if (centuryTag != null && !centuryTag.isEmpty()) {
			String codeIdVal = getCodeIdByCodeCentury(centurySgstBox.getValue(), centurySearchDo.getSearchResults());				
			CodeDo codeObjStandard=new CodeDo();
			codeObjStandard.setCodeId(Integer.parseInt(codeIdVal));
			codeObjStandard.setCode(centurySgstBox.getValue());
			standardsDo.add(codeObjStandard);

			centurySelectedValues.put(Long.parseLong(codeIdVal),centurySgstBox.getValue());
			centuryPanel.add(create21CenturyLabel(centuryTag, id, centuryCodesMap.get(id)));
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
				for(final CodeDo codeObj:standardsDo){
					if(isEditResource){
						if(codeObj.getCodeId()==Integer.parseInt(id)){
							AppClientFactory.getInjector().getResourceService().deleteTaxonomyResource(collectionItemDo.getResource().getGooruOid(), codeObj.getCodeId(), new SimpleAsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									CodeDo deletedObj=new CodeDo();
									deletedObj.setCodeId(codeObj.getCodeId());
									deletedStandardsDo.add(deletedObj);
									standardsDo.remove(codeObj);
								}
							});
						}
					}else
					{
						if(codeObj.getCodeId()==Integer.parseInt(id)){
							standardsDo.remove(codeObj);
						}
					}
				}
				this.getParent().removeFromParent();
			}
		};
		return new DownToolTipWidgetUc(closeLabel, description);
	}
	public void standardMaxShow() {
		standardSgstBox.addStyleName("standardTxtBox");
		standardMaxMsg.setStyleName("standardMax");
		standardsPanel.addStyleName("floatLeftNeeded");
		new FadeInAndOut(standardMaxMsg.getElement(), 5000, 5000);
	}
	public void setStandardSuggestions(SearchDo<CodeDo> standardSearchDo) {
		standardSuggestOracle.clear();
		this.standardSearchDo = standardSearchDo;
		if (this.standardSearchDo.getSearchResults() != null) {
			List<String> sources = getAddedStandards(standardsPanel);
			for (CodeDo code : standardSearchDo.getSearchResults()) {
				if (!sources.contains(code.getCode())) {
					standardSuggestOracle.add(code.getCode());
				}
				standardCodesMap.put(code.getCodeId() + "", code.getLabel());
			}
		}
		standardSgstBox.showSuggestionList();
	}
	public void setCenturySuggestions(SearchDo<StandardFo> centurySearchDo) {
		centurySuggestOracle.clear();
		this.centurySearchDo = centurySearchDo;
		if (this.centurySearchDo.getSearchResults() != null) {
			List<String> sources = getAddedCentury(centuryPanel);
			for (StandardFo code : centurySearchDo.getSearchResults()) {
				if (!sources.contains(code.getLabel())) {
					centurySuggestOracle.add(code.getLabel());
				}
				centuryCodesMap.put(code.getCodeId() + "", code.getLabel());
			}
		}
		centurySgstBox.showSuggestionList();
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
	 * get the standards are added for collection
	 * 
	 * @param flowPanel
	 *            having all added standards label
	 * @return standards text in list which are added for the collection
	 */
	private List<String> getAddedCentury(FlowPanel flowPanel) {
		List<String> suggestions = new ArrayList<String>();
		for (Widget widget : flowPanel) {
			if (widget instanceof DownToolTipWidgetUc) {
				suggestions.add(((CloseLabelCentury) ((DownToolTipWidgetUc) widget).getWidget()).getSourceText());
			}
		}
		return suggestions;
	}

	private class onBrowseStandardsClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			callBrowseStandards();
		}
	}

	public void callBrowseStandards() {
		getUiHandlers().browseStandardsInfo(true, false);
	}

	@Override
	public void OnBrowseStandardsClickEvent(Button addStandardsBtn) {
		if(handlerRegistration!=null){
			handlerRegistration.removeHandler();
		}
		handlerRegistration=addStandardsBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().addUpdatedBrowseStandards();
			}
		});
	}

	@Override
	public void setUpdatedStandardsCode(String standardsCodeVal,int id,String desc) {
		if (standardsPanel.getWidgetCount() <5) {
			if (standardsCodeVal != null && !standardsCodeVal.isEmpty()) {
				CodeDo codeObj=new CodeDo();
				codeObj.setCodeId(id);
				codeObj.setCode(standardsCodeVal);
				standardsDo.add(codeObj);
				standardsPanel.add(createStandardLabel(standardsCodeVal, Integer.toString(id), desc));
			}
		} else {
			standardMaxShow();
		}
		closeStandardsPopup();
	}

	public void closeStandardsPopup(){
		getUiHandlers().closeStandardsPopup();
	}
	public void DisableStandars(){
		browseStandardsTooltip=new BrowseStandardsTooltip("To see all standards, please edit your standards preference in","settings");
		browseStandards.getElement().getStyle().setColor("#999");
		browseStandards.getElement().addClassName("disabled");
		browseStandards.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(isBrowseTooltip == true){
					browseStandardsTooltip.show();
					browseStandardsTooltip.setPopupPosition(browseStandards.getAbsoluteLeft()+3, browseStandards.getAbsoluteTop()+33);
					browseStandardsTooltip.getElement().getStyle().setZIndex(999999);
					isBrowseStandardsToolTip= true;
				}
			}
		});

		Event.addNativePreviewHandler(new NativePreviewHandler() {
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				hideBrowseStandardsPopup(event);
			}
		});
	}

	public void hideBrowseStandardsPopup(NativePreviewEvent event){
		try{
			if(event.getTypeInt()==Event.ONMOUSEOVER){
				Event nativeEvent = Event.as(event.getNativeEvent());
				boolean target=eventTargetsPopup(nativeEvent);
				if(!target)
				{
					if(isBrowseStandardsToolTip){
						browseStandardsTooltip.hide();
					}
				}
			}
		}catch(Exception ex){}
	}
	private boolean eventTargetsPopup(NativeEvent event) {
		EventTarget target = event.getEventTarget();
		if (Element.is(target)) {
			try{
				return browseStandardsTooltip.getElement().isOrHasChild(Element.as(target));
			}catch(Exception ex){}
		}
		return false;
	}

	public void enableStandards(){
		browseStandards.getElement().getStyle().clearColor();
		browseStandards.getElement().removeClassName("disabled");
	}

	@UiHandler("addStandardsAnc")
	public void clickOnaddStandardsLabel(ClickEvent event){
		addStandardsAnc.setVisible(false);
		addStandardsAnc.addStyleName("advancedOptionsTabs");
		addStandardsAnc.removeStyleName("advancedOptionsTabActive");
		standardContainer.setVisible(true);
	}

	public void setCenturyContainer(){

		if(centuryPanel.getWidgetCount()>0){
			addCenturyAnc.removeStyleName("advancedOptionsTabs");
			addCenturyAnc.addStyleName("advancedOptionsTabActive");
		}else{
			addCenturyAnc.addStyleName("advancedOptionsTabs");
			addCenturyAnc.removeStyleName("advancedOptionsTabActive");
		}
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
				for(final CodeDo codeObj:standardsDo){
					if(id.equalsIgnoreCase(String.valueOf(codeObj.getCodeId()))){
						AppClientFactory.getInjector().getResourceService().deleteTaxonomyResource(collectionItemDo.getResource().getGooruOid(), codeObj.getCodeId(), new SimpleAsyncCallback<Void>() {
							@Override
							public void onSuccess(Void result) {
								CodeDo deletedObj=new CodeDo();
								deletedObj.setCodeId(codeObj.getCodeId());
								deletedStandardsDo.add(deletedObj);
								standardsDo.remove(codeObj);	
								centurySelectedValues.remove(Long.parseLong(id));
							}
						});
						this.getParent().removeFromParent();
						return;
					}
				}
			}
		};
		return new DownToolTipWidgetUc(closeLabel, description);
	}

	/**
	 * This will handle the click event on the browser century
	 * @param e
	 */
	@UiHandler("browseCentury")
	public void onClickOfBrowseCentury(ClickEvent e){
		centuryPopup.clear();
		centuryPresenterWidget.setAddResourceData(centurySelectedValues);
		centuryPopup.add(centuryPresenterWidget.getWidget());
		centuryPopup.show();
		centuryPopup.center();
		centuryPopup.getElement().getStyle().setZIndex(999999);
	}

	public void setCenturyData(){
		//This will hide the popup when clicked on the cancel button
		centuryPresenterWidget.getCancelBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hideCenturyPopup();
			}
		});
		//This will hide the popup when clicked on close button
		centuryPresenterWidget.getCloseBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hideCenturyPopup();
			}
		});
		centuryPresenterWidget.getAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				centurySelectedValues.clear();
				centurySelectedValues.putAll(centuryPresenterWidget.getSelectedValues());
				centuryPanel.clear();
				if(centurySelectedValues!=null && centurySelectedValues.size()>0){
					for (Map.Entry<Long, String> entry : centurySelectedValues.entrySet()){
						CodeDo codeObjStandard=new CodeDo();
						codeObjStandard.setCodeId(Integer.parseInt(entry.getKey()+""));
						codeObjStandard.setCode(entry.getValue());
						standardsDo.add(codeObjStandard);
						centuryPanel.add(create21CenturyLabel(entry.getValue(),entry.getKey()+"",""));
					}
				}
				hideCenturyPopup();
			}
		});
	}
	/**
	 * This method will hide the century popup
	 */
	public void hideCenturyPopup(){
		centuryPopup.hide();
	}
	@UiHandler("addCenturyAnc")
	public void clickOnaddCenturyLabel(ClickEvent event){
		addCenturyAnc.setVisible(false);
		addCenturyAnc.addStyleName("advancedOptionsTabs");
		addCenturyAnc.removeStyleName("advancedOptionsTabActive");
		centuryContainer.setVisible(true);
	}

	private class rightsChecked implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if(rightsChkBox.getValue()){
				rightsLbl.getElement().getStyle().setColor("black");
			}
			else{
				rightsLbl.getElement().getStyle().setColor("orange");
			}

		}
	}

	/**
	 * On mouse over panelContentRights will be visible.
	 * 
	 * @param event instance of {@link MouseOverEvent}
	 */
	@UiHandler("lblContentRights")
	public void onMouseOver(MouseOverEvent event){
		panelContentRights.setVisible(true);
	}

	/**
	 * On mouse out panelContentRights will not be visible.
	 * 
	 * @param event instance of {@link MouseOutEvent}
	 */

	@UiHandler("lblContentRights")
	public void onMouseOut(MouseOutEvent event){
		panelContentRights.setVisible(false);
	}
	public void getHideRightsToolTip()
	{
		panelContentRights.setVisible(false);
	}


	/**
	 * add functionality
	 */

	@UiHandler("addQuestionResourceButton")
	public void clickedOnAddQuestionButton(ClickEvent event)
	{
		boolean fieldValidationCheck;
		if(questionType.equalsIgnoreCase("HS")){
			if (isHotSpotAnswerChoiceEmpty(questionHotSpotAnswerChoiceContainer)) {
				fieldValidationCheck = false;
				isAddBtnClicked=true;
			}
			if (!isHotSpotAnswerChoiceSelected(questionHotSpotAnswerChoiceContainer)) {
				String errorMessage=ERROR_MSG_ANSWER_SELECTED;
				showErrorMessageForAnswer(errorMessage);
				fieldValidationCheck = false;
				isAddBtnClicked=true;
			}
		}


		if(isAddBtnClicked){
			isRightsClicked=rightsChkBox.getValue();
			isAddBtnClicked=false;
			final Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", questionNameTextArea.getText());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean value) {
					if(value){
						SetStyleForProfanity.SetStyleForProfanityForTinyMCE(questionNameTextArea, questionNameErrorLbl, value);
						isAddBtnClicked=true;
					}else{
						SetStyleForProfanity.SetStyleForProfanityForTinyMCE(questionNameTextArea, questionNameErrorLbl, value);
						parms.put("text", explainationTextArea.getText());
						AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

							@Override
							public void onSuccess(Boolean value) {
								if(value){
									SetStyleForProfanity.SetStyleForProfanityForTinyMCE(explainationTextArea, explainationErrorLbl, value);
									isAddBtnClicked=true;
								}else{

									MixpanelUtil.mixpanelEvent("Collaborator_edits_collection");

									SetStyleForProfanity.SetStyleForProfanityForTinyMCE(explainationTextArea, explainationErrorLbl, value);
									String mediaFileName=null;

									boolean isQuestEnteredFlag = true;
									boolean fieldValidationStaus=true;
									if(addQuestImgContainer.getElement().hasChildNodes()){
										AddQuestionImg addQuestionImage=(AddQuestionImg)addQuestImgContainer.getWidget(0);
										mediaFileName=addQuestionImage.getFileName();
									}
									clearErrorQuestionMessage();
									if(questionNameTextArea.getText()==null||questionNameTextArea.getText().trim().equals("")){
										showErrorQuestionMessage(ERROR_MSG_QUESTION);
										fieldValidationStaus=false;
										isQuestEnteredFlag=false;
										isAddBtnClicked=true;
									}
									//This regex is used to get text count with out html tags
									String questionNameText = questionNameTextArea.getText().replaceAll("\\<.*?>","");
									if(questionNameText.length()>QUESTION_TEXT_LENGTH){
										showErrorQuestionMessage(ERROR_MSG_QUESTION_LENGTH);
										fieldValidationStaus=false;
										isAddBtnClicked=true;
									}
									errorMessageForExplanation.setText("");
									if(questionNameText.length()>QUESTION_TEXT_LENGTH){
										showErrorQuestionMessage(ERROR_MSG_QUESTION_LENGTH);
										fieldValidationStaus=false;
										isAddBtnClicked=true;
									}
									String explainationText = explainationTextArea.getText().replaceAll("\\<.*?>","");

									if(explainationText.trim().length() > EXPLAINATION_TEXT_LENGTH){
										errorMessageForExplanation.setText(ERROR_MSG_EXPLAINATION_LENGTH);
										fieldValidationStaus=false;
										isAddBtnClicked=true;
									}


									else if(fieldValidationStaus && questionType.equalsIgnoreCase("HS")){
										clearErrorQuestionMessage();

										if (isHotSpotAnswerChoiceEmpty(questionHotSpotAnswerChoiceContainer)) {
											fieldValidationStaus = false;
											isAddBtnClicked=true;
										}else{

											if(!isHintsAdded(hintsContainer)){

												if (!isHotSpotAnswerChoiceSelected(questionHotSpotAnswerChoiceContainer)) {
													String errorMessage=ERROR_MSG_ANSWER_SELECTED;
													showErrorMessageForAnswer(errorMessage);
													fieldValidationStaus = false;
													isAddBtnClicked=true;
												}else{
													isProfanityCheckForAnswerChoice(fieldValidationStaus,mediaFileName);
												}

											}else{
												profanityCheckForHints(fieldValidationStaus,mediaFileName);

											}
										}
									}
								}
							}
						});
					}
				}
			});
		}
	}


	void clearErrorQuestionMessage(){
		errorMessageForQuestion.setText("");
		StringUtil.setAttributes(errorMessageForQuestion.getElement(), "errlblErrorMessageForQuestion", null, null);

		questionNameTextArea.getElement().removeClassName("errorBorderMessage");
	}

	void showErrorQuestionMessage(String errMessage){

		errorMessageForQuestion.setText(errMessage);
		StringUtil.setAttributes(errorMessageForQuestion.getElement(), "errlblErrorMessageForQuestion", errMessage, errMessage);

		questionNameTextArea.getElement().addClassName("errorBorderMessage");
	}

	public boolean isHintsAdded(HTMLPanel hintsContainer){
		boolean hintsAdded=false;
		hintsListForProfanity=new ArrayList<ProfanityCheckDo>();
		if(hintsContainer.getWidgetCount()>=1){
			for(int i=0;i<hintsContainer.getWidgetCount();i++){
				final AddHintsView addHints = (AddHintsView) hintsContainer.getWidget(i);
				ProfanityCheckDo profanitymodel=new ProfanityCheckDo();
				profanitymodel.setQuestionID(Integer.toString(i));

				String hintText=addHints.hintTextBox.getContent().toString().trim().replaceAll("&nbsp;", " ");
				hintText=hintText.replaceAll("\\<.*?>","");
				if(hintText!=null && !hintText.trim().equals("")){
					String hintsText=addHints.hintTextBox.getContent().replaceAll("\\<.*?>","");	
					if(hintsText.trim().length()>ANSWER_CHOICE_HINTS_TEXT_LENGTH){
						Document.get().getElementById(addHints.hintTextBox.getID()+"_message").setInnerText("");
						addHints.errorMessageforHints.setText(ERROR_MSG_HINTS_LENGTH);
						hintsAdded=true;
						isAddBtnClicked=true;
					}else{
						hintsAdded=false;
						isAddBtnClicked=true;
						addHints.errorMessageforHints.setText("");
						profanitymodel.setQuestionText(addHints.hintTextBox.getContent());
					}

				}else{
					addHints.errorMessageforHints.setText(ERROR_MSG_HINTS);
					hintsAdded=true;
					isAddBtnClicked=true;
				}
				hintsListForProfanity.add(profanitymodel);
			}
		}
		return hintsAdded;
	}

	public boolean profanityCheckForHints(final boolean fieldValidationStaus,final String mediaFileName){
		validationValue=false;
		AppClientFactory.getInjector().getResourceService().checkProfanityForList(hintsListForProfanity, new SimpleAsyncCallback<List<ProfanityCheckDo>>() {

			@Override
			public void onSuccess(List<ProfanityCheckDo> result) {
				for(int i=0;i<hintsContainer.getWidgetCount();i++){
					final AddHintsView addHints = (AddHintsView) hintsContainer.getWidget(i);
					addHints.errorMessageforHints.setText("");
					SetStyleForProfanity.SetStyleForProfanityForTinyMCE(addHints.hintTextBox, addHints.errorMessageforHints, result.get(i).questionValue);
					if(result.get(i).questionValue==true){
						addHints.errorMessageforHints.getElement().setAttribute("style", "float: left;left: 24px;");
						validationValue=true;
						isAddBtnClicked=true;
					}
				}
				if(validationValue){
					return;
				}else{
					if(!isRightsClicked){
						rightsLbl.getElement().getStyle().setColor("orange");
						isAddBtnClicked=true;
					}else{
						if(fieldValidationStaus){
							addFunctionality(!validationValue,mediaFileName);
						}
					}
				}
			}
		});
		return validationValue;
	}


	public boolean isHotSpotAnswerChoiceEmpty(HTMLPanel questionHotSpotAnswerChoiceContainer){
		profanityList=new ArrayList<ProfanityCheckDo>();
		String answerChoiceValue=null;
		boolean selectedAnswerImage = false;
		final AddHotSpotQuestionAnswerChoice addHotSpotQuestionAnswerChoice=(AddHotSpotQuestionAnswerChoice)questionHotSpotAnswerChoiceContainer.getWidget(0);

		addHotSpotQuestionAnswerChoice.errorMessageforAnswerChoice.setText("");
		addHotSpotQuestionAnswerChoice.ansImageContainer.getElement().removeClassName("errorBorderMessage");
		addHotSpotQuestionAnswerChoice.textAnsContainer.getElement().removeClassName("errorBorderMessage");

		if(addHotSpotQuestionAnswerChoice.imageRDButton.isChecked()){
			int count=addHotSpotQuestionAnswerChoice.ansImageContainer.getWidgetCount();
			if(count>1){
				selectedAnswerImage=false;
			}else{
				selectedAnswerImage=true;
				addHotSpotQuestionAnswerChoice.errorMessageforAnswerChoice.setText(ERROR_MSG_ANSWER);
				addHotSpotQuestionAnswerChoice.ansImageContainer.getElement().addClassName("errorBorderMessage");
			}
		}else if(addHotSpotQuestionAnswerChoice.textRDButton.isChecked()){
			int widgetCount=addHotSpotQuestionAnswerChoice.textAnsContainer.getWidgetCount();
			for(int i=0;i<widgetCount;i++){
				AddAnswerChoice addAnswerChoice=(AddAnswerChoice) addHotSpotQuestionAnswerChoice.textAnsContainer.getWidget(i);
				answerChoiceValue=addAnswerChoice.answerTextBox.getContent().replaceAll("\\<.*?>","");

				ProfanityCheckDo profanitymodel=new ProfanityCheckDo();
				if(answerChoiceValue==null||answerChoiceValue.trim().equalsIgnoreCase("")){
					selectedAnswerImage=true;
					addHotSpotQuestionAnswerChoice.errorMessageforAnswerChoice.setText(ERROR_MSG_ANSWER);
					addAnswerChoice.answerTextBox.getElement().addClassName("errorBorderMessage");
					profanitymodel.setQuestionID(Integer.toString(i));
					profanityList.add(profanitymodel);
				}else{
					if(answerChoiceValue.trim().length()>ANSWER_CHOICE_HINTS_TEXT_LENGTH){
						selectedAnswerImage=true;
						Document.get().getElementById(addAnswerChoice.answerTextBox.getID()+"_message").setInnerText("");
						addHotSpotQuestionAnswerChoice.errorMessageforAnswerChoice.setText(ERROR_MSG_ANSWER_LENGTH);
						addAnswerChoice.answerTextBox.getElement().addClassName("errorBorderMessage");
					}else{
						selectedAnswerImage=false;
						profanitymodel.setQuestionID(Integer.toString(i));
						profanitymodel.setQuestionText(answerChoiceValue);
						profanityList.add(profanitymodel);
					}
				}

			}
		}
		return selectedAnswerImage;
	}



	/**
	 * If all validations successful, question is added to the collection.
	 */
	public void addFunctionality(boolean fieldValidationStaus,String mediaFileName){
		if(fieldValidationStaus){
			buttonContainer.getElement().getStyle().setDisplay(Display.NONE);
			loadingTextLbl.setVisible(true);
			CollectionQuestionItemDo collectionQuestionItemDo = new CollectionQuestionItemDo();
			String questionText = questionNameTextArea.getRawContent().replace("&nbsp;","").trim();
			String explaination = explainationTextArea.getText();
			if(explaination!=null&&!explaination.trim().equals("")){
				explaination=explainationTextArea.getRawContent().trim();
			}

			ArrayList<QuestionAnswerDo> enteredAnswers = new ArrayList<QuestionAnswerDo>(); 
			ArrayList<QuestionHintsDo> enteredHints = new ArrayList<QuestionHintsDo>();
			HashMap<String,ArrayList<QuestionAnswerDo>> answerMap = new HashMap<String,ArrayList<QuestionAnswerDo>>();
			HashMap<String,ArrayList<QuestionHintsDo>> hintsMap = new HashMap<String,ArrayList<QuestionHintsDo>>();
			if(questionType.equalsIgnoreCase("HS")){

				AddHotSpotQuestionAnswerChoice addQuestionAnswerChoice=(AddHotSpotQuestionAnswerChoice)questionHotSpotAnswerChoiceContainer.getWidget(0);

				if(addQuestionAnswerChoice.imageRDButton.isChecked()){
					hsType=i18n.GL3228_1();

					for(int i=0;i<addQuestionAnswerChoice.ansImageContainer.getWidgetCount();i++)
					{
						QuestionAnswerDo questionAnswerDo = new QuestionAnswerDo();
						final AddAnswerImg ansImage=(AddAnswerImg)addQuestionAnswerChoice.ansImageContainer.getWidget(i);

						questionAnswerDo.setAnswerText(ansImage.getFileName());
						questionAnswerDo.setAnswerType(hsType);
						questionAnswerDo.setSequence(i+1);
						questionAnswerDo.setIsCorrect(ansImage.selectedImage);
						enteredAnswers.add(questionAnswerDo);
					}

				}else{
					hsType=i18n.GL3229_1();
					for(int i=0;i<addQuestionAnswerChoice.textAnsContainer.getWidgetCount();i++)
					{
						QuestionAnswerDo questionAnswerDo = new QuestionAnswerDo();
						final AddAnswerChoice addAnswerChoice=(AddAnswerChoice)addQuestionAnswerChoice.textAnsContainer.getWidget(i);
						questionAnswerDo.setAnswerText(addAnswerChoice.answerTextBox.getRawContent());
						questionAnswerDo.setAnswerType(hsType);
						questionAnswerDo.setSequence(i+1);
						questionAnswerDo.setIsCorrect(addAnswerChoice.isOptionSelectedButton);
						enteredAnswers.add(questionAnswerDo);
					}
				}

			}

			answerMap.put("answer",enteredAnswers);

			for(int i=0;i<hintsContainer.getWidgetCount();i++)
			{
				AddHintsView addHints = (AddHintsView)hintsContainer.getWidget(i);
				QuestionHintsDo questionHintsDo=new QuestionHintsDo();
				String hintText=addHints.hintTextBox.getText();
				if(hintText!=null&&!hintText.trim().equals("")){
					hintText=addHints.hintTextBox.getRawContent().trim();
				}
				questionHintsDo.setHintText(hintText); 
				questionHintsDo.setSequence(i+1);
				enteredHints.add(questionHintsDo);
			}
			hintsMap.put("hint",enteredHints);

			collectionQuestionItemDo.setTypeName(questionType); 
			collectionQuestionItemDo.setQuestionText(questionText);
			collectionQuestionItemDo.setAnswers(answerMap);
			collectionQuestionItemDo.setExplanation(explaination);
			collectionQuestionItemDo.setHints(hintsMap);
			collectionQuestionItemDo.setTitle(questionText);
			collectionQuestionItemDo.setDescription(questionText);  
			HashMap<String,ArrayList<CodeDo>> taxonomySet = new HashMap<String,ArrayList<CodeDo>>();
			taxonomySet.put("taxonomyCode", standardsDo);
			collectionQuestionItemDo.setTaxonomySet(taxonomySet);
			HashMap<String,ArrayList<checkboxSelectedDo>> depthOfKnowledge = new HashMap<String,ArrayList<checkboxSelectedDo>>();
			depthOfKnowledge.put("depthOfKnowledge", depthOfKnowledges);
			collectionQuestionItemDo.setDepthOfKnowledges(depthOfKnowledge);

			if(questionType.equalsIgnoreCase("HS")){
				CollectionHTQuestionItemDo HSObj=new CollectionHTQuestionItemDo();
				HSObj.setHlType(hsType);
				HSObj.setSingleCorrectAnswer(false);
				collectionQuestionItemDo.setAttributes(HSObj);
			}
			if(!isSaveButtonClicked){
				isSaveButtonClicked=true;
				createQuestionResource(mediaFileName,collectionQuestionItemDo);
			}
		}
	}


	@UiHandler("cancelButton")
	public void clickedOnCancelButton(ClickEvent clickEvent){

		resetToHints();
		if(isEditResource){
			if(deletedStandardsDo.size()>0){
				AppClientFactory.getInjector().getResourceService().UpdateResourceTaxonomy(collectionItemDo.getResource().getGooruOid(), deletedStandardsDo, new SimpleAsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						deletedStandardsDo.clear();
					}
				});
			}
		}
		getUiHandlers().hidePopup();
	}


	public void createQuestionResource(String mediaFileName, CollectionQuestionItemDo collectionQuestionItemDo) {
		if(getQuestionEditMode()){
			String thumbnailUrl=null;
			if(addQuestImgContainer.getWidgetCount()>0){
				AddQuestionImg addQuestionImage=(AddQuestionImg)addQuestImgContainer.getWidget(0);
				thumbnailUrl=addQuestionImage.getFileName();
			}
			getUiHandlers().v2UpdateQuestionResource(collectionItemDo,collectionQuestionItemDo,thumbnailUrl==null?null:"asset-question_"+thumbnailUrl);
		}else{
			getUiHandlers().addHSQuestionResource(mediaFileName,collectionQuestionItemDo);
		}


	}

	public void resetFields() {
		clearTinyMce();
		buttonContainer.getElement().getStyle().setDisplay(Display.BLOCK);
		resetToHints();
		setHotSpotAnswerFields();
		depthOfKnowledges.clear();
		standardsPanel.clear();
		centuryPanel.clear();
		setTextAndStyle();
		resetDepthOfKnowledges();
		isAddBtnClicked=true;
		isRightsClicked=false;
		validationValue=false;
		isSaveButtonClicked=false;
		clearObjects();
		ansChoiceErrMsg.setText("");
	}
	
	public void clearObjects(){
		standardCodesMap.clear();
		centuryCodesMap.clear();
		standardsDo.clear();
		deletedStandardsDo.clear();
		standardSearchDo=new SearchDo<CodeDo>();
		centurySearchDo=new SearchDo<StandardFo>();
		centurySelectedValues.clear();
		depthOfKnowledges.clear();
		isValidHintsList.clear();
	}


	public void resetDepthOfKnowledges(){
		chkLevelRecall.setChecked(false);
		chkLevelSkillConcept.setChecked(false);
		chkLevelStrategicThinking.setChecked(false);
		chkLevelExtendedThinking.setChecked(false);
	}

	private boolean isHotSpotAnswerChoiceSelected(HTMLPanel questionHotSpotAnswerChoiceContainer) 
	{
		boolean isAnswerChoiceSelected=false;
		AddHotSpotQuestionAnswerChoice addQuestionAnswerChoice=(AddHotSpotQuestionAnswerChoice)questionHotSpotAnswerChoiceContainer.getWidget(0);

		if(addQuestionAnswerChoice.imageRDButton.isChecked()){
			for(int i=0;i<addQuestionAnswerChoice.ansImageContainer.getWidgetCount();i++)
			{
				final AddAnswerImg addAnswerImg=(AddAnswerImg)addQuestionAnswerChoice.ansImageContainer.getWidget(i);
				if(addAnswerImg.selectedImage){
					isAnswerChoiceSelected=true;
				}
			}
		}else{
			for(int i=0;i<addQuestionAnswerChoice.textAnsContainer.getWidgetCount();i++)
			{
				final AddAnswerChoice addAnswerChoice=(AddAnswerChoice)addQuestionAnswerChoice.textAnsContainer.getWidget(i);
				if(addAnswerChoice.isOptionSelectedButton){
					isAnswerChoiceSelected=true;
				}
			}
		}

		return isAnswerChoiceSelected;
	}
	void showErrorMessageForAnswer(String errMessage){

		ansChoiceErrMsg.setText(errMessage);
		StringUtil.setAttributes(ansChoiceErrMsg.getElement(), "lblErrorMessageForAnswer", errMessage, errMessage);

		questionNameTextArea.getElement().addClassName("errorBorderMessage");
	}


	public boolean isProfanityCheckForAnswerChoice(final boolean fieldValidationStaus,final String mediaFileName){


		validationValue=false;
		AppClientFactory.getInjector().getResourceService().checkProfanityForList(profanityList, new SimpleAsyncCallback<List<ProfanityCheckDo>>() {

			@Override
			public void onSuccess(List<ProfanityCheckDo> result) {
				if(questionType.equalsIgnoreCase("HS") && hsType.equalsIgnoreCase(i18n.GL3228_1())){
					AddHotSpotQuestionAnswerChoice addQuestionAnswerChoice=(AddHotSpotQuestionAnswerChoice)questionHotSpotAnswerChoiceContainer.getWidget(0);
					for(int i=0;i<addQuestionAnswerChoice.textAnsContainer.getWidgetCount();i++)
					{
						final AddAnswerChoice addAnswerChoice=(AddAnswerChoice)addQuestionAnswerChoice.textAnsContainer.getWidget(i);
						addAnswerChoice.errorMessageforAnswerChoice.setText("");
						addAnswerChoice.getAnswerTextBox().getElement().removeClassName("errorBorderMessage");
						SetStyleForProfanity.SetStyleForProfanityForTinyMCE(addAnswerChoice.answerTextBox, addAnswerChoice.errorMessageforAnswerChoice, result.get(i).questionValue);
						if(result.get(i)!=null && result.get(i).questionValue==true){
							addQuestionAnswerChoice.errorMessageforAnswerChoice.getElement().setAttribute("style", "float: left;left: 24px;");
							validationValue=true;
							isAddBtnClicked=true;
						}

					}
				}
				if(validationValue && fieldValidationStaus){
					return;
				}else{
					isValidHintsList.clear();
					for(int i=0;i<hintsListForProfanity.size();i++){
						isValidHintsList.add(hintsListForProfanity.get(i).getQuestionText());
					}
					if(!isValidHintsList.toString().contains("undefined")){
						AppClientFactory.getInjector().getResourceService().checkProfanityForList(hintsListForProfanity, new SimpleAsyncCallback<List<ProfanityCheckDo>>() {

							@Override
							public void onSuccess(List<ProfanityCheckDo> result) {
								for(int i=0;i<hintsContainer.getWidgetCount();i++){
									if(result.get(i).questionValue==true){
										final AddHintsView addHints = (AddHintsView) hintsContainer.getWidget(i);
										addHints.errorMessageforHints.setText("");
										addHints.errorMessageforHints.getElement().setAttribute("style", "float: left;left: 24px;");
										SetStyleForProfanity.SetStyleForProfanityForTinyMCE(addHints.hintTextBox, addHints.errorMessageforHints, result.get(i).questionValue);
										validationValue=true;
										isAddBtnClicked=true;
									}
								}
								if(validationValue){
									return;
								}else{
									if(!isRightsClicked){
										rightsLbl.getElement().getStyle().setColor("orange");
										isAddBtnClicked=true;
									}else{
										if(fieldValidationStaus)
											addFunctionality(!validationValue,mediaFileName);
									}
								}
							}
						});
					}
				}
			}
		});
		return validationValue;
	}
	


	@Override
	public void editQuestion(CollectionItemDo collectionItemDo) {
		this.collectionItemDo=collectionItemDo;
		isEditResource=true;
		explanationContainer.setVisible(false);
    	depthOfKnowledgeContainer.setVisible(false);
    	hintsContainer.setVisible(false);
    	standardContainer.setVisible(false);
    	centuryContainer.setVisible(false);
    	
		
		TreeSet<QuestionHintsDo> hintsList = collectionItemDo.getResource().getHints() != null ? collectionItemDo.getResource().getHints() : collectionItemDo.getQuestionInfo().getHints();
		Iterator<QuestionHintsDo> iterator = hintsList.iterator();
		hintsContainer.clear();
		while (iterator.hasNext()) {
			QuestionHintsDo hints = iterator.next();
			int widgetCount=hintsContainer.getWidgetCount();
	        final AddHintsView addHints = new AddHintsView(widgetCount+1,hints.getHintText());
	        addHintsTextArea(addHints);
		}
		
		TreeSet<QuestionAnswerDo> answerChoicesSet = collectionItemDo.getResource().getAnswers() != null ? collectionItemDo.getResource().getAnswers() : collectionItemDo.getQuestionInfo().getAnswers();
		Iterator<QuestionAnswerDo> it = answerChoicesSet.iterator(); 
		
		
		AddHotSpotQuestionAnswerChoice addHotSpotQuestion=(AddHotSpotQuestionAnswerChoice) questionHotSpotAnswerChoiceContainer.getWidget(0);
		
		String HsType=	collectionItemDo.getResource().getAttributes().getHlType();
		
		if(HsType.equalsIgnoreCase(i18n.GL3229_1())){
			int widgetcount=0;
			while (it.hasNext()) {
				addHotSpotQuestion.setAnswerFields(false);
				QuestionAnswerDo answer = it.next();
				
				final AddAnswerChoice addAnswerChoice=new AddAnswerChoice(widgetcount+"",answer.getAnswerText());
				
				/*if(answer.isIsCorrect()){	
					addAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerSelected());
				}else{
					addAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
				}
				*/
				
				addHotSpotQuestion.textAnsContainer.add(addAnswerChoice);
				widgetcount++;
			}
			
		}
		
	}
	
	
	public void setEditData(){
		try{
			setEditQuestionImage();

			int type = collectionItemDo.getResource().getType() != null ? collectionItemDo.getResource().getType() : collectionItemDo.getQuestionInfo().getType();
			timer.schedule(0);

			if(collectionItemDo.getResource().getDepthOfKnowledges()!=null){
				int checkBoxCount=0;
				for (checkboxSelectedDo item : collectionItemDo.getResource().getDepthOfKnowledges()) {			
					   if(item.isSelected()){
						   if(checkBoxCount==0)
						   chkLevelRecall.setChecked(true);
						   if(checkBoxCount==1)
					       chkLevelSkillConcept.setChecked(true);
						   if(checkBoxCount==2)
					       chkLevelStrategicThinking.setChecked(true);
						   if(checkBoxCount==3)
					       chkLevelExtendedThinking.setChecked(true);
					   }
					   checkBoxCount++;
					}
				}
				
				if(collectionItemDo.getStandards()!=null){
					standardsPanel.clear();
					standardsDo.clear();
					String codeID="",code="",label="";
					for (Map<String, String> map: collectionItemDo.getStandards()) {
						 CodeDo codeObj=new CodeDo();
						for (Map.Entry<String, String> entry : map.entrySet()) {
							String key = entry.getKey();
							String values = entry.getValue();
							 if(key.contains("codeId")){
								 codeID=values;
								 codeObj.setCodeId(Integer.parseInt(values));
							 }
							 if(key.contains("code")){
								 code=values;
								 codeObj.setCode(values);
							 }
							 if(key.contains("description")){
								 label=values;
								 codeObj.setLabel(values);
							 }
							}
						 standardsDo.add(codeObj);
						 standardsPanel.add(createStandardLabel(code, codeID,label));
					}
				}
				if(collectionItemDo.getResource().getSkills()!= null && collectionItemDo.getResource().getSkills().size()>0){
					centuryPanel.clear();
					for (StandardFo standardObj : collectionItemDo.getResource().getSkills()) {
						 CodeDo codeObj=new CodeDo();
						 codeObj.setCodeId(standardObj.getCodeId());
						 codeObj.setCode(standardObj.getLabel());
						 standardsDo.add(codeObj);
						 centurySelectedValues.put(Long.parseLong(standardObj.getCodeId()+""), standardObj.getLabel());
						 centuryPanel.add(create21CenturyLabel(standardObj.getLabel(),standardObj.getCodeId()+"",""));
					}
				}

			
			setDepthOfKnowledgeContainer();
			setHintsContainer();
			setStandardsContainer();
			setCenturyContainer();

				 
			}catch(Exception e){
				AppClientFactory.printSevereLogger(e.getMessage());
			}
	}
	
	
	
	public void setEditQuestionImage(){
		String tumbnailUrl="";
		if(collectionItemDo!=null){
			if (collectionItemDo.getResource().getAssets() != null
					&& collectionItemDo.getResource().getAssets().size() > 0) {
				tumbnailUrl = collectionItemDo.getCollection().getAssetURI()
						+ collectionItemDo.getResource().getFolder()
						+ collectionItemDo.getResource().getAssets().get(0).getAsset().getName();
				
			}else if(collectionItemDo.getResource().getThumbnails()!=null && collectionItemDo.getResource().getThumbnails().getUrl()!=null) {
				tumbnailUrl = collectionItemDo.getResource().getThumbnails().getUrl();
			}
			
			setImageUrl(tumbnailUrl, null, true, false);
			
		}
		
	}
	
	
	Timer timer=new Timer() {
		
		@Override
		public void run() {
			String explanation = collectionItemDo.getResource().getExplanation() != null ? collectionItemDo.getResource().getExplanation() : collectionItemDo.getQuestionInfo().getExplanation();
			
			questionNameTextArea.setText(collectionItemDo.getResource().getTitle());
			questionNameTextArea.getElement().setAttribute("alt", collectionItemDo.getResource().getTitle());
			questionNameTextArea.getElement().setAttribute("title", collectionItemDo.getResource().getTitle());
			explainationTextArea.setText(explanation);
			explainationTextArea.getElement().setAttribute("alt", explanation);
			explainationTextArea.getElement().setAttribute("title", explanation);
			
			
			setExplanationContainer();
		}
	};
	
}