# Problem (below) was solved!
Place MvcConfig and SecurityConfig to same ApplicationContext.

https://github.com/MasatoshiTada/spring-security-java-config-sample/commit/eec17cb77a3fe8d8a0f365d83a7e4b796b78a8dc

https://docs.spring.io/spring-security/site/docs/5.0.5.RELEASE/reference/htmlsingle/#mvc-requestmatcher

# Environment
- JDK 8u162
- macOS 10.13.4
- Apache Tomcat 9.0.1
- Spring Framework 5.0.5.RELEASE
- Spring Security 5.0.4.RELEASE

# What's the problem?

When I use `web.ignoring().mvcMatchers()`, `NoSuchBeanDefinitionException` occurs.

`web.ignoring().antMatchers()` does not causes this problem.

```java
package com.example.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig() {
        logger.info("Instantiated.");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/resources/**"); // OK
        web.ignoring().mvcMatchers("/resources/**"); // NG - NoSuchBeanDefinitionException!!!
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().permitAll();
        http.authorizeRequests()
                .anyRequest().authenticated();
        http.logout().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER").and()
                .withUser("admin").password("{noop}password").roles("ADMIN");
    }
}
```

```bash
17:07:34.456 WARN  o.s.w.c.s.AnnotationConfigWebApplicationContext - Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'springSecurityFilterChain' defined in org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.servlet.Filter]: Factory method 'springSecurityFilterChain' threw exception; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'A Bean named mvcHandlerMappingIntrospector of type org.springframework.web.servlet.handler.HandlerMappingIntrospector is required to use MvcRequestMatcher. Please ensure Spring Security & Spring MVC are configured in a shared ApplicationContext.' available
17:07:34.462 ERROR o.s.web.context.ContextLoader - Context initialization failed
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'springSecurityFilterChain' defined in org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.servlet.Filter]: Factory method 'springSecurityFilterChain' threw exception; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'A Bean named mvcHandlerMappingIntrospector of type org.springframework.web.servlet.handler.HandlerMappingIntrospector is required to use MvcRequestMatcher. Please ensure Spring Security & Spring MVC are configured in a shared ApplicationContext.' available
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:587)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1250)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1099)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:541)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:501)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:317)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:228)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:315)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:304)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:760)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:869)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:550)
	at org.springframework.web.context.ContextLoader.configureAndRefreshWebApplicationContext(ContextLoader.java:409)
	at org.springframework.web.context.ContextLoader.initWebApplicationContext(ContextLoader.java:291)
	at org.springframework.web.context.ContextLoaderListener.contextInitialized(ContextLoaderListener.java:103)
	at org.apache.catalina.core.StandardContext.listenerStart(StandardContext.java:4641)
	at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:5105)
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183)
	at org.apache.catalina.core.ContainerBase.addChildInternal(ContainerBase.java:740)
	at org.apache.catalina.core.ContainerBase.addChild(ContainerBase.java:716)
	at org.apache.catalina.core.StandardHost.addChild(StandardHost.java:703)
	at org.apache.catalina.startup.HostConfig.manageApp(HostConfig.java:1729)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.apache.tomcat.util.modeler.BaseModelMBean.invoke(BaseModelMBean.java:287)
	at com.sun.jmx.interceptor.DefaultMBeanServerInterceptor.invoke(DefaultMBeanServerInterceptor.java:819)
	at com.sun.jmx.mbeanserver.JmxMBeanServer.invoke(JmxMBeanServer.java:801)
	at org.apache.catalina.mbeans.MBeanFactory.createStandardContext(MBeanFactory.java:456)
	at org.apache.catalina.mbeans.MBeanFactory.createStandardContext(MBeanFactory.java:405)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.apache.tomcat.util.modeler.BaseModelMBean.invoke(BaseModelMBean.java:287)
	at com.sun.jmx.interceptor.DefaultMBeanServerInterceptor.invoke(DefaultMBeanServerInterceptor.java:819)
	at com.sun.jmx.mbeanserver.JmxMBeanServer.invoke(JmxMBeanServer.java:801)
	at javax.management.remote.rmi.RMIConnectionImpl.doOperation(RMIConnectionImpl.java:1468)
	at javax.management.remote.rmi.RMIConnectionImpl.access$300(RMIConnectionImpl.java:76)
	at javax.management.remote.rmi.RMIConnectionImpl$PrivilegedOperation.run(RMIConnectionImpl.java:1309)
	at javax.management.remote.rmi.RMIConnectionImpl.doPrivilegedOperation(RMIConnectionImpl.java:1401)
	at javax.management.remote.rmi.RMIConnectionImpl.invoke(RMIConnectionImpl.java:829)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.rmi.server.UnicastServerRef.dispatch(UnicastServerRef.java:361)
	at sun.rmi.transport.Transport$1.run(Transport.java:200)
	at sun.rmi.transport.Transport$1.run(Transport.java:197)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.rmi.transport.Transport.serviceCall(Transport.java:196)
	at sun.rmi.transport.tcp.TCPTransport.handleMessages(TCPTransport.java:568)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run0(TCPTransport.java:826)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.lambda$run$0(TCPTransport.java:683)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run(TCPTransport.java:682)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.servlet.Filter]: Factory method 'springSecurityFilterChain' threw exception; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'A Bean named mvcHandlerMappingIntrospector of type org.springframework.web.servlet.handler.HandlerMappingIntrospector is required to use MvcRequestMatcher. Please ensure Spring Security & Spring MVC are configured in a shared ApplicationContext.' available
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:579)
	... 61 common frames omitted
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'A Bean named mvcHandlerMappingIntrospector of type org.springframework.web.servlet.handler.HandlerMappingIntrospector is required to use MvcRequestMatcher. Please ensure Spring Security & Spring MVC are configured in a shared ApplicationContext.' available
	at org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry.createMvcMatchers(AbstractRequestMatcherRegistry.java:167)
	at org.springframework.security.config.annotation.web.builders.WebSecurity$IgnoredRequestConfigurer.mvcMatchers(WebSecurity.java:354)
	at org.springframework.security.config.annotation.web.builders.WebSecurity$IgnoredRequestConfigurer.mvcMatchers(WebSecurity.java:362)
	at com.example.security.config.SecurityConfig.configure(SecurityConfig.java:23)
	at com.example.security.config.SecurityConfig.configure(SecurityConfig.java:11)
	at com.example.security.config.SecurityConfig$$EnhancerBySpringCGLIB$$b5ea5986.configure(<generated>)
	at org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder.configure(AbstractConfiguredSecurityBuilder.java:384)
	at org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder.doBuild(AbstractConfiguredSecurityBuilder.java:330)
	at org.springframework.security.config.annotation.AbstractSecurityBuilder.build(AbstractSecurityBuilder.java:41)
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration.springSecurityFilterChain(WebSecurityConfiguration.java:104)
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration$$EnhancerBySpringCGLIB$$20da523.CGLIB$springSecurityFilterChain$0(<generated>)
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration$$EnhancerBySpringCGLIB$$20da523$$FastClassBySpringCGLIB$$ed074a90.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:228)
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:361)
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration$$EnhancerBySpringCGLIB$$20da523.springSecurityFilterChain(<generated>)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154)
	... 62 common frames omitted
13-Apr-2018 17:07:34.462 重大 [RMI TCP Connection(2)-127.0.0.1] org.apache.catalina.core.StandardContext.startInternal One or more listeners failed to start. Full details will be found in the appropriate container log file
13-Apr-2018 17:07:34.466 重大 [RMI TCP Connection(2)-127.0.0.1] org.apache.catalina.core.StandardContext.startInternal 以前のエラーのためにコンテキストの起動が失敗しました [/sample]
[2018-04-13 05:07:34,478] Artifact spring-security-java-config-sample:war: Error during artifact deployment. See server log for details.

```

# How to reproduce
1. `mvn clean package`
2. Deploy WAR to Apache Tomcat 9.0.1 and run server.
3. See logs.
