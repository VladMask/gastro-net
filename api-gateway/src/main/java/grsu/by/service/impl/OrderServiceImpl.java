package grsu.by.service.impl;

import grsu.by.OrderRestClient;
import grsu.by.dto.orderDto.OrderCreationDto;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRestClient orderRestClient;

    @Override
    public OrderShortDto create(OrderCreationDto creationDto) {
        return orderRestClient.createOrder(creationDto);
    }
}
