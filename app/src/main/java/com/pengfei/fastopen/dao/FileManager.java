package com.pengfei.fastopen.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 提供一些文件的处理方法
 * Created by mengfei on 2016/8/15.
 */
public class FileManager {

    /**
     * Copy文件
     *
     * @param filePath     要进行Copy的文件的位置
     * @param copyFilePath Copy结束文件存放的文件夹
     * @param copyFileName Copy的文件的名称
     * @return 文件是否Copy成功
     */
    public boolean copyFile(String filePath, String copyFilePath, String copyFileName) {
        return copyFile(new File(filePath), copyFilePath, copyFileName);
    }

    /**
     * Copy文件
     *
     * @param file         要进行Copy的文件
     * @param copyFilePath Copy结束文件存放的文件夹
     * @param copyFileName Copy的文件的名称
     * @return 文件是否Copy成功
     */
    public boolean copyFile(File file, String copyFilePath, String copyFileName) {
        if (!file.exists()) {
            return false;
        } else {
            FileReader fileReader = null;
            FileWriter fileWriter = null;
            try {
                fileReader = new FileReader(file);
                File parentFile = new File(copyFilePath);
                if (!parentFile.exists()) {
                    parentFile.mkdir();
                }
                File copyFile = new File(parentFile, copyFileName);
                if (!copyFile.exists()) {
                    copyFile.createNewFile();
                }
                fileWriter = new FileWriter(copyFile);
                char[] buffer = new char[4 * 1024];
                while (fileReader.read(buffer) != -1) {
                    fileWriter.write(buffer,0,buffer.length);
                }
                fileWriter.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if(fileWriter != null) {
                        fileWriter.close();
                    }
                    if (fileReader != null) {
                        fileReader.close();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
