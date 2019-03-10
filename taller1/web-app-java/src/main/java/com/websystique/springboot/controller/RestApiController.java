package com.websystique.springboot.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.websystique.springboot.model.Artist;
import com.websystique.springboot.model.Ratings;
import com.websystique.springboot.model.RecommendationDTO;
import com.websystique.springboot.model.RecommendationExecutionDTO;
import com.websystique.springboot.model.User;
import com.websystique.springboot.repositories.ArtistRecommendationsRepository;
import com.websystique.springboot.repositories.ArtistRepository;
import com.websystique.springboot.repositories.RatingsRepository;
import com.websystique.springboot.service.RecommendationService;
import com.websystique.springboot.service.RecommendationUtil;
import com.websystique.springboot.service.UserService;
import com.websystique.springboot.util.CustomErrorType;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	UserService userService; // Service which will do all data retrieval/manipulation work

	@Autowired
	RecommendationService recommendationService;
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	RatingsRepository ratingsRepository;
	
	@Autowired
	RecommendationUtil recUtil;
	
	@Autowired
	ArtistRecommendationsRepository artistRecRepository;

	
	
	/**
	 * extrae urls y wiki desde lastfm api
	 * @return
	 */
	@RequestMapping(value = "/admin/photos/", method = RequestMethod.GET)
	public ResponseEntity<String> harvestArtistsPhotos() {
		recUtil.getArtistsPhotos(artistRepository);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	
	/**
	 * guarda caché generado en la bd con las recomendaciones item item 
	 * @return
	 */
	@RequestMapping(value = "/admin/items/", method = RequestMethod.GET)
	public ResponseEntity<String> generateSimilarItemsForItem() {
		int numberOfRecommendations = 10;
		recUtil.generateSimilarItemsForItem(
				 artistRecRepository,
				 artistRepository, recommendationService.getDataModelFromFile(),
				 numberOfRecommendations);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	
	 
	
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() {
		List<User> users = userService.findAllUsers();
		if (users.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// User------------------------------------------

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("id") long id) {
		logger.info("**********************************************");
		logger.info("Fetching User with id {}", id);
		User user = userService.findById(id);
		if (user == null) {
			logger.error("User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("User with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		logger.info("**********************************************");
		logger.info(user.toString());
		logger.info("**********************************************");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/ratings/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<RecommendationDTO>> getUserRatings(@PathVariable("id") long id) {
		logger.info("Fetching User with id {}", id);
		List<Ratings> ratings = ratingsRepository.findAllByUserIdOrderByRatingDesc(id);
		
		if (ratings == null) {
			logger.error("Ratings for User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Ratings for User with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<RecommendationDTO> recs = new ArrayList<RecommendationDTO>();
		for (Iterator iterator = ratings.iterator(); iterator.hasNext();) {
			Ratings rating = (Ratings) iterator.next();
			Artist artist =  artistRepository.findOne(rating.getArtistId()) ;
			recs.add(new RecommendationDTO(rating.getRating(), artist));
		}
		
		return new ResponseEntity<List<RecommendationDTO>>(recs, HttpStatus.OK);
	}
	
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/ratings/top5/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<RecommendationDTO>> getUserRatingsTop5(@PathVariable("id") long id) {
		logger.info("Fetching User with id {}", id);
		List<Ratings> ratings = ratingsRepository.findTop50ByUserIdOrderByRatingDesc(id);
		Collections.shuffle(ratings);
		if (ratings.size() > 5 ) {
			ratings = ratings.subList(0, 5);
		}
		
		if (ratings == null) {
			logger.error("Ratings for User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Ratings for User with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<RecommendationDTO> recs = new ArrayList<RecommendationDTO>();
		for (Iterator iterator = ratings.iterator(); iterator.hasNext();) {
			Ratings rating = (Ratings) iterator.next();
			Artist artist =  artistRepository.findOne(rating.getArtistId()) ;
			
			RecommendationExecutionDTO similar =  recommendationService.getSimilarItemsForItem(rating.getArtistId()); 
			
			RecommendationDTO dto = new RecommendationDTO(rating.getRating(), artist);
			dto.setSimilarArtists(similar);
			recs.add(dto);
		}
		
		return new ResponseEntity<List<RecommendationDTO>>(recs, HttpStatus.OK);
	}
	
	
	

	// -------------------Create a User-------------------------------------------

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		logger.info("Creating User : {}", user);

		if (userService.isUserExist(user)) {
			logger.error("Unable to create. A User with name {} already exist", user.getName());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A User with name " + user.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		userService.saveUser(user);
		this.recommendationService.refreshDatamodel();
		
		if(user.getLastfmUser() != null && !user.getLastfmUser().trim().equals("")) {
			this.recommendationService.getTopArtistsForLastfmUser(user);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	// ------------------- Update a User
	// ------------------------------------------------
	/**
	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		logger.info("Updating User with id {}", id);

		User currentUser = userService.findById(id);

		if (currentUser == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
	

		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}
	*/

	// ------------------- Delete a User-----------------------------------------
	/*
	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		User user = userService.findById(id);
		if (user == null) {
			logger.error("Unable to delete. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
	*/

	// ------------------- Delete All Users-----------------------------
	/*
	@RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		logger.info("Deleting All Users");

		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
	*/

	/**
	 * 
	 * @param similarityAlgorithm
	 * @param neighbors
	 * @param userID
	 * @param numberOfRecommendations
	 * @return
	 */
	// http://www.baeldung.com/spring-requestmapping
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/recommendation/user", 
			params = { "similarityAlgorithm", "neighbors", "userID", "numberOfRecommendations" }, 
			method = RequestMethod.GET)
	public ResponseEntity<RecommendationExecutionDTO> getRecommendationForUser(
			@RequestParam(value = "similarityAlgorithm") char similarityAlgorithm,
			@RequestParam(value = "neighbors") Integer neighbors, 
			@RequestParam(value = "userID") Long userID, 
			@RequestParam(value = "numberOfRecommendations") Integer numberOfRecommendations ) {
		RecommendationExecutionDTO result;
		try {
			result = recommendationService.getUserRecommendations(similarityAlgorithm, 
					neighbors, userID, numberOfRecommendations);
		} catch (TasteException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity(new CustomErrorType(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<RecommendationExecutionDTO>(result, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/similar", 
	params = { "similarityAlgorithm", "neighbors", "userID", "numberOfRecommendations" }, 
	method = RequestMethod.GET)
	public ResponseEntity<RecommendationExecutionDTO> getMostSimilarUsers(
			@RequestParam(value = "similarityAlgorithm") char similarityAlgorithm,
			@RequestParam(value = "neighbors") Integer neighbors, 
			@RequestParam(value = "userID") Long userID, 
			@RequestParam(value = "numberOfRecommendations") Integer numberOfRecommendations ) {
		logger.info("Fetching User with id {}", userID);

		RecommendationExecutionDTO result;
		try {
			result  = recommendationService.mostSimilarUsers( similarityAlgorithm, 
					 neighbors,  userID,numberOfRecommendations);
		} catch (TasteException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity(new CustomErrorType(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (result == null) {
			logger.error("Ratings for User with id {} not found.", userID);
			return new ResponseEntity(new CustomErrorType("Ratings for User with id " + userID + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RecommendationExecutionDTO>(result, HttpStatus.OK);
	}
	
	
	
	
	/**
	 * 
	 * @param userID
	 * @param itemID
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/recommendation/setPreference", 
			params = { "userID", "itemID", "value" }, 
			method = RequestMethod.GET)
	public ResponseEntity<String> setRecommendationForUser(
			@RequestParam(value = "userID") Long userID,
			@RequestParam(value = "itemID") Long itemID, 
			@RequestParam(value = "value") Float value) {
		try {
			//recommendationService.setPreferenceUsingMahout(userID, itemID, value);
			recommendationService.setPreferenceUsingJPA(userID, itemID, value);
		} catch (TasteException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity(new CustomErrorType(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/artist/", method = RequestMethod.POST)
	public ResponseEntity<Artist> createArtist(@RequestBody Artist artist, UriComponentsBuilder ucBuilder) {
		logger.info("Creating artist : {}", artist);

		if (this.artistRepository.findOneByName(artist.getName()) != null ) {
			logger.error("Unable to create. An artist with name {} already exist", artist.getName());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A User with name " + artist.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		
		if (this.artistRepository.findOneByGid(artist.getGid()) != null ) {
			logger.error("Unable to create. An artist with gid {} already exist", artist.getGid());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A User with gid " + artist.getGid() + " already exist."),
					HttpStatus.CONFLICT);
		}
		
		artistRepository.save(artist);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(artist.getId()).toUri());
		return new ResponseEntity<Artist>(artist, HttpStatus.CREATED);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/artist", 
			params = { "id" },  
			method = RequestMethod.GET)
	public ResponseEntity<Artist> getItem(
			@RequestParam(value = "id") Long id) {
		Artist result;
		try {
			result = this.artistRepository.findOne(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity(new CustomErrorType(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Artist>(result, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/artist/random/", 
			method = RequestMethod.GET)
	public ResponseEntity<List<Artist>> getRandomItem() {
		List<Artist> randomItems;
		try {
			randomItems = this.artistRepository.find50Random();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity(new CustomErrorType(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Artist>>(randomItems, HttpStatus.OK);
	}
	
	/**
	 * Obtiene los artistas similares 
	 * 
	 * @param userID
	 * @param itemID
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/artist/similar", 
			params = { "id" },  
			method = RequestMethod.GET)
	public ResponseEntity<RecommendationExecutionDTO> getSimilarItemsForItem(
			@RequestParam(value = "id") Long itemID) {
		RecommendationExecutionDTO result;
		try {
			result = recommendationService.getSimilarItemsForItem(itemID); 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity(new CustomErrorType(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<RecommendationExecutionDTO>(result, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/artist/popular", 	method = RequestMethod.GET)
	public ResponseEntity<List<Artist> > getPopularArtists() {
		List<Artist>  result;
		try {
			result = this.artistRepository.findTop200ByTotalUsersNotNullOrderByTotalUsersDesc();
			Collections.shuffle(result);
			result = result.subList(0, 50);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity(new CustomErrorType(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Artist> >(result, HttpStatus.OK);
	}

}