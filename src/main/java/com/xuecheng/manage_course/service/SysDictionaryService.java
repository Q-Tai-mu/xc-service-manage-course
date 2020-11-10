package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.SysDictionaryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author wuangjing
 * @create 2020/11/10-19:38
 * @Description:
 */
@Service
public class SysDictionaryService {

    @Autowired
    private RestTemplate restTemplate;

    public SysDictionary getByType(String type) {
        String url = "http://localhost:31001/sysTeam/kia/" + type;
        ResponseEntity<SysDictionary> entity = restTemplate.getForEntity(url, SysDictionary.class);
        SysDictionary body = entity.getBody();
        if (body == null) {
            ExceptionCast.cast(SysDictionaryCode.SysDiction_Po);
        }
        return body;
    }
}
