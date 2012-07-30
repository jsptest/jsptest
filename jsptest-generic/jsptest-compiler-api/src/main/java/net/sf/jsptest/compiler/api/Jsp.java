package net.sf.jsptest.compiler.api;

import java.util.Map;

/**
 * @author Lasse Koskela
 * @author Ronaldo Webb
 */
public interface Jsp {

    JspExecution request(String httpMethod, Map requestAttributes, Map sessionAttributes, Map requestParameters);
}
