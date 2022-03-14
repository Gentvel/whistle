package com.whistle.code.simulate;

import com.whistle.code.simulate.bean.Person;
import com.whistle.code.simulate.bean.Poster;
import com.whistle.code.simulate.bean.Reply;
import com.whistle.code.simulate.utils.Mock;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lin
 * @version 1.0.0
 */
public class DataBuilders {


    public static Person getPerson(){
        return Person.builder().age((int) Mock.natural(10, 100))
                .birthDate(Mock.date())
                .name(Mock.cname())
                .address(Mock.county(true))
                .email(Mock.email())
                .phone(String.valueOf(Mock.natural()))
                .idCard(Mock.id())
                .photoUrl(Mock.image()).build();
    }

    /**
     * 生成人物数据
     * @param numbers 数量，默认为空时，为10000个
     */
    public static List<Person> getPersons(@Nullable Integer numbers){
        List<Person> persons;
        int size;
        if(numbers!=null){
            persons = new ArrayList<>(numbers);
            size=numbers;
        }else {
            persons = new ArrayList<>(10000);
            size=10000;
        }
        for (int i = 0; i < size; i++) {
            persons.add(Person.builder().age((int) Mock.natural(10, 100))
                    .birthDate(Mock.date())
                    .name(Mock.cname())
                    .address(Mock.county(true))
                    .email(Mock.email())
                    .phone(String.valueOf(Mock.natural()))
                    .idCard(Mock.id())
                    .photoUrl(Mock.image()).build());

        }
        return persons;
    }

    /**
     * 生成帖子
     * @param personIdLimit limit
     */
    public static Poster getPoster(Integer personIdLimit){
        return Poster.builder().personId(Mock.natural(personIdLimit))
                .title(Mock.ctitle())
                .type(Mock.character())
                .content(Mock.cparagraph())
                .datetime(Mock.datetime()).build();
    }

    /**
     * 生成帖子数据
     * @param numbers 数量
     * @param personIdLimit id限制，根据生成的人物数设置随机id
     */
    public static List<Poster> getPosters(@Nullable Integer numbers,Integer personIdLimit) {
        int size;
        List<Poster> posters;
        if(numbers!=null){
            posters = new ArrayList<>(numbers);
            size=numbers;
        }else{
            posters = new ArrayList<>(10000);
            size=10000;
        }

        for (int i = 0; i < size; i++) {
            posters.add(Poster.builder().personId(Mock.natural(personIdLimit))
                    .title(Mock.ctitle())
                    .type(Mock.character())
                    .content(Mock.cparagraph())
                    .datetime(Mock.datetime()).build());

        }
        return posters;
    }

    /**
     * 获取回复数据
     * @param personIdLimit limit
     * @param posterIdLimit limit
     */
    public static Reply getReply(Integer personIdLimit,Integer posterIdLimit){
        return Reply.builder().personId(Mock.natural(personIdLimit))
                .posterId(Mock.natural(posterIdLimit))
                .datetime(Mock.datetime())
                .message(Mock.ctitle())
                .build();
    }

    /**
     * 生成回复数据
     * @param numbers 条数
     * @param personIdLimit 人物ID数
     * @param posterIdLimit 帖子ID数
     */
    public static List<Reply> getReplies(@Nullable Integer numbers,Integer personIdLimit,Integer posterIdLimit){
        int size;
        List<Reply> replies;
        if(numbers!=null){
            replies = new ArrayList<>(numbers);
            size=numbers;
        }else{
            replies = new ArrayList<>(10000);
            size=10000;
        }
        for (int i = 0; i < size; i++) {
            replies.add(Reply.builder().personId(Mock.natural(personIdLimit))
                    .posterId(Mock.natural(posterIdLimit))
                    .datetime(Mock.datetime())
                    .message(Mock.ctitle())
                    .build());

        }
        return replies;
    }
}
