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
var userBtn = document.querySelector(".user-btn");
function initLoginButton() {
    // 先移除可能存在的事件监听器
    userBtn.removeEventListener("click", showLoginModal);
    userBtn.removeEventListener("click", logoutUser);
    
    // 根据登录状态添加适当的事件监听器
    if (sessionStorage.getItem("userInfo")) {
        userBtn.addEventListener("click", logoutUser);
    } else {
        userBtn.addEventListener("click", showLoginModal);
    }
}

// 初始化按钮
initLoginButton();

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


// 获取发送验证码按钮
var sendCodeBtn = document.getElementById("send-code-btn");

// 发送验证码事件处理
sendCodeBtn.addEventListener("click", function() {
  var email = document.querySelector("#register-form input[name='new-email']").value;
  
  if (!email) {
    document.getElementById("register-error-msg").textContent = "请输入邮箱地址";
    return;
  }
  
  // 禁用按钮并开始倒计时
  var countdown = 60;
  sendCodeBtn.disabled = true;
  sendCodeBtn.textContent = countdown + "秒后重新发送";
  
  var timer = setInterval(function() {
    countdown--;
    if (countdown <= 0) {
      clearInterval(timer);
      sendCodeBtn.disabled = false;
      sendCodeBtn.textContent = "发送验证码";
    } else {
      sendCodeBtn.textContent = countdown + "秒后重新发送";
    }
  }, 1000);
  
  // 创建AJAX请求
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "sendVerificationCode", true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        try {
          var response = JSON.parse(xhr.responseText);
          document.getElementById("register-error-msg").textContent = response.message;
          document.getElementById("register-error-msg").style.color = response.success ? "green" : "red";
        } catch (e) {
          document.getElementById("register-error-msg").textContent = "发送验证码过程中发生错误";
          document.getElementById("register-error-msg").style.color = "red";
        }
      }
    }
  };
  
  // 发送请求
  xhr.send("email=" + encodeURIComponent(email));
});

registerSubmitBtn.addEventListener("click", function() {
  // 获取原有字段
  var username = document.querySelector("#register-form input[name='new-username']").value;
  var email = document.querySelector("#register-form input[name='new-email']").value;
  var password = document.querySelector("#register-form input[name='new-password']").value;
  var confirmPassword = document.querySelector("#register-form input[name='confirm-password']").value;
  // 获取验证码
  var verificationCode = document.querySelector("#register-form input[name='verification-code']").value;

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
  xhr.send("username=" + encodeURIComponent(username) + 
           "&email=" + encodeURIComponent(email) + 
           "&password=" + encodeURIComponent(password) + 
           "&confirmPassword=" + encodeURIComponent(confirmPassword) + 
           "&verificationCode=" + encodeURIComponent(verificationCode));
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
          
          // 更新用户卡片
          updateUserCard(response.user);
          
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




// 在页面加载时检查用户登录状态
$(document).ready(function() {
    const userInfoStr = sessionStorage.getItem("userInfo");
    if (userInfoStr) {
        try {
            const userInfo = JSON.parse(userInfoStr);
            updateUserCard(userInfo);
        } catch(e) {
            console.error("解析用户信息失败:", e);
            updateUserCard(null);
        }
    } else {
        updateUserCard(null);
    }
});




document.addEventListener('DOMContentLoaded', function() {
    // 检查用户是否已登录
    const userInfoStr = sessionStorage.getItem("userInfo");
    const userBtn = document.querySelector('.user-btn');
    const loginText = document.getElementById('login-text');
    const userAvatar = document.getElementById('sidebar-avatar');
    
    // 移动端元素
    const mobileLoginText = document.querySelector('.mobile-menu #login-text');
    const mobileUserAvatar = document.getElementById('mobile-sidebar-avatar');
    
    if (userInfoStr) {
        // 用户已登录，修改按钮显示
        if (loginText) loginText.textContent = "退出登录";
        if (mobileLoginText) mobileLoginText.textContent = "退出登录";
        
        // 如果有头像，显示头像
        try {
            const userInfo = JSON.parse(userInfoStr);
            if (userInfo.avatar_url) {
                const avatarSrc = window.baseUrl + userInfo.avatar_url;
                if (userAvatar) {
                    userAvatar.src = avatarSrc;
                    userAvatar.style.display = "block";
                }
                if (mobileUserAvatar) {
                    mobileUserAvatar.src = avatarSrc;
                    mobileUserAvatar.style.display = "block";
                }
            }
        } catch(e) {
            console.error("解析用户信息失败:", e);
        }
        
        // 修改点击事件为退出登录
        userBtn.removeEventListener('click', showLoginModal);
        userBtn.addEventListener('click', logoutUser);
    } else {
        // 用户未登录，保持原样
        if (loginText) loginText.textContent = "登录";
        if (mobileLoginText) mobileLoginText.textContent = "登录";
        
        // 确保点击事件是显示登录模态框
        userBtn.removeEventListener('click', logoutUser);
        userBtn.addEventListener('click', showLoginModal);
    }
});


// 原始的登录模态框显示函数
function showLoginModal() {
    loginModal.style.display = "flex";
}

// 新增退出登录函数
function logoutUser() {
    if (confirm("确定要退出登录吗？")) {
        // 先清除本地存储
        sessionStorage.removeItem("userInfo");
        localStorage.removeItem("user");
        
        // 清除用户卡片
        updateUserCard(null);
        
        // 发送退出登录请求
        fetch('logoutServlet', {
            method: 'POST',
            credentials: 'same-origin'
        })
        .then(response => {
            // 无论服务器响应如何，都执行UI更新和页面刷新
            // 更新UI
            const loginText = document.getElementById('login-text');
            const mobilLoginText = document.querySelector('.mobile-menu #login-text');
            const userAvatar = document.getElementById('user-avatar');
            const mobileUserAvatar = document.querySelector('.mobile-menu #user-avatar');
            
            if (loginText) loginText.textContent = "登录";
            if (mobilLoginText) mobilLoginText.textContent = "登录";
            if (userAvatar) userAvatar.style.display = "none";
            if (mobileUserAvatar) mobileUserAvatar.style.display = "none";
            
            // 一定要刷新页面
            window.location.reload();
        })
        .catch(error => {
            console.error('退出登录失败:', error);
            // 即使请求失败也刷新页面
            window.location.reload();
        });
    }
}



// 更新用户卡片显示
function updateUserCard(userInfo) {
    if (userInfo) {
        // 用户已登录
        $('#sidebar-username').text(userInfo.user_name || '用户');
        $('#mobile-sidebar-username').text(userInfo.user_name || '用户');
        
        if (userInfo.avatar_url) {
            const avatarSrc = window.baseUrl + userInfo.avatar_url;
            $('#sidebar-avatar').attr('src', avatarSrc);
            $('#mobile-sidebar-avatar').attr('src', avatarSrc);
        }
        
        // 添加我的订单按钮
        const orderBtnHtml = `
            <button id="my-orders-btn" class="action-btn">
                <i class="fas fa-shopping-bag"></i> 我的订单
            </button>
        `;
        
        $('#user-actions').html(orderBtnHtml);
        $('#user-profile-actions').html(`
            <button id="edit-profile-btn" class="user-action-btn">
                <i class="fas fa-user-edit"></i> 修改个人信息
            </button>
        `);
        $('#mobile-user-actions').html(orderBtnHtml);
    } else {
        // 用户未登录
        $('#sidebar-username').text('访客');
        $('#mobile-sidebar-username').text('访客');
        
        $('#sidebar-avatar').attr('src', './assets/images/avatars/OIP-C.webp');
        $('#mobile-sidebar-avatar').attr('src', './assets/images/avatars/OIP-C.webp');
        
        $('#user-actions').empty();
        $('#mobile-user-actions').empty();
    }
}

