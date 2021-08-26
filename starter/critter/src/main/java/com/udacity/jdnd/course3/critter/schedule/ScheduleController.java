package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService schedulerService;

    @Autowired
    private PetService petService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertScheduleDtoToSchedule(scheduleDTO);
        Schedule schedule1 = schedulerService.createSchedule(schedule);
        return convertScheduleToScheduleDto(schedule1);
    }

    @GetMapping("schedule")
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> allSchedules = schedulerService.getAllSchedules();
        List<ScheduleDTO> allSchedulesDto = new ArrayList<>();
        for (Schedule schedule : allSchedules) {
            allSchedulesDto.add(convertScheduleToScheduleDto(schedule));
        }
        return allSchedulesDto;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> petSchedules = schedulerService.getScheduleForPet(petId);
        List<ScheduleDTO> allSchedulesDto = new ArrayList<>();
        for (Schedule schedule : petSchedules) {
            allSchedulesDto.add(convertScheduleToScheduleDto(schedule));
        }
        return allSchedulesDto;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> petSchedules = schedulerService.getScheduleForEmployee(employeeId);
        List<ScheduleDTO> allSchedulesDto = new ArrayList<>();
        for (Schedule schedule : petSchedules) {
            allSchedulesDto.add(convertScheduleToScheduleDto(schedule));
        }
        return allSchedulesDto;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> petSchedules = schedulerService.getScheduleForCustomer(customerId);
        List<ScheduleDTO> allSchedulesDto = new ArrayList<>();
        for (Schedule schedule : petSchedules) {
            allSchedulesDto.add(convertScheduleToScheduleDto(schedule));
        }
        return allSchedulesDto;
    }

    private Schedule convertScheduleDtoToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        List<Employee> employees = new ArrayList<>();
        for (Long employeeId : scheduleDTO.getEmployeeIds()) {
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee != null) {
                employees.add(employee);
            }
        }
        schedule.setEmployees(employees);

        List<Pet> pets = new ArrayList<>();
        for (Long petId : scheduleDTO.getPetIds()) {
            Pet pet = petService.getPetById(petId);
            if (pet != null) {
                pets.add(pet);
            }
        }
        schedule.setPets(pets);
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        return schedule;
    }

    private ScheduleDTO convertScheduleToScheduleDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        List<Long> employeeIds = new ArrayList<>();
        for (Employee employee : schedule.getEmployees()) {
            employeeIds.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeIds);

        List<Long> petIds = new ArrayList<>();
        for (Pet pet : schedule.getPets()) {
            petIds.add(pet.getId());
        }
        scheduleDTO.setPetIds(petIds);

        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());
        return scheduleDTO;
    }
}
