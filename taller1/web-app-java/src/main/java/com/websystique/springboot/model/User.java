package com.websystique.springboot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_profile")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2966704200290943399L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_profile_id_seq")
	@SequenceGenerator(name = "user_profile_id_seq", sequenceName = "user_profile_id_seq", allocationSize = 1)
	private Long id;

	@NotEmpty
	@Column(name = "userid", nullable = false)
	private String name;

	@Column(name = "AGE", nullable = false)
	private Integer age;

	@Column(name = "total_plays")
	private Integer totalPlays;

	@Column(name = "total_artists")
	private Integer totalArtists;

	@Column(name = "lastfm_user")
	private String lastfmUser;
	
	
	@Transient
	private Long totalRatings;

	public Long getTotalRatings() {
		return totalRatings;
	}

	public void setTotalRatings(Long totalRatings) {
		this.totalRatings = totalRatings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getTotalPlays() {
		return totalPlays;
	}

	public void setTotalPlays(Integer totalPlays) {
		this.totalPlays = totalPlays;
	}

	public Integer getTotalArtists() {
		return totalArtists;
	}

	public void setTotalArtists(Integer totalArtists) {
		this.totalArtists = totalArtists;
	}

	public String getLastfmUser() {
		return lastfmUser;
	}

	public void setLastfmUser(String lastfmUser) {
		this.lastfmUser = lastfmUser;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
