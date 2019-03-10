package co.uniandes.mine4201.grupo07.taller01.util.music;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MusicItemInformation {

	private long id;
	private String gid;
	private String name;
	private long numRatings;
	private double itemAverage;

	public MusicItemInformation(long itemId, String gid, String name, int numRatings, double itemAverage) {

		this.id = itemId;
		this.gid = gid;
		this.name = name;

		this.numRatings = numRatings;
		this.itemAverage = itemAverage;
	}

	/**
	 * Default comparator compares based on numRatings for item
	 */
	public int compareTo(MusicItemInformation o) {

		return Long.compare(this.numRatings, o.numRatings);
	}

	public long getId() {
		return id;
	}

	public String getGid() {
		return gid;
	}

	public String getName() {
		return name;
	}

	public long getNumRatings() {
		return numRatings;
	}

	public double getItemAverage() {
		return itemAverage;
	}

	public void setNumRatings(long numRatings) {
		this.numRatings = numRatings;
	}

	/*
	@Override
	public String toString() {
		String ret = this.id + "\t"+this.gid+"\t" + this.name + '\t' + numRatings + '\t' + itemAverage + '\t';
		return ret.trim();
	}
	*/
	
	@Override
	public String toString()
	{
	    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

}
