package com.dexcoder.dal.batis.parser;

import com.dexcoder.commons.utils.ClassUtils;
import com.dexcoder.dal.build.MetaTable;
import com.dexcoder.dal.handler.MappingHandler;
import com.dexcoder.dal.handler.TokenHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liyd on 16/3/31.
 */
public class ClassFieldHandler implements TokenHandler {

    private MappingHandler mappingHandler;

    public ClassFieldHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public String handleToken(String content) {

        int start = StringUtils.indexOf(content, ")") + 1;
        int end = StringUtils.indexOf(content, "[");
        String clazzName = StringUtils.substring(content, start != -1 ? start : 0, end != -1 ? end : content.length());
        String tableAlias = this.getTokenStr(content, "(", ")");
        List<String> includeFields = this.getTokenStrSplitList(content, "[+", "+]", ",");
        List<String> excludeFields = this.getTokenStrSplitList(content, "[-", "-]", ",");
        Class<?> clazz = ClassUtils.loadClass(StringUtils.trim(clazzName));
        MetaTable metaTable = new MetaTable().initAutoFields().tableAlias(tableAlias).tableClass(clazz)
                .mappingHandler(mappingHandler);
        Set<String> classFields = new HashSet<String>(metaTable.getClassFields());
        StringBuilder sb = new StringBuilder();
        for (String field : classFields) {

            if (excludeFields.contains(field)) {
                continue;
            }
            String column = metaTable.getColumnAndTableAliasName(field);
            if (!includeFields.isEmpty()) {
                if (includeFields.contains(field)) {
                    sb.append(column).append(",");
                }
                continue;
            }
            sb.append(column).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private List<String> getTokenStrSplitList(String content, String openToken, String closeToken, String split) {
        String subStr = this.getTokenStr(content, openToken, closeToken);
        return Arrays.asList(StringUtils.split(subStr.replaceAll(" ", ""), split));
    }

    private String getTokenStr(String content, String openToken, String closeToken) {
        int start = StringUtils.indexOf(content, openToken);
        int end = StringUtils.indexOf(content, closeToken);
        if (start == -1 || end == -1) {
            return "";
        }
        return StringUtils.substring(content, start + openToken.length(), end);
    }
}
