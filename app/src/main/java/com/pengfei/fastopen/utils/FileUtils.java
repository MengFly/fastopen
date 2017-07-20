package com.pengfei.fastopen.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 提供一些有关文件管理的方法
 * Created by mengfei on 2017/7/20.
 */
public class FileUtils {

    public static void copyFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("源文件不能为空");
        } else if (destFile == null) {
            throw new NullPointerException("目标文件不能为空");
        } else if (!srcFile.exists()) {
            throw new FileNotFoundException("源文件 \'" + srcFile + "\' 不存在");
        } else if (srcFile.isDirectory()) {
            throw new IOException("源文件 \'" + srcFile + "\' 不可以是文件夹");
        } else if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source \'" + srcFile + "\' and destination \'" + destFile + "\' are the same");
        } else {
            File parentFile = destFile.getParentFile();
            if (parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination \'" + parentFile + "\' directory cannot be created");
            } else if (destFile.exists() && !destFile.canWrite()) {
                throw new IOException("Destination \'" + destFile + "\' exists but is read-only");
            } else {
                doCopyFile(srcFile, destFile);
            }
        }
    }

    private static void doCopyFile(File srcFile, File destFile) throws IOException {
        if(destFile.exists() && destFile.isDirectory()) {
            throw new IOException("目标文件 \'" + destFile + "\' 不能是文件夹");
        } else {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel input = null;
            FileChannel output = null;

            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                input = fis.getChannel();
                output = fos.getChannel();
                long size = input.size();
                long pos = 0L;

                for(long count = 0L; pos < size; pos += output.transferFrom(input, pos, count)) {
                    count = size - pos > 31457280L?31457280L:size - pos;
                }
            } finally {
                closeStream(output, fos, input, fis);
            }

            if(srcFile.length() != destFile.length()) {
                throw new IOException("Failed to copy full contents from \'" + srcFile + "\' to \'" + destFile + "\'");
            }
        }
    }

    private static void closeStream(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
