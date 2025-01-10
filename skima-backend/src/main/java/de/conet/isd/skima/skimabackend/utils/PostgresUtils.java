package de.conet.isd.skima.skimabackend.utils;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class PostgresUtils {
    // might want to move to adapters if it grows / other datasources get added

    public static boolean isUniqueConstraintViolation(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof ConstraintViolationException constraintEx) {
            return "23505".equals(constraintEx.getSQLState());  // SQL state for unique violation in PostgreSQL
        }

        return false;
    }
}
