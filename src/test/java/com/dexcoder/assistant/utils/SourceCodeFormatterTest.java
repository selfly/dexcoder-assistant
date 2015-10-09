package com.dexcoder.assistant.utils;

import org.junit.Test;

/**
 * Created by liyd on 2015-8-14.
 */
public class SourceCodeFormatterTest {

    @Test
    public void formatEscapeCode() {

        String code = SourceCodeFormatter
            .formatEscapeCode(
                    "String uri = request.getRequestURI();\n" +
                            "String contextPath = request.getContextPath();\n" +
                            "if (StringUtils.length(contextPath) > 0) {\n" +
                            "uri = StringUtils.substring(uri, contextPath.length());\n" +
                            "}");

        System.out.println(code);
    }
}
