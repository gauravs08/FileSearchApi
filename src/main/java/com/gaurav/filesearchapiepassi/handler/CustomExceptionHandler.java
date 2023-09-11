package com.gaurav.filesearchapiepassi.handler;

import com.gaurav.filesearchapiepassi.Exception.FileFormatException;
import com.gaurav.filesearchapiepassi.Exception.FileNotFoundException;
import com.gaurav.filesearchapiepassi.model.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponse> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse(1002, ex.getMessage()));
    }

    @ExceptionHandler(FileFormatException.class)
    public ResponseEntity<ApiResponse> handleFileFormatException(FileFormatException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse(1003, ex.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleFileSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse(1004, ex.getMessage()));
    }


}
