package com.dexcoder.assistant.persistence.manual;

import com.dexcoder.assistant.exceptions.AssistantException;
import com.dexcoder.assistant.utils.StrUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Created by liyd on 2015-11-24.
 */
public class SqlFactoryBean implements FactoryBean<SqlFactory>, InitializingBean {

    private String sqlLocation;

    private Configuration configuration;

    private SqlFactory sqlFactory;

    public void afterPropertiesSet() throws Exception {
        this.configuration = new Configuration();
        String[] sqlLocations = StrUtils.split(this.sqlLocation, ",");
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        for (String location : sqlLocations) {
            try {
                Resource[] resources = resourcePatternResolver.getResources(location);
                this.readResource(resources);
            } catch (Exception e) {
                throw new AssistantException("读取sqlLocation失败:" + location, e);
            }
        }
    }

    /**
     * 读取resource
     *
     * @param resources
     */
    private void readResource(Resource[] resources) {

        for (Resource resource : resources) {
            try {
                XMLMapperBuilder mapperParser = new XMLMapperBuilder(resource, this.configuration);
                mapperParser.parse();
            } catch (Exception e) {
                throw new AssistantException("读取resource文件失败:" + resource.getFilename(), e);
            }
        }
    }

    public SqlFactory getObject() throws Exception {
        this.sqlFactory = new SqlFactory(this.configuration);
        return this.sqlFactory;
    }

    public Class<?> getObjectType() {
        return SqlFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setSqlLocation(String sqlLocation) {
        this.sqlLocation = sqlLocation;
    }
}
