window.changeChatroom = function(roomId) {
    // 如果是移动端视图，先关闭侧边栏
    if (window.innerWidth < 1024) {
        document.querySelector('.sidebar').classList.add('hidden');
        document.querySelector('.sidebar').classList.remove('lg:block');
        document.querySelector('.chat-area').classList.add('lg:w-full');
        document.querySelector('.chat-area').classList.remove('lg:w-2/4');
    }

    // 跳转到新聊天室
    window.location.href = "/chat?chatroomId=" + roomId;
}
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

document.addEventListener('DOMContentLoaded', function() {
    const closeButton = document.getElementById('close-ann-board');
    const annBoardContainer = document.getElementById('ann-board-container');

    if (closeButton && annBoardContainer) {
        closeButton.addEventListener('click', function() {
            // 点击关闭按钮隐藏公告板
            annBoardContainer.style.display = 'none';
        });
    }
});

// 模拟消息发送功能
document.getElementById('message-input').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        const message = this.value.trim();
        if (message) {
            // 添加新消息到聊天区域
            const chatMessages = document.getElementById('chat-messages');
            const messageDiv = document.createElement('div');
            messageDiv.className = 'flex items-end justify-end mb-4 message-animation';
            messageDiv.innerHTML = `
                    <div class="message-out p-3 max-w-[80%] shadow-sm">
                        <div class="flex items-center justify-end mb-1">
                            <span class="text-xs text-gray-medium mr-2">刚刚</span>
                            <h4 class="font-medium text-dark">你</h4>
                        </div>
                        <p>${message}</p>
                    </div>
                `;
            chatMessages.appendChild(messageDiv);

            // 清空输入框
            this.value = '';

            // 滚动到底部
            chatMessages.scrollTop = chatMessages.scrollHeight;

            // 模拟其他成员回复
            setTimeout(() => {
                const replies = [
                    '这个观点很有意思！',
                    '我同意你的看法。',
                    '感谢分享！',
                    '你能详细说明一下吗？',
                    '很棒的见解！',
                    '我有类似的经验。',
                    '其他人怎么看？'
                ];
                const randomReply = replies[Math.floor(Math.random() * replies.length)];

                const replyDiv = document.createElement('div');
                replyDiv.className = 'flex items-end mb-4 message-animation';
                replyDiv.innerHTML = `
                        <img src="https://picsum.photos/id/1027/100/100" alt="张三" class="w-8 h-8 rounded-full mr-2">
                        <div class="message-in p-3 max-w-[80%] shadow-sm">
                            <div class="flex items-center mb-1">
                                <h4 class="font-medium text-primary">张三</h4>
                                <span class="text-xs text-gray-medium ml-2">刚刚</span>
                            </div>
                            <p>${randomReply}</p>
                        </div>
                    `;
                chatMessages.appendChild(replyDiv);
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }, 2000);
        }
    }
});

// 移动端菜单切换
document.getElementById('sidebar-toggle').addEventListener('click', function () {
    document.querySelector('.sidebar').classList.toggle('hidden');
    document.querySelector('.sidebar').classList.toggle('lg:block');
    document.querySelector('.chat-area').classList.toggle('lg:w-2/4');
    document.querySelector('.chat-area').classList.toggle('lg:w-full');
});

document.getElementById('back-btn').addEventListener('click', function () {
    document.querySelector('.sidebar').classList.toggle('hidden');
    document.querySelector('.sidebar').classList.toggle('lg:block');
    document.querySelector('.chat-area').classList.toggle('lg:w-2/4');
    document.querySelector('.chat-area').classList.toggle('lg:w-full');
});

// 添加在 index.js 底部
function changeChatroom(roomId) {
    window.location.href = "/chat?chatroomId=" + roomId;
}

// 初始化聊天室数据
document.addEventListener('DOMContentLoaded', function() {
    // 获取当前聊天室ID
    const currentChatroomId = window.currentChatroomId || 0;
    const currentUserId = window.currentUserId || 0;

    // 高亮显示当前聊天室
    document.querySelectorAll('.chatroom-item').forEach(item => {
        if (parseInt(item.dataset.roomId) === currentChatroomId) {
            item.classList.add('active-chatroom');
        }
    });

    // 移动端返回按钮事件
    document.getElementById('back-btn').addEventListener('click', function() {
        document.querySelector('.sidebar').classList.toggle('hidden');
        document.querySelector('.sidebar').classList.toggle('lg:block');
        document.querySelector('.chat-area').classList.toggle('lg:w-2/4');
        document.querySelector('.chat-area').classList.toggle('lg:w-full');
    });
});