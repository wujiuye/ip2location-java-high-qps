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
package com.wujiuye.ip2location.table.memory;

import com.wujiuye.ip2location.correct.RecordCorrect;
import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseException;
import com.wujiuye.ip2location.extend.SearchExtendService;
import com.wujiuye.ip2location.table.LocationTable;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/12/3 {描述：}
 */
@Slf4j
public class MemoryLocationTable implements LocationTable {


    public MemoryLocationTable() {
    }

    @Override
    public void dropTable() {

    }

    @Override
    public synchronized int insertBatch(List<IP2LocationEntity> records) throws Ip2LocationDatabaseException {
        for (IP2LocationEntity record : records) {
        }
        return records.size();
    }

    @Override
    public IP2LocationEntity selectOne(Long ipNumer) throws Ip2LocationDatabaseException {
        System.out.println(ipNumer);
        return null;
    }

    @Override
    public SearchExtendService getSearchExtendService() {
        return null;
    }

    @Override
    public RecordCorrect getRecordCorrect() {
        return null;
    }

}
