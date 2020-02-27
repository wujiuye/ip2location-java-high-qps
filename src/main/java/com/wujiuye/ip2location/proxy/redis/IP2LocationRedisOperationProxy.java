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
package com.wujiuye.ip2location.proxy.redis;

import com.wujiuye.ip2location.operation.redis.IP2LocationRedisOperation;

import com.wujiuye.ip2location.proxy.OperationProxy;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/26 {描述：
 * 代理类，目的是简化对score有序集合分区的操作
 * }
 */
@Slf4j
public class IP2LocationRedisOperationProxy implements IP2LocationRedisOperation, OperationProxy<IP2LocationRedisOperation> {

    /**
     * ip范围字典的key
     */
    private final static String rangeKey = "ip-country-city-locations-range";
    /**
     * 看文件得出的有效ip段的最小ip_to和最大ip_to
     */
    private final static long MIX = 16777216L;
    private final static long MAX = 3758096383L;
    /**
     * 设每个区间的步长
     * (由于每条记录都是一个范围，所以区间大小只能算是一个步长值，每个区间分配的记录数不会达到这个值)
     */
    private final static long RANGE_SIZE = 1000000L;
    /**
     * 被代理类
     */
    private IP2LocationRedisOperation redisOperation;

    public IP2LocationRedisOperationProxy(IP2LocationRedisOperation redisOperation) {
        this.redisOperation = redisOperation;
    }

    /**
     * 自动匹配分区
     *
     * @param key
     * @param scoreMap key:范围表达式（区间：1111-22222） value:成绩（取一个区别的最大值，如：22222）
     */
    @Override
    public void zadd(String key, Map<String, Double> scoreMap) {
        Map<String, Map<String, Double>> keyScoreMap = new HashMap<>();
        // 按区间分组
        scoreMap.entrySet().stream()
                .forEach(entry -> {
                    String range_key = computeRangeKey(entry.getValue());
                    if (!keyScoreMap.containsKey(range_key)) {
                        keyScoreMap.put(range_key, new HashMap<>());
                    }
                    Map<String, Double> map = keyScoreMap.get(range_key);
                    map.put(entry.getKey(), entry.getValue());
                });
        // 分组批量写入
        keyScoreMap.entrySet().stream()
                .forEach(entry -> redisOperation.zadd(entry.getKey(), entry.getValue()));
    }

    /**
     * 实现自动匹配分区
     *
     * @param key
     * @param rangeValue 区间的一个值
     * @return
     */
    @Override
    public String zrangeByScore(String key, Double rangeValue) {
        String range_key = computeRangeKey(rangeValue);
        String range_value = redisOperation.zrangeByScore(range_key, rangeValue);
        if (range_value == null) {
            // 可能落在下一个分区了，因为范围可能刚好跨两个分区的分界
            range_value = redisOperation.zrangeByScore(computeRangeKey(rangeValue + RANGE_SIZE), rangeValue);
        }
        return range_value;
    }

    @Override
    public void hmset(String key, Map<String, String> data) {
        redisOperation.hmset(key, data);
    }

    @Override
    public String hget(String key, String field) {
        return redisOperation.hget(key, field);
    }

    /**
     * 自动删除所有分区的key
     *
     * @param key
     */
    @Override
    public void del(String key) {
        redisOperation.del(key);
        int rengeCnt = (int) ((MAX + RANGE_SIZE - MIX) / RANGE_SIZE + 1);
        // 删除所有的分区字典
        for (int range = 0; range <= rengeCnt; range++) {
            redisOperation.del(rangeKey + "-" + range);
        }
        log.info("====> total del range keys {}", rengeCnt);
    }

    @Override
    public Set<String> smembers(String key) {
        return redisOperation.smembers(key);
    }

    @Override
    public Long sadd(String key, Set<String> data) {
        return redisOperation.sadd(key, data);
    }

    /**
     * 计算value所在的ip字典分区
     *
     * @param value ip转number的值
     * @return 所在的分区的key
     */
    private String computeRangeKey(double value) {
        int index = (int) ((BigDecimal.valueOf(value).longValue() - MIX) / RANGE_SIZE);
        return rangeKey + "-" + index;
    }

    @Override
    public IP2LocationRedisOperation getProxy() {
        return redisOperation;
    }

}
