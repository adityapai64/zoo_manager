package com.lf08_project.zoo_manager.api.client;

import java.util.List;

public class ShowAnimalsResultCO extends DefaultResultCO {
	private List<AnimalCO> animalList;

	public List<AnimalCO> getAnimalList() {
		return animalList;
		
	}

	public void setAnimalList(List<AnimalCO> animalList) {
		this.animalList = animalList;
		
	}
}
