package server.data;

public class StorageManager {
    private static volatile StorageManager _instance = null;

    private final IKeyStorage storage;

    private StorageManager() {
        storage = createStorage();
    }

    public static StorageManager getInstance() {
        if (_instance == null) {
            synchronized (StorageManager.class) {
                if (_instance == null) {
                    _instance = new StorageManager();
                }
            }
        }
        return _instance;
    }

    public IKeyStorage getStorage() {
        return this.storage;
    }

    private synchronized IKeyStorage createStorage() {
        return new SQLiteStorage();
    }
}
