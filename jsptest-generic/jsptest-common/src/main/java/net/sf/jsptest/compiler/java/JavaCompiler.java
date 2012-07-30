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
package net.sf.jsptest.compiler.java;

/**
 * The interface for Java compiler implementations.
 * 
 * @author Lasse Koskela
 */
public interface JavaCompiler {

    /**
     * Compiles the given .java source file into the given .class file using an explicitly set class
     * path.
     * 
     * @param pathToJavaSource
     *            Path to the source file to compile.
     * @param outputDirectory
     *            Path to the output directory under which to generate the .class file
     * @param classpath
     *            The class path to use for compilation.
     */
    boolean compile(String pathToJavaSource, String outputDirectory, String[] classpath)
            throws Exception;

    /**
     * Indicates whether this Java compiler implementation is available on the current system.
     */
    boolean isAvailable();
}
