package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(long petId) {
        return scheduleRepository.findByPets_Id(petId);
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        return scheduleRepository.findByEmployees_Id(employeeId);
    }

    public List<Schedule> getScheduleForCustomer(long customerId) {
        List<Schedule> customerSchedules = new ArrayList<>();
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer != null) {
            List<Pet> pets = customer.getPets();
            for (Pet pet : pets) {
                List<Schedule> scheduleForPet = getScheduleForPet(pet.getId());
                customerSchedules.addAll(scheduleForPet);
            }
        }
        return customerSchedules;
    }

    public void deleteAll() {
        scheduleRepository.deleteAll();
    }
}
