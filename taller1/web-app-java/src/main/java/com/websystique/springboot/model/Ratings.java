package com.websystique.springboot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "ratings_all_columns")
public class Ratings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ratings_all_columns_id_seq")
	@SequenceGenerator(name = "ratings_all_columns_id_seq", sequenceName = "ratings_all_columns_id_seq", allocationSize = 1)
	private Long id;

	@Column(name = "id_user")
	private Long userId;

	@Column(name = "id_artist")
	private Long artistId;

	@Column(name = "total_plays")
	private Integer totalPlays;
	
	@Column(name = "rank")
	private Integer rank;
	
	@Column(name = "rating")
	private Integer rating;
	
	@Column(name = "last_updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdatedAt;
	

	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Date lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getArtistId() {
		return artistId;
	}

	public void setArtistId(Long artistId) {
		this.artistId = artistId;
	}

	public Integer getTotalPlays() {
		return totalPlays;
	}

	public void setTotalPlays(Integer totalPlays) {
		this.totalPlays = totalPlays;
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
