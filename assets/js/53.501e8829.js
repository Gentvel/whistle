(window.webpackJsonp=window.webpackJsonp||[]).push([[53],{595:function(_,v,t){"use strict";t.r(v);var r=t(4),a=Object(r.a)({},(function(){var _=this,v=_.$createElement,t=_._self._c||v;return t("ContentSlotsDistributor",{attrs:{"slot-key":_.$parent.slotKey}},[t("h2",{attrs:{id:"一、六大设计原则"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#一、六大设计原则"}},[_._v("#")]),_._v(" 一、六大设计原则")]),_._v(" "),t("p",[t("strong",[_._v("单一职责原则")]),_._v("：一个类只负责一个功能领域的相应职责，一个函数/方法只做一件事，尽量做到高内聚、低耦合。"),t("br"),_._v(" "),t("strong",[_._v("开闭原则")]),_._v("： 对扩展开放，对修改关闭，不改变原有代码的情况下进行扩展。"),t("br"),_._v(" "),t("strong",[_._v("里氏替换原则")]),_._v("： 所有应用父类的地方必须能透明使用其子类对象。"),t("br"),_._v(" "),t("strong",[_._v("依赖倒置原则")]),_._v("：高层模块不应该依赖低层模块，两者都应该依赖其抽象；抽象不应该依赖细节，细节应该依赖抽象，其核心思想是：要面向接口编程，不要面向实现编程。"),t("br"),_._v(" "),t("strong",[_._v("接口隔离原则")]),_._v("：将臃肿庞大的接口拆分成更小的和更具体的接口，让接口中只包含客户感兴趣的方法。"),t("br"),_._v(" "),t("strong",[_._v("迪米特法则")]),_._v("：减少依赖，一个应用应该尽可能减少与其他实体发生相互作用。")]),_._v(" "),t("h2",{attrs:{id:"二、设计模式三大类型"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#二、设计模式三大类型"}},[_._v("#")]),_._v(" 二、设计模式三大类型")]),_._v(" "),t("h3",{attrs:{id:"_2-1-创建型模式"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#_2-1-创建型模式"}},[_._v("#")]),_._v(" 2.1 创建型模式")]),_._v(" "),t("p",[_._v("创建型模式的主要关注点是“怎样创建对象？”，它的主要特点是“将对象的创建与使用分离”。这样可以降低系统的耦合度，使用者不需要关注对象的创建细节，对象的创建由相关的工厂来完成。就像我们去商场购买商品时，不需要知道商品是怎么生产出来一样，因为它们由专门的厂商生产。")]),_._v(" "),t("p",[_._v("创建型模式分为以下几种。")]),_._v(" "),t("ul",[t("li",[_._v("单例（Singleton）模式：某个类只能生成一个实例，该类提供了一个全局访问点供外部获取该实例，其拓展是有限多例模式。")]),_._v(" "),t("li",[_._v("原型（Prototype）模式：将一个对象作为原型，通过对其进行复制而克隆出多个和原型类似的新实例。")]),_._v(" "),t("li",[_._v("工厂方法（FactoryMethod）模式：定义一个用于创建产品的接口，由子类决定生产什么产品。")]),_._v(" "),t("li",[_._v("抽象工厂（AbstractFactory）模式：提供一个创建产品族的接口，其每个子类可以生产一系列相关的产品。")]),_._v(" "),t("li",[_._v("建造者（Builder）模式：将一个复杂对象分解成多个相对简单的部分，然后根据不同需要分别创建它们，最后构建成该复杂对象。")])]),_._v(" "),t("h3",{attrs:{id:"_2-2-结构型模式"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#_2-2-结构型模式"}},[_._v("#")]),_._v(" 2.2 结构型模式")]),_._v(" "),t("p",[_._v("结构型模式描述如何将类或对象按某种布局组成更大的结构。它分为类结构型模式和对象结构型模式，前者采用继承机制来组织接口和类，后者釆用组合或聚合来组合对象。")]),_._v(" "),t("p",[_._v("由于组合关系或聚合关系比继承关系耦合度低，满足“合成复用原则”，所以对象结构型模式比类结构型模式具有更大的灵活性。")]),_._v(" "),t("p",[_._v("结构型模式分为以下 7 种：")]),_._v(" "),t("ul",[t("li",[_._v("代理（Proxy）模式：为某对象提供一种代理以控制对该对象的访问。即客户端通过代理间接地访问该对象，从而限制、增强或修改该对象的一些特性。")]),_._v(" "),t("li",[_._v("适配器（Adapter）模式：将一个类的接口转换成客户希望的另外一个接口，使得原本由于接口不兼容而不能一起工作的那些类能一起工作。")]),_._v(" "),t("li",[_._v("桥接（Bridge）模式：将抽象与实现分离，使它们可以独立变化。它是用组合关系代替继承关系来实现的，从而降低了抽象和实现这两个可变维度的耦合度。")]),_._v(" "),t("li",[_._v("装饰（Decorator）模式：动态地给对象增加一些职责，即增加其额外的功能。")]),_._v(" "),t("li",[_._v("外观（Facade）模式：为多个复杂的子系统提供一个一致的接口，使这些子系统更加容易被访问。")]),_._v(" "),t("li",[_._v("享元（Flyweight）模式：运用共享技术来有效地支持大量细粒度对象的复用。")]),_._v(" "),t("li",[_._v("组合（Composite）模式：将对象组合成树状层次结构，使用户对单个对象和组合对象具有一致的访问性。")])]),_._v(" "),t("h3",{attrs:{id:"_2-3-行为型模式"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#_2-3-行为型模式"}},[_._v("#")]),_._v(" 2.3 行为型模式")]),_._v(" "),t("p",[_._v("为型模式用于描述程序在运行时复杂的流程控制，即描述多个类或对象之间怎样相互协作共同完成单个对象都无法单独完成的任务，它涉及算法与对象间职责的分配。")]),_._v(" "),t("p",[_._v("行为型模式分为类行为模式和对象行为模式，前者采用继承机制来在类间分派行为，后者采用组合或聚合在对象间分配行为。由于组合关系或聚合关系比继承关系耦合度低，满足“合成复用原则”，所以对象行为模式比类行为模式具有更大的灵活性。")]),_._v(" "),t("p",[_._v("行为型模式是 GoF 设计模式中最为庞大的一类，它包含以下 11 种模式。")]),_._v(" "),t("ul",[t("li",[_._v("模板方法（Template Method）模式：定义一个操作中的算法骨架，将算法的一些步骤延迟到子类中，使得子类在可以不改变该算法结构的情况下重定义该算法的某些特定步骤。")]),_._v(" "),t("li",[_._v("策略（Strategy）模式：定义了一系列算法，并将每个算法封装起来，使它们可以相互替换，且算法的改变不会影响使用算法的客户。")]),_._v(" "),t("li",[_._v("命令（Command）模式：将一个请求封装为一个对象，使发出请求的责任和执行请求的责任分割开。")]),_._v(" "),t("li",[_._v("职责链（Chain of Responsibility）模式：把请求从链中的一个对象传到下一个对象，直到请求被响应为止。通过这种方式去除对象之间的耦合。")]),_._v(" "),t("li",[_._v("状态（State）模式：允许一个对象在其内部状态发生改变时改变其行为能力。")]),_._v(" "),t("li",[_._v("观察者（Observer）模式：多个对象间存在一对多关系，当一个对象发生改变时，把这种改变通知给其他多个对象，从而影响其他对象的行为。")]),_._v(" "),t("li",[_._v("中介者（Mediator）模式：定义一个中介对象来简化原有对象之间的交互关系，降低系统中对象间的耦合度，使原有对象之间不必相互了解。")]),_._v(" "),t("li",[_._v("迭代器（Iterator）模式：提供一种方法来顺序访问聚合对象中的一系列数据，而不暴露聚合对象的内部表示。")]),_._v(" "),t("li",[_._v("访问者（Visitor）模式：在不改变集合元素的前提下，为一个集合中的每个元素提供多种访问方式，即每个元素有多个访问者对象访问。")]),_._v(" "),t("li",[_._v("备忘录（Memento）模式：在不破坏封装性的前提下，获取并保存一个对象的内部状态，以便以后恢复它。")]),_._v(" "),t("li",[_._v("解释器（Interpreter）模式：提供如何定义语言的文法，以及对语言句子的解释方法，即解释器。")])])])}),[],!1,null,null,null);v.default=a.exports}}]);