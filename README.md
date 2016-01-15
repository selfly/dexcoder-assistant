#Dexcoder快速开发辅助工具包

最近更新：

### 版本 2.3.0 更新时间：2016-01-15

- 实体类表名及属性名映射增加注解支持
- 增加更多的执行自定义sql方法
- 因为sql权限问题去掉使用TRUNCATE的deleteAll方法
- 修正使用注解时注解的属性名不遵循规范时get方法主键错误问题
- 修正水平拆分数据分表不根据主键拆分时update无法获取表名的问题

[详细更新日志](md/update-log.md)

配置动态数据源：[在dexcoder-dal中使用动态数据源并设置读写分离](md/dy-datasource.md)

数据水平分表：[在dexcoder-dal中实现分表数据水平拆分](md/multi-table.md)

##核心组件dexcoder-dal使用说明

如果你不喜欢用`Hibernate`、`Mybaits`这类ORM框架，喜欢`JdbcTemplate`或`DbUtils`，那么可以试试这个封装的通用dal，这可能是目前封装的最方便易用的通用dal层了。


dexcoder-dal的一些特性：

1. 一个dao即可以搞定所有的实体类，不必再一个个建立跟实体对应的继承于类似BaseDao这类“通用dao”了。
2. 各类方法参数除了`Entity`外，支持更强大的`Criteria`方式。
3. sql的where条件支持一些复杂的条件，如`=`、`!=`、`or`、`in`、`not in`甚至是执行函数。
4. 允许在查询时指定使用哪个字段进行排序，可以指定多个进行组合升降序自由排序。
5. 支持在查询时指定返回字段的白名单和黑名单，可以指定只返回某些字段或不返回某些字段。
6. select查询时支持函数，`count()`、`max()`、`to_char()`、甚至是`distinct`,理论上都可以支持。
7. 方便强大的分页功能，无须额外操作，二三行代码搞定分页，自动判断数据库，无须指定。
8. 可以使用`{}`和`[]`完成一些特殊的操作，`{}`中的代码将原生执行，`[]`中的代码会进行命名转换，一般fieldName转columnName。
9. 支持执行自定义sql。
10. 支持使用类似mybatis的方式执行自定义sql。
11. 支持读写分离和动态数据源。
12. 对于数据分表水平拆分支持友好。

该通用dao是在使用过程中，针对常规的泛型dao经常遇到的一些不便问题进行了改进。命名上遵循了约定优于配置的原则，典型约定如下：

- 表名约定 `USER_INFO`表实体类名为`UserInfo`。
- 字段名约定 `USER_NAME`实体类中属性名为`userName`。
- 主键名约定 `USER_INFO`表主键名为`USER_INFO_ID`，同理实体类中属性名为`userInfoId`。
- `Oracle`序列名约定 `USER_INFO`表对应的主键序列名为`SEQ_USER_INFO`

当然，这些你可以在扩展中改变它，但不建议这么做，这本身就是一个良好的规范。

要在项目中使用通用dao十分简单，目前已上maven中央库，直接在pom.xml中添加依赖：

    <dependency>
        <groupId>com.dexcoder</groupId>
        <artifactId>dexcoder-dal-spring</artifactId>
        <version>${version}</version>
    </dependency>

然后在spring的配置文件中声明如下bean：

    <bean id="jdbcDao" class="com.dexcoder.dal.spring.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>
    <!--需要分页时声明-->
    <bean id="pageControl" class="com.dexcoder.dal.spring.page.PageControl"></bean>

接下来就可以注入到您的`Service`或者其它类中使用了。

##下面是一些常用的方法示例，这里的`Entity`对象为`User`，对于任何的`Entity`都是一样的.

先来看一下`User`对象及它继承的`Pageable`

	public class User extends Pageable {
        private Long              userId;
        private String            loginName;
        private String            password;
        private Integer           userAge;
        private String            userType;
        private String            email;
        private Date              gmtCreate;
		//......
	}

Pageable对象，用来保存页码、每页条数信息以支持分页

	public class Pageable implements Serializable {
		/** 每页显示条数 */
		protected int             itemsPerPage     = 20;
		/** 当前页码 */
		protected int             curPage          = 1;

		//......
	}

都是普通的JavaBean对象，下面来看看如何进行具体的增删改查，每种操作都演示了`Entity`和`Criteria`两种方式。

### insert操作

Entity方式

    User user = new User();
    user.setLoginName("selfly_a");
    //......
    Long userId = jdbcDao.insert(user);

Criteria 方式

    Criteria criteria = Criteria.insert(User.class).into("loginName", "selfly_b").into("password", "12345678")
        .into("email", "selflly@foxmail.com").into("userAge", 22).into("userType", "2").into("gmtCreate", new Date());
    Long userId = jdbcDao.insert(criteria);

### save操作，和insert的区别在于不处理主键，由调用者指定

Entity方式

    User user = new User();
    user.setUserId(-1L);
    //......
    jdbcDao.save(user);

Criteria 方式

    Criteria criteria = Criteria.insert(User.class).into("userId", -2L).into("loginName", "selfly-2")
        .into("password", "12345678").into("email", "selflly@foxmail.com").into("userAge", 22).into("userType", "2")
        .into("gmtCreate", new Date());
    jdbcDao.save(criteria);

### update操作

Entity方式

    User user = new User();
    user.setUserId(57L);
    user.setPassword("abcdef");
    //方式一 为null的属性值将被忽略
    jdbcDao.update(user);

    //方式二 为null的属性值将更新到数据库
    jdbcDao.update(user,false);

Criteria方式

    //criteria方式这里的email设为null也将被更新
    Criteria criteria = Criteria.update(User.class).set("password", "update222").set("email",null)
        .where("userId", new Object[] { 56L, -1L, -2L });
    jdbcDao.update(criteria);

###get操作

根据主键

    User user = jdbcDao.get(User.class, 63L);

### Criteria方式

    //criteria，主要用来指定字段白名单、黑名单等
    Criteria criteria = Criteria.select(User.class).include("loginName");
    User user = jdbcDao.get(criteria, 73L);

###delete操作

根据主键

    jdbcDao.delete(User.class, 57L);

Entity方式

    //会把不为空的属性做为where条件
    User u = new User();
    u.setLoginName("selfly-1");
    u.setUserType("1");
    jdbcDao.delete(u);

Criteria方式

    //where条件使用了or
    Criteria criteria = Criteria.delete(User.class).where("loginName", new Object[] { "liyd2" })
        .or("userAge", new Object[]{64});
    jdbcDao.delete(criteria);

### 列表查询操作

所有结果

    List<User> users = jdbcDao.queryList(User.class);

以Entity中不为空的属性作为查询条件

    User user = new User();
    user.setUserType("1");
    //......
    List<User> users = jdbcDao.queryList(user);

Criteria方式，可以指定黑白名单、排序字段等

    Criteria criteria = Criteria.select(User.class).exclude("userId")
        .where("loginName", new Object[]{"liyd"}).asc("userAge").desc("userId");
    List<User> users = jdbcDao.queryList(criteria);

指定逻辑操作符

    //使用了like，可以换成!=、in、not in等
    Criteria criteria = Criteria.select(User.class).where("loginName", "like",
        new Object[] { "%liyd%" });
    user.setUserAge(16);
    //这里entity跟criteria方式混合使用了，建议少用
    List<User> users = jdbcDao.queryList(user, criteria.include("userId"));

### count记录数查询，除了返回值不一样外，其它和列表查询一致

    //Entity方式
    user.setUserName("liyd");
    int count = jdbcDao.queryCount(user);

    //Criteria方式
    Criteria criteria = Criteria.select(User.class).where("loginName", new Object[] { "liyd" })
        .or("userAge", new Object[]{27});
    int count = jdbcDao.queryCount(criteria);

### 查询单个结果，参数使用方式同上

    //Entity
    User user = jdbcDao.querySingleResult(user);

    //Criteria
    Criteria criteria = Criteria.select(User.class).where("loginName", new Object[] { "liyd" })
        .and("userId", new Object[]{23L});
    User u = jdbcDao.querySingleResult(criteria);

### 指定属性白名单，在任何查询方法中都可以使用

    //将只返回loginName
    Criteria criteria = Criteria.select(User.class).include("loginName");
    User u = jdbcDao.get(criteria, 23L);

### 指定属性黑名单，在任何查询方法中都可以使用

    //将不返回loginName
    Criteria criteria = Criteria.select(User.class).exclude("loginName");
    User u = jdbcDao.get(criteria, 23L);

### 指定排序

    //指定多个排序字段，asc、desc
    Criteria criteria = Criteria.select(User.class).exclude("userId")
        .where("loginName", new Object[]{"liyd"}).asc("userId").desc("userAge");
    List<User> users = jdbcDao.queryList(criteria);

### 分页

直接传入Entity，继承于`Pageable`

    //进行分页，只需要增加这行，列表查询方式跟上面没有任何区别
    PageControl.performPage(user);
    //分页后该方法即返回null，由PageControl中获取
    jdbcDao.queryList(user);
    Pager pager = PageControl.getPager();
    //列表
    List<User> users = pager.getList(User.class);
    //总记录数
    int itemsTotal = pager.getItemsTotal();

直接传入页码和每页大小

    //直接传入页码和每页条数
    PageControl.performPage(1, 10);
    //使用Criteria方式，并指定排序字段方式为asc
    Criteria criteria = Criteria.select(User.class).include("loginName", "userId")
        .where("loginName", new Object[]{"liyd"}).asc("userId");
    jdbcDao.queryList(criteria);
    Pager pager = PageControl.getPager();

### 不同的属性在括号内or的情况：

    Criteria criteria = Criteria.select(User.class)
        .where("userType", new Object[] { "1" }).begin()
        .and("loginName", new Object[] { "selfly" })
        .or("email", new Object[] { "javaer@live.com" }).end()
        .and("password", new Object[] { "123456" });
    User user = jdbcDao.querySingleResult(criteria);

### 执行函数

    //max()
    Criteria criteria = Criteria.select(User.class).addSelectFunc("max([userId])");
    Long userId = jdbcDao.queryForObject(criteria);

    //count()
    Criteria criteria = Criteria.select(User.class).addSelectFunc("count(*)");
    Long count = jdbcDao.queryForObject(criteria);

    //distinct
    Criteria criteria = Criteria.select(User.class).addSelectFunc("distinct [loginName]");
    List<Map<String, Object>> mapList = jdbcDao.queryForList(criteria);

默认情况下，`addSelectFunc`方法返回结果和表字段互斥，并且没有排序，如果需要和表其它字段一起返回并使用排序，可以使用如下代码：

    Criteria criteria = Criteria.select(User.class).addSelectFunc("DATE_FORMAT(gmt_create,'%Y-%m-%d %h:%i:%s') date",false,true);
    List<Map<String, Object>> mapList = jdbcDao.queryForList(criteria);

这是在select中执行函数，那怎么在update和where条件中执行函数呢？前面提到的`{}`和`[]`就可以起到作用了。

看下面代码：

    Criteria criteria = Criteria.update(User.class).set("[userAge]", "[userAge]+1")
        .where("userId", new Object[] { 56L });
    jdbcDao.update(criteria);

以上代码将执行sql：`UPDATE USER SET USER_AGE = USER_AGE+1  WHERE USER_ID =  ?`，`[]`中的fieldName被转换成了columnName,

也可以使用`{}`直接写columnName，因为在`{}`中的内容都是不做任何操作原生执行的，下面代码效果是一样的：

    Criteria criteria = Criteria.update(User.class).set("{USER_AGE}", "{USER_AGE + 1}")
        .where("userId", new Object[] { 56L });
    jdbcDao.update(criteria);

同理，在where中也可以使用该方式来执行函数：

    Criteria criteria = Criteria.select(User.class).where("[gmtCreate]", ">",
        new Object[] { "str_to_date('2015-10-1','%Y-%m-%d')" });
    List<User> userList = jdbcDao.queryList(criteria);

### 表别名支持

有些时候，就算单表操作也必须用到表别名，例如oracle中的xmltype类型。可以在Criteria中设置表别名：

    Criteria criteria = Criteria.select(Table.class).tableAlias("t").addSelectFunc("[xmlFile].getclobval() xmlFile")
            .where("tableId", new Object[]{10000002L});
    Object obj = jdbcDao.queryForObject(criteria);

    //对应的sql
    select t.XML_FILE.getclobval() xmlFile from TABLE t where t.TABLE_ID = ?
    
### 使用注解

可以用注解来指定表名、主键、列名或者忽略某个属性。具体的用法下面代码一看就能明白，唯一需要注意的是注解是在`getter`方法上而不是属性上：

    @Table(name = "USER_A", pkField = "userId", pkColumn = "USER_ID")
    public class AnnotationUser extends Pageable {

        /** 用户id */
        private Long              userId;

        /** 数据库关键字 */
        private String            desc;

        /** 修改时间 数据库无 */
        private Date              gmtModify;
        
        //略...

        @Column(name = "`DESC`")
        public String getDesc() {
            return desc;
        }

        @Transient
        public Date getGmtModify() {
            return gmtModify;
        }
    }

### 执行自定义sql

在实际的应用中，一些复杂的查询如联表查询、子查询等是省不了的。鉴于这类sql的复杂性和所需要的各类优化，通用dao并没有直接封装而是提供了执行自定义sql的接口。

执行自定义sql支持两种方式：直接传sql执行和mybatis方式执行。

###直接传sql执行

该方式可能会让除了dao层之外的业务层出现sql代码，因此是不推荐的，它适合一些不在项目中的情况。

何为不在项目中的情况？例如做一个开发自用的小工具，临时处理一批业务数据等这类后期不需要维护的代码。

要执行自定义sql首先需要在`jdbcDao`中注入`sqlFactory`，这里使用`SimpleSqlFactory`：

    <bean id="jdbcDao" class="com.dexcoder.dal.spring.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
        <property name="sqlFactory" ref="sqlFactory"/>
    </bean>
    <bean id="sqlFactory" class="com.dexcoder.dal.SimpleSqlFactory">
    </bean>

然后就可以直接传入sql执行了：

    List<Map<String, Object>> list = jdbcDao.queryRowMapListForSql("select * from USER where login_name = ?",
        new Object[] { "selfly_a99" });

这个实现比较简单，参数Object数组中不支持复杂的自定义对象。

### mybatis方式执行

采用了插件式实现，使用该方式首先添加依赖：

    <dependency>
        <groupId>com.dexcoder</groupId>
        <artifactId>dexcoder-dal-batis</artifactId>
        <version>${version}</version>
    </dependency>

之后同样注入`sqlFactory`，把上面的`SimpleSqlFactory`替换成`BatisSqlFactoryBean`：

    <bean id="jdbcDao" class="com.dexcoder.dal.spring.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
        <property name="sqlFactory" ref="sqlFactory"/>
    </bean>
    <bean id="sqlFactory" class="com.dexcoder.dal.batis.BatisSqlFactoryBean">
        <property name="sqlLocation" value="user-sql.xml"/>
    </bean>

`BatisSqlFactoryBean`有一个`sqlLocation`属性，指定自定义的sql文件，因为使用了spring的解析方式，所以可以和指定spring配置文件时一样使用各类通配符。

`user-sql.xml`是一个和mybatis的mapper类似的xml文件：

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper
            PUBLIC "-//dexcoder.com//DTD Mapper 2.0//EN"
            "http://www.dexcoder.com/dtd/batis-mapper.dtd">
    <mapper namespace="User">
        <sql id="columns">
            user_id,login_name,password,user_age,user_type
        </sql>

        <select id="getUser">
            select
            <include refid="columns"/>
            from user
            <where>
                <if test="params[0] != null and params[0].userType != null">
                    user_type = #{params[0].userType}
                </if>
                <if test="params[1] != null">
                    and login_name in
                    <foreach collection="params[1]" index="index" item="item" separator="," open="(" close=")">
                        #{item}
                    </foreach>
                </if>
            </where>
        </select>
    </mapper>

然后使用代码调用：

    User user = new User();
    user.setUserType("1");
    Object[] names = new Object[] { "selfly_a93", "selfly_a94", "selfly_a95" };
    List<Map<String, Object>> mapList = jdbcDao.queryRowMapListForSql("User.getUser", "params", new Object[] { user, names });
    for (Map<String, Object> map : mapList) {
        System.out.println(map.get("userId"));
        System.out.println(map.get("loginName"));
    }

我们调用`queryForSql`方法时传入了三个参数：

- User.getUser 具体的sql全id，namespace+id。
- params 自定义sql中访问参数的key，如果不传入默认为`parameters`。
- Object[] sql中用到的参数。访问具体参数时可以使用parameters[0],parameters[1]对应里面相应的元素，支持复杂对象。

可以看到这里支持复杂参数，第一个是`User`bean对象，第二个是Object数组，至于获取方式可以看上面的xml代码。

除了传入的参数为Object数组并使用`parameters[0]`这种方式访问相应的元素外，其它的和mybatis可以说是一样的，mybatis支持的动态sql方式这里也可以支持,因为他本身就是来源于mybatis。

另外返回结果中map的key做了`LOGIN_NAME`到骆驼命名法`loginName`的转换。

###项目结构说明

dexcoder-commons 一些通用的工具类，里面的maven依赖可以按需添加。

dexcoder-dal 通用dal的接口，这里对于数据库访问没有具体的实现。具体的数据库操作取决于选择的实现方式(目前只有Spring JdbcTemplate)。

dexcoder-dal-spring	Spring JdbcTemplate的dal实现。

dexcoder-dal-batis	mybatis方式执行sql实现。

dexcoder-test 测试工程

###一些说明

`BatisSqlFactory`方式由分析了mybatis源码后，提取使用了大量mybatis的代码。

JdbcDao在声明时可以根据需要注入其它几个参数：

    <bean id="jdbcDao" class="com.dexcoder.dal.spring.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
        <property name="sqlFactory" ref="..."/>
        <property name="mappingHandler" ref="..."/>
        <property name="rowMapperClass" value="..."/>
        <property name="dialect" value="..."/>
    </bean>

- mappingHandler 默认使用DefaultMappingHandler，即遵守上面的约定优于配置，如果需要自定义可以实现该接口。
- sqlFactory 执行自定义sql时注入相应的sqlFactory。
- rowMapperClass 默认使用了spring的`BeanPropertyRowMapper.newInstance(clazz)`,需要自定义可以自行实现，标准spring的RowMapper实现即可。
- dialect 数据库方言，为空会自动判断，一般不需要注入。

>很多人问如果我要实现一个自己的dao是不是要继承这个dao然后实现自己的方法？其实不用，自己实现的dao按spring标准的JdbcTemplate路子走就可以，完全无耦合。
>另外自己实现的dao这个通用的分页组件仍然可以一样使用。
 
##其它主要包含的组件：
 
- 比Apache BeanUtils更强大高效的Bean转换工具类`BeanConverter`。支持属性的过滤及实现自定义的类型转换器。
 
- office操作封装工具类`ExcelReadTools`和`ExcelWriteTools`。对程序员来说office操作最多的莫过于excel了，提供了对excel的读写实现。支持扩展实现自宝义的表格样式。
 
- 运行时的异常结果拦截器。`RunBinderInterceptor`可以和`RunBinderMvcInterceptor`配对使用(web项目)，`RunBinder`用以获取异常结果信息。
 
其它：
 
- 自行实现的轻量简易缓存。包括`LRUCache`，`LFUCache`，`FIFOCache`，目前只用到了LRUCache，可以使用已有的缓存工具类`CacheUtils`直接进行操作。
 
- 枚举操作工具类`EnumUtils`。结合当中定义的枚举接口`IEnum`进行操作，可以方便清晰的使用枚举处理及显示一些信息。
 
- 加密工具类`EncryptUtils`。目前只有MD5实现。
 
- Class工具类`ClassUtils`。获取BeanInfo，自身+指定父类BeanInfo，加载class，反射实例化对象等。
 
- 图片工具类`ImageUtils`。支持图片缩放，裁剪，加水印等。
 
- 名称及命名转换工具类`NameUtils`。提供下划线命名到骆驼命名的相互转换，及首字母大写、首字母小写、保留后缀生成唯一文件名等操作。
 
- 配置文件属性获取工具类`PropertyUtils`。默认优先从tomcat的conf目录获取，如果conf目录下没有则从classpath获取，方便部署时不用修改properties配置文件。
 
- 序列化工具类`SerializeUtils`。序列化和反序列化，jdk原生实现。
 
- 代码格式化工具类`SourceCodeFormatter`。使用了eclipse的组件，目前只能完美格式化Java代码，另外使用dom4j增加了xml的格式化。ps：本来打算站点用的后来感觉比较鸡肋了。
 
- 字符文本内容工具类`TextUtils`。一些StringUtils中没有的字符串操作，奈何StringUtils这类太多了，只能命名TextUtils了。
 
- 时间工具类`TimeUtils`。提供返回跟当前时间`几分钟前`,`几小时前`这种xxxx前的时间格式。
 
- UUID工具类`UUIDUtils`。提供返回8位、16位、32位的UUID。
 
- 线程执行工具类`ThreadExecutionUtils`。方便执行多线程任务。
 
- Spring mvc中针对上面定义的IEnum从页面字符值到枚举的转换类`IEnumConverterFactory`。


#相关链接

博客：http://www.dexcoder.com/selfly

作者邮箱： javaer@live.com

交流QQ群： 32261424
