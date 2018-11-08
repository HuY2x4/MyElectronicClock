<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="ajax方式">
    <script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript">
        function startReceive() {
        	var bao = {
        				
                };
        	alert("调用start函数");
            $.ajax({
            
           	    data: bao,
                type: "POST",
                dataType: "text",
                url: "<%=basePath%>startGetMSG", 
                success: function (data) {  
           
                	getMSG("");
                	//getMSG2("");
                },
                error : function() {
                    
                }
            });
        }
        
        function getMSG(clientID) {
        	var bao = {
        			"clientId" : clientID,
                };
     
            $.ajax({
            
           	    data: bao,
                type: "POST",
                timeout :180000,//三分钟
                dataType: "json",
                url: "<%=basePath%>getMSG", 
                success: function (data) {  
                	if(data.state=="none"){
                		getMSG("");
                	}
                	else{
                		//alert("getMSG success");
                    	var getmsg=document.getElementById("getmsg");
                    	getmsg.value=data.data;
                    	//alert(data.data);
                    	var clientid1=document.getElementById("clientid1");
                    	clientid1.value=data.clientID;
                    	//alert(data.clientID);
                    	getMSG(data.clientID);
                	}
                	
                },
                error : function() {
                	alert("getMSG error");
                	//getMSG("");//重新找待绑定状态的设备
                	var clientid1=document.getElementById("clientid1");
                	clientid1.value="";
                }
            });
        }
        function startgetMSG2(){
        	getMSG2("");
        }
        
        function getMSG2(clientID) {
        	var bao = {
        			"clientId" : clientID,
                };
     
            $.ajax({
            
           	    data: bao,
                type: "POST",
                timeout :180000,//三分钟
                dataType: "json",
                url: "<%=basePath%>getMSG", 
                success: function (data) {  
                	if(data.state=="none"){
                		getMSG("");
                	}
                	else{
	                	//alert("收到数据："+data);
	                	var text1=document.getElementById("getmsg2");
	                	text1.value=data.data;
	                	var clientid2=document.getElementById("clientid2");
	                	clientid2.value=data.clientID;
	                	//alert(data.clientID);
	                	getMSG2(data.clientID);
                	}
                },
                error : function() {
                	alert("getMSG error");
                	//getMSG2("");
                	var clientid2=document.getElementById("clientid2");
                	clientid2.value="";
                }
            });
        }
        
        
        function sendMSG() {
        	var text=document.getElementById("sendmsg");
        	var clientID=document.getElementById("clientid1");
        	var bao = {
                    
                    "msg" : text.value,
                    "clientID": clientID.value
                    
                };
     
            $.ajax({
            //几个参数需要注意一下
           	    data: bao,
                type: "POST",//方法类型
                dataType: "text",
                url: "<%=basePath%>sendMSG" , 
                contentType:"application/x-www-form-urlencoded; charset=utf-8",
                success: function (result) {   //result就是返回的String或json
                	
                	alert("发送成功");
                	
                },
                error : function() {
                    alert("还是错的");
                }
            });
        }
        
        function sendMSG2() {
        	var text=document.getElementById("sendmsg2");
        	var clientID=document.getElementById("clientid2");
        	var bao = {
                    
                    "msg" : text.value,
                    "clientID": clientID.value
                    
                };
     
            $.ajax({
            //几个参数需要注意一下
           	    data: bao,
                type: "POST",//方法类型
                dataType: "text",
                url: "<%=basePath%>sendMSG" , 
                contentType:"application/x-www-form-urlencoded; charset=utf-8",
                success: function (result) {   //result就是返回的String或json
                	
                	alert("发送成功");
                	
                },
                error : function() {
                    alert("还是错的");
                }
            });
        }
        
       
    </script>
</head>
<body>
	<p><input type="button" value="开始接受数据" onclick="startReceive()"></p>
	<p><input name="getMSG" type="text" id="getmsg" tabindex="1" size="15" value=""/></p>
	<p><input type="button" value="发送数据" onclick="sendMSG()"></p>
	<p><input name="sendMSG" type="text" id="sendmsg" tabindex="1" size="15" value=""/></p>
	<p><input name="clientid" type="text" id="clientid1" tabindex="1" size="15" value=""/></p>
	
	<p></p>
	<p><input type="button" value="开始接受数据" onclick="startgetMSG2()"></p>
	<p><input name="getMSG2" type="text" id="getmsg2" tabindex="1" size="15" value=""/></p>
	<p><input type="button" value="发送数据" onclick="sendMSG2()"></p>
	<p><input name="sendMSG2" type="text" id="sendmsg2" tabindex="1" size="15" value=""/></p>
	<p><input name="clientid2" type="text" id="clientid2" tabindex="1" size="15" value=""/></p>
</body>
</html>