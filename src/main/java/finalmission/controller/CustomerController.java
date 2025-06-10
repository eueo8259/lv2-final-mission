package finalmission.controller;

import finalmission.dto.customer.CustomerRequest;
import finalmission.dto.customer.CustomerResponse;
import finalmission.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public CustomerResponse create(@RequestBody CustomerRequest request) {
        return customerService.saveCustomer(request);
    }
}
