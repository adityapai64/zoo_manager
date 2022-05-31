package com.lf08_project.zoo_manager.api.client;

import java.util.List;

public class ManagerCO {
	private Integer managerId;
	private String name;
	private List<ZooAnimalCO> zooAnimalsList;
	
	public Integer getManagerId() {
		return managerId;
	}
	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ZooAnimalCO> getZooAnimalsList() {
		return zooAnimalsList;
	}
	public void setZooAnimalsList(List<ZooAnimalCO> zooAnimalsList) {
		this.zooAnimalsList = zooAnimalsList;
	}
}
