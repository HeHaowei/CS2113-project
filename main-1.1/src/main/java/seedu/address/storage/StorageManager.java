package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.StockListChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyStockList;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of StockList data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private StockListStorage StockListStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(StockListStorage StockListStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.StockListStorage = StockListStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ StockList methods ==============================

    @Override
    public Path getStockListFilePath() {
        return StockListStorage.getStockListFilePath();
    }

    @Override
    public Optional<ReadOnlyStockList> readStockList() throws DataConversionException, IOException {
        return readStockList(StockListStorage.getStockListFilePath());
    }

    @Override
    public Optional<ReadOnlyStockList> readStockList(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return StockListStorage.readStockList(filePath);
    }

    @Override
    public void saveStockList(ReadOnlyStockList StockList) throws IOException {
        saveStockList(StockList, StockListStorage.getStockListFilePath());
    }

    @Override
    public void saveStockList(ReadOnlyStockList StockList, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        StockListStorage.saveStockList(StockList, filePath);
    }

    @Override
    public void backupStockList(ReadOnlyStockList StockList) throws IOException {
        StockListStorage.backupStockList(StockList);
    }


    @Override
    @Subscribe
    public void handleStockListChangedEvent(StockListChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveStockList(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
