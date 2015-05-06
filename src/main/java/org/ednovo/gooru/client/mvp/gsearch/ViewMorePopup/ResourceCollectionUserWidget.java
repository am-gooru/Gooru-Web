package org.ednovo.gooru.client.mvp.gsearch.ViewMorePopup;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.uc.PPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.model.content.ResourceCollDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ResourceCollectionUserWidget extends Composite {

	private static ResourceImageWidgetUiBinder uiBinder = GWT
			.create(ResourceImageWidgetUiBinder.class);

	interface ResourceImageWidgetUiBinder extends
			UiBinder<Widget, ResourceCollectionUserWidget> {
	}
	
	@UiField PPanel gradePanel;
	@UiField Image creatorImage,collectionImage;
	@UiField InlineLabel relatedUserName,relatedCollectionTitle;
	
	private static String DEFULT_IMAGE = "images/default-collection-image.png";
	
	
	public ResourceCollectionUserWidget(final ResourceCollDo resourceCollDo) {
		initWidget(uiBinder.createAndBindUi(this));
		if(resourceCollDo.getUser()!=null && resourceCollDo.getUser().getMeta() != null)
		{
			String gradesCourses = "";
			if(resourceCollDo.getUser().getMeta().getGrade()!=null)
			{
				gradesCourses = resourceCollDo.getUser().getMeta().getGrade();
			}
			if(resourceCollDo.getUser().getMeta().getCourse()!=null && resourceCollDo.getUser().getMeta().getCourse().size()>0)
			{
				for(int i=0;i<resourceCollDo.getUser().getMeta().getCourse().size();i++)
				{
					if(!gradesCourses.isEmpty())
					{
					gradesCourses = gradesCourses+","+resourceCollDo.getUser().getMeta().getCourse().get(i).getCode().getLabel();
					}
					else
					{
					gradesCourses = resourceCollDo.getUser().getMeta().getCourse().get(i).getCode().getLabel();
					}
				}
			}
		gradePanel.setText(gradesCourses);
		}	
		
		if(resourceCollDo.getUser()!=null && resourceCollDo.getUser().getCustomFields()!=null && resourceCollDo.getUser().getCustomFields().size()>0)
		{
			if(resourceCollDo.getUser().getCustomFields().get(0).getOptionalValue()!=null && resourceCollDo.getUser().getCustomFields().get(0).getOptionalValue().equalsIgnoreCase("true"))
			{
			relatedUserName.getElement().getStyle().setColor("#1076bb");
			relatedUserName.getElement().getStyle().setCursor(Cursor.POINTER);
			relatedUserName.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					MixpanelUtil.Click_Username();
					Map<String, String> params = new HashMap<String, String>();
					params.put("id", resourceCollDo.getUser().getGooruUId());
					params.put("user", resourceCollDo.getUser().getUsername());
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.PROFILE_PAGE, params, true);
				}
			});
			}
			
		;
		}
		if(resourceCollDo.getUser()!=null)
		{
			creatorImage.setUrl(resourceCollDo.getUser().getProfileImageUrl());
			creatorImage.setUrl(resourceCollDo.getUser().getProfileImageUrl());
			if(resourceCollDo.getUser().getUsername().length()>15)
			{
				resourceCollDo.getUser().setUsername(resourceCollDo.getUser().getUsername().substring(0, 11)+"...");
			}
			relatedUserName.setText(resourceCollDo.getUser().getUsername());
		}
		
		
		collectionImage.setUrl(resourceCollDo.getThumbnails().getUrl());



		relatedCollectionTitle.setText(resourceCollDo.getTitle());
		relatedCollectionTitle.getElement().setAttribute("id", resourceCollDo.getGooruOid());
		relatedCollectionTitle.getElement().getStyle().setColor("#1076bb");
		relatedCollectionTitle.getElement().getStyle().setCursor(Cursor.POINTER);
		relatedCollectionTitle.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", resourceCollDo.getGooruOid());
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION_PLAY, params, true);
			}
		});
		
		collectionImage.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				collectionImage.setUrl(DEFULT_IMAGE);
			}
		});
		creatorImage.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				creatorImage.setUrl("images/profilepage/user-profile-pic.png");
			}
		});
	}


	public Image getCollectionImage() {
		return collectionImage;
	}

	public InlineLabel getRelatedCollectionTitle() {
		return relatedCollectionTitle;
	}

	
	

}
