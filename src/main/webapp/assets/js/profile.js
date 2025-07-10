$(document).ready(function() {
    // 获取模态框元素
    var profileModal = document.getElementById("profile-modal");
    var editProfileBtn = document.getElementById("edit-profile-btn");
    var closeProfileBtn = profileModal.querySelector(".close-button");
    var cancelProfileBtn = document.getElementById("cancel-profile-btn");
    var saveProfileBtn = document.getElementById("save-profile-btn");
    
    // 检查用户是否已登录，并相应显示或隐藏编辑按钮
    function checkUserLoginStatus() {
        const userInfoStr = sessionStorage.getItem("userInfo");
        if (userInfoStr) {
            $("#edit-profile-btn").show();
            
            // 填充表单默认值
            const userInfo = JSON.parse(userInfoStr);
            $("#update-username").val(userInfo.user_name || '');
            
            // 如果有头像，设置预览
            if (userInfo.avatar_url) {
                $("#avatar-preview").attr("src", window.baseUrl + userInfo.avatar_url);
            }
        } else {
            $("#edit-profile-btn").hide();
        }
    }
    
    // 初始检查登录状态
    checkUserLoginStatus();
    
    // 点击编辑按钮显示模态框
    $(document).on('click', '#edit-profile-btn', function() {
        if (!isUserLoggedIn()) {
            $('#login-modal').css('display', 'flex');
            return;
        }
        profileModal.style.display = "flex";
    });
    
    // 关闭按钮点击事件
    closeProfileBtn.addEventListener("click", function() {
        profileModal.style.display = "none";
    });
    
    // 取消按钮点击事件
    cancelProfileBtn.addEventListener("click", function() {
        profileModal.style.display = "none";
    });
    
    // 头像上传预览
    $('#avatar-upload').change(function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                $('#avatar-preview').attr('src', e.target.result);
            }
            reader.readAsDataURL(file);
        }
    });
    
    // 保存修改按钮点击事件
    saveProfileBtn.addEventListener("click", function() {
        const username = $('#update-username').val();
        const currentPassword = $('#current-password').val();
        const newPassword = $('#new-profile-password').val();
        const confirmPassword = $('#confirm-profile-password').val();
        
        // 基本验证
        if (!username) {
            $('#profile-message').text('用户名不能为空').css('color', 'red');
            return;
        }
        
        if (newPassword && newPassword !== confirmPassword) {
            $('#profile-message').text('两次输入的新密码不一致').css('color', 'red');
            return;
        }
        
        // 创建FormData对象用于文件上传
        const formData = new FormData();
        formData.append('username', username);
        
        // 如果输入了密码相关信息
        if (currentPassword) {
            formData.append('currentPassword', currentPassword);
            if (newPassword) {
                formData.append('newPassword', newPassword);
            }
        }
        
        // 添加头像文件（如果有）
        const avatarFile = $('#avatar-upload')[0].files[0];
        if (avatarFile) {
            formData.append('avatar', avatarFile);
        }
        
        // 发送AJAX请求
        $.ajax({
            url: 'updateUserProfile',
            type: 'POST',
            data: formData,
            processData: false,  // 不处理数据
            contentType: false,  // 不设置内容类型
            success: function(response) {
                if (response.success) {
                    // 更新sessionStorage中的用户信息
                    const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
                    userInfo.user_name = username;
                    if (response.avatarUrl) {
                        userInfo.avatar_url = response.avatarUrl;
                    }
                    sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
                    
                    // 更新用户卡片显示
                    updateUserCard(userInfo);
                    
                    // 显示成功消息并关闭模态框
                    $('#profile-message').text('个人信息修改成功').css('color', 'green');
                    setTimeout(function() {
                        profileModal.style.display = "none";
                    }, 1500);
                } else {
                    $('#profile-message').text(response.message || '修改失败').css('color', 'red');
                }
            },
            error: function() {
                $('#profile-message').text('系统错误，请稍后重试').css('color', 'red');
            }
        });
    });
    
    // 检查用户是否登录
    function isUserLoggedIn() {
        return sessionStorage.getItem("userInfo") !== null;
    }
});
