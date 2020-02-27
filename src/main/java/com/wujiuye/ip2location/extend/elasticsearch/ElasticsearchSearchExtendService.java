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
package com.wujiuye.ip2location.extend.elasticsearch;

import com.wujiuye.ip2location.extend.SearchExtendService;
import com.wujiuye.ip2location.operation.elasticsearch.IP2LocationElasticsearchOperation;
import com.wujiuye.ip2location.proxy.elasticsearch.IP2LocationElasticsearchOperationProxy;

import java.util.Map;
import java.util.Set;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/31 {描述：
 * 扩展复杂查询实现类
 * }
 */
public class ElasticsearchSearchExtendService implements SearchExtendService {

    private IP2LocationElasticsearchOperation operation;

    public ElasticsearchSearchExtendService(IP2LocationElasticsearchOperation operation) {
        if (operation instanceof IP2LocationElasticsearchOperationProxy) {
            this.operation = ((IP2LocationElasticsearchOperationProxy) operation).getProxy();
        } else {
            this.operation = operation;
        }
    }

    @Override
    public Set<String> selectAllCountorys() {
        // todo
        return null;
    }

    @Override
    public Set<String> selectAllCitys(String countoryCode) {
        // todo
        return null;
    }

    @Override
    public Map<String, Set<String>> selectAllCarriers() {
        // todo
        return null;
    }

    @Override
    public Map<String, Set<String>> selectAllCarriers(Set<String> countoryCodes) {
        // todo
        return null;
    }

}
