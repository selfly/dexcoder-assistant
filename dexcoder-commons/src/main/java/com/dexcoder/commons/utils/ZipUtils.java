package com.dexcoder.commons.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * zip包压缩工具类 jdk原生实现，不支持中文
 */
public class ZipUtils {

    /**
     * 压缩打包文件成zip包
     *
     * @param srcFileName
     * @param targetFileName
     */
    public static void zip(String srcFileName, String targetFileName) {

        ZipOutputStream zos = null;
        BufferedOutputStream bos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(targetFileName));
            bos = new BufferedOutputStream(zos);
            File srcFile = new File(srcFileName);
            zip(zos, srcFile, srcFile.getName(), bos);
        } catch (Exception e) {
            throw new CommonsAssistantException("压缩打包文件成zip包失败:" + srcFileName, e);
        } finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(zos);
        }
    }

    /**
     * 打包压缩文件
     *
     * @param zos
     * @param file
     * @param base
     * @param bos
     * @throws Exception
     */
    private static void zip(ZipOutputStream zos, File file, String base, BufferedOutputStream bos) throws Exception {
        if (file.isDirectory()) {
            // 创建zip压缩进入点base
            zos.putNextEntry(new ZipEntry(base + "/"));
            zos.flush();
            File[] fl = file.listFiles();
            for (int i = 0; i < fl.length; i++) {
                // 递归遍历子文件夹
                zip(zos, fl[i], base + "/" + fl[i].getName(), bos);
            }
        } else {
            //创建zip压缩进入点base
            zos.putNextEntry(new ZipEntry(base));
            bos.write(FileUtils.readFileToByteArray(file));
            bos.flush();
        }
    }
}
