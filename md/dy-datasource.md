dexcoder-dal可以十分方便的在配置文件中将原来的单数据源修改成动态数据源，而无须修改任何的业务代码。

### 第一步 当然是在spring中声明数据源bean了

    <bean id="dynamicDataSource" class="com.dexcoder.assistant.persistence.DynamicDataSource">
        <property name="dsConfigFile" value="dynamic-ds.xml"/>
    </bean>
    
像其它普通的bean一样，声明DynamicDataSource，只有一个属性参数dsConfigFile指定动态数据源的配置文件，默认为dynamic-ds.xml。上面的跟默认相同，可以省略。

对于dsConfigFile配置文件的查找会遵循以下顺序：

Tomcat的conf目录 -> project目录 -> classpath

这是为了方便在部署项目时，不管在开发环境还是正式环境都不用去频繁的修改配置信息，其实现效果可以参考：让项目、Spring在不同的环境加载不同的配置文件。

另外，别忘了将你使用数据源的地方改为dynamicDataSource:

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource">
            <ref bean="dynamicDataSource"/>
        </property>
    </bean>
    
还有事务管理：

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dynamicDataSource"/>
    </bean>
    
### 第二步 编写动态数据源配置文件dynamic-ds.xml

先来看个样例：
    
    <?xml version="1.0" encoding="UTF-8"?>
    <dataources>
        <datasource id="dataSource1" class="org.apache.commons.dbcp.BasicDataSource" default="true">
            <property name="weight" value="10"/>
            <property name="mode" value="w"/>
            <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/db1?useUnicode=true&amp;characterEncoding=utf-8"/>
            <property name="username" value="root"/>
            <property name="password" value=""/>
        </datasource>
        <datasource id="dataSource2" class="org.apache.commons.dbcp.BasicDataSource">
            <property name="weight" value="5"/>
            <property name="mode" value="r"/>
            <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/db2?useUnicode=true&amp;characterEncoding=utf-8"/>
            <property name="username" value="root"/>
            <property name="password" value=""/>
        </datasource>
        <datasource id="dataSource3" class="org.apache.commons.dbcp.BasicDataSource">
            <property name="weight" value="5"/>
            <property name="mode" value="r"/>
            <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/db3?useUnicode=true&amp;characterEncoding=utf-8"/>
            <property name="username" value="root"/>
            <property name="password" value=""/>
        </datasource>
    </dataources>

- dataources 根标签，固定
- datasource 对应一个数据源
    - Attributes:
        - id 数据源的id，方便在日志中查看当前操作使用了哪个数据源
        - class 数据源使用的DataSource类，可以是dbcp，c3p0等各大数据源
        - default 是否默认，对应spring中的defaultTargetDataSource，应该只有一个
        - properties:
        - weight 权重，value为int型，虽然没有做限定，但仍建议为其设置一个合理且容易理解的值，如权重总和为10
        - mode 读写模式，r-读、w-写、rw-读写
        - 其它的属性，由你使用的具体数据源决定。等同于数据源在spring中声明时可以注入的属性都可以在这里进行设置
        
### 第三步 声明使用动态数据源的拦截器：

    <bean id="dynamicDsInterceptor" class="com.dexcoder.assistant.interceptor.DynamicDsInterceptor"></bean>
    
这就可以，大功告成了，接下来就可以在你的项目中尽情的使用动态数据源了。

## 动态修改数据源

`DynamicDataSource`中提供了initDataSources方法来初始化数据源，它的参数是 List<Map<String, String>> dataSourceList，只需要将数据源的参数封装成map的list传入调用该方法就能实现动态修改了。以下是一个简单的候示例：

    List<Map<String, String>> dsList = new ArrayList<Map<String, String>>();
    Map<String, String> map = new HashMap<String, String>();
    map.put("id", "dataSource4");
    map.put("class", "org.apache.commons.dbcp.BasicDataSource");
    map.put("default", "true");
    map.put("weight", "10");
    map.put("mode", "rw");
    map.put("driverClassName", "com.mysql.jdbc.Driver");
    map.put("url",
        "jdbc:mysql://localhost:3306/db1?useUnicode=true&amp;characterEncoding=utf-8");
    map.put("username", "root");
    map.put("password", "");
    dsList.add(map);
    dynamicDataSource.initDataSources(dsList);
    
在实际的场景中，根据项目使用技术的不同，你可以使用监听器、socket、配置中心等来实现该数据源动态修改的功能，只要保存调用initDataSources方法时传入的数据源信息是正确的就可以了。