<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/tag.jsp"%>
      <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + "://" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀详情页</title>
    <%@include file="../common/head.jsp"%>
    
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- 倒计时插件 -->
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.js"></script>

<!--cookie插件 -->
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>

<!-- 把手写的js插件迎进来 -->
<script src="<%=path%>/resources/script/seckill.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	//alert("aa");
	seckill.detail.init({
		seckillId:${seckill.seckillId},	
		startTime:${seckill.startTime.time},
		endTime:${seckill.endTime.time}
	});
});
</script>
    
</head>
<body>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
     <h1>
        ${seckill.name}
        </h1>
        </div>
        <div class="panel-body">
        <h2 class="test-danger">
        <!-- 显示time图标 -->
        <span class="glyphicon glyphicon=time"></span>
         <!-- 展示倒计时 -->
        <span class="glyphicon" id="seckill-box"></span>
        </h2>
        </div>
          </div>
       </div>
    
    <!-- 模态框（Modal） -->
<div class="modal fade" id="killPhoneModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">	
				<h4 class="modal-title" id="myModalLabel">
					输入手机号，才能开始秒杀
				</h4>
			</div>
			<div class="modal-body">
			<div>			
				<input class="form-control" type="text" name="killPhone" id="killPhone" placeholder="请填写手机号">
			</div>
			</div>	
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				
				<span id="killPhoneMessage" class="glyphicon"></span>
				<button type="button" id="killPhoneBtn" class="btn btn-primary">
					提交
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
    
    
    
    
</body>


</html>
