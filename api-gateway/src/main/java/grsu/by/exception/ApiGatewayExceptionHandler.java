package grsu.by.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.ExceptionDtoFactory;
import grsu.by.dto.ExceptionMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class ApiGatewayExceptionHandler implements WebExceptionHandler {

    private final ExceptionDtoFactory exceptionDtoFactory;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle( ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = resolveStatus(ex);

        log.error("Gateway exception [{}]: {}", status, ex.getMessage());

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ExceptionMessageDto dto = exceptionDtoFactory.build(status, (Exception) ex);

        return writeResponse(exchange, dto);
    }

    private HttpStatus resolveStatus(Throwable ex) {
        if (ex instanceof AuthenticationServiceException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (ex instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (ex instanceof io.jsonwebtoken.JwtException) {
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, ExceptionMessageDto dto) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(dto);
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize error response", e);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}
