/**
 * 
 * Código original tomado de 
 * https://github.com/apache/mahout/blob/08e02602e947ff945b9bd73ab5f0b45863df3e53/mr/src/main/java/org/apache/mahout/cf/taste/impl/eval/AbstractDifferenceRecommenderEvaluator.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mahout.cf.taste.common.NoSuchItemException;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.RunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.common.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Abstract superclass of a couple implementations, providing shared
 * functionality.
 */
public abstract class CustomAbstractDifferenceRecommenderEvaluator  {

	private static final Logger log = LoggerFactory.getLogger(CustomAbstractDifferenceRecommenderEvaluator.class);

	private final Random random;
	private float maxPreference;
	private float minPreference;

	protected CustomAbstractDifferenceRecommenderEvaluator() {
		random = RandomUtils.getRandom();
		maxPreference = Float.NaN;
		minPreference = Float.NaN;
	}


	public final float getMaxPreference() {
		return maxPreference;
	}


	public final void setMaxPreference(float maxPreference) {
		this.maxPreference = maxPreference;
	}


	public final float getMinPreference() {
		return minPreference;
	}


	public final void setMinPreference(float minPreference) {
		this.minPreference = minPreference;
	}


	public double evaluate(RecommenderBuilder recommenderBuilder, 
			DataModelBuilder dataModelBuilder,
			DataModel trainDataModel, 
			DataModel testDataModel, 
			double evaluationPercentage) throws TasteException {
		Preconditions.checkNotNull(recommenderBuilder);
		Preconditions.checkNotNull(trainDataModel);
		Preconditions.checkArgument(evaluationPercentage >= 0.0 && evaluationPercentage <= 1.0,
				"Invalid evaluationPercentage: " + evaluationPercentage
						+ ". Must be: 0.0 <= evaluationPercentage <= 1.0");

		log.info("Beginning evaluation using {} of {}", trainDataModel, testDataModel);

		int trainNumUsers = trainDataModel.getNumUsers();
		FastByIDMap<PreferenceArray> trainingPrefs = new FastByIDMap<>(1 + (int) (evaluationPercentage * trainNumUsers));
		LongPrimitiveIterator trainIt = trainDataModel.getUserIDs();
		while (trainIt.hasNext()) {
			long userID = trainIt.nextLong();
			if (random.nextDouble() < evaluationPercentage) {
				getTrainOneUsersPrefs(trainingPrefs, userID, trainDataModel);
			}
		}

		DataModel trainingModel = dataModelBuilder == null ? new GenericDataModel(trainingPrefs)
				: dataModelBuilder.buildDataModel(trainingPrefs);
		Recommender recommender = recommenderBuilder.buildRecommender(trainingModel);

		
		int testNumUsers = testDataModel.getNumUsers();
		FastByIDMap<PreferenceArray> testPrefs = new FastByIDMap<>(1 + (int) (evaluationPercentage * testNumUsers));
		LongPrimitiveIterator testIt = testDataModel.getUserIDs();
		while (testIt.hasNext()) {
			long userID = testIt.nextLong();
			if (random.nextDouble() < evaluationPercentage) {
				getTestOneUsersPrefs(testPrefs, userID, testDataModel);
			}
		}
		
		
		double result = getEvaluation(testPrefs, recommender);
		log.info("Evaluation result: {}", result);
		return result;
	}

	
	private void getTrainOneUsersPrefs(
			FastByIDMap<PreferenceArray> trainingPrefs,
			long userID, 
			DataModel trainDataModel) throws TasteException {
		List<Preference> oneUserTrainingPrefs = new ArrayList<>(3);
		PreferenceArray prefs = trainDataModel.getPreferencesFromUser(userID);
		int size = prefs.length();
		for (int i = 0; i < size; i++) {
			Preference newPref = new GenericPreference(userID, prefs.getItemID(i), prefs.getValue(i));
			oneUserTrainingPrefs.add(newPref);
		}
		if (oneUserTrainingPrefs.size()>0) {
			trainingPrefs.put(userID, new GenericUserPreferenceArray(oneUserTrainingPrefs));
		}
	}
	
	
	private void getTestOneUsersPrefs(
			FastByIDMap<PreferenceArray> testPrefs, 
			long userID, 
			DataModel testDataModel) throws TasteException {
		List<Preference> oneUserTestPrefs  = new ArrayList<>(3);
		PreferenceArray prefs = testDataModel.getPreferencesFromUser(userID);
		int size = prefs.length();
		for (int i = 0; i < size; i++) {
			Preference newPref = new GenericPreference(userID, prefs.getItemID(i), prefs.getValue(i));
			oneUserTestPrefs.add(newPref);			
		}

		if (oneUserTestPrefs.size() > 0) {
			testPrefs.put(userID, new GenericUserPreferenceArray(oneUserTestPrefs));
		}
		
	}
	
	
	/**
	 * Método original 
	 * 
	 * @param trainingPercentage
	 * @param trainingPrefs
	 * @param testPrefs
	 * @param userID
	 * @param dataModel
	 * @throws TasteException
	 */
	private void splitOneUsersPrefs(double trainingPercentage, FastByIDMap<PreferenceArray> trainingPrefs,
			FastByIDMap<PreferenceArray> testPrefs, long userID, DataModel dataModel) throws TasteException {
		List<Preference> oneUserTrainingPrefs = null;
		List<Preference> oneUserTestPrefs = null;
		PreferenceArray prefs = dataModel.getPreferencesFromUser(userID);
		int size = prefs.length();
		for (int i = 0; i < size; i++) {
			Preference newPref = new GenericPreference(userID, prefs.getItemID(i), prefs.getValue(i));
			if (random.nextDouble() < trainingPercentage) {
				if (oneUserTrainingPrefs == null) {
					oneUserTrainingPrefs = new ArrayList<>(3);
				}
				oneUserTrainingPrefs.add(newPref);
			} else {
				if (oneUserTestPrefs == null) {
					oneUserTestPrefs = new ArrayList<>(3);
				}
				oneUserTestPrefs.add(newPref);
			}
		}
		if (oneUserTrainingPrefs != null) {
			trainingPrefs.put(userID, new GenericUserPreferenceArray(oneUserTrainingPrefs));
			if (oneUserTestPrefs != null) {
				testPrefs.put(userID, new GenericUserPreferenceArray(oneUserTestPrefs));
			}
		}
	}

	private float capEstimatedPreference(float estimate) {
		if (estimate > maxPreference) {
			return maxPreference;
		}
		if (estimate < minPreference) {
			return minPreference;
		}
		return estimate;
	}

	/**
	 * 
	 * @param testPrefs
	 * @param recommender
	 * @return
	 * @throws TasteException
	 */
	private double getEvaluation(FastByIDMap<PreferenceArray> testPrefs, Recommender recommender)
			throws TasteException {
		reset();
		Collection<Callable<Void>> estimateCallables = new ArrayList<>();
		AtomicInteger noEstimateCounter = new AtomicInteger();
		for (Map.Entry<Long, PreferenceArray> entry : testPrefs.entrySet()) {
			estimateCallables.add(
					new PreferenceEstimateCallable(recommender, entry.getKey(), entry.getValue(), noEstimateCounter));
		}
		log.info("Beginning evaluation of {} users", estimateCallables.size());
		RunningAverageAndStdDev timing = new FullRunningAverageAndStdDev();
		execute(estimateCallables, noEstimateCounter, timing);
		return computeFinalEvaluation();
	}

	protected static void execute(Collection<Callable<Void>> callables, AtomicInteger noEstimateCounter,
			RunningAverageAndStdDev timing) throws TasteException {

		Collection<Callable<Void>> wrappedCallables = wrapWithStatsCallables(callables, noEstimateCounter, timing);
		int numProcessors = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(numProcessors);
		log.info("Starting timing of {} tasks in {} threads", wrappedCallables.size(), numProcessors);
		try {
			List<Future<Void>> futures = executor.invokeAll(wrappedCallables);
			// Go look for exceptions here, really
			for (Future<Void> future : futures) {
				future.get();
			}

		} catch (InterruptedException ie) {
			throw new TasteException(ie);
		} catch (ExecutionException ee) {
			throw new TasteException(ee.getCause());
		}

		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new TasteException(e.getCause());
		}
	}

	private static Collection<Callable<Void>> wrapWithStatsCallables(Iterable<Callable<Void>> callables,
			AtomicInteger noEstimateCounter, RunningAverageAndStdDev timing) {
		Collection<Callable<Void>> wrapped = new ArrayList<>();
		int count = 0;
		for (Callable<Void> callable : callables) {
			boolean logStats = count++ % 1000 == 0; // log every 1000 or so iterations
			wrapped.add(new StatsCallable(callable, logStats, timing, noEstimateCounter));
		}
		return wrapped;
	}

	protected abstract void reset();

	protected abstract void processOneEstimate(float estimatedPreference, Preference realPref);

	protected abstract double computeFinalEvaluation();

	public final class PreferenceEstimateCallable implements Callable<Void> {

		private final Recommender recommender;
		private final long testUserID;
		private final PreferenceArray prefs;
		private final AtomicInteger noEstimateCounter;

		public PreferenceEstimateCallable(Recommender recommender, long testUserID, PreferenceArray prefs,
				AtomicInteger noEstimateCounter) {
			this.recommender = recommender;
			this.testUserID = testUserID;
			this.prefs = prefs;
			this.noEstimateCounter = noEstimateCounter;
		}

		@Override
		public Void call() throws TasteException {
			for (Preference realPref : prefs) {
				float estimatedPreference = Float.NaN;
				try {
					estimatedPreference = recommender.estimatePreference(testUserID, realPref.getItemID());
				} catch (NoSuchUserException nsue) {
					// It's possible that an item exists in the test data but not training data in
					// which case
					// NSEE will be thrown. Just ignore it and move on.
					log.debug("User exists in test data but not training data: {}", testUserID);
				} catch (NoSuchItemException nsie) {
					log.debug("Item exists in test data but not training data: {}", realPref.getItemID());
				}
				if (Float.isNaN(estimatedPreference)) {
					noEstimateCounter.incrementAndGet();
				} else {
					estimatedPreference = capEstimatedPreference(estimatedPreference);
					processOneEstimate(estimatedPreference, realPref);
				}
			}
			return null;
		}

	}

}
