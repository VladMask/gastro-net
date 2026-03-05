package grsu.by.service;

import grsu.by.dto.orderDto.OrderCreationDto;
import grsu.by.dto.orderDto.OrderShortDto;

public interface OrderService {
    OrderShortDto create(OrderCreationDto creationDto);
}
