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
package com.wujiuye.ip2location.listener;

import com.wujiuye.ip2location.exception.Ip2LocationDatabaseParserException;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/28 {描述：}
 */
public interface IP2LocationDatabaseListener {

    /**
     * 数据预热完成（成功）
     *
     * @param total 总记录数
     * @param cntMs 总耗时，单位毫秒
     */
    void dataPreheatSuccess(long total, long cntMs);

    /**
     * 数据预热（更新）异常
     *
     * @param exception 发生的异常
     */
    void dataPreheatException(Ip2LocationDatabaseParserException exception);

}
