package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.RoutineTaskRequestDto;
import com.khamarbd.backend.entity.support.RoutineTask;
import com.khamarbd.backend.service.support.RoutineTaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routine-task")
public class RoutineTaskController {

    @Autowired
    private RoutineTaskService routineTaskService;

    @PostMapping("/apply-template")
    public ResponseEntity<?> applyTemplate(@RequestParam Long lotId, @RequestParam String templateCode) {
        try {
            List<RoutineTask> tasks = routineTaskService.generateTasksFromTemplate(lotId, templateCode);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/apply-prescription")
    public ResponseEntity<?> applyPrescription(
            @RequestParam Long prescriptionId,
            @RequestParam(required = false) Long lotId
    ) {
        try {
            List<RoutineTask> tasks = routineTaskService.generateTasksFromPrescription(prescriptionId, lotId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @RequestBody — create a scheduled routine task for a tracking unit
    @PostMapping("/create")
    public ResponseEntity<?> createRoutineTask(
            @Valid @RequestBody RoutineTaskRequestDto routineTaskRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        RoutineTask savedTask = routineTaskService.saveRoutineTask(routineTaskRequestDto);
        return ResponseEntity.ok(savedTask);
    }

    // @RequestParam — search routine tasks by lotId and status
    @GetMapping("/search")
    public ResponseEntity<List<RoutineTask>> searchRoutineTasks(
            @RequestParam Long lotId,
            @RequestParam(defaultValue = "PENDING") String status
    ) {
        List<RoutineTask> tasks = routineTaskService.getTasksByLotIdAndStatus(lotId, status);
        return ResponseEntity.ok(tasks);
    }

    // @PathVariable — update task status to DONE or SKIPPED
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam String status
    ) {
        RoutineTask updated = routineTaskService.updateTaskStatus(taskId, status);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }
}
