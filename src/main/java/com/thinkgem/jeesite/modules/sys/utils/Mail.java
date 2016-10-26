package com.thinkgem.jeesite.modules.sys.utils;

import com.thinkgem.jeesite.common.utils.IdGen;

/**
 * Created by Administrator on 2016/9/7.
 */
public class Mail {
    public static void main(String[] args) {
        String keyCode = "MlDS"+ IdGen.uuid().substring(3);
        System.out.println(keyCode);
    }
}
