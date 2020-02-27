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
package com.wujiuye.ip2location.operation.elasticsearch;

import com.wujiuye.ip2location.entity.IP2LocationEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/28 {描述：}
 */
@Slf4j
public class DebugElasticsearchOperation implements IP2LocationElasticsearchOperation {

    @Override
    public void ensureIndex(String index, String type, Map<String, String> fieldMapper) {

    }

    @Override
    public int batchPut(String index, String type, List<IP2LocationEntity> records) {
        records.forEach(entry -> {
            log.info("index:{}, type:{}, record:{}", index, type, entry);
            System.out.println("index=" + index + ", type=" + type + ", " + entry);
        });
        return records.size();
    }

    @Override
    public IP2LocationEntity filterOne(String index, String type, Long ipNumer) {
        return null;
    }

}
