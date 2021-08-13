package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.AvailableEmployee;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.getOne(id);
    }

    public List<Employee> findEmployeesForService(EmployeeRequestDTO employeeDTO) {
        List<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> employeesWithAvailableDays = new ArrayList<>();
        List<Employee> employeesAvailableOnSpecificDay = new ArrayList<>();
        List<Employee> availableEmployeesForService = new ArrayList<>();

        if (!allEmployees.isEmpty()) {
            for (Employee employee : allEmployees) {
                if (!employee.getDaysAvailable().isEmpty() || employee.getDaysAvailable() != null) {
                    employeesWithAvailableDays.add(employee);
                }
            }
        }

        if (!employeesWithAvailableDays.isEmpty()) {
            for (Employee employee : employeesWithAvailableDays) {
                for (DayOfWeek day : employee.getDaysAvailable()) {
                    if (day == employeeDTO.getDate().getDayOfWeek()) {
                        employeesAvailableOnSpecificDay.add(employee);
                    }
                }
            }
        }

        List<Boolean> hasSKills = new ArrayList<>();
        List<AvailableEmployee> availableEmployeeList = new ArrayList<>();
        if (!employeesAvailableOnSpecificDay.isEmpty()) {
            for (Employee employee : employeesAvailableOnSpecificDay) {
                if (!employee.getSkills().isEmpty()) {
                    for (EmployeeSkill employeeSkill : employee.getSkills()) {
                        for (EmployeeSkill wantedSkill : employeeDTO.getSkills()) {
                            if (employeeSkill == wantedSkill) {
                                hasSKills.add(true);
                            }
                        }
                    }

                }
                availableEmployeeList.add(new AvailableEmployee(employee, hasSKills));
                hasSKills = new ArrayList<>();
            }
        }

        for (AvailableEmployee availableEmployee : availableEmployeeList) {
            if (availableEmployee.getHasAvailableSkills().size() == employeeDTO.getSkills().size()) {
                availableEmployeesForService.add(availableEmployee.getEmployee());
            }
        }

        return availableEmployeesForService;
    }
}
