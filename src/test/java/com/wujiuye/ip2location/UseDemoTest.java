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

import com.wujiuye.ip2location.entity.IP2LocationEntity;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseException;
import com.wujiuye.ip2location.exception.Ip2LocationDatabaseParserException;
import com.wujiuye.ip2location.listener.IP2LocationDatabaseListener;
import com.wujiuye.ip2location.template.IP2LocationTemplate;

import java.util.concurrent.CountDownLatch;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：}
 */
public class UseDemoTest {

    public static void main(String[] args) throws Ip2LocationDatabaseParserException, Ip2LocationDatabaseException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 创建数据库
        IP2LocationDataBase ip2LocationDataBase = IP2LocationDataBase.newInstance(
                "/Users/wjy/DB19-IP-COUNTRY-REGION-CITY-LATITUDE-LONGITUDE-ISP-DOMAIN-MOBILE.CSV/IP-COUNTRY-REGION-CITY-LATITUDE-LONGITUDE-ISP-DOMAIN-MOBILE.CSV",
                // 如果配置update为true，则数据预热完成或者出现异常时回调
                new IP2LocationDatabaseListener() {

                    @Override
                    public void dataPreheatSuccess(long total, long cntMs) {
                        countDownLatch.countDown();
                        System.out.println("init success! record total:{" + total + "}, cnt {" + (cntMs / 1000) + "}s");
                    }

                    @Override
                    public void dataPreheatException(Ip2LocationDatabaseParserException exception) {
                        countDownLatch.countDown();
                        exception.printStackTrace();
                    }
                });
        // 创建查询模版
        IP2LocationTemplate locationTemplate = ip2LocationDataBase.getLocationTemplate();
        // 等待数据预热完成
        countDownLatch.await();
        // 根据ip查询位置信息
        IP2LocationEntity entity = locationTemplate.selectByIp("161.132.13.1");
        System.out.println(entity);
    }

}
