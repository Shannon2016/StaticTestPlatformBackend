package com.gbdpcloud.service.impl;

import com.gbdpcloud.service.UacRoleService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UacRoleServiceImpl extends BaseService<UacRole> implements UacRoleService {
}
