package com.websystique.springboot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "artist_recommendations")
public class ArtistRecommendations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_artist")
	private Long artistId;

	@Column(name = "id_related_artist")
	private Long relatedArtistId;

	@Column(name = "recommendation_value")
	private Float value;

	public Long getRelatedArtistId() {
		return relatedArtistId;
	}

	public void setRelatedArtistId(Long relatedArtistId) {
		this.relatedArtistId = relatedArtistId;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Long getArtistId() {
		return artistId;
	}

	public void setArtistId(Long artistId) {
		this.artistId = artistId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
