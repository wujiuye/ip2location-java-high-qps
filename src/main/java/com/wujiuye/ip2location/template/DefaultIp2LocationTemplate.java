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
package com.wujiuye.ip2location.template;

import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.extend.SearchExtendService;
import com.wujiuye.ip2location.table.LocationTable;
import com.wujiuye.ip2location.util.Ip2LocationNumberUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：
 * ip库操作模版
 * }
 */
public class DefaultIp2LocationTemplate implements IP2LocationTemplate {

    private LocationTable locationTable;

    public DefaultIp2LocationTemplate(LocationTable table) {
        this.locationTable = table;
    }

    @Override
    public IP2LocationEntity selectByIp(String ipv4Address) {
        Long midNumber = Ip2LocationNumberUtils.parseIp(ipv4Address);
        return locationTable.selectOne(midNumber);
    }

    @Override
    public Set<String> selectAllCountorys() {
        return locationTable.getSearchExtendService().selectAllCountorys();
    }

    @Override
    public Set<String> selectAllCitys(String countoryCode) {
        SearchExtendService searchExtendService = locationTable.getSearchExtendService();
        if (searchExtendService == null) {
            throw new UnsupportedOperationException(locationTable.getClass() + " not suppor!");
        }
        return searchExtendService.selectAllCitys(countoryCode);
    }

    @Override
    public Map<String, Set<String>> selectAllCarriers() {
        SearchExtendService searchExtendService = locationTable.getSearchExtendService();
        if (searchExtendService == null) {
            throw new UnsupportedOperationException(locationTable.getClass() + "not suppor!");
        }
        return searchExtendService.selectAllCarriers();
    }

    @Override
    public Map<String, Set<String>> selectAllCarriers(Set<String> countorys) {
        SearchExtendService searchExtendService = locationTable.getSearchExtendService();
        if (searchExtendService == null) {
            throw new UnsupportedOperationException(locationTable.getClass() + "memory table not suppor!");
        }
        return searchExtendService.selectAllCarriers(countorys);
    }

}
