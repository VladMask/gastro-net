package grsu.by.service.impl;

import grsu.by.dto.restaurantTableDto.RestaurantTableCreationDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableFullDto;
import grsu.by.entity.RestaurantTable;
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
        return mapper.map(restaurantTableRepository.save(table), RestaurantTableFullDto.class);
    }

    @Transactional
    @Override
    public List<RestaurantTableFullDto> createAll(List<RestaurantTableCreationDto> creationDtos) {
        List<RestaurantTable> tables = creationDtos.stream()
                .map(dto -> mapper.map(dto, RestaurantTable.class))
                .toList();
        return restaurantTableRepository.saveAll(tables).stream()
                .map(t -> mapper.map(t, RestaurantTableFullDto.class))
                .toList();
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

    @Transactional
    @Override
    public void delete(Long id) {
        restaurantTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant table not found: " + id));
        restaurantTableRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAllByIds(List<Long> ids) {
        restaurantTableRepository.deleteAllById(ids);
    }
}