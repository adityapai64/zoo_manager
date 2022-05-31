package com.lf08_project.zoo_manager.api.client;

import java.util.List;

public class ShowBirdsResultCO extends DefaultResultCO {
	private List<BirdCO> birdList;

	public List<BirdCO> getBirdList() {
		return birdList;
	}

	public void setBirdList(List<BirdCO> birdList) {
		this.birdList = birdList;
	}
}
