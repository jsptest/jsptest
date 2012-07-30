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

/**
 * @author Lasse Koskela
 */
public class TldLocation {

    private final boolean wasFound;

    /**
     * @param wasFound
     *            <tt>true</tt> means the TLD location was found, <tt>false</tt> means it wasn't
     *            found.
     */
    protected TldLocation(boolean wasFound) {
        this.wasFound = wasFound;
    }

    /**
     * Indicates whether the location was found or not.
     * 
     * @return true if the TLD was found, false if it wasn't.
     */
    public boolean wasFound() {
        return wasFound;
    }

    /**
     * <p>
     * Formats the location of the TLD into the array format used by <tt>TldLocationsCache</tt>,
     * i.e. an array in one of these formats:
     * </p>
     * <p> { "/WEB-INF/foo.tld", null } for exploded TLD files<br/> { "file:/url/to/taglibs.jar",
     * "META-INF/foo.tld } for TLD files found in archives
     * </p>
     */
    public String[] toArray() {
        return null;
    }

    /**
     * Creates a "not found" instance of TldLocation.
     */
    public static TldLocation notFound() {
        return new TldLocation(false);
    }

    /**
     * Creates a <tt>TldLocation</tt> instance for a file located under WEB-INF.
     * 
     * @param filename
     *            The base name of the TLD file, e.g. "c-rt.tld".
     */
    public static TldLocation foundFromWebInf(final String filename) {
        return new TldLocation(true) {

            public String[] toArray() {
                return new String[] { "/WEB-INF/" + filename, null };
            }
        };
    }

    /**
     * Creates a <tt>TldLocation</tt> instance for a file located from the class path.
     * 
     * @param pathToArchive
     *            URL to the archive containing the TLD file
     * @param pathInsideArchive
     *            Path to the TLD file inside the archive
     */
    public static TldLocation foundFromClassPath(final String pathToArchive,
            final String pathInsideArchive) {
        return new TldLocation(true) {

            public String[] toArray() {
                return new String[] { pathToArchive, pathInsideArchive };
            }
        };
    }
}
