<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Jchat聊天室</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="static/css/index.css">
    <script src="static/js/index.js"></script>
    <script>
        // 全局变量，用于JS访问
        var currentUserId = ${not empty sessionScope.user ? sessionScope.user.id : 0};
        var currentChatroomId = ${currentChatroomId};
    </script>
</head>
<body class="bg-gray-light relative">
<div class="shadow-2xl page-container w-2/3 mx-auto">
    <header class="mt-5 rounded-lg bg-primary text-white shadow-md h-16 flex items-center justify-between px-6">
        <div class="flex items-center">
            <button class="lg:hidden text-2xl mr-4" id="sidebar-toggle">
                <i class="fa fa-bars"></i>
            </button>
            <a href="/chat"><h1 class="text-xl font-bold">Jchat交你聊天</h1></a>
        </div>
        <div class="flex items-center space-x-4">
            <c:if test="${not empty sessionScope.user}">
                <img src="static/avatar/${sessionScope.user.avatarUrl}" alt="用户头像"
                     class="w-8 h-8 rounded-full object-cover border-2 border-white avatar-hover">
                <span class="hidden md:inline-block font-semibold">${sessionScope.user.uname}</span>
                <a href="#" class="nav-link">
                    <i class="fa fa-cog nav-link-icon"></i>
                    <span class="hidden md:inline-block">设置</span>
                </a>
                <a href="logout" class="nav-link">
                    <i class="fa fa-sign-out nav-link-icon"></i>
                    <span class="hidden md:inline-block">登出</span>
                </a>
            </c:if>
        </div>
    </header>

    <!-- 主内容区 -->
    <main class="chat-container" style="height: calc(100vh - 64px - 40px);">
        <!-- 左侧聊天室列表 -->
        <aside class="sidebar bg-white">
            <!-- 原搜索框部分 -->
            <div class="p-4 border-b border-gray-200">
                <form id="search-form" action="/chatroom" method="post">
                    <input type="hidden" name="action" value="search">
                    <div class="relative">
                        <input
                                type="text"
                                name="keyword"
                                placeholder="搜索聊天室"
                                class="w-full py-2 pl-10 pr-8 rounded-lg bg-gray-100 border-0 focus:ring-2 focus:ring-primary/50 focus:bg-white transition-all"
                                value="${param.keyword}"
                        >
                        <i class="fa fa-search absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                        <a href="/chat" class="absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 hover:text-primary transition-colors">
                            <i class="fa fa-times"></i>
                        </a>
                    </div>
                </form>
            </div>

            <div class="p-3 border-b border-gray-light">
                <button id="create-chatroom-btn" class="w-full py-2.5 bg-primary text-white rounded-lg hover:bg-secondary transition-colors flex items-center justify-center">
                    <i class="fa fa-plus mr-2"></i> 创建新聊天室
                </button>
            </div>

            <c:if test="${not empty searchResults}">
                <div class="p-3 bg-yellow-50 border-l-4 border-yellow-400 mb-2">
                    <div class="space-y-2 mt-2">
                        <c:forEach items="${searchResults}" var="result">
                            <div class="p-3 bg-white rounded-lg shadow-sm border border-gray-100 cursor-pointer transition-all hover:shadow-md hover:border-primary/80">
                                <div class="flex items-center justify-between">
                                    <div class="flex-1 min-w-0">
                                        <h3 class="font-semibold text-gray-800 truncate">${result.rname}</h3>
                                        <p class="text-xs text-gray-400">${result.memberCount}人</p>
                                    </div>
                                    <!-- 修改按钮为 AJAX 触发（关键修改） -->
                                    <button onclick="joinChatroom(${result.id})"
                                    class="ml-4 px-3 py-1.5 bg-primary text-white text-sm rounded-lg hover:bg-secondary transition-colors"
                                    >
                                    加入
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <!-- 聊天室列表 -->
            <div class="overflow-y-auto scrollbar-hide flex-1">
                <!-- 活跃聊天室 -->
                <c:forEach items="${chatrooms}" var="chatroom">
                <div class="p-3 bg-white rounded-lg shadow-sm border border-gray-100 cursor-pointer transition-all hover:shadow-md hover:border-primary/80 group
    ${chatroom.chatroom.id == currentChatroomId ? 'bg-blue-50 border-primary' : ''}"
                     onclick="window.changeChatroom(${chatroom.chatroom.id})"
                     data-room-id="${chatroom.chatroom.id}">
                    <div class="flex items-center space-x-3">
                        <div class="flex-1 min-w-0">
                            <div class="flex justify-between items-center">
                                <h3 class="font-semibold text-gray-800 truncate">${chatroom.chatroom.rname}</h3>
                                <span class="text-xs text-gray-400">${chatroom.chatroom.memberCount}人</span>
                            </div>
                            <p class="text-xs text-gray-500 mt-1 flex items-center">
                                <i class="fa fa-user-circle mr-1 text-gray-400"></i>
                                <span class="truncate">${chatroom.user.uname}</span>
                            </p>
                        </div>
                    </div>
                </div>
                </c:forEach>
        </aside>

        <!-- 中间聊天区域 -->
        <section class="chat-area bg-gray-50">
            <!-- 聊天头部 -->
            <div class="bg-white border-b border-gray-light p-4 flex items-center justify-between">
                <div class="flex items-center">
                    <button class="lg:hidden text-xl mr-3" id="back-btn">
                        <i class="fa fa-arrow-left"></i>
                    </button>
                    <div class="relative mr-3">
                        <div class="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
                            <i class="fa fa-users text-primary text-lg"></i>
                        </div>
                    </div>
                    <div>
                        <h2 class="font-semibold">${currentChatroom.rname}</h2>
                        <p class="text-xs text-gray-medium">${currentChatroom.memberCount} 成员</p>
                    </div>
                </div>
                <div class="flex items-center space-x-4">
                    <button class="text-md hover:text-primary transition-colors" id="exit-chatroom-btn">
                        <i class="fa fa-sign-out"></i> 退出群聊
                    </button>
                </div>
            </div>

            <!-- 聊天消息区域 -->
            <div class="flex-1 p-4 overflow-y-auto scrollbar-hide" id="chat-messages">

                <!-- 系统消息 -->
                <div class="flex justify-center mb-4">
                    <div class="bg-gray-200 text-gray-medium text-xs px-3 py-1 rounded-full">
                        你加入了聊天室
                    </div>
                </div>

                <!-- 成员消息 -->
                <div class="flex items-end mb-4 message-animation">
                    <img src="https://picsum.photos/id/1027/100/100" alt="张三" class="w-8 h-8 rounded-full mr-2">
                    <div class="message-in p-3 max-w-[80%] shadow-sm">
                        <div class="flex items-center mb-1">
                            <h4 class="font-medium text-primary">张三</h4>
                            <span class="text-xs text-gray-medium ml-2">12:15 PM</span>
                        </div>
                        <p>早上好！有人看过React最新版本的新特性吗？</p>
                    </div>
                </div>


                <!-- 我的消息 -->
                <div class="flex items-end justify-end mb-4 message-animation">
                    <div class="message-out p-3 max-w-[80%] shadow-sm">
                        <div class="flex items-center justify-end mb-1">
                            <span class="text-xs text-gray-medium mr-2">12:30 PM</span>
                            <h4 class="font-medium text-dark">David</h4>
                        </div>
                        <p>我昨天刚升级了我的项目，目前运行得很好！</p>
                    </div>
                </div>
            </div>

            <!-- 消息输入区域 -->
            <div class="bg-white p-4 border-t border-gray-light">
                <div class="flex items-center">
                    <input type="text" placeholder="输入消息..."
                           class="flex-1 py-2 px-4 bg-gray-light rounded-full focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all"
                           id="message-input">
                </div>
            </div>
        </section>

        <!-- 右侧成员列表 hidden xl:block-->
        <aside class="members-list bg-white ">
            <div class="m-2 text-xs text-gray-medium bg-gray-100 rounded p-2" id="chatroom-desc">
                ${currentChatroom.description}
            </div>
            <!-- 成员列表头部 -->
            <div class="p-4 border-b border-gray-light">
                <div class="flex justify-between items-center">
                    <h2 class="font-semibold">成员列表</h2>
                </div>
            </div>

            <!-- 在线成员 -->
            <div class="p-3 border-b border-gray-light">
                <h3 class="text-xs font-medium text-gray-medium mb-2">成员列表</h3>
                <div class="space-y-3">
                    <c:forEach items="${members}" var="member">
                        <div class="flex items-center p-2 hover:bg-gray-50 rounded-lg transition-colors cursor-pointer">
                            <div class="relative mr-3">
                                <img src="static/avatar/${member.user.avatarUrl}" alt="${member.user.uname}"
                                     class="w-10 h-10 rounded-full object-cover">
                                <span class="absolute bottom-0 right-0 w-2.5 h-2.5 ${member.user.status == 'online' ? 'bg-green-500' : 'bg-gray-300'} rounded-full border-2 border-white"></span>
                            </div>
                            <div>
                                <h4 class="font-medium">${member.user.uname}
                                    <c:if test="${member.user.id == currentChatroom.creator}">
                                        <span class="text-xs bg-blue-100 text-blue-800 px-1 rounded">创建者</span>
                                    </c:if>
                                </h4>
                                <p class="text-xs text-gray-medium">
                                    <c:choose>
                                        <c:when test="${member.user.status == 'online'}">在线</c:when>
                                        <c:otherwise>
                                            最后活跃: ${member.user.lastActiveDisplay}
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </aside>
    </main>
</div>

<!-- 常驻公告板 -->
<div id="ann-board-container"
     class="fixed top-4 right-4 bg-white border border-gray-300 rounded-lg p-4 w-60 shadow-xl z-50">
    <div class="flex justify-between items-center mb-2">
        <h3 class="font-semibold">系统公告</h3>
        <button class="text-gray-600 hover:text-red-500" id="close-ann-board">
            <i class="fa fa-times"></i>
        </button>
    </div>
    <div id="ann-board" class="max-h-64 overflow-y-auto">
        <!-- 公告内容动态插入 -->
        示例公告内容，可根据实际情况更新。
    </div>
</div>

<!-- 在页面底部添加创建聊天室模态框 -->
<div id="create-modal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center hidden z-50">
    <div class="bg-white rounded-lg p-6 w-96">
        <h3 class="text-xl font-semibold mb-4">创建新聊天室</h3>
        <form id="create-form" action="/chatroom" method="post">
            <input type="hidden" name="action" value="create">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="rname">聊天室名称</label>
                <input type="text" id="rname" name="rname" required
                       class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary">
            </div>
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="description">描述</label>
                <textarea id="description" name="description" rows="3"
                          class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"></textarea>
            </div>
            <div class="flex justify-end">
                <button type="button" id="cancel-create" class="mr-2 px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-100">取消</button>
                <button type="submit" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-secondary">创建</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>