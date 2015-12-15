package com.dexcoder.dal.batis.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.dexcoder.commons.utils.ClassUtils;

/**
 * Offline entity resolver for the MyBatis DTDs
 *
 * @author Clinton Begin
 */
public class XMLMapperEntityResolver implements EntityResolver {

    private static final Map<String, String> doctypeMap            = new HashMap<String, String>();

    private static final String              BATIS_MAPPER_PUBLIC   = "-//dexcoder.com//DTD Mapper 2.0//EN"
                                                                       .toUpperCase(Locale.ENGLISH);
    private static final String              MYBATIS_MAPPER_SYSTEM = "http://mybatis.org/dtd/mybatis-3-mapper.dtd"
                                                                       .toUpperCase(Locale.ENGLISH);
    private static final String              BATIS_MAPPER_DTD      = "batis-mapper.dtd";

    static {

        doctypeMap.put(BATIS_MAPPER_PUBLIC, BATIS_MAPPER_DTD);

        doctypeMap.put(MYBATIS_MAPPER_SYSTEM, BATIS_MAPPER_DTD);
    }

    /*
     * Converts a public DTD into a local one
     * 
     * @param publicId The public id that is what comes after "PUBLIC"
     * @param systemId The system id that is what comes after the public id.
     * @return The InputSource for the DTD
     * 
     * @throws org.xml.sax.SAXException If anything goes wrong
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {

        if (publicId != null) {
            publicId = publicId.toUpperCase(Locale.ENGLISH);
        }
        if (systemId != null) {
            systemId = systemId.toUpperCase(Locale.ENGLISH);
        }

        InputSource source = null;
        try {
            String path = doctypeMap.get(publicId);
            source = getInputSource(path, source);
            if (source == null) {
                path = doctypeMap.get(systemId);
                source = getInputSource(path, source);
            }
        } catch (Exception e) {
            throw new SAXException(e.toString());
        }
        return source;
    }

    private InputSource getInputSource(String path, InputSource source) {
        if (path != null) {
            InputStream in;
            in = ClassUtils.getDefaultClassLoader().getResourceAsStream(path);
            source = new InputSource(in);
        }
        return source;
    }

}
