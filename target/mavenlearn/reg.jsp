<%--
  Created by IntelliJ IDEA.
  User: 87453
  Date: 2025/7/3
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html>
<head>
    <title>Title</title>
</head>

<body>
<h1>用户注册</h1>
<form action="seg" method="post" onsubmit="return checkAll()">
    昵称：<input id="name" name="name"><span id="span1"></span><br>
    密码：<input type="password" name="pd"><br>
    <input type="submit" value="注册">
</form>

</body>
<script type="text/javascript" src="js/jquery-3.6.0.js"></script>
<script>

    // jquery的ajax
    // url后端地址 请求哪个后端
    // data携带到后端的数据
    // success : function(data)  响应成功 data：后端响应给前端的数据
    name=true;
    $(function(){
        $("#name").blur(function(){
            $.ajax({//发起ajax请求
                url : "checkName",
                type : "get",
                data :	{"name":$("#name").val()},
                success : function(data){
                    document.getElementById("span1").innerText=data
                    $("#span1").text(data)
                    if(data=="可用"){
                        name=true
                        $("#span1").css("color","green")
                    }else{
                        name=false
                        $("#span1").css("color","red");
                    }
                },
            });
        });

    });

    function checkAll(){
        if(name == false){
            alert("用户名不可用，请更换用户名");
            return false;
        }
        
        // 检查表单是否填写完整
        if($("#name").val() == "" || $("input[name='pd']").val() == ""){
            alert("用户名和密码不能为空");
            return false;
        }
        
        return true;
    }

</script>
</html>
