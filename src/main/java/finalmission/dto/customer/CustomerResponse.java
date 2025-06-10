package finalmission.dto.customer;

import finalmission.entity.Customer;

public record CustomerResponse(
        String email,
        String name
) {
    public static CustomerResponse of(Customer customer){
        return new CustomerResponse(customer.getEmail(), customer.getName());
    }
}
