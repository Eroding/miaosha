1.创建时使用webapp,如果是idea spring-boot的话就是quckily
2.修改web.xml（默认2.3，不支持jsp el表达式使用），修改为3.1
https://blog.csdn.net/Lucky22Amin/article/details/82929627

进入pom.xml ，把junit改为4.11版本


自动生成代码。先在build里插入代码。

然后项目 右键--》run as --》 maven bulid --》弹出对话框 --》在goals中输入mybatis-generator:generate

                                                                                 或者 点击select --》选择你的mybatis插件 --》apply --》run
                                                                                 
 successkilled的实体类中包括了seckill。秒杀记录包含了秒杀的内容       
 dto层是                 数据传输层                            

当自己写的js不能被访问时
在web.xml 中插入

	 <servlet-mapping>
     <servlet-name>default</servlet-name>
     <url-pattern>*.js</url-pattern>
 </servlet-mapping>              
 
 
 mapper.xml
 =映射语句要写插入的类型或者返回的类型，必须写一个
 
 一个事务
 update(减库存) mysql 每秒40万条
 
 java处理速度也很快。主要问题出在  update---(GC处理器和网络延迟)--->insert---(GC处理器和网络延迟)---->commit/rollback
 
 
 本来是先减库存。再添加购买明细
 
 后来嫌添加购买明细，在减库存
 
 为什么这样做的解释：
 
 首先可以确定的是，insert也会有行锁的。因为作者的表有设置主键，innode默认对有主键的表为主键添加索引。只要有索引，insert，update和delete操作都会变成行级锁。
只是insert锁住了是要插入的行，不影响其它事务继续插入，因为插入的不是同一行！但是update的行锁会影响其他事务对同一行的update。因此，仅仅对并行插入而言，insert就相当于没锁。 
 
              