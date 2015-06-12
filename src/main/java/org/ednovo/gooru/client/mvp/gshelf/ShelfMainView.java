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
package org.ednovo.gooru.client.mvp.gshelf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.folder.toc.FolderContainerCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.SetFolderMetaDataEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.SetFolderParentNameEvent;
import org.ednovo.gooru.client.mvp.shelf.list.ShelfCollection;
import org.ednovo.gooru.client.mvp.shelf.list.TreeMenuImages;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClassPageCollectionDo;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * @fileName : ShelfView.java
 * 
 * @description :
 * 
 * 
 * @version : 5.5
 * 
 * @date: June 17, 2013
 * 
 * @Author Gooru Team
 * 
 * @Reviewer:
 */
public class ShelfMainView extends BaseViewWithHandlers<ShelfMainUiHandlers> implements IsShelfMainView {

	@UiField HTMLPanel floderTreeContainer;

	@UiField HTMLPanel gShelfMainContainer,pnlSlot;
	
	@UiField HTMLEventPanel organizeRootPnl;
	
	private static final String O1_LEVEL = "o1";
	
	private static final String O2_LEVEL = "o2";
	
	private static final String O3_LEVEL = "o3";
	
	private static final String ID = "id";

	private static final String FOLDER = "folder";
	private static final String TYPE = "type";
	
	private static final String SCOLLECTION = "scollection";
	
	static Integer pageNumber = 1;

	private TreeItem treeChildSelectedItem = new TreeItem();

	private TreeItem previousTreeChildSelectedItem = new TreeItem();
	
	private static final List<FolderDo> SHELF_COLLECTIONS = new ArrayList<FolderDo>();

	private static final String GOORU_UID = "gooruuid";
	
	private Integer childPageNumber = 1;
	
	int collectionItemDoSize, count;

	boolean collectionItemsNotFriendly = false;

	static MessageProperties i18n = GWT.create(MessageProperties.class);

	String selectedFolderId = "";

	List<ClassPageCollectionDo> classpageTitles = null;


	private static ShelfMainViewUiBinder uiBinder = GWT
			.create(ShelfMainViewUiBinder.class);

	interface ShelfMainViewUiBinder extends UiBinder<Widget, ShelfMainView> {
	}

	private Tree shelfFolderTree = new Tree(new TreeMenuImages()) {
		@Override
		public void onBrowserEvent(Event event) {
			int eventType = DOM.eventGetType(event);
			if (!(eventType == Event.ONKEYUP || eventType == Event.ONKEYPRESS || eventType == Event.ONKEYDOWN)) {
				super.onBrowserEvent(event);
			}
		}
		public void onLoad(){
			 super.onLoad();
			 adjustTreeItemElementsStyle(shelfFolderTree);
		}
	};

	/**
	 * class constructor
	 */
	public ShelfMainView() {
		setWidget(uiBinder.createAndBindUi(this));
		setIdForFields();
		setTreeStucture();
	}

	/**
	 * To set id's for Ui fields
	 */
	private void setIdForFields() {
		gShelfMainContainer.getElement().setId("gShelfMainContainer");
	}
	private void setTreeStucture() {
		floderTreeContainer.clear();
		ShelfCollection treeItemShelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(treeItemShelfCollection!=null) {
			treeItemShelfCollection.setActiveStyle(false);
			/*if(organizeRootPnl.getStyleName().contains(folderStyle.active())) {
				//AppClientFactory.fireEvent(new SetFolderParentNameEvent(i18n.GL0180()));
				treeItemShelfCollection.setActiveStyle(false);
			} else {
				treeItemShelfCollection.setActiveStyle(true);
			}*/
			
		}
		shelfFolderTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				ShelfCollection shelfCollection = (ShelfCollection) event.getSelectedItem().getWidget();
				if(!shelfCollection.getCollectionDo().getCollectionType().equals("assessment/url")){
					treeChildSelectedItem = event.getSelectedItem();
					((ShelfCollection) treeChildSelectedItem.getWidget()).openFolderItem();
					setFolderActiveStatus();
				}
			}
		});
		floderTreeContainer.add(shelfFolderTree);
	}


	public void setFolderActiveStatus() { 
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(!shelfCollection.getCollectionDo().getCollectionType().equals("assessment/url")){
			if(shelfCollection.getCollectionDo().getType().equals("folder")) {
				TreeItem parent = treeChildSelectedItem.getParentItem();
				treeChildSelectedItem.getTree().setSelectedItem(parent, false);
				if(parent != null)parent.setSelected(false);
				treeChildSelectedItem.setState(treeChildSelectedItem.getState(), false);
				getUiHandlers().getChildFolderItems(shelfCollection.getCollectionDo().getGooruOid(),shelfCollection.getFolderOpenedStatus());
				shelfCollection.setFolderOpenedStatus(true);
			} else {
				//getUiHandlers().getCollectionItems(shelfCollection.getCollectionDo().getGooruOid(),shelfCollection.getCollectionOpenedStatus()); 
				shelfCollection.setCollectionOpenedStatus(true);
			}
			if((AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.MYCOLLECTION))) {
				shelfCollection.setActiveStyle(true);
			}
			
			ShelfCollection previousShelfCollection = (ShelfCollection) previousTreeChildSelectedItem.getWidget();
			if(previousShelfCollection==null) {
				previousTreeChildSelectedItem = treeChildSelectedItem;
			}
			if(previousShelfCollection!=null&&(shelfCollection.getCollectionDo().getGooruOid()!=previousShelfCollection.getCollectionDo().getGooruOid())) {
				previousShelfCollection.setActiveStyle(false);
			}
			previousTreeChildSelectedItem = treeChildSelectedItem;
		}
	}

	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.ViewImpl#setInSlot(java.lang.Object, com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void setInSlot(Object slot, Widget content) {
		pnlSlot.clear();
		if (content != null) {
			 if(slot==ShelfMainPresenter.COURSE_LIST_SLOT){
				 pnlSlot.add(content);
			 }else{
				 
			}
		}else{

		}
	}
	@Override
	public void getChildFolderItems(List<FolderDo> folderListDo) {
		String o2 = null, o3 = null, selectedFolder = null, selectedFolderName = null, id = null;
		FolderDo folderDo = null;
		TreeItem selectedItem = null;
		ShelfCollection selectedWidget = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(folderListDo!=null) {
			int nextLevel = 1;
			if(selectedWidget.getLevel()==1) {
				o2=AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 2;
			} else if (selectedWidget.getLevel()==2) { 
				o3=AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 3;
			}else if (selectedWidget.getLevel()==3) {
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 4;
			}
			for(int i=0;i<folderListDo.size();i++) {
				ShelfCollection shelfCollection = new ShelfCollection(folderListDo.get(i), nextLevel);
				shelfCollection.setWidgetPositions(nextLevel, i, selectedWidget.getUrlParams());
				TreeItem item = new TreeItem(shelfCollection);
				treeChildSelectedItem.addItem(item);
				adjustTreeItemStyle(item,folderListDo.get(i).getType(),nextLevel);
				correctStyle(item);
				if(nextLevel==2&& (o2!=null&&o2.equalsIgnoreCase(folderListDo.get(i).getGooruOid()) || id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid()))) {
					if(o2!=null) {
						selectedFolder = o2;
					} else {
						selectedFolder = id;
					}
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} else if(nextLevel==3&& (o3!=null&&o3.equalsIgnoreCase(folderListDo.get(i).getGooruOid()) || id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid()))) {
					if(o3!=null) {
						selectedFolder = o3;
					} else {
						selectedFolder = id;
					}
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} else if (nextLevel==4&&id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid())) {
					selectedFolder = id;
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} 
			}
		}
		treeChildSelectedItem.setState(true);
		selectedWidget.setOpenStyle(true, treeChildSelectedItem.getChildCount());
		
		if(selectedFolder!=null&&selectedItem!=null) { 
			checkShelfRefreshStatus(selectedItem, selectedFolder);
			ShelfCollection selectedWidget1 = (ShelfCollection) treeChildSelectedItem.getWidget();
			selectedWidget1.setFolderOpenedStatus(true);
		}
		
	}	
	
	private static void correctStyle(final UIObject uiObject) {
	      if (uiObject instanceof TreeItem) {
	         if (uiObject != null && uiObject.getElement() != null) {
	            Element element = uiObject.getElement();
	            element.getStyle().setPadding(0, Unit.PX);
	            element.getStyle().setMarginLeft(0, Unit.PX);
	         }
	      } else {
	         if (uiObject != null && uiObject.getElement() != null && uiObject.getElement().getParentElement() != null
	               && uiObject.getElement().getParentElement().getParentElement() != null
	               && uiObject.getElement().getParentElement().getParentElement().getStyle() != null) {
	            Element element = uiObject.getElement().getParentElement().getParentElement();
	            element.getStyle().setPadding(0, Unit.PX);
	            element.getStyle().setMarginLeft(0, Unit.PX);
	         }
	      }
 }
	/** 
	 * This method is to get the childPageNumber
	 */
	public int getChildPageNumber() {
		return childPageNumber;
	}

	@Override
	public void setChildPageNumber(Integer childPageNumber) {
		this.childPageNumber = childPageNumber;
	}

	@Override
	public void setUserShelfData(List<FolderDo> collections, boolean clearShelfPanel) {
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		
		String gooruOid = o1!=null?o1:id;
		int collectionCount=0;
		if(collections!=null){
			 if(collections.size()>0){
				 for(int i=0;i<collections.size();i++){
					 FolderDo floderDo=collections.get(i);
					 if(!getShelffCollection(floderDo.getGooruOid())){
						 ShelfCollection shelfCollection = new ShelfCollection(floderDo, 1);
						 shelfCollection.setWidgetPositions(1, collectionCount, null);
						 TreeItem folderItem=new TreeItem(new ShelfCollection(floderDo, 1));
						 shelfFolderTree.addItem(folderItem);
						 //adjustTreeItemStyle(folderItem,floderDo.getType(),0);
						//When page is refreshed, the folderItem previously selected will be highlighted.
							if(gooruOid!=null&&gooruOid.equalsIgnoreCase(floderDo.getGooruOid())) {
								checkShelfRefreshStatus(folderItem, floderDo.getGooruOid());
								AppClientFactory.fireEvent(new SetFolderParentNameEvent(floderDo.getTitle()));
								AppClientFactory.fireEvent(new SetFolderMetaDataEvent(StringUtil.getFolderMetaData(floderDo)));
								shelfCollection.setFolderOpenedStatus(true);
							}
						 collectionCount++;

					 }
				}
				 
				 floderTreeContainer.clear();
				 floderTreeContainer.add(shelfFolderTree);
			 }
		}
	}
	
	
	/**
	 * 
	 * @fileName : FolderTocView.java
	 *
	 * @description : 
	 *
	 * @version : 1.3
	 *
	 * @date: 10-02-2015
	 *
	 * @Author Gooru
	 *
	 * @Reviewer:
	 */
	public class FolderTreeItem extends Composite{
		private FlowPanel folderContainer=null;
		private String gooruOid=null;
		Label floderName=null;
		Label arrowLabel=null;
		private boolean isOpen=false;
		private boolean isApiCalled=false;
		private int folerLevel=1;
		public FolderTreeItem(){
			initWidget(folderContainer=new FlowPanel());
			folderContainer.setStyleName(FolderContainerCBundle.INSTANCE.css().folderLevel());
			arrowLabel=new Label();
			arrowLabel.setStyleName(FolderContainerCBundle.INSTANCE.css().folderTitlearrow());
			folderContainer.add(arrowLabel);
			floderName=new Label();
			floderName.setStyleName(FolderContainerCBundle.INSTANCE.css().folderTitleStyle());
			folderContainer.add(floderName);
		}
		public FolderTreeItem(String levelStyleName,String folderTitle,String gooruOid){
			this();
			if(levelStyleName!=null){
				folderContainer.addStyleName(levelStyleName);
			}
			this.gooruOid=gooruOid;
			floderName.setText(folderTitle);
		}
		public boolean isOpen() {
			return isOpen;
		}
		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}
		public String getGooruOid(){
			return gooruOid;
		}
		public boolean isApiCalled() {
			return isApiCalled;
		}
		public void setApiCalled(boolean isApiCalled) {
			this.isApiCalled = isApiCalled;
		}
		public int getFolerLevel() {
			return folerLevel;
		}
		public void setFolerLevel(int folerLevel) {
			this.folerLevel = folerLevel;
		}
	}
	private boolean getShelffCollection(String collectionId) {
		boolean flag = false;
		Iterator<Widget> widgets = shelfFolderTree.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof ShelfCollection && ((ShelfCollection) widget).getCollectionDo().getGooruOid().equals(collectionId)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * @function checkShelfRefreshStatus 
	 * @created_date : 11-Feb-2014
	 * @description
	 * @parm(s) : @param treeItem
	 * @return : void
	 * @throws : <Mentioned if any exceptions>
	*/
	
	private void checkShelfRefreshStatus(TreeItem treeItem, String parentId) {
		treeChildSelectedItem = treeItem;
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		shelfCollection.setActiveStyle(true);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		id = id!=null?id:"";
		if(!parentId.equalsIgnoreCase(id)) {
			getUiHandlers().getChildFolderItems(parentId,false);
		}
		ShelfCollection previousShelfCollection = (ShelfCollection) previousTreeChildSelectedItem.getWidget();
		if(previousShelfCollection!=null) {
			previousShelfCollection.setActiveStyle(false);
		}
		previousTreeChildSelectedItem = treeChildSelectedItem;
	}
	
	public void adjustTreeItemElementsStyle(Tree shelfTreePanel){
		int treeItemsCount=shelfTreePanel.getItemCount();
		if(shelfTreePanel!=null&&treeItemsCount>0){
			for(int i=0;i<treeItemsCount;i++){
				TreeItem treeItem=shelfTreePanel.getItem(i);
				Widget shelfWidget= treeItem.getWidget();
				if(shelfWidget instanceof ShelfCollection){
					adjustChildTreeItemsStyle(treeItem);
				}
				correctStyle(treeItem);
			}
		}
	}
	public void adjustChildTreeItemsStyle(TreeItem treeItem){
		int treeItemsCount=treeItem.getChildCount();
		if(treeItem!=null&&treeItemsCount>0){
			for(int i=0;i<treeItemsCount;i++){
				TreeItem childTreeItem=treeItem.getChild(i);
				Widget shelfWidget= childTreeItem.getWidget();
				if(shelfWidget instanceof ShelfCollection){
					adjustChildTreeItemsStyle(childTreeItem);
				}
				correctStyle(childTreeItem);
			}
		}
	}
	
	/**
	 * @function adjustTreeItemStyle 
	 * 
	 * @created_date : 10-02-2015
	 * 
	 * @description
	 * 
	 * @parm(s) : @param uiObject
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
   	private  void adjustTreeItemStyle(final UIObject uiObject, String itemType, int folderLevel) {
	      if (uiObject instanceof TreeItem) {
	         if (uiObject != null && uiObject.getElement() != null) {
	            Element element = uiObject.getElement();  
				 if(FOLDER.equalsIgnoreCase(itemType)){
						if(folderLevel>=2){
							 element.getStyle().setPaddingLeft(28, Unit.PX);
					            element.getStyle().setMarginLeft(0, Unit.PX);
					            if(folderLevel>2)
					            {
					            Element element1 = uiObject.getElement().getParentElement().getParentElement();
					            element1.getStyle().setPaddingLeft(28, Unit.PX);
				 		        element1.getStyle().setMarginLeft(0, Unit.PX);
					            }
						}
						else
						{
							if(folderLevel == 1)
							{
					           element.getStyle().setPaddingLeft(0, Unit.PX);
					           element.getStyle().setMarginLeft(-10, Unit.PX);
							}
							else
							{
					           element.getStyle().setPaddingLeft(0, Unit.PX);
					           element.getStyle().setMarginLeft(0, Unit.PX);
							}

						}
				 }else if(SCOLLECTION.equalsIgnoreCase(itemType)){
					 if(folderLevel>=2){
						 element.getStyle().setPaddingLeft(69, Unit.PX);
				            element.getStyle().setMarginLeft(0, Unit.PX);
				            
				            Element element1 = uiObject.getElement().getParentElement().getParentElement();
				            element1.getStyle().setPaddingLeft(28, Unit.PX);
			 		        element1.getStyle().setMarginLeft(0, Unit.PX);
			
							}
					 else
					 {
					  element.getStyle().setPaddingLeft(28, Unit.PX);
		              element.getStyle().setMarginLeft(0, Unit.PX);
					 }
					 if(folderLevel==2)
					 {
				            Element element1 = uiObject.getElement().getParentElement().getParentElement();
				            element1.getStyle().setPaddingLeft(0, Unit.PX);
			 		        element1.getStyle().setMarginLeft(-12, Unit.PX);
					 }
			
				 }

	         }
	      } else {
	         if (uiObject != null && uiObject.getElement() != null && uiObject.getElement().getParentElement() != null
	               && uiObject.getElement().getParentElement().getParentElement() != null
	               && uiObject.getElement().getParentElement().getParentElement().getStyle() != null) {
	            Element element = uiObject.getElement().getParentElement().getParentElement();

	 	           element.getStyle().setPadding(0, Unit.PX);
		           element.getStyle().setMarginLeft(1, Unit.PX);
	            
	         }
	      }
   	}
}