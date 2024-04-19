package com.jovisco.spring6restmvc.services;

import java.util.UUID;

import com.jovisco.spring6restmvc.model.Beer;

public interface BeerService {
    Beer getBeerById(UUID id);
}
