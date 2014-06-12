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
package org.ednovo.gooru.client.mvp.classpages.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;


public interface AssignmentProgressCBundle extends ClientBundle {

	static final AssignmentProgressCBundle INSTANCE = GWT
			.create(AssignmentProgressCBundle.class);

	public interface AssignmentProgressCss extends CssResource {
		String circle();
		String line();
		String container();
		String circleContainer();
		String leftMargin();
		String rightMargin();
		String assignmentNumbers();
		String viewedCircle();
		
		String profileVisiblityPopup();
		String bubblearrowstyle();
		String standardsinner();
		String standardsPopup();
		String userLibraryPopup();
		String title();
		String move();
		String myFolderCollectionCategoryDiv();

		String myFolderCollectionCategoryInputDiv();

		String myFolderCollectionCategoryDivText();

		String resourceCategoryLabel();

		String myFolderCollectionArrowleftContainer();
		String myFolderCollectionPopupSprite();

		String myFolderCollectionArrowleft();
		String moveContainer();
		String reorderLabelContainer();
		String myFolderCollectionFolderDropdown();
		String myFolderCollectionFolderVideoTitle();
		String bluecircle();
		String greencircle();
		String assignmentCollectionTitle();
		String headerLeft();
		String dueDataIcon();
		String headerDueDate();
		String directionHeading();
		String directionDesc();
		String studyPopup();
		String assignmentStudyPopup();
		String assignmentStudyPopupArrow();
		
	}

	@Source("AssignmentProgress.css")
	AssignmentProgressCss css();

}
