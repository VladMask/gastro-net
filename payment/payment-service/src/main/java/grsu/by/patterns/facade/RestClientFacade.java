package grsu.by.patterns.facade;

import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.dto.reservationDto.ReservationFullDto;

public interface RestClientFacade {
    ReservationFullDto findReservationById(Long id);
    OrderShortDto findOrderById(Long orderId);
}
