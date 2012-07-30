package org.apache.jasper.compiler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagInfo;
import net.sf.jsptest.TagKey;
import org.apache.jasper.JasperException;
import org.apache.jasper.compiler.Node.Nodes;

/**
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 */
public class MockTagPluginManager extends TagPluginManager {

    protected Map mockTaglibs;

    public MockTagPluginManager(ServletContext ctx, TagPluginManager manager, Map taglibs) {
        super(ctx);
        mockTaglibs = new HashMap(taglibs);
    }

    public void apply(Nodes nodes, ErrorDispatcher errorDispatcher, PageInfo pageInfo)
            throws JasperException {
        invokePrivateInitWith(errorDispatcher);
        assignToPrivateField("pageInfo", pageInfo);
        substituteMocksFor(nodes);
    }

    private void substituteMocksFor(Nodes nodes) throws JasperException {
        nodes.visit(new TagSubstitutor(mockTaglibs));
    }

    /**
     * Walks through the node tree representing the JSP and replaces the <i>handler</i> of certain
     * tags with a mock implementation if one has been configured.
     */
    private static class TagSubstitutor extends Node.Visitor {

        private final Map mocks;

        public TagSubstitutor(Map mockTaglibs) {
            this.mocks = mockTaglibs;
        }

        public void visit(Node.CustomTag n) throws JasperException {
            Class mockClass = substitute(n.getTagInfo());
            if (mockClass != null) {
                n.setTagHandlerClass(mockClass);
            }
            super.visit(n);
        }

        private Class substitute(TagInfo tagInfo) {
            String prefix = tagInfo.getTagLibrary().getPrefixString();
            String name = tagInfo.getTagName();
            TagKey[] matchingOrder = new TagKey[] { new TagKey(prefix, name), new TagKey(prefix) };
            return firstMatch(mocks, matchingOrder);
        }

        private Class firstMatch(Map map, TagKey[] keys) {
            for (int i = 0; i < keys.length; i++) {
                if (map.containsKey(keys[i])) {
                    return (Class) mocks.get(keys[i]);
                }
            }
            return null;
        }
    }

    private void assignToPrivateField(String fieldName, Object value) {
        try {
            Field field = getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokePrivateInitWith(ErrorDispatcher errorDispatcher) {
        try {
            Method init = getClass().getSuperclass().getDeclaredMethod("init",
                    new Class[] { ErrorDispatcher.class });
            init.setAccessible(true);
            init.invoke(this, new Object[] { errorDispatcher });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
