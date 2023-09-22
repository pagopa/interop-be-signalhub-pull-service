package it.pagopa.interop.signalhub.pull.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Utility {

    private Utility(){
        // private constructor
    }

    public static Mono<String> getFromContext(String key, String defaultValue){
        return Mono.deferContextual(ctx -> {
            String value = ctx.getOrDefault(key, defaultValue);
            if (value == null) return Mono.empty();
            return Mono.just(value);
        });
    }

    public static <T> T jsonToObject(ObjectMapper objectMapper, String json, Class<T> tClass){
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            log.error("Error with mapping : {}", e.getMessage(), e);
            return null;
        }
    }

    public static <T> String objectToJson(ObjectMapper objectMapper, T object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error with mapping : {}", e.getMessage(), e);
            return null;
        }
    }
}
