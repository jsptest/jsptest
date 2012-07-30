package net.sf.jsptest.compiler.jsp20.mock.taglibs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lasse Koskela
 */
public class TldUriMappings {

    private static final Map standardTlds = new HashMap();
    static {
        initialize();
    }

    private static void initialize() {
        map("http://java.sun.com/jstl/core", "c.tld");
        map("http://java.sun.com/jstl/sql", "sql.tld");
        map("http://java.sun.com/jstl/xml", "x.tld");
        map("http://java.sun.com/jstl/fmt", "fmt.tld");
        map("http://java.sun.com/jstl/functions", "fn.tld");
        map("http://java.sun.com/jsp/jstl/core", "c.tld");
        map("http://java.sun.com/jsp/jstl/sql", "sql.tld");
        map("http://java.sun.com/jsp/jstl/xml", "x.tld");
        map("http://java.sun.com/jsp/jstl/fmt", "fmt.tld");
        map("http://java.sun.com/jsp/jstl/functions", "fn.tld");
        map("http://java.sun.com/jstl/core-rt", "c-rt.tld");
        map("http://java.sun.com/jstl/sql-rt", "sql-rt.tld");
        map("http://java.sun.com/jstl/xml-rt", "x-rt.tld");
        map("http://java.sun.com/jstl/fmt-rt", "fmt-rt.tld");
        map("http://java.sun.com/jstl/functions-rt", "fn-rt.tld");
        map("http://java.sun.com/jstl/core-rt", "c-1_0-rt.tld");
        map("http://java.sun.com/jstl/sql-rt", "sql-1_0-rt.tld");
        map("http://java.sun.com/jstl/xml-rt", "x-1_0-rt.tld");
        map("http://java.sun.com/jstl/fmt-rt", "fmt-1_0-rt.tld");
        map("http://java.sun.com/jstl/functions-rt", "fn-1_0-rt.tld");
    }

    private static void map(String uri, String filename) {
        List filenames = (List) standardTlds.get(uri);
        if (filenames == null) {
            filenames = new ArrayList();
        }
        filenames.add(filename);
        standardTlds.put(uri, filenames);
    }

    public boolean contains(String uri) {
        return standardTlds.containsKey(uri);
    }

    public List get(String uri) {
        return (List) standardTlds.get(uri);
    }
}
