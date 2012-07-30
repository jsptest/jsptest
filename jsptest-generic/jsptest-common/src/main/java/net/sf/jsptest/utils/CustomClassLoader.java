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

/**
 * A class loader implementation that first checks if the class asked for can be found under the
 * given base directory and only then delegates to the parent class' implementation.
 * 
 * @author Lasse Koskela
 */
public class CustomClassLoader extends ClassLoader {

    private File baseDir;

    public CustomClassLoader(String baseDir) {
        this(new File(baseDir));
    }

    public CustomClassLoader(File baseDir) {
        this.baseDir = baseDir;
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        File classFile = new File(baseDir, name.replace('.', '/') + ".class");
        if (!classFile.exists()) {
            return super.loadClass(name);
        }
        try {
            byte[] data = IO.readToByteArray(classFile);
            return defineClass(name, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Class defineClass(String name, byte[] data) throws ClassFormatError {
        return super.defineClass(name, data, 0, data.length);
    }
}
