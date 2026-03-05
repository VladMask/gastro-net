package grsu.by.service.impl;

import grsu.by.InvoiceRestClient;
import grsu.by.OrderRestClient;
import grsu.by.PaymentRestClient;
import grsu.by.RestaurantRestClient;
import grsu.by.UserRestClient;
import grsu.by.dto.InvoiceDto;
import grsu.by.dto.OrderItem;
import grsu.by.dto.UserShortDto;
import grsu.by.dto.mealDto.MealInvoiceInfo;
import grsu.by.dto.orderDto.OrderFullDto;
import grsu.by.dto.orderMealDto.OrderMealFullDto;
import grsu.by.dto.paymentDto.PaymentInvoiceDto;
import grsu.by.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final RestaurantRestClient restaurantRestClient;
    private final UserRestClient userRestClient;
    private final PaymentRestClient paymentRestClient;
    private final OrderRestClient orderRestClient;
    private final InvoiceRestClient invoiceRestClient;

    @Override
    public byte[] getInvoiceByPaymentId(Long paymentId) {
        PaymentInvoiceDto payment = paymentRestClient.getInvoiceDataById(paymentId);
        UserShortDto user = userRestClient.findById(payment.getUserId());
        String restaurantName = restaurantRestClient.findRestaurantById(payment.getRestaurantId()).getName();

        OrderFullDto orderFullDto = orderRestClient.findOrderByIdWithDetails(payment.getOrderId());
        List<MealInvoiceInfo> mealInvoiceInfos = restaurantRestClient.findMealsInfoByIds(
                orderFullDto.getOrderMeals().stream().map(OrderMealFullDto::getMealId).collect(Collectors.toList())
        );

        List<OrderItem> items = new ArrayList<>();
        for(int i = 0; i < mealInvoiceInfos.size(); i++) {
            items.add(new OrderItem(orderFullDto.getOrderMeals().get(i), mealInvoiceInfos.get(i)));
        }

        InvoiceDto invoiceDto = InvoiceDto.builder()
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .userEmail(user.getEmail())
                .restaurantName(restaurantName)
                .orderId(payment.getOrderId())
                .createdAt(Instant.now())
                .totalPrice(orderFullDto.getTotalPrice())
                .items(items)
                .build();

        return invoiceRestClient.createInvoiceByPaymentId(invoiceDto);
    }
}
