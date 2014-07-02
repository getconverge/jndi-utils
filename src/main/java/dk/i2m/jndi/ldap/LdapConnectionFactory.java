/*
 * LdapConnectionFactory.java
 *
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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.spi.ObjectFactory;

/**
 * JNDI {@link ObjectFactory} for storing and serving LDAP connections.
 * The connection factory creates a {@link DirContext} with the environment set
 * as the properties of the JNDI reference in the application server. Since
 * the connection factory is for LDAP connections the LDAP properties specified
 * in {@link Context}. The property name should be the constants encaptulated
 * by the static properties exposed by {@link Context}. For example, instead of
 * naming a property {@link Context#SECURITY_AUTHENTICATION} it should be named
 * <code>java.naming.security.authentication</code>.<br/><br/>
 * <h2>Usage</h2>
 * <ol>
 *     <li>
 *         Copy the library jar file (<code>i2m-jndi.jar</code>) to the
 *         application server library directory
 *         (e.g. <code>${glassfish.home}/lib</code>) and restart the application
 *         server.
 *     </li>
 *     <li>
 *         Create a custom JNDI reference with the following settings:
 *         <ul>
 *             <li>JNDI Name = <code>ldap/&lt;your-custom-name&gt;</code></li>
 *             <li>Resource Type = <code>javax.naming.directory.DirContext</code></li>
 *             <li>Factory Class = <code>dk.i2m.jndi.ldap.LdapConnectionFactory</code></li>
 *         </ul>
 *     </li>
 * </ol>
 * 
 * The properties of the LDAP connection depends on the connecting server.
 * Example of properties to connect to an Apache DS 1.5 server:<br/><br/>
 * 
 * <table border="1">
 *  <tr>
 *      <th>Name</th>
 *      <th>Value</th>
 *  </tr>
 *  <tr>
 *      <td><code>java.naming.factory.initial</code></td>
 *      <td><code>com.sun.jndi.ldap.LdapCtxFactory</code></td>
 *  </tr>
 *  <tr>
 *      <td><code>java.naming.security.authentication</code></td>
 *      <td><code>simple</code></td>
 *  </tr>
 *  <tr>
 *      <td><code>java.naming.provider.url</code></td>
 *      <td><code>ldap://localhost:10389/ou=system</code></td>
 *  </tr>
 *  <tr>
 *      <td><code>java.naming.security.principal</code></td>
 *      <td><code>uid=admin,ou=system</code></td>
 *  </tr>
 *  <tr>
 *      <td><code>java.naming.security.credentials</code></td>
 *      <td><code>secret</code></td>
 *  </tr>
 * </table>
 *
 * @see javax.naming.Context
 * @author <a href="mailto:allan@i2m.dk">Allan Lykke Christensen</a>
 */
public class LdapConnectionFactory implements ObjectFactory {

    private static Logger log = Logger.getLogger(LdapConnectionFactory.class.
            getName());

    /**
     * Creates a new instance of {@link LdapConnectionFactory}.
     */
    public LdapConnectionFactory() {
    }

    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
            Hashtable<?, ?> environment) throws Exception {

        Reference reference = (Reference) obj;
        Hashtable env = new Hashtable();

        // Copy all properties to the connection environment
        Enumeration<RefAddr> references = reference.getAll();
        while (references.hasMoreElements()) {
            RefAddr addr = references.nextElement();
            env.put(addr.getType(), addr.getContent());
            log.config("Property [" + addr.getType() + "] = [" +
                    addr.getContent() + "]");
        }

        DirContext dscontext = new InitialDirContext(env);
        return dscontext;
    }
}
