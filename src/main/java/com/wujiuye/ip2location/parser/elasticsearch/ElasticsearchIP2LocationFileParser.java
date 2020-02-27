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
package com.wujiuye.ip2location.parser.elasticsearch;

import com.wujiuye.ip2location.exception.Ip2LocationDatabaseParserException;
import com.wujiuye.ip2location.parser.CsvIP2LocationFileParser;
import com.wujiuye.ip2location.proxy.elasticsearch.IP2LocationElasticsearchOperationProxy;
import com.wujiuye.ip2location.operation.elasticsearch.IP2LocationElasticsearchOperation;
import com.wujiuye.ip2location.table.LocationTable;
import com.wujiuye.ip2location.table.elasticsearch.ElasticsearchLocationTable;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/28 {描述：}
 */
public class ElasticsearchIP2LocationFileParser extends CsvIP2LocationFileParser {

    @Override
    protected LocationTable newTable() throws Ip2LocationDatabaseParserException {
        ServiceLoader<IP2LocationElasticsearchOperation> serviceLoader = ServiceLoader.load(IP2LocationElasticsearchOperation.class);
        Iterator<IP2LocationElasticsearchOperation> iterator = serviceLoader.iterator();
        IP2LocationElasticsearchOperation elasticsearchOperation;
        if (iterator.hasNext()) {
            elasticsearchOperation = iterator.next();
        } else {
            throw new Ip2LocationDatabaseParserException("not found IP2LocationElasticsearchOperation !");
        }
        IP2LocationElasticsearchOperationProxy proxy = new IP2LocationElasticsearchOperationProxy(elasticsearchOperation);
        return new ElasticsearchLocationTable(proxy);
    }

}
