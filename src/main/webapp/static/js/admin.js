tailwind.config = {
    theme: {
        extend: {
            colors: {
                primary: '#165DFF',
                secondary: '#36CFC9',
                danger: '#F53F3F',
                warning: '#FF7D00',
                success: '#00B42A',
                info: '#86909C',
                light: '#F2F3F5',
                dark: '#1D2129',
            },
            fontFamily: {
                inter: ['Inter', 'system-ui', 'sans-serif'],
            },
        }
    }
}

// 侧边栏切换
$('#sidebar-toggle').click(function() {
    $('#sidebar').toggleClass('-translate-x-full');
});

// 选项卡切换
$('.sidebar-item').click(function() {
    $('.sidebar-item').removeClass('active');
    $(this).addClass('active');

    // 这里可以添加内容切换逻辑
    const target = $(this).find('span').text().trim();
    console.log('切换到:', target);
    // 根据实际需求更新内容
});

// 显示编辑用户模态框
function showEditUserModal(id) {
    // AJAX获取用户数据填充表单
    // 这里只是示例，实际应通过AJAX获取数据
    $('#editId').val(id);
    // 假设我们已经获取到了用户数据
    const mockUser = {
        id: id,
        uname: '用户' + id,
        email: 'user' + id + '@example.com',
        password: '********',
        status: 'online'
    };

    $('#editUname').val(mockUser.uname);
    $('#editEmail').val(mockUser.email);
    $('#editPassword').val(mockUser.password);
    $('#editStatus').val(mockUser.status);

    $('#editModal').removeClass('hidden');
}

// 删除用户
function deleteUser(id) {
    if (confirm('确定删除该用户？此操作不可撤销！')) {
        $.post('/admin/deleteUser', {id: id}, function() {
            // 显示成功提示
            alert('用户删除成功！');
            location.reload();
        }).fail(function() {
            alert('删除失败，请重试！');
        });
    }
}

// 关闭模态框
function closeModal() {
    $('#editModal').addClass('hidden');
}

// 表单提交处理
$('#editForm').submit(function(e) {
    e.preventDefault();
    // 获取表单数据
    const formData = $(this).serialize();

    // 提交表单数据
    $.post('/admin/updateUser', formData, function(response) {
        if (response.success) {
            alert('用户信息更新成功！');
            closeModal();
            location.reload();
        } else {
            alert('更新失败：' + response.message);
        }
    }).fail(function() {
        alert('网络错误，请重试！');
    });
});