package co.uniandes.mine4201.grupo07.taller01;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.uniandes.mine4201.grupo07.taller01.util.music.MusicItemInfoLoader;
import co.uniandes.mine4201.grupo07.taller01.util.music.MusicItemInformation;

public class MusicRecommender {
	
	static final Logger LOG = LoggerFactory.getLogger(MusicRecommender.class);


	public static void main(String[] args) throws IOException, TasteException {
		LOG.debug("****************************************");
		LOG.debug("MusicRecommender - User / Item");

	
		String itemFileName = "data/mb_artist.tsv";
		DataModel model = new FileDataModel(new File("data/ratings_all.tsv"), "\t");

		HashMap<Long, MusicItemInformation> itemInfo = MusicItemInfoLoader.load(model, itemFileName);

		LOG.debug("Model loaded: numUsers:" + model.getNumUsers() + " numItems:" + model.getNumItems());

		UserSimilarity pearsonSimilarity = new PearsonCorrelationSimilarity(model);
		UserSimilarity jacardSimilarity = new TanimotoCoefficientSimilarity(model);
		UserSimilarity cosineSimilarity = new UncenteredCosineSimilarity(model);
		// UserSimilarity similarity = new EuclideanDistanceSimilarity(model,
		// Weighting.WEIGHTED);
		// UserSimilarity similarity = new SpearmanCorrelationSimilarity(model);

		Double uth = 0.10;

		UserNeighborhood pearsonNeighborhood = new ThresholdUserNeighborhood(uth, pearsonSimilarity, model);
		UserNeighborhood jacardNeighborhood = new ThresholdUserNeighborhood(uth, jacardSimilarity, model);
		UserNeighborhood cosineNeighborhood = new ThresholdUserNeighborhood(uth, cosineSimilarity, model);

		// UserNeighborhood neighborhood = new NearestNUserNeighborhood(3,
		// similarity, model);

		UserBasedRecommender pearsonRecommender = new GenericUserBasedRecommender(model, pearsonNeighborhood,
				pearsonSimilarity);
		UserBasedRecommender jacardRecommender = new GenericUserBasedRecommender(model, jacardNeighborhood,
				jacardSimilarity);
		UserBasedRecommender cosineRecommender = new GenericUserBasedRecommender(model, cosineNeighborhood,
				cosineSimilarity);

		long userId = 888;
		int numberOfRecommendation = 5;

		long itemid = 0;

		LOG.debug("*** Pearson Recommendations: recommend");
		List<RecommendedItem> pearsonRecommendations = pearsonRecommender.recommend(userId, numberOfRecommendation);
		LOG.debug("*** Pearson Recommendations:");
		for (RecommendedItem recommendation : pearsonRecommendations) {
			itemid = recommendation.getItemID();
			LOG.debug(recommendation + " - " + itemInfo.get(itemid).getName());

		}

		LOG.debug("*** Jacard Recommendations: recommend");
		List<RecommendedItem> jacardRecommendations = jacardRecommender.recommend(userId, numberOfRecommendation);
		LOG.debug("*** Jacard Recommendations:");
		for (RecommendedItem recommendation : jacardRecommendations) {
			itemid = recommendation.getItemID();
			LOG.debug(recommendation + " - " + itemInfo.get(itemid).getName());

		}

		LOG.debug("*** Cosine Recommendations: recommend");
		List<RecommendedItem> cosineRecommendations = cosineRecommender.recommend(userId, numberOfRecommendation);
		LOG.debug("*** Cosine Recommendations:");
		for (RecommendedItem recommendation : cosineRecommendations) {
			itemid = recommendation.getItemID();
			LOG.debug(recommendation + " - " + itemInfo.get(itemid).getName());
		}

		double training = 0.9;
		double eval = 1;
		double result = 0;
		
		// Evaluador con medidas de predicción <<Pearson>>
		LOG.debug("*** Building Evaluation: Begin");
		RecommenderBuilder pearsonBuilder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericUserBasedRecommender(dataModel, pearsonNeighborhood, pearsonSimilarity);
			}
		};
		LOG.debug("*** Pearson Evaluation: Begin");
		RecommenderEvaluator pearsonEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		result = pearsonEvaluator.evaluate(pearsonBuilder, null, model, training, eval);
		LOG.debug("*** Pearson Evaluation: Result : "+result);


		// Evaluador con medidas de predicción <<Cosine>>
		RecommenderBuilder cosineBuilder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericUserBasedRecommender(dataModel, cosineNeighborhood, cosineSimilarity);
			}
		};
		
		LOG.debug("*** Cosine Evaluation: Begin");
		RecommenderEvaluator cosineEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		result = cosineEvaluator.evaluate(cosineBuilder, null, model, training, eval);
		LOG.debug("*** Cosine Evaluation: Result : "+result);


		
		// Evaluador con medidas de predicción <<Jacard>>
		RecommenderBuilder jacardBuilder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericUserBasedRecommender(dataModel, jacardNeighborhood, jacardSimilarity);
			}
		};
		
		LOG.debug("*** Jacard Evaluation: Begin");
		RecommenderEvaluator jacardEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		result = jacardEvaluator.evaluate(jacardBuilder, null, model, training, eval);
		LOG.debug("*** Jacard Evaluation: Result : "+result);

		
/*		
		//TODO  Evaluador con medidas de IR
		RecommenderIRStatsEvaluator irEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irEvaluator.evaluate(builder, null, model, null, 10,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
		LOG.debug("Generic Recommender IR Stats Evaluator results");
		LOG.debug("Precision:" + stats.getPrecision());
		LOG.debug("Recall:" + stats.getRecall());
		LOG.debug(stats);
*/
		LOG.debug("****************************************");
	}

}
