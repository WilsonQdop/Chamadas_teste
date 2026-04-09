package com.WilsonQdop.Chamadas.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BackupService {

    @Value("${database.host}")
    private String dbHost;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    @Value("${database.name}")
    private String dbName;

    @Value("${backup.storage.location:./backups/}")
    private String storagePath;

    @Scheduled(cron = "0 0 * * * *")
    public String executeBackup() throws IOException, InterruptedException {
        File directory = new File(storagePath);
        if (!directory.exists()) directory.mkdirs();

        String fileName = "backup-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) + ".sql";
        String fullPath = new File(storagePath + fileName).getAbsolutePath();

        ProcessBuilder pb = new ProcessBuilder(
                "mariadb-dump",
                "--host=" + dbHost,
                "--user=" + dbUser,
                "--password=" + dbPass,
                "--protocol=tcp",
                "--ssl=0",
                dbName,
                "--result-file=" + fullPath
        );
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            return "Backup realizado: " + fileName;
        } else {
            String errorStream = new String(process.getErrorStream().readAllBytes());
            throw new RuntimeException("Erro no mysqldump: " + errorStream);
        }
    }

    public void restoreBackup(String fileName) throws IOException, InterruptedException {
        String fullPath = new File(storagePath + fileName).getAbsolutePath();

        ProcessBuilder pb = new ProcessBuilder(
                "mariadb",
                "--host=" + dbHost,
                "--user=" + dbUser,
                "--password=" + dbPass,
                "--ssl=0",
                dbName
        );

        pb.redirectInput(ProcessBuilder.Redirect.from(new File(fullPath)));

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String errorStream = new String(process.getErrorStream().readAllBytes());
            throw new RuntimeException("Erro no restore: " + errorStream);
        }
    }
    public List<String> listarBackups() {
        File directory = new File(storagePath);
        if (!directory.exists()) return Collections.emptyList();

        return Arrays.stream(directory.listFiles())
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".sql"))
                .map(File::getName)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}