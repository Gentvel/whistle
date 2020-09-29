---
title: Spring系列 Annotation
date: 2020-08-27
sidebar: auto
prev: false
next: false
---

## 一、Configuration
作用： 表明当前类时spring的一个配置类，作用是替代spring的applicationContext.xml。本质上是一个Component注解，修饰的类会被存入spring的ioc容器中。  
使用场景： 主要配置一些初始化的类，通常和@Bean注解使用
## 二、ComponentSacn
作用：扫描带注解的包生成bean交给容器管理
## 三、Bean
作用：在容器中生成bean，beanname 默认为方法名，方法重载会以最后的方法为准。
可以注解在注解上，自定义注解可添加其他需求
## 四、Import
作用： 该注解是卸载类上的，通常和注解驱动的配置类一起使用。它是引入其他配置类的。使用此注解后，可以使我们的注解驱动开发和早期的xml配置一样，分别配置不同的内容，使配置更加清晰。同时指定了此注解后，被引入的类不再使用@Configuration和@Component

使用场景：
当我们在使用注解开发的时候，由于配置项过多的情况下，都写在一个类里的话，配置结构和内容将杂乱不堪，此时使用此注解可以把配置进行分类配置

### 4.1 ImportSelector & ImportBeanDifinitionRegister

在注入bean时，有很多中方法，可以使用@Component、@Controller、@Service、@Regitory等，或者使用@Configuration和@Bean。
但是，在当我们注入很多类的时候，每个类上写这些注解会变得很麻烦，所以我们可以使用其他类似@EnableEurekaClient这样的注解去实现类的加载注入。

而ImportSelector和ImportBeanDifinitionRegister就是实现这样的功能。

ImportSelector是一个接口，使用时需要提供实现类。实现类中返回要注册的bean的全限定类名数组，然后执行ConfigurationClassParser类中的processImports方法注册bean对象。

ImportBeanDifinitionRegister也是一个接口，使用时需要提供实现类，在实现类中手动注入到容器中。

:::warning 注意
实现了ImportSelector和ImportBeanDifinitionRegister的类不会被解析成一个bean注册到容器中
:::


## 五、PropertySource

## 六、DepdendsOn
用于指定某个类的创建依赖的bean对象必须被先创建出来，@DependsOn("类名")
## 七、Lazy
用于指定单例bean对象的创建实际。在没有使用这个注解时，单例bean的生命周期与容器相同，但使用此注解后，单例对象的创建时机变成了第一次使用时创建。
<!-- :::warning 注意
这不是延迟加载思想。
::: -->
## 八、Conditional
根据条件选择注入bean的对象

## 九、Profile
环境不同时使用
同@ActiveProfile使用

## 十、Scope

## 十一、Inject&Named | Autowired&Qualifier

## 十二、Resource
## 十三、Primary
