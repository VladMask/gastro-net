package grsu.by.service.impl;

import grsu.by.dto.restaurantTableDto.RestaurantTableCreationDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableFullDto;
import grsu.by.entity.RestaurantTable;
import grsu.by.enums.RestaurantTableStatus;
import grsu.by.repository.RestaurantTableRepository;
import grsu.by.service.RestaurantTableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public RestaurantTableFullDto create(RestaurantTableCreationDto creationDto) {
        RestaurantTable table = mapper.map(creationDto, RestaurantTable.class);
        table.setStatus(RestaurantTableStatus.AVAILABLE);
        return mapper.map(restaurantTableRepository.save(table), RestaurantTableFullDto.class);
    }

    @Override
    public RestaurantTableFullDto findById(Long id) {
        RestaurantTable table = restaurantTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant table with id " + id + " not found"));
        return mapper.map(table, RestaurantTableFullDto.class);
    }

    @Override
    public List<RestaurantTableFullDto> findByRestaurantId(Long restaurantId) {
        return restaurantTableRepository.findByRestaurantId(restaurantId).stream()
                .map(t -> mapper.map(t, RestaurantTableFullDto.class))
                .toList();
    }

    @Override
    public List<RestaurantTableFullDto> findByRestaurantIdAndStatus(Long restaurantId, RestaurantTableStatus status) {
        return restaurantTableRepository.findByRestaurantIdAndStatus(restaurantId, status).stream()
                .map(t -> mapper.map(t, RestaurantTableFullDto.class))
                .toList();
    }

    @Transactional
    @Override
    public RestaurantTableFullDto updateStatus(Long id, RestaurantTableStatus status) {
        RestaurantTable table = restaurantTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant table with id " + id + " not found"));
        table.setStatus(status);
        return mapper.map(restaurantTableRepository.save(table), RestaurantTableFullDto.class);
    }
}