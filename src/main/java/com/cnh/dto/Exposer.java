package com.cnh.dto;

/*
逻辑业务层使用的,service的数据包装
当前时间没有到秒杀时间的时候是不会暴露秒杀接口地址的只返回当前系统时间和秒杀开始时间
，当时间可以时，暴露秒杀接口
 */
public class Exposer {
    //是否开启秒杀
    private boolean exposed;

    //一种加密措施
    private String md5;

    //id
    private long seckillId;

    //系统当前时间（毫秒）
    private long now;

    //开启时间
    private long start;

    //结束时间
    private long end;

/*
类的重写方法原因。返回状态
因为当秒杀项目还没开始时，前端点击只能返回开始时间和当前时间；
当可以的时候，返回参加秒杀活动的商品
 */
    /*
     * 这些都是给实现类的返回提供构造方法，当判断当前为可以开启秒杀时，就返回true，加密。id
     */
    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }
/*
 * 返回否和id，还有现在的时间和秒杀开启和结束时间
 */
    public Exposer(boolean exposed,long seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.seckillId=seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }
/*
 * 当发现当前id不存在，直接返回否和id
 */
    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}

