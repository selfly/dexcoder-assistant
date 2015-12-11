package com.dexcoder.test.persistence;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Created by liyd on 2015-11-17.
 */
public class ResourceTest {

    @Test
    public void resourceTest() throws Exception {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("*.xml");

        for (Resource resource : resources) {
            System.out.println(resource.getFile().getAbsolutePath());
        }

    }
}
