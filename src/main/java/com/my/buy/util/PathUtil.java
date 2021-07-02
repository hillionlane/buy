package com.my.buy.util;

import java.io.File;

/**
 * 返回项目中所用图片的根路径
 *
 */
 
public class PathUtil {
	//获取/的分隔符
    //private static String seperator=System.getProperty("file.seperator");
	public static String getImgBasePath()
	{
		String os=System.getProperty("os.name");
		String basePath="";
		//如果是window，则存放在
		//为了实现动态更新，防止有新用户上传图片图片会丢失，保持在服务器另一个路径之下
		if(os.toLowerCase().startsWith("win"))
		{
			basePath="C:/WorkSpace/eclipse/buy/images";
		}
		else
		{
			basePath="/home/hillion/image";
		}
		//确保basePath的路径在具体操作系统中有效
       	basePath=basePath.replace("/", File.separator);
		return basePath;
	}
	/**
	 * 注明商品图片应该存放的位置
	 * 商品是用户创建的应存放在用户id之下
	 * 
	 */
    public static String getProductImagePath(long userId) {
        String imagePath = "/upload/item/product/"+ userId + "/";
        return imagePath.replace("/", File.separator);
    }
    public static String getUserImagePath(long userId) {
        String imagePath = "/upload/item/user/"+ userId + "/";
        return imagePath.replace("/", File.separator);
    }
    public static String getHeadLineImagePath()
    {
    	String imagePath = "/upload/item/headline/"+"/";
        return imagePath.replace("/", File.separator);
    }
    
    public static String getProductCategoryImagePath() {
        String imagePath = "/upload/item/productcategory/"+ "/";
        return imagePath.replace("/", File.separator);
    }
}
