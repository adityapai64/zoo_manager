package com.lf08_project.zoo_manager.api.boundary;

import com.lf08_project.zoo_manager.api.client.AnimalCO;
import com.lf08_project.zoo_manager.api.client.BirdCO;
import com.lf08_project.zoo_manager.api.client.DefaultResultCO;
import com.lf08_project.zoo_manager.api.client.IDedRequest;
import com.lf08_project.zoo_manager.api.client.ManagerCO;
import com.lf08_project.zoo_manager.api.client.ShowAnimalsResultCO;
import com.lf08_project.zoo_manager.api.client.ShowBirdsResultCO;
import com.lf08_project.zoo_manager.api.client.ShowManagersResultCO;

public interface RestInterface {
	public ShowManagersResultCO showAllManagers ();
	public ShowAnimalsResultCO showAllAnimals ();
	public ShowBirdsResultCO showAllBirds ();
	public ManagerCO searchManager(IDedRequest request);
	public AnimalCO searchAnimal(IDedRequest request);
	public BirdCO searchBird (IDedRequest request);
	public DefaultResultCO addManager (ManagerCO request);
	public DefaultResultCO addAnimal (AnimalCO request);
	public DefaultResultCO addBird (BirdCO request);
	public DefaultResultCO updateManager(ManagerCO request);
	public DefaultResultCO updateAnimal(AnimalCO request);
	public DefaultResultCO updateBird (BirdCO request);
	public DefaultResultCO deleteManager (IDedRequest request);
	public DefaultResultCO deleteAnimal	(IDedRequest request);
	public DefaultResultCO deleteBird (IDedRequest request);
}
