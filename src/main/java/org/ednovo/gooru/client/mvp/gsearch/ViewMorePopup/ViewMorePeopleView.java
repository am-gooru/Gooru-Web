package org.ednovo.gooru.client.mvp.gsearch.ViewMorePopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.search.util.CollectionSearchWidget.OnCollectionImageClick;
import org.ednovo.gooru.client.uc.H4Panel;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ResourceCollDo;
import org.ednovo.gooru.shared.util.ClientConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class ViewMorePeopleView extends PopupViewWithUiHandlers<ViewMorePeopleUiHandlers> implements IsViewMorePeopleView,ClientConstants {

	private static ViewMorePeopleViewUiBinder uiBinder = GWT
			.create(ViewMorePeopleViewUiBinder.class);

	interface ViewMorePeopleViewUiBinder extends
			UiBinder<Widget, ViewMorePeopleView> {
	}


	
	static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	PopupPanel appPopUp;
	@UiField Anchor cancelResourcePopupBtnLbl;
	@UiField HTMLPanel scrollContainer;
	@UiField H4Panel UsedByStr;

	@Inject
	public ViewMorePeopleView(EventBus eventBus) {
		super(eventBus);
		appPopUp=new PopupPanel();
		appPopUp.setWidget(uiBinder.createAndBindUi(this));
		appPopUp.setGlassEnabled(true);
		appPopUp.getElement().getStyle().setZIndex(999999);
		
	}

	@UiHandler("cancelResourcePopupBtnLbl")
	public void cancelButtonEvent(ClickEvent event){
		hide();
		enableTopFilters();
	}

	@Override
	public Widget asWidget() {
		return appPopUp;
	}

	@Override
	public void reset() {
	}

	@Override
	public void onLoad() {
		
	}

	@Override
	public void onUnload() {
		
	}


	public void enableTopFilters(){
		Element element = Document.get().getElementById("fixedFilterSearchID");
		if(element!=null){
			element.removeAttribute("style");
		}
		Window.enableScrolling(true);
	}


	@Override
	public void hidePopup() {
		Element element = Document.get().getElementById("fixedFilterSearchID");
		if(element!=null)
		{
		element.removeAttribute("style");
		}
		hide();
		enableTopFilters();
		
	}
	
	@Override
	public void displayContents(ArrayList<ResourceCollDo> userCollectionsList) {
		UsedByStr.setText("Used by "+userCollectionsList.size()+" people");
		for(int i=0; i<userCollectionsList.size(); i++)
		{
			ResourceCollectionUserWidget resourceCollwidget = new ResourceCollectionUserWidget(userCollectionsList.get(i));
			resourceCollwidget.getCollectionImage().addClickHandler(new OnCollectionImageClick(userCollectionsList.get(i).getGooruOid()));
			resourceCollwidget.getRelatedCollectionTitle().addClickHandler(new OnCollectionImageClick(userCollectionsList.get(i).getGooruOid()));
			scrollContainer.add(resourceCollwidget);
		}
	}
	
	/**
	 * This inner class will handle the click event on the collection image click
	 * @author Gooru
	 */
	public class OnCollectionImageClick implements ClickHandler{
		String gooruOid;
		OnCollectionImageClick(String gooruOid){
			this.gooruOid=gooruOid;
		}
		@Override
		public void onClick(ClickEvent event) {
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onFailure(Throwable reason) {
				}

				@Override
				public void onSuccess() {
					hidePopup();
					Map<String, String> params = new HashMap<String, String>();
					params.put("id", gooruOid);
					//Cookies.setCookie("getScrollTop", Window.getScrollTop()+"");
					PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
					AppClientFactory.getPlaceManager().revealPlace(false,placeRequest,false);
				}
			});
		}
	}
	
	
}