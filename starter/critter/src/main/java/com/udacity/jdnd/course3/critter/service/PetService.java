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
public class PetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Pet savePet(Pet pet) {
        Pet petToSave = petRepository.save(pet);
        Customer customer = petToSave.getCustomer();
        if (customer != null) {
            customer.addPet(petToSave);
            customerRepository.save(customer);
        }
        return petToSave;
    }

    public Pet savePet(Pet pet, long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        if (customer != null) pet.setCustomer(customer);
        else {
            throw new CustomerNotFoundException();
        }
        return this.petRepository.save(pet);
    }

    public Pet getPetById(Long id) {
        String message = String.format("No pet exists or the id %s provided.", id);
        return this.petRepository.findById(id).orElseThrow(() -> new NoDataFoundForRequest(message));
    }

    public List<Pet> getAllPet() {
        return petRepository.findAllPets();
    }

    public List<Pet> getPetsByOwnerId(Long customerId) {
        String message = String.format("Customer does NOT exist for %s provided.", customerId);
        List<Pet> allPets = petRepository.findAllPets();
        List<Pet> petsByCustomer = new ArrayList<>();
        if (!allPets.isEmpty()) {
            for (Pet pet : allPets) {
                if (pet.getCustomer() != null && pet.getCustomer().getId() == customerId) {
                    petsByCustomer.add(pet);
                }
            }
            if (petsByCustomer.isEmpty()) {
                throw new NoDataFoundForRequest(message);
            } else {
                return petsByCustomer;
            }
        } else {
            throw new NoDataFoundForRequest(message);
        }
    }

    public void deleteAll() {
        petRepository.deleteAll();
    }
}
