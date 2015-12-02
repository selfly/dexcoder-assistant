package com.dexcoder.assistant.utils;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexcoder.assistant.exceptions.AssistantException;

/**
 * Created by liyd on 2015-11-17.
 */
public class VelocityUtils {

    private static final Logger LOG = LoggerFactory.getLogger(VelocityUtils.class);

    static {
        try {
            Velocity.init();
        } catch (Exception e) {
            LOG.error("初始化velocity失败", e);
            throw new AssistantException("初始化velocity失败");
        }
    }

    /**
     * 渲染内容.
     *
     * @param template 模板内容.
     * @param context 变量Map.
     */
    public static String render(String template, Map<String, ?> context) {
        try {
            VelocityContext velocityContext = new VelocityContext(context);
            StringWriter result = new StringWriter();
            Velocity.evaluate(velocityContext, result, "", template);
            return result.toString();
        } catch (Exception e) {
            LOG.error("velocity解析template失败", e);
            throw new AssistantException("velocity解析template失败");
        }
    }
}
