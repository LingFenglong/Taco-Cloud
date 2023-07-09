package com.taco.tacocloud.tacos.data;

import com.taco.tacocloud.tacos.Order;

public interface OrderRepository {
    Order save(Order taco);
}
