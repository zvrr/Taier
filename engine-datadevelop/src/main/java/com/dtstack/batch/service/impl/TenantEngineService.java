/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.batch.service.impl;

import com.dtstack.batch.dao.TenantEngineDao;
import com.dtstack.batch.domain.TenantEngine;
import com.dtstack.engine.common.annotation.Forbidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目关联引擎相关
 * Date: 2019/6/3
 * Company: www.dtstack.com
 * @author xuchao
 */

@Service
@Slf4j
public class TenantEngineService {

    @Autowired
    private TenantEngineDao tenantEngineDao;


    @Forbidden
    public TenantEngine getByTenantAndEngineType(Long tenantId, Integer engineType) {
        return tenantEngineDao.getByTenantAndEngineType(tenantId, engineType);
    }

    @Forbidden
    public List<Integer> getUsedEngineTypeList(Long tenantId) {
        return tenantEngineDao.getUsedEngineTypeList(tenantId);
    }

    @Forbidden
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(TenantEngine projectEngine) {
        return tenantEngineDao.insert(projectEngine);
    }


}