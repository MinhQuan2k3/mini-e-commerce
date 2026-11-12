package com.miniecommerce.backend.common.exception;

import com.miniecommerce.backend.common.response.ApiResponse;
import com.miniecommerce.backend.common.response.ErrorDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<ErrorDetail> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorDetail(
                        error.getField(),
                        error.getDefaultMessage() != null
                                ? error.getDefaultMessage()
                                : "Invalid value"
                ))
                .toList();

        return new ResponseEntity<>(
                ApiResponse.<Void>failure("Validation failed", errors),
                headers,
                status
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        if (status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value()
                && request instanceof ServletWebRequest servletWebRequest) {

            HttpServletResponse response = servletWebRequest.getResponse();

            if (response != null && response.isCommitted()) {
                return null;
            }

            servletWebRequest.getRequest().setAttribute(
                    WebUtils.ERROR_EXCEPTION_ATTRIBUTE,
                    exception
            );
        }

        return new ResponseEntity<>(
                ApiResponse.<Void>failure(resolveMessage(exception)),
                headers,
                status
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(
            BadRequestException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>failure(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(
            UnauthorizedException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<Void>failure(exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>failure(exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflictException(
            ConflictException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>failure(exception.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception
    ) {
        logger.warn("Database integrity constraint violation", exception);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>failure(
                        "A resource with the same unique value already exists"
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(
            Exception exception
    ) {
        logger.error("An unexpected error occurred", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Void>failure("An unexpected error occurred"));
    }

    private String resolveMessage(Exception exception) {
        if (exception instanceof NoHandlerFoundException
                || exception instanceof NoResourceFoundException) {
            return "Resource not found";
        }

        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return "HTTP method not supported";
        }

        if (exception instanceof HttpMediaTypeNotSupportedException) {
            return "Unsupported media type";
        }

        if (exception instanceof TypeMismatchException) {
            return "Invalid request parameter";
        }

        if (exception instanceof HttpMessageNotReadableException) {
            return "Invalid request body";
        }

        return "Request could not be processed";
    }
}
