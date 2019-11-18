package com.ewide.photograph.common.filebrowsing;

import java.io.File;
import java.util.Comparator;

/**
 * Created by Taoze on 2016/11/18.
 */
public class CustomComparator implements Comparator<File> {

    @Override
    public int compare(File pFile1, File pFile2) {
        /**
         * 1.先比较文件夹 （文件夹在文件的顺序之上）
         * 2.以A-Z的字典排序
         * 3.比较文件夹和文件
         * 4.比较文件和文件夹
         */
        if (pFile1.isDirectory() && pFile2.isDirectory()) {
            return pFile1.getName().compareToIgnoreCase(pFile2.getName());
        } else {
            if (pFile1.isDirectory() && pFile2.isFile()) {
                return -1;
            } else if (pFile1.isFile() && pFile2.isDirectory()) {
                return 1;
            } else {
                return pFile1.getName().compareToIgnoreCase(pFile2.getName());
            }
        }
    }
}
