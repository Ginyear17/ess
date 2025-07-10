$(document).ready(function() {
    // 初始化加载轮播图数据
    function loadSliders() {
        $.ajax({
            url: 'admin/sliders',
            type: 'GET',
            dataType: 'json',
            success: function(sliders) {
                renderSlidersTable(sliders);
            },
            error: function() {
                alert('加载轮播图数据失败');
            }
        });
    }
    
    // 点击轮播图管理标签时加载数据
    $('a[data-target="#sliders"]').on('click', function() {
        loadSliders();
    });
    
    // 刷新按钮事件
    $('#refresh-sliders-btn').click(function() {
        loadSliders();
    });
    
    // 渲染轮播图表格
    function renderSlidersTable(sliders) {
        var $tbody = $('#sliders-table');
        $tbody.empty();
        
        sliders.forEach(function(slider, index) {
            var $tr = $('<tr>');
            $tr.append($('<td>').text(index + 1)); // 序号
            $tr.append($('<td>').html('<img src="' + slider.imagePath + '" alt="轮播图" width="100">'));
            $tr.append($('<td>').text(slider.imagePath));
            $tr.append($('<td>').text(slider.title || ''));
            $tr.append($('<td>').text(slider.subtitle || ''));
            
            // 操作按钮
            var $actions = $('<td>');
            
            var $editBtn = $('<button>')
                .addClass('btn btn-xs btn-primary edit-slider-btn')
                .text('编辑')
                .data('slider', slider)
                .css('margin-right', '5px');
                
            var $deleteBtn = $('<button>')
                .addClass('btn btn-xs btn-danger delete-slider-btn')
                .text('删除')
                .data('slider-id', slider.id);
                
            $actions.append($editBtn, $deleteBtn);
            $tr.append($actions);
            
            $tbody.append($tr);
        });
    }
    
    // 添加轮播图按钮事件
    $('#add-slider-btn').click(function() {
        $('#sliderModalTitle').text('添加轮播图');
        $('#slider-id').val('');
        $('#sliderForm')[0].reset();
        $('#slider-preview').attr('src', '');
        $('#sliderModal').modal('show');
    });
    
    // 编辑按钮点击事件
    $(document).on('click', '.edit-slider-btn', function() {
        var slider = $(this).data('slider');
        
        $('#sliderModalTitle').text('编辑轮播图');
        $('#slider-id').val(slider.id);
        $('#slider-preview').attr('src', slider.imagePath);
        $('#slider-title').val(slider.title || '');
        $('#slider-subtitle').val(slider.subtitle || '');
        $('#slider-link').val(slider.link || '/');
        
        $('#sliderModal').modal('show');
    });
    
    // 图片上传预览
    $('#slider-image').change(function() {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $('#slider-preview').attr('src', e.target.result);
            }
            reader.readAsDataURL(this.files[0]);
        }
    });
    
    // 保存轮播图
    $('#save-slider-btn').click(function() {
        var formData = new FormData(document.getElementById('sliderForm'));
        var isNew = !$('#slider-id').val();
        
        formData.append('action', isNew ? 'add' : 'update');
        
        $.ajax({
            url: 'admin/sliders',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                $('#sliderModal').modal('hide');
                loadSliders();
                alert(isNew ? '添加成功' : '更新成功');
            },
            error: function() {
                alert(isNew ? '添加失败' : '更新失败');
            }
        });
    });
    
    // 删除轮播图
    $(document).on('click', '.delete-slider-btn', function() {
        if (confirm('确定要删除这张轮播图吗？')) {
            var sliderId = $(this).data('slider-id');
            
            $.ajax({
                url: 'admin/sliders',
                type: 'POST',
                data: {
                    action: 'delete',
                    sliderId: sliderId
                },
                success: function() {
                    loadSliders();
                    alert('删除成功');
                },
                error: function() {
                    alert('删除失败');
                }
            });
        }
    });
});
