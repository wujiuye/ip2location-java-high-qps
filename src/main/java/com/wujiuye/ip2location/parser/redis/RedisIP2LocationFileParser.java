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
package com.wujiuye.ip2location.parser.redis;

import com.wujiuye.ip2location.context.IP2LocationDatabaseContext;
import com.wujiuye.ip2location.entity.SystemProperty;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseParserException;
import com.wujiuye.ip2location.parser.CsvIP2LocationFileParser;
import com.wujiuye.ip2location.proxy.redis.IP2LocationRedisOperationProxy;
import com.wujiuye.ip2location.operation.redis.IP2LocationRedisOperation;
import com.wujiuye.ip2location.table.LocationTable;
import com.wujiuye.ip2location.table.redis.RedisLocationTable;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：}
 */
@Slf4j
public class RedisIP2LocationFileParser extends CsvIP2LocationFileParser {

    private final static String key = "ip-country-city-locations";
    private LocationTable table;

    protected synchronized LocationTable newTable() throws Ip2LocationDatabaseParserException {
        if (table == null) {
            ServiceLoader<IP2LocationRedisOperation> serviceLoader = ServiceLoader.load(IP2LocationRedisOperation.class);
            Iterator<IP2LocationRedisOperation> iterator = serviceLoader.iterator();
            IP2LocationRedisOperation redisOperation;
            if (iterator.hasNext()) {
                redisOperation = iterator.next();
            } else {
                throw new Ip2LocationDatabaseParserException("not found IP2LocationRedisOperation !");
            }
            // 创建代理
            IP2LocationRedisOperationProxy redisOperationProxy = new IP2LocationRedisOperationProxy(redisOperation);
            table = new RedisLocationTable(key, redisOperationProxy);
            SystemProperty property = IP2LocationDatabaseContext.getSystemProperty();
            if (property.isUpdate()) {
                // 清除旧数据
                table.dropTable();
                return table;
            }
        }
        return table;
    }

}
