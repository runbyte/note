# mybatis-plus

**此笔记只用于补充个人理解，具体参考`MP使用教程.epub`或者`MP使用教程.pdf`**



## 快速开始

### 数据库脚本

```sql
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

DELETE FROM user;

INSERT INTO user (id, name, age, email) VALUES
(1, 'Jone', 18, 'test1@baomidou.com'),
(2, 'Jack', 20, 'test2@baomidou.com'),
(3, 'Tom', 28, 'test3@baomidou.com'),
(4, 'Sandy', 21, 'test4@baomidou.com'),
(5, 'Billie', 24, 'test5@baomidou.com');
```

### pom.xml

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.1.RELEASE</version>
    <relativePath/>
</parent>

<dependencies>
    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.3.2</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
    </dependency>
</dependencies>

```

### application.properties

```properties
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/mpdemo?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
```

### 新建pojo

```java
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

### 新建UserMapper

```java
// 继承BaseMapper即可传入实体泛型
public interface UserMapper extends BaseMapper<User> {
}
```

### Test类中测试

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMpDemo {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

}
```

### 总结

大大简化了开发，无需手动写SQL以及XML文件，BaseMapper提供了大量的常用方法



## 开始学习

`insert`新增方法：`UserMapper.insert(user)`

注意：当实体某个属性为空时，MP不会将该字段属性体现在SQL语句中。但是当主键ID为空时，MP会基于雪花算法给一个ID插入到数据库中



### 注解

#### `@TableName`

表名注解，当表名跟实体类名不一致的时候

在实体类上加上此注解，并且参数写表名（无论是否一致，都建议写上）

```java
@TableName("mp_user")
public class User{
    private Long userId;
    private String name;
    private Integer age;
}
```



#### `@TableId`

主键注解，主键字段名

在主键对应的字段上加上次注解，并且参数写主键字段名（无论是否一致，都建议写上）

```java
@TableName("mp_user")
public class User{
    @TableId("user_id")
    private Long userId;
    private String name;
    private Integer age;
}
```



#### `@TableField`

非主键字段注解，当字段名和属性名不一致的时候使用



#### 排除非表字段

实体类有属性，但是表中没有字段，直接插入会报错

三种方案：

1.修改字段，新增关键字`transient`，表示该字段不参与序列化过程

`private transient String desc;`



2.修改字段，改为`static`方法,mp会忽略static字段，生成SQL时，不会参与增删

`private static String desc;`



3.【推荐】属性上添加注解，将字段的exist属性改成false，标识该字段不存在表中，完美解决

`@TableField(exist = false)`

