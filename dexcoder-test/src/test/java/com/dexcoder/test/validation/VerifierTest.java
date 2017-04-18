package com.dexcoder.test.validation;

import com.dexcoder.commons.utils.EncryptUtils;
import com.dexcoder.commons.validation.CollectionEachNotNullValidator;
import com.dexcoder.commons.validation.ValidationError;
import com.dexcoder.commons.validation.ValidationResult;
import com.dexcoder.commons.validation.Verifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> nameList4;

    @Before
    public void before() {
        nameList2 = new ArrayList<String>();
        nameList2.add("selfly");

        nameList3 = new ArrayList<String>();
        nameList3.add("1111");
        nameList3.add(null);
        nameList3.add("3333");

        nameList4 = new ArrayList<String>();
    }

    @Test
    public void notNull() {

        String bdone123 = EncryptUtils.getMD5("bdone123");
        System.out.println(bdone123.toUpperCase());

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

        String[] array = { "111", "222" };

        result = Verifier.init().notEmpty(array, "数组列表").result();

        Assert.assertTrue(result.isSuccess());

        String[] array2 = {};

        result = Verifier.init().notEmpty(array2, "数组列表").result();

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
    public void minLength() {
        ValidationResult result = Verifier.init().minLength("1234", 3, "字符串").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().minLength("1234", 8, "字符串").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void maxLength() {
        ValidationResult result = Verifier.init().maxLength("1234", 3, "字符串").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().maxLength("1234", 8, "字符串").result();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void eqLength() {
        ValidationResult result = Verifier.init().eqLength("1234", 3, "字符串").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().eqLength("1234", 4, "字符串").result();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void mustEmpty() {
        ValidationResult result = Verifier.init().mustEmpty(nameList1, "列表").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().mustEmpty(nameList3, "列表").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().mustEmpty(nameList4, "列表").result();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void mustFalse() {
        ValidationResult result = Verifier.init().mustFalse(false, "列表").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().mustFalse(null, "列表").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().mustFalse(true, "列表").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void mustTrue() {
        ValidationResult result = Verifier.init().mustTrue(false, "列表").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().mustTrue(null, "列表").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().mustTrue(true, "列表").result();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void minSize() {
        ValidationResult result = Verifier.init().minSize(nameList1, 3, "列表").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().minSize(nameList3, 1, "列表").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().minSize(nameList4, 2, "列表").result();
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void maxSize() {
        ValidationResult result = Verifier.init().maxSize(nameList1, 3, "列表").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().maxSize(nameList3, 1, "列表").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().maxSize(nameList4, 2, "列表").result();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void eqSize() {
        //null 不同于大小为0
        ValidationResult result = Verifier.init().eqSize(nameList1, 0, "列表").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().eqSize(nameList3, 3, "列表").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().eqSize(nameList4, 0, "列表").result();
        Assert.assertTrue(result.isSuccess());
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
        ValidationResult result = Verifier.init().mustEq(3, 3, "数量").result();
        Assert.assertTrue(result.isSuccess());

        result = Verifier.init().mustEq(5, 9, "数量").result();
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

    @Test
    public void with() {

        ValidationResult result = Verifier.init().notBlank("", "字符串").result();
        Assert.assertFalse(result.isSuccess());

        result = Verifier.init().notBlank("", "字符串").with(false).result();
        Assert.assertTrue(result.isSuccess());

    }

    @Test
    public void resultCode() {
        ValidationResult result = Verifier.init().notBlank(username2, "用户名").errorCode("404").result();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(result.getErrors().get(0).getErrorCode(), "404");
    }

}
