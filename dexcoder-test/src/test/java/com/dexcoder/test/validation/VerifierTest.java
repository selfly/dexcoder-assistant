package com.dexcoder.test.validation;

import com.dexcoder.commons.validation.CollectionEachNotNullValidator;
import com.dexcoder.commons.validation.ValidationError;
import com.dexcoder.commons.validation.ValidationResult;
import com.dexcoder.commons.validation.Verifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by liyd on 17/1/24.
 */
public class VerifierTest {

    private String       username1;
    private String       username2 = "  ";
    private String       username3 = "selfy";
    private List<String> nameList1;
    private List<String> nameList2;
    private List<String> nameList3;

    @Before
    public void before() {
        nameList2 = new ArrayList<String>();
        nameList2.add("selfly");

        nameList3 = new ArrayList<String>();
        nameList3.add("1111");
        nameList3.add(null);
        nameList3.add("3333");
    }

    @Test
    public void notNull() {

        ValidationResult result = Verifier.init().notNull(username2, "用户名").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().notNull(username1, "用户名").result();
        Assert.assertFalse(result.isSuccess());
        for (ValidationError validationError : result.getErrors()) {
            Assert.assertEquals("用户名不能为空", "用户名不能为空");
        }
    }

    @Test
    public void notEmpty() {

        ValidationResult result = Verifier.init().notEmpty(nameList2, "用户列表").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().notEmpty(nameList1, "用户列表").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void notBlank() {
        ValidationResult result = Verifier.init().notBlank(username3, "用户名").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().notBlank(username1, "用户名").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void gtThan() {
        ValidationResult result = Verifier.init().gtThan(5, 3, "数量").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().gtThan(5, 9, "数量").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void gtEq() {
        ValidationResult result = Verifier.init().gtEq(3, 3, "数量").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().gtEq(5, 9, "数量").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void ltThan() {
        ValidationResult result = Verifier.init().ltThan(2, 3, "数量").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().ltThan(34, 9, "数量").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void ltEq() {
        ValidationResult result = Verifier.init().ltEq(3, 3, "数量").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().ltEq(10, 9, "数量").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void eq() {
        ValidationResult result = Verifier.init().eq(3, 3, "数量").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().eq(5, 9, "数量").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void eachNotNull() {
        ValidationResult result = Verifier.init().eachNotNull(nameList3, "用户列表").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().eachNotNull(nameList2, "用户列表").result();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void regex() {
        ValidationResult result = Verifier.init().regex("3234234", "^[0-9]*$", "金额").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().regex("323a4234", "^[0-9]*$", "金额").result();
        Assert.assertFalse(result.isSuccess());

    }

    @Test
    public void on() {
        ValidationResult result = Verifier.init().on(nameList3, "用户列表", new CollectionEachNotNullValidator()).result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().on(nameList2, "用户列表", new CollectionEachNotNullValidator()).result();
        Assert.assertTrue(result.isSuccess());
    }

}
