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

import com.wujiuye.ip2location.entity.IP2LocationEntity;

import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/31 {描述：}
 */
public interface ExtendService {

    /**
     * 在入库之前拦截器做扩展处理
     *
     * @param records 批量记录
     */
    void preInster(final List<IP2LocationEntity> records);

    /**
     * 做数据清理之前
     */
    void preClear();

}
