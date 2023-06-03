package hu.rivalsnetwork.rivalsapi.users;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Key {
    private final String namespace;
    private final Object value;

    public Key(@NotNull final String namespace, @NotNull final Object value) {
        this.namespace = namespace;
        this.value = value;
    }

    public String toString() {
        return namespace + ":" + value;
    }

    public Object value() {
        return value;
    }

    public String namespace() {
        return namespace;
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static Key of(@NotNull final String namespace, @NotNull final Object value) {
        return new Key(namespace, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Key other = (Key) obj;
        return hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, value);
    }
}
