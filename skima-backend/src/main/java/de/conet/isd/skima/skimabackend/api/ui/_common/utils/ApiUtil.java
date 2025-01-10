package de.conet.isd.skima.skimabackend.api.ui._common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class ApiUtil {
    /**
     * Throw if input is false (inteded use: .exists(...) Queries)
     */
    public static void throwIfNotFound(boolean exists) throws ResponseStatusException {
        if (!exists) doThrowNotFound("");
    }

    /**
     * If the given param is null, an exception is thrown that will result in 404 NOT FOUND Status for the client.
     */
    public static <T> T throwIfNotFound(T any) throws ResponseStatusException {
        return throwIfNotFound(any, null);
    }

    public static <T> T throwIfNotFound(Optional<T> any) throws ResponseStatusException {
        return throwIfNotFound(any, null);
    }

    public static <T> T throwIfNotFound(Optional<T> any, String description) throws ResponseStatusException {
        T unwrapped = any.orElse(null);
        throwIfNotFound(unwrapped, description);
        return unwrapped;
    }

    /**
     * @param description Description of the object that was not found (usually the name of
     *                   a business entity from the domain)
     */
    public static <T> T throwIfNotFound(T any, String description) throws ResponseStatusException {
        if (any == null) doThrowNotFound(description);
        return any;
    }

    private static void doThrowNotFound(String description) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, description);
    }

}
