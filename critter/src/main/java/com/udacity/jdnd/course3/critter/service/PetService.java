package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.exception.PetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerService customerService;

    public Pet save(Pet pet) {
        pet = petRepository.save(pet);
        customerService.addPetToCustomer(pet, pet.getCustomer());
        return pet;
    }

    public Pet getPetById(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            return pet.get();
        } else {
            throw new PetNotFoundException("There is no pet with id: " + id);
        }
    }

    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByCustomerId(Long customerId) {
        return petRepository.findPetsByCustomerId(customerId);
    }
}
