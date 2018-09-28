package seedu.address.storage;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores StockList data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given StockList data to the specified file.
     */
    public static void saveDataToFile(Path file, XmlSerializableStockList StockList)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, StockList);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage(), e);
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializableStockList loadDataFromSaveFile(Path file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableStockList.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
