package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.StockListChangedEvent;
import seedu.address.model.item.Item;

/**
 * Represents the in-memory model of the stock list data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedStockList versionedStockList;
    private final FilteredList<Item> filteredItems;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyStockList addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with stock list: " + addressBook + " and user prefs " + userPrefs);

        versionedStockList = new VersionedStockList(addressBook);
        filteredItems = new FilteredList<>(versionedStockList.getItemList());
    }

    public ModelManager() {
        this(new StockList(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyStockList newData) {
        versionedStockList.resetData(newData);
        indicateStockListChanged();
    }

    @Override
    public ReadOnlyStockList getStockList() {
        return versionedStockList;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateStockListChanged() {
        raise(new StockListChangedEvent(versionedStockList));
    }

    @Override
    public boolean hasItem(Item item) {
        requireNonNull(item);
        return versionedStockList.hasItem(item);
    }

    @Override
    public void deleteItem(Item target) {
        versionedStockList.removeItem(target);
        indicateStockListChanged();
    }

    @Override
    public void addItem(Item item) {
        versionedStockList.addItem(item);
        updateFilteredItemList(PREDICATE_SHOW_ALL_PERSONS);
        indicateStockListChanged();
    }

    @Override
    public void updateItem(Item target, Item editedItem) {
        requireAllNonNull(target, editedItem);

        versionedStockList.updateItem(target, editedItem);
        indicateStockListChanged();
    }

    //=========== Filtered Item List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Item} backed by the internal list of
     * {@code versionedStockList}
     */
    @Override
    public ObservableList<Item> getFilteredItemList() {
        return FXCollections.unmodifiableObservableList(filteredItems);
    }

    @Override
    public void updateFilteredItemList(Predicate<Item> predicate) {
        requireNonNull(predicate);
        filteredItems.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoStockList() {
        return versionedStockList.canUndo();
    }

    @Override
    public boolean canRedoStockList() {
        return versionedStockList.canRedo();
    }

    @Override
    public void undoStockList() {
        versionedStockList.undo();
        indicateStockListChanged();
    }

    @Override
    public void redoStockList() {
        versionedStockList.redo();
        indicateStockListChanged();
    }

    @Override
    public void commitStockList() {
        versionedStockList.commit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedStockList.equals(other.versionedStockList)
                && filteredItems.equals(other.filteredItems);
    }

}
