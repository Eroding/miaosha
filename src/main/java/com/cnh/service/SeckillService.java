package com.cnh.service;

import java.util.List;

import com.cnh.dataobject.SeckillDo;
import com.cnh.dto.Exposer;
import com.cnh.dto.SeckillExcution;
import com.cnh.exception.RepeatKillException;
import com.cnh.exception.SeckillCloseException;
import com.cnh.exception.SeckillException;

public interface SeckillService {

	  /*
    查询所有秒杀记录
     */
    List<SeckillDo> getSeckillList();
	
    /*
    查询单个秒杀记录
     */
      SeckillDo getById(long seckillId);

      /*
      秒杀开启时输出秒杀接口地址
      否则输出系统时间和秒杀时间
       */

      Exposer exportSeckillUrl(long seckillId);

   /*
   执行秒杀操作，需要商品id，手机号码。md5加密
   这里SeckillException是另外两个的父类，却要写出另外两个类，是要告诉前端，后面两个会实际告诉你哪里错了，而父类只会告诉你程序出错
    */
      SeckillExcution executeSeckill(long seckillId, long userPhone, String md5)throws SeckillCloseException, RepeatKillException, SeckillException;



      SeckillExcution executeSeckillProcedure(long seckillId, long userPhone, String md5)throws SeckillCloseException, RepeatKillException, SeckillException;


}
