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
package com.wujiuye.ip2location.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：}
 */
@Getter
@Slf4j
public class SystemProperty {

    /**
     * 选择数据源类型，es|redis|memory
     * -Dip.database.db=es
     * or
     * -Dip.database.db=redis
     * or
     * -Dip.database.db=memory
     */
    private String dbType;
    /**
     * 是否更新ip库
     * （当表类型为非内存缓存时才会起作用）
     * -Dip.database.table.update=true
     */
    private boolean update;

    private SystemProperty() {

    }

    public static SystemProperty getInstance() {
        SystemProperty property = new SystemProperty();
        String updateStr = System.getProperty("ip.database.table.update");
        if (updateStr == null || updateStr.trim().length() == 0) {
            property.update = false;
        } else {
            log.info("===> read config [ip.database.table.update] is {}", updateStr);
            property.update = Boolean.valueOf(updateStr);
        }
        property.dbType = System.getProperty("ip.database.db");
        return property;
    }

}
