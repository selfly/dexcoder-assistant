package com.dexcoder.test.bean;

import org.junit.Test;

import com.dexcoder.commons.bean.BeanConverter;
import com.dexcoder.test.model.User;
import com.dexcoder.test.model.UserVo;

/**
 * Created by liyd on 2015-6-4.
 */
public class BeanConvertTest {

    @Test
    public void covert() {

        UserVo user = new UserVo();
        user.setUserId(1000L);
        user.setLoginName("liyd");

        User userVo = BeanConverter.convert(new User(), user);

        System.out.println(userVo.getLoginName());

    }
}
