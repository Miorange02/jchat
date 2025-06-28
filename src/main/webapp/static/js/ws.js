let socket;
const currentUserId = window.appConfig?.currentUserId || 0;

// 在initWebSocket中添加重连机制
function initWebSocket() {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}/ws/chat`;

    socket = new WebSocket(wsUrl);

    // 添加心跳检测
    const heartbeatInterval = setInterval(() => {
        if (socket.readyState === WebSocket.OPEN) {
            socket.send(JSON.stringify({type: "heartbeat"}));
        }
    }, 30000);

    socket.onclose = function() {
        clearInterval(heartbeatInterval);
        console.log('尝试在5秒后重连...');
        setTimeout(initWebSocket, 5000);
    };
}

function formatTime(timestamp) {
    const date = new Date(timestamp);
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${month}-${day} ${hours}:${minutes}`;
}

function appendMessage(data) {
    const chatMessages = document.getElementById('chat-messages');

    // 系统消息处理
    if (data.type === 'system') {
        const systemDiv = document.createElement('div');
        systemDiv.className = 'flex justify-center mb-4';
        systemDiv.innerHTML = `
            <div class="bg-gray-200 text-gray-600 text-xs px-3 py-1 rounded-full">
                ${data.content}
            </div>
        `;
        chatMessages.appendChild(systemDiv);
        return;
    }

    const messageDiv = document.createElement('div');
    messageDiv.className = `flex items-end mb-4 ${data.userId === currentUserId ? 'justify-end' : ''}`;

    const isCurrentUser = data.userId === currentUserId;
    const timeString = data.timestamp ? formatTime(data.timestamp) : formatTime(new Date());

    messageDiv.innerHTML = `
        ${!isCurrentUser ? `
            <img src="${data.avatar}" 
                 class="w-8 h-8 rounded-full mr-2" 
                 alt="${data.username}的头像">` : ''}
        <div class="${isCurrentUser ? 'message-out' : 'message-in'} p-3 max-w-[80%] shadow-sm">
            <div class="flex items-center ${isCurrentUser ? 'justify-end' : ''} mb-1">
                ${isCurrentUser ? `
                    <span class="text-xs text-gray-medium mr-2">${timeString}</span>
                    <h4 class="font-medium text-dark">你</h4>
                ` : `
                    <h4 class="font-medium text-primary">${data.username}</h4>
                    <span class="text-xs text-gray-medium ml-2">${timeString}</span>
                `}
            </div>
            <p class="break-words">${data.content}</p>
        </div>
    `;

    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 在文件底部添加初始化滚动
document.addEventListener('DOMContentLoaded', () => {
    const chatMessages = document.getElementById('chat-messages');
    if (chatMessages) {
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
});

// 消息发送功能
// 修改事件监听方式，确保DOM加载完成
document.addEventListener('DOMContentLoaded', function() {
    const inputElement = document.getElementById('message-input');
    if (inputElement) {
        // 修改消息发送事件监听
        inputElement.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault(); // 阻止默认回车行为
                const message = this.value.trim();
                if (!message) return;

                if (!socket || socket.readyState !== WebSocket.OPEN) {
                    console.log('尝试重新连接WebSocket...');
                    initWebSocket();
                }

                // 修改消息发送逻辑
                // 在消息发送前添加校验
                if (!window.currentChatroomId || window.currentChatroomId === 0) {
                    console.error('无效的聊天室ID');
                    return;
                }

                const msg = {
                    type: "message",
                    content: message,
                    chatroomId: Number(window.currentChatroomId),
                    userId: Number(currentUserId)
                };
                console.log('发送消息内容:', JSON.stringify(msg));
                socket.send(JSON.stringify({
                    type: "message",
                    content: message,
                    chatroomId: Number(window.currentChatroomId), // 确保数字类型
                    userId: Number(currentUserId) // 确保数字类型
                }));

                this.value = '';
            }
        });

    } else {
        console.warn('消息输入框元素未找到');
    }
});

// 初始化连接
document.addEventListener('DOMContentLoaded', initWebSocket);
