package com.whistle.code.simulate.bean;

import lombok.Builder;

import java.io.Serializable;

/**
 * @author Lin
 * @version 1.0.0
 */
@Builder
public class Poster implements Serializable {
    private long id;
    private long personId;
    private String title;
    private char type;
    private String content;
    private String datetime;

    @Override
    public String toString() {
        return String.format("##%d|%s|%c|%s|%s",this.personId,this.title,this.type,this.content,this.datetime);
    }
}
