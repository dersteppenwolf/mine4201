package com.websystique.springboot.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.websystique.springboot.model.Artist;
import com.websystique.springboot.model.ArtistRecommendations;
import com.websystique.springboot.repositories.ArtistRecommendationsRepository;
import com.websystique.springboot.repositories.ArtistRepository;

import de.umass.lastfm.ImageSize;

/**
 * Se separó en otra clase para poder hacer la ejecución asincrónica
 * 
 * @author juanmendez
 *
 */
@Service
public class RecommendationUtil {

	static final Logger LOG = LoggerFactory.getLogger(RecommendationUtil.class);

	public static final String lastfmApiKey = "b25b959554ed76058ac220b7b2e0a026"; // this is the key used in the Last.fm
																					// API examples

	@Async
	public void getArtistsPhotos(ArtistRepository artistRepository) {
		// List<Artist> artists =
		// artistRepository.findTop50ByTotalUsersNotNullAndPhotoUrlNullOrderByTotalUsersDesc();
		List<Artist> artists = artistRepository.findTop500ByTotalUsersNotNullAndWikiNullOrderByTotalUsersDesc();
		for (Iterator iterator = artists.iterator(); iterator.hasNext();) {
			Artist artist = (Artist) iterator.next();
			LOG.debug(artist.toString());
			de.umass.lastfm.Artist lastfmArtist = de.umass.lastfm.Artist.getInfo(artist.getGid(), lastfmApiKey);
			if (lastfmArtist != null) {
				String imageUrl = lastfmArtist.getImageURL(ImageSize.LARGE);
				// LOG.debug(lastfmArtist.toString());
				// LOG.debug(lastfmArtist.getWikiSummary());
				LOG.debug(imageUrl);
				artist.setPhotoUrl(imageUrl);
				artist.setWiki(lastfmArtist.getWikiSummary());
			} else {
				// imagen no disponible
				artist.setPhotoUrl("https://pbs.twimg.com/profile_images/600060188872155136/st4Sp6Aw.jpg");
				artist.setWiki("Not Avaailable");
			}

			artistRepository.save(artist);
		}
	}

	/**
	 * generar recomendaciones para items
	 * 
	 * @param numberOfRecommendations
	 * @throws TasteException
	 */
	@Async
	public void generateSimilarItemsForItem(ArtistRecommendationsRepository artistRecRepository,
			ArtistRepository artistRepository, DataModel dataModelFromFile, int numberOfRecommendations) {
		LOG.debug("*********************************************************************");
		LOG.debug("generateSimilarItemsForItem");
		ItemSimilarity similarity;
		try {
			// cosine
			similarity = new UncenteredCosineSimilarity(dataModelFromFile);
			GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModelFromFile, similarity);

			// List<Artist> artists =
			// artistRepository.findTop5ByTotalUsersNotNullAndLastRecommendedAtNullOrderByTotalUsersDesc();
			List<Artist> artists = artistRepository
					.findTop1000ByTotalUsersNotNullAndLastRecommendedAtNullOrderByTotalUsersDesc();

			for (Iterator iterator = artists.iterator(); iterator.hasNext();) {
				LOG.debug("*********************************************************************");
				LOG.debug("*********************************************************************");
				Artist artist = (Artist) iterator.next();
				try {
					// getItemRecommendations(ALGORITHM_JACCARD, artist.getId(),
					// numberOfRecommendations);
					getRecommendationsForItem(artistRecRepository, artistRepository, recommender, artist.getId(),
							numberOfRecommendations);
					artist.setLastRecommendedAt(new Date());
					if (artist.getName() == null) {
						artist.setName("Not Found");
					}
					artistRepository.saveAndFlush(artist);
				} catch (TasteException e) {
					LOG.error(e.getMessage(), e);

				} catch (Exception e) {
					LOG.debug(artist.toString());
					LOG.error(e.getMessage(), e);
					throw e;
				}
				LOG.debug("*********************************************************************");
				LOG.debug("*********************************************************************");
			}
		} catch (TasteException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	private void getRecommendationsForItem(ArtistRecommendationsRepository artistRecRepository,
			ArtistRepository artistRepository, ItemBasedRecommender recommender, long itemId,
			int numberOfRecommendations) throws TasteException {
		LOG.debug("****************************************");
		LOG.debug("getRecommendationsForItem - begin");
		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		LOG.debug("\t itemId :  " + itemId);
		LOG.debug("\t Item : " + artistRepository.findOne(itemId));
		LOG.debug("\t numberOfRecommendations :  " + numberOfRecommendations);
		LOG.debug("recommend - begin");

		List<RecommendedItem> recommendations = recommender.mostSimilarItems(itemId, numberOfRecommendations);
		LOG.debug("recommend - end");
		for (RecommendedItem recommendation : recommendations) {
			long recommendedItemID = recommendation.getItemID();
			Artist recommendedArtist = artistRepository.findOne(recommendedItemID);
			LOG.debug(recommendation.toString());
			LOG.debug(recommendedArtist.toString());

			ArtistRecommendations ar = new ArtistRecommendations();
			ar.setArtistId(itemId);
			ar.setRelatedArtistId(recommendedItemID);
			ar.setValue(recommendation.getValue());

			artistRecRepository.save(ar);
		}
		LOG.debug("Finished calculating {}", stopwatch);
		LOG.debug("****************************************");
	}

}
