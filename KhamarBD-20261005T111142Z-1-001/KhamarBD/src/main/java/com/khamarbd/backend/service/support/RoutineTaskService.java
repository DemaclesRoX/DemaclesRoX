package com.khamarbd.backend.service.support;

import com.khamarbd.backend.dto.RoutineTaskRequestDto;
import com.khamarbd.backend.entity.LotOrAnimal;
import com.khamarbd.backend.entity.support.RoutineTask;
import com.khamarbd.backend.repository.LotOrAnimalRepository;
import com.khamarbd.backend.repository.support.RoutineTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class RoutineTaskService {

    @Autowired
    private RoutineTaskRepository routineTaskRepository;

    @Autowired
    private LotOrAnimalRepository lotOrAnimalRepository;

    @Autowired
    private com.khamarbd.backend.repository.support.SystemTemplateRepository templateRepository;

    @Autowired
    private com.khamarbd.backend.repository.PrescriptionRepository prescriptionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<RoutineTask> generateTasksFromPrescription(Long prescriptionId, Long overrideLotId) {
        com.khamarbd.backend.entity.Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + prescriptionId));

        Long lotId = (overrideLotId != null) ? overrideLotId : (prescription.getLotOrAnimal() != null ? prescription.getLotOrAnimal().getLotId() : null);
        if (lotId == null) {
            throw new RuntimeException("Please select a tracking unit to assign this prescription.");
        }

        LotOrAnimal lot = lotOrAnimalRepository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Tracking unit not found with ID: " + lotId));

        java.time.LocalDate startDate = java.time.LocalDate.now();
        java.util.List<RoutineTask> createdTasks = new java.util.ArrayList<>();

        // 1. Generate scheduled tasks for each medicine / input item
        if (prescription.getItems() != null && !prescription.getItems().isEmpty()) {
            for (com.khamarbd.backend.entity.PrescriptionItem item : prescription.getItems()) {
                int duration = (item.getDurationDays() != null && item.getDurationDays() > 0) ? item.getDurationDays() : 1;
                for (int day = 0; day < duration; day++) {
                    RoutineTask task = new RoutineTask();
                    task.setLotOrAnimal(lot);
                    task.setSourceTemplateCode("RX-" + prescriptionId);

                    java.time.LocalDate dueDate = startDate.plusDays(day);
                    task.setDueDate(dueDate);

                    long dayOffset = 0;
                    if (lot.getStartDate() != null) {
                        dayOffset = java.time.temporal.ChronoUnit.DAYS.between(lot.getStartDate(), dueDate);
                        if (dayOffset < 0) dayOffset = day;
                    } else {
                        dayOffset = day;
                    }
                    task.setDayOffset((int) dayOffset);

                    String medName = item.getItemName() != null ? item.getItemName() : "Prescribed Medicine";
                    String title = String.format("Administer %s - %s (%s) [Rx #%d Day %d/%d]",
                            medName,
                            (item.getDosage() != null ? item.getDosage() : "As directed"),
                            (item.getInstructions() != null ? item.getInstructions() : "Oral"),
                            prescriptionId,
                            day + 1,
                            duration
                    );

                    task.setTitleEn(title);
                    task.setTitleBn(title);
                    task.setActionTypeCode("TREATMENT");
                    task.setCostPrompt(true);
                    task.setStatus("PENDING");

                    createdTasks.add(routineTaskRepository.save(task));
                }
            }
        } else if (prescription.getDiagnosis() != null) {
            RoutineTask task = new RoutineTask();
            task.setLotOrAnimal(lot);
            task.setSourceTemplateCode("RX-" + prescriptionId);
            task.setDueDate(startDate);
            task.setDayOffset(0);
            task.setTitleEn("Clinical Treatment: " + prescription.getDiagnosis() + " [Rx #" + prescriptionId + "]");
            task.setTitleBn("Clinical Treatment: " + prescription.getDiagnosis() + " [Rx #" + prescriptionId + "]");
            task.setActionTypeCode("TREATMENT");
            task.setCostPrompt(true);
            task.setStatus("PENDING");
            createdTasks.add(routineTaskRepository.save(task));
        }

        // 2. Generate task for clinical advice / special notice if present
        if (prescription.getSpecialNotice() != null && !prescription.getSpecialNotice().trim().isEmpty()) {
            RoutineTask adviceTask = new RoutineTask();
            adviceTask.setLotOrAnimal(lot);
            adviceTask.setSourceTemplateCode("RX-" + prescriptionId);
            adviceTask.setDueDate(startDate);
            adviceTask.setDayOffset(0);
            adviceTask.setTitleEn("Clinical Advisory: " + prescription.getSpecialNotice() + " [Rx #" + prescriptionId + "]");
            adviceTask.setTitleBn("Clinical Advisory: " + prescription.getSpecialNotice() + " [Rx #" + prescriptionId + "]");
            adviceTask.setActionTypeCode("MANAGEMENT");
            adviceTask.setCostPrompt(false);
            adviceTask.setStatus("PENDING");
            createdTasks.add(routineTaskRepository.save(adviceTask));
        }

        return createdTasks;
    }

    public List<RoutineTask> generateTasksFromTemplate(Long lotId, String templateCode) {
        LotOrAnimal lot = lotOrAnimalRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Lot not found"));
        com.khamarbd.backend.entity.support.SystemTemplate template = templateRepository.findById(templateCode).orElseThrow(() -> new RuntimeException("Template not found"));
        
        // Delete existing PENDING tasks for this lot before applying a new template.
        // We keep DONE tasks so the farmer doesn't lose their historical records.
        List<RoutineTask> existingPending = routineTaskRepository.findByLotOrAnimal_LotIdAndStatus(lotId, "PENDING");
        routineTaskRepository.deleteAll(existingPending);

        // Also update the Lot's routine_template_code
        lot.setRoutineTemplateCode(templateCode);
        lotOrAnimalRepository.save(lot);

        try {
            JsonNode tasksJson = objectMapper.readTree(template.getContentJson());
            for (JsonNode node : tasksJson) {
                int dayOffset = node.get("dayOffset").asInt();
                String title = node.get("taskTitle").asText();
                String actionType = node.get("actionType").asText();

                RoutineTask task = new RoutineTask();
                task.setLotOrAnimal(lot);
                task.setSourceTemplateCode(templateCode);
                task.setDayOffset(dayOffset);
                task.setDueDate(lot.getStartDate().plusDays(dayOffset));
                task.setTitleEn(title);
                task.setTitleBn(title);
                task.setActionTypeCode(actionType);
                task.setStatus("PENDING");
                routineTaskRepository.save(task);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse template JSON", e);
        }
        return getTasksByLotId(lotId);
    }

    public RoutineTask saveRoutineTask(RoutineTaskRequestDto dto) {
        RoutineTask task = new RoutineTask();
        LotOrAnimal lot = lotOrAnimalRepository.findById(dto.getLotId()).orElse(null);
        task.setLotOrAnimal(lot);
        task.setTitleEn(dto.getTaskTitle());
        task.setTitleBn(dto.getTaskTitle());
        task.setActionTypeCode(dto.getActionType());
        task.setDueDate(dto.getDueDate());
        task.setCostPrompt(dto.isCostPrompt());
        task.setStatus("PENDING");
        return routineTaskRepository.save(task);
    }

    public List<RoutineTask> getAllTasks() {
        return routineTaskRepository.findAll();
    }

    public List<RoutineTask> getTasksByLotId(Long lotId) {
        return routineTaskRepository.findByLotOrAnimal_LotIdOrderByDueDateAsc(lotId);
    }

    public List<RoutineTask> getTasksByLotIdAndStatus(Long lotId, String status) {
        return routineTaskRepository.findByLotOrAnimal_LotIdAndStatus(lotId, status);
    }

    public RoutineTask updateTaskStatus(Long taskId, String status) {
        RoutineTask task = routineTaskRepository.findById(taskId).orElse(null);
        if (task != null) {
            task.setStatus(status);
            if ("DONE".equalsIgnoreCase(status)) {
                task.setDoneAt(ZonedDateTime.now());
            }
            return routineTaskRepository.save(task);
        }
        return null;
    }
}
