package com.example.interfaces;

import com.example.model.TacoOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository <TacoOrder, Long> {
}
