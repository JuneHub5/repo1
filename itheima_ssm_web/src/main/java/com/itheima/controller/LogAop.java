package com.itheima.controller;

import com.itheima.ssm.domain.SysLog;
import com.itheima.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 切面类处理日志信息
 *
 * @auther itheima
 * @create 2018-09-05 19:39
 */
@Component
@Aspect
public class LogAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService sysLogService;

    private Date startTime;
    private Class executionClass;
    private Method executionMethod;


    @Before("execution(* com.itheima.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws Exception{
        startTime = new Date();//访问时间

        executionClass = jp.getTarget().getClass();//获取访问的类

        String methodName = jp.getSignature().getName();//获取访问时的方法

        Object[] args = jp.getArgs();//获取访问时方法的参数
        if (args == null || args.length == 0){ //无参数
            executionMethod = executionClass.getMethod(methodName);//只能获取无参数方法
        }else {
            // 有参数，就将args中所有元素遍历，获取对应的Class,装入到一个Class[]
            Class[] classes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classes[i] = args[i].getClass();
            }
            executionMethod = executionClass.getMethod(methodName,classes);//获取有参数的方法
        }
    }


    @After("execution(* com.itheima.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception{
        // 获取类上的@RequestMapping对象
        if (executionClass != SysLogController.class){
            RequestMapping classAannotation = (RequestMapping) executionClass.getAnnotation(RequestMapping.class);
            if (classAannotation != null){
                //获取方法上的@RequestMapping对象
                RequestMapping methodAnnotation = executionMethod.getAnnotation(RequestMapping.class);
                if (methodAnnotation != null){
                    String url = "";
                    url = classAannotation.value()[0] + methodAnnotation.value()[0];
                    SysLog sysLog = new SysLog();

                    //获取访问时长
                    long executionTime = new Date().getTime() - startTime.getTime();

                    // 将sysLog对象属性封装
                    //获取执行时长
                    sysLog.setExecutionTime(executionTime);

                    //获取访问资源路径
                    sysLog.setUrl(url);

                    //获取访问 ip
                    String ip = request.getRemoteAddr();
                    sysLog.setIp(ip);

                    //获取当前访问的用户
                    SecurityContext context = SecurityContextHolder.getContext();
                    String username = ((User) context.getAuthentication().getPrincipal()).getUsername();
                    sysLog.setUsername(username);

                    //获取当前访问的方法名
                    sysLog.setMethod("[类名 ]"+executionClass.getName() + "[ 方法名 ]"+executionMethod.getName());

                    //获取访问时间
                    sysLog.setVisitTime(startTime);

                    //调用service，保存日志信息
                    sysLogService.save(sysLog);
                }
            }
        }
    }
}
