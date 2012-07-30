/*
 * Copyright 2007 Lasse Koskela.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jsptest.compiler.jsp20.mock.taglibs;

import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.jasper.JasperException;
import org.apache.jasper.compiler.TldLocationsCache;

/**
 * @author Lasse Koskela
 */
public class MockTldLocationsCache extends TldLocationsCache {

    private TldUriMappings standardTlds;
    private TldLocationsCache realCache;
    private String webRoot;
    private TldLocator[] locators;

    public MockTldLocationsCache(TldLocationsCache delegate, ServletContext ctx) {
        this(delegate, ctx, true);
    }

    public MockTldLocationsCache(TldLocationsCache delegate, ServletContext ctx,
            boolean redeployMode) {
        super(ctx, redeployMode);
        realCache = delegate;
        webRoot = ctx.getRealPath("/");
        standardTlds = new TldUriMappings();
        locators = new TldLocator[] { new WebInfLibTldLocator(webRoot),
                new ExplodedTldLocator(webRoot), new ClasspathTldLocator() };
    }

    public String[] getLocation(String uri) throws JasperException {
        // TODO: Optimize. Call to getLocation(String) can take a full second.
        if (standardTlds.contains(uri)) {
            List tldFileNames = (List) standardTlds.get(uri);
            for (Iterator it = tldFileNames.iterator(); it.hasNext();) {
                String tldFileName = (String) it.next();
                for (int i = 0; i < locators.length; i++) {
                    TldLocation location = locators[i].find(tldFileName);
                    if (location.wasFound()) {
                        return location.toArray();
                    }
                }
            }
        }
        return realCache.getLocation(uri);
    }
}
