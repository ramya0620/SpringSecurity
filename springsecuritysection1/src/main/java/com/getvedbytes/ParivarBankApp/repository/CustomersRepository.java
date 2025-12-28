package com.getvedbytes.ParivarBankApp.repository;

import com.getvedbytes.ParivarBankApp.model.Customers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomersRepository extends CrudRepository<Customers,Long> {
    Optional<Customers> findByEmail(String email);
}
