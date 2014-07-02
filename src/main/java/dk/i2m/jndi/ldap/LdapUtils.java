/*
 * Copyright (C) 2009 Interactive Media Management
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.jndi.ldap;

import java.util.NoSuchElementException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

/**
 * Static utility class for working with LDAP servers and responses through the
 * <code>javax.naming</code> classes. To obtain verbose logging for this class
 * enable the {@link Level#FINER} level.
 *
 * @author <a href="mailto:allan@i2m.dk">Allan Lykke Christensen</a>
 * @since 1.1
 */
public final class LdapUtils {

    private static final Logger logger = Logger.getLogger(LdapUtils.class.getName());

    /**
     * Prevents creating instances of the static class.
     */
    protected LdapUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Ensures that a value coming from an {@link Attribute} object does not
     * contain a <code>null</code> value or cause an {@link Exception} to be
     * thrown. If an {@link Exception} occur the return value will be blank and
     * the exception will be logged as {@link Level#FINER}.
     *
     * @param att
     *          {@link Attribute} to validate
     * @return {@link String} value of the attribute or an empty {@link String}
     *         if the attribute contained <code>null</code> or did not exist
     */
    public static String validateAttribute(final Attribute att) {
        String returnValue = "";

        try {
            if (att != null) {
                returnValue = att.get().toString();
            }
        } catch (NoSuchElementException e) {
            logger.log(Level.FINER, "Attribute did not exist", e);
            returnValue = "";
        } catch (NamingException e) {
            logger.log(Level.FINER, "Attribute value could not be obtained", e);
            returnValue = "";
        }

        return returnValue;
    }

    /**
     * Ensures that a value coming from an {@link Attribute} object does not
     * contain a <code>null</code> value or cause an {@link Exception} to be
     * thrown. If an {@link Exception} occur the return value will be blank and
     * the exception will be logged as {@link Level#FINER}.
     *
     * @param att
     *          {@link Attribute} to validate
     * @param defaultValue
     *          Value to return if the {@link Attribute} does not exist
     *
     * @return {@link String} value of the attribute or an empty
     *         {@link String} if the attribute contained null or did not exist.
     */
    public static String validateAttribute(final Attribute att,
            final String defaultValue) {
        String returnValue = "";

        try {
            if (att != null) {
                returnValue = att.get().toString();
            }
        } catch (NoSuchElementException e) {
            logger.log(Level.FINER, "Attribute did not exist", e);
            returnValue = defaultValue;
        } catch (NamingException e) {
            logger.log(Level.FINER, "Attribute value could not be obtained", e);
            returnValue = defaultValue;
        }

        return returnValue;
    }

    /**
     * Validates a modification of an attribute. The method will detemine if the
     * attribute needs to be <code>ADDED</code>, <code>REMOVED</code> or
     * <code>MODIFIED</code>.
     *
     * @param key
     *            Key of the attribute
     * @param newValue
     *            New value of the attribute
     * @param oldValue
     *            Old value of the attribute
     * @return {@link ModificationItem} for either ADD, REMOVE or MODIFY
     */
    public static ModificationItem validateModification(final String key,
            final String newValue, final String oldValue) {

        ModificationItem mod;

        if (newValue.equalsIgnoreCase("")) {
            if (!oldValue.equalsIgnoreCase("")) {
                mod = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
                        new BasicAttribute(key));
            } else {
                mod = null;
            }
        } else {
            mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute(key, newValue));
        }

        return mod;
    }
}
