package com.dexcoder.commons.result;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.dexcoder.commons.exceptions.AssistantException;

/**
 * 系统返回结果
 * 
 * User: liyd
 * Date: 2/13/14
 * Time: 4:36 PM
 */
public class RunBinder {

    /** 线程变量，存放运行错误信息 */
    private static ThreadLocal<RunResult> resultHolder = new ThreadLocal<RunResult>();

    /**
     * 设置结果
     *
     * @param e the e
     */
    public static void addError(AssistantException e) {
        addResult(false, e.getResultCode(), e.getResultMsg());
    }

    /**
     * 设置结果
     *
     * @param e the e
     */
    public static void addError(Throwable e) {
        addResult(false, "UN_KNOWN_EXCEPTION", e.getMessage());
    }

    /**
     * 添加错误信息，success将变为false
     * @param errorMsg
     */
    public static void addError(String errorMsg) {
        addResult(false, null, errorMsg);
    }

    /**
     * 添加错误信息
     * 
     * @param errorCode
     * @param errorMsg
     */
    public static void addError(String errorCode, String errorMsg) {
        addResult(false, errorCode, errorMsg);
    }

    /**
     * 添加运行消息，success仍然为true
     *
     * @param message
     */
    public static void addMessage(String message) {

        addResult(true, null, message);
    }

    /**
     * 添加运行消息，success仍然为true
     *
     * @param message
     */
    public static void addMessage(String code, String message) {

        addResult(true, code, message);
    }

    /**
     * 添加结果信息
     * 
     * @param success
     * @param code
     * @param message
     */
    private static void addResult(boolean success, String code, String message) {
        RunResult runResult = resultHolder.get();
        if (runResult == null) {
            runResult = new RunResult();
            resultHolder.set(runResult);
        }
        runResult.setSuccess(success);
        runResult.setCode(code);
        if (success) {
            runResult.addMessage(message);
        } else {
            runResult.addError(message);
        }
    }

    /**
     * 是否成功
     * 
     * @return
     */
    public static boolean isSuccess() {
        RunResult runResult = resultHolder.get();
        if (runResult != null) {
            return runResult.isSuccess();
        }
        return true;
    }

    /**
     * 是否有错误
     * 
     * @return
     */
    public static boolean hasErrors() {
        RunResult runResult = resultHolder.get();
        if (runResult != null && !runResult.isSuccess()) {
            return true;
        }
        return false;
    }

    /**
     * 是否有错误
     * 
     * @return
     */
    public static boolean hasMessages() {
        RunResult runResult = resultHolder.get();
        if (runResult != null && CollectionUtils.isNotEmpty(runResult.getMessages())) {
            return true;
        }
        return false;
    }

    /**
     * 获取错误结果
     *
     * @param isClear true时从内存中清除错误信息
     * @return run result
     */
    public static RunResult getRunResult(boolean isClear) {
        RunResult runResult = resultHolder.get();
        if (isClear) {
            resultHolder.remove();
        }
        return runResult;
    }

    /**
     * 获取错误结果,默认获取后从内存中清除
     *
     * @return
     */
    public static RunResult getRunResult() {
        return getRunResult(true);
    }

    /**
     * 获取运行结果信息
     *
     * @param isClear true时从内存中清除信息
     * @return str messages
     */
    public static String getStrMessages(boolean isClear) {
        RunResult runResult = resultHolder.get();
        if (runResult != null) {
            if (isClear) {
                resultHolder.remove();
            }
            return runResult.getStrMessages();
        }
        return null;
    }

    /**
     * 获取运行结果信息
     *
     * @return str messages
     */
    public static String getStrMessages() {
        return getStrMessages(true);
    }

    /**
     * 获取错误结果信息
     *
     * @param isClear true时从内存中清除错误信息
     * @return str errors
     */
    public static String getStrErrors(boolean isClear) {
        RunResult runResult = resultHolder.get();
        if (runResult != null) {
            if (isClear) {
                resultHolder.remove();
            }
            return runResult.getStrErrors();
        }
        return null;
    }

    /**
     * 获取错误结果信息
     *
     * @return str errors
     */
    public static String getStrErrors() {
        return getStrErrors(true);
    }

    /**
     * 获取运行结果信息
     *
     * @param isClear true时清除信息
     * @return messages
     */
    public static List<String> getMessages(boolean isClear) {
        RunResult runResult = resultHolder.get();
        if (runResult != null) {
            if (isClear) {
                resultHolder.remove();
            }
            return runResult.getMessages();
        }
        return null;
    }

    /**
     * 获取运行结果信息
     *
     * @return messages
     */
    public static List<String> getMessages() {
        return getMessages(true);
    }

    /**
     * 获取错误结果信息
     *
     * @param isClear true时清除信息
     * @return errors
     */
    public static List<String> getErrors(boolean isClear) {
        RunResult runResult = resultHolder.get();
        if (runResult != null) {
            if (isClear) {
                resultHolder.remove();
            }
            return runResult.getErrors();
        }
        return null;
    }

    /**
     * 获取错误结果信息
     *
     * @return errors
     */
    public static List<String> getErrors() {
        return getErrors(true);
    }

    /**
     * 获取错误结果码
     *
     * @return
     */
    public static String getCode(boolean isClear) {
        RunResult runResult = resultHolder.get();
        if (runResult != null) {
            if (isClear) {
                resultHolder.remove();
            }
            return runResult.getCode();
        }
        return null;
    }

    /**
     * 获取错误结果码
     *
     * @return
     */
    public static String getCode() {
        return getCode(true);
    }

    /**
     * 清除信息，释放资源
     */
    public static void clear() {
        resultHolder.remove();
    }
}
