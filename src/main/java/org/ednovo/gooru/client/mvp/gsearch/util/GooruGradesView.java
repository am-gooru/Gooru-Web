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
package org.ednovo.gooru.client.mvp.gsearch.util;

import java.util.Arrays;
import java.util.Iterator;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.gsearch.events.UpdateFilterEvent;
import org.ednovo.gooru.client.uc.LiPanel;
import org.ednovo.gooru.client.uc.UlPanel;
import org.ednovo.gooru.shared.i18n.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author Search Team
 *
 */
public class GooruGradesView extends BaseViewWithHandlers<GooruGradesUiHandlers> implements IsGooruGradesView {

	private static GooruGradesViewUiBinder uiBinder = GWT.create(GooruGradesViewUiBinder.class);
	
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@UiTemplate("GooruGradesView.ui.xml")
	interface GooruGradesViewUiBinder extends UiBinder<Widget, GooruGradesView> {
	}
	
	@UiField UlPanel elementryPanel,middlePanel,higherPanel;
	
	@UiField HTMLPanel gradeContainer;
	
	public static final String GRADE_FLT = "flt.grade";
	
	public static final String ADD = "add";
	
	public static final String REMOVE = "remove";
	
	String[] elementaryGrades = new String[]{"Elementary",i18n.GL3071(),i18n.GL3072(),i18n.GL3073(),i18n.GL3074(),i18n.GL3075(),i18n.GL3076()};
	String[] middleGrades = new String[]{"Middle School",i18n.GL3077(),i18n.GL3078(),i18n.GL3079()};
	String[] higherGrades = new String[]{"High School",i18n.GL3080(),i18n.GL3081(),i18n.GL3082(),i18n.GL3083()};
	String backgroundColor="rgb(16, 118, 187)";
	/**
	 * Class constructor 
	 * @param eventBus {@link EventBus}
	 */
	@Inject
	public GooruGradesView() {
		setWidget(uiBinder.createAndBindUi(this));
		renderGradesLiPanel(elementryPanel,elementaryGrades);
		renderGradesLiPanel(middlePanel,middleGrades);
		renderGradesLiPanel(higherPanel,higherGrades);
		showGradesFilter();
	}
	/**
	 * To render the grade values.
	 * @param ulPanel {@link UlPanel}
	 * @param stringArray {@link Arrays}
	 */
	public void renderGradesLiPanel(UlPanel ulPanel, final String[] stringArray) {
		for(int i=0;i<stringArray.length;i++){
		    final LiPanel gradePanel = new LiPanel();
			if(i==0){
				gradePanel.setStyleName("gradeTypes");
			}
			final Anchor lblGrade=new Anchor(stringArray[i]);
			gradePanel.add(lblGrade);
			ulPanel.add(gradePanel);
			gradePanel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(gradePanel.getWidget(0).getElement().getInnerText().equals(stringArray[0])){
						if(lblGrade.getElement().getStyle().getBackgroundColor().equalsIgnoreCase(backgroundColor)){
							lblGrade.getElement().getStyle().clearBackgroundColor();
							selectAllGrades(stringArray,REMOVE);
						}else{
							lblGrade.getElement().getStyle().setBackgroundColor("#1076bb");
							selectAllGrades(stringArray,ADD);
						}
						
					}else{
						boolean value=checkSelectedGrades(stringArray, lblGrade.getElement().getInnerText());
						if(value){
							if(gradePanel.getParent().getElement().getFirstChildElement().getFirstChildElement().getStyle().getBackgroundColor().equals(backgroundColor)){
								gradePanel.getParent().getElement().getFirstChildElement().getFirstChildElement().getStyle().clearBackgroundColor();
							}else{
								gradePanel.getParent().getElement().getFirstChildElement().getFirstChildElement().getStyle().setBackgroundColor(backgroundColor);
							}
							
						}else{
						}
						if(lblGrade.getElement().getStyle().getBackgroundColor().equalsIgnoreCase(backgroundColor)){
							lblGrade.getElement().getStyle().clearBackgroundColor();
							AppClientFactory.fireEvent(new UpdateFilterEvent("Grade "+lblGrade.getText(), REMOVE));
						}else{
							AppClientFactory.fireEvent(new UpdateFilterEvent("Grade "+lblGrade.getText(), ADD));
							lblGrade.getElement().getStyle().setBackgroundColor("#1076bb");
						}
					}
					
				}

			});
		}
	}
	
	/**
	 * Pre-Selected grades showing in search page
	 */
	private void showGradesFilter() {
		String grades = AppClientFactory.getPlaceManager().getRequestParameter(GRADE_FLT);
		if(grades!=null){
			String[] gradesSplit = grades.split(",");
			for(int i=0; i<gradesSplit.length; i++){
				updateFilterStyle(gradesSplit[i], "add");
			}
		}
	}
	
	/**
	 * This method will update the filter style
	 * @param filterName {@link String}
	 */
	@Override
	public void updateFilterStyle(String filterName, String addOrRemove){
		Iterator<Widget> widgets= gradeContainer.iterator();
		while(widgets.hasNext()){
			Widget widget = widgets.next();

			if(widget instanceof UlPanel){
				Iterator<Widget>  liwidgets = ((UlPanel) widget).iterator();
				while(liwidgets.hasNext()){
					Widget liwidget = liwidgets.next();
					if(liwidget instanceof LiPanel){
						if(filterName.equals(((LiPanel) liwidget).getElement().getInnerText())){
							if(addOrRemove.equals("add")){
								((LiPanel) liwidget).getWidget(0).getElement().getStyle().setBackgroundColor(backgroundColor);
							}else{
								((LiPanel) liwidget).getWidget(0).getElement().getStyle().clearBackgroundColor();
							}
						}
					}
				}

			}
		}
	}
	/**
	 * To select all grades when click on Elementry,Middle or Higher grade
	 * @param gradeArray {@link Arrays}
	 * @param addOrRemove  {@link String}
	 */
	private void selectAllGrades(String[] gradeArray, String addOrRemove) {
		String grades = AppClientFactory.getPlaceManager().getRequestParameter(GRADE_FLT, null);
		for(int i=1; i<gradeArray.length; i++){
			if(grades!=null && !grades.contains(gradeArray[i])){
				updateFilterStyle(gradeArray[i],addOrRemove);
				AppClientFactory.fireEvent(new UpdateFilterEvent(i18n.GL0325()+" "+gradeArray[i], addOrRemove));
			}else if(addOrRemove.equals(REMOVE)){
				updateFilterStyle(gradeArray[i],addOrRemove);
				AppClientFactory.fireEvent(new UpdateFilterEvent(i18n.GL0325()+" "+gradeArray[i], addOrRemove));
			}else if(grades==null){
				updateFilterStyle(gradeArray[i],addOrRemove);
				AppClientFactory.fireEvent(new UpdateFilterEvent(i18n.GL0325()+" "+gradeArray[i], addOrRemove));
			}
		}

	}

    /**
     * Checking all Elementry/Middle/Higher grades are selected or not
     * @param stringArray {@link Arrays}
     * @param selectedValue {@link String}
     * @return {@link Boolean}
     */
	private boolean checkSelectedGrades(String[] stringArray, String selectedValue) {
		String grades = AppClientFactory.getPlaceManager().getRequestParameter(GRADE_FLT);
		if(grades!=null){
			grades+=selectedValue;
			for(int i=1;i<stringArray.length;i++){
				if(!grades.contains(stringArray[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
