---
title: Spring系列 注解驱动
date: 2020-08-27
sidebar: auto
prev: false
next: false
---

代码在code项目中的spring模块

## 一、Configuration
作用： 表明当前类时spring的一个配置类，作用是替代spring的applicationContext.xml。本质上是一个Component注解，修饰的类会被存入spring的ioc容器中。  
使用场景： 主要配置一些初始化的类，通常和@Bean注解使用,在构架的时候传入扫描包时可以首先读取被这个注解修饰的类  
这个注解不是必须的，在AnnotationApplicationContext中还有一个构造器是使用字节码构造

```java
public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
    this();
    register(componentClasses);
    refresh();
}
```

:::warning 
如果在注解驱动开发时，构建ioc容器使用的是传入包，那么此注解不能被省略
:::

Configuration的注解源码就两个属性比较简单

:::details 点击查看源码

```java
package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

	/**
	 * Explicitly specify the name of the Spring bean definition associated with the
	 * {@code @Configuration} class. If left unspecified (the common case), a bean
	 * name will be automatically generated.
	 * <p>The custom name applies only if the {@code @Configuration} class is picked
	 * up via component scanning or supplied directly to an
	 * {@link AnnotationConfigApplicationContext}. If the {@code @Configuration} class
	 * is registered as a traditional XML bean definition, the name/id of the bean
	 * element will take precedence.
	 * @return the explicit component name, if any (or empty String otherwise)
	 * @see AnnotationBeanNameGenerator
	 */
	@AliasFor(annotation = Component.class)
	String value() default "";

	/**
	 * Specify whether {@code @Bean} methods should get proxied in order to enforce
	 * bean lifecycle behavior, e.g. to return shared singleton bean instances even
	 * in case of direct {@code @Bean} method calls in user code. This feature
	 * requires method interception, implemented through a runtime-generated CGLIB
	 * subclass which comes with limitations such as the configuration class and
	 * its methods not being allowed to declare {@code final}.
	 * <p>The default is {@code true}, allowing for 'inter-bean references' via direct
	 * method calls within the configuration class as well as for external calls to
	 * this configuration's {@code @Bean} methods, e.g. from another configuration class.
	 * If this is not needed since each of this particular configuration's {@code @Bean}
	 * methods is self-contained and designed as a plain factory method for container use,
	 * switch this flag to {@code false} in order to avoid CGLIB subclass processing.
	 * <p>Turning off bean method interception effectively processes {@code @Bean}
	 * methods individually like when declared on non-{@code @Configuration} classes,
	 * a.k.a. "@Bean Lite Mode" (see {@link Bean @Bean's javadoc}). It is therefore
	 * behaviorally equivalent to removing the {@code @Configuration} stereotype.
	 * @since 5.2
	 */
	boolean proxyBeanMethods() default true;

}

```
:::


## 二、ComponentSacn
作用：扫描带注解的包生成bean交给容器管理

从测试代码可以得出，被ComponentScan修饰的类不会被加载进IOC容器里面，它只提供应该扫描的包或者其他内容。

默认扫描当前包下的类


:::details 点击查看源码
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {

	/**
      对应的包扫描路径 可以是单个路径，也可以是扫描的路径数组
	 * Alias for {@link #basePackages}.
	 * <p>Allows for more concise annotation declarations if no other attributes
	 * are needed &mdash; for example, {@code @ComponentScan("org.my.pkg")}
	 * instead of {@code @ComponentScan(basePackages = "org.my.pkg")}.
	 */
	@AliasFor("basePackages")
	String[] value() default {};

	/**
    和value一样是对应的包扫描路径 可以是单个路径，也可以是扫描的路径数组
	 * Base packages to scan for annotated components.
	 * <p>{@link #value} is an alias for (and mutually exclusive with) this
	 * attribute.
	 * <p>Use {@link #basePackageClasses} for a type-safe alternative to
	 * String-based package names.
	 */
	@AliasFor("value")
	String[] basePackages() default {};

	/**
    指定具体的扫描的类，在扫描该类时，会在该类的同一个包内或者子包继续扫描
	 * Type-safe alternative to {@link #basePackages} for specifying the packages
	 * to scan for annotated components. The package of each class specified will be scanned.
	 * <p>Consider creating a special no-op marker class or interface in each package
	 * that serves no purpose other than being referenced by this attribute.
	 */
	Class<?>[] basePackageClasses() default {};

	/**
    对应的bean名称的生成器 默认的是BeanNameGenerator子类，可以自定义bean名称构造器，只要继承BeanNameGenerator
	 * The {@link BeanNameGenerator} class to be used for naming detected components
	 * within the Spring container.
	 * <p>The default value of the {@link BeanNameGenerator} interface itself indicates
	 * that the scanner used to process this {@code @ComponentScan} annotation should
	 * use its inherited bean name generator, e.g. the default
	 * {@link AnnotationBeanNameGenerator} or any custom instance supplied to the
	 * application context at bootstrap time.
	 * @see AnnotationConfigApplicationContext#setBeanNameGenerator(BeanNameGenerator)
	 * @see AnnotationBeanNameGenerator
	 * @see FullyQualifiedAnnotationBeanNameGenerator
	 */
	Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

	/**
    处理检测到的bean的scope范围
	 * The {@link ScopeMetadataResolver} to be used for resolving the scope of detected components.
	 */
	Class<? extends ScopeMetadataResolver> scopeResolver() default AnnotationScopeMetadataResolver.class;

	/**
    是否为检测到的组件生成代理
	 * Indicates whether proxies should be generated for detected components, which may be
	 * necessary when using scopes in a proxy-style fashion.
	 * <p>The default is defer to the default behavior of the component scanner used to
	 * execute the actual scan.
	 * <p>Note that setting this attribute overrides any value set for {@link #scopeResolver}.
	 * @see ClassPathBeanDefinitionScanner#setScopedProxyMode(ScopedProxyMode)
	 */
	ScopedProxyMode scopedProxy() default ScopedProxyMode.DEFAULT;

	/**
    控制符合组件检测条件的类文件   默认是包扫描下的  **//*.class
	 * Controls the class files eligible for component detection.
	 * <p>Consider use of {@link #includeFilters} and {@link #excludeFilters}
	 * for a more flexible approach.
	 */
	String resourcePattern() default ClassPathScanningCandidateComponentProvider.DEFAULT_RESOURCE_PATTERN;

	/**
    是否对带有@Component @Repository @Service @Controller注解的类开启检测,默认是开启的
	 * Indicates whether automatic detection of classes annotated with {@code @Component}
	 * {@code @Repository}, {@code @Service}, or {@code @Controller} should be enabled.
	 */
	boolean useDefaultFilters() default true;

	/**
    指定某些定义Filter满足条件的组件 FilterType有5种类型如：
     *                                  ANNOTATION, 注解类型 默认
                                        ASSIGNABLE_TYPE,指定固定类
                                        ASPECTJ， ASPECTJ类型
                                        REGEX,正则表达式
                                        CUSTOM,自定义类型
	 * Specifies which types are eligible for component scanning.
	 * <p>Further narrows the set of candidate components from everything in {@link #basePackages}
	 * to everything in the base packages that matches the given filter or filters.
	 * <p>Note that these filters will be applied in addition to the default filters, if specified.
	 * Any type under the specified base packages which matches a given filter will be included,
	 * even if it does not match the default filters (i.e. is not annotated with {@code @Component}).
	 * @see #resourcePattern()
	 * @see #useDefaultFilters()
	 */
	Filter[] includeFilters() default {};

	/**
    排除某些过来器扫描到的类
	 * Specifies which types are not eligible for component scanning.
	 * @see #resourcePattern
	 */
	Filter[] excludeFilters() default {};

	/**
    扫描到的类是都开启懒加载 ，默认是不开启的
	 * Specify whether scanned beans should be registered for lazy initialization.
	 * <p>Default is {@code false}; switch this to {@code true} when desired.
	 * @since 4.1
	 */
	boolean lazyInit() default false;


	/**
	 * Declares the type filter to be used as an {@linkplain ComponentScan#includeFilters
	 * include filter} or {@linkplain ComponentScan#excludeFilters exclude filter}.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({})
	@interface Filter {

		/**
		 * The type of filter to use.
		 * <p>Default is {@link FilterType#ANNOTATION}.
		 * @see #classes
		 * @see #pattern
		 */
		FilterType type() default FilterType.ANNOTATION;

		/**
		 * Alias for {@link #classes}.
		 * @see #classes
		 */
		@AliasFor("classes")
		Class<?>[] value() default {};

		/**
		 * The class or classes to use as the filter.
		 * <p>The following table explains how the classes will be interpreted
		 * based on the configured value of the {@link #type} attribute.
		 * <table border="1">
		 * <tr><th>{@code FilterType}</th><th>Class Interpreted As</th></tr>
		 * <tr><td>{@link FilterType#ANNOTATION ANNOTATION}</td>
		 * <td>the annotation itself</td></tr>
		 * <tr><td>{@link FilterType#ASSIGNABLE_TYPE ASSIGNABLE_TYPE}</td>
		 * <td>the type that detected components should be assignable to</td></tr>
		 * <tr><td>{@link FilterType#CUSTOM CUSTOM}</td>
		 * <td>an implementation of {@link TypeFilter}</td></tr>
		 * </table>
		 * <p>When multiple classes are specified, <em>OR</em> logic is applied
		 * &mdash; for example, "include types annotated with {@code @Foo} OR {@code @Bar}".
		 * <p>Custom {@link TypeFilter TypeFilters} may optionally implement any of the
		 * following {@link org.springframework.beans.factory.Aware Aware} interfaces, and
		 * their respective methods will be called prior to {@link TypeFilter#match match}:
		 * <ul>
		 * <li>{@link org.springframework.context.EnvironmentAware EnvironmentAware}</li>
		 * <li>{@link org.springframework.beans.factory.BeanFactoryAware BeanFactoryAware}
		 * <li>{@link org.springframework.beans.factory.BeanClassLoaderAware BeanClassLoaderAware}
		 * <li>{@link org.springframework.context.ResourceLoaderAware ResourceLoaderAware}
		 * </ul>
		 * <p>Specifying zero classes is permitted but will have no effect on component
		 * scanning.
		 * @since 4.2
		 * @see #value
		 * @see #type
		 */
		@AliasFor("value")
		Class<?>[] classes() default {};

		/**
		 * The pattern (or patterns) to use for the filter, as an alternative
		 * to specifying a Class {@link #value}.
		 * <p>If {@link #type} is set to {@link FilterType#ASPECTJ ASPECTJ},
		 * this is an AspectJ type pattern expression. If {@link #type} is
		 * set to {@link FilterType#REGEX REGEX}, this is a regex pattern
		 * for the fully-qualified class names to match.
		 * @see #type
		 * @see #classes
		 */
		String[] pattern() default {};

	}

}
```
:::





## 三、Bean
作用：在容器中生成bean，beanname 默认为方法名，方法重载会以最多参数的方法为准。
可以注解在注解上，自定义注解可添加其他需求


:::details 点击查看源码
```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

	/**
	 * Alias for {@link #name}.
	 * <p>Intended to be used when no other attributes are needed, for example:
	 * {@code @Bean("customBeanName")}.
	 * @since 4.3.3
	 * @see #name
	 */
	@AliasFor("name")
	String[] value() default {};

	/**
	 * The name of this bean, or if several names, a primary bean name plus aliases.
	 * <p>If left unspecified, the name of the bean is the name of the annotated method.
	 * If specified, the method name is ignored.
	 * <p>The bean name and aliases may also be configured via the {@link #value}
	 * attribute if no other attributes are declared.
	 * @see #value
	 */
	@AliasFor("value")
	String[] name() default {};

	/**
	 * Are dependencies to be injected via convention-based autowiring by name or type?
	 * <p>Note that this autowire mode is just about externally driven autowiring based
	 * on bean property setter methods by convention, analogous to XML bean definitions.
	 * <p>The default mode does allow for annotation-driven autowiring. "no" refers to
	 * externally driven autowiring only, not affecting any autowiring demands that the
	 * bean class itself expresses through annotations.
	 * @see Autowire#BY_NAME
	 * @see Autowire#BY_TYPE
	 * @deprecated as of 5.1, since {@code @Bean} factory method argument resolution and
	 * {@code @Autowired} processing supersede name/type-based bean property injection
	 */
	@Deprecated
	Autowire autowire() default Autowire.NO;

	/**
	 * Is this bean a candidate for getting autowired into some other bean?
	 * <p>Default is {@code true}; set this to {@code false} for internal delegates
	 * that are not meant to get in the way of beans of the same type in other places.
	 * @since 5.1
	 */
	boolean autowireCandidate() default true;

	/**
	 * The optional name of a method to call on the bean instance during initialization.
	 * Not commonly used, given that the method may be called programmatically directly
	 * within the body of a Bean-annotated method.
	 * <p>The default value is {@code ""}, indicating no init method to be called.
	 * @see org.springframework.beans.factory.InitializingBean
	 * @see org.springframework.context.ConfigurableApplicationContext#refresh()
	 */
	String initMethod() default "";

	/**
	 * The optional name of a method to call on the bean instance upon closing the
	 * application context, for example a {@code close()} method on a JDBC
	 * {@code DataSource} implementation, or a Hibernate {@code SessionFactory} object.
	 * The method must have no arguments but may throw any exception.
	 * <p>As a convenience to the user, the container will attempt to infer a destroy
	 * method against an object returned from the {@code @Bean} method. For example, given
	 * an {@code @Bean} method returning an Apache Commons DBCP {@code BasicDataSource},
	 * the container will notice the {@code close()} method available on that object and
	 * automatically register it as the {@code destroyMethod}. This 'destroy method
	 * inference' is currently limited to detecting only public, no-arg methods named
	 * 'close' or 'shutdown'. The method may be declared at any level of the inheritance
	 * hierarchy and will be detected regardless of the return type of the {@code @Bean}
	 * method (i.e., detection occurs reflectively against the bean instance itself at
	 * creation time).
	 * <p>To disable destroy method inference for a particular {@code @Bean}, specify an
	 * empty string as the value, e.g. {@code @Bean(destroyMethod="")}. Note that the
	 * {@link org.springframework.beans.factory.DisposableBean} callback interface will
	 * nevertheless get detected and the corresponding destroy method invoked: In other
	 * words, {@code destroyMethod=""} only affects custom close/shutdown methods and
	 * {@link java.io.Closeable}/{@link java.lang.AutoCloseable} declared close methods.
	 * <p>Note: Only invoked on beans whose lifecycle is under the full control of the
	 * factory, which is always the case for singletons but not guaranteed for any
	 * other scope.
	 * @see org.springframework.beans.factory.DisposableBean
	 * @see org.springframework.context.ConfigurableApplicationContext#close()
	 */
	String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;

}

```
:::
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
:::details 点击查看源码
```java
```
:::