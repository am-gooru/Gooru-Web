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
package org.ednovo.gooru.shared.model.content;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.user.UserDoMorePeople;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ContentDoMorePeople implements Serializable {

	private static final long serialVersionUID = -4726217674900854767L;
	
	private String gooruOid;
	private UserDoMorePeople user;
	private UserDoMorePeople creator;
	private Set<CodeDo> taxonomySet = new HashSet<CodeDo>();
	private String sharing;
	
	public ContentDoMorePeople(){}

	public String getGooruOid() {
		return gooruOid;
	}

	public void setGooruOid(String gooruOid) {
		this.gooruOid = gooruOid;
	}

	public UserDoMorePeople getUser() {
		return user;
	}

	public void setUser(UserDoMorePeople user) {
		this.user = user;
	}

	public UserDoMorePeople getCreator() {
		return creator;
	}

	public void setCreator(UserDoMorePeople creator) {
		this.creator = creator;
	}

	public void setTaxonomySet(Set<CodeDo> taxonomySet) {
		this.taxonomySet = taxonomySet;
	}

	public Set<CodeDo> getTaxonomySet() {
		return taxonomySet;
	}

	public void setSharing(String sharing) {
		this.sharing = sharing;
	}

	public String getSharing() {
		return sharing;
	}

}
