/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2011 by
 * + Christian-Albrechts-University of Kiel
 *     + Department of Computer Science
 *         + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */

package de.cau.cs.kieler.kwebs.server.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * This class provides an application wide configuration context and
 * defines constants and default values for accessing and storing application
 * related configuration parameters.
 *
 * @kieler.design 2011-08-25 reviewed by ckru, msp, mri
 * @author swe
 */
public final class Configuration extends Properties {

    /**
     * 
     */
    private static final long serialVersionUID = -8065497054352065920L;

    /** The singleton instance. */
    private static final Configuration INSTANCE
        = new Configuration();
    
    /** Common prefix for property identifiers. */
    private static final String PREFIX
        = "de.cau.cs.kieler.kwebs.";

    // Properties for HTTP based publishing of the JAXWS service

    /** Shall JAXWS service be published via HTTP? */
    public static final String JAXWS_PUBLISH_HTTP
        = PREFIX + "jaxws.publishHttp";

    /** JAXWS HTTP service address. */
    public static final String JAXWS_HTTP_ADDRESS
        = PREFIX + "jaxws.httpAddress";

    // Preferences for HTTPS based publishing of the JAXWS service

    /** Shall JAXWS service be published via HTTPS? */
    public static final String JAXWS_PUBLISH_HTTPS
        = PREFIX + "jaxws.publishHttps";

    /** JAXWS HTTPS service address. */
    public static final String JAXWS_HTTPS_ADDRESS
        = PREFIX + "jaxws.httpsAddress";

    // Properties for HTTP based publishing of the REST service

    /** Shall REST service be published via HTTP? */
    public static final String REST_PUBLISH_HTTP
        = PREFIX + "rest.publishHttp";

    /** REST HTTP service address. */
    public static final String REST_HTTP_ADDRESS
        = PREFIX + "rest.httpAddress";

    // Preferences for HTTPS based publishing of the REST service

    /** Shall REST service be published via HTTPS? */
    public static final String REST_PUBLISH_HTTPS
        = PREFIX + "rest.publishHttps";

    /** REST HTTPS service address. */
    public static final String REST_HTTPS_ADDRESS
        = PREFIX + "rest.httpsAddress";
    
    // Preferences for the HTTPS configuration, the 
    // HTTPS configuration is used for both JAXWS and REST service
    
    /** Fully qualified path to the JKS formatted key store file. */
    public static final String HTTPSKEYSTORE_JKS_PATH
        = PREFIX + "httpsKeystore.jks.path";

    /** Password for the JKS key store. */
    public static final String HTTPSKEYSTORE_JKS_PASS
        = PREFIX + "httpsKeystore.jks.pass";

    // jETI properties

    /** Shall service be published via jETI? */
    public static final String PUBLISH_JETI
        = PREFIX + "publishJeti";

    /** . */
    public static final String JETI_PROVIDER_ID
        = PREFIX + "jeti.provider.id";

    /** . */
    public static final String JETI_SESSIONSTIMEOUT
        = PREFIX + "jeti.sessions.timeout";

    /** . */
    public static final String JETI_SESSIONSCHECKINTERVAL
        = PREFIX + "jeti.sessions.checkinterval";

    /** . */
    public static final String JETI_DEBUG
        = PREFIX + "jeti.debug";

    /** . */
    public static final String JETI_TOOLXML
        = PREFIX + "jeti.toolxml";

    /** . */
    public static final String JETI_SERVERHOSTNAME
        = PREFIX + "jeti.server.hostname";

    /** . */
    public static final String JETI_CONNECTORSEPPPORT
        = PREFIX + "jeti.connector.sepp.port";

    /** . */
    public static final String JETI_SESSIONSFOLDER
        = PREFIX + "jeti.sessions.folder";
    
    /** . */
    public static final String JETI_LOG4JCONFIG
        = PREFIX + "jeti.log4j.config";

    /** . */
    public static final String JETI_LOGPATH
        = PREFIX + "jeti.logpath";
    
    // Graphviz preferences

    /** Path to the graphviz executable. */
    public static final String GRAPHVIZ_PATH
        = PREFIX + "graphviz.path";

    /** Timeout for graphviz layout processes. */
    public static final String GRAPHVIZ_TIMEOUT
        = PREFIX + "graphviz.timeout";

    // OGDF preferences

    /** Timeout for ogdf layout processes. */
    public static final String OGDF_TIMEOUT
        = PREFIX + "ogdf.timeout";

    // Common preferences

    /** Size of executor pool for http/s server. */
    public static final String SERVER_POOLSIZE
        = PREFIX + "server.poolsize";
    
    /** Size of backlog for http/s server. */
    public static final String SERVER_BACKLOG
        = PREFIX + "server.backlog";

    /** Path to log file. */
    public static final String KWEBS_LOGPATH
        = PREFIX + "log.path";
    
    /** Round trip size of log file in mega bytes. */
    public static final String KWEBS_LOGSIZE
        = PREFIX + "log.size";

    /** Whether to set the application in debugging mode. */
    public static final String KWEBS_LOGDEBUGMODE
        = PREFIX + "log.debugMode";

    /** Address for the support server. */
    public static final String SUPPORTINGSERVER_ADDRESS
        = PREFIX + "supportServerAddress";

    /** Shall the support server be published? */
    public static final String PUBLISH_SUPPORTSERVER
        = PREFIX + "publishSupportServer";

    // Management preferences

    /** Port on which the server listens for management requests. */
    public static final String MANAGEMENT_PORT
        = PREFIX + "management.port";

    // Preferences for configuring restrictions on user transmitted graphs
    
    /** Maximum number of graphs a user may transmit with a single request. */
    public static final String MAXNUMBER_GRAPHS
        = PREFIX + "graphs.maxnumber";

    /** Whether to test on maximum number of graphs a user may transmit with a single request. */
    public static final String TESTMAXNUMBER_GRAPHS
        = PREFIX + "graphs.maxnumber.test";

    /** Maximum number of elements a user transmitted graph may contain. */
    public static final String MAXELEMENTS_GRAPHS
        = PREFIX + "graphs.maxelements";

    /** Whether to test on maximum number of elements a user transmitted graph may contain. */
    public static final String TESTMAXELEMENTS_GRAPHS
        = PREFIX + "graphs.maxelements.test";
        
    //

    /**
     * This is a container class for configuration identifiers so no
     * instantiation is required.
     */
    private Configuration() {
    }

    /**
     * Get the singleton instance.
     *
     * @return the singleton instance
     */
    public static Configuration getInstance() {
        return INSTANCE;
    }

    /**
     * Loads the property based server configuration from an input stream.
     * 
     * @param stream
     *            the input stream to load from
     * @throws IOException
     *            if an i/o exception on the input stream occurs
     */
    public synchronized void loadFromStream(final InputStream stream) throws IOException {
        load(stream);
    }

    /**
     * Loads the property based server configuration from a reader.
     * 
     * @param reader
     *            the reader to load from
     * @throws IOException
     *            if an i/o exception on the reader occurs
     */
    public synchronized void loadFromReader(final Reader reader) throws IOException {
        load(reader);
    }

    /**
     * Loads the XML based server configuration from an input stream.
     * 
     * @param stream
     *            the input stream to load from
     * @throws IOException
     *            if an i/o exception on the input stream occurs
     */
    public synchronized void loadFromXmlStream(final InputStream stream) throws IOException {
        loadFromXML(stream);
    }
    
    /**
     * Returns the value of the configuration property defined by key.
     * 
     * @param key
     *            the name of the configuration property
     * @return the value of the configuration property. may be {@code null} 
     */
    public synchronized String getConfigProperty(final String key) {
        return getProperty(key);
    }

    /**
     * Sets the value of the configuration property defined by key to the value
     * defined by value.
     * 
     * @param key
     *            the name of the configuration property
     * @param value
     *            the value of the configuration property
     */
    public synchronized void setConfigProperty(final String key, final String value) {
        setProperty(key, value);
    }

    /**
     * Returns whether the configuration property defined by key is defined.
     * 
     * @param key
     *            the name of the configuration property
     * @return whether the configuration property defined by key is defined
     */
    public synchronized boolean hasConfigProperty(final String key) {
        return containsKey(key);
    }
    
    /**
     * Returns a configuration property parsed to an integer value. 
     * 
     * @param key
     *            the name of the configuration property
     * @param defaultValue
     *            the default value to be returned if the entry does not exist in the
     *            configuration or it can not be parsed to an integer value
     * @return
     */
    public synchronized int getConfigPropertyAsInteger(final String key, final int defaultValue) {
    	int value = defaultValue;
    	if (hasConfigProperty(key)) {
    		try {
				value = Integer.parseInt(getConfigProperty(key));
			} catch (Exception e) {
				// Nothing to do, returns default value
			}
    	}
    	return value;
    }

    /**
     * Returns a configuration property parsed to a boolean value. 
     * 
     * @param key
     *            the name of the configuration property
     * @param defaultValue
     *            the default value to be returned if the entry does not exist in the
     *            configuration or it can not be parsed to a boolean value
     * @return
     */
    public synchronized boolean getConfigPropertyAsBoolean(final String key, final boolean defaultValue) {
    	boolean value = defaultValue;
    	if (hasConfigProperty(key)) {
    		try {
				value = Boolean.parseBoolean(getConfigProperty(key));
			} catch (Exception e) {
				// Nothing to do, returns default value
			}
    	}
    	return value;
    }
    
}
