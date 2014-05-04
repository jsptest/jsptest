package net.sf.jsptest.compiler.jsp20.mock;

public class RequestHelper {
	public static String getInclusionPath(String path) {
    	String result = path;
    	if(path.lastIndexOf('/') >= 0) {
    		result = path.substring(0, path.lastIndexOf('/') + 1);
    	}
		return result;
	}
}
