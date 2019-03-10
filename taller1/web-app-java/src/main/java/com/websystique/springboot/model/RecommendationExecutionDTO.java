package com.websystique.springboot.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RecommendationExecutionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4226483317573514774L;
	/**
	 * 
	 */

	private String executionTime;

	private List<RecommendationDTO> recommendations;
	
	private List<User> similarUsers;

	private Long userId;
	private Integer numberOfUserRatings;

	private char algorithm;
	private Integer neighbors;

	private Long itemId;

	public RecommendationExecutionDTO(String executionTime) {
		super();
		this.executionTime = executionTime;
	}

	public RecommendationExecutionDTO(String executionTime, List<RecommendationDTO> recs) {
		super();
		this.executionTime = executionTime;
		this.recommendations = recs;
	}

	public RecommendationExecutionDTO(List<RecommendationDTO> recs) {
		super();
		this.recommendations = recs;
	}
	
	

	public List<User> getSimilarUsers() {
		return similarUsers;
	}

	public void setSimilarUsers(List<User> similarUsers) {
		this.similarUsers = similarUsers;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public List<RecommendationDTO> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<RecommendationDTO> recommendations) {
		this.recommendations = recommendations;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getNumberOfUserRatings() {
		return numberOfUserRatings;
	}

	public void setNumberOfUserRatings(Integer numberOfUserRatings) {
		this.numberOfUserRatings = numberOfUserRatings;
	}

	public char getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(char algorithm) {
		this.algorithm = algorithm;
	}

	public Integer getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(Integer neighbors) {
		this.neighbors = neighbors;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
