package grsu.by.service;

import grsu.by.dto.orderDto.OrderCreationDto;
import grsu.by.dto.orderDto.OrderFullDto;
import grsu.by.dto.orderDto.OrderShortDto;

public interface OrderService {
    OrderShortDto create(OrderCreationDto creationDto);
    OrderShortDto findById(Long id);
    OrderFullDto findByIdWithDetails(Long id);
}
