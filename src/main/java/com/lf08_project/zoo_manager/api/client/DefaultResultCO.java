package com.lf08_project.zoo_manager.api.client;

import java.util.ArrayList;
import java.util.List;

public class DefaultResultCO {
	private String result;
	private List<String> errorList;

	public List<String> getErrorList() {
		return errorList;
		
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
		
	}
	
	public void addError(String errorMessage) {
		if (errorList == null) {
			errorList = new ArrayList<>();
		}
		errorList.add(errorMessage);
	}

	public String getResult() {
		return result;
		
	}

	public void setResult(String result) {
		this.result = result;
		
	}
}
