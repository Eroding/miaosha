package com.cnh.dto;

import com.cnh.dataobject.SuccessKilledDo;
import com.cnh.enums.SeckillStateEnum;

//service层的数据包装


/*
封装秒杀执行后的结果
 */
public class SeckillExcution {
//秒杀的商品
    private long seckillId;

    //状态表示
    private int state;

    //状态使用文字表示
    private  String stateInfo;

    //秒杀成功对象
private SuccessKilledDo successKilledDo;


    public SeckillExcution(long seckillId, SeckillStateEnum seckillStatEnum, SuccessKilledDo successKilledDo) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
        this.successKilledDo = successKilledDo;

    }

    public SeckillExcution(long seckillId, SeckillStateEnum seckillStatEnum) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();

    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

	public SuccessKilledDo getSuccessKilledDo() {
		return successKilledDo;
	}

	public void setSuccessKilledDo(SuccessKilledDo successKilledDo) {
		this.successKilledDo = successKilledDo;
	}

}

