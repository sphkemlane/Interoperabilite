package com.interoperabilite.demo.Service;



import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class DatabaseExtractionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void executeSqlScript(MultipartFile sqlFile) throws IOException {
        String sqlScript = new String(sqlFile.getBytes(), StandardCharsets.UTF_8);

        // Diviser le script SQL en instructions individuelles.
        // Ceci est une approximation; les scripts SQL complexes peuvent nécessiter un parsing plus robuste.
        String[] sqlInstructions = sqlScript.split(";");

        for (String instruction : sqlInstructions) {
            // Ignorer les instructions vides après le split
            if (!instruction.trim().isEmpty()) {
                entityManager.createNativeQuery(instruction).executeUpdate();
            }
        }
    }
}
