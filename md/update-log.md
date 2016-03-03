##更新日志

### 版本 2.3.1 更新时间：2016-03-03

- 增加queryRowMap方法
- 增加queryObjectList方法
- 增加一系列执行自定义sql时返回实体重载方法
- 保持命名统一，querySingleRowMapForSql方法改名为querySingleResultForSql，queryRowMapListForSql方法改名为queryListForSql
- batis方式加载xml出错时提示信息细化
- 实体父类Pageable增加put、get、remove等方法方便操作时在实体不增加属性的情况下保存想要的数据
- Bean转换工具类BeanConverter增加map转实体bean方法
- 修正多个参数使用or时操作符有时会出错的bug
- 修正where条件使用in、not in等操作符当值只有一个时括号bug
- 修正使用动态数据源时，数据源属性不是String是出错的bug
- 修正动态数据源配置文件添加注释时导致出错bug
- 优化Excel处理工具类
- 修正Commons包中TextUtils替换特殊字符时数组越界bug
- 其它细节修正

### 版本 2.3.0 更新时间：2016-01-18

- 实体类表名及属性名映射增加注解支持
- 增加更多的执行自定义sql方法
- 因为sql权限问题去掉使用TRUNCATE的deleteAll方法
- 修正使用注解时注解的属性名不遵循规范时get方法主键错误问题
- 修正水平拆分数据分表不根据主键拆分时update无法获取表名的问题
- 修改NameHandler类名为MappingHandler

### 版本 2.2.0-beta1 更新时间：2015-12-23

- update更新支持选择是否更新值为null的属性

### 版本 2.1.0-beta1 更新时间：2015-12-22

- 增加表别名支持

具体请看：[增加表别名支持](http://www.dexcoder.com/selfly/article/4309)

### 版本：v2.0.0-beta1 更新时间：2015-12-15

- 本版进行了彻底的重构，提供了更好的扩展和更多的功能。
- 重构之前版本分支：[通用dao v1.2.4](https://github.com/selfly/dexcoder-assistant/tree/v1.2.4)

### 版本：V1.2.3 更新时间：2015-11-2 

- 增加读写分离，动态数据源支持

具体请看：[动态数据源的使用](http://www.dexcoder.com/selfly/article/4049 "通用JdbcDao更新，增加动态数据源，支持权重和读写分离")

### 版本：2015-10-27 更新时间：V1.1.2版本

- 增加了数据拆分时，水平分表的支持。

具体请看：[增加数据分表水平拆分支持](http://www.dexcoder.com/selfly/article/3857 "增加数据分表水平拆分支持")

### 版本：2015-10-9 更新时间：V1.0.2版本

- 增加了括号的支持，可以实现不同的属性在括号内or的情况：

具体请看：[通用JdbcDao更新，增加括号支持](http://www.dexcoder.com/selfly/article/3846 "通用JdbcDao更新，增加括号支持")