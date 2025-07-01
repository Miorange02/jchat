tailwind.config = {
    theme: {
        extend: {
            colors: {
                primary: '#2AABEE',
                secondary: '#0088CC',
                dark: '#1E2A3A',
                light: '#F5F8FA',
                'gray-medium': '#828282',
                'gray-light': '#E5E5E5',
            },
            fontFamily: {
                inter: ['Inter', 'sans-serif'],
            },
        }
    }
}

// 全局函数：切换聊天室（已存在）
window.changeChatroom = function(roomId) {
    // 移动端关闭侧边栏
    if (window.innerWidth < 1024) {
        document.querySelector('.sidebar').classList.add('hidden');
        document.querySelector('.chat-area').classList.add('lg:w-full');
    }
    window.location.href = "/chat?chatroomId=" + roomId;
};

// 全局函数：加入聊天室（已调整）
window.joinChatroom = function(chatroomId) {
    if (!confirm("确定要加入该聊天室吗？")) return;

    $.ajax({
        url: "/chatroom",
        type: "POST",
        data: {
            action: "join",
            chatroomId: chatroomId
        },
        dataType: "json",
        success: function(response) {
            if (response.success) {
                // 调用全局函数动态添加聊天室到侧边栏
                window.addChatroomToSidebar(response.chatroom);
                alert("加入成功！");
            } else {
                alert("加入失败：" + response.message);
            }
        },
        error: function(xhr) {
            alert("网络请求失败：" + xhr.responseText);
        }
    });
};

// 全局函数：退出当前聊天室
window.exitChatroom = function() {
    if (!confirm("确定要退出当前聊天室吗？退出后将无法接收该聊天室消息")) return;

    $.ajax({
        url: "/chatroom",
        type: "POST",
        data: {
            action: "exit",
            chatroomId: currentChatroomId
        },
        dataType: "json",
        success: function(response) {
            if (response.success) {
                alert("退出成功！");
                // 跳转到聊天首页（会自动加载用户加入的其他聊天室）
                window.location.href = "/chat";
            } else {
                alert("退出失败：" + response.message);
            }
        },
        error: function(xhr) {
            alert("网络请求失败：" + xhr.responseText);
        }
    });
};

// 全局函数：动态添加聊天室到侧边栏（关键修复）
window.addChatroomToSidebar = function(chatroom) {
    const sidebar = document.querySelector('.sidebar .overflow-y-auto');
    const chatroomItem = document.createElement('div');
    chatroomItem.className = 'p-3 bg-white rounded-lg shadow-sm border border-gray-100 cursor-pointer transition-all hover:shadow-md hover:border-primary/80 chatroom-item';
    chatroomItem.dataset.roomId = chatroom.id;
    chatroomItem.innerHTML = `
        <div class="flex items-center space-x-3">
            <div class="flex-1 min-w-0">
                <div class="flex justify-between items-center">
                    <h3 class="font-semibold text-gray-800 truncate">${chatroom.rname}</h3>
                    <span class="text-xs text-gray-400">${chatroom.memberCount}人</span>
                </div>
                <p class="text-xs text-gray-500 mt-1 flex items-center">
                    <i class="fa fa-user-circle mr-1 text-gray-400"></i>
                    <span class="truncate">创建者：${chatroom.creatorName}</span>  <!-- 需后端返回创建者名称 -->
                </p>
            </div>
        </div>
    `;
    // 绑定点击跳转事件
    chatroomItem.addEventListener('click', function() {
        window.changeChatroom(chatroom.id);
    });
    sidebar.appendChild(chatroomItem);
};

// DOM 加载完成后初始化交互
document.addEventListener('DOMContentLoaded', function() {
    // 关闭公告板
    const closeButton = document.getElementById('close-ann-board');
    const annBoardContainer = document.getElementById('ann-board-container');
    const exitBtn = document.getElementById('exit-chatroom-btn');
    if (exitBtn) {
        exitBtn.addEventListener('click', window.exitChatroom);
    }
    if (closeButton && annBoardContainer) {
        closeButton.addEventListener('click', () => annBoardContainer.style.display = 'none');
    }

    // 高亮当前聊天室（需 HTML 中 chatroom-item 类）
    const currentChatroomId = window.currentChatroomId || 0;
    document.querySelectorAll('.chatroom-item').forEach(item => {
        if (parseInt(item.dataset.roomId) === currentChatroomId) {
            item.classList.add('bg-blue-50', 'border-primary');
        }
    });

    // 创建聊天室模态框
    const createBtn = document.getElementById('create-chatroom-btn');
    const createModal = document.getElementById('create-modal');
    const cancelCreate = document.getElementById('cancel-create');
    if (createBtn) {
        createBtn.addEventListener('click', () => createModal.classList.remove('hidden'));
    }
    if (cancelCreate) {
        cancelCreate.addEventListener('click', () => createModal.classList.add('hidden'));
    }
    if (createModal) {
        createModal.addEventListener('click', (e) => {
            if (e.target === createModal) createModal.classList.add('hidden');
        });
    }

    // 移动端侧边栏切换
    const sidebarToggle = document.getElementById('sidebar-toggle');
    const backBtn = document.getElementById('back-btn');
    const sidebar = document.querySelector('.sidebar');
    const chatArea = document.querySelector('.chat-area');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', () => {
            sidebar.classList.toggle('hidden');
            chatArea.classList.toggle('lg:w-full');
        });
    }
    if (backBtn) {
        backBtn.addEventListener('click', () => {
            sidebar.classList.toggle('hidden');
            chatArea.classList.toggle('lg:w-full');
        });
    }

    // 新增：初始化滚动到底部
    const scrollToBottom = () => {
        const chatMessages = document.getElementById('chat-messages');
        if (chatMessages) {
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    };

    // 立即执行一次
    scrollToBottom();

    // 添加500ms延迟确保动态内容加载完成
    setTimeout(scrollToBottom, 500);
});
