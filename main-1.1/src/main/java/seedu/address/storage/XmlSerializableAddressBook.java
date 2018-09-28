package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.StockList;
import seedu.address.model.ReadOnlyStockList;
import seedu.address.model.person.Person;

/**
 * An Immutable StockList that is serializable to XML format
 */
@XmlRootElement(name = "StockList")
public class XmlSerializableStockList {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    @XmlElement
    private List<XmlAdaptedPerson> persons;

    /**
     * Creates an empty XmlSerializableStockList.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableStockList() {
        persons = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableStockList(ReadOnlyStockList src) {
        this();
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this StockList into the model's {@code StockList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedPerson}.
     */
    public StockList toModelType() throws IllegalValueException {
        StockList StockList = new StockList();
        for (XmlAdaptedPerson p : persons) {
            Person person = p.toModelType();
            if (StockList.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            StockList.addPerson(person);
        }
        return StockList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableStockList)) {
            return false;
        }
        return persons.equals(((XmlSerializableStockList) other).persons);
    }
}
