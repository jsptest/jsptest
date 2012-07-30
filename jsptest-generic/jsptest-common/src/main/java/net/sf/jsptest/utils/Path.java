/*
 * Copyright 2008 Lasse Koskela.
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
package net.sf.jsptest.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * @author Lasse Koskela
 */
public class Path extends ArrayList {

    private static final String SEPARATOR = System.getProperty("path.separator");

    public void addSystemProperty(String name) {
        String property = System.getProperty(name);
        if (property != null) {
            String[] systemClasspath = property.split(SEPARATOR);
            for (int i = 0; i < systemClasspath.length; i++) {
                String entry = systemClasspath[i];
                File file = new File(entry);
                if (file.exists()) {
                    add(file.getAbsolutePath());
                } else {
                    add(entry);
                }
            }
        }
    }

    public boolean add(Object pathElement) {
        if (contains(pathElement)) {
            return false;
        }
        return super.add(pathElement);
    }

    public boolean add(File file) {
        return add(file.getAbsolutePath());
    }

    public String[] toStringArray() {
        return (String[]) toArray(new String[size()]);
    }

    public void addContainer(Class klass) {
        String resource = resourcePathFor(klass);
        addJarFile(klass.getResource(resource));
        try {
            ClassLoader context = klass.getClassLoader();
            if (context != null) {
                Enumeration en = context.getResources(resource);
                while (en.hasMoreElements()) {
                    addJarFile((URL) en.nextElement());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String resourcePathFor(Class klass) {
        String resource = klass.getName().replaceAll("\\.", "/");
        return "/" + resource + ".class";
    }

    private void addJarFile(URL url) {
        if (url != null) {
            addJarFile(url.toExternalForm());
        }
    }

    private void addJarFile(String url) {
        String prefix = "jar:file:";
        if (url.startsWith(prefix)) {
            String file = url.substring(prefix.length());
            if (file.indexOf("!") > -1) {
                file = file.substring(0, file.indexOf('!'));
            }
            add(new File(file));
        }
    }
}
