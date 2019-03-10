package com.websystique.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.websystique.springboot.model.Ratings;

@Repository
public interface RatingsRepository extends JpaRepository<Ratings, Long> {
	
	// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.named-queries
	@Query(value = "select calculate_rating(?1) ", nativeQuery = true)
	boolean calculateRatingsForUser(Long id_user);
	
	Ratings  findOneByUserIdAndArtistId(Long userId, Long itemid);
	
	List<Ratings>  findAllByUserIdOrderByRatingDesc(Long userId);
	
	
	List<Ratings>  findTop5ByUserIdOrderByRatingDesc(Long userId);
	List<Ratings>  findTop50ByUserIdOrderByRatingDesc(Long userId);
	
	Long countByUserId(Long userId);

   

}
