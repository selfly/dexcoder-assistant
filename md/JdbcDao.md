##基于Spring JdbcTemplate的通用dao使用说明

>很多人问如果我要实现一个自己的dao是不是要继承这个dao然后实现自己的方法？其实不用，自己实现的dao按spring标准的JdbcTemplate路子走就可以，完全无耦合。
>另外自己实现的dao这个通用的分页组件仍然可以一样使用。

如果你不喜欢用`Hibernate`、`Mybaits`这类ORM框架，喜欢`JdbcTemplate`或`DbUtils`，那么可以试试这个封装的通用dao，这可能是目前封装的最方便的通用dao了。

该通用dao是在使用过程中，针对常规的泛型dao经常遇到的一些不便问题进行了改进。命名上遵循了约定优于配置的原则，典型约定如下：

- 表名约定 `USER_INFO`表实体类名为`UserInfo`。
- 字段名约定 `USER_NAME`实体类中属性名为`userName`。
- 主键名约定 `USER_INFO`表主键名为`USER_INFO_ID`，同理实体类中属性名为`userInfoId`。
- `Oracle`序列名约定 `USER_INFO`表对应的主键序列名为`SEQ_USER_INFO`

当然，这些你可以在扩展中改变它，但不建议这么做，这本身就是一个良好的规范。

目前除了具有常规泛型dao的功能外还具有如下特性：

1. 一个dao可以操作所有的实体类，不必再一个个建立跟实体对应的继承于类似BaseDao这类“通用dao”了。
2. 各类方法参数支持`Entity`和`Criteria`两种方式。
3. sql的where条件支持不等于`(column != value)`、等于多个值`(column = value1 or column = value2)`和in的方式`(column in (value1,value2,...))`。
4. 允许在查询时指定使用哪个字段进行排序，可以指定多个进行组合升降序自由排序。
5. 支持在查询时指定返回字段的白名单和黑名单，可以指定只返回某些字段或不返回某个大字段。
6. 方便强大的分页功能，无须额外操作，二三行代码搞定分页，自动判断数据库，无须指定。

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
