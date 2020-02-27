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
package com.wujiuye.ip2location.util;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：}
 */
public class Ip2LocationNumberUtils {

    /**
     * 将ip转为number
     *
     * @param ipv4Address
     * @return
     */
    public static Long parseIp(String ipv4Address) {
        String[] ipStr = ipv4Address.trim()
                .split("\\.");
        if (ipStr.length != 4) {
            return 0L;
        }
        return Long.valueOf(ipStr[0]) * (1 << 24)
                + Long.valueOf(ipStr[1]) * (1 << 16)
                + Long.valueOf(ipStr[2]) * (1 << 8)
                + Long.valueOf(ipStr[3]);
    }

}
