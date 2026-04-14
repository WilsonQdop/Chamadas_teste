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
import java.util.concurrent.TimeUnit;
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

    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    public String executeBackup() throws IOException, InterruptedException {
        File directory = new File(storagePath);
        if (!directory.exists()) directory.mkdirs();

        // No Linux, nomes de arquivos com espaços ou caracteres especiais podem ser chatos,
        // manter o padrão yyyyMMddHHmm é o ideal.
        String fileName = "backup-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) + ".sql";
        String fullPath = new File(storagePath + fileName).getAbsolutePath();

        System.out.println("--- 🕒 Iniciando backup no Linux: " + fileName + " ---");

        // Usando o caminho absoluto para garantir que o Java encontre o binário no Fedora
        ProcessBuilder pb = new ProcessBuilder(
                "/usr/bin/mariadb-dump",
                "--host=" + dbHost,
                "--user=" + dbUser,
                "--password=" + dbPass,
                "--protocol=tcp",
                "--ssl=0",
                dbName,
                "--result-file=" + fullPath
        );

        Process process = pb.start();

        boolean terminouNoTempo = process.waitFor(30, TimeUnit.SECONDS);

        if (terminouNoTempo) {
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                String msg = "✅ Sucesso: Arquivo criado em " + fileName;
                System.out.println(msg);
                return msg;
            } else {
                // No Linux, ler o errorStream é vital para debugar permissões de pasta
                String errorStream = new String(process.getErrorStream().readAllBytes());
                String msgErro = "❌ Erro no dump (Código " + exitCode + "): " + errorStream;
                System.err.println(msgErro);
                return msgErro;
            }
        } else {
            process.destroyForcibly();
            String msgTimeout = "⏰ Erro: O backup no Linux excedeu 30s.";
            System.err.println(msgTimeout);
            return msgTimeout;
        }
    }

    public void restoreBackup(String fileName) throws IOException, InterruptedException {
        String fullPath = new File(storagePath + fileName).getAbsolutePath();
        File inputFile = new File(fullPath);

        if (!inputFile.exists()) {
            throw new RuntimeException("Arquivo de backup não encontrado: " + fullPath);
        }

        // No Linux, o cliente chama-se 'mariadb' ou 'mysql'. No seu caso, use /usr/bin/mariadb
        ProcessBuilder pb = new ProcessBuilder(
                "/usr/bin/mariadb",
                "--host=" + dbHost,
                "--user=" + dbUser,
                "--password=" + dbPass,
                "--ssl=0",
                dbName
        );

        pb.redirectInput(ProcessBuilder.Redirect.from(inputFile));

        Process process = pb.start();

        // Timeout também no restore para não travar a VM se o banco estiver ocupado
        boolean terminou = process.waitFor(60, TimeUnit.SECONDS);

        if (!terminou || process.exitValue() != 0) {
            String errorStream = new String(process.getErrorStream().readAllBytes());
            process.destroyForcibly();
            throw new RuntimeException("Erro no restore Linux: " + errorStream);
        }
        System.out.println("✅ Restore concluído com sucesso!");
    }


    public List<String> ListerBackups() {
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