package com.dexcoder.dal.batis;

import com.dexcoder.dal.handler.DefaultMappingHandler;
import com.dexcoder.dal.handler.MappingHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.dexcoder.dal.batis.build.Configuration;
import com.dexcoder.dal.batis.xml.XMLMapperBuilder;
import com.dexcoder.dal.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-11-24.
 */
public class BatisSqlFactoryBean implements FactoryBean<BatisSqlFactory>, InitializingBean {

    private String          sqlLocation;

    private Configuration   configuration;

    private MappingHandler mappingHandler;

    private BatisSqlFactory sqlFactory;

    public void afterPropertiesSet() throws Exception {
        this.configuration = new Configuration();
        String[] sqlLocations = StringUtils.split(this.sqlLocation, ",");
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
                XMLMapperBuilder mapperParser = new XMLMapperBuilder(resource, this.configuration,getMappingHandler());
                mapperParser.parse();
            } catch (Exception e) {
                throw new JdbcAssistantException("读取resource文件失败:" + resource.getFilename(), e);
            }
        }
    }

    /**
     * 获取名称处理器
     *
     * @return
     */
    protected MappingHandler getMappingHandler() {

        if (this.mappingHandler == null) {
            this.mappingHandler = new DefaultMappingHandler();
        }
        return this.mappingHandler;
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

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public void setSqlLocation(String sqlLocation) {
        this.sqlLocation = sqlLocation;
    }
}
