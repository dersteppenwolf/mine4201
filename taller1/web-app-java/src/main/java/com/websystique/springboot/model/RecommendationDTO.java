package com.websystique.springboot.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RecommendationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7184896093283204209L;

	private float value;
	private Artist artist;

	private RecommendationExecutionDTO similarArtists;

	public RecommendationDTO(float value, Artist artist) {
		super();

		this.value = value;
		this.artist = artist;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public RecommendationExecutionDTO getSimilarArtists() {
		return similarArtists;
	}

	public void setSimilarArtists(RecommendationExecutionDTO similarArtists) {
		this.similarArtists = similarArtists;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
