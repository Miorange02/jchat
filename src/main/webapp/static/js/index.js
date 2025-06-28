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
    // 公告板关闭功能
    const closeButton = document.getElementById('close-ann-board');
    const annBoardContainer = document.getElementById('ann-board-container');
    if (closeButton && annBoardContainer) {
        closeButton.addEventListener('click', function() {
            annBoardContainer.style.display = 'none';
        });
    }

    // 高亮当前聊天室
    const currentChatroomId = window.currentChatroomId || 0;
    document.querySelectorAll('.chatroom-item').forEach(item => {
        if (parseInt(item.dataset.roomId) === currentChatroomId) {
            item.classList.add('active-chatroom');
        }
    });

    // 移动端菜单切换
    document.getElementById('sidebar-toggle').addEventListener('click', function() {
        document.querySelector('.sidebar').classList.toggle('hidden');
        document.querySelector('.sidebar').classList.toggle('lg:block');
        document.querySelector('.chat-area').classList.toggle('lg:w-2/4');
        document.querySelector('.chat-area').classList.toggle('lg:w-full');
    });

    // 移动端返回按钮
    document.getElementById('back-btn').addEventListener('click', function() {
        document.querySelector('.sidebar').classList.toggle('hidden');
        document.querySelector('.sidebar').classList.toggle('lg:block');
        document.querySelector('.chat-area').classList.toggle('lg:w-2/4');
        document.querySelector('.chat-area').classList.toggle('lg:w-full');
    });
});
