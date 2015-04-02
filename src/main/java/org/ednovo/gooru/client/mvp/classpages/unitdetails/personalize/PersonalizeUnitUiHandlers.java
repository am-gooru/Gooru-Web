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
package org.ednovo.gooru.client.mvp.classpages.unitdetails.personalize;

import org.ednovo.gooru.client.gin.BaseUiHandlers;
import org.ednovo.gooru.shared.model.content.ClasspageDo;

public interface PersonalizeUnitUiHandlers extends BaseUiHandlers {

	/**
	 * @function getStudentsList
	 * 
	 * @created_date : 09-Sep-2014
	 * 
	 * @description
	 * 
	 * 
	 * @param offSet
	 * @param limit
	 * @param type
	 * @param classCode
	 * 
	 * @return : void
	 * 
	 * @throws : <Mentioned if any exceptions>
	 * 
	 * 
	 * 
	 * 
	 */

	void getStudentsList(int offSet, int limit, String type, String classCode);

	/**
	 * @function setClasspageData
	 * 
	 * @created_date : 09-Sep-2014
	 * 
	 * @description
	 * 
	 * 
	 * @param classpageDo
	 * 
	 * @return : void
	 * 
	 * @throws : <Mentioned if any exceptions>
	 * 
	 * 
	 * 
	 * 
	 */

	void setClasspageData(ClasspageDo classpageDo);

	/**
	 * @function getList
	 * 
	 * @created_date : 09-Sep-2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 * 
	 * @throws : <Mentioned if any exceptions>
	 * 
	 * 
	 * 
	 * 
	 */

	void getList();

	/**
	 * @function getClasspageDo
	 * 
	 * @created_date : 18-Sep-2014
	 * 
	 * @description
	 * 
	 * 
	 * @return
	 * 
	 * @return : ClasspageDo
	 * 
	 * @throws : <Mentioned if any exceptions>
	 * 
	 * 
	 * 
	 * 
	 */

	ClasspageDo getClasspageDo();
}
