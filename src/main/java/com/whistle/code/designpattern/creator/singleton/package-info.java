/**
 *
 *  单例设计模式，总共有5种写法：<br/>
 *  <ul>
 *      <li>懒汉式 {@link com.whistle.code.designpattern.creator.singleton.Singleton#getLazyInstance()}</li>
 *      <li>饿汉式 {@link com.whistle.code.designpattern.creator.singleton.Singleton#getHungryInstance()}</li>
 *      <li>双重锁 {@link com.whistle.code.designpattern.creator.singleton.Singleton#getDoubleCheckInstance()}</li>
 *      <li>静态内部类 {@link com.whistle.code.designpattern.creator.singleton.Singleton#getInnerInstance()}</li>
 *      <li>枚举 {@link com.whistle.code.designpattern.creator.singleton.Singleton}</li>
 *  </ul>
 * @auther: Gentvel
 * @since 1.0
 * @see: com.whistle.code.designpattern.creator.singleton.package-info
 */
package com.whistle.code.designpattern.creator.singleton;