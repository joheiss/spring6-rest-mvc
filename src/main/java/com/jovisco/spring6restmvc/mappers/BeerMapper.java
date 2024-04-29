package com.jovisco.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.model.BeerDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO beerDTO);
    BeerDTO beerToBeerDto(Beer beer);

}
