package edu.uniandes.mine.rec_sys.laboratorio1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import edu.uniandes.mine.rec_sys.laboratorio1.util.ItemInfoLoader;
import edu.uniandes.mine.rec_sys.laboratorio1.util.ItemInformation;

public class SampleRecommender {

	/**
	 * mvn exec:java
	 * -Dexec.mainClass="edu.uniandes.mine.rec_sys.laboratorio1.SampleRecommender"
	 * 
	 * @param args
	 * @throws IOException
	 * @throws TasteException
	 */
	public static void main(String[] args) throws IOException, TasteException {

		String itemFileName = "data/u.item";
		DataModel model = new FileDataModel(new File("data/u.data"), "\t");

		HashMap<Long, ItemInformation> itemInfo = ItemInfoLoader.load(model, itemFileName);

		System.out.println("Model loaded: numUsers:" + model.getNumUsers() + " numItems:" + model.getNumItems());

		// UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		// UserSimilarity similarity = new EuclideanDistanceSimilarity(model,
		// Weighting.WEIGHTED);
		UserSimilarity similarity = new SpearmanCorrelationSimilarity(model);

		// UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.75,
		// similarity, model);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(20, similarity, model);

		UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

		long userId = 4444;
		List<RecommendedItem> recommendations = recommender.recommend(userId, 10);
		for (RecommendedItem recommendation : recommendations) {
			long itemid = recommendation.getItemID();
			System.out.println(recommendation + " - " + itemInfo.get(itemid).getItemName());

		}

		RecommenderBuilder builder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
			}

		};

		// Evaluador con medidas de predicción
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		System.out.println(result);

		// Evaluador con medidas de IR
		RecommenderIRStatsEvaluator irEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irEvaluator.evaluate(builder, null, model, null, 10,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
		System.out.println("Generic Recommender IR Stats Evaluator results");
		System.out.println("Precision:"+stats.getPrecision());
		System.out.println("Recall:"+stats.getRecall());
		System.out.println(stats);

	}

}
