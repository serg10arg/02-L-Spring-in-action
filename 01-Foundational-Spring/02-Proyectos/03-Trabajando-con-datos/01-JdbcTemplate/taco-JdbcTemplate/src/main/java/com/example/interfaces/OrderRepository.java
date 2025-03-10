package com.example.interfaces;

import com.example.model.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder order);
}
