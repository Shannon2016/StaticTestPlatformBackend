package com.gbdpcloud.service.impl;

import com.gbdpcloud.service.UacRoleUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRoleUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UacRoleUserServiceImpl extends BaseService<UacRoleUser> implements UacRoleUserService {
}
