package com.pronnect.professionalSkill;

import com.pronnect.skill.AddSkillsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
public class ProfessionalSkillController {

    private final ProfessionalSkillService professionalSkillService;

    @PostMapping("/{profileId}/skills")
    public ResponseEntity<Void> addSkills(
            @PathVariable UUID profileId,
            @RequestBody AddSkillsRequest request
    ) {

        professionalSkillService.addSkills(profileId, request.skillIds());

        return ResponseEntity.ok().build();
    }
}
