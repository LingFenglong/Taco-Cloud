package com.taco.tacocloud.tacos.data;

import com.taco.tacocloud.tacos.Taco;

public interface TacoRepository {
    Taco save(Taco taco);
}
