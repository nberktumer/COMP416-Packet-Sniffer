package server.data;

public class StorageManager {
    private static volatile StorageManager _instance = null;

    private final IKeyStorage storage;

    private StorageManager() {
        storage = createStorage();
    }

    /**
     * Returns the StorageManager instance
     *
     * @return StorageManager instance
     */
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

    /**
     * Gets the storage instance
     *
     * @return storage instance
     */
    public IKeyStorage getStorage() {
        return this.storage;
    }

    /**
     * Creates a new storage instance
     *
     * @return new storage instance
     */
    private synchronized IKeyStorage createStorage() {
        return new SQLiteStorage();
    }
}
