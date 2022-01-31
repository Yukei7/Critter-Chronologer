package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerRepository customerRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(Long petId) {
        Pet pet = petService.getPetById(petId);
        return scheduleRepository.findByPetsContaining(pet);
    }

    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return scheduleRepository.findByEmployeesContaining(employee);
    }

    public List<Schedule> getScheduleForCustomer(Long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        return scheduleRepository.findByPetsIn(customer.getPets());
    }
}
