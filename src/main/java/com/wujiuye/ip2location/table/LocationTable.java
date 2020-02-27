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
package com.wujiuye.ip2location.table;

import com.wujiuye.ip2location.correct.RecordCorrect;
import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseException;
import com.wujiuye.ip2location.extend.SearchExtendService;

import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：
 * ip-位置映射表
 * }
 */
public interface LocationTable {

    /**
     * 清空表
     */
    void dropTable();

    /**
     * 批量插入
     *
     * @param records 批量记录
     * @return 插入的总数
     * @throws Ip2LocationDatabaseException
     */
    int insertBatch(List<IP2LocationEntity> records) throws Ip2LocationDatabaseException;

    /**
     * 根据条件查询
     *
     * @param ipNumer ip转为整形后的数值
     * @return 匹配的结果
     */
    IP2LocationEntity selectOne(Long ipNumer) throws Ip2LocationDatabaseException;

    /**
     * 获取搜索|复杂查询需求的扩展服务
     *
     * @return
     */
    SearchExtendService getSearchExtendService();

    /**
     * 获取数据修正器
     *
     * @return
     */
    RecordCorrect getRecordCorrect();

}
