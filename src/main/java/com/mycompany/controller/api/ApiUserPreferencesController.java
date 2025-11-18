package com.mycompany.controller.api;

import com.mycompany.dto.UserPreferenceDto;
import com.mycompany.model.UserPreference;
import com.mycompany.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/preferences")
public class ApiUserPreferencesController {

    @Autowired
    private UserPreferenceRepository prefRepo;

    @PostMapping
    public ResponseEntity<?> savePreference(@RequestBody UserPreferenceDto dto, Principal principal) {
        String username = principal != null ? principal.getName() : dto.getKey();
        UserPreference p = new UserPreference();
        p.setUsername(username);
        p.setKey(dto.getKey());
        p.setValue(dto.getValue());
        prefRepo.save(p);
        return ResponseEntity.ok(dto);
    }
}
