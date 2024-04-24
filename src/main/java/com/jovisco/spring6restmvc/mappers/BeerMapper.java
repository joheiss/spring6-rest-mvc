package com.jovisco.spring6restmvc.mappers;

import org.mapstruct.Mapper;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.model.BeerDTO;

@Mapper
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO beerDTO);
    BeerDTO beerToBeerDto(Beer beer);

}
