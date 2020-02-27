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
package com.wujiuye.ip2location.correct.redis;

import com.alibaba.fastjson.JSON;
import com.wujiuye.ip2location.correct.RecordCorrect;
import com.wujiuye.ip2location.entity.IPLocationEntity;
import com.wujiuye.ip2location.operation.redis.IP2LocationRedisOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujiuye
 * @version 1.0 on 2019/11/2 {描述：}
 */
public class RedisIPLocationRecordCorrect implements RecordCorrect {

    private final static String KEY = "correct-ip-database-map";
    private IP2LocationRedisOperation redisOperation;

    public RedisIPLocationRecordCorrect(IP2LocationRedisOperation redisOperation) {
        this.redisOperation = redisOperation;
    }

    @Override
    public void correct(IPLocationEntity record) {
        Map<String, String> oneRecord = new HashMap<>();
        oneRecord.put(record.getIp(), JSON.toJSONString(record));
        redisOperation.hmset(KEY, oneRecord);
    }

    @Override
    public IPLocationEntity queryBy(String ipv4Address) {
        String value = redisOperation.hget(KEY, ipv4Address);
        if (value == null || value.length() == 0) {
            return null;
        }
        return JSON.parseObject(value, IPLocationEntity.class);
    }
}
