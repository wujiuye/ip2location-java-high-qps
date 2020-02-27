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
package com.wujiuye.ip2location.extend;

import java.util.Map;
import java.util.Set;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/31 {描述：
 * 搜索扩展服务，负载查询
 * }
 */
public interface SearchExtendService {

    /**
     * 获取所有国家
     *
     * @return
     */
    Set<String> selectAllCountorys();

    /**
     * 获取国家的所有城市
     *
     * @param countoryCode 国家编码
     * @return
     */
    Set<String> selectAllCitys(String countoryCode);

    /**
     * 获取所有运营商信息
     * 国家->运营商 映射
     *
     * @return
     */
    Map<String, Set<String>> selectAllCarriers();

    /**
     * 获取所有运营商信息
     * 国家->运营商 映射
     *
     * @param countoryCodes 指定国家的编码
     * @return
     */
    Map<String, Set<String>> selectAllCarriers(Set<String> countoryCodes);

}
