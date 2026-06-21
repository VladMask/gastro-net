package grsu.by.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.floorSchemaDto.FloorElementDto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;

@Converter
public class FloorElementsConverter
        implements AttributeConverter<List<FloorElementDto>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override @SneakyThrows
    public String convertToDatabaseColumn(List<FloorElementDto> attr) {
        return attr != null ? objectMapper.writeValueAsString(attr) : "[]";
    }

    @Override @SneakyThrows
    public List<FloorElementDto> convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        return objectMapper.readValue(json,
                new TypeReference<List<FloorElementDto>>() {});
    }
}
