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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add;

import org.ednovo.gooru.client.ui.TinyMCE;
import org.ednovo.gooru.shared.i18n.MessageProperties;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AddAnswerChoice extends Composite implements HasMouseOutHandlers,HasMouseOverHandlers{
	
	public interface AddAnswerChoiceUiBinder extends UiBinder<Widget, AddAnswerChoice>{
		
	}
	
	public static AddAnswerChoiceUiBinder uiBinder=GWT.create(AddAnswerChoiceUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	
	@UiField
	public Label errorMessageforAnswerChoice;

	@UiField
	Label labelChoice;

	@UiField
	public
	Label optionSelectedButton;
	@UiField public TinyMCE answerTextBox;
	@UiField HTMLPanel tinyOrTextBoxConatiner,deleteButtonContainer;
	
	public String fieldValue;
	public Label ansChoiceDeleteButton=new Label();
	private String richTextData=null;
	public boolean isOptionSelectedButton=false;
	
	public AddAnswerChoice(){
		initWidget(uiBinder.createAndBindUi(this));
		labelChoice.getElement().setId("lblLabelChoice");
		tinyOrTextBoxConatiner.getElement().setId("pnlTinyOrTextBoxConatiner");
		answerTextBox.getElement().setId("tinyMCEAnswerTextBox");
		ansChoiceDeleteButton.setStyleName("answerMarkDelete");
		ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
		deleteButtonContainer.getElement().setId("pnlDeleteButtonContainer");
		errorMessageforAnswerChoice.getElement().setId("errlblErrorMessageforAnswerChoice");
		deleteButtonContainer.add(ansChoiceDeleteButton);

	}
	public AddAnswerChoice(String labelName){
		initWidget(uiBinder.createAndBindUi(this));
		labelChoice.getElement().setId("lblLabelChoice");
		tinyOrTextBoxConatiner.getElement().setId("pnlTinyOrTextBoxConatiner");
		answerTextBox.getElement().setId("tinyMCEAnswerTextBox");
		deleteButtonContainer.getElement().setId("pnlDeleteButtonContainer");
		ansChoiceDeleteButton.setStyleName("answerMarkDelete");
		ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
		deleteButtonContainer.add(ansChoiceDeleteButton);
		errorMessageforAnswerChoice.getElement().setId("errlblErrorMessageforAnswerChoice");
		labelChoice.setText(labelName);
		labelChoice.getElement().setAttribute("alt", labelName);
		labelChoice.getElement().setAttribute("title", labelName);

	}
	public AddAnswerChoice(String labelName,String richTextData){
		initWidget(uiBinder.createAndBindUi(this));
		this.richTextData=richTextData;
		tinyOrTextBoxConatiner.getElement().setId("pnlTinyOrTextBoxConatiner");
		answerTextBox.getElement().setId("tinyMCEAnswerTextBox");
		ansChoiceDeleteButton.setStyleName("answerMarkDelete");
		ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
		deleteButtonContainer.getElement().setId("pnlDeleteButtonContainer");
		deleteButtonContainer.add(ansChoiceDeleteButton);
		errorMessageforAnswerChoice.getElement().setId("errlblErrorMessageforAnswerChoice");
		labelChoice.getElement().setId("lblLabelChoice");
		labelChoice.setText(labelName);
		labelChoice.getElement().setAttribute("alt", labelName);
		labelChoice.getElement().setAttribute("title", labelName);
	}
	
	@Override
	public void onLoad(){
		super.onLoad();
		 Scheduler.get().scheduleDeferred(new ScheduledCommand(){
				@Override
				public void execute() {
						setRichTextData();
				}
	       });
	}
	 public void setRichTextData(){
		 tinyOrTextBoxConatiner.getElement().setId("pnlTinyOrTextBoxConatiner");
		 answerTextBox.getElement().setId("tinyMCEAnswerTextBox");
		 deleteButtonContainer.getElement().setId("pnlDeleteButtonContainer");
		 errorMessageforAnswerChoice.getElement().setId("errlblErrorMessageforAnswerChoice");
		   if(richTextData!=null){
			   answerTextBox.setText(richTextData);
			   answerTextBox.getElement().setAttribute("alt", richTextData);
			   answerTextBox.getElement().setAttribute("title", richTextData); 
		   }	   
	   }
	public void setLabelName(String labelName){
		labelChoice.setText(labelName);
		labelChoice.getElement().setId("lblLabelChoice");
		labelChoice.getElement().setAttribute("alt", labelName);
		labelChoice.getElement().setAttribute("title", labelName);
		deleteButtonContainer.getElement().setId("pnlDeleteButtonContainer");
		tinyOrTextBoxConatiner.getElement().setId("pnlTinyOrTextBoxConatiner");
		answerTextBox.getElement().setId("tinyMCEAnswerTextBox");
		errorMessageforAnswerChoice.getElement().setId("errlblErrorMessageforAnswerChoice");
	}
	
	
	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		// TODO Auto-generated method stub
		return addDomHandler(handler, MouseOverEvent.getType());
	}
	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		// TODO Auto-generated method stub
		return addDomHandler(handler, MouseOutEvent.getType());
	}
	public TinyMCE getAnswerTextBox() {
		return answerTextBox;
	}
	public void setAnswerTextBox(TinyMCE answerTextBox) {
		this.answerTextBox = answerTextBox;
	}
	
	
}
