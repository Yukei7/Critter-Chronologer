package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new UserNotFoundException("There is no customer with id: "+ id);
        }
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer getByPetId(Long petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isPresent()) {
            Customer owner = customerRepository.findByPetsContaining(pet.get());
            return owner;
        } else {
            throw new PetNotFoundException("There is no pet with id: " + petId);
        }
    }

    public void addPetToCustomer(Pet pet, Customer customer) {
        List<Pet> pets = customer.getPets();
        if(pets != null) {
            pets.add(pet);
        } else {
            pets = new ArrayList<>();
            pets.add(pet);
        }
        customer.setPets(pets);
        customerRepository.save(customer);
    }
}
