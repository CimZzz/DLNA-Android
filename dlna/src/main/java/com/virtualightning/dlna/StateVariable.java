package com.virtualightning.dlna;

import java.util.HashMap;
import java.util.List;

public class StateVariable {
    String name;//声明变量名
    String dataType;//变量类型
    String sendEvents;//Unknown
    String defaultValue;//缺省值
    List<String> allowedValueList = null;//变量限定可设值
    HashMap<String,String> allowedValueRange = null;//变量范围值
}
