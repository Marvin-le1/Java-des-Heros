package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    @Query("SELECT o FROM Organisation o WHERE LOWER(o.nom) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Organisation> search(@Param("q") String query);
}
