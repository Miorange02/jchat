<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Jchat聊天室</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="static/css/index.css">
    <script src="static/js/index.js"></script>

<body class="bg-gray-light relative">
<div class="shadow-2xl page-container w-2/3 mx-auto">
    <header class="mt-5 rounded-lg bg-primary text-white shadow-md h-16 flex items-center justify-between px-6">
        <div class="flex items-center">
            <button class="lg:hidden text-2xl mr-4" id="sidebar-toggle">
                <i class="fa fa-bars"></i>
            </button>
            <a href="/index"><h1 class="text-xl font-bold">Jchat交你聊天</h1></a>
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
            <a href="#" class="nav-link">
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
            <!-- 搜索框 -->
            <div class="p-4 border-b border-gray-light">
                <div class="relative">
                    <input type="text" placeholder="搜索聊天室"
                           class="w-full py-2 pl-10 pr-4 rounded-lg bg-gray-light focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all">
                    <i class="fa fa-search absolute left-3 top-3 text-gray-medium"></i>
                </div>
            </div>

            <!-- 创建聊天室按钮 -->
            <div class="p-3 border-b border-gray-light">
                <button class="w-full py-2.5 bg-primary text-white rounded-lg hover:bg-secondary transition-colors flex items-center justify-center">
                    <i class="fa fa-plus mr-2"></i> 创建新聊天室
                </button>
            </div>

            <!-- 聊天室列表 -->
            <div class="overflow-y-auto scrollbar-hide flex-1">
                <!-- 活跃聊天室 -->
                <c:forEach items="${chatrooms}" var="chatroom">
                <div class="p-3 bg-primary/10 border-l-4 border-primary cursor-pointer transition-all hover:bg-primary/20">
                    <div class="flex items-center">
<%--                        <div class="relative mr-3">--%>
<%--                            <div class="w-12 h-12 rounded-full bg-primary/20 flex items-center justify-center">--%>
<%--                                <i class="fa fa-users text-primary text-xl"></i>--%>
<%--                            </div>--%>
<%--                        </div>--%>
                        <div class="flex-1 min-w-0">
<%--                            <h3 class="font-semibold text-dark truncate">${chatroom.chatroom.rname}</h3>--%>
                        </div>
                    </div>
                </div>
                </c:forEach>
<%--                <!-- 其他聊天室 -->--%>
<%--                <div class="p-3 cursor-pointer transition-all hover:bg-gray-50">--%>
<%--                    <div class="flex items-center">--%>
<%--                        <div class="relative mr-3">--%>
<%--                            <div class="w-12 h-12 rounded-full bg-purple-100 flex items-center justify-center">--%>
<%--                                <i class="fa fa-code text-purple-600 text-xl"></i>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                        <div class="flex-1 min-w-0">--%>
<%--                            <div class="flex justify-between items-center mb-1">--%>
<%--                                <h3 class="font-semibold text-dark truncate">前端大师</h3>--%>
<%--                                <span class="text-xs text-gray-medium">昨天</span>--%>
<%--                            </div>--%>
<%--                            <div class="flex justify-between items-center">--%>
<%--                                <p class="text-sm text-gray-medium truncate">--%>
<%--                                    李四: Tailwind CSS太好用了！--%>
<%--                                </p>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
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
                        <h2 class="font-semibold">Web开发者社区</h2>
                        <p class="text-xs text-gray-medium">52位成员</p>
                    </div>
                </div>
                <div class="flex items-center space-x-4">
                    <button class="text-xl hover:text-primary transition-colors" id="search-chat-btn">
                        <i class="fa fa-search"></i>
                    </button>
                </div>
            </div>

            <!-- 聊天消息区域 -->
            <div class="flex-1 p-4 overflow-y-auto scrollbar-hide" id="chat-messages">
                <!-- 日期分隔线 -->
                <div class="flex justify-center my-4">
                    <span class="bg-white text-gray-medium text-xs px-3 py-1 rounded-full shadow-sm">今天</span>
                </div>

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

                <div class="flex items-end mb-4 message-animation">
                    <img src="https://picsum.photos/id/1062/100/100" alt="李四" class="w-8 h-8 rounded-full mr-2">
                    <div class="message-in p-3 max-w-[80%] shadow-sm">
                        <div class="flex items-center mb-1">
                            <h4 class="font-medium text-purple-600">李四</h4>
                            <span class="text-xs text-gray-medium ml-2">12:17 PM</span>
                        </div>
                        <p>是的，我一直在研究它们。新的钩子非常强大！</p>
                    </div>
                </div>

                <!-- 我的消息 -->
                <div class="flex items-end justify-end mb-4 message-animation">
                    <div class="message-out p-3 max-w-[80%] shadow-sm">
                        <div class="flex items-center justify-end mb-1">
                            <span class="text-xs text-gray-medium mr-2">12:30 PM</span>
                            <h4 class="font-medium text-dark">你</h4>
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
                    这里是Web开发者社区，欢迎大家交流前端、后端和全栈技术！
                </div>
            <!-- 成员列表头部 -->
            <div class="p-4 border-b border-gray-light">
                <div class="flex justify-between items-center">
                    <h2 class="font-semibold">成员列表</h2>
                    <span class="text-xs bg-primary/10 text-primary px-2 py-1 rounded-full">52位</span>
                </div>
                <div class="relative mt-3">
                    <input type="text" placeholder="搜索成员"
                           class="w-full py-2 pl-10 pr-4 rounded-lg bg-gray-light focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all">
                    <i class="fa fa-search absolute left-3 top-3 text-gray-medium"></i>
                </div>
            </div>

            <!-- 在线成员 -->
            <div class="p-3 border-b border-gray-light">
                <h3 class="text-xs font-medium text-gray-medium mb-2">在线</h3>
                <div class="space-y-3">
                    <div class="flex items-center p-2 hover:bg-gray-50 rounded-lg transition-colors cursor-pointer">
                        <div class="relative mr-3">
                            <img src="https://picsum.photos/id/64/100/100" alt="你"
                                 class="w-10 h-10 rounded-full object-cover">
                            <span class="absolute bottom-0 right-0 w-2.5 h-2.5 bg-green-500 rounded-full border-2 border-white"></span>
                        </div>
                        <div>
                            <h4 class="font-medium">你</h4>
                            <p class="text-xs text-gray-medium">在线</p>
                        </div>
                    </div>

                    <div class="flex items-center p-2 hover:bg-gray-50 rounded-lg transition-colors cursor-pointer">
                        <div class="relative mr-3">
                            <img src="https://picsum.photos/id/1027/100/100" alt="张三"
                                 class="w-10 h-10 rounded-full object-cover">
                            <span class="absolute bottom-0 right-0 w-2.5 h-2.5 bg-green-500 rounded-full border-2 border-white"></span>
                        </div>
                        <div>
                            <h4 class="font-medium">张三</h4>
                            <p class="text-xs text-gray-medium">在线</p>
                        </div>
                    </div>

                    <div class="flex items-center p-2 hover:bg-gray-50 rounded-lg transition-colors cursor-pointer">
                        <div class="relative mr-3">
                            <img src="https://picsum.photos/id/1062/100/100" alt="李四"
                                 class="w-10 h-10 rounded-full object-cover">
                            <span class="absolute bottom-0 right-0 w-2.5 h-2.5 bg-green-500 rounded-full border-2 border-white"></span>
                        </div>
                        <div>
                            <h4 class="font-medium">李四</h4>
                            <p class="text-xs text-gray-medium">在线</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 离线成员 -->
            <div class="overflow-y-auto scrollbar-hide flex-1 p-3">
                <h3 class="text-xs font-medium text-gray-medium mb-2">离线</h3>
                <div class="space-y-3">
                    <div class="flex items-center p-2 hover:bg-gray-50 rounded-lg transition-colors cursor-pointer">
                        <div class="relative mr-3">
                            <img src="https://picsum.photos/id/338/100/100" alt="王五"
                                 class="w-10 h-10 rounded-full object-cover">
                            <span class="absolute bottom-0 right-0 w-2.5 h-2.5 bg-gray-300 rounded-full border-2 border-white"></span>
                        </div>
                        <div>
                            <h4 class="font-medium">王五</h4>
                            <p class="text-xs text-gray-medium">2小时前</p>
                        </div>
                    </div>

                    <div class="flex items-center p-2 hover:bg-gray-50 rounded-lg transition-colors cursor-pointer">
                        <div class="relative mr-3">
                            <img src="https://picsum.photos/id/91/100/100" alt="赵六"
                                 class="w-10 h-10 rounded-full object-cover">
                            <span class="absolute bottom-0 right-0 w-2.5 h-2.5 bg-gray-300 rounded-full border-2 border-white"></span>
                        </div>
                        <div>
                            <h4 class="font-medium">赵六</h4>
                            <p class="text-xs text-gray-medium">5小时前</p>
                        </div>
                    </div>
                </div>
            </div>
        </aside>
    </main>
</div>

<!-- 常驻公告板 -->
<div id="ann-board-container" class="fixed top-4 right-4 bg-white border border-gray-300 rounded-lg p-4 w-60 shadow-xl z-50">
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
</body>
</html>