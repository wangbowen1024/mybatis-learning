# mybatis学习笔记

## 目录
1. mybatis入门

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