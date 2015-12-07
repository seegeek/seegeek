package com.seegeek.cms.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.IOUtils;

import com.seegeek.cms.param.Param;

public class UploadFileUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

	
	public static String convertPath(HttpServletRequest request,String path)
	{
		
		String projectPath= request.getSession().getServletContext().getRealPath("/").replaceAll("\\\\","/");
  
		return 	projectPath.replaceAll("\\", "//");
	}
	/**
	 * 保存上传的文件，返回保存的路径
	 * 重新整合上传文件
	 * 上传分为 
	 * type=1,2
	 * 如果type=1,指定默认文件夹
	 * type=2，制定默认文件夹，生成规则需要修改,
	 * 证明需要
	 * @param request
	 * @param formFile
	 * @return
	 */
	public static String saveUploadFile(HttpServletRequest request, FileItemStream formFile) {
		// 1，获取路径
		
//		String basePath ="d:/content/";// 返回值最后没有'/'
		String basePath =Param.upload_filepath; // 返回值最后没有'/'
		String path="";
		String subPath="/"+sdf.format(new Date());
		String fName="";
			 //修改文件名称
		int suffix=formFile.getName().lastIndexOf(".");
		fName=UUID.randomUUID().toString()+formFile.getName().substring(suffix, formFile.getName().length());
		path =  subPath +fName;
	
		// 如果文件夹不存在，就创建
		File directory = new File(basePath+subPath);
		if(!directory.exists()){
			directory.mkdirs();
		}
		// 2，保存文件
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = formFile.openStream();
			out = new FileOutputStream(basePath+path);
			IOUtils.copy(in, out);
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return path;
	}
}
