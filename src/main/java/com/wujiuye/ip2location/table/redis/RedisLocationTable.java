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
package com.wujiuye.ip2location.table.redis;

import com.alibaba.fastjson.JSON;
import com.wujiuye.ip2location.correct.RecordCorrect;
import com.wujiuye.ip2location.correct.redis.RedisIPLocationRecordCorrect;
import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseException;
import com.wujiuye.ip2location.extend.ExtendService;
import com.wujiuye.ip2location.extend.SearchExtendService;
import com.wujiuye.ip2location.extend.redis.CountryMapExtendService;
import com.wujiuye.ip2location.operation.redis.IP2LocationRedisOperation;
import com.wujiuye.ip2location.proxy.redis.IP2LocationRedisOperationProxy;
import com.wujiuye.ip2location.table.LocationTable;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：
 * 使用redis缓存的ip-地理位置映射表
 * }
 */
@Slf4j
public class RedisLocationTable implements LocationTable {

    private IP2LocationRedisOperation redisOperation;
    private String key;
    private List<ExtendService> extendServiceList;

    /**
     * 构建器
     *
     * @param key            ip-国家信息映射表的key
     * @param redisOperation redis操作实现类
     */
    public RedisLocationTable(String key, IP2LocationRedisOperation redisOperation) {
        this.redisOperation = redisOperation;
        this.key = key;
        if (redisOperation == null) {
            throw new NullPointerException("IP2LocationRedisOperation is null !");
        }
        // 扩展处理
        this.extendServiceList = new ArrayList<>();
        this.extendServiceList.add(new CountryMapExtendService(redisOperation));
    }

    @Override
    public void dropTable() {
        // 清理扩展
        if (extendServiceList != null && extendServiceList.size() > 0) {
            for (ExtendService extendService : extendServiceList) {
                extendService.preClear();
            }
        }
        // 清理数据
        redisOperation.del(key);
    }

    /**
     * 批量写入
     *
     * @param records 批量记录
     * @return
     * @throws Ip2LocationDatabaseException
     */
    @Override
    public int insertBatch(List<IP2LocationEntity> records) throws Ip2LocationDatabaseException {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        // 扩展
        if (extendServiceList != null && extendServiceList.size() > 0) {
            for (ExtendService extendService : extendServiceList) {
                extendService.preInster(records);
            }
        }
        // 插入
        Map<String, String> data = new HashMap<>();
        Map<String, Double> scoreData = new HashMap<>();
        for (int retry = 1; retry <= 3; ) {
            try {
                for (IP2LocationEntity record : records) {
                    String fieldKey = String.valueOf(record.getIpFrom());
                    if (fieldKey == null) {
                        continue;
                    }
                    String value = JSON.toJSONString(record);
                    data.put(fieldKey, value);
                    // 拼接区间
                    String range = record.getIpFrom() + "~" + record.getIpTo();
                    // 取最大为score
                    double score = record.getIpTo().doubleValue();
                    scoreData.put(range, score);
                }
                redisOperation.hmset(key, data);
                redisOperation.zadd(null, scoreData);
                return data.size();
            } catch (Exception e) {
                if (retry == 3) {
                    Ip2LocationDatabaseException exception = new Ip2LocationDatabaseException(e.getLocalizedMessage());
                    exception.setStackTrace(e.getStackTrace());
                    throw exception;
                }
                retry++;
                log.info("error:{}, start retry {}", e.getLocalizedMessage(), retry - 1);
            }
        }
        throw new Ip2LocationDatabaseException("retry out 3. insert error. read time out.");
    }

    /**
     * 根据ip的number查询
     *
     * @param midNumber ip的number值
     * @return
     * @throws Ip2LocationDatabaseException
     */
    @Override
    public IP2LocationEntity selectOne(Long midNumber) throws Ip2LocationDatabaseException {
        // 先拿到midNumber所在的区间
        String range = redisOperation.zrangeByScore(null, midNumber.doubleValue());
        if (range == null) {
            return null;
        }
        String[] rangeSn = range.split("~");
        if (rangeSn.length != 2) {
            return null;
        }
        // 验证是否在范围内，要验证的原因请看：IP2LocationRedisOperation#zrangeByScore方法的注释
        // [0] <= midNumber <= [1]
        if (midNumber.compareTo(Long.valueOf(rangeSn[0])) < 0
                || midNumber.compareTo(Long.valueOf(rangeSn[1])) > 0) {
            return null;
        }
        // 再根据区间拿到记录的key
        String recordKey = rangeSn[0];
        // 根据key获取记录
        return selectByKey(recordKey);
    }

    @Override
    public SearchExtendService getSearchExtendService() {
        if (extendServiceList != null && extendServiceList.size() > 0) {
            for (ExtendService extendService : extendServiceList) {
                if (extendService instanceof SearchExtendService) {
                    return (SearchExtendService) extendService;
                }
            }
        }
        return null;
    }

    /**
     * 获取数据修正器
     *
     * @return
     */
    @Override
    public RecordCorrect getRecordCorrect() {
        if (this.redisOperation instanceof IP2LocationRedisOperationProxy) {
            return new RedisIPLocationRecordCorrect(((IP2LocationRedisOperationProxy) this.redisOperation).getProxy());
        }
        return new RedisIPLocationRecordCorrect(this.redisOperation);
    }

    /**
     * 根据从字典中拿到的ip地址映射的key去redis中查找记录
     *
     * @param recordKey ip地址映射记录的key
     * @return
     * @throws Ip2LocationDatabaseException
     */
    private IP2LocationEntity selectByKey(String recordKey) throws Ip2LocationDatabaseException {
        String value = redisOperation.hget(key, String.valueOf(recordKey));
        if (value != null) {
            try {
                return JSON.parseObject(value, IP2LocationEntity.class);
            } catch (Exception e) {
                throw new Ip2LocationDatabaseException(e.getLocalizedMessage());
            }
        }
        return null;
    }

}
