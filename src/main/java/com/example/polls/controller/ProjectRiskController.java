package com.example.polls.controller;

import com.example.polls.model.project.ProjectRisk;
import com.example.polls.model.project.Risk;
import com.example.polls.model.project.RiskType;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.requests.project.RiskRequest;
import com.example.polls.payload.response.project.RiskChangeNotification;
import com.example.polls.payload.response.project.RiskResponse;
import com.example.polls.repository.UserRepository;
import com.example.polls.service.ProjectService;
import com.example.polls.service.ProjectRiskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProjectRiskController {
    private final ProjectRiskService projectRiskService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ProjectService projectService;
    private final UserRepository userRepository;

    @MessageMapping("/risk")
    public void changeRisk(RiskRequest riskRequest) {
        ProjectRisk saved = projectRiskService.save(riskRequest);
        List<Long> users = projectService.getUsersIdByProject(saved.getProject().getId());
        User user = userRepository.findById(riskRequest.getChangerId()).get();
        for (Long it : users)
            if (!it.equals(riskRequest.getChangerId()))
                messagingTemplate.convertAndSendToUser(
                        it.toString(), "/queue/risks",
                        RiskChangeNotification.builder()
                                .riskName(saved.getState().getTitle())
                                .changerName(user.getName())
                                .build());
        messagingTemplate.convertAndSendToUser(
                riskRequest.getChangerId().toString(), "/queue/risks",
                RiskChangeNotification.builder()
                        .riskName(saved.getState().getTitle())
                        .build());

    }

    @GetMapping("/risk/{riskId}")
    public ResponseEntity<RiskResponse> getRisk(@PathVariable Long riskId) {
        return ResponseEntity
                .ok(projectRiskService.getRisk(riskId));
    }

    @GetMapping("/risk/users/available/{prId}/{riskId}")
    public ResponseEntity<List<UserSummary>> getAvailableUsers(@PathVariable Long prId,
                                                               @PathVariable Long riskId) {
        return ResponseEntity
                .ok(projectService.getAvailableUser(prId, riskId));
    }


}
