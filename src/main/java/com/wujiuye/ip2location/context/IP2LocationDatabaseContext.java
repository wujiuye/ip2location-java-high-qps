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
package com.wujiuye.ip2location.context;

import com.wujiuye.ip2location.entity.SystemProperty;
import com.wujiuye.ip2location.listener.IP2LocationDatabaseListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/28 {描述：}
 */
public final class IP2LocationDatabaseContext {

    private static Map<String, Object> CONTEXT = new ConcurrentHashMap<>();

    public static SystemProperty getSystemProperty() {
        if (!CONTEXT.containsKey(SystemProperty.class.getName())) {
            CONTEXT.put(SystemProperty.class.getName(), SystemProperty.getInstance());
        }
        return (SystemProperty) CONTEXT.get(SystemProperty.class.getName());
    }

    public static IP2LocationDatabaseListener getIP2LocationDatabaseListener() {
        return (IP2LocationDatabaseListener) CONTEXT.get(IP2LocationDatabaseListener.class.getName());
    }

    public static void settingIP2LocationDatabaseListener(IP2LocationDatabaseListener listener) {
        if (listener == null) {
            return;
        }
        CONTEXT.put(IP2LocationDatabaseListener.class.getName(), listener);
    }

}
