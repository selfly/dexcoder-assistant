#Dexcoder快速开发辅助工具包

##核心组件dexcoder-dal使用说明

如果你不喜欢用`Hibernate`、`Mybaits`这类ORM框架，喜欢`JdbcTemplate`或`DbUtils`，那么可以试试这个封装的通用dal，这可能是目前封装的最方便的通用dal层了。

dexcoder-dal的一些特性：

1. 一个dao即可以搞定所有的实体类，不必再一个个建立跟实体对应的继承于类似BaseDao这类“通用dao”了。
2. 各类方法参数除了`Entity`外，支持更强大的`Criteria`方式。
3. sql的where条件支持一些复杂的条件，如`等于`、`不等于`、`or`、`in`、`not in`甚至是执行函数。
4. 允许在查询时指定使用哪个字段进行排序，可以指定多个进行组合升降序自由排序。
5. 支持在查询时指定返回字段的白名单和黑名单，可以指定只返回某些字段或不返回某些字段。
6. select查询时支持函数，`count()`、`max()`、`to_char()`等等,理论上都可以支持。
6. 方便强大的分页功能，无须额外操作，二三行代码搞定分页，自动判断数据库，无须指定。

该通用dao是在使用过程中，针对常规的泛型dao经常遇到的一些不便问题进行了改进。命名上遵循了约定优于配置的原则，典型约定如下：

- 表名约定 `USER_INFO`表实体类名为`UserInfo`。
- 字段名约定 `USER_NAME`实体类中属性名为`userName`。
- 主键名约定 `USER_INFO`表主键名为`USER_INFO_ID`，同理实体类中属性名为`userInfoId`。
- `Oracle`序列名约定 `USER_INFO`表对应的主键序列名为`SEQ_USER_INFO`

当然，这些你可以在扩展中改变它，但不建议这么做，这本身就是一个良好的规范。

目前除了具有常规泛型dao的功能外还具有如下特性：



要在项目中使用通用dao十分简单，只需要在spring的配置文件中声明如下bean：

    <bean id="jdbcDao" class="com.dexcoder.dal.spring.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>
    <!--需要分页时声明-->
    <bean id="pageControl" class="com.dexcoder.assistant.interceptor.PageControl"/>

接下来就可以注入到您的`Service`或者其它类中使用了。

##下面是一些常用的方法示例，这里的`Entity`对象为`User`，对于任何的`Entity`都是一样的.

先来看一下`User`对象及它继承的`Pageable`

	public class User extends Pageable {
		private Long    userId;
		private String  userName;
		private Integer userAge;
		private Date    gmtCreate;
		private Date    gmtModify;
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

###insert操作
    public void insert() {
        User user = new User();
        user.setUserName("liyd");
        user.setUserAge(20);
        user.setGmtCreate(new Date());
        Long id = jdbcDao.insert(user);
        System.out.println(id);
    }

	public void insert2() {
		Criteria criteria = Criteria.create(User.class).set("userName", "liyd22")
			.set("userAge", 22).set("gmtCreate", new Date());
		Long id = jdbcDao.insert(criteria);
		System.out.println(id);
    }

###save操作，和insert的区别在于不处理主键，由调用者指定
    public void save() {
        user.setUserId(-123L);
        jdbcDao.save(user);
    }

	public void save2() {
        Criteria criteria = Criteria.create(User.class).set("userId", -122L)
            .set("userName", "liyd22").set("userAge", 22).set("gmtCreate", new Date())
            .set("gmtModify", new Date());
        jdbcDao.save(criteria);
    }

###update操作
    public void update() {
        user.setUserId(34L);
        user.setUserName("liyd34");
        user.setGmtCreate(null);
        user.setGmtModify(new Date());
        jdbcDao.update(user);
    }

	public void update2() {
        //这里where条件userId传入了多个值，会生成in语句
        Criteria criteria = Criteria.create(User.class).set("userName", "liydCriteria")
            .set("userAge", "18").where("userId", new Object[] { 34L, 33L, 32L });
        jdbcDao.update(criteria);
    }

###get操作
    public void get1() {
        //根据主键
        User u = jdbcDao.get(User.class, 23L);
    }

	public void get2() {
        //criteria，主要用来指定字段白名单、黑名单等
        Criteria criteria = Criteria.create(User.class).include("userName");
        User u = jdbcDao.get(criteria, 23L);
    }

###delete操作
	public void delete() {
        //会把不为空的属性做为where条件
        User u = new User();
        u.setUserName("selfly");
        u.setUserAge(16);
        jdbcDao.delete(u);
    }

    public void delete2() {
        //where条件使用了or
        Criteria criteria = Criteria.create(User.class).where("userName", new Object[] { "liyd2" })
            .or("userAge", new Object[]{64});
        jdbcDao.delete(criteria);
    }

    public void delete3() {
        //根据主键
        jdbcDao.delete(User.class, 25L);
    }

###列表查询操作
	public void queryList1() {
        //以不为空的属性作为查询条件
        User u = new User();
        u.setUserName("liyd");
        List<User> users = jdbcDao.queryList(u);
    }

    public void queryList2() {
        //Criteria方式
        Criteria criteria = Criteria.create(User.class).exclude("userId")
            .where("userName", new Object[]{"liyd"});
        List<User> users = jdbcDao.queryList(criteria);
    }

    public void queryList3() {
        //使用了like，可以换成!=、in、not in等
        Criteria criteria = Criteria.create(User.class).where("userName", "like",
            new Object[] { "%liyd%" });
        user.setUserAge(16);
        //这里entity跟criteria方式混合使用了，建议少用
        List<User> users = jdbcDao.queryList(user, criteria.include("userId"));
    }

    public void queryList4() {
        //不指定条件即查询出所有
        List<User> users = jdbcDao.queryList(Criteria.create(User.class));
    }

###count记录数查询，除了返回值不一样外，其它和列表查询一致
    public void queryCount() {
        user.setUserName("liyd");
        int count = jdbcDao.queryCount(user);
    }

    public void queryCount2() {
        Criteria criteria = Criteria.create(User.class).where("userName", new Object[] { "liyd" })
            .or("userAge", new Object[]{27});
        int count = jdbcDao.queryCount(criteria);
    }

###查询单个结果
    public void querySingleResult() {
        user = jdbcDao.querySingleResult(user);
    }

    public void querySingleResult2() {
        Criteria criteria = Criteria.create(User.class).where("userName", new Object[] { "liyd" })
            .and("userId", new Object[]{23L});
        User u = jdbcDao.querySingleResult(criteria);
    }

###指定字段白名单，在任何查询方法中都可以使用
    public void get(){
        //将只返回userName
        Criteria criteria = Criteria.create(User.class).include("userName");
        User u = jdbcDao.get(criteria, 23L);
    }

###指定字段黑名单，在任何查询方法中都可以使用
	public void get4(){
        //将不返回userName
        Criteria criteria = Criteria.create(User.class).exclude("userName");
        User u = jdbcDao.get(criteria, 23L);
    }

###指定排序
    public void queryList() {
        //指定多个排序字段，asc、desc
        Criteria criteria = Criteria.create(User.class).exclude("userId")
            .where("userName", new Object[]{"liyd"}).asc("userId").desc("userAge");
        List<User> users = jdbcDao.queryList(criteria);
    }

###分页
	public void queryList1() {
		//进行分页
		PageControl.performPage(user);
		//分页后该方法即返回null，由PageControl中获取
		jdbcDao.queryList(user);
		Pager pager = PageControl.getPager();
		//列表
		List<User> users = pager.getList(User.class);
		//总记录数
		int itemsTotal = pager.getItemsTotal();
	}

	public void queryList2() {
		//直接传入页码和每页条数
		PageControl.performPage(1, 10);
		//使用Criteria方式，并指定排序字段方式为asc
		Criteria criteria = Criteria.create(User.class).include("userName", "userId")
			.where("userName", new Object[]{"liyd"}).asc("userId");
		jdbcDao.queryList(criteria);
		Pager pager = PageControl.getPager();
	}

### 不同的属性在括号内or的情况：

        Criteria criteria = Criteria.create(User.class)
            .where("userType", new Object[] { "1" }).beginBracket()
            .and("loginName", new Object[] { "selfly" })
            .or("email", new Object[] { "javaer@live.com" }).endBracket()
            .and("password", new Object[] { "123456" });
        User user = jdbcDao.querySingleResult(criteria);

###一些说明

JdbcDao在声明时可以根据需要注入其它几个参数：

    <bean id="jdbcDao" class="com.dexcoder.dal.spring.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
        <property name="nameHandler" ref="..."/>
        <property name="rowMapperClass" value="..."/>
        <property name="dialect" value="..."/>
    </bean>

- nameHandler 默认使用DefaultNameHandler，即遵守上面的约定优于配置，如果需要自定义可以实现该接口。
- rowMapperClass 默认使用了spring的`BeanPropertyRowMapper.newInstance(clazz)`,需要自定义可以自行实现，标准spring的RowMapper实现即可。
- dialect 默认为自增类主键如MySql，其它类型请指定数据库如oracle




>很多人问如果我要实现一个自己的dao是不是要继承这个dao然后实现自己的方法？其实不用，自己实现的dao按spring标准的JdbcTemplate路子走就可以，完全无耦合。
>另外自己实现的dao这个通用的分页组件仍然可以一样使用。











此工具包旨在实现一些常用且通用的工具类，主要为本人做个人开发时所用。
 
一些工具类功能可能不够齐全，例如加密目前只有MD5，这是因为本人在使用过程中只用到了MD5，其它的也类似，只求够用就好。
 
目前主要包含的组件：
 
- [基于Spring JdbcTemplate的通用dao][link_jdbc_dao_desc]。`JdbcDao`和分页组件`PageControl`结合使用，可以快速的实现增删改查操作及分页。
 
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
 
- Sitemap工具类`SiteMapUtils`。做网站的可能会用到，生成sitemap用。
 
- Spring mvc中针对上面定义的IEnum从页面字符值到枚举的转换类`IEnumConverterFactory`。


#相关链接

博客：http://www.dexcoder.com/selfly

作者邮箱： javaer@live.com

交流QQ群： 32261424

[link_jdbc_dao_desc]: md/JdbcDao.md "JdbcDao使用说明"
