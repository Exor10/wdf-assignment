package com.enomyfinances.service;

import com.enomyfinances.entity.ErrorLog;
import com.enomyfinances.repository.ErrorLogRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ErrorLoggingService {

    private static final Logger log = LoggerFactory.getLogger(ErrorLoggingService.class);

    private final ErrorLogRepository errorLogRepository;

    public ErrorLoggingService(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    public void logError(String source, String message, String details) {
        try {
            ErrorLog errorLog = new ErrorLog();
            errorLog.setSource(source);
            errorLog.setMessage(message);
            errorLog.setDetails(details);
            errorLog.setCreatedAt(LocalDateTime.now());
            errorLogRepository.save(errorLog);
        } catch (Exception ex) {
            log.error("Failed to write error to database, writing to file fallback", ex);
            writeFileFallback(source, message, details);
        }
    }

    private void writeFileFallback(String source, String message, String details) {
        String entry = LocalDateTime.now() + " | " + source + " | " + message + " | " + details + System.lineSeparator();
        try {
            Files.writeString(Path.of("error-fallback.log"), entry,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ioException) {
            log.error("Unable to write error fallback log", ioException);
        }
    }
}
