package co.uniandes.mine4201.grupo07.taller01.util.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicItemInfoLoader {

	static final Logger LOG = LoggerFactory.getLogger(MusicItemInfoLoader.class);

	/**
	 * Returns a map of ItemInformation, where the key is the itemId
	 * 
	 * @param fileName
	 *            - The file to be loaded
	 * @param m
	 *            - The loaded DataModel to obtain the item popularity and average.
	 * @throws FileNotFoundException
	 *             if the file is not found
	 * @throws TasteException
	 *             if Mahout model fails
	 */
	public static HashMap<Long, MusicItemInformation> load(DataModel m, String fileName)
			throws FileNotFoundException, TasteException {

		HashMap<Long, MusicItemInformation> itemInformationMap = new HashMap<>();
		LongPrimitiveIterator iter = m.getItemIDs();
		Map<Long, Double> itemAverages = new HashMap<>();
		Map<Long, Integer> itemPopularity = new HashMap<>();

		long itemId = 0;
		PreferenceArray arr = null;
		RunningAverage avg = null;

		while (iter.hasNext()) {
			itemId = iter.next();
			arr = m.getPreferencesForItem(itemId);
			itemPopularity.put(itemId, arr.length());
			avg = new FullRunningAverage();
			for (Preference preference : arr) {
				avg.addDatum(preference.getValue());
			}
			itemAverages.put(itemId, avg.getAverage());
		}

		BufferedReader reader = null;
		String line = null;
		String[] tokens = null;
		// StringTokenIterator tokens = null;

		String itemGid = null;
		String itemName = null;

		long idNum = 0;
		int ItemPopularity = 0;
		double itemAvg = 0;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			while ((line = reader.readLine()) != null) {
				if (line.trim().startsWith("//")) {
					continue;
				}
				//LOG.debug(line);
				tokens = line.split("\t");
				
				if(tokens.length < 3) {
					continue;
				}
					
				idNum = Long.parseLong(tokens[0]);
				itemGid = (tokens[1]);
				itemName = (tokens[2]);

				try {
					ItemPopularity = itemPopularity.get(idNum);
				} catch (Exception e) {
					ItemPopularity = 0;
				}

				try {
					itemAvg = itemAverages.get(idNum);
				} catch (Exception e) {
					itemAvg = 0;
				}

				MusicItemInformation itemInformation = new MusicItemInformation(idNum, itemGid, itemName,
						ItemPopularity, itemAvg);

				itemInformationMap.put(idNum, itemInformation);

			}
		} catch (FileNotFoundException e) {

			throw e;
		} catch (IOException e) {

			LOG.error("Error reading file " + fileName);

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}

		}
		return itemInformationMap;

	}

	/**
	 * mvn exec:java -Dexec.mainClass=
	 * "edu.uniandes.mine.rec_sys.laboratorio1.util.ItemInfoLoader"
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			DataModel model = new FileDataModel(new File("data/ratings_all.tsv"), "\t");

			String itemFileName = "data/mb_artist.tsv";
			MusicItemInformation[] array = getItemsSortedByPopularity(model, itemFileName);
			
			LOG.debug("**********************************************************");
			LOG.debug("Most popular items sorted by number of ratings are: ");
			LOG.debug("id gid itemName  numRatings itemAverage");
			int count = 30;
			for (int i = 0; i < count; i++) {
				LOG.debug("" + array[i]);
			}
			
			LOG.debug("**********************************************************");
			LOG.debug("Least popular items sorted by number of ratings are: ");
			LOG.debug("id gid itemName  numRatings itemAverage");
			for (int i = array.length - 1; i > array.length - 1 - count; i--) {
				LOG.debug("" + array[i]);
			}
			int atLeast = 20;
			array = getItemsSortedByAverage(model, itemFileName, atLeast);

			LOG.debug("**********************************************************");
			LOG.debug("Most popular items sorted by average of item with at least " + atLeast + " ratings are: ");
			LOG.debug("id gid itemName  numRatings itemAverage");

			for (int i = 0; i < count; i++) {
				LOG.debug("" + array[i]);
			}
			LOG.debug("**********************************************************");
			LOG.debug("Least popular items sorted by average of item with at least " + atLeast + " ratings are: ");
			LOG.debug("id gid itemName  numRatings itemAverage");
			for (int i = array.length - 1; i > array.length - 1 - count; i--) {
				LOG.debug("" + array[i]);
			}

		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param model
	 * @param itemFileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws TasteException
	 */
	public static MusicItemInformation[] getItemsSortedByPopularity(DataModel model, String itemFileName)
			throws FileNotFoundException, TasteException {

		HashMap<Long, MusicItemInformation> itemInfo = MusicItemInfoLoader.load(model, itemFileName);
		MusicItemInformation[] array = null;

		array = new MusicItemInformation[itemInfo.keySet().size()];
		int index = 0;
		for (Long itemId : itemInfo.keySet()) {
			array[index++] = itemInfo.get(itemId);
		}

		Arrays.sort(array, new Comparator<MusicItemInformation>() {

			@Override
			public int compare(MusicItemInformation o1, MusicItemInformation o2) {
				// * -1 for reverse order
				return Long.compare(o1.getNumRatings(), o2.getNumRatings()) * -1;
			}
		});
		return array;
	}

	public static MusicItemInformation[] getItemsSortedByAverage(DataModel model, String itemFileName, int atLeast)
			throws FileNotFoundException, TasteException {

		HashMap<Long, MusicItemInformation> itemInfo = MusicItemInfoLoader.load(model, itemFileName);
		LinkedList<MusicItemInformation> list = new LinkedList<>();
		for (Long itemId : itemInfo.keySet()) {
			MusicItemInformation inf = itemInfo.get(itemId);
			if (inf.getNumRatings() >= atLeast) {
				list.add(inf);
			}
		}

		MusicItemInformation[] array = new MusicItemInformation[list.size()];
		list.toArray(array);

		Arrays.sort(array, new Comparator<MusicItemInformation>() {

			@Override
			public int compare(MusicItemInformation o1, MusicItemInformation o2) {
				// * -1 for reverse order
				return Double.compare(o1.getItemAverage(), o2.getItemAverage()) * -1;
			}
		});
		return array;
	}

}
