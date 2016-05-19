package com.dexcoder.commons.interceptor;

import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * Created by liyd on 16/3/17.
 */
public class RunBinderTransactionAspectSupport extends TransactionAspectSupport {

    public static void setRollbackOnly() {
        TransactionAspectSupport.TransactionInfo transactionInfo = currentTransactionInfo();
        if (transactionInfo != null && transactionInfo.hasTransaction()) {
            transactionInfo.getTransactionStatus().setRollbackOnly();
        }
    }

    public static boolean isRollbackOnly() {
        TransactionAspectSupport.TransactionInfo transactionInfo = currentTransactionInfo();
        if (transactionInfo != null && transactionInfo.hasTransaction()) {
            return transactionInfo.getTransactionStatus().isRollbackOnly();
        }
        return false;
    }

}
