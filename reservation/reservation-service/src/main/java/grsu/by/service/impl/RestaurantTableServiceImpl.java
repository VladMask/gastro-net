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
        RestaurantTable table = restaurantTableRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Restaurant table with id " + id + " not found")
        );
        return mapper.map(table, RestaurantTableFullDto.class);
    }

}
