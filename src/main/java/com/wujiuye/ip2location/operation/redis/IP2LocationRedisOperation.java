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

import java.util.Map;
import java.util.Set;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：}
 */
public interface IP2LocationRedisOperation {

    /**
     * 批量写入分区字典
     *
     * @param key
     * @param scoreMap key:范围表达式（区间：1111-22222） value:成绩（取一个区间的最大值，如：22222）
     */
    void zadd(String key, Map<String, Double> scoreMap);

    /**
     * 查询指定值落到的区间
     * 假设当前存储内存：
     * score:100 range:0～100，
     * score:300 range:200～300，
     * score:500 range:450～500
     * 则：
     * (1)、假设查询ip转为numner后为340，zrangeByScore(key,340)
     * 返回的结果就是：450~500
     * (2)、假设查询ip转为numner后为200，zrangeByScore(key,200)
     * 返回的结果就是：200～300
     * 即返回第一个大于rangeValue（最小值）的记录
     *
     * @param key
     * @param rangeValue 区间的一个值
     * @return (? ~ rangeValue] | | [rangeValue ~ + ∞) limit 0,1
     */
    String zrangeByScore(String key, Double rangeValue);

    /**
     * 批量写入
     *
     * @param key
     * @param data
     */
    void hmset(String key, Map<String, String> data);

    /**
     * 获取一条记录
     *
     * @param key
     * @param field
     * @return
     */
    String hget(String key, String field);

    /**
     * 删除
     *
     * @param key
     */
    void del(String key);

    /**
     * set集合，获取所有元素
     *
     * @param key
     * @return
     */
    Set<String> smembers(String key);

    /**
     * 向集合批量添加元素
     *
     * @param key
     * @param data
     */
    Long sadd(String key, Set<String> data);

}
