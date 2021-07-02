package com.my.buy.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.my.buy.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;


public class ImageUtil 
{
	//获取classPath的绝对路径
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	//生成时间和随机数
	private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r=new Random();
	
	
//	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr)
//	{
//		Logger logger=LoggerFactory.getLogger(ImageUtil.class);
//		//由于用户上传的文件可能重名，因此这里给每个用户上传的文件设置一个随机的名字
//		String realFileName=getRandomFileName();
//		//获取图片的扩展名（jpg/png），再来组合生成新的名字
//		String extension=getFileExtension(thumbnail.getImageName());
//		//新命名的文件存储在targetAddr这个目录下，但这个目录有时不存在，需要创建 
//		makeDirPath(targetAddr);
//		//文件的相对路径为
//		String relativeAddr=targetAddr+realFileName+extension;
//		logger.debug("current relativeAddr is:"+relativeAddr);
//		//新的文件的路径就=相对路径+根路径
//		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
//		logger.debug("current complete is:"+PathUtil.getImgBasePath()+relativeAddr);
//		//创建缩略图thumbnail.getInputStream()获取用户上传图片，size调整图片大小
//		//watermark表示添加水印，0.25f表示水印的透明度
//		//0.8f表压缩率，tofile表图片处理之后保存的位置
//		try 
//		{
//			//thumbnail.getImage()=thumbnailInputStream
//          Thumbnails.of(thumbnail.getImage()).size(200, 200)
//          .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "watermark.jpg")),0.25f)
//          .outputQuality(0.8f).toFile(dest);
//      }
//		catch (IOException e) 
//		{
//          // TODO: handle exception
//          e.printStackTrace();
//      }
//		return relativeAddr;
//	}
	/**
	 * //不给图片加图片水印，直接输出
	 * 接收用户上传的图片，targetAddr(PathUtil.getProductImagePath)为保存文件的目录，是相对路径
	 * @param thumbnail 在商品图片中=PathUtil.getShopImagePath(shopId)
	 * @param targetAddr
	 * @return 返回图片的绝对路径
	 *  生成商品图片绝对值
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateNormalImg(ImageHolder thumbnail,String targetAddr)
	{
		Logger logger=LoggerFactory.getLogger(ImageUtil.class);
		//由于用户上传的文件可能重名，因此这里给每个用户上传的文件设置一个随机的名字
		String realFileName=getRandomFileName();
		//获取图片的扩展名（jpg/png），再来组合生成新的名字
		String extension=getFileExtension(thumbnail.getImageName());
		//新命名的文件存储在targetAddr这个目录下，但这个目录有时不存在，需要创建 
		makeDirPath(targetAddr);
		
		//文件的相对路径为
		String relativeAddr=targetAddr+realFileName+extension;
		logger.debug("current relativeAddr is:"+relativeAddr);
		//新的文件的路径就=相对路径+根路径
//		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		String dest=PathUtil.getImgBasePath()+relativeAddr;
		logger.debug("current complete is:"+dest);
		//创建缩略图thumbnail.getInputStream()获取用户上传图片，size调整图片大小
		//watermark表示添加水印，0.25f表示水印的透明度
		//0.8f表压缩率，tofile表图片处理之后保存的位置
		try 
		{
			//thumbnail.getImage()=thumbnailInputStream
			//.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "watermark.jpg")),0.25f)
			if(dest.contains("user"))
			{
				Thumbnails.of(thumbnail.getImage()).size(337, 200).outputQuality(0.9f).toFile(dest);
			}
			else
			{
				Thumbnails.of(thumbnail.getImage()).size(337, 640).outputQuality(0.9f).toFile(dest);
			}
			
		}
		catch (IOException e) 
		{
          // TODO: handle exception
          e.printStackTrace();
		}
		//old,new
		return relativeAddr.replace(File.separator,"/");
	}
	
	/**
	 * 给图片添加文字水印
	 * @param thumbnail 原图片地址
	 * @param targetAddr 图片保存地址
	 * @param waterMarkContent 水印内容
	 * @return
	 */
	public static String generateNormalWordImg(ImageHolder thumbnail,String targetAddr, String waterMarkContent)
	{
		//由于用户上传的文件可能重名，因此这里给每个用户上传的文件设置一个随机的名字
	    String realFileName=getRandomFileName();
		//获取图片的扩展名（jpg/png），再来组合生成新的名字
		String extension=getFileExtension(thumbnail.getImageName());
	    //新命名的文件存储在targetAddr这个目录下，但这个目录有时不存在，需要创建 
		makeDirPath(targetAddr);
		//文件全程的相对路径为
		String relativeAddr=targetAddr+realFileName+extension;
		//文件全程的绝对路径为
		String absoluteAddr=PathUtil.getImgBasePath()+relativeAddr;
		try
		{ 
			 //将上传文件等比例压缩之后输出至absoluteAddr
			 Thumbnails.of(thumbnail.getImage()).size(337, 640).outputQuality(0.9f).toFile(absoluteAddr);
			 //将目标地址的文件转换成文件流
			 File absoluteFile=new File(absoluteAddr);
			 //将文件转化为图片
			 Image srcImg = ImageIO.read(absoluteFile); 
			 int tempWidth=srcImg.getWidth(null);
			 int tempHeight=srcImg.getHeight(null);  
			 //加水印
			 BufferedImage bufImg=new BufferedImage(tempWidth, tempHeight, BufferedImage.TYPE_INT_RGB);
			 Graphics2D g = bufImg.createGraphics();
			 //将原图片写入画板
			 g.drawImage(srcImg, 0, 0, tempWidth,tempHeight,null);
			 //根据图片的背景设置水印颜色
			 g.setColor(Color.WHITE);
			 //new java.awt.Font("微软雅黑", java.awt.Font.BOLD, srcImgHeight/2)
			 g.setFont(new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 15));
			 //设置水印透明度
			 g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.85f));
			 //设置水印位置和内容 
			 int x= tempWidth - (getWatermarkLength(waterMarkContent, g)+5);
			 int y =tempHeight -15; 
			 //画出水印
			 g.drawString(waterMarkContent,x,y);
			 //释放资源
			 g.dispose();
			 //输出图片
			 FileOutputStream outImgStream = new FileOutputStream(absoluteAddr); 
			 ImageIO.write(bufImg, "jpg", outImgStream); 
			 System.out.println("添加水印完成");  
			 outImgStream.flush();  
	         outImgStream.close();  
		}
		catch(Exception e)
		{
			 e.printStackTrace();
		} 
		return relativeAddr.replace(File.separator,"/");
	}
	 
	/**
	 * 获取水印长度
	 * @param waterMarkContent
	 * @param g
	 * @return
	 */
	private static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
		// TODO Auto-generated method stub
		return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());  
	}
	
	/**
	 * 判断是否需要更新图片，若需要则删除原来的
	 * @param storePath
	 * storePath是文件路径还是目录文件
	 * 如果storePath是文件路径，则删除该文件
	 * 如果storePath是目录路径，则删除该目录下的所有文件
	 */
	public static void deleteFileOrPath(String storePath)
	{
		File fileOrPath=new File((PathUtil.getImgBasePath()+storePath).toString());
		
		if(fileOrPath.exists())
		{
			//如果是目录路径，则递归删除该目录下所有文件，并最终删除自己
			if(fileOrPath.isDirectory())
			{
				File files[]=fileOrPath.listFiles();
				for(int i=0;i<files.length;i++)
				{
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
	
	
	/**
	 * 生成随机的文件名，当前年月日小时分钟秒钟+五位随机数
	 * @return
	 */
	public static String getRandomFileName() 
	{
		// 获取随机的五位数,在10000-89999之间取值
		int rannum=r.nextInt(89999)+10000;
		String nowTimeStr=sDateFormat.format(new Date());
		return nowTimeStr+rannum;
	}
	/**
	 * 获取输入文件流的扩展名
	 * @param thumbnail
	 * @return
	 */
	public static String getFileExtension(String  fileName) 
	{
		//获取最后点号的位置，返回之后的字符即可
		//String originalFileName=thumbnail.getName();
		return fileName.substring(fileName.lastIndexOf("."));
	}
	
	/**
	 * 创建目标路径所涉及到的目录，即/home/work/hillion/xxx.jpg
	 * 那么home work hillion这三个文件夹都得自动创建
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) 
	{
		//获取targetAddr的绝对路径
		String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
		//将全路径传入
		File dirPath=new File(realFileParentPath);
		//判断路径是否存在
		if(!dirPath.exists())
		{
			//不存在则递归创建
			dirPath.mkdirs();
		}
		
	}

	public static void main(String[] args) throws IOException
	{
//		Thumbnails.of(new File("/Users/野甜甜的空心菜/Pictures/Saved Pictures/nanshen.jpg")).size(200, 200)
		/*水印的位置，水印图片的路径，水印的透明度*/
//				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
//				.outputQuality(0.8f).toFile("/Users/野甜甜的空心菜/Pictures/Saved Pictures/nanshen2.jpg");
		//创建文件夹
		//File dirPath=new File("C:\\Users\\Hillionlane\\projectdev\\image\\1");
		File fileOrPath=new File(("C:\\Users\\photos\\ddd.jpg"));
		
		if(fileOrPath.exists())
		{
			//如果是目录路径，则递归删除该目录下所有文件，并最终删除自己
			if(fileOrPath.isDirectory())
			{
				File files[]=fileOrPath.listFiles();
				for(int i=0;i<files.length;i++)
				{
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
}
