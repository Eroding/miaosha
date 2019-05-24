package com.cnh.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnh.dataobject.SeckillDo;
import com.cnh.dto.Exposer;
import com.cnh.dto.SeckillExcution;
import com.cnh.dto.SeckillResult;
import com.cnh.enums.SeckillStateEnum;
import com.cnh.exception.RepeatKillException;
import com.cnh.exception.SeckillCloseException;
import com.cnh.exception.SeckillException;
import com.cnh.service.SeckillService;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

	
    @Autowired
    private SeckillService seckillIdService;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
    	//获取列表页面
    	List<SeckillDo> list=seckillIdService.getSeckillList();
    
    	for(SeckillDo se:list) {
    		System.out.println("此时的seckillDo="+se.getName());
    	}
    	model.addAttribute("list", list);
    	return "miaosha/list";
    }
    
    
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail( @PathVariable(value="seckillId")Long seckillId,Model model) {
		//如果传进来的id为空
    	System.out.println("进入单查");
    	if(seckillId==null) {
    		return "redirect:seckill/miaosha/list";
    	}
    	SeckillDo  seckillDo = seckillIdService.getById(seckillId);
    	
    	if(seckillDo==null) {
    		return "forward:/seckill/miaosha/list";
    	}
    	model.addAttribute("seckill", seckillDo);
    	return "miaosha/detail";	
}
  
    //ajax json
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillIdService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }

        return result;
    }   
    
    
  //执行秒杀
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExcution> excute(@PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "killPhone", required = false) Long phone) {
				
    	//上面的手机号是从cookie取得的， required = false代表不是必须的
    	
    	 if (phone == null) {
    		 return new SeckillResult<SeckillExcution>(false, "未注册，输入的手机号不正确");
         }
    	   SeckillResult<SeckillExcution> result;
    	
    	   try {
               SeckillExcution execution = seckillIdService.executeSeckill(seckillId, phone, md5);
               return new SeckillResult<SeckillExcution>(true, execution);
           } catch (RepeatKillException e) {
               SeckillExcution execution = new SeckillExcution(seckillId, SeckillStateEnum.REPEAT_KILL);
               return new SeckillResult<SeckillExcution>(true, execution);
           } catch (SeckillCloseException e) {
               SeckillExcution execution = new SeckillExcution(seckillId, SeckillStateEnum.END);
               return new SeckillResult<SeckillExcution>(true, execution);
           } catch (Exception e) {
               logger.error(e.getMessage(), e);
               SeckillExcution execution = new SeckillExcution(seckillId, SeckillStateEnum.INNER_ERROR);
               return new SeckillResult<SeckillExcution>(true, execution);
           }
    }
    
    
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {

     Date now = new Date();
     System.out.println("进入取时间controller"+now.getTime());
   return new SeckillResult(true,now.getTime());
  
    }
    
}
