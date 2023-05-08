package hu.rivalsnetwork.rivalsapi.serializer;

import org.jetbrains.annotations.NotNull;

public interface Serializer {

    String serialize(@NotNull Object object);

    Object deserialize(@NotNull String object);
}
