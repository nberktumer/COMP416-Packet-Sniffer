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

    private static synchronized void saveMapToFile(ConcurrentMap<String, String> map) {
        try (FileOutputStream outStream = new FileOutputStream(FILE_NAME);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream)) {

            objectOutputStream.writeObject(map);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getKey(String key) {
        return this.storage.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return this.storage.containsKey(key);
    }

    @Override
    public void setKey(String key, String value) {
        this.storage.put(key, value);
        saveMapToFile(this.storage);
    }
}
