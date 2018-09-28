package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.item.Address;
import seedu.address.model.item.Email;
import seedu.address.model.item.Name;
import seedu.address.model.item.Item;
import seedu.address.model.item.Phone;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Item.
 */
public class XmlAdaptedItem {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Item's %s field is missing!";

    @XmlElement(required = true)
    private String name;


    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedItem.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedItem() {}

    /**
     * Constructs an {@code XmlAdaptedItem} with the given item details.
     */
    public XmlAdaptedItem(String name,  List<XmlAdaptedTag> tagged) {
        this.name = name;

        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Item into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedItem
     */
    public XmlAdaptedItem(Item source) {
        name = source.getName().fullName;

        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
    }

    /**
     * Converts this jaxb-friendly adapted Item object into the model's Item object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted Item
     */
    public Item toModelType() throws IllegalValueException {
        final List<Tag> itemTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            itemTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name modelName = new Name(name);


        return new Item(modelName, modelTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedItem)) {
            return false;
        }

        XmlAdaptedItem otherItem = (XmlAdaptedItem) other;
        return Objects.equals(name, otherItem.name)
                && tagged.equals(otherItem.tagged);
    }
}
