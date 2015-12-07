package com.seegeek.cms.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.seegeek.cms.param.Param;

import sun.misc.BASE64Encoder;
import Decoder.BASE64Decoder;

public class QRCodeDecoderHandlerUtil {
	private static  String imageURL = "d:/content/fans.png";

	/** 
	 * 解析二维码 
	 * @param imgStr 图片的Base64信息 
	 * @return 
	 */
	public static String decoderQRCodeForBase64(String imgStr) throws Exception {
		if (imgStr == null||imgStr.length()==0) {
			return "";
		}
		//分离
		String[] str=imgStr.split(",");
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(str[1]);
			          for(int i=0;i<b.length;++i){ 
			              if(b[i]<0){//调整异常数据 
			                  b[i]+=256; 
			              } 
			          } 
		InputStream input = new ByteArrayInputStream(b);
		InputStream in = null;
		FileOutputStream out = null;
		File	file= new File(Param.upload_filepath);
		if(!file.exists())
		{
			file.mkdir();
		}
		//图片
		String icon=UUID.randomUUID().toString()+".png";
		try {
			in = input;
			out = new FileOutputStream(file.getPath()+"/"+icon);
			IOUtils.copy(in, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		//	          String content = decoderQRCode(input); 
		return icon;

	}

}
