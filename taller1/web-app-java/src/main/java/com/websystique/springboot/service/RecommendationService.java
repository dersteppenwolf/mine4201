package com.websystique.springboot.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springboot.configuration.JpaConfiguration;
import com.websystique.springboot.model.Artist;
import com.websystique.springboot.model.ArtistRecommendations;
import com.websystique.springboot.model.Ratings;
import com.websystique.springboot.model.RecommendationDTO;
import com.websystique.springboot.model.RecommendationExecutionDTO;
import com.websystique.springboot.model.User;
import com.websystique.springboot.repositories.ArtistRecommendationsRepository;
import com.websystique.springboot.repositories.ArtistRepository;
import com.websystique.springboot.repositories.RatingsRepository;
import com.websystique.springboot.repositories.UserRepository;


/**
 * https://archive.cloudera.com/cdh5/cdh/5/mahout-0.9-cdh5.5.1/mahout-integration/org/apache/mahout/cf/taste/impl/model/jdbc/PostgreSQLJDBCDataModel.html
 * https://www.ibm.com/developerworks/websphere/techjournal/1109_zegarra/1109_zegarra.html
 * 
 * @author juanmendez
 *
 */
@Transactional
@Service("recommendationService")
public class RecommendationService {

	static final Logger LOG = LoggerFactory.getLogger(RecommendationService.class);

	public static final char ALGORITHM_JACCARD = 'J';
	public static final char ALGORITHM_COSINE = 'C';
	public static final char ALGORITHM_PEARSON = 'P';
	
	

	private DataModel dataModel;
	
	private DataModel dataModelFromFile;


	



	JpaConfiguration config;
	
	ArtistRepository artistRepository;
	RatingsRepository ratingsRepository;
	ArtistRecommendationsRepository artistRecRepository;
	UserRepository userRepository;
	
	RecommendationUtil recUtil;
	
	

	/**
	 * Integración Mahout - Postgresql
	 * 
	 * @param config
	 */
	public RecommendationService(JpaConfiguration config, 
			ArtistRepository artistRepository, 
			RatingsRepository ratingsRepository, 
			ArtistRecommendationsRepository artistRecRepository, 
			UserRepository userRepository,
			RecommendationUtil recUtil) {
		LOG.debug("RecommendationService");
		this.config = config;
		this.artistRepository = artistRepository;
		this.ratingsRepository = ratingsRepository;
		this.artistRecRepository = artistRecRepository;
		this.recUtil = recUtil;
		this.userRepository =  userRepository;
		
		try {
			// create the data model object
			dataModel = new PostgreSQLJDBCDataModel(
					this.config.dataSource(), 
					"ratings_all_columns", "id_user", "id_artist",
					"rating", null);
			

			ClassLoader cl = this.getClass().getClassLoader(); 
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
			Resource[] resources = resolver.getResources("classpath*:/ratingsdb.tsv") ;
			InputStream in = resources[0].getInputStream();
			final File tempFile = File.createTempFile("tmp", ".tsv");
	        tempFile.deleteOnExit();
	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(in, out);
	        }
			this.LOG.debug(tempFile.getAbsolutePath());
			dataModelFromFile  = 		new FileDataModel(tempFile, "\t");

			// test
			int USER_ID = 1;
			int ITEM_ID = 956;
			int numberOfRecommendations = 10;
			
			
			// progresivamente va cargando las fotos 
			// (cada vez que la aplicación se reinicia carga 50
			// para no saturar el acceso al api de lastfm
			// spring boot recarga el servidor cuando el código cambia mientras el desarrollo
			//TODO
			//  recUtil.getArtistsPhotos(artistRepository);
		
			/*			
			getUserRecommendations(ALGORITHM_JACCARD, 5, USER_ID, numberOfRecommendations);
			
			//se demora mucho si se ejecuta contra la bd
			getItemRecommendations(ALGORITHM_JACCARD, ITEM_ID, numberOfRecommendations);
			*/
			
			// pre-calcula los similares para artistas (en reinicióndel servidor genera 100)
			// spring boot recarga el servidor cuando el código cambia mientras el desarrollo
			//TODO
			/*
			recUtil.generateSimilarItemsForItem(
					 artistRecRepository,
					 artistRepository,
					 dataModelFromFile,
					 numberOfRecommendations); 
			*/
			// nota con dataModel (bd) se demora más de 4 segundos
			// nota con dataModelFromFile (file) se demora menos de 1 segundos

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}
	
	/**
	 * Carga las fotos de las fm en la tabla local
	 * 
	 */
	public void getTopArtistsForLastfmUser(User localUser) {
		
		Collection<de.umass.lastfm.Artist> lasfmArtists = de.umass.lastfm.User.getTopArtists(localUser.getLastfmUser(), 
				RecommendationUtil.lastfmApiKey);
		int counter = 0;
		for (Iterator iterator = lasfmArtists.iterator(); iterator.hasNext();) {
			de.umass.lastfm.Artist lastfmArtist = (de.umass.lastfm.Artist) iterator.next();
			//LOG.debug("playcount:"+lastfmArtist.getPlaycount());
			LOG.debug(lastfmArtist.toString());
			
			Ratings rating = new Ratings();
			rating.setUserId(localUser.getId());
			rating.setTotalPlays(lastfmArtist.getPlaycount());
			
			Artist artist = artistRepository.findOneByGid(lastfmArtist.getMbid());
			if (artist == null) {
				continue;
			}
			rating.setArtistId(artist.getId());
			
			rating.setRank(++counter);
			rating.setRating(5);
			try {
				this.ratingsRepository.saveAndFlush(rating);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			
		}
		//  calcular total_plays_normalized y rating según la distribución de los datos
		this.ratingsRepository.calculateRatingsForUser(localUser.getId());
		
		// Recargar modelo de mahout en memoria con la nueva info de bd...
		this.dataModel.refresh(null);
		
	}
	
	
	/**
	 * 
	 * @param userID
	 * @param itemID
	 * @param value
	 * @throws TasteException
	 */
	public void setPreferenceUsingMahout(long userID, long itemID, float value ) throws TasteException {	
		LOG.debug("setPreference - begin");
		try {
			dataModel.setPreference( userID,  itemID,  value);
		
		} catch (TasteException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
		LOG.debug("setPreference - end");
	}
	
	public void setPreferenceUsingJPA(long userID, long itemID, float value ) throws TasteException {	
		LOG.debug("setPreference - begin");
		try {
			LOG.debug("userID - "+userID);
			LOG.debug("itemID - "+itemID);
			LOG.debug("value - "+value);
			Ratings rating = this.ratingsRepository.findOneByUserIdAndArtistId(userID, itemID);
			if( rating == null) {
				rating = new Ratings();
				rating.setUserId(userID);
				rating.setArtistId(itemID);
			}
			rating.setRating(Math.round(value));
			rating.setLastUpdatedAt(new Date());
			this.ratingsRepository.save(rating);
			
			refreshDatamodel() ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
		LOG.debug("setPreference - end");
	}
	
	/**
	 * // Recargar modelo de mahout en memoria con la nueva info de bd...
	 */
	public void refreshDatamodel() {
		this.dataModel.refresh(null);
	}
	
	
	/**
	 * 
	 * @param itemId
	 * @return
	 */
	public RecommendationExecutionDTO getSimilarItemsForItem(Long itemId) {
		LOG.debug("****************************************");
		LOG.debug("getSimilarItemsForItem : "+itemId);
		LOG.debug("****************************************");
		List<ArtistRecommendations> recommendations =  artistRecRepository.findAllByArtistIdOrderByValueDesc(itemId);
		//////////////////////////////////////////////////////////////////////
		if(recommendations == null || recommendations.isEmpty()) {
			LOG.debug("****************************************");
			LOG.debug("** Generando recs desde DB");
			LOG.debug("****************************************");
			recommendations = new ArrayList<ArtistRecommendations>();
			try {
				Artist artist = artistRepository.findOne(itemId);
				LOG.debug(artist.toString());
				ItemSimilarity 	similarity = new UncenteredCosineSimilarity(dataModel);
					GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModelFromFile, similarity);
				List<RecommendedItem> recs = recommender.mostSimilarItems(itemId, 10);
				LOG.debug("recommend - end");
				for (RecommendedItem recommendation : recs) {
					long recommendedItemID = recommendation.getItemID();
					Artist recommendedArtist = artistRepository.findOne(recommendedItemID);
					LOG.debug(recommendation.toString());
					LOG.debug(recommendedArtist.toString());

					ArtistRecommendations ar = new ArtistRecommendations();
					ar.setArtistId(itemId);
					ar.setRelatedArtistId(recommendedItemID);
					ar.setValue(recommendation.getValue());
					artistRecRepository.save(ar);
					recommendations.add(ar);
				}
				
				
				artist.setLastRecommendedAt(new Date());
				if (artist.getName() == null) {
					artist.setName("Not Found");
				}
				artistRepository.saveAndFlush(artist);
			} catch (TasteException e) {
				LOG.error(e.getMessage(), e);

			} catch (Exception e) {
				LOG.debug("Error generating recs:"+itemId);
				LOG.error(e.getMessage(), e);
			}
			LOG.debug("****************************************");
			LOG.debug("** Generando recs desde DB - END");
			LOG.debug("****************************************");
		}
		//////////////////////////////////////////////////////////////////////
		List<RecommendationDTO> recs = new ArrayList<RecommendationDTO>();
		for (ArtistRecommendations recommendation : recommendations) {
			long recommendedItem = recommendation.getRelatedArtistId();
			Artist artist =  artistRepository.findOne(recommendedItem) ;
			//LOG.debug(recommendation.toString());
			//LOG.debug(artist.toString());
			recs.add(new RecommendationDTO(recommendation.getValue(), artist));
		}
		RecommendationExecutionDTO result = new RecommendationExecutionDTO(recs);
		result.setItemId(itemId);
		return result;
		
	}

	
	/**
	 * 
	 * @param similarityAlgorithm
	 * @param neighbors
	 * @param userID
	 * @param numberOfRecommendations
	 * @return
	 * @throws TasteException
	 */
	public RecommendationExecutionDTO getUserRecommendations(char similarityAlgorithm, int neighbors, long userID,
			int numberOfRecommendations) throws TasteException {
		LOG.debug("getUserRecommendations - begin");
		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		UserSimilarity similarity = null;
		switch (similarityAlgorithm) {
		case ALGORITHM_JACCARD:
			similarity = new TanimotoCoefficientSimilarity(dataModel);
			break;
		case ALGORITHM_COSINE:
			similarity = new UncenteredCosineSimilarity(dataModel);
			break;
		case ALGORITHM_PEARSON:
			similarity = new PearsonCorrelationSimilarity(dataModel);
			break;
		default:
			LOG.error("Invalid Algorithm:" + similarityAlgorithm);
			throw new RuntimeException("Invalid Algorithm:" + similarityAlgorithm);
		}
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighbors, similarity, dataModel);
		GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
		LOG.debug("recommend - begin");
		List<RecommendedItem> recommendations = recommender.recommend(userID, numberOfRecommendations);
		LOG.debug("recommend - end");
		
		List<RecommendationDTO> recs = new ArrayList<RecommendationDTO>();
		for (RecommendedItem recommendation : recommendations) {
			long itemid = recommendation.getItemID();
			Artist artist =  artistRepository.findOne(itemid) ;
			//LOG.debug(recommendation.toString());
			//LOG.debug(artist.toString());
			recs.add(new RecommendationDTO(recommendation.getValue(), artist));
		}
		LOG.debug("getUserRecommender - end");
		LOG.debug("Finished calculating {}", stopwatch);
		RecommendationExecutionDTO result = new RecommendationExecutionDTO(stopwatch.toString(), recs);
		result.setUserId(userID);
		result.setNumberOfUserRatings(dataModel.getItemIDsFromUser(userID).size());
		result.setAlgorithm(similarityAlgorithm);
		result.setNeighbors(neighbors);
		LOG.debug("****************************************");
		LOG.debug(result.toString());
		LOG.debug("****************************************");
		return result;
	}
	
	
	
	public RecommendationExecutionDTO mostSimilarUsers(char similarityAlgorithm, int neighbors, long userID,
			int numberOfRecommendations) throws TasteException {
		LOG.debug("mostSimilarUsers - begin");
		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		UserSimilarity similarity = null;
		switch (similarityAlgorithm) {
		case ALGORITHM_JACCARD:
			similarity = new TanimotoCoefficientSimilarity(dataModel);
			break;
		case ALGORITHM_COSINE:
			similarity = new UncenteredCosineSimilarity(dataModel);
			break;
		case ALGORITHM_PEARSON:
			similarity = new PearsonCorrelationSimilarity(dataModel);
			break;
		default:
			LOG.error("Invalid Algorithm:" + similarityAlgorithm);
			throw new RuntimeException("Invalid Algorithm:" + similarityAlgorithm);
		}
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighbors, similarity, dataModel);
		GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
		LOG.debug("recommend - begin");
		long[] similarUsersIds = recommender.mostSimilarUserIDs(userID, numberOfRecommendations);
		LOG.debug("recommend - end");
		
		List<User> similarUsers = new ArrayList<User>();
		for (int i = 0; i < similarUsersIds.length; i++) {
			long userId = similarUsersIds[i];
			User user =  userRepository.findOne(userId);
			similarUsers.add(user);
		}
		LOG.debug("getUserRecommender - end");
		LOG.debug("Finished calculating {}", stopwatch);
		RecommendationExecutionDTO result = new RecommendationExecutionDTO(stopwatch.toString());
		result.setSimilarUsers(similarUsers);
		result.setUserId(userID);
		result.setNumberOfUserRatings(dataModel.getItemIDsFromUser(userID).size());
		result.setAlgorithm(similarityAlgorithm);
		result.setNeighbors(neighbors);
		LOG.debug("****************************************");
		LOG.debug(result.toString());
		LOG.debug("****************************************");
		return result;
	}

	
	
	/**
	 * Originalmente se hizo contra la bd pero era muy demorado
	 * 
	 * @param similarityAlgorithm
	 * @param itemId
	 * @param numberOfRecommendations
	 * @throws TasteException
	 */
	public void getItemRecommendations(char similarityAlgorithm, long itemId, int numberOfRecommendations)
			throws TasteException {
		LOG.debug("****************************************");
		LOG.debug("getItemRecommendations - begin");
		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		LOG.debug("\t similarityAlgorithm :  " + similarityAlgorithm);
		LOG.debug("\t itemId :  " + itemId);
		LOG.debug("\t Item : " + artistRepository.findOne(itemId) );

		LOG.debug("\t numberOfRecommendations :  " + numberOfRecommendations);

		ItemSimilarity similarity = null;
		switch (similarityAlgorithm) {
		case ALGORITHM_JACCARD:
			similarity = new TanimotoCoefficientSimilarity(dataModelFromFile);
			break;
		case ALGORITHM_COSINE:
			similarity = new UncenteredCosineSimilarity(dataModelFromFile);
			break;
		case ALGORITHM_PEARSON:
			similarity = new PearsonCorrelationSimilarity(dataModelFromFile);
			break;
		default:
			LOG.error("Invalid Algorithm:" + similarityAlgorithm);
			throw new RuntimeException("Invalid Algorithm:" + similarityAlgorithm);
		}

		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModelFromFile, similarity);
		LOG.debug("recommend - begin");
		List<RecommendedItem> recommendations = recommender.mostSimilarItems(itemId, numberOfRecommendations);
		LOG.debug("recommend - end");
		for (RecommendedItem recommendation : recommendations) {
			
			long recommendedItemID = recommendation.getItemID();
			Artist recommendedArtist =  artistRepository.findOne(recommendedItemID) ;
			LOG.debug(recommendation.toString());
			LOG.debug(recommendedArtist.toString());
			
			ArtistRecommendations ar = new  ArtistRecommendations();
			ar.setArtistId(itemId);
			ar.setRelatedArtistId(recommendedItemID);
			ar.setValue(recommendation.getValue());
			
			artistRecRepository.save(ar);
		}
		LOG.debug("Finished calculating {}", stopwatch);
		LOG.debug("****************************************");
	}
	
	
	public DataModel getDataModelFromFile() {
		return dataModelFromFile;
	}
	
	
	
	
	
	

}
