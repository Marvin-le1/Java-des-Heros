package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Satisfaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SatisfactionRepository extends JpaRepository<Satisfaction, Long> {

    List<Satisfaction> findByMissionId(Long missionId);

    List<Satisfaction> findByCivilId(Long civilId);

    @Query("SELECT AVG(s.note) FROM Satisfaction s WHERE s.mission.id = :missionId")
    Double avgNoteByMission(@Param("missionId") Long missionId);

    @Query("SELECT s FROM Satisfaction s WHERE s.mission.id = :missionId ORDER BY s.dateEvaluation DESC")
    List<Satisfaction> findByMissionIdOrderByDate(@Param("missionId") Long missionId);

    @Query("SELECT AVG(s.note) FROM Satisfaction s JOIN s.mission m JOIN m.superheros h WHERE h.id = :heroId")
    Double avgNoteByHero(@Param("heroId") Long heroId);
}
