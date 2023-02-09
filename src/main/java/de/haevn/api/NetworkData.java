package de.haevn.api;

import java.util.Optional;
import java.util.function.Consumer;

public class NetworkData<T> {
    private final NetworkDataChain<T> chain;
    private final Optional<T> data;
    private final Throwable exception;

    public NetworkData(T data) {
        this(data, null);
    }

    public NetworkData(T data, Throwable throwable) {
        this.data = null == data ? Optional.empty() : Optional.of(data);
        this.chain = new NetworkDataChain<>(data);
        this.exception = throwable;
    }

    public T getData() {
        return data.get();
    }

    public Throwable getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }

    public void ifPresent(Consumer<T> action) {
        data.ifPresent(action);
    }

    public boolean hasData() {
        return data.isPresent();
    }

    public NetworkDataChain<T> ifException(Consumer<Throwable> action) {
        chain.done = false;
        if (exception != null) {
            action.accept(exception);
            chain.done = true;
        }
        return chain;
    }

    public void ifExceptionAbsent(Consumer<T> action) {
        if (exception != null && data.isPresent()) {
            action.accept(data.get());
        }
    }

    public static class NetworkDataChain<T> {
        private boolean done = false;
        private final T data;

        NetworkDataChain(T data) {
            this.data = data;
        }

        public void orElse(Consumer<T> action) {
            if (data != null && !done) {
                action.accept(data);
            }
        }
    }
}
