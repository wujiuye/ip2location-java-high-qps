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
package com.wujiuye.ip2location.table.elasticsearch;

import com.wujiuye.ip2location.correct.RecordCorrect;
import com.wujiuye.ip2location.correct.elasticsearch.ElasticsearchIPLocationRecordCorrect;
import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseException;
import com.wujiuye.ip2location.extend.SearchExtendService;
import com.wujiuye.ip2location.extend.elasticsearch.ElasticsearchSearchExtendService;
import com.wujiuye.ip2location.operation.elasticsearch.IP2LocationElasticsearchOperation;
import com.wujiuye.ip2location.proxy.elasticsearch.IP2LocationElasticsearchOperationProxy;
import com.wujiuye.ip2location.table.LocationTable;

import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/28 {描述：
 * <p>
 * }
 */
public class ElasticsearchLocationTable implements LocationTable {

    private IP2LocationElasticsearchOperation elasticsearchOperation;
    private ElasticsearchSearchExtendService searchExtendService;

    public ElasticsearchLocationTable(IP2LocationElasticsearchOperation elasticsearchOperation) {
        this.elasticsearchOperation = elasticsearchOperation;
        this.searchExtendService = new ElasticsearchSearchExtendService(elasticsearchOperation);
    }

    @Override
    public void dropTable() {
        throw new IllegalAccessError("not suppor drop table by es!");
    }

    /**
     * 在需要更新ip库的时候，完成数据的批量写入
     *
     * @param records 批量记录
     * @return
     * @throws Ip2LocationDatabaseException
     */
    @Override
    public int insertBatch(List<IP2LocationEntity> records) throws Ip2LocationDatabaseException {
        return elasticsearchOperation.batchPut(null, null, records);
    }

    /**
     * 完成查询路基，根据ip转为number后的值查询出对应记录
     *
     * @param ipNumer ip转为整形后的数值
     * @return
     * @throws Ip2LocationDatabaseException
     */
    @Override
    public IP2LocationEntity selectOne(Long ipNumer) throws Ip2LocationDatabaseException {
        return elasticsearchOperation.filterOne(null, null, ipNumer);
    }

    @Override
    public SearchExtendService getSearchExtendService() {
        return this.searchExtendService;
    }

    /**
     * 获取数据修正器
     *
     * @return
     */
    @Override
    public RecordCorrect getRecordCorrect() {
        if (elasticsearchOperation instanceof IP2LocationElasticsearchOperationProxy) {
            return new ElasticsearchIPLocationRecordCorrect(((IP2LocationElasticsearchOperationProxy) elasticsearchOperation).getProxy());
        }
        return new ElasticsearchIPLocationRecordCorrect(elasticsearchOperation);
    }

}
