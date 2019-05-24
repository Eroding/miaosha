//存放主要逻辑的js代码
var seckill={
		//封装秒杀相关的ajax的url
		URL:{
			now: function(){
				return '../time/now';
			},
			exposer:function(seckillId){
				return '../'+seckillId+'/exposer';
			},
			exection:function(seckillId,md5){
				return '../'+seckillId+'/'+md5+'/execution';
			},
		},
		//验证手机号
		//isNaN非数字为true，！isNaN为数字才是true
		validatePhone:function(phone){
			if(phone && phone.length ==11 && !isNaN(phone)){
				return true;
			}else{
				return false;
			}
		},
		
		
		
		handleSeckillkill: function(seckillId,node){
			//获取秒杀地址。处理秒杀逻辑
			//先创建一个按钮。先隐藏
	
			node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
			
			$.post(seckill.URL.exposer(seckillId),{},function(result){
				if(result && result['success']){
					var exposer = result['data'];
					if(exposer['exposed']){
						//开启秒杀 
						//获取秒杀地址
						var md5 = exposer['md5'];
						var killUrl = seckill.URL.exection(seckillId,md5);
						console.log("killURL"+killUrl);
						//绑定一次点击事件
						$('#killBtn').one('click',function(){
							//执行秒杀请求
							//首先禁用按钮
							$(this).addClass('disable');
							//发送秒杀请求执行秒杀
							$.post(killUrl,{},function(result){
								if(result && result['success']){
								var killResult =result['data'];
								var state = killResult['state'];
								
								var stateInfo = killResult['stateInfo'];
								//显示秒杀结果
								node.html('<span class="label label-success">'+stateInfo+'</span>');
								
								}
								
							});
						});
						node.show();
						
					}else{
						//未开启秒杀
						var now = exposer['now'];
						var start = exposer['start'];
						var end = exposer['end'];
						//重新计算计时逻辑
						seckill.countdown(seckillId,start,end,now);
						
					}
					
				}else{
					console.log('result'+result);
				}
				
				
			})
			
		},
		
		countdown:function(seckillId,startTime,endTime,nowTime){
			
			var seckillBox = $('#seckill-box');
			
			if(nowTime>endTime){
				//秒杀结束
				seckillBox.html("秒杀结束。请等待下次");
				
			}else if(nowTime<startTime){
				//秒杀未开始
				var killTime =new Date(startTime);
				seckillBox.countdown(killTime,function(event){
					//时间格式
					var format = event.strftime('秒杀倒计时：%D天  %H时  %M分  %S秒 ');
					seckillBox.html(format);
				}).on('finish.countdown',function(){
					//获取秒杀地址
					
					//前面是id，后面是节点
					seckill.handleSeckillkill(seckillId,seckillBox);
				});
			}else{
				//秒杀开始
				
				seckill.handleSeckillkill(seckillId,seckillBox);
			}
			
		},
		
		
		
		//详情页秒杀逻辑
detail:{	
	//详情页初始化
	init:function(params){
		//将detail页面传过来的数据验证
		//在cookie中查找手机号
		
		var killPhone = $.cookie('killPhone');
		
		//验证手机号码
		if(!seckill.validatePhone(killPhone)){
			//绑定phone，把detail页面的填写手机号这个div的根据id定义一个变量
			
			var killPhoneModal = $('#killPhoneModal');
			//显示弹出层
			killPhoneModal.modal({
				show:true,//显示弹出层
				backdrop:'static',//进制位置关闭
				keyboard:false//关闭键盘事件		
			});
			
			//div有个提交按钮
			$('#killPhoneBtn').click(function(){
				//取得input框上的
				var inputPhone = $('#killPhone').val();
			
				if(seckill.validatePhone(inputPhone)){
					//电话写入cookie
				
					$.cookie('killPhone',inputPhone,{expires:7});
				
					//刷新页面
					window.location.reload();
				}else{
					$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号码错误</label>').show(300);
				}				
			});
			
		}
		
		//首先获得后端的时间，用时间来和传到这个页面的开启和结束时间做判断，
		
		var startTime = params['startTime'];
	
		var endTime = params['endTime'];
		var seckillId = params['seckillId'];
	
		$.get(seckill.URL.now(),{},function(result){
			if(result && result['success']){
				//获取controller穿回来的系统时间
				var nowTime = result['data'];
				seckill.countdown(seckillId,startTime,endTime,nowTime);	
				
				
				
			}else{
				console.log('result'+result);
			}
			
			
		});
		
	}
}
}