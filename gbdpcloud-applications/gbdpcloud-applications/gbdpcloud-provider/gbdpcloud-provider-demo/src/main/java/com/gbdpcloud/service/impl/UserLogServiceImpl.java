package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.UserLog;
import com.gbdpcloud.service.UserLogService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserLog service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class UserLogServiceImpl extends BaseService<UserLog> implements UserLogService {


}
