package com.jovisco.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


import com.jovisco.spring6restmvc.entities.BeerOrder;
import com.jovisco.spring6restmvc.entities.BeerOrderLine;
import com.jovisco.spring6restmvc.entities.BeerOrderShipment;
import com.jovisco.spring6restmvc.model.BeerOrderDTO;
import com.jovisco.spring6restmvc.model.BeerOrderLineDTO;
import com.jovisco.spring6restmvc.model.BeerOrderShipmentDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeerOrderMapper {

    BeerOrder dtoToOrder(BeerOrderDTO beerOrderDTO);
    BeerOrderDTO orderToDto(BeerOrder beerOrder);

    BeerOrderLine dtoToOrderLine(BeerOrderLineDTO beerOrderLineDTO);
    BeerOrderLineDTO orderLineToDto(BeerOrderLine beerOrderLine);

    BeerOrderShipment dtoToOrderShipment(BeerOrderShipmentDTO beerOrderShipmentDTO);
    BeerOrderShipmentDTO orderShipmentToDto(BeerOrderShipment beerOrderShipment);

}
