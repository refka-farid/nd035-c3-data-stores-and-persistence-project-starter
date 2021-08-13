package com.udacity.jdnd.course3.critter.user;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException() {
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
