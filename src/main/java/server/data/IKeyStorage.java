package server.data;

public interface IKeyStorage {
    /**
     * Returns the value from the saved map
     *
     * @param key key value to get the value from the map
     * @return the value according to the given key
     */
    String getKey(String key);

    /**
     * Checks whether the key exists
     *
     * @param key key value to be checked
     * @return true if the given key exists
     */
    boolean containsKey(String key);

    /**
     * Saves a new key, value pair to the map. If the key already exists, it overrides the old value.
     *
     * @param key   key to be saved
     * @param value value to be saved
     */
    void setKey(String key, String value);
}
