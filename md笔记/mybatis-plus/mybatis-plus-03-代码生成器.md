# 代码生成器

**MP的代码生成器和mybatis MBG代码生成器对比**

```properties
MP基于Java代码生成；MBG基于XML文件生成

mabatis代码生成器可以生成：
实体类、Mapper接口、Mapper映射文件

MP的代码生成器可以生成：
实体类（可以选择是否支持AR）、Mapper接口、Mapper映射文件、service层、controller层
```



**表及字段命名策略选择**

在MP中，我们建议数据库表名和表字段名采用驼峰命名方式，如果采用下划线命名方式请开启全局下划线开关，如果表名字段名命名方式不一致请注解指定，我们建议最好保持一致。



这么做的原因是为了避免在对应实体类时产生的性能损耗,这样字段不用做映射就能直接和实体类对应。当然如果项目里不用考虑这点性能损耗,那么你采用下滑线也是没问题的，只需要在生成代码时配置dbColumnUnderline属性就可以



# 代码生成器实例

```properties
1.全局配置
2.数据源配置
3.策略配置
4.包名策略配置
5.整合配置
6.执行
```

**实现步骤**

```java
public void CodeGenerator(){
    
    // 1.全局配置
    GlobalConfig glConfig = new GlobalConfig();
    // 是否支持AR模式
    glConfig.setActiveRecord(true)
        // 作者
        .setAuthor("ylx5")
        // 要生成的路径
        .setOutputDir("G:\\Project\\mpdemo-03\\src\\main\\java")
        // 多次生成是否进行文件覆盖
        .setFileOverride(true)
        // 主键策略
        .setIdType(IdType.AUTO)
        // 设置生成的service接口的名字的首字母是否为I，默认为 IUserService
        .setServiceName("%sService")
        // 生成一个基本的resultMap
        .setBaseResultMap(true)
        // 生成一个基本查询sql片段
        .setBaseColumnList(true);


    // 2.数据源配置
    DataSourceConfig dsConfig = new DataSourceConfig();
    // 数据库类型
    dsConfig.setDbType(DbType.MYSQL)
        .setDriverName("com.mysql.jdbc.Driver")
        .setUrl("jdbc:mysql://localhost:3306/mpdemo")
        .setUsername("root")
        .setPassword("root");


    // 3.策略配置
    StrategyConfig stConfig = new StrategyConfig();
    // 全局大小写命名
    stConfig.setCapitalMode(true)
        // 指定表名 字段名是否使用下划线
        .setColumnNaming(NamingStrategy.underline_to_camel)
        // 数据库表映射到实体的命名策略
        .setNaming(NamingStrategy.underline_to_camel)
        // 表名的前缀
        //.setTablePrefix("tbl_")
        // 生成实体类用到的表，多个表用‘,’隔开
        .setInclude("user");


    // 4.包名策略配置
    PackageConfig pkConfig = new PackageConfig();
    // 父包名
    pkConfig.setParent("com.ylx5.mp")
        // mapper放在mapper里
        .setMapper("mapper")
        .setService("service")
        .setController("controller")
        .setEntity("pojo")
        // xml文件
        .setXml("mapper");


    // 5.整合配置
    AutoGenerator ag = new AutoGenerator();
    ag.setGlobalConfig(glConfig)
        .setDataSource(dsConfig)
        .setStrategy(stConfig)
        .setPackageInfo(pkConfig);

    // 6.执行
    ag.execute();
}
```



**UserServiceImpl继承了 ServiceImpl**
**1.在ServiceImpl中 已经完成Mapper对象的注入,直接在UserServiceImpl中进行使用**
**2.在ServiceImpl中 也帮我们提供了常用的CRUD方法，基本的一些CRUD方法在Service中不需要我们自己定义**



所以在controller中可以直接注入UserService并且直接使用自带的方法了



# 代码生成器更多配置

```java
// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("jobob");
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/ant?useUnicode=true&useSSL=false&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("密码");
        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.baomidou.ant");
        mpg.setPackageInfo(pc);
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
```



# 使用教程

## 添加依赖

MyBatis-Plus 从 `3.0.3` 之后移除了代码生成器与模板引擎的默认依赖，需要手动添加相关依赖：

添加代码生成器依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>latest-version</version>
</dependency>
```

添加 模板引擎 依赖，MyBatis-Plus 支持 Velocity（默认）、Freemarker、Beetl，用户可以选择自己熟悉的模板引擎，如果都不满足您的要求，可以采用自定义模板引擎。

Velocity（默认）：

```xml
<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity-engine-core</artifactId>
    <version>latest-velocity-version</version>
</dependency>
```

Freemarker：

```xml
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>latest-freemarker-version</version>
</dependency>
```

Beetl：

```xml
<dependency>
    <groupId>com.ibeetl</groupId>
    <artifactId>beetl</artifactId>
    <version>latest-beetl-version</version>
</dependency>
```



注意！如果您选择了非默认引擎，需要在 AutoGenerator 中 设置模板引擎。

```java
AutoGenerator generator = new AutoGenerator();

// set freemarker engine
generator.setTemplateEngine(new FreemarkerTemplateEngine());

// set beetl engine
generator.setTemplateEngine(new BeetlTemplateEngine());

// set custom engine (reference class is your custom engine class)
generator.setTemplateEngine(new CustomTemplateEngine());

// other config
...
```



## 编写配置

MyBatis-Plus 的代码生成器提供了大量的自定义参数供用户选择，能够满足绝大部分人的使用需求。

### 配置 GlobalConfig

```java
GlobalConfig globalConfig = new GlobalConfig();
globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
globalConfig.setAuthor("jobob");
globalConfig.setOpen(false);
```



### 配置 DataSourceConfig

```java
DataSourceConfig dataSourceConfig = new DataSourceConfig();
dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/ant?useUnicode=true&useSSL=false&characterEncoding=utf8");
dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
dataSourceConfig.setUsername("root");
dataSourceConfig.setPassword("root");
```



### 自定义模板引擎

请继承类 com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine



### 自定义代码模板

```java
//指定自定义模板路径, 位置：/resources/templates/entity2.java.ftl(或者是.vm)
//注意不要带上.ftl(或者是.vm), 会根据使用的模板引擎自动识别
TemplateConfig templateConfig = new TemplateConfig()
    .setEntity("templates/entity2.java");
AutoGenerator mpg = new AutoGenerator();
//配置自定义模板
mpg.setTemplate(templateConfig);
```



### 自定义属性注入

```java
InjectionConfig injectionConfig = new InjectionConfig() {
    //自定义属性注入:abc
    //在.ftl(或者是.vm)模板中，通过${cfg.abc}获取属性
    @Override
    public void initMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
        this.setMap(map);
    }
};
AutoGenerator mpg = new AutoGenerator();
//配置自定义属性注入
mpg.setCfg(injectionConfig);
```

```java
entity2.java.ftl
自定义属性注入abc=${cfg.abc}
entity2.java.vm
自定义属性注入abc=$!{cfg.abc}
```



### 字段其他信息查询注入

```java
new DataSourceConfig().setDbQuery(new MySqlQuery() {
    /**
     * 重写父类预留查询自定义字段<br>
     * 这里查询的 SQL 对应父类 tableFieldsSql 的查询字段，默认不能满足你的需求请重写它<br>
     * 模板中调用：  table.fields 获取所有字段信息，
     * 然后循环字段获取 field.customMap 从 MAP 中获取注入字段如下  NULL 或者 PRIVILEGES
     */
    @Override
    public String[] fieldCustom() {
        return new String[]{"NULL", "PRIVILEGES"};
    }
})
```



# 代码生成器配置

## 基本配置

### dataSource

- 类型：`DataSourceConfig`
- 默认值：`null`

数据源配置，通过该配置，指定需要生成代码的具体数据库，具体请查看数据源配置

### strategy

- 类型：`StrategyConfig`
- 默认值：`null`

数据库表配置，通过该配置，可指定需要生成哪些表或者排除哪些表，具体请查看数据库表配置

### packageInfo

- 类型：`PackageConfig`
- 默认值：`null`

包名配置，通过该配置，指定生成代码的包路径，具体请查看包名配置

### template

- 类型：`TemplateConfig`
- 默认值：`null`

模板配置，可自定义代码生成的模板，实现个性化操作，具体请查看模板配置

### globalConfig

- 类型：`GlobalConfig`
- 默认值：`null`

全局策略配置，具体请查看全局策略配置

### injectionConfig

- 类型：`InjectionConfig`
- 默认值：`null`

注入配置，通过该配置，可注入自定义参数等操作以实现个性化操作，具体请查看注入配置



## 数据源 `dataSourceConfig` 配置

### dbQuery

- 数据库信息查询类
- 默认由 `dbType` 类型决定选择对应数据库内置实现

实现 `IDbQuery` 接口自定义数据库查询 `SQL 语句` 定制化返回自己需要的内容

### dbType

- 数据库类型
- 该类内置了常用的数据库类型【必须】

### schemaName

- 数据库 schema name
- 例如 `PostgreSQL` 可指定为 `public`

### typeConvert

- 类型转换
- 默认由 `dbType` 类型决定选择对应数据库内置实现

实现 `ITypeConvert` 接口自定义数据库 `字段类型` 转换为自己需要的 `java` 类型，内置转换类型无法满足可实现 `IColumnType` 接口自定义

### url

- 驱动连接的URL

### driverName

- 驱动名称

### username

- 数据库连接用户名

### password

- 数据库连接密码

## 数据库表配置

### isCapitalMode

是否大写命名

### skipView

是否跳过视图

### naming

数据库表映射到实体的命名策略

### columnNaming

数据库表字段映射到实体的命名策略, 未指定按照 naming 执行

### tablePrefix

表前缀

### fieldPrefix

字段前缀

### superEntityClass

自定义继承的Entity类全称，带包名

### superEntityColumns

自定义基础的Entity类，公共字段

### superMapperClass

自定义继承的Mapper类全称，带包名

### superServiceClass

自定义继承的Service类全称，带包名

### superServiceImplClass

自定义继承的ServiceImpl类全称，带包名

### superControllerClass

自定义继承的Controller类全称，带包名

### enableSqlFilter（since 3.3.1）

默认激活进行sql模糊表名匹配

关闭之后likeTable与notLikeTable将失效，include和exclude将使用内存过滤

如果有sql语法兼容性问题的话，请手动设置为false

已知无法使用：微软系，达梦，MyCat中间件，支持情况传送门

### include

需要包含的表名，当enableSqlFilter为false时，允许正则表达式（与exclude二选一配置）

### likeTable

自3.3.0起，模糊匹配表名（与notLikeTable二选一配置）

### exclude

需要排除的表名，当enableSqlFilter为false时，允许正则表达式

### notLikeTable

自3.3.0起，模糊排除表名

### entityColumnConstant

【实体】是否生成字段常量（默认 false）

### entityBuilderModel

【实体】是否为构建者模型（默认 false），自3.3.2开始更名为chainModel

### chainModel（since 3.3.2）

【实体】是否为链式模型（默认 false）

### entityLombokModel

【实体】是否为lombok模型（默认 false）

3.3.2以下版本默认生成了链式模型，3.3.2以后，默认不生成，如有需要，请开启chainModel

### entityBooleanColumnRemoveIsPrefix

Boolean类型字段是否移除is前缀（默认 false）

### restControllerStyle

生成 `@RestController` 控制器

### controllerMappingHyphenStyle

驼峰转连字符

### entityTableFieldAnnotationEnable

是否生成实体时，生成字段注解

### versionFieldName

乐观锁属性名称

### logicDeleteFieldName

逻辑删除属性名称

### tableFillList

表填充字段

## 包名配置

### parent

父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名

### moduleName

父包模块名

### entity

Entity包名

### service

Service包名

### serviceImpl

Service Impl包名

### mapper

Mapper包名

### xml

Mapper XML包名

### controller

Controller包名

### pathInfo

路径配置信息

## 模板配置

### entity

Java 实体类模板

### entityKt

Kotin 实体类模板

### service

Service 类模板

### serviceImpl

Service impl 实现类模板

### mapper

mapper 模板

### xml

mapper xml 模板

### controller

controller 控制器模板

## 全局策略 `globalConfig` 配置

### outputDir

- 生成文件的输出目录
- 默认值：`D 盘根目录`

### fileOverride

- 是否覆盖已有文件
- 默认值：`false`

### open

- 是否打开输出目录
- 默认值：`true`

### enableCache

- 是否在xml中添加二级缓存配置
- 默认值：`false

### author

- 开发人员
- 默认值：`null`

### kotlin

- 开启 Kotlin 模式
- 默认值：`false`

### swagger2

- 开启 swagger2 模式
- 默认值：`false`

### activeRecord

- 开启 ActiveRecord 模式
- 默认值：`false`

### baseResultMap

- 开启 BaseResultMap
- 默认值：`false`

### baseColumnList

- 开启 baseColumnList
- 默认值：`false`

### dateType

- 时间类型对应策略
- 默认值：`TIME_PACK`

注意事项:

如下配置 `%s` 为占位符

### entityName

- 实体命名方式
- 默认值：`null` 例如：`%sEntity` 生成 `UserEntity`

### mapperName

- mapper 命名方式
- 默认值：`null` 例如：`%sDao` 生成 `UserDao`

### xmlName

- Mapper xml 命名方式
- 默认值：`null` 例如：`%sDao` 生成 `UserDao.xml`

### serviceName

- service 命名方式
- 默认值：`null` 例如：`%sBusiness` 生成 `UserBusiness`

### serviceImplName

- service impl 命名方式
- 默认值：`null` 例如：`%sBusinessImpl` 生成 `UserBusinessImpl`

### controllerName

- controller 命名方式
- 默认值：`null` 例如：`%sAction` 生成 `UserAction`

### idType

- 指定生成的主键的ID类型
- 默认值：`null`

## 注入 `injectionConfig` 配置

### map

- 自定义返回配置 Map 对象
- 该对象可以传递到模板引擎通过 `cfg.xxx` 引用

### fileOutConfigList

- 自定义输出文件
- 配置 `FileOutConfig` 指定模板文件、输出文件达到自定义文件生成目的

### fileCreate

- 自定义判断是否创建文件
- 实现 `IFileCreate` 接口

该配置用于判断某个类是否需要覆盖创建，当然你可以自己实现差异算法 `merge` 文件

### initMap

- 注入自定义 Map 对象(注意需要setMap放进去)