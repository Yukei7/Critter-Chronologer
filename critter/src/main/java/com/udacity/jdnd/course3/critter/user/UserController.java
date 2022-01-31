package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
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
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    public Customer convertCustomerDTOToEntity(CustomerDTO customerDTO, List<Pet> pets) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customer.setPets(pets);
        return customer;
    }

    public CustomerDTO convertEntityToCustomerDTO(Customer customer, List<Long> petIds) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    public static EmployeeDTO convertEntityToEmployeeDTO(Employee employee)
    {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    public static Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO)
    {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    // helper
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

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertCustomerDTOToEntity(
                customerDTO,
                getPetsFromIds(customerDTO.getPetIds())
        );
        return convertEntityToCustomerDTO(
                customerService.save(customer),
                getIdsFromPets(customer.getPets())
        );
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        List<Customer> customers = customerService.getAll();
        for (Customer customer : customers) {
            customerDTOs.add(convertEntityToCustomerDTO(
                    customer,
                    getIdsFromPets(customer.getPets())
            ));
        }
        return customerDTOs;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return convertEntityToCustomerDTO(
                customerService.getByPetId(petId),
                getIdsFromPets(customerService.getByPetId(petId).getPets())
        );
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return convertEntityToEmployeeDTO(employeeService.save(convertEmployeeDTOToEntity(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEntityToEmployeeDTO(employeeService.getEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        List<Employee> employees = employeeService.getAvailableEmployeesWithSkills(employeeDTO.getSkills(), employeeDTO.getDate());
        for (Employee employee : employees) {
            employeeDTOs.add(convertEntityToEmployeeDTO(employee));
        }
        return employeeDTOs;
    }

}
