package com.fish;

import java.lang.ref.PhantomReference;

/**
 * Created by zhouxiaodong on 2017/8/9.
 * 文件名称model
 */
public class SourceNameModel {
    private String originName;//源文件名
    private String newName;//自定义文件名

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
