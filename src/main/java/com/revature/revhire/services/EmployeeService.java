package com.revature.revhire.services;

import com.revature.revhire.enums.Role;
import com.revature.revhire.exceptions.InvalidCredentialsException;
import com.revature.revhire.exceptions.InvalidEmailException;
import com.revature.revhire.exceptions.NotFoundException;
import com.revature.revhire.models.Employee;
import com.revature.revhire.models.Skills;
import com.revature.revhire.repositories.EmployeeRepository;
import com.revature.revhire.utilities.EmailService;
import com.revature.revhire.utilities.ModelUpdater;
import com.revature.revhire.utilities.PasswordEncrypter;
import com.revature.revhire.utilities.RandomCredentialsGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    SkillsService skillsService;

    @Autowired
    PasswordEncrypter passwordEncrypter;

    @Autowired
    RandomCredentialsGenerator generator;

    @Autowired
    ModelUpdater modelUpdater;

    public Employee createEmployee(Employee employee) {
        // Check if an employee with the given email already exists
        Employee dbEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (dbEmployee == null) {
            if (employee.getSkills() == null) {
                employee.setSkills(new ArrayList<>());
            }

            // Validate the role and set it
            if (employee.getEmployeeRole() == null || !isValidRole(employee.getEmployeeRole())) {
                throw new InvalidCredentialsException("Invalid role");
            }
            String password = employee.getPassword();
            employee.setPassword(passwordEncrypter.hashPassword(password));
            employee.setSkills(new ArrayList<>());
            Employee createdEmployee = employeeRepository.save(employee);
            try {
                sendWelcomeEmail(employee.getEmail(), employee.getFirstName());
            } catch (MessagingException e) {
                throw new InvalidEmailException("Failed to send email to " + employee.getEmail());
            }

            return createdEmployee;
        } else {
            throw new InvalidCredentialsException("Email already exists");
        }
    }
    private boolean isValidRole(Role role) {
        return Arrays.asList(Role.values()).contains(role);
    }


    private void sendWelcomeEmail(String email, String firstName) throws MessagingException {
        String subject = "Welcome to Rev Hire!";
        String body = "Hello " + firstName + ",\n\nYour account has been created successfully. ";
        emailService.sendEmail(email, subject, body);
    }

    public Employee loginEmployee(String email, String password) {
        Employee dbEmployee = employeeRepository.findByEmail(email);
        if (dbEmployee == null) {
            throw new InvalidCredentialsException("Invalid email");
        }
        String hashedPassword = passwordEncrypter.hashPassword(password);
        if (hashedPassword.equals(dbEmployee.getPassword())) {
            dbEmployee.setPassword(null);
            return dbEmployee;
        }
        System.out.println("Password mismatch: " + hashedPassword + " != " + dbEmployee.getPassword());
        throw new InvalidCredentialsException("Invalid password");
    }

    public Employee updateEmployee(long id, Employee employee) {
        Employee dbEmployee = employeeRepository.findById(id).orElseThrow(() -> new InvalidCredentialsException("User not found"));

        List<Skills> existingSkills = skillsService.getAllSkills();
        List<Skills> updatedSkills = employee.getSkills().stream()
                .map(skill -> existingSkills.stream()
                        .filter(s -> s.getSkillName().equals(skill.getSkillName()))
                        .findFirst()
                        .orElse(skill))
                .collect(Collectors.toList());

        employee.setSkills(updatedSkills);

        return employeeRepository.save(modelUpdater.updateFields(dbEmployee, employee));
    }

    public List<Employee> fetchAllEmployees() {
        return employeeRepository.findAll();
    }
    public String generateOtp(String email) {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new InvalidCredentialsException("Email does not exist");
        }
        String OTP = generator.generateOtp();
        String emailBody = "Hello " + employee.getFirstName() + ",\n\n" +
                "Use the following OTP to reset your password: " + OTP + "\n\n" +
                "Best regards,\n" +
                "The RevHire Team";
        try {
            emailService.sendEmail(employee.getEmail(), "Password Reset", emailBody);
        } catch (MessagingException e) {
            throw new InvalidEmailException("Invalid email");
        }

        return OTP;
    }

    public Employee fetchByEmail(String email) {
        Employee dbEmployee = employeeRepository.findByEmail(email);
        if (dbEmployee == null) {
            throw new NotFoundException("Employee with email: " + email + " not found");
        }
        return dbEmployee;
    }

    public Employee updateEmployeePassword(String email, String password) {
        Employee dbEmployee = employeeRepository.findByEmail(email);
        if (dbEmployee == null) {
            throw new NotFoundException("No user with the email " + email);
        }
        dbEmployee.setPassword(passwordEncrypter.hashPassword(password));
        return employeeRepository.save(dbEmployee);
    }
}
