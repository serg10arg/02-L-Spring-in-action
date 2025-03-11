package com.example.interfaces;

import com.example.model.TacoOrder;

import java.util.Optional;

public interface OrderRepository {
    TacoOrder save(TacoOrder order);

    Optional<TacoOrder> findById(Long id);
}
