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
package com.wujiuye.ip2location;

import com.wujiuye.ip2location.context.IP2LocationDatabaseContext;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseException;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseParserException;
import com.wujiuye.ip2location.listener.IP2LocationDatabaseListener;
import com.wujiuye.ip2location.parser.IP2LocationFileParser;
import com.wujiuye.ip2location.parser.elasticsearch.ElasticsearchIP2LocationFileParser;
import com.wujiuye.ip2location.parser.memory.MemoryIP2LocationFileParser;
import com.wujiuye.ip2location.parser.redis.RedisIP2LocationFileParser;
import com.wujiuye.ip2location.table.LocationTable;
import com.wujiuye.ip2location.template.DefaultIPLocationTemplate;
import com.wujiuye.ip2location.template.DefaultIp2LocationTemplate;
import com.wujiuye.ip2location.template.IP2LocationTemplate;
import com.wujiuye.ip2location.template.IPLocationTemplate;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：
 * 数据库
 * }
 */
public final class IP2LocationDataBase {

    /**
     * 表
     */
    private LocationTable locationTable;
    /**
     * 文件的路径
     */
    private final String url;
    private IP2LocationDatabaseListener listener;

    private IP2LocationDataBase(String url, IP2LocationDatabaseListener listener) {
        this.url = url;
        this.listener = listener;
    }

    /**
     * 通过url实例化获取数据源（非单利）
     *
     * @param url
     * @param listener 监听器
     * @return
     * @throws Ip2LocationDatabaseParserException
     */
    public static IP2LocationDataBase newInstance(String url, IP2LocationDatabaseListener listener) throws Ip2LocationDatabaseParserException {
        if (url == null) {
            throw new Ip2LocationDatabaseParserException("url is null...");
        }
        IP2LocationDatabaseContext.settingIP2LocationDatabaseListener(listener);
        IP2LocationDataBase IP2LocationDataBase = new IP2LocationDataBase(url, listener);
        IP2LocationDataBase.init();
        return IP2LocationDataBase;
    }

    /**
     * 初始化获取ip库并解析生成数据源
     *
     * @throws Ip2LocationDatabaseParserException
     */
    private synchronized void init() throws Ip2LocationDatabaseParserException {
        if (url.startsWith("http")) {
            throw new Ip2LocationDatabaseParserException("暂时不支持远程下载");
        } else {
            String dbType = IP2LocationDatabaseContext.getSystemProperty().getDbType();
            IP2LocationFileParser parser;
            switch (dbType) {
                case "es":
                    parser = new ElasticsearchIP2LocationFileParser();
                    break;
                case "redis":
                    parser = new RedisIP2LocationFileParser();
                    break;
                case "memory":
                    parser = new MemoryIP2LocationFileParser();
                    break;
                default:
                    throw new Ip2LocationDatabaseParserException("不支持的数据源类型，找不到匹配的解析器！");
            }
            locationTable = parser.parser(this.url);
        }
    }

    /**
     * 获取ip库查询模版
     *
     * @return
     * @throws Ip2LocationDatabaseException
     */
    public IP2LocationTemplate getLocationTemplate() throws Ip2LocationDatabaseException {
        if (this.locationTable == null) {
            throw new Ip2LocationDatabaseException("locationTable is null.");
        }
        return new DefaultIp2LocationTemplate(this.locationTable);
    }

    /**
     * 获取修正库的访问模版
     *
     * @return
     */
    public IPLocationTemplate getCorrectLocationTemplate() {
        if (this.locationTable == null) {
            throw new Ip2LocationDatabaseException("locationTable is null.");
        }
        return new DefaultIPLocationTemplate(locationTable.getRecordCorrect());
    }

}
