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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Lasse Koskela
 */
public class WebInfLibTldLocator implements TldLocator {

    private final String webRoot;

    public WebInfLibTldLocator(String webRoot) {
        this.webRoot = webRoot;
    }

    public TldLocation find(String filename) {
        File webInfLib = new File(new File(webRoot, "WEB-INF"), "lib");
        if (webInfLib.exists()) {
            File[] archives = findArchivesFrom(webInfLib);
            return findTldFromArchives(filename, archives);
        } else {
            return TldLocation.notFound();
        }
    }

    private TldLocation findTldFromArchives(String filename, File[] archives) {
        for (int i = 0; i < archives.length; i++) {
            try {
                JarFile archive = new JarFile(archives[i]);
                JarEntry entry = archive.getJarEntry("META-INF/" + filename);
                if (entry != null) {
                    return TldLocation.foundFromClassPath(archives[i].toURL().toString(),
                            "META-INF/" + filename);
                }
            } catch (IOException ignored) {
            }
        }
        return TldLocation.notFound();
    }

    private File[] findArchivesFrom(File dir) {
        return dir.listFiles(new FilenameFilter() {

            public boolean accept(File directory, String name) {
                return name.endsWith(".jar");
            }
        });
    }
}
