package com.banking.graphql.service;

import com.banking.graphql.exception.BankingException;
import com.banking.graphql.exception.CustomerNotFoundException;
import com.banking.graphql.model.Customer;
import com.banking.graphql.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(String name, String email, String phone, String address) {
        if (customerRepository.existsByEmail(email)) {
            throw new BankingException("Customer with email " + email + " already exists");
        }
        
        Customer customer = new Customer(name, email, phone, address);
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(email));
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer updateCustomer(Long id, String name, String phone, String address) {
        Customer customer = getCustomerById(id);
        
        if (name != null && !name.isEmpty()) {
            customer.setName(name);
        }
        if (phone != null && !phone.isEmpty()) {
            customer.setPhone(phone);
        }
        if (address != null && !address.isEmpty()) {
            customer.setAddress(address);
        }
        
        return customerRepository.save(customer);
    }

    @Transactional
    public boolean deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        
        if (!customer.getAccounts().isEmpty()) {
            throw new BankingException("Cannot delete customer with active accounts");
        }
        
        customerRepository.delete(customer);
        return true;
    }
}
