package grsu.by.service;

import grsu.by.dto.orderDto.OrderCreationDto;
import grsu.by.dto.orderDto.OrderFullDto;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderShortDto create(OrderCreationDto creationDto);
    OrderShortDto findById(Long id);
    OrderFullDto findByIdWithDetails(Long id);
    OrderShortDto updateStatus(Long id, OrderStatus status);
    Page<OrderShortDto> findByUserId(Long userId, Pageable pageable);
    Page<OrderShortDto> findByRestaurantId(Long restaurantId, Pageable pageable);
}
