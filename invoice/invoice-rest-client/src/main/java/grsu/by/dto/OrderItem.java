package grsu.by.dto;

import grsu.by.dto.mealDto.MealInvoiceInfo;
import grsu.by.dto.orderMealDto.OrderMealFullDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String name;
    private Short quantity;
    private BigDecimal price;

    public OrderItem(OrderMealFullDto orderMealFullDto, MealInvoiceInfo mealInvoiceInfo) {
        this.name = mealInvoiceInfo.getName();
        this.quantity = orderMealFullDto.getQuantity();
        this.price = orderMealFullDto.getPrice();
    }
}
