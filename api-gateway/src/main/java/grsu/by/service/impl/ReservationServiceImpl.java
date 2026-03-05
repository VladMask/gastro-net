package grsu.by.service.impl;

import grsu.by.OrderRestClient;
import grsu.by.ReservationRestClient;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import grsu.by.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRestClient reservationRestClient;
    private final OrderRestClient orderRestClient;

    @Override
    public ReservationFullDto create(ReservationCreationDto creationDto) {
        return reservationRestClient.createReservation(creationDto);
    }

    @Override
    public ReservationFullDto findById(Long id) {
        return reservationRestClient.findReservationById(id);
    }

    @Override
    public void confirmReservationByOrderId(Long orderId) {
        OrderShortDto order = orderRestClient.findOrderById(orderId);
        reservationRestClient.confirmReservationById(order.getReservationId());
    }


}
