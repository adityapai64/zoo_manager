package com.lf08_project.zoo_manager.api.boundary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.lf08_project.zoo_manager.api.client.AnimalCO;
import com.lf08_project.zoo_manager.api.client.BirdCO;
import com.lf08_project.zoo_manager.api.client.DefaultResultCO;
import com.lf08_project.zoo_manager.api.client.IDedRequest;
import com.lf08_project.zoo_manager.api.client.ManagerCO;
import com.lf08_project.zoo_manager.api.client.ShowAnimalsResultCO;
import com.lf08_project.zoo_manager.api.client.ShowBirdsResultCO;
import com.lf08_project.zoo_manager.api.client.ShowManagersResultCO;
import com.lf08_project.zoo_manager.api.client.ZooAnimalCO;





@Stateless
@Path("zooManager")
@Consumes("application/json")
@Produces("application/json")
public class ZooManagerController implements RestInterface {
	@Resource(lookup = "java:jboss/datasources/PostgresDS") 
	private DataSource ds;
	
	
	private static final String SHOW_ALL_MANAGERS = "SELECT * FROM zooManagers "
			+ "FULL JOIN zooAnimals ON (zooManagers.manager_id = zooAnimals.manager_id)";
	private static final String SHOW_ALL_ANIMALS = "SELECT * FROM zooAnimals WHERE TYPE = ?";
	private static final String SHOW_ALL_BIRDS = "SELECT * FROM zooAnimals WHERE TYPE = ?";
	private static final String SEARCH_MANAGER = "SELECT * FROM zooManagers FULL JOIN zooAnimals "
			+ "ON (zooManagers.manager_id = zooAnimals.manager_id) WHERE zooAnimals.manager_id = ?";
	private static final String SEARCH_ZOO_ANIMAL = "SELECT * FROM zooAnimals WHERE zoo_animal_id = ?";
	private static final String ADD_MANAGER = "INSERT INTO zooManagers (NAME) VALUES (?)";
	private static final String ADD_ANIMAL = "INSERT INTO zooAnimals ("
			+ "zoo_animal_id, manager_id, animal_name, "
			+ "type, sub_type, breed) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String ADD_BIRD = "INSERT INTO zooAnimals ("
			+ "zoo_animal_id, manager_id, animal_name, "
			+ "type, sub_type, breed, flightless) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_MANAGER = "UPDATE zooManagers SET name = ? "
			+ "WHERE manager_id = ?";
	private static final String UPDATE_ANIMAL = "UPDATE zooAnimals SET zoo_animal_id = ?, "
			+ "manager_id = ?, animal_name = ?, type = ?, sub_type = ?, breed = ? "
			+ "WHERE zoo_animal_id = ?" ;
	private static final String UPDATE_BIRD = "UPDATE zooAnimals SET zoo_animal_id = ?, "
			+ "manager_id = ?, animal_name = ?, type = ?, sub_type = ?, breed = ?, flightless = ? "
			+ "WHERE zoo_animal_id = ?" ;
	private static final String DELETE_MANAGER = "DELETE FROM zooManagers WHERE manager_id = ?";
	private static final String DELETE_ANIMAL = "DELETE FROM zooAnimals WHERE zoo_animal_id = ?";
	
	private void close(Connection con, PreparedStatement ps, ResultSet rs) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			
		} catch (SQLException e) {
			
		}
	}
	
	private void rollback (Connection con) {
			try {
				if (con != null) {
				con.rollback();
				}
			} catch (SQLException e) {
				
			}
	}
	
	@Override
	@Path("showAllManagers")
	@GET
	public ShowManagersResultCO showAllManagers() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ShowManagersResultCO result = new ShowManagersResultCO();
		List <ManagerCO> managerList = new ArrayList<>();
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(SHOW_ALL_MANAGERS);
			rs = ps.executeQuery();
			
			Map<Integer, ManagerCO> map = new HashMap<>();
			while (rs.next()) {
				Integer managerId = rs.getInt("manager_id");
				ManagerCO manager = map.get(managerId);
				if (manager == null) {
					manager = new ManagerCO();
					manager.setName(rs.getString("name"));
					manager.setManagerId(rs.getInt("manager_id"));
					manager.setZooAnimalsList(new ArrayList<>());
					map.put(managerId, manager);
					managerList.add(manager);
				}
				if (rs.getString("type") != null && rs.getString("type").equals("Animal")) {
					AnimalCO animal = new AnimalCO();
					animal.setId(rs.getInt("zoo_animal_id"));
					animal.setName(rs.getString("animal_name"));
					animal.setType(rs.getString("type"));
					animal.setSubType(rs.getString("sub_type"));
					animal.setBreed(rs.getString("breed"));
					manager.getZooAnimalsList().add(animal);
				} else if (rs.getString("type") != null) {
					BirdCO bird = new BirdCO();
					bird.setId(rs.getInt("zoo_animal_id"));
					bird.setName(rs.getString("animal_name"));
					bird.setType(rs.getString("type"));
					bird.setSubType(rs.getString("sub_type"));
					bird.setBreed(rs.getString("breed"));
					bird.setFlightless(rs.getBoolean("flightless"));
					manager.getZooAnimalsList().add(bird);
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con, ps, rs);
		}
		result.setManagerList(managerList); 
		return result;
	}
	
	@Override
	@Path("showAllAnimals")
	@GET
	public ShowAnimalsResultCO showAllAnimals() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ShowAnimalsResultCO result = new ShowAnimalsResultCO();
		List<AnimalCO> animalList = new ArrayList<>();
		
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(SHOW_ALL_ANIMALS);
			ps.setString(1, "Animal");
			rs = ps.executeQuery();
			
			while (rs.next()) {
				AnimalCO animal = new AnimalCO();
				animal.setId(rs.getInt("zoo_animal_id"));
				animal.setManagerId(rs.getInt("manager_id"));
				animal.setName(rs.getString("animal_name"));
				animal.setType(rs.getString("type"));
				animal.setSubType(rs.getString("sub_type"));
				animal.setBreed(rs.getString("breed"));
				animalList.add(animal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con, ps, rs);
		}
		result.setAnimalList(animalList);
		return result;
	}

	@Override
	@Path("showAllBirds")
	@GET
	public ShowBirdsResultCO showAllBirds() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ShowBirdsResultCO result = new ShowBirdsResultCO();
		List<BirdCO> birdList = new ArrayList<>();
		
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(SHOW_ALL_BIRDS);
			ps.setString(1, "Bird");
			rs = ps.executeQuery();
			
			while (rs.next()) {
				
				BirdCO bird = new BirdCO();
				bird.setId(rs.getInt("zoo_animal_id"));
				bird.setManagerId(rs.getInt("manager_id"));
				bird.setName(rs.getString("animal_name"));
				bird.setType(rs.getString("type"));
				bird.setSubType(rs.getString("sub_type"));
				bird.setBreed(rs.getString("breed"));
				bird.setFlightless(rs.getBoolean("flightless"));
				birdList.add(bird);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con, ps, rs);
		}
		result.setBirdList(birdList);
		return result;
	}

	@Override
	@Path("searchManager")
	@POST
	public ManagerCO searchManager(IDedRequest request) {
 		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ManagerCO result = null;
		List<ZooAnimalCO> animalList = new ArrayList<>();
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(SEARCH_MANAGER);
			ps.setInt(1, request.getId());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				if (result == null) {
					result = new ManagerCO();
					result.setManagerId(rs.getInt("manager_id"));
					result.setName(rs.getString("name"));
				}
				if (rs.getString("type") != null && rs.getString("type").equals("Animal")) {
					AnimalCO animal = new AnimalCO();
					animal.setId(rs.getInt("zoo_animal_id"));
					animal.setManagerId(rs.getInt("manager_id"));
					animal.setName(rs.getString("animal_name"));
					animal.setType(rs.getString("type"));
					animal.setSubType(rs.getString("sub_type"));
					animal.setBreed(rs.getString("breed"));
					animalList.add(animal);
				} else if (rs.getString("type") != null) {
					BirdCO bird = new BirdCO();
					bird.setId(rs.getInt("zoo_animal_id"));
					bird.setManagerId(rs.getInt("manager_id"));
					bird.setName(rs.getString("animal_name"));
					bird.setType(rs.getString("type"));
					bird.setSubType(rs.getString("sub_type"));
					bird.setBreed(rs.getString("breed"));
					bird.setFlightless(rs.getBoolean("flightless"));
					animalList.add(bird);
				}
			}
			result.setZooAnimalsList(animalList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con, ps, rs);
		}
		return result;
	}

	@Override
	@Path("searchZooAnimal")
	@POST
	public ZooAnimalCO searchAnimal(IDedRequest request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ZooAnimalCO result = null;
		int index = 1;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(SEARCH_ZOO_ANIMAL);
			ps.setInt(index++, request.getId());
			rs = ps.executeQuery();
			if (rs.next() && rs.getString("type").equals("Animal")) {
				result = new AnimalCO();
				result.setId(rs.getInt("zoo_animal_id"));
				result.setManagerId(rs.getInt("manager_id"));
				result.setName(rs.getString("animal_name"));
				result.setType(rs.getString("type"));
				result.setSubType(rs.getString("sub_type"));
				result.setBreed(rs.getString("breed"));
			} else if (rs.next()) {
				result = new BirdCO();
				result.setId(rs.getInt("zoo_animal_id"));
				result.setManagerId(rs.getInt("manager_id"));
				result.setName(rs.getString("animal_name"));
				result.setType(rs.getString("type"));
				result.setSubType(rs.getString("sub_type"));
				result.setBreed(rs.getString("breed"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con, ps, rs);
		}
		return result;
	}

//	@Override
//	@Path("searchBird")
//	@POST
//	public BirdCO searchBird(IDedRequest request) {
//		Connection con = null;
//		ResultSet rs = null;
//		PreparedStatement ps = null;
//		BirdCO bird = new BirdCO();
//		String query = SHOW_ALL_ANIMALS + " WHERE zoo_animal_id = ?";
//		int index = 1;
//		try {
//			con = ds.getConnection();
//			ps = con.prepareStatement(query);
//			ps.setString(index++, "Bird");
//			ps.setInt(index++, request.getId());
//			rs = ps.executeQuery();
//			bird.setId(rs.getInt("zoo_animal_id"));
//			bird.setManagerId(rs.getInt("manager_id"));
//			bird.setName(rs.getString("animal_name"));
//			bird.setType(rs.getString("type"));
//			bird.setSubType(rs.getString("sub_type"));
//			bird.setBreed(rs.getString("breed"));
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			close(con, ps, rs);
//		}
//		return bird;
//	}

	@Override
	@Path("addManager")
	@POST
	public DefaultResultCO addManager(ManagerCO request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(ADD_MANAGER);
			ps.setString(1, request.getName());
			ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con);
			e.printStackTrace();
			result.addError("Manager could not be added");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty()) {
			result.setResult("Manager added successfully!");
		}
		return result;
	}

	@Override
	@Path("addAnimal")
	@POST
	public DefaultResultCO addAnimal(AnimalCO request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		int index = 1;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(ADD_ANIMAL);
			ps.setObject(index++, request.getId());
			ps.setObject(index++, request.getManagerId());
			ps.setObject(index++, request.getName());
			ps.setObject(index++, request.getType());
			ps.setObject(index++, request.getSubType());
			ps.setObject(index++, request.getBreed());
			ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con); 
			e.printStackTrace();
			result.addError("Animal could not be added");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty()) {
			result.setResult("Animal added successfully!");
		}
		return result;
	}

	@Override
	@Path("addBird")
	@POST
	public DefaultResultCO addBird(BirdCO request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		int index = 1;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(ADD_BIRD);
			ps.setObject(index++, request.getId());
			ps.setObject(index++, request.getManagerId());
			ps.setObject(index++, request.getName());
			ps.setObject(index++, request.getType());
			ps.setObject(index++, request.getSubType());
			ps.setObject(index++, request.getBreed());
			ps.setObject(index++, request.getFlightless());
			ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con);
			e.printStackTrace();
			result.addError("Animal could not be added");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty()) {
			result.setResult("Animal added successfully!");
		}
		return result;
	}

	@Override
	@Path("updateManager")
	@POST
	public DefaultResultCO updateManager(ManagerCO request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		int index = 1;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(UPDATE_MANAGER);
			ps.setObject(index++, request.getName());
			ps.setObject(index++, request.getManagerId());
			ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con);
			e.printStackTrace();
			result.addError("Manager could not be updated");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty()) {
			result.setResult("Manager updated successfully!");
		}
		return result;
	}

	@Override
	@Path("updateAnimal")
	@POST
	public DefaultResultCO updateAnimal(AnimalCO request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		int index = 1;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(UPDATE_ANIMAL);
			ps.setObject(index++, request.getId());
			ps.setObject(index++, request.getManagerId());
			ps.setObject(index++, request.getName());
			ps.setObject(index++, request.getType());
			ps.setObject(index++, request.getSubType());
			ps.setObject(index++, request.getBreed());
			ps.setObject(index++, request.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con);
			e.printStackTrace();
			result.addError("Manager could not be updated");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty()) {
			result.setResult("Animal updated successfully!");
		}
		return result;
	}

	@Override
	@Path("updateBird")
	@POST
	public DefaultResultCO updateBird(BirdCO request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		int index = 1;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(UPDATE_BIRD);
			ps.setObject(index++, request.getId());
			ps.setObject(index++, request.getManagerId());
			ps.setObject(index++, request.getName());
			ps.setObject(index++, request.getType());
			ps.setObject(index++, request.getSubType());
			ps.setObject(index++, request.getBreed());
			ps.setObject(index++, request.getFlightless());
			ps.setObject(index++, request.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con);
			e.printStackTrace();
			result.addError("Manager could not be updated");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty()) {
			result.setResult("Manager updated successfully!");
		}
		return result;
	}

	@Override
	@Path("deleteManager")
	@POST
	public DefaultResultCO deleteManager(IDedRequest request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		int index = 1;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(DELETE_MANAGER);
			ps.setObject(index++, request.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con);
			e.printStackTrace();
			result.addError("Manager could not be deleted");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty()) {
			result.setResult("Manager deleted successfully!");
		}
		return result;
	}

	@Override
	@Path("deleteAnimal")
	@POST
	public DefaultResultCO deleteAnimal(IDedRequest request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		int index = 1;
		int res = 0;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(DELETE_ANIMAL);
			ps.setInt(index++, request.getId());
			res = ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con);
			e.printStackTrace();
			result.addError("Animal could not be deleted");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty() && res > 0) {
			result.setResult("Animal deleted successfully!");
		} else {
			result.setResult("Something went wrong with the deletion.");
		}
		return result;
	}

	@Override
	@Path("deleteBird")
	@POST
	public DefaultResultCO deleteBird(IDedRequest request) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		DefaultResultCO result = new DefaultResultCO();
		int index = 1;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(DELETE_ANIMAL);
			ps.setObject(index++, request.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			rollback(con);
			e.printStackTrace();
			result.addError("Bird could not be deleted");
		} finally {
			close(con, ps, rs);
		}
		if (result.getErrorList() == null || result.getErrorList().isEmpty()) {
			result.setResult("Bird deleted successfully!");
		}
		return result;
	}
	
}
