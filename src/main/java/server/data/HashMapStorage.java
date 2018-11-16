package server.data;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HashMapStorage implements IKeyStorage {
    private static final String FILE_NAME = "server_data.obj";

    private ConcurrentMap<String, String> storage;

    public HashMapStorage() {
        this.storage = readMapFromFile();
        if (this.storage == null) {
            this.storage = new ConcurrentHashMap<>();
            saveMapToFile(this.storage);
        }
    }

    /**
     * Gets the saved map file from the disk
     *
     * @return the map object
     */
    @SuppressWarnings("unchecked")
    private static synchronized ConcurrentMap<String, String> readMapFromFile() {
        try (FileInputStream inStream = new FileInputStream(FILE_NAME);
             ObjectInputStream objectInputStream = new ObjectInputStream(inStream)) {

            Object obj = objectInputStream.readObject();
            if (!(obj instanceof ConcurrentMap)) {
                return null;
            }
            return (ConcurrentMap<String, String>) obj;
        } catch (IOException ignored) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves the given map to the disk
     *
     * @param map Map to be saved to the disk
     */
    private static synchronized void saveMapToFile(ConcurrentMap<String, String> map) {
        try (FileOutputStream outStream = new FileOutputStream(FILE_NAME);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream)) {

            objectOutputStream.writeObject(map);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the value from the saved map
     *
     * @param key key value to get the value from the map
     * @return the value according to the given key
     */
    @Override
    public String getKey(String key) {
        return this.storage.get(key);
    }

    /**
     * Checks whether the key exists
     *
     * @param key key value to be checked
     * @return true if the given key exists
     */
    @Override
    public boolean containsKey(String key) {
        return this.storage.containsKey(key);
    }

    /**
     * Saves a new key, value pair to the map. If the key already exists, it overrides the old value.
     *
     * @param key   key to be saved
     * @param value value to be saved
     */
    @Override
    public void setKey(String key, String value) {
        this.storage.put(key, value);
        saveMapToFile(this.storage);
    }
}
