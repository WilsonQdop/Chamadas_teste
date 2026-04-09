package com.WilsonQdop.Chamadas.repositories;

import com.WilsonQdop.Chamadas.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository  extends JpaRepository<Customer, UUID> {
}
