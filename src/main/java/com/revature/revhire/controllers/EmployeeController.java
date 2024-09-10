package com.revature.revhire.controllers;

import com.revature.revhire.exceptions.InvalidCredentialsException;
import com.revature.revhire.models.Employee;
import com.revature.revhire.services.EmployeeService;
import com.revature.revhire.utilities.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Employee>> registerEmployee(@RequestBody Employee employee) {
        BaseResponse<Employee> baseResponse = new BaseResponse<>();
        baseResponse.setMessages("Registration Successful");
        baseResponse.setData(employeeService.createEmployee(employee));
        baseResponse.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Employee>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        BaseResponse<Employee> baseResponse = new BaseResponse<>();

        try {
            Employee employee = employeeService.loginEmployee(email, password);
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessages("Login Successful");
            baseResponse.setData(employee);
            return ResponseEntity.ok(baseResponse);
        } catch (InvalidCredentialsException e) {
            baseResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            baseResponse.setMessages(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessages("Error during authentication: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<Employee>> updateEmployee(@RequestBody Employee employee){
        BaseResponse<Employee> baseResponse=new BaseResponse<>();
        baseResponse.setData(employeeService.updateEmployee(employee.getEmployeeId(),employee));
        baseResponse.setMessages("Updated successfull");
        baseResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @GetMapping("/otp")
    public  ResponseEntity<BaseResponse<String>> generateOTP(String email){
        BaseResponse<String> baseResponse=new BaseResponse<>();
        baseResponse.setData(employeeService.generateOtp(email));
        baseResponse.setStatus(HttpStatus.CREATED.value());
        baseResponse.setMessages("OTP generated");
        return new ResponseEntity<>(baseResponse,HttpStatus.CREATED);
    }

}

