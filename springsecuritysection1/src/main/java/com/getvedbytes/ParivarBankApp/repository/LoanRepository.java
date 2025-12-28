package com.getvedbytes.ParivarBankApp.repository;

import java.util.List;

import com.getvedbytes.ParivarBankApp.model.Loans;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoanRepository extends CrudRepository<Loans, Long> {
	
	List<Loans> findByCustomerIdOrderByStartDtDesc(long customerId);

}