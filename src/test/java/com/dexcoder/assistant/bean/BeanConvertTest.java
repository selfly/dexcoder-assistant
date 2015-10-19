package com.dexcoder.assistant.bean;

import com.dexcoder.assistant.model.UserVo;
import com.dexcoder.assistant.model.User;
import org.junit.Test;

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
