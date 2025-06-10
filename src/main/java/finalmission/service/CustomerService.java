package finalmission.service;

import finalmission.dto.customer.CustomerRequest;
import finalmission.dto.customer.CustomerResponse;
import finalmission.entity.Customer;
import finalmission.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse saveCustomer(CustomerRequest request) {
        Customer saveCustomer = customerRepository.save(request.convertToCustomer());
        return CustomerResponse.of(saveCustomer);
    }
}
