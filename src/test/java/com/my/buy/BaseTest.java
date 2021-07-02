package com.my.buy;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 初始化spring容器
 * 用来配置spring和junit整合，junit启动时加载springIOC容器
 * @author 野甜甜的空心菜
 *
 */
//使用spring和mybatis整合的方式
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件的位置，并加载他们，为dao和service层做单元测试提供服务
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class BaseTest 
{

}
