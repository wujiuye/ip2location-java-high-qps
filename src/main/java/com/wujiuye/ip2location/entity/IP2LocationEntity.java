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

import lombok.*;

import java.io.Serializable;

/**
 * @author wujiuye
 * @version 1.0 on 2019/10/25 {描述：}
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class IP2LocationEntity implements Serializable {

    /**
     * ip地址范围-from
     */
    private Long ipFrom;
    /**
     * ip地址范围-to
     */
    private Long ipTo;
    /**
     * 国家代码
     */
    private String countryCode;
    /**
     * 国家
     */
    private String country;
    /**
     * 运营商
     */
    private String carrier;
    /**
     * 地区/区域
     * 比如：美国的哪个州，中国的哪个省
     */
    private String region;
    /**
     * 城市
     */
    private String city;

}
