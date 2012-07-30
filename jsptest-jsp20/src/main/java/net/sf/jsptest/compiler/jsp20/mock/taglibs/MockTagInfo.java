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

import javax.servlet.jsp.tagext.TagInfo;

/**
 * @author Lasse Koskela
 */
public class MockTagInfo extends TagInfo {

    public MockTagInfo() {
        super("", "", "", "", null, null, null);
    }

    public String getTagClassName() {
        try {
            throw new RuntimeException("MockTagInfo being asked for getTagClassName()");
        } catch (RuntimeException e) {
            e.printStackTrace(System.out);
        }
        return super.getTagClassName();
    }
}
