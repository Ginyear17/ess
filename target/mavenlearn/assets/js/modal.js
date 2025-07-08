// 获取模态框元素
var loginModal = document.getElementById("login-modal");
var registerModal = document.getElementById("register-modal");

// 获取打开模态框的按钮
var userBtn = document.querySelector(".user-btn");
var registerBtn = document.getElementById("register-btn");
var backToLoginBtn = document.getElementById("back-to-login-btn");

// 获取关闭模态框的按钮
var closeButtons = document.getElementsByClassName("close-button");

// 当用户点击用户按钮时，打开登录模态框
userBtn.addEventListener("click", function() {
  loginModal.style.display = "flex";
});

// 当用户点击注册按钮时，切换到注册模态框
registerBtn.addEventListener("click", function() {
  loginModal.style.display = "none";
  registerModal.style.display = "flex";
});

// 当用户点击返回登录按钮时，切换回登录模态框
backToLoginBtn.addEventListener("click", function() {
  registerModal.style.display = "none";
  loginModal.style.display = "flex";
});

// 为所有关闭按钮添加事件监听
for (var i = 0; i < closeButtons.length; i++) {
  closeButtons[i].addEventListener("click", function() {
    loginModal.style.display = "none";
    registerModal.style.display = "none";
  });
}



// 获取登录表单提交按钮
var loginSubmitBtn = document.getElementById("login-submit-btn");

// 登录提交事件处理
loginSubmitBtn.addEventListener("click", function() {
  
  var email = document.querySelector("#login-form input[name='email']").value;
  var password = document.querySelector("#login-form input[name='password']").value;
  console.log("登录按钮被点击"); // 添加这行来确认事件是否触发
  
  // 创建AJAX请求
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "login", true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        try {
          var response = JSON.parse(xhr.responseText);
          if (response.success) {
            // 登录成功，存储用户信息
            localStorage.setItem("user", JSON.stringify(response.user));    

          // 根据用户类型决定跳转
          if (response.isAdmin) {
            // 管理员用户，直接跳转到管理页面
            window.location.href = "admin.jsp";
          } else {
            // 普通用户，获取用户完整信息并跳转到商品页面
            getUserInfoByEmail(response.user.email, function() {
              window.location.href = "getProducts";
            });
          }
          
          console.log("登录成功");
        } else {
            console.log("登录失败，显示错误消息"); 
            // 登录失败，显示错误消息
            document.getElementById("login-message").textContent = response.message;
          }
        } catch (e) {
          console.log("登入出错"); 
          // 如果返回的不是JSON，说明可能是重定向或其他响应
          document.getElementById("login-message").textContent = "登录过程中发生错误，请稍后再试";
        }
      }
    }
  };
  
  // 发送请求
  xhr.send("email=" + encodeURIComponent(email) + "&password=" + encodeURIComponent(password));
});


var registerSubmitBtn = document.getElementById("register-submit-btn");

registerSubmitBtn.addEventListener("click", function() {
  console.log("注册按钮被点击"); // 添加这行来确认事件是否触发
  var username = document.querySelector("#register-form input[name='new-username']").value;
  var email = document.querySelector("#register-form input[name='new-email']").value;
  var password = document.querySelector("#register-form input[name='new-password']").value;
  var confirmPassword = document.querySelector("#register-form input[name='confirm-password']").value;

  // 创建AJAX请求
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "register", true);  // 修改为正确的注册servlet地址
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        try {
          var response = JSON.parse(xhr.responseText);
          if (response.success) {
            // 注册成功
            registerModal.style.display = "none";
            loginModal.style.display = "flex";
            document.getElementById("login-message").textContent = "注册成功，请登录!";
          } else {
            // 注册失败，显示错误消息
            document.getElementById("register-error-msg").textContent = response.message;  // 修改为register-error-msg
          }
        } catch (e) {
          // 如果返回的不是JSON，说明可能是重定向或其他响应
          document.getElementById("register-error-msg").textContent = "注册过程中发生错误，请稍后再试";  // 修改为register-error-msg
        }
      }
    }
  };
  
  // 发送请求（需要添加用户名参数）
  xhr.send("username=" + encodeURIComponent(username) + "&email=" + encodeURIComponent(email) + "&password=" + encodeURIComponent(password) + "&confirmPassword=" + encodeURIComponent(confirmPassword));
});


// 获取存储在localStorage中的用户信息
const user = JSON.parse(localStorage.getItem("user"));

// 根据email获取用户完整信息并存入sessionStorage
function getUserInfoByEmail(email, callback) {
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "getUserInfo?email=" + encodeURIComponent(email), true);
  
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
      try {
        var response = JSON.parse(xhr.responseText);
        if (response.success) {
          // 存储处理后的用户信息
          sessionStorage.setItem("userInfo", JSON.stringify(response.user));
          console.log("用户完整信息已存入sessionStorage:", response.user);
          
          // 执行回调函数
          if (typeof callback === 'function') {
            callback();
          }
        } else {
          console.error("获取用户信息失败:", response.message);
        }
      } catch (e) {
        console.error("解析响应时出错:", e);
      }
    }
  };
  
  xhr.send();
}



// 修改为从sessionStorage获取完整用户信息并渲染侧边栏
// 页面加载时自动触发，所以可以通过刷新触发
(function() {
    // 从sessionStorage获取完整用户信息
    var userInfoStr = sessionStorage.getItem("userInfo");
    if (userInfoStr) {
        try {
            var userInfo = JSON.parse(userInfoStr);
            console.log("用户完整信息:", userInfo);
            
            // 更新用户名
            if (userInfo.user_name) {
                document.getElementById("sidebar-username").textContent = userInfo.user_name;
            }
            
            // 更新头像
            if (userInfo.avatar_url) {
                document.getElementById("sidebar-avatar").src =window.baseUrl + userInfo.avatar_url;
            }

        } catch(e) {
            console.error("解析用户信息时出错:", e);
        }
    } else {
        // 未登录或无用户信息时显示默认头像和名称
        document.getElementById("sidebar-avatar").src = "./assets/images/avatars/avatar-main.webp";
        document.getElementById("sidebar-username").textContent = "未登录";
    }
})();