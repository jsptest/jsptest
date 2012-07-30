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
package net.sf.jsptest.acceptance.jsp;

import net.sf.jsptest.JspTestCase;

/**
 * @author Lasse Koskela
 */
public class TaglibsDefinedInJarFileTest extends JspTestCase {

    protected String getWebRoot() {
        return "src/test/resources/websrc2";
    }

    public void testStandardTaglibsWork() throws Exception {
        get("/standard-taglibs.jsp");
        for (int i = 1; i <= 10; i++) {
            output().shouldContain("loop-" + i);
        }
    }
}
