package com.whistle.code.java.java8.functionquote;

/**
 * 方法名不用对应，只要返回值、参数个数与类型相同即可。
 *
 * 对象方法的特殊引用：注意接口方法中参数必须要有对象类型的参数，该对象中返回值和接口方法返回值类型相同
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicSyntax {
    public static void main(String[] args) {
        //静态方法引用
        Calculator calculator =(x,y)->BasicStaticMethodQuote.calculate(x,y);

        System.out.println(calculator.calculate(5,6));
        //静态方法引用-》函数式引用
        Calculator calculator1 =BasicStaticMethodQuote::calculate;
        System.out.println( calculator1.calculate(10,9));

        //非静态方法引用（使用对象::方法）
        Calculator calculator2 =(x,y)->new BasicNoneStaticMethodQuote().call(x,y);
        System.out.println(calculator2.calculate(19,3));

        //非静态方法引用-》函数式引用
        Calculator calculator3 = new BasicNoneStaticMethodQuote()::call;
        System.out.println(calculator3.calculate(3,8));

        //构造函数引用---无参
        NoneParameterConstructor noneParameterConstructor = ()->new Person();
        System.out.println(noneParameterConstructor.getPerson());
        //构造函数引用-》函数式引用
        NoneParameterConstructor noneParameterConstructor1 = Person::new;
        System.out.println(noneParameterConstructor1.getPerson());
        //构造函数引用---有参
        SingleParameterConstructor singleParameterConstructor=(name)->new Person(name);
        System.out.println(singleParameterConstructor.getPerson("张三"));

        SingleParameterConstructor singleParameterConstructor1=Person::new;
        System.out.println(singleParameterConstructor1.getPerson("李四"));

        MultipleParameterConstructor multipleParameterConstructor=Person::new;
        System.out.println(multipleParameterConstructor.getPerson("王五",16));


        Person person = new Person("黄六",14);
        //特殊引用
        ObjectMethodSpecialQuote objectMethodSpecialQuote = person1 -> person1.getName();
        System.out.println("特殊引用："+objectMethodSpecialQuote.get(person));
        //特殊引用---函数式引用
        ObjectMethodSpecialQuote objectMethodSpecialQuote1 = Person::getName;
        System.out.println("特殊引用："+objectMethodSpecialQuote1.get(person));
        //相同返回值的无参方法都能调用
        ObjectMethodSpecialQuote objectMethodSpecialQuote2 = Person::toString;
        System.out.println("特殊引用："+objectMethodSpecialQuote2.get(person));

        ObjectMethodWithParameter objectMethodWithParameter = Person::getMeddle;
        System.out.println("特殊引用："+objectMethodWithParameter.get(person,"hello"));
    }

    /*构造函数引用接口*/
    private interface NoneParameterConstructor{
        Person getPerson();
    }
    private interface SingleParameterConstructor{
        Person getPerson(String name);
    }
    private interface MultipleParameterConstructor{
        Person getPerson(String name,int age);
    }
    /*对象方法的特殊引用*/
    private interface ObjectMethodSpecialQuote{
        String get(Person person);
    }
    private interface ObjectMethodWithParameter{
        String get(Person person,String meddle);
    }
}
