/**
 * Copyright [2019-2020] [wujiuye]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wujiuye.ip2location.proxy.elasticsearch;

import com.wujiuye.ip2location.context.IP2LocationDatabaseContext;
import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.entity.SystemProperty;
import com.wujiuye.ip2location.operation.elasticsearch.IP2LocationElasticsearchOperation;
import com.wujiuye.ip2location.proxy.OperationProxy;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/28 {描述：}
 */
public class IP2LocationElasticsearchOperationProxy implements IP2LocationElasticsearchOperation, OperationProxy<IP2LocationElasticsearchOperation> {

    private final static String DOC_INDEX = "ip-location-database";
    private final static String DOC_TYPE = "ip-location-table";
    private static volatile boolean INIT = true;

    private IP2LocationElasticsearchOperation elasticsearchOperation;

    public IP2LocationElasticsearchOperationProxy(IP2LocationElasticsearchOperation elasticsearchOperation) {
        this.elasticsearchOperation = elasticsearchOperation;
    }

    @Override
    public void ensureIndex(String index, String type, Map<String, String> fieldMapper) {
        Field[] fields = IP2LocationEntity.class.getDeclaredFields();
        Map<String, String> realFieldMapper = new HashMap<>();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                realFieldMapper.put(field.getName(), "text");
            } else if (field.getType() == Date.class
                    || field.getType() == LocalDateTime.class) {
                realFieldMapper.put(field.getName(), "date");
            } else if (field.getType() == Long.class) {
                realFieldMapper.put(field.getName(), "long");
            } else if (field.getType() == Integer.class) {
                realFieldMapper.put(field.getName(), "integer");
            }
        }
        this.elasticsearchOperation.ensureIndex(DOC_INDEX, DOC_TYPE, realFieldMapper);
    }

    @Override
    public int batchPut(String index, String type, List<IP2LocationEntity> records) {
        if (INIT) {
            synchronized (this) {
                if (INIT) {
                    INIT = false;
                    SystemProperty property = IP2LocationDatabaseContext.getSystemProperty();
                    if (property != null && property.isUpdate()) {
                        this.elasticsearchOperation.ensureIndex(null, null, null);
                    }
                }
            }
        }
        return elasticsearchOperation.batchPut(DOC_INDEX, DOC_TYPE, records);
    }

    @Override
    public IP2LocationEntity filterOne(String index, String type, Long ipNumer) {
        return elasticsearchOperation.filterOne(DOC_INDEX, DOC_TYPE, ipNumer);
    }

    @Override
    public IP2LocationElasticsearchOperation getProxy() {
        return this.elasticsearchOperation;
    }
}
