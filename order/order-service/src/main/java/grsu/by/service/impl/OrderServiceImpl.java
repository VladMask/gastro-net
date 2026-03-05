package grsu.by.service.impl;

import grsu.by.ReservationRestClient;
import grsu.by.dto.orderDto.OrderCreationDto;
import grsu.by.dto.orderDto.OrderFullDto;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.entity.Order;
import grsu.by.entity.OrderMeal;
import grsu.by.enums.OrderStatus;
import grsu.by.repository.OrderMealRepository;
import grsu.by.repository.OrderRepository;
import grsu.by.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMealRepository orderMealRepository;
    private final ModelMapper mapper;
    private final ReservationRestClient reservationRestClient;

    @Transactional
    @Override
    public OrderShortDto create(OrderCreationDto creationDto) {
        try {
            reservationRestClient.findReservationById(creationDto.getReservationId());
        } catch (HttpClientErrorException e) {
            throw new EntityNotFoundException("No active reservation was found");
        }

        Order order = mapper.map(creationDto, Order.class);
        order.setStatus(OrderStatus.CREATED);

        List<OrderMeal> orderMeals = order.getOrderMeals();
        order.setTotalPrice(computeTotalPrice(orderMeals));
        order.setOrderMeals(null);

        Long orderId = orderRepository.save(order).getId();

        orderMeals.forEach(orderMeal -> orderMeal.setOrderId(orderId));
        orderMealRepository.saveAll(orderMeals);

        return mapper.map(order, OrderShortDto.class);
    }

    @Override
    public OrderShortDto findById(Long id) {
        Order order = orderRepository.findWithDetailsById(id).orElseThrow(
                () -> new EntityNotFoundException("Order not found")
        );
        order.setTotalPrice(computeTotalPrice(order.getOrderMeals()));
        return mapper.map(order, OrderShortDto.class);
    }

    @Override
    public OrderFullDto findByIdWithDetails(Long id) {
        Order order = orderRepository.findWithDetailsById(id).orElseThrow(
                () -> new EntityNotFoundException("Order not found")
        );
        order.setTotalPrice(computeTotalPrice(order.getOrderMeals()));
        return mapper.map(order, OrderFullDto.class);
    }

    private BigDecimal computeTotalPrice(List<OrderMeal> meals) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderMeal meal : meals) {
            total = total.add(meal.getPrice().multiply(new BigDecimal(meal.getQuantity())));
        }
        return total;
    }
}
