package de.haevn.v2.api;


import com.fasterxml.jackson.core.type.TypeReference;
import de.haevn.network.NetworkInteraction;
import de.haevn.network.NetworkUtils;
import de.haevn.utils.SerializationUtils;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface IApi<T> {
    CompletableFuture<T> refresh();

    default <K> CompletableFuture<Optional<K>> get(String url, TypeReference<K> typeReference, String ... elements){
        return CompletableFuture.supplyAsync(()->{
            final var queryResult = NetworkInteraction.sendGetRequest(url);
            if(queryResult.isEmpty() || NetworkUtils.isNot2xx(queryResult.get().statusCode())){
                return Optional.empty();
            }
            final String json = queryResult.get().body();
            return SerializationUtils.getElementSecure(json, typeReference, elements);
        });
    }
}
