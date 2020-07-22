# 通用`Mapper`

# 查询`Retrieve`

## 基本查询

```java
// 根据 ID 查询
T selectById(Serializable id);
```

根据ID查询



```java
/**
 * 查询（根据ID 批量查询）
 *
 * @param idList 主键ID列表(不能为 null 以及 empty)
 */
List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

// 数据库中只有1,2,3,4,5 所以只能查到五条数据
List<Long> idList = Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L);
List<User> users = userMapper.selectBatchIds(idList);
users.forEach(System.out::println);
```

根据ID的List查询，匹配的id就查到，没有的就不显示

生成的SQL是`where id in (...)`



```java
/**
 * 查询（根据 columnMap 条件）
 *
 * @param columnMap 表字段 map 对象
 */
List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

// 下面这样的两个put，条件会用and连接
// WHERE name = 'Jone' AND age = 20 
Map<String, Object> map = new HashMap<>();
map.put("name","Jone");
map.put("age",20);
List<User> users = userMapper.selectByMap(map);
users.forEach(System.out::println);
```

根据Map查询，Map中key存的是表字段，value存的是字段的值

多个条件用and连接



## 条件构造器查询

**需求：名字中包含雨并且年龄小于40**

`name like '%雨%' and age<40`

```java
// 以下两条语句都可以创建出一个条件构造器，作用一样
QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//QueryWrapper<User> query = Wrappers.<User>query();

// 条件构造器
queryWrapper.like("name", "雨").lt("age", 40);

List<User> users = userMapper.selectList(queryWrapper);
users.forEach(System.out::println);
```



**需求：名字中包含雨年并且龄大于等于20且小于等于40并且email不为空**

`name like '%雨%' and age between 20 and 40 and email is not null`

```java
queryWrapper.like("name", "雨").between("age", 20, 40).isNotNull("email");
```

**条件构造器链式编程的话，条件会用and连接**



**需求：名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列**

`name like '王%' or age>=25 order by age desc,id asc`

```java
queryWrapper.likeRight("name", "王").or().ge("age",25).orderByDesc("age").orderByAsc("id");
```

需要或者or的时候，就调用一个`.or()`排序的话就调用方法`.orderByDesc()`参数为要排序的字段

