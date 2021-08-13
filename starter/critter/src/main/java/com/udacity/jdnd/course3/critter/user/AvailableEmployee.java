package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.Employee;

import java.util.List;

public class AvailableEmployee {

    private Employee employee;
    private List<Boolean> hasAvailableSkills;

    public AvailableEmployee(Employee employee, List<Boolean> hasAvailableSkills) {
        this.employee = employee;
        this.hasAvailableSkills = hasAvailableSkills;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Boolean> getHasAvailableSkills() {
        return hasAvailableSkills;
    }

    public void setHasAvailableSkills(List<Boolean> hasAvailableSkills) {
        this.hasAvailableSkills = hasAvailableSkills;
    }
}
