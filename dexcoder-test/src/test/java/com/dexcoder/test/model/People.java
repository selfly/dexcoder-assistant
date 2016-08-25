package com.dexcoder.test.model;

import com.dexcoder.commons.pager.Pageable;

import java.util.Date;

/**
 * Created by liyd on 16/8/25.
 */
public class People extends Pageable {

    private static final long serialVersionUID = -2382877626585956174L;

    private String            peopleId;
    private String            peopleName;
    private Integer           peopleAge;
    private Date              gmtCreate;

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public Integer getPeopleAge() {
        return peopleAge;
    }

    public void setPeopleAge(Integer peopleAge) {
        this.peopleAge = peopleAge;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
