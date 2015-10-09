#Dexcoder快速开发辅助工具包
 
此工具包旨在实现一些常用且通用的工具类，主要为本人做个人开发时所用。欢迎大家一起来完善它！
 
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

网站：http://www.dexcoder.com

作者邮箱： javaer@live.com

交流QQ群： 32261424

[link_jdbc_dao_desc]: md/JdbcDao.md "JdbcDao使用说明"