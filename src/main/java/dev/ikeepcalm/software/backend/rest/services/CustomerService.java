package dev.ikeepcalm.software.backend.rest.services;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public String getCustomerEmail(Long customerId) {
        return "customer" + customerId + "@example.com";
    }
    
    public String getCustomerPhone(Long customerId) {
        return "+1234567890";
    }
}