package finalmission.dto.customer;

import finalmission.entity.Customer;

public record CustomerRequest(
        String email,
        String name
) {
    public Customer convertToCustomer(){
        return new Customer(email, name);
    }
}
