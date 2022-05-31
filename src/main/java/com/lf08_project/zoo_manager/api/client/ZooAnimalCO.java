package com.lf08_project.zoo_manager.api.client;

public abstract class ZooAnimalCO {
	private Integer id;
	private Integer managerId;
	private String name;
	private String type;
	private String subType;
	private String breed;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	public String getSubType() {
		return subType;
		
	}
	public void setSubType(String subType) {
		this.subType = subType;
		
	}
	
}
