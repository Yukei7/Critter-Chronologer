package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
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
    ScheduleService scheduleService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    public ScheduleDTO convertEntityToScheduleDTO(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);
        return scheduleDTO;
    }

    public Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO, List<Employee> employees, List<Pet> pets) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        return schedule;
    }

    // helper
    private List<Employee> getEmployeesFromIds(List<Long> employeeIds) {
        if (employeeIds == null) {
            return null;
        }
        List<Employee> employees = new ArrayList<>();
        for (Long id : employeeIds) {
            employees.add(employeeService.getEmployeeById(id));
        }
        return employees;
    }

    private List<Pet> getPetsFromIds(List<Long> petIds) {
        if (petIds == null) {
            return null;
        }
        List<Pet> pets = new ArrayList<>();
        for (Long id : petIds) {
            pets.add(petService.getPetById(id));
        }
        return pets;
    }

    private List<Long> getIdsFromEmployees(List<Employee> employees) {
        if (employees == null) {
            return null;
        }
        List<Long> ids = new ArrayList<>();
        for (Employee employee : employees) {
            ids.add(employee.getId());
        }
        return ids;
    }

    private List<Long> getIdsFromPets(List<Pet> pets) {
        if (pets == null) {
            return null;
        }
        List<Long> ids = new ArrayList<>();
        for (Pet pet : pets) {
            ids.add(pet.getId());
        }
        return ids;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertScheduleDTOToEntity(
                scheduleDTO,
                getEmployeesFromIds(scheduleDTO.getEmployeeIds()),
                getPetsFromIds(scheduleDTO.getPetIds()));
        return convertEntityToScheduleDTO(
                scheduleService.save(schedule),
                getIdsFromEmployees(schedule.getEmployees()),
                getIdsFromPets(schedule.getPets()));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getAll();
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(convertEntityToScheduleDTO(
                    schedule,
                    getIdsFromEmployees(schedule.getEmployees()),
                    getIdsFromPets(schedule.getPets())
            ));
        }
        return scheduleDTOs;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getScheduleForPet(petId);
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(convertEntityToScheduleDTO(
                    schedule,
                    getIdsFromEmployees(schedule.getEmployees()),
                    getIdsFromPets(schedule.getPets())
            ));
        }
        return scheduleDTOs;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(convertEntityToScheduleDTO(
                    schedule,
                    getIdsFromEmployees(schedule.getEmployees()),
                    getIdsFromPets(schedule.getPets())
            ));
        }
        return scheduleDTOs;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getScheduleForCustomer(customerId);
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(convertEntityToScheduleDTO(
                    schedule,
                    getIdsFromEmployees(schedule.getEmployees()),
                    getIdsFromPets(schedule.getPets())
            ));
        }
        return scheduleDTOs;
    }
}
