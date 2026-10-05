package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.ItemRequestDto;

import java.time.Duration;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory.class)
                        .setConnectTimeout(Duration.ofSeconds(5))
                        .setReadTimeout(Duration.ofSeconds(5))
                        .build()
        );
    }

    public ResponseEntity<Object> findById(Long requestId, Long userId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> create(ItemRequestDto itemRequestDto, Long userId) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getAllRequests(Long userId) {
        return get("/all", userId);
    }

    public ResponseEntity<Object> getUserRequests(Long userId) {
        return get("", userId);
    }
}
