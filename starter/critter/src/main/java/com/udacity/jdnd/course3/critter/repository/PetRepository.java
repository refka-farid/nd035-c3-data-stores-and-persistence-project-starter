package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT p FROM Pet p where p.id = :id")
    Pet findPetById(Long id);

    @Query("SELECT p FROM Pet p")
    List<Pet> findAllPets();
//    List<Pet> findByCustomer(Long id);
    List<Pet> findPetsByCustomer(Customer customer);
    List<Pet> findPetsByCustomerId(Long id);
}
