package models;

public class JsonNode<T> {
    // private models.JSON_TYPE jsonType;
    private final T value;

    public JsonNode(T value) {
        this.value = value;
    }

    public String getClazz() {
        return value.getClass().toString();
    }

}
