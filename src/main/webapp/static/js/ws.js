document.addEventListener('DOMContentLoaded', function() {
    const currentChatroomId = window.currentChatroomId;
    const currentUserId = window.currentUserId;
    const currentUserUname = window.currentUserUname;
    const currentUserAvatar = window.currentUserAvatar;
    const ws = new WebSocket(`ws://${window.location.host}/ws/${currentChatroomId}`);
    const messageInput = document.getElementById('message-input');
    const chatMessages = document.getElementById('chat-messages');

    // 新增：页面卸载时关闭 WebSocket 连接
    window.addEventListener('beforeunload', function() {
        if (ws.readyState === WebSocket.OPEN) {
            ws.close(); // 主动关闭连接
        }
    });

    // WebSocket 接收消息（渲染）
    ws.onmessage = function(event) {
        const message = JSON.parse(event.data);
        renderMessage(message);
    };

    // 发送消息（按 Enter，修复无法发送问题）
    if (messageInput) {
        messageInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const content = this.value.trim();
                if (content) {
                    // 关键修改：用user对象替代单独的username和avatarUrl
                    const message = {
                        type: "text",
                        content: content,
                        userId: currentUserId,
                        user: {  // 后端Message类的user字段需要这个结构
                            id: currentUserId,
                            uname: currentUserUname,  // 用户名放在user对象里
                            avatarUrl: currentUserAvatar  // 头像路径也放在user对象里
                        },
                        chatroomId: currentChatroomId,
                        createdAt: new Date().toISOString()
                    };
                    ws.send(JSON.stringify(message));
                    this.value = '';
                }
            }
        });
    }

    // 渲染消息
        function renderMessage(message) {
            const messageDiv = document.createElement('div');
            messageDiv.className = 'mb-4 message-animation flex w-full';

            if (message.type === 'system') {
                messageDiv.className += ' justify-center';
                messageDiv.innerHTML = `
        <div class="bg-gray-200 text-gray-600 text-sm px-3 py-1 rounded-full">
            ${message.content}
        </div>
    `;
            } else {
                const isSelf = message.userId === currentUserId;
                messageDiv.className += ` items-end ${isSelf ? 'justify-end' : 'justify-start'}`;
                messageDiv.innerHTML = `
        <div class="flex items-start ${isSelf ? 'flex-row-reverse' : ''}">
            <!-- Avatar -->
            <img src="${message.user.avatarUrl}" alt="头像"
                   class="w-8 h-8 rounded-full object-cover ${isSelf ? 'ml-3' : 'mr-3'}">
            <!-- Message bubble -->
            <div class="${isSelf ? 'message-out bg-green-100 text-green-800' : 'message-in bg-white text-gray-800'}
                       p-4 max-w-[70%] rounded-lg shadow-sm">
                <div class="flex items-center ${isSelf ? 'justify-end' : 'justify-start'} mb-1">
                    ${!isSelf ? `<h4 class="font-medium text-base text-primary">${message.user.uname}</h4>` : ''}
                    <span class="text-xs text-gray-500 mx-2">
                        ${window.formatTime(message.createdAt)}
                    </span>
                    ${isSelf ? `<h4 class="font-medium text-base text-dark">${currentUserUname}</h4>` : ''}
                </div>
                <p class="text-base">${message.content}</p>zz
            </div>
        </div>
    `;
            }

            chatMessages.appendChild(messageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
}
    //自动滚动到最新消息（最底部）

);
