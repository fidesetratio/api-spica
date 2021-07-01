package com.app.utils;

import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.ParseException;
import java.text.ParsePosition;

public abstract class Format implements Serializable, Cloneable {
	
	
	private static final long serialVersionUID = -299282585814624189L;

    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected Format() {
    }

    /**
     * Formats an object to produce a string. This is equivalent to
     * <blockquote>
     * {@link #format(Object, StringBuffer, FieldPosition) format}<code>(obj,
     *         new StringBuffer(), new FieldPosition(0)).toString();</code>
     * </blockquote>
     *
     * @param obj    The object to format
     * @return       Formatted string.
     * @exception IllegalArgumentException if the Format cannot format the given
     *            object
     */
    public final String format (Object obj) {
        return format(obj, new StringBuffer(), new FieldPosition(0)).toString();
    }

    /**
     * Formats an object and appends the resulting text to a given string
     * buffer.
     * If the <code>pos</code> argument identifies a field used by the format,
     * then its indices are set to the beginning and end of the first such
     * field encountered.
     *
     * @param obj    The object to format
     * @param toAppendTo    where the text is to be appended
     * @param pos    A <code>FieldPosition</code> identifying a field
     *               in the formatted text
     * @return       the string buffer passed in as <code>toAppendTo</code>,
     *               with formatted text appended
     * @exception NullPointerException if <code>toAppendTo</code> or
     *            <code>pos</code> is null
     * @exception IllegalArgumentException if the Format cannot format the given
     *            object
     */
    public abstract StringBuffer format(Object obj,
                    StringBuffer toAppendTo,
                    FieldPosition pos);

    /**
     * Formats an Object producing an <code>AttributedCharacterIterator</code>.
     * You can use the returned <code>AttributedCharacterIterator</code>
     * to build the resulting String, as well as to determine information
     * about the resulting String.
     * <p>
     * Each attribute key of the AttributedCharacterIterator will be of type
     * <code>Field</code>. It is up to each <code>Format</code> implementation
     * to define what the legal values are for each attribute in the
     * <code>AttributedCharacterIterator</code>, but typically the attribute
     * key is also used as the attribute value.
     * <p>The default implementation creates an
     * <code>AttributedCharacterIterator</code> with no attributes. Subclasses
     * that support fields should override this and create an
     * <code>AttributedCharacterIterator</code> with meaningful attributes.
     *
     * @exception NullPointerException if obj is null.
     * @exception IllegalArgumentException when the Format cannot format the
     *            given object.
     * @param obj The object to format
     * @return AttributedCharacterIterator describing the formatted value.
     * @since 1.4
     */
    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        return createAttributedCharacterIterator(format(obj));
    }

    /**
     * Parses text from a string to produce an object.
     * <p>
     * The method attempts to parse text starting at the index given by
     * <code>pos</code>.
     * If parsing succeeds, then the index of <code>pos</code> is updated
     * to the index after the last character used (parsing does not necessarily
     * use all characters up to the end of the string), and the parsed
     * object is returned. The updated <code>pos</code> can be used to
     * indicate the starting point for the next call to this method.
     * If an error occurs, then the index of <code>pos</code> is not
     * changed, the error index of <code>pos</code> is set to the index of
     * the character where the error occurred, and null is returned.
     *
     * @param source A <code>String</code>, part of which should be parsed.
     * @param pos A <code>ParsePosition</code> object with index and error
     *            index information as described above.
     * @return An <code>Object</code> parsed from the string. In case of
     *         error, returns null.
     * @exception NullPointerException if <code>pos</code> is null.
     */
    public abstract Object parseObject (String source, ParsePosition pos);

    /**
     * Parses text from the beginning of the given string to produce an object.
     * The method may not use the entire text of the given string.
     *
     * @param source A <code>String</code> whose beginning should be parsed.
     * @return An <code>Object</code> parsed from the string.
     * @exception ParseException if the beginning of the specified string
     *            cannot be parsed.
     */
    public Object parseObject(String source) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Object result = parseObject(source, pos);
        if (pos.getIndex() == 0) {
            throw new ParseException("Format.parseObject(String) failed",
                pos.getErrorIndex());
        }
        return result;
    }

    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // will never happen
            return null;
        }
    }

    //
    // Convenience methods for creating AttributedCharacterIterators from
    // different parameters.
    // 

    /**
     * Creates an <code>AttributedCharacterIterator</code> for the String
     * <code>s</code>.
     *
     * @param s String to create AttributedCharacterIterator from
     * @return AttributedCharacterIterator wrapping s
     */
    AttributedCharacterIterator createAttributedCharacterIterator(String s) {
	AttributedString as = new AttributedString(s);

	return as.getIterator();
    }

    /**
     * Returns an AttributedCharacterIterator with the String
     * <code>string</code> and additional key/value pair <code>key</code>,
     * <code>value</code>.
     *
     * @param string String to create AttributedCharacterIterator from
     * @param key Key for AttributedCharacterIterator
     * @param value Value associated with key in AttributedCharacterIterator
     * @return AttributedCharacterIterator wrapping args
     */
    AttributedCharacterIterator createAttributedCharacterIterator(
                      String string, AttributedCharacterIterator.Attribute key,
                      Object value) {
        AttributedString as = new AttributedString(string);

        as.addAttribute(key, value);
        return as.getIterator();
    }

    /**
     * Creates an AttributedCharacterIterator with the contents of
     * <code>iterator</code> and the additional attribute <code>key</code>
     * <code>value</code>.
     *
     * @param iterator Initial AttributedCharacterIterator to add arg to
     * @param key Key for AttributedCharacterIterator
     * @param value Value associated with key in AttributedCharacterIterator
     * @return AttributedCharacterIterator wrapping args
     */
    AttributedCharacterIterator createAttributedCharacterIterator(
	      AttributedCharacterIterator iterator,
              AttributedCharacterIterator.Attribute key, Object value) {
	AttributedString as = new AttributedString(iterator);

	as.addAttribute(key, value);
	return as.getIterator();
    }


    /**
     * Defines constants that are used as attribute keys in the
     * <code>AttributedCharacterIterator</code> returned
     * from <code>Format.formatToCharacterIterator</code> and as
     * field identifiers in <code>FieldPosition</code>.
     *
     * @since 1.4
     */
    public static class Field extends AttributedCharacterIterator.Attribute {

        // Proclaim serial compatibility with 1.4 FCS
        private static final long serialVersionUID = 276966692217360283L;

        /**
         * Creates a Field with the specified name.
         *
         * @param name Name of the attribute
         */
        protected Field(String name) {
            super(name);
        }
    }


    /**
     * FieldDelegate is notified by the various <code>Format</code>
     * implementations as they are formatting the Objects. This allows for
     * storage of the individual sections of the formatted String for
     * later use, such as in a <code>FieldPosition</code> or for an
     * <code>AttributedCharacterIterator</code>.
     * <p>
     * Delegates should NOT assume that the <code>Format</code> will notify
     * the delegate of fields in any particular order.
     *
     * @see FieldPosition.Delegate
     * @see CharacterIteratorFieldDelegate
     */
    interface FieldDelegate {
        /**
         * Notified when a particular region of the String is formatted. This
         * method will be invoked if there is no corresponding integer field id
         * matching <code>attr</code>.
         *
         * @param attr Identifies the field matched
         * @param value Value associated with the field
         * @param start Beginning location of the field, will be >= 0
         * @param end End of the field, will be >= start and <= buffer.length()
         * @param buffer Contains current formatted value, receiver should
         *        NOT modify it.
         */
        public void formatted(Format.Field attr, Object value, int start,
                              int end, StringBuffer buffer);

        /**
         * Notified when a particular region of the String is formatted.
         *
         * @param fieldID Identifies the field by integer
         * @param attr Identifies the field matched
         * @param value Value associated with the field
         * @param start Beginning location of the field, will be >= 0
         * @param end End of the field, will be >= start and <= buffer.length()
         * @param buffer Contains current formatted value, receiver should
         *        NOT modify it.
         */
        public void formatted(int fieldID, Format.Field attr, Object value,
                              int start, int end, StringBuffer buffer);
    }
}
