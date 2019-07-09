# mybatis学习笔记

## 目录
1. [mybatis入门](#一、mybatis入门)
2. [自定义mybatis框架](#二、自定义mybatis框架)
3. [使用mybatis进行CURD](#三、使用mybatis进行CURD)

##  一、mybatis入门
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

## 二、自定义mybatis框架
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
				
## 三、使用mybatis进行CURD
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
	* 方案二：使用配置文件【因为还要解析所以运行效率慢，但是开发效率高】
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
		
	    <select id="findAll" resultMap="userMap">
	        select * from user
	    </select>
		```