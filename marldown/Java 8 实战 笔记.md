# 行为参数化

行为参数化，就是一个方法接收多个不同的行为作为参数，并在内部使用它们，完成不同行为的能力。

**行为参数化可让代码更好地适应不断变化的要求，减轻未来的工作量。**

传递代码，就是将新行为作为参数传递给方法。但在Java 8之前这实现起来很啰嗦。为接口声明许多只用一次的实体类而造成的啰嗦代码，在Java 8之前可以用匿名类来减少。

Java API包含很多可以用不同行为进行参数化的方法，包括排序、线程和GUI处理。

案例：

用 Comparator来排序

```java
// java.util.Comparator 
public interface Comparator<T> { 
    public int compare(T o1, T o2); 
}

// 库存排序
inventory.sort(new Comparator<Apple>() { 
    public int compare(Apple a1, Apple a2){ 
        return a1.getWeight().compareTo(a2.getWeight()); 
    } 
});

// Lambda表达式
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
```



用 Runnable 执行代码块

```java
// java.lang.Runnable 
public interface Runnable{ 
    public void run(); 
} 

Thread t = new Thread(new Runnable() { 
    public void run(){ 
        System.out.println("Hello world"); 
    } 
}); 

// Lambda表达式
Thread t = new Thread(() -> System.out.println("Hello world"));
```



GUI 事件处理 

```java
Button button = new Button("Send"); 
button.setOnAction(new EventHandler<ActionEvent>() { 
    public void handle(ActionEvent event) { 
        label.setText("Sent!!"); 
    } 
});

// Lambda表达式
button.setOnAction((ActionEvent event) -> label.setText("Sent!!")); 
```



# Lambda表达式

## Lambda表达式有三部分

**参数列表、箭头、Lambda主体**



## 五个有效的Lambda表达式的例子

```java
// 第一个Lambda表达式
// 具有一个String类型的参数并返回一个int。Lambda没有return语句，因为已经隐含了return
(String s) -> s.length()
```

```java
// 第二个Lambda表达式
// 有一个Apple 类型的参数并返回一个boolean（苹果的重量是否超过150克）
(Apple a) -> a.getWeight() > 150
```

```java
// 第三个Lambda表达式
// 具有两个int类型的参数而没有返回值（void返回）
// 注意Lambda表达式可以包含多行语句，这里是两行 
(int x, int y) -> {
    System.out.println("Result:"); 
    System.out.println(x+y); 
}
```

```java
// 第四个Lambda表达式
// 没有参数，返回一个int
() -> 42
```

```java
// 第五个Lambda表达式
// 具有两个Apple类型的参数，返回一个int：比较两个Apple的重量
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight())
```



## 合法的Lambda表达式示例

布尔表达式，返回一个布尔值：`(List<String> list) -> list.isEmpty()`

创建并返回对象：`() -> new Apple(10)`

消费一个对象无返回：`(Apple a) -> {System.out.println(a.getWeight());}`

从一个对象中选择/抽取并返回：`(String s) -> s.length() `

组合两个值并返回：`(int a, int b) -> a * b`

比较两个对象返回`int`：`(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight())`



## 在哪里以及如何使用 Lambda

**可以在函数式接口上使用Lambda表达式**



### 函数式接口

**函数式接口就是只定义一个抽象方法的接口**

```properties
接口现在还可以拥有默认方法（即在类没有对方法进行实现时，其主体为方法提供默认实现的方法）。哪怕有很多默认方法，只要接口只定义了一个抽象方法，它就仍然是一个函数式接口。
```

Lambda表达式允许你直接以内联的形式为函数式接口的抽象方法提供实现，并把整个表达式作为函数式接口的实例（具体说来，是函数式接口一个具体实现的实例）。你用匿名内部类也可以完成同样的事情，只不过比较笨拙：需要提供一个实现，然后再直接内联将它实例化。



### 函数描述符

