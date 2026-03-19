package com.pronnect.skill.controller;

import com.pronnect.skill.repository.SkillRepository;
import com.pronnect.skill.dto.SkillResponse;
import com.pronnect.skill.mapper.SkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillRepository repository;
    private final SkillMapper mapper;

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAll() {
        List<SkillResponse> skills = repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(skills);
    }
}
