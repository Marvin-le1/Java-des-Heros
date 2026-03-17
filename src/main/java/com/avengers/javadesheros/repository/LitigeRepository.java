package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Litige;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LitigeRepository extends JpaRepository<Litige, Long> {

    @Query("SELECT l FROM Litige l WHERE LOWER(l.titre) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Litige> search(@Param("q") String query);

    List<Litige> findByStatut(Litige.Statut statut);

    List<Litige> findByMissionId(Long missionId);
}
