package com.WilsonQdop.Chamadas.controllers;

import com.WilsonQdop.Chamadas.services.BackupService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("backup")
@CrossOrigin(origins = "http://localhost:4200")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping("/execute")
    //@PreAuthorize("hasAuthority('SCOPE_USER')")
    //@SecurityRequirement(name="bearerAuth")
    public ResponseEntity<String> manualBackup() {
        try {
            String result = backupService.executeBackup();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/restore")
    //@PreAuthorize("hasAuthority('SCOPE_USER')")
    //@SecurityRequirement(name="bearerAuth")
    public ResponseEntity<String> restore(@RequestParam String fileName) {
        try {
            backupService.restoreBackup(fileName);
            return ResponseEntity.ok("Restauração concluída para o arquivo: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na restauração: " + e.getMessage());
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        try {
            List<String> files = backupService.listarBackups();
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}