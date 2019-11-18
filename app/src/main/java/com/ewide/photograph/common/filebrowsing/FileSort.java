package com.ewide.photograph.common.filebrowsing;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * 自定义文件字典排序
 * @author Taozebi
 * @date 2016年10月10日
 */

public class FileSort {
	public static File[] sortFile(File[] files) {
		List<File> listfile = Arrays.asList(files);
		Collections.sort(listfile, new CustomComparator());   //按照指定的规则进行一个排序
		File[] array = (File[]) listfile.toArray(new File[listfile.size()]); 
		return array;
	}
}