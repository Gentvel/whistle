package com.whistle.code.simulate.bean;

import lombok.Builder;

import java.io.Serializable;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Lin
 * @version 1.0.0
 */
@Builder
public class Reply implements Serializable {
    private long id;
    private long posterId;
    private long personId;
    private String message;
    private String datetime;
    private int agreed;
    private int disagreed;

    @Override
    public String toString() {
        return String.format("##%d|%d|%s|%s|%d|%d\r\n",this.posterId,this.personId,this.message,this.datetime,this. agreed,this.disagreed);
    }
}
