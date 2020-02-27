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

import com.wujiuye.ip2location.correct.RecordCorrect;
import com.wujiuye.ip2location.entity.IPLocationEntity;

/**
 * @author wujiuye
 * @version 1.0 on 2019/11/2 {描述：
 * 修正库操作模版
 * }
 */
public class DefaultIPLocationTemplate implements IPLocationTemplate {

    private RecordCorrect recordCorrect;

    public DefaultIPLocationTemplate(RecordCorrect recordCorrect) {
        this.recordCorrect = recordCorrect;
    }

    @Override
    public void correct(IPLocationEntity record) {
        if (record.getIp() == null
                || record.getCountryCode() == null
                || record.getCountry() == null) {
            return;
        }
        this.recordCorrect.correct(record);
    }

    @Override
    public IPLocationEntity queryBy(String ipv4Address) {
        if (ipv4Address == null || ipv4Address.length() == 0) {
            return null;
        }
        return recordCorrect.queryBy(ipv4Address);
    }

}
