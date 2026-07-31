package org.example.backend_vet_in_house.shared.exception;

import org.example.backend_vet_in_house.shared.exception.appointment.AppointmentAlreadyExistException;
import org.example.backend_vet_in_house.shared.exception.catalog.InsufficientStockException;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductAlreadyExistsException;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductNotFoundException;
import org.example.backend_vet_in_house.shared.exception.pet.PetAlreadyExistException;
import org.example.backend_vet_in_house.shared.exception.pet.PetNotFoundException;
import org.example.backend_vet_in_house.shared.exception.sales.OrderAlreadyExistsException;
import org.example.backend_vet_in_house.shared.exception.sales.OrderByUserIdNotFoundException;
import org.example.backend_vet_in_house.shared.exception.sales.OrderNotFoundException;
import org.example.backend_vet_in_house.shared.exception.user.UserAlreadyExistsException;
import org.example.backend_vet_in_house.shared.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> response = new HashMap<>();

        String errorMessage = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();

        response.put("error", "Bad request");
        response.put("message", errorMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleAuthenticationExceptions(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();

        response.put("error", "Unauthorized");
        // Puedes poner un mensaje genérico por seguridad (para no revelar si falló el correo o la clave)
        response.put("message", "Correo o contraseña incorrectos");

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientStock(InsufficientStockException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INSUFFICIENT_STORAGE);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFound(OrderNotFoundException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderByUserIdNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderByUserIdNotFound(OrderByUserIdNotFoundException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleProductAlreadyExists(ProductAlreadyExistsException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OrderAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleOrderAlreadyExists(OrderAlreadyExistsException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePetNotFound(PetNotFoundException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PetAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handlePetAlreadyExist(PetAlreadyExistException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AppointmentAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleAppointmentAlreadyExist(AppointmentAlreadyExistException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Conflicto");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "not found");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
