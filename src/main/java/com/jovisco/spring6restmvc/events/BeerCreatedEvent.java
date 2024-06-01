package com.jovisco.spring6restmvc.events;

import org.springframework.security.core.Authentication;

import com.jovisco.spring6restmvc.entities.Beer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class BeerCreatedEvent {

    private Beer beer;
    private Authentication authentication;
}
