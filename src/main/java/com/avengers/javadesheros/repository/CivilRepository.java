package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Civil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CivilRepository extends JpaRepository<Civil, Long> {

    @Query("SELECT c FROM Civil c WHERE LOWER(c.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(c.prenom) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Civil> search(@Param("q") String query);
}
