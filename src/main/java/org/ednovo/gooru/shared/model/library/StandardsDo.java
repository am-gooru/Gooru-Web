package org.ednovo.gooru.shared.model.library;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StandardsDo implements Serializable {
	
	private static final long serialVersionUID = 2411080367742513414L;
	private String code;
	private ArrayList<StandardCourseDo> data;
	
	public StandardsDo(){}

	/** 
	 * This method is to get the code
	 */
	public String getCode() {
		return code;
	}

	/** 
	 * This method is to set the code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<StandardCourseDo> getData() {
		return data;
	}

	public void setData(ArrayList<StandardCourseDo> data) {
		this.data = data;
	}


}