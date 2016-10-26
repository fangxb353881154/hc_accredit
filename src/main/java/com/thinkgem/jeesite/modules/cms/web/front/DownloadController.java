package com.thinkgem.jeesite.modules.cms.web.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2016/9/22.
 */
@Controller
@RequestMapping(value = "${frontPath}/download/")
public class DownloadController {

    @RequestMapping("index")
    public String downloadApp(){

        return "download/index";
    }
}
