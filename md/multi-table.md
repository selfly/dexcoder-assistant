数据水平拆分的分表，很难做到通用，因为它往往是跟业务紧密结合的。

以书籍和章节为例，书籍可以简单的根据主键id(这里主键为数字类型)和表数量取模，根据取模得到的结果插入不同的表中。

但是章节就不能也根据主键了，如果还是按照书籍的做法，那么当需要查询一本书的目录时就要遍历所有的章节表，显然是不合理的。比较好的做法当然也是根据书籍id(这里应该是外键)拆分，这样一本书的所有章节就在同一张表中，列表、分页等查询也会相对的方便很多。

dexcoder-dal的做法是在需要进行水平分表时，自定义实现一个MappingHandler，由用户自己来决定根据哪个字段哪种规则来确定具体操作的表名。

## 一个简单的示例

### 创建表及对应的实体类

这里我们创建了3张书籍表和5张章节表，用来存放拆分后的数据。

对应的实体类就不贴了，字段参考表结构。要知道的是只有Book和Chapter两个实体类，并不是一张表对应一个。

    -- 以下为书籍表
    CREATE TABLE `TEST`.`BOOK_0` (
            `BOOK_ID` BIGINT(19) NOT NULL COMMENT '书籍ID',
            `BOOK_NAME` VARCHAR(45) NOT NULL COMMENT '书籍名称',
            `GMT_CREATE` DATETIME NOT NULL COMMENT '创建时间',
            PRIMARY KEY (`BOOK_ID`))
            COMMENT = '书籍表';
    CREATE TABLE `TEST`.`BOOK_1` (
            `BOOK_ID` BIGINT(19) NOT NULL COMMENT '书籍ID',
            `BOOK_NAME` VARCHAR(45) NOT NULL COMMENT '书籍名称',
            `GMT_CREATE` DATETIME NOT NULL COMMENT '创建时间',
            PRIMARY KEY (`BOOK_ID`))
            COMMENT = '书籍表';
    CREATE TABLE `TEST`.`BOOK_2` (
            `BOOK_ID` BIGINT(19) NOT NULL COMMENT '书籍ID',
            `BOOK_NAME` VARCHAR(45) NOT NULL COMMENT '书籍名称',
            `GMT_CREATE` DATETIME NOT NULL COMMENT '创建时间',
            PRIMARY KEY (`BOOK_ID`))
            COMMENT = '书籍表';

    -- 以下为章节表
    CREATE TABLE `TEST`.`CHAPTER_0` (
            `CHAPTER_ID` BIGINT(19) NOT NULL COMMENT '章节ID',
            `BOOK_ID` BIGINT(19) NOT NULL COMMENT '书籍ID',
            `CHAPTER_NAME` VARCHAR(45) NOT NULL COMMENT '章节名称',
            `GMT_CREATE` DATETIME NOT NULL COMMENT '创建时间',
            PRIMARY KEY (`CHAPTER_ID`))
            COMMENT = '章节表';
    CREATE TABLE `TEST`.`CHAPTER_1` (
            `CHAPTER_ID` BIGINT(19) NOT NULL COMMENT '章节ID',
            `BOOK_ID` BIGINT(19) NOT NULL COMMENT '书籍ID',
            `CHAPTER_NAME` VARCHAR(45) NOT NULL COMMENT '章节名称',
            `GMT_CREATE` DATETIME NOT NULL COMMENT '创建时间',
            PRIMARY KEY (`CHAPTER_ID`))
            COMMENT = '章节表';
    CREATE TABLE `TEST`.`CHAPTER_2` (
            `CHAPTER_ID` BIGINT(19) NOT NULL COMMENT '章节ID',
            `BOOK_ID` BIGINT(19) NOT NULL COMMENT '书籍ID',
            `CHAPTER_NAME` VARCHAR(45) NOT NULL COMMENT '章节名称',
            `GMT_CREATE` DATETIME NOT NULL COMMENT '创建时间',
            PRIMARY KEY (`CHAPTER_ID`))
            COMMENT = '章节表';
    CREATE TABLE `TEST`.`CHAPTER_3` (
            `CHAPTER_ID` BIGINT(19) NOT NULL COMMENT '章节ID',
            `BOOK_ID` BIGINT(19) NOT NULL COMMENT '书籍ID',
            `CHAPTER_NAME` VARCHAR(45) NOT NULL COMMENT '章节名称',
            `GMT_CREATE` DATETIME NOT NULL COMMENT '创建时间',
            PRIMARY KEY (`CHAPTER_ID`))
            COMMENT = '章节表';
    CREATE TABLE `TEST`.`CHAPTER_4` (
            `CHAPTER_ID` BIGINT(19) NOT NULL COMMENT '章节ID',
            `BOOK_ID` BIGINT(19) NOT NULL COMMENT '书籍ID',
            `CHAPTER_NAME` VARCHAR(45) NOT NULL COMMENT '章节名称',
            `GMT_CREATE` DATETIME NOT NULL COMMENT '创建时间',
            PRIMARY KEY (`CHAPTER_ID`))
            COMMENT = '章节表';

### 实现自定义的MappingHandler

数据水平拆分说的简单点就是将不同的数据根据指定的规则插入到不同表中，在MappingHandler中我们只要根据规则返回相应的表名就可以了。

主要的实现在于getTableName方法，参数`Map<String, AutoField> fieldMap`里面包含了当次操作的所有属性和值，分表肯定是根据某个或多个属性来确定规则的，既然拿到了所有的属性和值就能根据自己想要的方式来进行表的拆分了，这里的key就是属性名。

参考下面代码：

    public String getTableName(Class<?> entityClass, Map<String, AutoField> fieldMap) {
        //Java属性的骆驼命名法转换回数据库下划线“_”分隔的格式
        String tableName = NameUtils.getUnderlineName(entityClass.getSimpleName());
        if (Book.class.equals(entityClass)) {
            AutoField autoField = fieldMap.get("bookId");
            if (autoField == null || autoField.getValue() == null) {
                throw new AssistantException("书籍bookId不能为空");
            }
            if (!(autoField.getValue() instanceof Long)) {
                throw new AssistantException("书籍bookId错误");
            }
            Long id = (Long) autoField.getValue();
            //书籍3张表
            long tableNum = id % 3;
            return tableName + "_" + tableNum;
        } else if (Chapter.class.equals(entityClass)) {
            AutoField autoField = fieldMap.get("bookId");
            if (autoField == null || autoField.getValue() == null) {
                throw new AssistantException("章节bookId不能为空");
            }
            if (!(autoField.getValue() instanceof Long)) {
                throw new AssistantException("书籍bookId错误");
            }
            Long id = (Long) autoField.getValue();
            //章节5张表
            long tableNum = id % 5;
            return tableName + "_" + tableNum;
        }
        return tableName;
    }


要使用自定义的MappingHandler只要在声明JdbcDao时注入就可以了

    <bean id="customMappingHandler" class="com.dexcoder.assistant.persistence.CustomMappingHandler"></bean>
    <bean id="jdbcDao" class="com.dexcoder.assistant.persistence.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
        <property name="mappingHandler" ref="customMappingHandler"/>
    </bean>

### 编写测试方法

先插入书籍信息，简单起见主键为1-50，注意这里用的是save指定主键而不是用insert由数据库自动生成主键：

    @Test
    public void multiTableBook() {
        for (int i = 1; i < 51; i++) {
            Book book = new Book();
            book.setBookId((long) i);
            book.setBookName("测试book" + i);
            book.setGmtCreate(new Date());
            jdbcDao.save(book);
        }
    }

查看数据，发现已经被存到不同的表里了。

插入章节信息：

    @Test
    public void multiTableChapter() {
        for (int i = 1; i < 51; i++) {
            Chapter chapter = new Chapter();
            chapter.setChapterId((long) i);
            chapter.setBookId(5L);
            chapter.setChapterName("章节一" + i);
            chapter.setGmtCreate(new Date());
            jdbcDao.save(chapter);
        }

        for (int i = 51; i < 101; i++) {
            Chapter chapter = new Chapter();
            chapter.setChapterId((long) i);
            chapter.setBookId(6L);
            chapter.setChapterName("章节二" + i);
            chapter.setGmtCreate(new Date());
            jdbcDao.save(chapter);
        }
    }

章节是根据bookId拆分的，章节一部分bookId是5，数据应该在chapter_0表中。章节二部分bookId是6，数据应该在chapter_1表中，而其它表为空。

查询数据库，发现确实如我们想象的一样，说明分表正确！

插入数据成功了，那么查询、更新和删除呢？以下测试代码：

    @Test
    public void multiTableChapterQuery() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(22L);
        chapter.setBookId(5L);
        chapter = jdbcDao.querySingleResult(chapter);
        Assert.assertNotNull(chapter);
        chapter = jdbcDao.querySingleResult(Criteria.select(Chapter.class).where("chapterId", new Object[] { 67L })
            .and("bookId", new Object[] { 6L }));
        Assert.assertNotNull(chapter);
    }

    @Test
    public void multiTableChapterUpdate() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(22L);
        chapter.setBookId(5L);
        chapter.setChapterName("updateChapter");
        jdbcDao.update(chapter);

        Chapter tmp = jdbcDao.querySingleResult(Criteria.select(Chapter.class).where("chapterId", new Object[] { 22L })
            .and("bookId", new Object[] { 5L }));
        Assert.assertEquals("updateChapter", tmp.getChapterName());
    }

    @Test
    public void multiTableChapterDelete() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(23L);
        chapter.setBookId(5L);
        jdbcDao.delete(chapter);

        Chapter tmp = jdbcDao.querySingleResult(Criteria.select(Chapter.class).where("chapterId", new Object[] { 23L })
            .and("bookId", new Object[] { 5L }));
        Assert.assertNull(tmp);
    }

上面代码均成功运行且结果与我们预想的一致！

到这里我们想要的数据水平拆分的功能就基本完成了，可以根据自己的需要确定拆分规则，如根据时间段拆分等。

或许有人会说如果我要拆分1000张表那岂不是在NameHandler中要判断1000个实体类？确实，这个是没有办法的，不管你用反射还是其它方法判断总是要的。

但如果你真的有1000张表要拆分，那么你应该先考虑垂直拆分而不是水平拆分。通常在一个项目中要拆分的表数量应该不多，毕竟不是每张表的数据量都有那么大的！