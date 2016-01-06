package com.dexcoder.dal.spring.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.CollectionUtils;

import com.dexcoder.commons.utils.ClassUtils;
import com.dexcoder.commons.utils.RandomUtils;
import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.commons.utils.UUIDUtils;

/**
 * Created by liyd on 2015-10-26.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public static final String  ATTR_ID                = "id";
    public static final String  ATTR_CLASS             = "class";
    public static final String  ATTR_DEFAULT           = "default";
    public static final String  ATTR_NAME              = "name";
    public static final String  ATTR_VALUE             = "value";
    public static final String  DS_WEIGHT              = "weight";
    public static final String  DS_MODE                = "mode";
    public static final String  DS_MODE_R              = "r";
    public static final String  DS_MODE_W              = "w";
    public static final String  DS_MODE_RW             = "rw";

    private static final Logger       LOG                    = LoggerFactory.getLogger(DynamicDataSource.class);

    /**
     * 默认配置文件名
     */
    private static final String DEFAULT_DS_CONFIG_FILE = "dynamic-ds.xml";

    /**
     * 数据源配置文件
     */
    private String              dsConfigFile;

    /**
     * 读数据源列表，这里保存key 根据权重会有相应的数量
     */
    private List<String>        readDataSourceKeyList;

    /**
     * 写数据源列表，这里保存key 根据权重会有相应的数量
     */
    private List<String>        writeDataSourceKeyList;

    @Override
    protected Object determineCurrentLookupKey() {

        DataSourceContext dsContent = DynamicDataSourceHolder.getDsContent();
        //已设置过数据源，直接返回
        if (StrUtils.isNotBlank(dsContent.getDsKey())) {
            return dsContent.getDsKey();
        }

        if (dsContent.getIsWrite()) {
            String dsKey = writeDataSourceKeyList.get(RandomUtils.nextInt(writeDataSourceKeyList.size()));
            dsContent.setDsKey(dsKey);
        } else {
            String dsKey = readDataSourceKeyList.get(RandomUtils.nextInt(readDataSourceKeyList.size()));
            dsContent.setDsKey(dsKey);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("当前操作使用数据源:{}", dsContent.getDsKey());
        }
        return dsContent.getDsKey();
    }

    @Override
    public void afterPropertiesSet() {
        this.initDataSources();
    }

    /**
     * 初始化数据源
     */
    public void initDataSources() {

        List<Map<String, String>> dataSourceList = DynamicDataSourceUtils.parseDataSources(this.getDsConfigFile());
        this.initDataSources(dataSourceList);
    }

    /**
     * 初始化数据源
     *
     * @param dataSourceList
     */
    public void initDataSources(List<Map<String, String>> dataSourceList) {
        LOG.info("开始初始化动态数据源");
        readDataSourceKeyList = new ArrayList<String>();
        writeDataSourceKeyList = new ArrayList<String>();
        Map<Object, Object> targetDataSource = new HashMap<Object, Object>();
        Object defaultTargetDataSource = null;
        for (Map<String, String> map : dataSourceList) {
            String dataSourceId = DynamicDataSourceUtils.getAndRemoveValue(map, ATTR_ID, UUIDUtils.getUUID8());
            String dataSourceClass = DynamicDataSourceUtils.getAndRemoveValue(map, ATTR_CLASS, null);
            String isDefaultDataSource = DynamicDataSourceUtils.getAndRemoveValue(map, ATTR_DEFAULT, "false");
            String weight = DynamicDataSourceUtils.getAndRemoveValue(map, DS_WEIGHT, "1");
            String mode = DynamicDataSourceUtils.getAndRemoveValue(map, DS_MODE, "rw");
            DataSource dataSource = (DataSource) ClassUtils.newInstance(dataSourceClass);
            DynamicDataSourceUtils.setDsProperties(map, dataSource);
            targetDataSource.put(dataSourceId, dataSource);
            if (Boolean.valueOf(isDefaultDataSource)) {
                defaultTargetDataSource = dataSource;
            }
            DynamicDataSourceUtils.addWeightDataSource(readDataSourceKeyList, writeDataSourceKeyList, dataSourceId,
                Integer.valueOf(weight), mode);
            LOG.info("dataSourceId={},dataSourceClass={},isDefaultDataSource={},weight={},mode={}", new Object[] {
                    dataSourceId, dataSourceClass, isDefaultDataSource, weight, mode });
        }
        this.setTargetDataSources(targetDataSource);
        if (defaultTargetDataSource == null) {
            defaultTargetDataSource = (CollectionUtils.isEmpty(writeDataSourceKeyList) ? targetDataSource
                .get(readDataSourceKeyList.iterator().next()) : targetDataSource.get(writeDataSourceKeyList.iterator()
                .next()));
        }
        this.setDefaultTargetDataSource(defaultTargetDataSource);
        super.afterPropertiesSet();
        LOG.info("初始化动态数据源完成");
    }

    public String getDsConfigFile() {
        if (StrUtils.isBlank(this.dsConfigFile)) {
            return DEFAULT_DS_CONFIG_FILE;
        }
        return dsConfigFile;
    }

    public void setDsConfigFile(String dsConfigFile) {
        this.dsConfigFile = dsConfigFile;
    }
}
