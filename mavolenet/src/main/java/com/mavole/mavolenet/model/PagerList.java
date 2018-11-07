package com.mavole.mavolenet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by 宋棋安, Email sgngqian@sina.com
 * Date on 2018/11/7.
 */
public class PagerList <T> {
    public List<T> list = new ArrayList<>();
    public PageInfo pageInfo = new PageInfo();
}
