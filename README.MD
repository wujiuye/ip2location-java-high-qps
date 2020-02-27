# ip2location-java-high-qps

> auth:     wujiuye \
> version:  1.0.0  date: 2019-10-25 完成redis的支持\
> version:  1.1.0  date: 2019-10-29 完成es的支持，es和redis的切换由SPI改为启动参数配置\
> version:  1.1.1  date: 2019-10-31 扩展支持复杂查询 \
> version:  1.1.2  date: 2019-11-02 添加修正库

使用前提，需要有IP库位置信息来源
```shell script
DB19-IP-COUNTRY-REGION-CITY-LATITUDE-LONGITUDE-ISP-DOMAIN-MOBILE.CSV
```
[IP库文件获取链接：https://www.ip2location.com/database/db19-ip-country-region-city-latitude-longitude-isp-domain-mobile](https://www.ip2location.com/database/db19-ip-country-region-city-latitude-longitude-isp-domain-mobile)

框架功能：
* 1、封装对多种缓存方式的支持；
* 2、封装文件解析到数据预热逻辑。

优点：
* 1、扩展性强，当前支持redis、es缓存ip2库数据；
* 2、满足于高并发服务根据ip查询位置信息的场景。

### 项目中使用，添加依赖

* Gradle

Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```groovy
    dependencies {
	        implementation 'com.github.wujiuye:ip2location-java-high-qps:1.1.2'
	}
```

* maven

```xml
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Step 2. Add the dependency
```xml
    <dependency>
	    <groupId>com.github.wujiuye</groupId>
	    <artifactId>ip2location-java-high-qps</artifactId>
	    <version>1.1.2</version>
	</dependency>
```

### 配置使用数据源

选择数据源类型，es|redis
```text
-Dip.database.db=es
or
-Dip.database.db=redis     
```
如：
```text
java -Xss256k -jar -Dip.database.db=redis xxx-1.0.0.jar
```
框架会根据配置的数据源类型自动注册文件解析器

### 关于数据源表的初始化

使用需要配置update启动参数，默认false
```text
 -Dip.database.table.update=true
```
如：
```text
java -Xss256k -jar -Dip.database.table.update=true -Dip.database.db=redis xxx-1.0.0.jar
```

* 1、true: 首次启动就会从指定的url文件读取解析记录，插入数据表
* 2、false: 表示已经确认表存在记录了，不需要再更新。（也不会去解析文件）

解析记录与插入表是异步的，后台开启一个线程执行，就是数据预热。

### 组件不依赖jedis与ES，需外部提供操作支持

通过使用java的SPI实现由外部注册操作支持

#### 1、使用Redis

1、配置数据源类型为redis
```text
-Dip.database.db=redis
```

2、配置redis操作支持实现类
需要自己实现RedisOperation，并在resource/META-INF/services目录下创建文件
IP2LocationRedisOperation
在文件中配置redis操作的实现类，如：
```text
DebugRedisOperation
```

3、 关于redis缓存的key
>如果使用redis存储数据，则key固定为
>```shell script
>ip-country-city-locations       // 存储真实记录
>ip-country-city-locations-range // 存储范围与真实记录的key的映射
>```

#### 2、使用Elasticsearch

1、配置数据源类型为es
```text
-Dip.database.db=es
```

2、在resource/META-INF/services目录下创建文件
IP2LocationElasticsearchOperation
在文件中配置Es操作的支持的实现类，如：
```text
DebugElasticsearchOperation
```

3、默认索引、类型分别为
```text
index: ip-location-database
type: ip-location-table
```

### 3、功能扩展

* 1、提供ExtendService扩展功能接口，在数据插入时和清除时被调用，实现些扩展功能，但非必须实现
* 2、提供SearchExtendService复杂查询的扩展支持，每种类型的table都必须要实现这个接口，
或者委托实现。

ExtendService是为SearchExtendService提供数据支持的，比如redis这种key-value的存储方式
只有扩展做额外的数据存在，才能实现。而es就不需要借助ExtendService.

### 4、关于修正库

提供给外部纠正错误数据的库，使用库不必关心存储与查询。

使用场景：
ip2中查不到的数据可以通过第三方查找，将从第三方找到的数据修正到数据库中，
但考虑到不影响旧数据，将其放到新的库中。当要求精确度高的场景，可以先查正常
的库，如果找不到再从修正库中找。注意高并发下的耗时，根据业务场景考虑取舍。

