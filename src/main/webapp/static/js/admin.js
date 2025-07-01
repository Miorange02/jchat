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

    const page = $(this).data('page');
    $('#usersContent').addClass('hidden');
    $('#chatroomsContent').addClass('hidden');
    $('#messagesContent').addClass('hidden');

    if (page === 'users') {
        $('#pageTitle').text('用户管理');
        $('#pageDescription').text('管理系统中的所有用户信息');
        $('#usersContent').removeClass('hidden');
    } else if (page === 'chatrooms') {
        $('#pageTitle').text('聊天室管理');
        $('#pageDescription').text('管理系统中的所有聊天室');
        $('#chatroomsContent').removeClass('hidden');
    } else if (page === 'messages') {
        $('#pageTitle').text('消息记录');
        $('#pageDescription').text('查看和管理系统中的所有消息记录');
        $('#messagesContent').removeClass('hidden');
    }
});

// 页面加载时默认显示用户管理内容
$(document).ready(function() {
    $('.sidebar-item[data-page="users"]').addClass('active');
    $('#usersContent').removeClass('hidden');
});

// 全选/取消全选
$('#selectAll').change(function() {
    const isChecked = $(this).prop('checked');
    $('.message-checkbox').prop('checked', isChecked);
    updateDeleteButtonState();
});

// 更新删除按钮状态
function updateDeleteButtonState() {
    const checkedCount = $('.message-checkbox:checked').length;
    $('#deleteSelectedBtn').prop('disabled', checkedCount === 0);
}

// 单个复选框变化
$('.message-checkbox').change(updateDeleteButtonState);

// 删除单条消息
function deleteMessage(id) {
    if (confirm('确定删除这条消息？此操作不可撤销！')) {
        $.post('/admin/deleteMessage', {id: id}, function() {
            alert('消息删除成功！');
            location.reload();
        }).fail(function() {
            alert('删除失败，请重试！');
        });
    }
}

// 批量删除消息
$('#deleteSelectedBtn').click(function() {
    const selectedIds = $('.message-checkbox:checked').map(function() {
        return $(this).data('id');
    }).get();

    if (selectedIds.length === 0) {
        alert('请先选择要删除的消息！');
        return;
    }

    if (confirm('确定删除选中的 ' + selectedIds.length + ' 条消息？此操作不可撤销！')) {
        $.post('/admin/batchDeleteMessages', {ids: selectedIds.join(',')}, function(response) {
            if (response.success) {
                alert('消息删除成功！');
                location.reload();
            } else {
                alert('删除失败：' + response.message);
            }
        }).fail(function() {
            alert('网络错误，请重试！');
        });
    }
});

// 搜索消息
$('#messageSearch').on('input', function() {
    const keyword = $(this).val().toLowerCase();
    const rows = $('#messageTableBody tr');

    rows.each(function() {
        const content = $(this).find('td:eq(4)').text().toLowerCase();
        const visible = content.includes(keyword);
        $(this).toggle(visible);
    });
});

// 显示编辑用户模态框并预填数据
function showEditUserModal(id) {
    $.get('/admin/getUser', {id: id}, function(response) {
        if (typeof response === 'object' && response.success && response.data) {
            $('#editUserId').val(response.data.id);
            $('#editUserName').val(response.data.uname);
            $('#editUserEmail').val(response.data.email);
            $('#editUserPassword').val(response.data.password);
            $('#editUserModal').removeClass('hidden');
        } else {
            alert(response.message || '获取用户数据失败，请重试！');
        }
    }).fail(function() {
        alert('获取用户数据失败，请重试！');
    });
}

// 关闭编辑用户模态框
function closeEditUserModal() {
    $('#editUserModal').addClass('hidden');
}

// 删除用户
function deleteUser(id) {
    if (confirm('确定删除该用户？此操作不可撤销！')) {
        $.post('/admin/deleteUser', {id: id}, function() {
            alert('用户删除成功！');
            location.reload();
        }).fail(function() {
            alert('删除失败，请重试！');
        });
    }
}

// 关闭添加聊天室模态框
function closeAddChatroomModal() {
    $('#addChatroomModal').addClass('hidden');
}

// 显示编辑聊天室模态框并预填数据
function showEditChatroomModal(id, name, description) {
    $('#editChatroomId').val(id);
    $('#editChatroomName').val(name);
    $('#editChatroomDescription').val(description);
    $('#editChatroomModal').removeClass('hidden');
}

// 关闭编辑聊天室模态框
function closeEditChatroomModal() {
    $('#editChatroomModal').addClass('hidden');
}

// 删除聊天室
function deleteChatroom(id) {
    if (confirm('确定删除该聊天室？此操作不可撤销！')) {
        $.post('/admin/deleteChatroom', {id: id}, function() {
            alert('聊天室删除成功！');
            location.reload();
        }).fail(function() {
            alert('删除失败，请重试！');
        });
    }
}

// 添加聊天室表单提交处理
$('#addChatroomForm').submit(function(e) {
    e.preventDefault();
    const formData = $(this).serialize();
    $.post('/admin/addChatroom', formData, function(response) {
        if (response.success) {
            alert('聊天室创建成功！');
            closeAddChatroomModal();
            location.reload();
        } else {
            alert('创建失败：' + response.message);
        }
    }).fail(function() {
        alert('网络错误，请重试！');
    });
});

// 编辑聊天室表单提交处理
$('#editChatroomForm').submit(function(e) {
    e.preventDefault();
    const formData = $(this).serialize();
    $.post('/admin/updateChatroom', formData, function(response) {
        if (response.success) {
            alert('聊天室更新成功！');
            closeEditChatroomModal();
            location.reload();
        } else {
            alert('更新失败：' + response.message);
        }
    }).fail(function() {
        alert('网络错误，请重试！');
    });
});

// 编辑用户表单提交处理
$('#editUserForm').submit(function(e) {
    e.preventDefault();
    const formData = $(this).serialize();
    $.post('/admin/updateUser', formData, function(response) {
        if (response.success) {
            alert('用户信息更新成功！');
            closeEditUserModal();
            location.reload();
        } else {
            alert('更新失败：' + response.message);
        }
    }).fail(function() {
        alert('网络错误，请重试！');
    });
});