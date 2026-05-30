package grsu.by.service;

import grsu.by.dto.orderDto.OrderCreationDto;
import grsu.by.dto.orderDto.OrderFullDto;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderShortDto create(OrderCreationDto creationDto);
    OrderShortDto findById(Long id);
    OrderFullDto findByIdWithDetails(Long id);
    List<OrderShortDto> findByUserId(Long userId);
    OrderShortDto updateStatus(Long id, OrderStatus status);
    List<OrderShortDto> findByRestaurantId(Long restaurantId);
}
