package org.ednovo.gooru.client.mvp.gsearch.ViewMorePopup;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.uc.PPanel;
import org.ednovo.gooru.shared.model.content.ResourceCollDo;

import com.google.gwt.core.client.GWT;
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
		if(resourceCollDo.getUser().getMeta() != null && resourceCollDo.getUser().getMeta().getGrade()!=null)
		{
		gradePanel.setText(resourceCollDo.getUser().getMeta().getGrade());
		}
		creatorImage.setUrl(AppClientFactory.getLoggedInUser().getSettings().getProfileImageUrl()+resourceCollDo.getUser().getGooruUId()+".png");
		creatorImage.setUrl(resourceCollDo.getUser().getProfileImageUrl());
		collectionImage.setUrl(resourceCollDo.getThumbnails().getUrl());


		relatedUserName.setText(resourceCollDo.getUser().getUsername());
		relatedCollectionTitle.setText(resourceCollDo.getTitle());
		
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
