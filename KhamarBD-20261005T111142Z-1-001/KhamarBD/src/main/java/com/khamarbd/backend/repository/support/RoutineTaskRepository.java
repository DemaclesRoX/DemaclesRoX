package com.khamarbd.backend.repository.support;

import com.khamarbd.backend.entity.support.RoutineTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoutineTaskRepository extends JpaRepository<RoutineTask, Long> {

    @org.springframework.data.jpa.repository.Query("SELECT rt FROM RoutineTask rt WHERE rt.lotOrAnimal.lotId = :lotId")
    List<RoutineTask> findByLotOrAnimal_LotId(@org.springframework.data.repository.query.Param("lotId") Long lotId);

    @org.springframework.data.jpa.repository.Query("SELECT rt FROM RoutineTask rt WHERE rt.lotOrAnimal.lotId = :lotId AND rt.status = :status")
    List<RoutineTask> findByLotOrAnimal_LotIdAndStatus(@org.springframework.data.repository.query.Param("lotId") Long lotId, @org.springframework.data.repository.query.Param("status") String status);

    @org.springframework.data.jpa.repository.Query("SELECT rt FROM RoutineTask rt WHERE rt.lotOrAnimal.lotId = :lotId ORDER BY rt.dueDate ASC")
    List<RoutineTask> findByLotOrAnimal_LotIdOrderByDueDateAsc(@org.springframework.data.repository.query.Param("lotId") Long lotId);

    @org.springframework.data.jpa.repository.Query("SELECT rt FROM RoutineTask rt WHERE rt.lotOrAnimal.lotId = :lotId AND rt.dueDate = :dueDate")
    List<RoutineTask> findByLotOrAnimal_LotIdAndDueDate(@org.springframework.data.repository.query.Param("lotId") Long lotId, @org.springframework.data.repository.query.Param("dueDate") LocalDate dueDate);

    List<RoutineTask> findByDueDateLessThanEqualAndStatus(LocalDate dueDate, String status);
}
