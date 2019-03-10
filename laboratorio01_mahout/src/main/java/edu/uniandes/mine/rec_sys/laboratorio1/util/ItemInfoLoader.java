package edu.uniandes.mine.rec_sys.laboratorio1.util;

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
import java.util.logging.Logger;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

/**
 * Loads the information about items from the Movielens100k dataset
 * 
 * @author AndresM
 *
 */
public class ItemInfoLoader {

	private final static Logger LOG = Logger.getLogger(ItemInfoLoader.class.getName());

	private static String[] genres = { "unknown", "Action", "Adventure", "Animation", "Children's", "Comedy", "Crime",
			"Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi",
			"Thriller", "War", "Western" };

	/**
	 * Returns a map of ItemInformation, where the key is the itemId
	 * 
	 * @param fileName
	 *            - The file to be loaded
	 * @param m
	 *            - The loaded DataModel to obtain the item popularity and
	 *            average.
	 * @throws FileNotFoundException
	 *             if the file is not found
	 * @throws TasteException
	 *             if Mahout model fails
	 */
	public static HashMap<Long, ItemInformation> load(DataModel m, String fileName)
			throws FileNotFoundException, TasteException {
		HashMap<Long, ItemInformation> itemInformationMap = new HashMap<>();
		LongPrimitiveIterator iter = m.getItemIDs();
		Map<Long, Double> itemAverages = new HashMap<>();
		Map<Long, Integer> itemPopularity = new HashMap<>();
		while (iter.hasNext()) {
			long itemId = iter.next();
			PreferenceArray arr = m.getPreferencesForItem(itemId);
			itemPopularity.put(itemId, arr.length());
			RunningAverage avg = new FullRunningAverage();

			for (Preference preference : arr) {
				avg.addDatum(preference.getValue());
			}
			itemAverages.put(itemId, avg.getAverage());
		}

		BufferedReader reader = null;
		String line = null;
		String[] tokens = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			while ((line = reader.readLine()) != null) {
				if (line.trim().startsWith("//")) {
					continue;
				}
				tokens = line.split("\\|");
				String itemId = tokens[0];
				String itemName = (tokens[1]);
				String itemURL = (tokens[4]);
				LinkedList<String> itemGenres = new LinkedList<>();
				for (int i = 0; i < genres.length; i++) {
					String genreIs = tokens[5 + i];
					if (genreIs.equals("1")) {
						itemGenres.add(genres[i]);
					}
				}
				long idNum = Long.parseLong(itemId);

				ItemInformation itemInformation = new ItemInformation(idNum, itemName, itemURL, itemGenres,
						itemPopularity.get(idNum), itemAverages.get(idNum));
				itemInformationMap.put(idNum, itemInformation);

			}
		} catch (FileNotFoundException e) {

			throw e;
		} catch (IOException e) {

			LOG.severe("Error reading file " + fileName);

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
	 * mvn exec:java
	 * -Dexec.mainClass="edu.uniandes.mine.rec_sys.laboratorio1.util.ItemInfoLoader"
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			DataModel model = new FileDataModel(new File("data/u.data"), "\t");

			String itemFileName = "data/u.item";
			ItemInformation[] array = getItemsSortedByPopularity(model, itemFileName);
			System.out.println();
			System.out.println("Most popular items sorted by number of ratings are: ");
			System.out.println("id: itemName itemURL numRatings itemAverage Genres");
			int count = 30;
			for (int i = 0; i < count; i++) {
				System.out.println(array[i]);
			}
			System.out.println();
			System.out.println("Least popular items sorted by number of ratings are: ");
			System.out.println("id: itemName itemURL numRatings itemAverage Genres");
			for (int i = array.length - 1; i > array.length - 1 - count; i--) {
				System.out.println(array[i]);
			}
			int atLeast = 20;
			array = getItemsSortedByAverage(model, itemFileName, atLeast);
			System.out.println();
			System.out.println(
					"Most popular items sorted by average of item with at least " + atLeast + " ratings are: ");
			System.out.println("id: itemName itemURL numRatings itemAverage Genres");

			for (int i = 0; i < count; i++) {
				System.out.println(array[i]);
			}
			System.out.println();
			System.out.println(
					"Least popular items sorted by average of item with at least " + atLeast + " ratings are: ");
			System.out.println("id: itemName itemURL numRatings itemAverage Genres");
			for (int i = array.length - 1; i > array.length - 1 - count; i--) {
				System.out.println(array[i]);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ItemInformation[] getItemsSortedByPopularity(DataModel model, String itemFileName)
			throws FileNotFoundException, TasteException {

		HashMap<Long, ItemInformation> itemInfo = ItemInfoLoader.load(model, itemFileName);
		ItemInformation[] array = null;

		array = new ItemInformation[itemInfo.keySet().size()];
		int index = 0;
		for (Long itemId : itemInfo.keySet()) {
			array[index++] = itemInfo.get(itemId);
		}

		Arrays.sort(array, new Comparator<ItemInformation>() {

			@Override
			public int compare(ItemInformation o1, ItemInformation o2) {
				// * -1 for reverse order
				return Long.compare(o1.getNumRatings(), o2.getNumRatings()) * -1;
			}
		});
		return array;
	}

	public static ItemInformation[] getItemsSortedByAverage(DataModel model, String itemFileName, int atLeast)
			throws FileNotFoundException, TasteException {

		HashMap<Long, ItemInformation> itemInfo = ItemInfoLoader.load(model, itemFileName);
		LinkedList<ItemInformation> list = new LinkedList<>();
		for (Long itemId : itemInfo.keySet()) {
			ItemInformation inf = itemInfo.get(itemId);
			if (inf.getNumRatings() >= atLeast) {
				list.add(inf);
			}
		}

		ItemInformation[] array = new ItemInformation[list.size()];
		list.toArray(array);

		Arrays.sort(array, new Comparator<ItemInformation>() {

			@Override
			public int compare(ItemInformation o1, ItemInformation o2) {
				// * -1 for reverse order
				return Double.compare(o1.getItemAverage(), o2.getItemAverage()) * -1;
			}
		});
		return array;
	}

}
