package com.dexcoder.assistant.utils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对源代码进行格式化
 */
public class SourceCodeFormatter {

    private static final Logger LOG = LoggerFactory.getLogger(SourceCodeFormatter.class);

    /**
     * 自动格式化 会尝试判断格式  python等语言不支持
     *
     * @param source
     * @return
     */
    public static String formatEscapeCode(String source) {

        source = StringUtils.trim(source);
        //需要先还原，因为提交过来页面显示的代码都是转义后的
        source = TextUtils.reverseSpecialChars(source);
        if (StringUtils.startsWith(source, "<")) {
            source = formatXML(source);
        } else {
            source = formatCode(source);
        }
        //还原成转义后
        return TextUtils.convertSpecialChars(source);
    }

    /**
     * 对源代码进行格式化，格式化失败返回原格式
     * 
     * @param sourceCode
     * @return
     */
    public static String formatCode(String sourceCode) {

        BufferedReader reader = new BufferedReader(new StringReader(sourceCode));
        try {
            StringBuilder header = new StringBuilder();
            StringBuilder content = new StringBuilder();
            String lineSeparator = System.getProperty("line.separator", "\n");
            boolean isHeader = true;
            String line;
            while ((line = reader.readLine()) != null) {
                line = StringUtils.trim(line);
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                if (isHeader
                    && (StringUtils.startsWith(line, "package") || StringUtils.startsWith(line,
                        "import"))) {
                    header.append(line).append(lineSeparator);
                } else {
                    isHeader = false;
                    content.append(line).append(lineSeparator);
                }
            }
            String finalCode = StringUtils.trim(content.toString());
            finalCode = formatBodyCode(finalCode);
            return header.append(finalCode).toString();

        } catch (Exception e) {
            LOG.error("格式化源代码失败", e);
            //忽略，返回原格式
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return sourceCode;
    }

    /**
     * 格式化源码
     * 
     * @param code
     * @return
     */
    private static String formatBodyCode(String code) {
        try {
            //此处可以修改格式化配置
            Map m = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
            m.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "100");
            m.put(DefaultCodeFormatterConstants.FORMATTER_ALIGN_TYPE_MEMBERS_ON_COLUMNS, "true");
            m.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
            CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(m);
            TextEdit textEdit = codeFormatter.format(CodeFormatter.K_UNKNOWN, code, 0,
                code.length(), 0, null);
            if (textEdit != null) {
                IDocument doc = new Document(code);
                textEdit.apply(doc);
                return doc.get();
            }
        } catch (BadLocationException e) {
            LOG.error("格式化源代码失败", e);
        }
        return code;
    }

    /**
     * 格式化xml
     * 
     * @param xml
     * @return
     */
    public static String formatXML(String xml) {

        SAXReader reader = null;
        XMLWriter writer = null;
        StringWriter stringWriter = null;
        try {
            reader = new SAXReader();
            org.dom4j.Document document = reader.read(new StringReader(xml));
            if (document != null) {
                stringWriter = new StringWriter();
                OutputFormat format = new OutputFormat("    ", false);
                format.setSuppressDeclaration(true);
                writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
            }
            return stringWriter.getBuffer().toString();
        } catch (Exception e) {
            LOG.error("格式化xml失败", e);
        } finally {
            try {
                writer.close();
                stringWriter.close();
            } catch (Exception e) {
                //ignore
            }
        }
        return xml;
    }
}
