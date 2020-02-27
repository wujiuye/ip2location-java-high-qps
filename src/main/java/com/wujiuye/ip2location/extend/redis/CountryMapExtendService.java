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
package com.wujiuye.ip2location.extend.redis;

import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.extend.ExtendService;
import com.wujiuye.ip2location.extend.SearchExtendService;
import com.wujiuye.ip2location.operation.redis.IP2LocationRedisOperation;
import com.wujiuye.ip2location.proxy.redis.IP2LocationRedisOperationProxy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/31 {描述：
 * 存储国家、城市映射
 * 存储所有运营商
 * }
 */
public class CountryMapExtendService implements ExtendService, SearchExtendService {

    private IP2LocationRedisOperation operation;
    private final static String COUNTORY_SET_KEY = "ip-database-countorys";
    private final static String COUNTORY_CITYS_MAP_KEY = "ip-database-countory-citys-{countory}";
    private final static String COUNTORY_CARRIERS_MAP_KEY = "ip-database-countory-carriers-{countory}";

    public CountryMapExtendService(IP2LocationRedisOperation operation) {
        if (operation instanceof IP2LocationRedisOperationProxy) {
            this.operation = ((IP2LocationRedisOperationProxy) operation).getProxy();
        } else {
            this.operation = operation;
        }
    }

    @Override
    public void preInster(List<IP2LocationEntity> records) {
        if (records == null || records.size() == 0) {
            return;
        }
        Map<String, List<IP2LocationEntity>> maps = records.parallelStream()
                .collect(Collectors.groupingBy(IP2LocationEntity::getCountryCode));
        if (maps == null || maps.isEmpty()) {
            return;
        }
        Map<String, Set<String>> countryCityMap = new HashMap<>();
        Map<String, Set<String>> countoryCarrierMap = new HashMap<>();
        Iterator<Map.Entry<String, List<IP2LocationEntity>>> iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<IP2LocationEntity>> entry = iterator.next();
            List<IP2LocationEntity> entities = entry.getValue();
            if (!countryCityMap.containsKey(entry.getKey())) {
                countryCityMap.put(entry.getKey(), new HashSet<>());
            }
            if (!countoryCarrierMap.containsKey(entry.getKey())) {
                countoryCarrierMap.put(entry.getKey(), new HashSet<>());
            }
            Set<String> citys = countryCityMap.get(entry.getKey());
            Set<String> carriers = countoryCarrierMap.get(entry.getKey());
            citys.addAll(entities.parallelStream().map(IP2LocationEntity::getCity).collect(Collectors.toSet()));
            carriers.addAll(entities.parallelStream().map(IP2LocationEntity::getCarrier).collect(Collectors.toSet()));
            operation.sadd(COUNTORY_CITYS_MAP_KEY.replace("{countory}", entry.getKey()), citys);
            operation.sadd(COUNTORY_CARRIERS_MAP_KEY.replace("{countory}", entry.getKey()), carriers);
        }
        operation.sadd(COUNTORY_SET_KEY, maps.keySet());
    }

    @Override
    public void preClear() {
        Set<String> allCountorys = operation.smembers(COUNTORY_SET_KEY);
        if (allCountorys == null || allCountorys.isEmpty()) {
            return;
        }
        allCountorys.stream().forEach(countory -> {
            operation.del(COUNTORY_CITYS_MAP_KEY.replace("{countory}", countory));
            operation.del(COUNTORY_CARRIERS_MAP_KEY.replace("{countory}", countory));
        });
    }

    @Override
    public Set<String> selectAllCountorys() {
        return operation.smembers(COUNTORY_SET_KEY);
    }

    @Override
    public Set<String> selectAllCitys(String countoryCode) {
        return operation.smembers(COUNTORY_CITYS_MAP_KEY.replace("{countory}", countoryCode));
    }

    @Override
    public Map<String, Set<String>> selectAllCarriers() {
        Set<String> allCountorys = operation.smembers(COUNTORY_SET_KEY);
        return selectAllCarriers(allCountorys);
    }

    @Override
    public Map<String, Set<String>> selectAllCarriers(Set<String> countoryCodes) {
        Map<String, Set<String>> map = new HashMap<>();
        if (countoryCodes == null || countoryCodes.isEmpty()) {
            return map;
        }
        countoryCodes.forEach(countory -> {
            map.put(countory, operation.smembers(COUNTORY_CARRIERS_MAP_KEY.replace("{countory}", countory)));
        });
        return map;
    }

}
