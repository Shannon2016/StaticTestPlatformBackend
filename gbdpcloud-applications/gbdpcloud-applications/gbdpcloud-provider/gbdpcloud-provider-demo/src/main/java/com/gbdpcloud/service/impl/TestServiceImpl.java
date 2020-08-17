package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.Test;
import com.gbdpcloud.mapper.TestMapper;
import com.gbdpcloud.service.TestService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Test service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class TestServiceImpl extends BaseService<Test> implements TestService {

    @Resource
    private TestMapper testMapper;

    @Override
    public List<Test> getByProject(String id) {
       List<Test> list = testMapper.getByProject(id);
       return list;
    }
}
