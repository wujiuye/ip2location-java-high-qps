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

import java.util.List;
import java.util.Map;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/28 {描述：}
 */
public interface IP2LocationElasticsearchOperation {

    /**
     * 确保索引存在
     *
     * @param index
     * @param type
     * @param fieldMapper
     */
    void ensureIndex(String index, String type, Map<String, String> fieldMapper);

    /**
     * 批量数据写入
     *
     * @param index   索引
     * @param type    类型
     * @param records 记录总数
     * @return
     */
    int batchPut(String index, String type, List<IP2LocationEntity> records);

    /**
     * 查询ip_from 大于等于ipNumer 且 ip_to小于等于ipNumer的记录
     * 如果匹配多条，只返回匹配分值最高的一条
     *
     * @param index   索引
     * @param type    类型
     * @param ipNumer ip的数值
     * @return
     */
    IP2LocationEntity filterOne(String index, String type, Long ipNumer);

}
