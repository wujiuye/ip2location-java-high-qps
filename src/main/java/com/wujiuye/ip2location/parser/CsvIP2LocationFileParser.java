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
package com.wujiuye.ip2location.parser;

import com.wujiuye.ip2location.context.IP2LocationDatabaseContext;
import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.entity.SystemProperty;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseParserException;
import com.wujiuye.ip2location.table.LocationTable;

import java.util.ArrayList;
import java.util.List;

import java.io.*;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：
 * csv文件格式的ip地理位置库文件解析器
 * }
 */
public abstract class CsvIP2LocationFileParser implements IP2LocationFileParser {

    protected abstract LocationTable newTable() throws Ip2LocationDatabaseParserException;

    @Override
    public LocationTable parser(String url) throws Ip2LocationDatabaseParserException {
        LocationTable table = newTable();
        SystemProperty property = IP2LocationDatabaseContext.getSystemProperty();
        if (property.isUpdate()) {
            this.doParser(table, url);
        }
        return table;
    }

    /**
     * 解析获取所有记录，并插入表
     *
     * @param table 解析的记录会插入这张表
     * @param url   ip库文件路径
     * @throws Ip2LocationDatabaseParserException
     */
    private void doParser(LocationTable table, String url) throws Ip2LocationDatabaseParserException {
        final File file = new File(url);
        if (!file.isFile() || !file.exists()) {
            throw new Ip2LocationDatabaseParserException("Could not parse local file ! not do update.");
        }
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            int count = 0;
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr);) {
                String line;
                List<IP2LocationEntity> batchRecords = new ArrayList<>();
                while (null != (line = br.readLine())) {
                    IP2LocationEntity entiry = newLocationEntity(line);
                    if (entiry == null) {
                        continue;
                    }
                    if (entiry.getIpFrom() == 0L) {
                        continue;
                    }
                    batchRecords.add(entiry);
                    if (batchRecords.size() >= 1000) {
                        int insertBatchResult = table.insertBatch(batchRecords);
                        count += insertBatchResult;
                        batchRecords.clear();
                    }
                }
                if (batchRecords.size() > 0) {
                    int insertBatchResult = table.insertBatch(batchRecords);
                    count += insertBatchResult;
                    batchRecords.clear();
                }
                if (IP2LocationDatabaseContext.getIP2LocationDatabaseListener() != null) {
                    IP2LocationDatabaseContext.getIP2LocationDatabaseListener().dataPreheatSuccess(count, System.currentTimeMillis() - startTime);
                }
            } catch (Exception e) {
                if (IP2LocationDatabaseContext.getIP2LocationDatabaseListener() != null) {
                    Ip2LocationDatabaseParserException exception = new Ip2LocationDatabaseParserException("ip-location faile parse error.");
                    exception.addSuppressed(e);
                    IP2LocationDatabaseContext.getIP2LocationDatabaseListener().dataPreheatException(exception);
                }
            }
        }).start();
    }


    /**
     * 解析一行记录，生成一个IP2LocationEntity实例
     *
     * @param line
     * @return
     */
    private IP2LocationEntity newLocationEntity(String line) {
        String[] item = line.replace("\"", "")
                .split(",");
        IP2LocationEntity entiry = new IP2LocationEntity();
        entiry.setIpFrom(Long.valueOf(item[0]));
        entiry.setIpTo(Long.valueOf(item[1]));
        entiry.setCountryCode(item[2]);
        entiry.setCountry(item[3]);
        entiry.setRegion(item[4]);
        entiry.setCity(item[5]);
        entiry.setCarrier(item[8]);
        return entiry;
    }

}
