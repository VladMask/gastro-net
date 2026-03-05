package grsu.by.patterns.facade.impl;

import grsu.by.OrderRestClient;
import grsu.by.ReservationRestClient;
import grsu.by.patterns.facade.RestClientFacade;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestClientFacadeImpl implements RestClientFacade {
    private final OrderRestClient orderRestClient;
    private final ReservationRestClient reservationRestClient;

    @Override
    public ReservationFullDto findReservationById(Long id) {
        return reservationRestClient.findReservationById(id);
    }

    @Override
    public OrderShortDto findOrderById(Long orderId) {
        return orderRestClient.findOrderById(orderId);
    }
}
