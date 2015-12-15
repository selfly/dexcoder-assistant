package com.dexcoder.dal.batis;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.batis.build.Configuration;
import com.dexcoder.dal.batis.xml.XMLMapperBuilder;
import com.dexcoder.dal.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-11-24.
 */
public class SqlFactoryBean implements FactoryBean<BatisSqlFactory>, InitializingBean {

    private String          sqlLocation;

    private Configuration   configuration;

    private BatisSqlFactory sqlFactory;

    public void afterPropertiesSet() throws Exception {
        this.configuration = new Configuration();
        String[] sqlLocations = StrUtils.split(this.sqlLocation, ",");
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        for (String location : sqlLocations) {
            try {
                Resource[] resources = resourcePatternResolver.getResources(location);
                this.readResource(resources);
            } catch (Exception e) {
                throw new JdbcAssistantException("读取sqlLocation失败:" + location, e);
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
                throw new JdbcAssistantException("读取resource文件失败:" + resource.getFilename(), e);
            }
        }
    }

    public BatisSqlFactory getObject() throws Exception {
        this.sqlFactory = new BatisSqlFactory(this.configuration);
        return this.sqlFactory;
    }

    public Class<?> getObjectType() {
        return BatisSqlFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setSqlLocation(String sqlLocation) {
        this.sqlLocation = sqlLocation;
    }
}
