package com.websystique.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.websystique.springboot.model.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

	List<Artist> findTop50ByTotalUsersNotNullAndPhotoUrlNullOrderByTotalUsersDesc();

	List<Artist> findTop50ByTotalUsersNotNullAndWikiNullOrderByTotalUsersDesc();

	List<Artist> findTop100ByTotalUsersNotNullAndWikiNullOrderByTotalUsersDesc();

	List<Artist> findTop500ByTotalUsersNotNullAndWikiNullOrderByTotalUsersDesc();

	Artist findOneByGid(String gid);

	Artist findOneByName(String name);

	List<Artist> findTop5ByTotalUsersNotNullAndLastRecommendedAtNullOrderByTotalUsersDesc();

	List<Artist> findTop100ByTotalUsersNotNullAndLastRecommendedAtNullOrderByTotalUsersDesc();

	List<Artist> findTop1000ByTotalUsersNotNullAndLastRecommendedAtNullOrderByTotalUsersDesc();

	List<Artist> findTop50ByTotalUsersNotNullOrderByTotalUsersDesc();
	List<Artist> findTop200ByTotalUsersNotNullOrderByTotalUsersDesc();

	@Query(value = "select * from musicbrainz_artists where last_recommended_at is not null ORDER BY RANDOM() LIMIT 50  ", nativeQuery = true)
	List<Artist> find50Random();

}
