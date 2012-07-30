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

/**
 * The <tt>Strings</tt> class provides utility methods for string manipulation.
 * 
 * @author Lasse Koskela
 */
public class Strings {

    /**
     * Replace instances of "what" from "fromWhere" with "withWhat". This is essentially a
     * replacement for not being able to use {@link String#replace(String, String, String)} with JDK
     * 1.4.
     * 
     * @param fromWhere
     *            The string to replace stuff from.
     * @param what
     *            The string to replace.
     * @param withWhat
     *            The string to replace with.
     * @return A new string with the replaced content.
     */
    public static String replace(String fromWhere, String what, String withWhat) {
        if (what.length() > 0) {
            int indexOfWhat = fromWhere.indexOf(what);
            if (indexOfWhat != -1) {
                String beforeMatch = fromWhere.substring(0, indexOfWhat);
                String afterMatch = fromWhere.substring(indexOfWhat + what.length());
                fromWhere = beforeMatch + withWhat + replace(afterMatch, what, withWhat);
            }
        }
        return fromWhere;
    }
}
