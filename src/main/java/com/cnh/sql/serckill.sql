--整个过程为秒杀执行存储过程
--本来是 ; 是结束语句，现在用$$ 来代替
DELIMITER $$
--定义存储过程 。procedure 创建存储过程名称 参数
--IN 为输入参数    out为输出参数
-- DECLARE 局部变量
--row_count() 返回上一条修改类型的影响行数  0：未修改数据
-->0 :表示修改的行数  <0 ：sql错误/未执行修改
--前面的那个代表哪个数据库
CREATE PROCEDURE `guanwang`.`execute_seckill`
(in v_seckill_id bigint,in v_phone bigint,in v_kill_time timestamp,out r_result int)
BEGIN
	DECLARE insert_count int DEFAULT 0;
	START TRANSACTION;
	insert ignore into success_killed(seckill_id,user_phone,create_time)values
	(v_seckill_id,v_phone,v_kill_time);
	select row_count() into insert_count;
	IF(insert_count =0)THEN
	ROLLBACK;
	set r_result=-1;
	ELSEIF(insert_count<0)THEN
	ROLLBACK;
	set r_result=-2;
	ELSE
	update seckill
	set number =number-1
	where seckill_id =v_seckill_id
	and end_time >v_kill_time
	and start_time < v_kill_time
	and number>0;
	select row_count() into insert_count;
	IF(insert_count =0)THEN
	ROLLBACK;
	set r_result=0;
	ELSEIF(insert_count<0)THEN
	ROLLBACK;
	set r_result=-2;
		ELSE
		COMMIT;
		set r_result=1;
	END IF;	
	END IF;		
END ;
$$

--存储过程定义结束



DELIMITER ;

set @r_result =-3;




