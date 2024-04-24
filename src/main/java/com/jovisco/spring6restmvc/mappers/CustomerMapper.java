package com.jovisco.spring6restmvc.mappers;

import org.mapstruct.Mapper;

import com.jovisco.spring6restmvc.entities.Customer;
import com.jovisco.spring6restmvc.model.CustomerDTO;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO customerDTO);
    CustomerDTO customerToCustomerDto(Customer customer);
}
