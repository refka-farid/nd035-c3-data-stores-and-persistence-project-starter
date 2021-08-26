package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer) {
        List<Pet> allPets = petRepository.findAllPets();
        List<Pet> petsByCustomer = new ArrayList<>();
        for (Pet pet : allPets) {
            if (pet.getCustomer()!= null && pet.getCustomer().getId() == customer.getId()) {
                petsByCustomer.add(pet);
            }
        }
        customer.setPets(petsByCustomer);
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findCustomerById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    public Customer getCustomerByPetId(Long id) {
        List<Pet> allPets = petRepository.findAllPets();
        Customer customer = null;
        if (!allPets.isEmpty()) {
            for (Pet pet : allPets) {
                if (pet.getCustomer() != null && pet.getId() == id) {
                        customer = pet.getCustomer();
                    }
            }
        }
        return customer;
    }

    public void deleteAll() {
          customerRepository.deleteAll();
    }
}
