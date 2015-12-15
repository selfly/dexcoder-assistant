package com.dexcoder.dal.spring.datasource;

/**
 * Created by liyd on 2015-11-2.
 */
public class DataSourceContext {

    /** dataSource key */
    private String  dsKey;

    /** 是否可写 */
    private Boolean isWrite;

    public Boolean getIsWrite() {
        return isWrite;
    }

    public void setIsWrite(Boolean isWrite) {
        this.isWrite = isWrite;
    }

    public String getDsKey() {
        return dsKey;
    }

    public void setDsKey(String dsKey) {
        this.dsKey = dsKey;
    }
}
