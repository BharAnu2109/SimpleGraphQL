package com.banking.graphql.resolver;

import com.banking.graphql.model.Customer;
import com.banking.graphql.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CustomerResolver {

    private final CustomerService customerService;

    @QueryMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @QueryMapping
    public Customer getCustomerById(@Argument Long id) {
        return customerService.getCustomerById(id);
    }

    @QueryMapping
    public Customer getCustomerByEmail(@Argument String email) {
        return customerService.getCustomerByEmail(email);
    }

    @MutationMapping
    public Customer createCustomer(@Argument Map<String, Object> input) {
        String name = (String) input.get("name");
        String email = (String) input.get("email");
        String phone = (String) input.get("phone");
        String address = (String) input.get("address");
        
        return customerService.createCustomer(name, email, phone, address);
    }

    @MutationMapping
    public Customer updateCustomer(@Argument Map<String, Object> input) {
        Long id = Long.parseLong(input.get("id").toString());
        String name = (String) input.get("name");
        String phone = (String) input.get("phone");
        String address = (String) input.get("address");
        
        return customerService.updateCustomer(id, name, phone, address);
    }

    @MutationMapping
    public Boolean deleteCustomer(@Argument Long id) {
        return customerService.deleteCustomer(id);
    }
}
