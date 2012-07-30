package net.sf.jsptest.compiler.api;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * @author Lasse Koskela
 */
public class JspCompilerFactory {

    private static final Logger log = Logger.getLogger(JspCompilerFactory.class);
    private static final String RESOURCE_PATH = "jsptest.properties";
    private static final String CONFIGURATION_KEY = "jsptest.compiler.implementation";

    public static JspCompiler newInstance() {
        RuntimeException exception = new RuntimeException(
                "No JSP compiler implementation configured: " + "(configuration file "
                        + RESOURCE_PATH + " not found from class path.");
        try {
            printConfigurationFiles();
            Enumeration resources = getClassLoader().getResources(RESOURCE_PATH);
            while (resources.hasMoreElements()) {
                URL resource = (URL) resources.nextElement();
                if (resources.hasMoreElements()
                        && resource.toString().indexOf("test-classes") != -1) {
                    log.debug("Ignoring " + resource + " because it's from 'test-classes' "
                            + "and there's another matching resource available");
                    continue;
                }
                try {
                    return loadCompilerFrom(resource);
                } catch (RuntimeException invalidConfig) {
                    exception = invalidConfig;
                } catch (Exception invalidConfig) {
                    exception = new RuntimeException(invalidConfig);
                }
            }
            throw exception;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void printConfigurationFiles() throws Exception {
        Enumeration enumeration = getClassLoader().getResources(RESOURCE_PATH);
        List resources = new ArrayList();
        while (enumeration.hasMoreElements()) {
            resources.add(enumeration.nextElement());
        }
        log.debug("Found " + resources.size() + " resources matching '" + RESOURCE_PATH + "':");
        for (Iterator i = resources.iterator(); i.hasNext();) {
            log.debug("  " + ((URL) i.next()));
        }
    }

    private static JspCompiler loadCompilerFrom(URL resource) throws Exception {
        Properties properties = new Properties();
        properties.load(resource.openStream());
        if (!properties.containsKey(CONFIGURATION_KEY)) {
            throw new RuntimeException("Property " + CONFIGURATION_KEY + " not found from "
                    + resource);
        }
        String klass = properties.getProperty(CONFIGURATION_KEY);
        return (JspCompiler) Class.forName(klass).newInstance();
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
