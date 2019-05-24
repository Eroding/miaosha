package com.cnh.dao.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnh.dataobject.SeckillDo;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
//类似jdbc的数据库连接池
private JedisPool jedisPool;

private final Logger logger = LoggerFactory.getLogger(this.getClass());




public RedisDao(String ip,int port) {
	jedisPool = new JedisPool(ip,port);
}
//序列化seckillDo这个类的
private RuntimeSchema<SeckillDo> schema = RuntimeSchema.createFrom(SeckillDo.class);

//去redis拿东西
public SeckillDo getSeckill(long seckillId) {
	
	try {
		//从数据库连接池拿类似conction
		Jedis jedis = jedisPool.getResource();
	
		try {
			
			String key = "seckill:"+seckillId;
			
			//redis存的是字节数组。取也是取字节数组
		byte[] bytes=	jedis.get(key.getBytes());
			
		//如果从redis获取到
		if(bytes!=null) {
			
			//创建一个空对象
			SeckillDo seckillDo = schema.newMessage();
			//ProtostuffIOUtil 是一个工具类
			ProtostuffIOUtil.mergeFrom(bytes, seckillDo, schema);
			//bytes根据反序列化赋值给seckillDo
			return seckillDo;
		}
		
			
		} finally {
			jedis.close();
		}
		
		
		
	} catch (Exception e) {
		// TODO: handle exception
		logger.error(e.getMessage(),e);
	}
	return null;
	
}


//插入到redis
public String putSeckill(SeckillDo seckillDo) {
	
	try {
		
		Jedis jedis = jedisPool.getResource();
		
		
		try {
			
			String key = "seckill:"+seckillDo.getSeckillId();
			
			byte[] bytes =ProtostuffIOUtil.toByteArray(seckillDo, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			
			//超时缓存，一小时
			int timeout =60*60;
			
			//如果是正确的话就返回ok
			String result =	jedis.setex(key.getBytes(), timeout, bytes);
			
		return result;
			
		} finally {
			// TODO: handle finally clause
			jedis.close();
		}
		
		
		
		
	} catch (Exception e) {
		logger.error(e.getMessage(),e);
	}
	
	
	
	
	return null;
}

}
