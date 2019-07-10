# mybatis学习笔记

## 目录
1. [mybatis入门](#mybatis入门)
2. [自定义mybatis框架](#自定义mybatis框架)
3. [使用mybatis进行CURD](#使用mybatis进行CURD)
4. [标签拓展](#标签拓展)
5. [mybatis的连接池及事务](#mybatis的连接池及事务)
6. [动态SQL](#动态SQL)
7. [多表查询](#多表查询)

##  mybatis入门
* mybatis的环境搭配
	* 第一步：创建maven工程并导入依赖（主要是mybatis和Mysql连接）
	* 第二步：创建实体类和dao接口
	* 第三步：创建mybatis主配置文件（SqlMapConifg.xml）
		1. mysql环境
		2. 指定映射配置文件
	* 第四步：创建映射配置文件（UserDao.xml这里取这个名字是为了和接口类名保持一直，取UserMapper.xml也是一样的）
		1. mybatis的映射配置文件位置必须和dao接口的包结构相同
		2. 映射配置文件的mapper标签namespace属性的取值必须是dao接口的全限定类名
		3. 映射配置文件的操作配置（select），id属性的取值必须是dao接口的方法名
		4. 当我们遵从了以上三点后，我们在开发中就无须再写dao的实现类

* mybatis的入门案例
	* 第一步：读取配置文件
	* 第二步：创建SqlSessionFactory工厂
	* 第三步：创建SqlSession
	* 第四步：创建Dao接口的代理对象
	* 第五步：执行dao中的方法
	* 第六步：释放资源

	* mybatis基于xml的入门案例：
		1. 不要忘记在映射配置中告知mybatis要封装到哪个实体类中（即resultType属性）
		2. 配置的方式：指定实体类的全限定类名

	* mybatis基于注解的入门案例：
		1. 把IUserDao.xml移除，在dao接口的方法上使用@Select注解，并且指定SQL语句
		2. 同时需要在SqlMapConfig.xml中的mapper配置时，使用class属性指定dao接口的全限定类名。

	* 明确：
		我们在实际开发中，都是越简便越好，所以都是采用不写dao实现类的方式。不管使用XML还是注解配置。但是Mybatis它是支持写dao实现类的。

## 自定义mybatis框架
* 前提知识：反射、xml基础及解析、注解、JDBC、代理
* 分析（这里仅仅以简单的查询所有和Select注解为例，是简化版mybatis，实际的要麻烦的多）：
	* 按照JDBC的流程，我们要想处理一个查询需要的步骤（这里将以下所有步骤看作一个selectList方法）：
		1. 根据配置信息连接数据库，获取Connection对象
			注册驱动、获取连接
		2. 获取预处理对象PreparedStatement对象
			这时候需要SQL语句，即 conn.preparedStatement(sql)
		3. 执行查询
			ResultSet rs = prep.executeQuery();
		4. 遍历结果集用于封装
			```java
			List<E> list = new ArrayList<>();
			while(rs.next()) {
				// 这里使用反射来创建实体类
				E element = (E)Class.forName(配置的全限定类名).newInsttance();
				/*
				 因为我们数据库表字段名和实体类属性名一致，
				 所以可以把表列名看成实体类的属性名，
				 并通过反射根据每个列名，来设置相应属性值
				*/
				...
				// 将对象添加到List中
				list.add(element);
			}
			```
	* 那么要想执行成功上诉方法，就需要给它2个信息：
		1. 连接数据库的连接信息
		2. 映射信息
			* 要执行的SQL语句
			* 要封装的结果的实体类的全限定类名
			
			```
			这里就可以将上诉2个内容封装成一个对象Mapper(包含SQL语句和实体类的全限定类名),并且通过接口方法名全限定名来作为Key
			```
	* 最后，根据dao接口字节码创建dao的代理对象，并调用上诉方法
		```java
		// 使用SqlSession创建Dao接口的代理对象
		UserDao userDao = session.getMapper(UserDao.class);

		// 根据dao接口字节码创建dao的代理对象
		public <T> T getMapper(Class<T> daoInterfaceClass) {
			/*
			 类加载器：它使用和被代理对象是相同的类加载器
			 代理对象要实现的接口：和被代理对象实现相同的接口
			 如何代理：它就是增强的方法，要我们自己提供
				此处是一个InvocationHandler接口，我们需要写一个该接口的实现类，并在实现类中调用selectList方法
			*/
			Proxy.newProxyInstance(类加载器，代理对象要实现的接口字节码数组，如何代理);
		}
		```
	* 总结：核心就两件事
		1. 创建代理对象（即通过xml或注解解析获得三要素：SQL,返回值类型，要执行的类方法的全限定名。然后通过反射、代理等创建dao接口的代理对象，可以理解为是实现类）
		2. 用过上诉实现类，调用查询所有（经过代理增强后，所有调用的方法都会被代理的invoke拦截，然后根据不同的调用者及方法识别出对应的SQL语句，返回类型等）
				
## 使用mybatis进行CURD
* 基本步骤
	1. 编写UserDao接口，以及方法
	2. 在SqlMapConfig.xml主配置文件中添加对应映射
	3. 在对应映射xml中进行填写
		* 如果有返回值使用：resultType属性（全限定类名）
		* 如果有参数使用：parameterType属性（全限定类名）
		* SQL语句中，如果有参数。使用#{value}的语法来写，其中value当：
			1. 取于类对象的时候，直接填属性名，如User中的username
			2. 如果参数是基本数据类型或者包装类parameterType属性中可以直接写String或java.lang.String都可以
			3. 如果只有一个参数，且是基本数据类型，value可以写成随意，如id，uid都可以
	4. 例子
	```xml
	<select id="findUserById" parameterType="Integer" resultType="com.mybatis.domain.User">
        select * from user where id = #{uid}
    </select>
	```

* 模糊查询
	* 方式一（采用字符串拼接方式）
	```xml
	<select id="findUserByName" parameterType="string" resultType="com.mybatis.domain.User">
        select * from user where username like '%${value}%'
    </select>
	```
	```java
	List<User> userList = userDao.findUserByName("王");
	```
	* 方式二（推荐：采用占位符，即perparedStatement形式）
	```xml
	<select id="findUserByName" parameterType="string" resultType="com.mybatis.domain.User">
        select * from user where username like #{value}
    </select>
	```
	```java
	List<User> userList = userDao.findUserByName("%王%");
	```
	
* 使用实体包装类作为参数（类似二级调用，使用.来操作）
	```xml
	 <select id="findUserByCard" parameterType="com.mybatis.domain.Card" resultType="com.mybatis.domain.User">
        select * from user where username like #{user.username}
    </select>
	```
	```java
	public class Card {
    	private User user;
	}
	```

* 获取添加后数据的Id值（相当于执行完查询后，再次调用一个查询函数，并将结果追加到第一次返回的结果实体类中）
	```xml
	<insert id="saveUser" parameterType="com.mybatis.domain.User">
		<!-- 这里注意：keyProperty对应实体类的Id，keyColumn对应数据库Id，order执行时机-->
        <selectKey keyProperty="id" keyColumn="id" resultType="int" order="AFTER">
            select last_insert_id()
        </selectKey>
        insert into user (username,sex,address,birthday) values (#{username},#{sex},#{address},#{birthday})
    </insert>
	```

* 关于实体类属性和数据库列名不一致的解决方案
	* 方案一：通过使用别名，在SQL语句层面解决问题【运行效率高，但是开发效率慢】
	* 方案二：使用配置文件【因为要多解析xml所以运行效率慢，但是开发效率高】
		```xml
		<!-- 配置 查询结果的列名和实体类的属性名的对应关系 -->
	    <!-- id是配置映射关系的标识，type是说明属于哪个实体类的映射 -->
	    <resultMap id="userMap" type="com.mybatis.domain.User">
	        <!-- 主键字段的对应，其中property为实体类中的属性名，column为数据库中的列名 -->
	        <id property="userId" column="id"/>
	        <!-- 非主键字段的对应 -->
	        <result property="userName" column="username"/>
	        <result property="userAddress" column="address"/>
	        <result property="userSex" column="sex"/>
	        <result property="userBirthday" column="birthday"/>
	    </resultMap>
		<!-- 注意：使用映射配置后，需要改一下select标签中的原属性resultType为resultMap，值是配置映射的id -->
	    <select id="findAll" resultMap="userMap">
	        select * from user
	    </select>
		```

## 标签拓展
* properties
	* 使用该标签可以将配置信息单独写到一个文件中
	* 引用资源后，直接在xml中使用${xxx}来使用
	* 其中有2种方式引用：resource和url
	```xml
	<configuration>
	    <!-- 配置properties
	            可以在标签内部配置连接数据库的信息。也可以通过属性引用外部配置文件信息
	            resource属性： 常用的
	                用于指定配置文件的位置，是按照类路径的写法来写，并且必须存在于类路径下。
	            url属性：
	                是要求按照Url的写法来写地址
	                URL：Uniform Resource Locator 统一资源定位符。它是可以唯一标识一个资源的位置。
	                它的写法：
	                    http://localhost:8080/mybatisserver/demo1Servlet
	                    协议      主机     端口       URI
	
	                URI:Uniform Resource Identifier 统一资源标识符。它是在应用中可以唯一定位一个资源的。
	    -->
		<!-- 使用resource属性 -->
	    <!--<properties resource="jdbcConfig.properties"/>-->

		<!-- 使用url属性 -->
	    <properties url="file:///D:/IDEA_Project/mybatis-learning/No04_%E4%BD%BF%E7%94%A8mybatis%E8%BF%9B%E8%A1%8CCURD/src/main/resources/jdbcConfig.properties"/>
	
	    <environments default="mysql">
	        <environment id="mysql">
	            <transactionManager type="JDBC"/>
	            <dataSource type="POOLED">
					<!-- 这里直接使用${xxx}调用 -->
	                <property name="driver" value="${jdbc.driver}"/>
	                <property name="url" value="${jdbc.url}"/>
	                <property name="username" value="${jdbc.username}"/>
	                <property name="password" value="${jdbc.password}"/>
	            </dataSource>
	        </environment>
	    </environments>
	
	    <mappers>
	        <mapper resource="com/mybatis/dao/UserMapper.xml"/>
	    </mappers>
	</configuration>
	```

	外部文件：jdbcConfig.properties
	```xml
	jdbc.driver=com.mysql.jdbc.Driver
	jdbc.url=jdbc:mysql://localhost:3306/mybatis
	jdbc.username=root
	jdbc.password=Bow1024
	```
* typeAliases
	* 还记得之前哪个基本数据类型为什么不用写全限定类名吗？就是使用了这个标签（起别名）
	* typeAlias 和 package
	```xml
	<!--使用typeAliases配置别名，它只能配置domain中类的别名 -->
    <typeAliases>
        <!--typeAlias用于配置别名。type属性指定的是实体类全限定类名。alias属性指定别名，当指定了别名就再区分大小写 
        <typeAlias type="com.itheima.domain.User" alias="user"></typeAlias>-->

        <!-- 用于指定要配置别名的包，当指定之后，该包下的实体类都会注册别名，并且类名就是别名，不再区分大小写-->
        <package name="com.itheima.domain"></package>
    </typeAliases> 
	```
* mappers标签中的package
	```xml
	<mappers>
        <!--<mapper resource="com/mybatis/dao/UserMapper.xml"/>-->
        
        <!-- package标签是用于指定dao接口所在的包,当指定了之后就不需要在写mapper以及resource或者class了 -->
        <package name="com.mybatis.dao"/>
    </mappers>
	```
	* 如果报错：org.apache.ibatis.binding.BindingException: Invalid bound statement (not found)
		这是一个很容易忽视的点，记住：接口名与Mybatis的映射文件名一定要一模一样。

## mybatis的连接池及事务
* mybatis有一套自己的连接池技术
	* 当需要连接的时候，去池子里面找有没有空闲的。有的话直接拿，没有的话去活动线程池中查看是否已经达到线程数上上限。如果上限了，从活动线程池中取出最先使用的那个，进行清理操作，然后返回该连接。
	* 在配置文件中 <dataSource type="xxx">取值有3种：
		* POOLED：使用线程池（推荐）
		* UNPOOLED：不适用线程池
		* JNDI
* 事务
	* 自动提交事务设置，将参数设为true：sqlSession.openSession(true);

## 动态SQL
* if标签
* where标签

	多条件查询
```xml
<select id="findAll" parameterType="com.mybatis.domain.User" resultType="com.mybatis.domain.User">
    select * from user
    <where>
        <if test="username != null">
            and username like #{username}
        </if>
        <if test="sex != null">
            and sex = #{sex}
        </if>
    </where>
</select>
```
* foreach标签

	集合遍历
```xml
<select id="findUserByIds" parameterType="com.mybatis.domain.Card" resultType="com.mybatis.domain.User">
    select * from user
    <where>
        <if test="ids != null">
            <!-- 从collection里遍历，每个对象为item,并用separator分隔，最后放在open和close之间 -->
            <foreach collection="ids" open="id in (" close=")" item="id" separator=",">
                #{id}
            </foreach>
        </if>
    </where>
</select>
```

## 多表查询
学生类
```java
public class Student implements Serializable {
    private int id;
    private String name;
    private int age;
    /**
     * 一个学生可以写多篇文章（即，一对多）
     */
    private List<Article> articles;
}
```
文章类
```java
public class Article implements Serializable {
    private int id;
    private String title;
    private int sid;
    /**
     * 一个文章的作者只能一个学生(即，一对一。多对一在Mybatis中也是看成一对一)
     */
    private Student student;
}
```
* 一对多（collection）
```xml
<!-- 查询所有学生写的所有文章 -->
<mapper namespace="com.mybatis.dao.StudentDao">
    <resultMap id="resultStudentMap" type="Student">
        <id column="sid" property="id"/>
        <result property="name" column="sname"/>
        <result property="age" column="age"/>
        <collection property="articles" ofType="Article">
            <id column="aid" property="id"/>
            <result property="title" column="title"/>
            <result property="sid" column="sid"/>
        </collection>
    </resultMap>

    <select id="getArticlesByStudents" resultMap="resultStudentMap">
        select * from student s left outer join article a on s.sid = a.sid
    </select>
</mapper>
```
* 一对一、多对一（association）
```xml
<!-- 查询每篇文章的作者信息 -->
<mapper namespace="com.mybatis.dao.ArticleDao">
    <resultMap id="resultArticleMap" type="Article">
        <id column="aid" property="id"/>
        <result property="title" column="title"/>
        <result property="sid" column="sid"/>
        <association property="student" javaType="Student">
            <id column="sid" property="id"/>
            <result property="name" column="sname"/>
            <result property="age" column="age"/>
        </association>
    </resultMap>
    
    <select id="getArticlesInfo" resultMap="resultArticleMap">
        select a.*,s.age,s.sname from article a join student s on s.sid = a.sid
    </select>
</mapper>
```