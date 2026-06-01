package com.pronnect.project.controller;

import com.pronnect.project.dto.*;
import com.pronnect.project.entity.Project;
import com.pronnect.project.entity.ProjectBid;
import com.pronnect.project.mapper.BidMapper;
import com.pronnect.project.mapper.ProjectMapper;
import com.pronnect.project.service.AiSuggestionService;
import com.pronnect.project.service.ProjectBidService;
import com.pronnect.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectBidService bidService;
    private final AiSuggestionService aiSuggestionService;
    private final ProjectMapper projectMapper;
    private final BidMapper bidMapper;

    @PostMapping("/ai-suggest")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<AiSuggestionResponse> suggest(@RequestBody @Valid AiSuggestionRequest request) {
        AiSuggestionResponse response = aiSuggestionService.suggest(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ProjectResponse> create(@RequestBody @Valid CreateProjectRequest request) {
        Project project = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMapper.toResponse(project));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAll(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        Page<ProjectResponse> page = projectService.getAll(search, pageable)
                .map(projectMapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable UUID id) {
        Project project = projectService.getById(id);
        return ResponseEntity.ok(projectMapper.toResponse(project));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<ProjectResponse>> getMyProjects() {
        List<ProjectResponse> list = projectService.getMyProjects()
                .stream()
                .map(projectMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> closeProject(@PathVariable UUID id) {
        projectService.close(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/bids")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<BidResponse> createBid(
            @PathVariable UUID id,
            @RequestBody @Valid CreateBidRequest request
    ) {
        ProjectBid bid = bidService.createBid(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bidMapper.toResponse(bid));
    }

    @GetMapping("/{id}/bids")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<BidResponse>> getBidsForProject(@PathVariable UUID id) {
        List<BidResponse> list = bidService.getBidsForProject(id)
                .stream()
                .map(bid -> bidMapper.toResponse(bid))
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/bids/my")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<List<BidResponse>> getMyBids() {
        List<BidResponse> list = bidService.getMyBids()
                .stream()
                .map(bid -> bidMapper.toResponse(bid))
                .toList();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/bids/{bidId}/accept")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> acceptBid(@PathVariable UUID bidId) {
        bidService.acceptBid(bidId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/bids/{bidId}/reject")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> rejectBid(@PathVariable UUID bidId) {
        bidService.rejectBid(bidId);
        return ResponseEntity.noContent().build();
    }
}
