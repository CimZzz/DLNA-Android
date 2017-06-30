package com.virtualightning.dlna;

import java.util.LinkedList;
import java.util.List;

public class Action {
    String name;//动作名

    List<Argument> inArgumentList = new LinkedList<>();//输入参数列表
    List<Argument> outArgumentList = new LinkedList<>();//输出参数列表
}
