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
package com.wujiuye.ip2location.operation.redis;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：
 * 提供本地debug的IP2LocationRedisOperation实现类
 * }
 */
@Slf4j
public class DebugRedisOperation implements IP2LocationRedisOperation {

    @Override
    public void zadd(String key, Map<String, Double> scoreMap) {
        scoreMap.entrySet().forEach(entry -> log.info("range:{}, score:{}", entry.getKey(), entry.getValue()));
    }

    @Override
    public String zrangeByScore(String key, Double rangeValue) {
        return null;
    }

    @Override
    public void hmset(String key, Map<String, String> data) {
        data.entrySet().forEach(entry -> log.info("field:{}, value:{}", entry.getKey(), entry.getValue()));
    }

    @Override
    public String hget(String key, String field) {
        return null;
    }

    @Override
    public void del(String key) {
    }

    @Override
    public Set<String> smembers(String key) {
        return null;
    }

    @Override
    public Long sadd(String key, Set<String> data) {
        return null;
    }

}
