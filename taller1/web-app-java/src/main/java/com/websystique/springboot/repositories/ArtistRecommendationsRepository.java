package com.websystique.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.websystique.springboot.model.ArtistRecommendations;

/**
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.named-queries
 * 
 * @author 
 *
 */
@Repository
public interface ArtistRecommendationsRepository extends JpaRepository<ArtistRecommendations, Long> {
	
	
	List<ArtistRecommendations>  findAllByArtistIdOrderByValueDesc(Long artistId);
	
	

   

}
