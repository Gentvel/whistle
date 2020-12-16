package com.whistle.code.java.java8.lambda;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicSyntax {
    public static void main(String[] args) {
        //无参，无返回值
        NoneReturnNoneParameter noneReturnNoneParameter = ()->{
            System.out.println("None Return None Parameter!");
        };
        noneReturnNoneParameter.test();
        //有参，无返回值
        NoneReturnSingleParameter<String> noneReturnSingleParameter= test->{
            System.out.println("None Return Single Parameter!" + test);
        };
        noneReturnSingleParameter.test("你好啊");
        //有参，无返回值
        NoneReturnMultipleParameter<String,String> noneReturnMultipleParameter =(str,str1)->{
            System.out.println("None Return Two Parameter!:"+ str+ " "+str1);
        };
        noneReturnMultipleParameter.test("你好","今天");
        //无参，有返回值
        SingleReturnNoneParameter singleReturnNoneParameter =()->{
            System.out.println("Single Return none Parameter");
            return true;
        };
        boolean test = singleReturnNoneParameter.test();
        System.out.println(test);
        //有参，有返回值
        SingleReturnSingleParameter<String> stringSingleReturnSingleParameter=str->{
            System.out.println("Single Return Single Parameter!"+ str);
            return false;
        };
        boolean test1 = stringSingleReturnSingleParameter.test("你好");
        System.out.println(test1);
        //有参，有返回值
        SingleReturnMultipleParameter<String,String> singleReturnMultipleParameter=(str,str1)->{
            System.out.println("Single Return Multiple parameter!"+str+str1);
            return false;
        };
        boolean test2 = singleReturnMultipleParameter.test("你好", "引用");
        System.out.println(test2);
    }
}
