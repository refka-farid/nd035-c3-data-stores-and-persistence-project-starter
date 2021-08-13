package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = convertCustomerDTOToCustomer(customerDTO);
        return convertCustomerToCustomerDTO(customerService.saveCustomer(customer));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO> customersDto = new ArrayList<>();
        if (!customers.isEmpty()) {
            for (Customer customer : customers) {
                customersDto.add(convertCustomerToCustomerDTO(customer));
            }
        }
        return customersDto;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Customer customer = customerService.getCustomerByPetId(petId);
        if (customer != null) return convertCustomerToCustomerDTO(customer);
        else throw new CustomerNotFoundException();
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeToEmployeeDto(employeeDTO);
        return convertEmployeeToEmployeeDto(employeeService.saveEmployee(employee));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee != null) {
            return convertEmployeeToEmployeeDto(employee);
        } else {
            throw new EmployeeNotFoundException("Employee with this ID not found");
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee != null) {
            employee.setDaysAvailable(daysAvailable);
        } else {
            throw new EmployeeNotFoundException("Employee with this ID not found");
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employeesForService = employeeService.findEmployeesForService(employeeDTO);
        List<EmployeeDTO> employeesAvailableForService = new ArrayList<>();

        for (Employee employee : employeesForService) {
            employeesAvailableForService.add(convertEmployeeToEmployeeDto(employee));
        }
        return employeesAvailableForService;
    }

    private Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        List<Long> petsIds = customerDTO.getPetIds();
        List<Pet> pets = new ArrayList<>();
        if (petsIds != null) {
            for (long id : petsIds) {
                pets.add(petService.getPetById(id));
            }
            customer.setPets(pets);
        } else {
            customer.setPets(pets);
        }
        return customer;
    }

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Pet> pets = customer.getPets();
        if (pets != null) {
            List<Long> petIds = new ArrayList<>();
            for (Pet pet : pets) {
                petIds.add(pet.getId());
            }
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private EmployeeDTO convertEmployeeToEmployeeDto(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private Employee convertEmployeeToEmployeeDto(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }
}
