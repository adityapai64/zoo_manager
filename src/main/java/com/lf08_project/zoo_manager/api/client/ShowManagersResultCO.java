package com.lf08_project.zoo_manager.api.client;

import java.util.List;

public class ShowManagersResultCO extends DefaultResultCO {
	private List<ManagerCO> managerList;

	public List<ManagerCO> getManagerList() {
		return managerList;
		
	}

	public void setManagerList(List<ManagerCO> managerList) {
		this.managerList = managerList;
		
	}
}
