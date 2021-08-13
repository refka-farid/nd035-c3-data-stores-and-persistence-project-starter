package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertPetDTOToPet(petDTO);
        return convertPetToPetDTO(petService.savePet(pet));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet storedPet = petService.getPetById(petId);
        PetDTO petDto = null;
        if (storedPet != null) {
            petDto = convertPetToPetDTO(storedPet);
        }
        return petDto;
    }

    @GetMapping("pets")
    public List<PetDTO> getPets() {
        List<Pet> pets = petService.getAllPet();
        List<PetDTO> petsDto = new ArrayList<>();
        if (!pets.isEmpty()) {
            for (Pet pet : pets) {
                petsDto.add(convertPetToPetDTO(pet));
            }
        }
        return petsDto;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets =  petService.getPetsByOwnerId(ownerId);
        List<PetDTO> petsDto = new ArrayList<>();
        if (!pets.isEmpty()) {
            for (Pet pet : pets) {
                petsDto.add(convertPetToPetDTO(pet));
            }
        }
        return petsDto;
    }

    private Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        if (petDTO.getOwnerId() != 0) {
            Customer customer = customerService.getCustomerById(petDTO.getOwnerId());
            pet.setCustomer(customer);
        }
        return pet;
    }

    private PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        if (pet.getCustomer() != null) {
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());
        return petDTO;
    }
}
