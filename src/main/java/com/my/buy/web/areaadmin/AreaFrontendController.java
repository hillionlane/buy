package com.my.buy.web.areaadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.entity.Area;
import com.my.buy.service.AreaService;

import ch.qos.logback.classic.Logger;

//controller与前端交互

//与@Service标签作用一致，告诉spring这是一个控制器类，由spring拦截到的访问地址会交由相对于的controller处理
//如网址访问到/superadmin，则交由AreaController类处理，当AreaController类在处理时，会实例化，则完成AreaServiceImpl的自动注入
@Controller
@RequestMapping("/areaadmin")
public class AreaFrontendController 
{
	 Logger logger=(Logger) LoggerFactory.getLogger(AreaFrontendController.class);
	//@Autowired在调用areaService时，将areaService的实现类注入
	@Autowired
	private AreaService areaService;
	//访问主目录下的哪个方法，指定接受get请求（默认情况下也是get请求）
	@RequestMapping(value="/listarea",method=RequestMethod.GET)
	//告诉cotroller，其返回的对象自动转换成jason
	//@responseBody注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，
	//写入到response对象的body区，通常用来返回JSON数据或者是XML数据
	@ResponseBody
	private Map<String,Object> listArea()
	{
		//logger用info信息记录程序的开始和结束
		logger.info("====start====");
		long startTime=System.currentTimeMillis();
		Map<String,Object> modelMap=new HashMap<String,Object>();
		List<Area> areaList=new ArrayList<Area>();
		try{ 
			areaList=areaService.getAreaList(); 
			modelMap.put("success", true);
			modelMap.put("areaList", areaList);
		}catch(Exception e)
		{
			//捕获异常并打印
			e.printStackTrace();
			//将错误信息打印出来
			modelMap.put("success",false);
			modelMap.put("errMsg", e.toString());
		}
		logger.error("test error!");
		long endTime=System.currentTimeMillis();
		//用debug调优，程序花费的时间
		logger.debug("costTime:[{}ms]",endTime-startTime);
		logger.info("===end===");
		return modelMap;
	}
}
