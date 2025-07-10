// Mobile menu toggle
const menuBtn = document.querySelector('.menu-btn');
const closeMenuBtn = document.querySelector('.close-menu-btn');
const mobileMenu = document.querySelector('.mobile-menu');
const overlay = document.querySelector('.overlay');

menuBtn.addEventListener('click', () => {
  mobileMenu.classList.add('active');
  overlay.classList.add('active');
  document.body.style.overflow = 'hidden';
  
  // 同步用户信息
  $('#mobile-sidebar-avatar').attr('src', $('#sidebar-avatar').attr('src'));
  $('#mobile-sidebar-username').text($('#sidebar-username').text());
  
  // 同步用户操作按钮
  let allActions = '';
  allActions += $('#user-actions').html() || '';
  allActions += $('#user-profile-actions').html() || '';
  $('#mobile-user-actions').html(allActions);

  
  // 同步购物车内容
  $('.mobile-shopping-list').html($('.shopping-list').html());
  $('#mobile-cart-total').text($('#cart-total').text());
});

function closeMenu() {
  mobileMenu.classList.remove('active');
  overlay.classList.remove('active');
  document.body.style.overflow = '';
}

closeMenuBtn.addEventListener('click', closeMenu);
overlay.addEventListener('click', closeMenu);

// 确保移动端导航功能与桌面端一致
$(document).ready(function() {
  $('.mobile-nav-link').click(function(e) {
    e.preventDefault();
    
    // 更新活动状态
    $('.mobile-nav-link').removeClass('active');
    $(this).addClass('active');
    
    // 获取选中的分类
    const category = $(this).data('category');
    
    // 关闭移动菜单
    closeMenu();
    
    // 触发桌面端相应类别的点击
    $('.nav-link[data-category="' + category + '"]').click();
  });
  
  // 绑定移动端结算按钮事件
  $(document).on('click', '#mobile-checkout-btn', function() {
    // 关闭移动菜单
    closeMenu();
    // 触发桌面端结算按钮点击事件
    $('#checkout-btn').click();
  });
});

// Theme toggle
const themeToggle = document.querySelector('.theme-toggle');
const themeIcon = themeToggle.querySelector('i');

themeToggle.addEventListener('click', () => {
  document.body.classList.toggle('dark');
  
  if (document.body.classList.contains('dark')) {
    themeIcon.className = 'fas fa-moon';
    localStorage.setItem('theme', 'dark');
  } else {
    themeIcon.className = 'fas fa-sun';
    localStorage.setItem('theme', 'light');
  }
});

// Check for saved theme preference
const savedTheme = localStorage.getItem('theme');
if (savedTheme === 'dark') {
  document.body.classList.add('dark');
  themeIcon.className = 'fas fa-moon';
}

// Search functionality
const searchBtn = document.querySelector('.search-btn');
searchBtn.addEventListener('click', () => {
  alert('搜索功能将在这里实现');
});
