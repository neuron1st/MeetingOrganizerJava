package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspPathCreator {
    private static final String JSP_FORMAT = "/WEB-INF/%s.jsp";

    public static String getPath(String path) {
        return String.format(JSP_FORMAT, path);
    }
}
