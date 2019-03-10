package co.uniandes.mine4201.grupo07.taller01;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.EuclideanDistanceSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.uniandes.mine4201.grupo07.taller01.util.music.MusicItemInfoLoader;
import co.uniandes.mine4201.grupo07.taller01.util.music.MusicItemInformation;

public class MusicRecommenderItemItem {
	
	static final Logger LOG = LoggerFactory.getLogger(MusicRecommenderItemItem.class);


	public static void main(String[] args) throws IOException, TasteException {
		LOG.debug("****************************************");
		LOG.debug("MusicRecommender - Item / Item");

		RandomUtils.useTestSeed(); // Generates repeatable results

		String itemFileName = "data/mb_artist.tsv";
		
		DataModel allModel = 		new FileDataModel(new File("data/ratings_all.tsv"), "\t");
		//DataModel trainingModel = 	new FileDataModel(new File("data/ratings_train.tsv"), "\t");
		//DataModel testingModel = 		new FileDataModel(new File("data/ratings_test.tsv"), "\t");
		
		HashMap<Long, MusicItemInformation> itemDb = MusicItemInfoLoader.load(allModel, itemFileName);
		
		
		LOG.debug("Model All:  loaded: numUsers:" + allModel.getNumUsers() + " numItems:" + allModel.getNumItems());
		//LOG.debug("Model Training:  loaded: numUsers:" + trainingModel.getNumUsers() + " numItems:" + trainingModel.getNumItems());
		//LOG.debug("Model Testing:  loaded: numUsers:" + testingModel.getNumUsers() + " numItems:" + testingModel.getNumItems());
		
	
		long itemId = 956; //... david bowie 
		int numberOfRecommendations = 10;
		double evaluationPercentage = 0.01; // porcentaje de items a tener en cuenta durante la evaluación
		int numberOfRecommendationsUsedForPrecision = 10;
		
		double trainingPercentage = 0.9;
		
		// Modelos con archivos separados para train y test
		//runJaccardModelUsingTrainAndTestModels( trainingModel, testingModel, itemId,  numberOfRecommendations,  itemDb,  evaluationPercentage, numberOfRecommendationsUsedForPrecision) ;
		//runPearsondModelUsingTrainAndTestModels( trainingModel, testingModel, itemId,  numberOfRecommendations,  itemDb,  evaluationPercentage, numberOfRecommendationsUsedForPrecision) ;
		
		// Modelos con un solo archivo de entrada
		//runCosineModel(allModel, 	itemId,  numberOfRecommendations, itemDb, trainingPercentage,evaluationPercentage, numberOfRecommendationsUsedForPrecision );	
		//runPearsonModel(allModel, 	itemId,  numberOfRecommendations, itemDb, trainingPercentage,evaluationPercentage, numberOfRecommendationsUsedForPrecision );
		runJaccardModel(allModel,  itemId,  numberOfRecommendations, itemDb, trainingPercentage,evaluationPercentage, numberOfRecommendationsUsedForPrecision );
		
		LOG.debug("****************************************");
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param trainingModel
	 * @param itemId
	 * @param numberOfRecommendations
	 * @param itemDb
	 * @param trainingPercentage
	 * @param evaluationPercentage
	 * @param numberOfRecommendationsUsedForPrecision
	 * @throws TasteException
	 */
	public static void runPearsonModel(DataModel trainingModel, 
			long itemId, 
			int numberOfRecommendations, 
			HashMap<Long, MusicItemInformation> itemDb, 
			double trainingPercentage,
			double evaluationPercentage, 
			int numberOfRecommendationsUsedForPrecision ) throws TasteException {
		
		
		LOG.debug("****************************************");
		LOG.debug("****************************************");
		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		LOG.debug("runPearsonModel");
		LOG.debug("\t Item :  "+itemDb.get(itemId));
		LOG.debug("\t numberOfRecommendations :  "+numberOfRecommendations);
		LOG.debug("\t trainingPercentage :  "+trainingPercentage);
		LOG.debug("\t evaluationPercentage :  "+evaluationPercentage);
		LOG.debug("\t numberOfRecommendationsUsedForPrecision :  "+numberOfRecommendationsUsedForPrecision);
		
		/**
		 	https://archive.cloudera.com/cdh4/cdh/4/mahout-0.7-cdh4.5.0/mahout-core/org/apache/mahout/cf/taste/impl/similarity/PearsonCorrelationSimilarity.html
		 	
		 	An implementation of the Pearson correlation. For users X and Y, the following values are calculated:

			sumX2: sum of the square of all X's preference values
			sumY2: sum of the square of all Y's preference values
			sumXY: sum of the product of X and Y's preference value for all items for which both X and Y express a preference
			The correlation is then:
			
			sumXY / sqrt(sumX2 * sumY2)
			
			Note that this correlation "centers" its data, shifts the user's preference values so that each of their means is 0. 
			This is necessary to achieve expected behavior on all data sets.
			
			This correlation implementation is equivalent to the cosine similarity since the data it receives is assumed to be 
			centered -- mean is 0. The correlation may be interpreted as the cosine of the angle between the two vectors defined 
			by the users' preference values.
			
			For cosine similarity on uncentered data, see UncenteredCosineSimilarity. 
		 */
		ItemSimilarity similarity =       new PearsonCorrelationSimilarity(trainingModel);
		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(trainingModel, similarity);
			
		LOG.debug("recommend using Pearson:");
		List<RecommendedItem> jacardRecommendations = recommender.mostSimilarItems(itemId, numberOfRecommendations);
		for (RecommendedItem recommendation : jacardRecommendations) {
			long itemid = recommendation.getItemID();
			LOG.debug(recommendation + " - " + itemDb.get(itemid));
		}
		
		// Evaluador con medidas de predicción <<Jacard>>
		RecommenderBuilder builder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericItemBasedRecommender(dataModel,  similarity);
			}
		};
		
		LOG.debug("****************************************");
		LOG.debug("*** Pearson Evaluation: Begin");

		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		
		
		double total = 0;
		int numExecutions = 100;
		for (int i = 0; i < 100; i++) {
			//LOG.debug("Iteración: " + i);
			double result = evaluator.evaluate(builder, null, trainingModel, trainingPercentage,  evaluationPercentage);		
			LOG.debug("*** ; "+i+"  ; "+result);
			total+=result;
		}
		LOG.debug("average: " + (total/numExecutions) );
		
		/*
		LOG.debug("****************************************");
		LOG.debug("** Evaluador con medidas de IR");
		RecommenderIRStatsEvaluator irEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irEvaluator.evaluate(builder, null, trainingModel, null, numberOfRecommendationsUsedForPrecision,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, evaluationPercentage);
		LOG.debug("Generic Recommender IR Stats Evaluator results");
		//LOG.debug("Precision:" + stats.getPrecision());
		//LOG.debug("Recall:" + stats.getRecall());
		LOG.debug("Statistics:" +stats);	
		*/
		LOG.debug("****************************************");
		LOG.debug("Finished calculating {}", stopwatch);
		LOG.debug("****************************************");
		LOG.debug("****************************************");
	}
	
	
	
	/**
	 * 
	 * 
	 * @param trainingModel
	 * @param itemId
	 * @param numberOfRecommendations
	 * @param itemDb
	 * @param trainingPercentage
	 * @param evaluationPercentage
	 * @param numberOfRecommendationsUsedForPrecision
	 * @throws TasteException
	 */
	public static void runCosineModel(DataModel trainingModel, 
			long itemId, 
			int numberOfRecommendations, 
			HashMap<Long, MusicItemInformation> itemDb, 
			double trainingPercentage,
			double evaluationPercentage, 
			int numberOfRecommendationsUsedForPrecision ) throws TasteException {
		LOG.debug("****************************************");
		LOG.debug("****************************************");
		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		LOG.debug("runCosineModel");
		LOG.debug("\t Item :  "+itemDb.get(itemId));
		LOG.debug("\t numberOfRecommendations :  "+numberOfRecommendations);
		LOG.debug("\t trainingPercentage :  "+trainingPercentage);
		LOG.debug("\t evaluationPercentage :  "+evaluationPercentage);
		LOG.debug("\t numberOfRecommendationsUsedForPrecision :  "+numberOfRecommendationsUsedForPrecision);
		
		/**
		  https://archive.cloudera.com/cdh4/cdh/4/mahout-0.7-cdh4.5.0/mahout-core/org/apache/mahout/cf/taste/impl/similarity/UncenteredCosineSimilarity.html	
		  An implementation of the cosine similarity. The result is the cosine of the angle formed between the two preference vectors.
		  Note that this similarity does not "center" its data, shifts the user's preference values so that each of their means is 0. 
		  For this behavior, use PearsonCorrelationSimilarity, 
		  which actually is mathematically equivalent for centered data.
		 */
		ItemSimilarity similarity =       new UncenteredCosineSimilarity(trainingModel);
		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(trainingModel, similarity);
			
		LOG.debug("similar using Cosine:");
		List<RecommendedItem> jacardRecommendations = recommender.mostSimilarItems(itemId, numberOfRecommendations);
		for (RecommendedItem recommendation : jacardRecommendations) {
			long itemid = recommendation.getItemID();
			//LOG.debug(recommendation + " - " + itemDb.get(itemid));
		}
		
		// Evaluador con medidas de predicción <<Jacard>>
		RecommenderBuilder builder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericItemBasedRecommender(dataModel,  similarity);
			}
		};
		
		LOG.debug("****************************************");
		LOG.debug("*** Cosine Evaluation: Begin");
	
		/**
		 * 	A RecommenderEvaluator which computes the average absolute difference between predicted and actual ratings for users.
			This algorithm is also called "mean average error".
			MAE (Mean Average Error)  is a quantity used to measure how close forecasts or predictions are to the eventual outcomes. 
			The mean absolute error is given by
			https://www.kaggle.com/wiki/MeanAbsoluteError
			https://medium.com/human-in-a-machine-world/mae-and-rmse-which-metric-is-better-e60ac3bde13d
			http://www.statisticshowto.com/absolute-error/
		 */
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		double total = 0;
		int numExecutions = 100;
		for (int i = 0; i < 100; i++) {
			//LOG.debug("Iteración: " + i);
			double result = evaluator.evaluate(builder, null, trainingModel, trainingPercentage,  evaluationPercentage);		
			LOG.debug("*** ; "+i+"  ; "+result);
			total+=result;
		}
		LOG.debug("average: " + (total/numExecutions) );
		
		/*
		LOG.debug("****************************************");
		LOG.debug("** Evaluador con medidas de IR");
		RecommenderIRStatsEvaluator irEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irEvaluator.evaluate(builder, null, trainingModel, null, numberOfRecommendationsUsedForPrecision,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, evaluationPercentage);
		LOG.debug("Generic Recommender IR Stats Evaluator results");
		//LOG.debug("Precision:" + stats.getPrecision());
		//LOG.debug("Recall:" + stats.getRecall());
		LOG.debug("Statistics:" +stats);	
		LOG.debug("****************************************");
		*/
		LOG.debug("Finished calculating {}", stopwatch);
		LOG.debug("****************************************");
		LOG.debug("****************************************");
	}
	
	
	
	
	
	public static void runJaccardModel(DataModel trainingModel, 
			long itemId, 
			int numberOfRecommendations, 
			HashMap<Long, MusicItemInformation> itemDb, 
			double trainingPercentage,
			double evaluationPercentage, 
			int numberOfRecommendationsUsedForPrecision ) throws TasteException {
		
		LOG.debug("****************************************");
		LOG.debug("****************************************");
		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		LOG.debug("runJaccardModel");
		LOG.debug("\t Item :  "+itemDb.get(itemId));
		LOG.debug("\t numberOfRecommendations :  "+numberOfRecommendations);
		LOG.debug("\t trainingPercentage :  "+trainingPercentage);
		LOG.debug("\t evaluationPercentage :  "+evaluationPercentage);
		LOG.debug("\t numberOfRecommendationsUsedForPrecision :  "+numberOfRecommendationsUsedForPrecision);
		
		/**
		 	https://archive.cloudera.com/cdh4/cdh/4/mahout-0.7-cdh4.5.0/mahout-core/org/apache/mahout/cf/taste/impl/similarity/TanimotoCoefficientSimilarity.html
		 	
		 	An implementation of a "similarity" based on the Tanimoto coefficient, or extended Jaccard coefficient.

			This is intended for "binary" data sets where a user either expresses a generic "yes" preference 
			for an item or has no preference. The actual preference values do not matter here, only their presence or absence.
			
			The value returned is in [0,1].
		 */
		ItemSimilarity similarity =       new TanimotoCoefficientSimilarity(trainingModel);
		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(trainingModel, similarity);
			
		LOG.debug("recommend using Jacard:");
		List<RecommendedItem> jacardRecommendations = recommender.mostSimilarItems(itemId, numberOfRecommendations);
		for (RecommendedItem recommendation : jacardRecommendations) {
			long itemid = recommendation.getItemID();
			LOG.debug(recommendation + " - " + itemDb.get(itemid));
		}
		
		// Evaluador con medidas de predicción <<Jacard>>
		RecommenderBuilder builder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericItemBasedRecommender(dataModel,  similarity);
			}
		};
		
		LOG.debug("****************************************");
		LOG.debug("*** Jacard Evaluation: Begin");
		
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		
		
		double total = 0;
		int numExecutions = 100;
		for (int i = 0; i < 100; i++) {
			//LOG.debug("Iteración: " + i);
			double result = evaluator.evaluate(builder, null, trainingModel, trainingPercentage,  evaluationPercentage);		
			LOG.debug("*** ; "+i+"  ; "+result);
			total+=result;
		}
		LOG.debug("average: " + (total/numExecutions) );
		
		/*
		LOG.debug("****************************************");
		LOG.debug("** Evaluador con medidas de IR");
		RecommenderIRStatsEvaluator irEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irEvaluator.evaluate(builder, null, trainingModel, null, numberOfRecommendationsUsedForPrecision,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, evaluationPercentage);
		LOG.debug("Generic Recommender IR Stats Evaluator results");
		//LOG.debug("Precision:" + stats.getPrecision());
		//LOG.debug("Recall:" + stats.getRecall());
		LOG.debug("Statistics:" +stats);	
		LOG.debug("****************************************");
		*/
		LOG.debug("Finished calculating {}", stopwatch);
		LOG.debug("****************************************");
		
		LOG.debug("****************************************");
		LOG.debug("****************************************");
		
	}
	
	
	
	
	/**
	 * 
	 * @param trainingModel
	 * @param testingModel
	 * @param itemId
	 * @param numberOfRecommendation
	 * @param itemDb
	 * @param trainingPercentage
	 * @param evaluationPercentage
	 * @param numberOfRecommendationsUsedForPrecision
	 * @throws TasteException
	 */
	public static void runJaccardModelUsingTrainAndTestModels(DataModel trainingModel, DataModel testingModel, 
			long itemId, 
			int numberOfRecommendations, 
			HashMap<Long, MusicItemInformation> itemDb, 
			double evaluationPercentage, 
			int numberOfRecommendationsUsedForPrecision ) throws TasteException {
		
		LOG.debug("****************************************");
		LOG.debug("****************************************");
		LOG.debug("runJaccardModelUsingTrainAndTestModels");
		LOG.debug("\t Item :  "+itemDb.get(itemId));
		LOG.debug("\t numberOfRecommendations :  "+numberOfRecommendations);
		LOG.debug("\t evaluationPercentage :  "+evaluationPercentage);
		LOG.debug("\t numberOfRecommendationsUsedForPrecision :  "+numberOfRecommendationsUsedForPrecision);
		
		// An implementation of a "similarity" based on the Tanimoto coefficient, or extended Jaccard coefficient.
		ItemSimilarity similarity =       new TanimotoCoefficientSimilarity(trainingModel);
		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(trainingModel, similarity);
		
		
		LOG.debug("recommend using Jacard:");
		List<RecommendedItem> jacardRecommendations = recommender.recommend(itemId, numberOfRecommendations);
		for (RecommendedItem recommendation : jacardRecommendations) {
			long itemid = recommendation.getItemID();
			LOG.debug(recommendation + " - " + itemDb.get(itemid));
		}
		
		// Evaluador con medidas de predicción <<Jacard>>
		RecommenderBuilder builder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericItemBasedRecommender(dataModel,  similarity);
			}
		};
		
		LOG.debug("****************************************");
		LOG.debug("*** Jacard Evaluation: Begin");
		//Nota: implementación personalizada para recibir los modelos de entrenamiento y pruebas de forma separada
		CustomAverageAbsoluteDifferenceRecommenderEvaluator jacardEvaluator = new CustomAverageAbsoluteDifferenceRecommenderEvaluator();
		double result = jacardEvaluator.evaluate(builder, null, trainingModel, testingModel,  evaluationPercentage);		
		LOG.debug("*** Jacard Evaluation: Result : "+result);
		
		LOG.debug("****************************************");
		LOG.debug("** Evaluador con medidas de IR");
		//Nota: implementación personalizada para recibir los modelos de entrenamiento y pruebas de forma separada
		//Nota: implementación de CustomizedRecommenderIRStatsEvaluator 
		// realizada según https://stackoverflow.com/questions/21382160/test-and-training-with-different-dataset-with-mahout
		CustomizedRecommenderIRStatsEvaluator irEvaluator = new CustomizedRecommenderIRStatsEvaluator();
		IRStatistics stats = irEvaluator.evaluate(builder, null, trainingModel, testingModel, 
				null, numberOfRecommendationsUsedForPrecision,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, evaluationPercentage);
		LOG.debug("Generic Recommender IR Stats Evaluator results");
		//LOG.debug("Precision:" + stats.getPrecision());
		//LOG.debug("Recall:" + stats.getRecall());
		LOG.debug("Statistics:" +stats);
				
		LOG.debug("****************************************");
		LOG.debug("****************************************");
		
	}
	
	
	public static void runPearsondModelUsingTrainAndTestModels(DataModel trainingModel, DataModel testingModel, 
			long itemId, 
			int numberOfRecommendations, 
			HashMap<Long, MusicItemInformation> itemDb, 
			double evaluationPercentage, 
			int numberOfRecommendationsUsedForPrecision ) throws TasteException {
		
		LOG.debug("****************************************");
		LOG.debug("****************************************");
		LOG.debug("runPearsondModelUsingTrainAndTestModels");
		LOG.debug("\t Item :  "+itemDb.get(itemId));
		LOG.debug("\t numberOfRecommendations :  "+numberOfRecommendations);
		LOG.debug("\t evaluationPercentage :  "+evaluationPercentage);
		LOG.debug("\t numberOfRecommendationsUsedForPrecision :  "+numberOfRecommendationsUsedForPrecision);
		
		ItemSimilarity similarity =       new PearsonCorrelationSimilarity(trainingModel);
		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(trainingModel, similarity);
		
		
		LOG.debug("recommend using Pearson:");
		List<RecommendedItem> jacardRecommendations = recommender.recommend(itemId, numberOfRecommendations);
		for (RecommendedItem recommendation : jacardRecommendations) {
			long itemid = recommendation.getItemID();
			LOG.debug(recommendation + " - " + itemDb.get(itemid));
		}
		
		// Evaluador con medidas de predicción <<Jacard>>
		RecommenderBuilder builder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				return new GenericItemBasedRecommender(dataModel,  similarity);
			}
		};
		
		LOG.debug("****************************************");
		LOG.debug("*** Pearson Evaluation: Begin");
		//Nota: implementación personalizada para recibir los modelos de entrenamiento y pruebas de forma separada
		CustomAverageAbsoluteDifferenceRecommenderEvaluator jacardEvaluator = new CustomAverageAbsoluteDifferenceRecommenderEvaluator();
		double result = jacardEvaluator.evaluate(builder, null, trainingModel, testingModel,  evaluationPercentage);		
		LOG.debug("*** Pearson Evaluation: Result : "+result);
		
		LOG.debug("****************************************");
		LOG.debug("** Evaluador con medidas de IR");
		//Nota: implementación personalizada para recibir los modelos de entrenamiento y pruebas de forma separada
		//Nota: implementación de CustomizedRecommenderIRStatsEvaluator 
		// realizada según https://stackoverflow.com/questions/21382160/test-and-training-with-different-dataset-with-mahout
		CustomizedRecommenderIRStatsEvaluator irEvaluator = new CustomizedRecommenderIRStatsEvaluator();
		IRStatistics stats = irEvaluator.evaluate(builder, null, trainingModel, testingModel, 
				null, numberOfRecommendationsUsedForPrecision,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, evaluationPercentage);
		LOG.debug("Generic Recommender IR Stats Evaluator results");
		//LOG.debug("Precision:" + stats.getPrecision());
		//LOG.debug("Recall:" + stats.getRecall());
		LOG.debug("Statistics:" +stats);
				
		LOG.debug("****************************************");
		LOG.debug("****************************************");
		
	}
	
	
	

}
