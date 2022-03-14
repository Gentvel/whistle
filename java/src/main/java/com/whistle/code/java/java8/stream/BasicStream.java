package com.whistle.code.java.java8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 中间操作主要有以下方法（此类型方法返回的都是Stream）：
 * map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
 *
 * 终止操作主要有以下方法：
 * forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、
 * count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
 *
 *
 * 对于简单操作，比如最简单的遍历，Stream串行API性能明显差于显示迭代，但并行的Stream API能够发挥多核特性。
 * 对于复杂操作，Stream串行API性能可以和手动实现的效果匹敌，在并行执行时Stream API效果远超手动实现。
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicStream {
    public static void main(String[] args) {
        Person s1 = new Person(1L, "肖战", 15, "浙江");
        Person s2 = new Person(2L, "王一博", 15, "湖北");
        Person s3 = new Person(3L, "杨紫", 17, "北京");
        Person s4 = new Person(4L, "李现", 17, "浙江");
        Person s5 = new Person(5L, "张三", 13, "北京");
        Person s6 = new Person(6L, "李五", 21, "湖北");
        ArrayList<Person> objects = new ArrayList<>();
        objects.add(s4);
        objects.add(s5);
        objects.add(s6);
        objects.add(s3);
        objects.add(s2);
        objects.add(s1);

        List<Person> people = testFilter(objects);
        //List<Person> people = testFilter(objects, person -> person.getAge() > 15);
        //List<String> people = testMap(objects);
        //List<String> people = testMap(objects,person -> "年龄："+person.getAge());
        people.forEach(System.out::println);
        testReduce();
    }



    private static List<Person> testFilter(List<Person> personList){
        return personList.parallelStream().filter(person -> "浙江".equals(person.getAddress())).sorted((Comparator.comparingLong(Person::getId))).collect(Collectors.toList());
    }

    private static List<Person> testFilter(List<Person> people, Predicate<Person> predicate){
        return people.parallelStream().filter(predicate).sorted((o1, o2) -> o1.compare(o1,o2)).collect(Collectors.toList());
    }


    private static List<String> testMap(List<Person> people){
        return people.parallelStream().map(person -> "住址："+person.getAddress()).collect(Collectors.toList());
    }

    private static List<String> testMap(List<Person> people, Function<Person,String> function){
        return people.parallelStream().map(function).sorted().distinct().collect(Collectors.toList());
    }

    /**
     * 集合reduce,将集合中每个元素聚合成一条数据
     */
    private static void testReduce() {
        List<String> list = Arrays.asList("欢","迎","你");
        String appendStr = list.stream().reduce("北京",(a,b) -> a+b);
        System.out.println(appendStr);
    }
}

