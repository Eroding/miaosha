package com.cnh.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.cnh.dao.SeckillDoMapper;
import com.cnh.dao.SuccessKilledDoMapper;
import com.cnh.dao.cache.RedisDao;
import com.cnh.dataobject.SeckillDo;
import com.cnh.dataobject.SuccessKilledDo;
import com.cnh.dto.Exposer;
import com.cnh.dto.SeckillExcution;
import com.cnh.enums.SeckillStateEnum;
import com.cnh.exception.RepeatKillException;
import com.cnh.exception.SeckillCloseException;
import com.cnh.exception.SeckillException;
import com.cnh.service.SeckillService;


@Service("seckilledService")
public class SeckilledServiceimpl implements SeckillService {

	 @Autowired
	    private SeckillDoMapper seckillDoMapper;

	    @Autowired
	    private SuccessKilledDoMapper successsKilledDoMapper;
	    
	    @Autowired
	    private RedisDao redisDao;
	    

	    private Logger logger = LoggerFactory.getLogger(this.getClass());
	    
	    /*
	    slat随便定义成什么字符串，用来给md5加密使用
	     */
	    private final String slat ="cnh-Edison";
	    

		public List<SeckillDo> getSeckillList() {
			// TODO Auto-generated method stub
			return seckillDoMapper.queryAll(0, 4);
		}

		public SeckillDo getById(long seckillId) {
			// TODO Auto-generated method stub
			
			
			return seckillDoMapper.selectByPrimaryKey(seckillId);
		}

		public Exposer exportSeckillUrl(long seckillId) {
			// TODO 根据从前端传过来的id开始判断是否开启秒杀接口
			//因为每个都要通过id去访问暴露接口，所以使用缓存机制
			//通过redis
			
			
			SeckillDo seckillDo = redisDao.getSeckill(seckillId);
			if(seckillDo==null) {
				//访问数据库
			 seckillDo = seckillDoMapper.selectByPrimaryKey(seckillId);
				//发现根本没有这个id
				if(seckillDo==null){
				    return  new Exposer(false,seckillId);
				}else {
					//放入redis
					redisDao.putSeckill(seckillDo);
				}
			}
		
			//取系统当前时间，如果id不为空。就开始判断时间
	        Date startTime = seckillDo.getStartTime();
	        Date endTime = seckillDo.getEndTime();
	        Date nowTime = new Date();
	        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
	            return  new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
	        }
	        
	      //转换字符串过程，不可逆，加盐
	        String md5 = getMD5(seckillId);

	         return new Exposer(true,md5,seckillId);	
		}
		 //md5加密过程，加盐使得不会被破解
	    private  String getMD5(long seckillId){
	        String base = seckillId+"/"+slat;
	        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
	        return md5;
	    }
	   
	    
	    @Transactional
	    /*
	    使用@Tra..注解事务的方法优点：
	    开发团队达成一致的约定，保证事务的执行时间够短。因为某个方法有多条操作数据库的方法（比如
	    本案例中的执行秒杀项目要减库存，还要记录购买行为，所以一定要有事务，
	    不是所有的方法都需要事务，如果只有一条修改语句或者是只读操作不需要事务。根本用不到，
	    ）

	     */
		public SeckillExcution executeSeckill(long seckillId, long userPhone, String md5)
				throws SeckillCloseException, RepeatKillException, SeckillException {
			// TODO Auto-generated method stub
			//先检测传过来的md5是否为空或者是前端客户伪造的加密数据
	        if (md5 == null || !md5.equals(getMD5(seckillId))) {

	            throw new SeckillException("从前端传过来的md5和后端产生的不一致，");
	        }
	      //执行秒杀逻辑：减库存要和记录购买行为放在一起
	        Date nowTime = new Date();
	      //减库存,这里使用try catch的原因是万一运行过程中出现数据库连接异常，时间过长断开异常，统一抛出异常,反正不属于自定义异常的都是由这个抛出
	        try{
	        	//记录购买行为
                int insertCount = successsKilledDoMapper.insertSuccessKilled(seckillId, userPhone);
    //保证唯一性，因为不能让同一手机，同一商品被秒杀多次。所以数据库insert ingore into （如果不插入，就会返回 0 ，而不是出现错误）
                if (insertCount <= 0) {
                    //发生重复秒杀
                    throw new RepeatKillException("商品已经下单了。不能再次重复秒杀");
                } else {
                    //秒杀成功,热点商品的竞争
                	  int updateCount = seckillDoMapper.reduceNumber(seckillId, nowTime);
      	            if (updateCount <= 0) {
      	                throw new SeckillCloseException("秒杀结束，期待下次吧");
      	            } else {
      	              SuccessKilledDo successsKilledDo = successsKilledDoMapper.queryByIdWithSeckill(seckillId, userPhone);
                      return new SeckillExcution(seckillId, SeckillStateEnum.SUCCESS,successsKilledDo);
      	            }
              
                }
	          
	        }catch (SeckillCloseException e1){
	                throw  e1;
	            } catch (RepeatKillException e2){
	               throw  e2;
	            }catch (Exception e){
	                logger.error(e.getMessage(),e);
	                throw  new SeckillException("方法的运行期异常、交给自定义的seckillExption"+e.getMessage());
	            }

		}

		public SeckillExcution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
			// TODO Auto-generated method stub
			//先检测传过来的md5是否为空或者是前端客户伪造的加密数据
	        if (md5 == null || !md5.equals(getMD5(seckillId))) {

	            throw new SeckillException("从前端传过来的md5和后端产生的不一致，");
	        }
	      //执行秒杀逻辑：减库存要和记录购买行为放在一起
	        Date nowTime = new Date();
	        
	        
			
			
			
			
			
			return null;
		}
	    
		 

}
