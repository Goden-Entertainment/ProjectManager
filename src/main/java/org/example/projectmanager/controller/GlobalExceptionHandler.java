package org.example.projectmanager.controller;

import org.example.projectmanager.exceptions.DatabaseOperationException;
import org.example.projectmanager.exceptions.ProfileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({DatabaseOperationException.class})
    public String handleDatabaseError(Exception ex, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", "Der opstod en fejl med databasen.");
        return "errorpage";
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public String handleNotFound(ProfileNotFoundException ex, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "Not Found");
        model.addAttribute("message", ex.getMessage());
        return "errorpage";
    }
}
