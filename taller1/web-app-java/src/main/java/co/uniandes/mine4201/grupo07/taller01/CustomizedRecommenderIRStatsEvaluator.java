/**
 * 
 * Código original tomado de 
 * https://github.com/apache/mahout/blob/08e02602e947ff945b9bd73ab5f0b45863df3e53/mr/src/main/java/org/apache/mahout/cf/taste/impl/eval/GenericRecommenderIRStatsEvaluator.java
 * 
 * 
 * Modificaciones realizadas según:
 * https://stackoverflow.com/questions/21382160/test-and-training-with-different-dataset-with-mahout
 * 
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.uniandes.mine4201.grupo07.taller01;

import java.util.List;
import java.util.Random;

import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.eval.RelevantItemsDataSplitter;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.cf.taste.impl.common.RunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.eval.GenericRelevantItemsDataSplitter;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.common.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * <p>
 * For each user, these implementation determine the top {@code n} preferences,
 * then evaluate the IR statistics based on a {@link DataModel} that does not
 * have these values. This number {@code n} is the "at" value, as in "precision
 * at 5". For example, this would mean precision evaluated by removing the top 5
 * preferences for a user and then finding the percentage of those 5 items
 * included in the top 5 recommendations for that user.
 * </p>
 */
public final class CustomizedRecommenderIRStatsEvaluator {

	private static final Logger log = LoggerFactory.getLogger(CustomizedRecommenderIRStatsEvaluator.class);

	private static final double LOG2 = Math.log(2.0);

	/**
	 * Pass as "relevanceThreshold" argument to
	 * {@link #evaluate(RecommenderBuilder, DataModelBuilder, DataModel, IDRescorer, int, double, double)}
	 * to have it attempt to compute a reasonable threshold. Note that this will
	 * impact performance.
	 */
	public static final double CHOOSE_THRESHOLD = Double.NaN;

	private final Random random;
	private final RelevantItemsDataSplitter dataSplitter;

	public CustomizedRecommenderIRStatsEvaluator() {
		this(new GenericRelevantItemsDataSplitter());
	}

	public CustomizedRecommenderIRStatsEvaluator(RelevantItemsDataSplitter dataSplitter) {
		Preconditions.checkNotNull(dataSplitter);
		random = RandomUtils.getRandom();
		this.dataSplitter = dataSplitter;
	}

	/**
	 * 
	 * @param recommenderBuilder
	 * @param dataModelBuilder
	 * @param trainDataModel
	 * @param testDataModel
	 * @param rescorer
	 * @param at
	 * @param relevanceThreshold
	 * @param evaluationPercentage
	 * @return
	 * @throws TasteException
	 */
	public IRStatistics evaluate(RecommenderBuilder recommenderBuilder, DataModelBuilder dataModelBuilder,
			DataModel trainDataModel, DataModel testDataModel, 
			IDRescorer rescorer, int at, double relevanceThreshold,
			double evaluationPercentage) throws TasteException {

		Preconditions.checkArgument(recommenderBuilder != null, "recommenderBuilder is null");
		Preconditions.checkArgument(trainDataModel != null, "dataModel is null");
		Preconditions.checkArgument(at >= 1, "at must be at least 1");
		Preconditions.checkArgument(evaluationPercentage > 0.0 && evaluationPercentage <= 1.0,
				"Invalid evaluationPercentage: " + evaluationPercentage
						+ ". Must be: 0.0 < evaluationPercentage <= 1.0");

		int numItems = trainDataModel.getNumItems();
		RunningAverage precision = new FullRunningAverage();
		RunningAverage recall = new FullRunningAverage();
		RunningAverage fallOut = new FullRunningAverage();
		RunningAverage nDCG = new FullRunningAverage();
		int numUsersRecommendedFor = 0;
		int numUsersWithRecommendations = 0;

		LongPrimitiveIterator it = trainDataModel.getUserIDs();
		while (it.hasNext()) {

			long userID = it.nextLong();

			if (random.nextDouble() >= evaluationPercentage) {
				// Skipped
				continue;
			}

			long start = System.currentTimeMillis();

			PreferenceArray prefs = trainDataModel.getPreferencesFromUser(userID);

			// List some most-preferred items that would count as (most) "relevant" results
			double theRelevanceThreshold = Double.isNaN(relevanceThreshold) ? computeThreshold(prefs)	: relevanceThreshold;

			// Nota: Código original
			// FastIDSet relevantItemIDs = dataSplitter.getRelevantItemsIDs(userID, at,
			// theRelevanceThreshold, dataModel);

			// Nota: Código cambiado para separación de modelos entre training y test
			FastIDSet relevantItemIDs = dataSplitter.getRelevantItemsIDs(userID, at, theRelevanceThreshold,testDataModel);
			log.debug("relevantItemIDs.size: "+relevantItemIDs.size());

			int numRelevantItems = relevantItemIDs.size();
			if (numRelevantItems <= 0) {
				continue;
			}

			FastByIDMap<PreferenceArray> trainingUsers = new FastByIDMap<>(trainDataModel.getNumUsers());
			LongPrimitiveIterator it2 = trainDataModel.getUserIDs();
			while (it2.hasNext()) {
				dataSplitter.processOtherUser(userID, relevantItemIDs, trainingUsers, it2.nextLong(), trainDataModel);
			}

			DataModel trainingModel = dataModelBuilder == null ? new GenericDataModel(trainingUsers)
					: dataModelBuilder.buildDataModel(trainingUsers);
			try {
				trainingModel.getPreferencesFromUser(userID);
			} catch (NoSuchUserException nsee) {
				continue; // Oops we excluded all prefs for the user -- just move on
			}

			int size = numRelevantItems + trainingModel.getItemIDsFromUser(userID).size();
			if (size < 2 * at) {
				// Really not enough prefs to meaningfully evaluate this user
				continue;
			}

			Recommender recommender = recommenderBuilder.buildRecommender(trainingModel);

			int intersectionSize = 0;
			List<RecommendedItem> recommendedItems = recommender.recommend(userID, at, rescorer);
			for (RecommendedItem recommendedItem : recommendedItems) {
				if (relevantItemIDs.contains(recommendedItem.getItemID())) {
					intersectionSize++;
				}
			}

			int numRecommendedItems = recommendedItems.size();

			// Precision
			if (numRecommendedItems > 0) {
				precision.addDatum((double) intersectionSize / (double) numRecommendedItems);
			}

			// Recall
			recall.addDatum((double) intersectionSize / (double) numRelevantItems);

			// Fall-out
			if (numRelevantItems < size) {
				fallOut.addDatum(
						(double) (numRecommendedItems - intersectionSize) / (double) (numItems - numRelevantItems));
			}

			// nDCG
			// In computing, assume relevant IDs have relevance 1 and others 0
			double cumulativeGain = 0.0;
			double idealizedGain = 0.0;
			for (int i = 0; i < numRecommendedItems; i++) {
				RecommendedItem item = recommendedItems.get(i);
				double discount = 1.0 / log2(i + 2.0); // Classical formulation says log(i+1), but i is 0-based here
				if (relevantItemIDs.contains(item.getItemID())) {
					cumulativeGain += discount;
				}
				// otherwise we're multiplying discount by relevance 0 so it doesn't do anything

				// Ideally results would be ordered with all relevant ones first, so this
				// theoretical
				// ideal list starts with number of relevant items equal to the total number of
				// relevant items
				if (i < numRelevantItems) {
					idealizedGain += discount;
				}
			}
			if (idealizedGain > 0.0) {
				nDCG.addDatum(cumulativeGain / idealizedGain);
			}

			// Reach
			numUsersRecommendedFor++;
			if (numRecommendedItems > 0) {
				numUsersWithRecommendations++;
			}

			long end = System.currentTimeMillis();

			log.info("Evaluated with user {} in {}ms", userID, end - start);
			log.info("Precision/recall/fall-out/nDCG/reach: {} / {} / {} / {} / {}", precision.getAverage(),
					recall.getAverage(), fallOut.getAverage(), nDCG.getAverage(),
					(double) numUsersWithRecommendations / (double) numUsersRecommendedFor);
		}

		return new IRStatisticsImpl(precision.getAverage(), recall.getAverage(), fallOut.getAverage(),
				nDCG.getAverage(), (double) numUsersWithRecommendations / (double) numUsersRecommendedFor);
	}

	private static double computeThreshold(PreferenceArray prefs) {
		if (prefs.length() < 2) {
			// Not enough data points -- return a threshold that allows everything
			return Double.NEGATIVE_INFINITY;
		}
		RunningAverageAndStdDev stdDev = new FullRunningAverageAndStdDev();
		int size = prefs.length();
		for (int i = 0; i < size; i++) {
			stdDev.addDatum(prefs.getValue(i));
		}
		return stdDev.getAverage() + stdDev.getStandardDeviation();
	}

	private static double log2(double value) {
		return Math.log(value) / LOG2;
	}

}