package com.domor.domain;

import lombok.Data;

/**
 * @desc: 设备节拍表
 * @auther: liyuyang
 * @date: 2019/12/20
 **/
@Data
public class DevBeat {

    private int beat;
    private long time;

    public DevBeat(int beat, long time) {
        this.beat = beat;
        this.time = time;
    }

}
