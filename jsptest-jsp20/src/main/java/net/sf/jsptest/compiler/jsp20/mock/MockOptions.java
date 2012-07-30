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
package net.sf.jsptest.compiler.jsp20.mock;

import java.io.File;
import java.util.Map;
import javax.servlet.ServletContext;
import net.sf.jsptest.compiler.JspCompilationInfo;
import net.sf.jsptest.compiler.jsp20.mock.taglibs.MockTldLocationsCache;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspConfig;
import org.apache.jasper.compiler.MockTagPluginManager;
import org.apache.jasper.compiler.TagPluginManager;
import org.apache.jasper.compiler.TldLocationsCache;

/**
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 */
public class MockOptions implements Options {

    private Options options;
    private ServletContext servletContext;
    private JspCompilationInfo compilationInfo;

    public MockOptions(Options options, ServletContext context, JspCompilationInfo compilationInfo) {
        this.options = options;
        this.servletContext = context;
        this.compilationInfo = compilationInfo;
    }

    public boolean getErrorOnUseBeanInvalidClassAttribute() {
        return options.getErrorOnUseBeanInvalidClassAttribute();
    }

    public boolean getKeepGenerated() {
        return options.getKeepGenerated();
    }

    public boolean isPoolingEnabled() {
        return options.isPoolingEnabled();
    }

    public boolean getMappedFile() {
        return options.getMappedFile();
    }

    public boolean getSendErrorToClient() {
        return options.getSendErrorToClient();
    }

    public boolean getClassDebugInfo() {
        return options.getClassDebugInfo();
    }

    public int getCheckInterval() {
        return options.getCheckInterval();
    }

    public boolean getDevelopment() {
        return options.getDevelopment();
    }

    public boolean isSmapSuppressed() {
        return true; // options.isSmapSuppressed();
    }

    public boolean isSmapDumped() {
        return false; // options.isSmapDumped();
    }

    public boolean getTrimSpaces() {
        return options.getTrimSpaces();
    }

    public String getIeClassId() {
        return options.getIeClassId();
    }

    public File getScratchDir() {
        return options.getScratchDir();
    }

    public String getClassPath() {
        return options.getClassPath();
    }

    public String getCompiler() {
        return options.getCompiler();
    }

    public String getCompilerTargetVM() {
        return options.getCompilerTargetVM();
    }

    public String getCompilerSourceVM() {
        return options.getCompilerSourceVM();
    }

    public TldLocationsCache getTldLocationsCache() {
        TldLocationsCache realCache = options.getTldLocationsCache();
        return new MockTldLocationsCache(realCache, servletContext);
    }

    public String getJavaEncoding() {
        return options.getJavaEncoding();
    }

    public boolean getFork() {
        return options.getFork();
    }

    public JspConfig getJspConfig() {
        return options.getJspConfig();
    }

    public boolean isXpoweredBy() {
        return options.isXpoweredBy();
    }

    public TagPluginManager getTagPluginManager() {
        return new MockTagPluginManager(servletContext, options.getTagPluginManager(),
                compilationInfo.getTaglibs());
    }

    public boolean genStringAsCharArray() {
        return options.genStringAsCharArray();
    }

    public int getModificationTestInterval() {
        return options.getModificationTestInterval();
    }

    public boolean isCaching() {
        return options.isCaching();
    }

    public Map getCache() {
        return options.getCache();
    }
}
