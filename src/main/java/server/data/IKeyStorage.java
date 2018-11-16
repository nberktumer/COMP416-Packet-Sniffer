package server.data;

public interface IKeyStorage {
    String getKey(String key);

    boolean containsKey(String key);

    void setKey(String key, String value);
}
