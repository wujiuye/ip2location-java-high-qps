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

import com.wujiuye.ip2location.entity.IPLocationEntity;

/**
 * @author wujiuye
 * @version 1.0 on 2019/11/2 {描述：
 * 提供访问修正库的模版支持
 * }
 */
public interface IPLocationTemplate {

    /**
     * 添加一条修正记录
     * 使用场景：
     * 1、正常库中没有记录
     * 2、当前记录与库中记录不同
     *
     * @param record
     */
    void correct(IPLocationEntity record);

    /**
     * 从修正记录中查询
     * 使用场景：
     * 1、当正常的库中没有查到数据时，可从修正库中查询
     * 2、为保证精准，可优先从修正库中查询，没有再从正常的库中查询
     *
     * @param ipv4Address
     * @return
     */
    IPLocationEntity queryBy(String ipv4Address);

}
