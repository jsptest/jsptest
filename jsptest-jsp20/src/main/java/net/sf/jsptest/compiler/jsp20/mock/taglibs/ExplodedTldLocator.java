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

import java.io.File;

/**
 * @author Lasse Koskela
 */
public class ExplodedTldLocator implements TldLocator {

    private final String webRoot;

    public ExplodedTldLocator(String webRoot) {
        this.webRoot = webRoot;
    }

    public TldLocation find(String filename) {
        File webInf = new File(webRoot, "WEB-INF");
        if (new File(webInf, filename).exists()) {
            return TldLocation.foundFromWebInf(filename);
        }
        return TldLocation.notFound();
    }
}
