package com.my.buy.util;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.my.buy.dto.ImageHolder;
 

public class Test 
{
	public static void main(String[] args) throws FileNotFoundException {
    Font font = new Font("微软雅黑", Font.PLAIN, 10);                     //水印字体
    String srcImgPath="C:/Users/hzq/Pictures/photos/sss.jpg"; //源图片地址
    File img=new File(srcImgPath);
    InputStream is=new FileInputStream(img);
    ImageHolder imageHolder=new ImageHolder("sss.jpg", is);
    String tarImgPath="C:/Users/hzq/Pictures/photos/sssa.jpg"; //待存储的地址
    String waterMarkContent="@我是一个很长的水印啊啊啊啊啊啊啊啊aaaa";  //水印内容
    Color color=new Color(255,255,255,128);                               //水印图片色彩以及透明度
    ImageUtil.generateNormalWordImg(imageHolder, tarImgPath, waterMarkContent) ;

} 

}
