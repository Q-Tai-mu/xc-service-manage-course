package com.xuecheng.manage_course.controller;

import com.xuecheng.api.system.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuangjing
 * @create 2020/11/10-19:39
 * @Description: sys/dictionary/get/200
 */
@RestController
@RequestMapping("/sys")
public class SysDictionaryController implements SysDictionaryControllerApi {

    @Autowired
    private SysDictionaryService dictionaryService;

    @Override
    @GetMapping("dictionary/get/{type}")
    public SysDictionary getByType(@PathVariable("type") String type) {
        return dictionaryService.getByType(type);
    }
}
