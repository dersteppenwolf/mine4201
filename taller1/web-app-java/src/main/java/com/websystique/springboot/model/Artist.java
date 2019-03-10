package com.websystique.springboot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "musicbrainz_artists")
public class Artist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist__id_seq")
	@SequenceGenerator(name = "artist__id_seq", sequenceName = "artist__id_seq", allocationSize = 1)
	private Long id;

	@NotEmpty
	@Column(name = "gid", nullable = false)
	private String gid;

	@NotEmpty
	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "comment")
	private String comment;

	@Column(name = "photo_url")
	private String photoUrl;

	@Column(name = "total_plays")
	private Integer totalPlays;

	@Column(name = "total_users")
	private Integer totalUsers;
	
	@Column(name = "wiki")
	private String wiki;
	
	@Column(name = "last_recommended_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastRecommendedAt;
	
	
	

	public Date getLastRecommendedAt() {
		return lastRecommendedAt;
	}

	public void setLastRecommendedAt(Date lastRecommendedAt) {
		this.lastRecommendedAt = lastRecommendedAt;
	}

	public String getWiki() {
		return wiki;
	}

	public void setWiki(String wiki) {
		this.wiki = wiki;
	}

	public Integer getTotalPlays() {
		return totalPlays;
	}

	public void setTotalPlays(Integer totalPlays) {
		this.totalPlays = totalPlays;
	}

	public Integer getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
