<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>管理后台</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css">
    <style type="text/tailwindcss">
        @layer utilities {
            .content-auto {
                content-visibility: auto;
            }
            .sidebar-item {
                @apply flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200;
            }
            .sidebar-item.active {
                @apply bg-primary/10 text-primary font-medium;
            }
            .sidebar-item:not(.active):hover {
                @apply bg-gray-100;
            }
            .btn {
                @apply px-4 py-2 rounded-lg font-medium transition-all duration-200;
            }
            .btn-primary {
                @apply bg-primary text-white hover:bg-primary/90;
            }
            .btn-secondary {
                @apply bg-gray-200 text-gray-700 hover:bg-gray-300;
            }
            .btn-danger {
                @apply bg-danger text-white hover:bg-danger/90;
            }
            .card {
                @apply bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden transition-all duration-200 hover:shadow-md;
            }
            .badge {
                @apply inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium;
            }
            .badge-system {
                @apply bg-warning/10 text-warning;
            }
            .badge-text {
                @apply bg-primary/10 text-primary;
            }
            .table-row-hover {
                @apply hover:bg-gray-50 transition-colors duration-150;
            }
            .fade-in {
                animation: fadeIn 0.3s ease-in-out;
            }
            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(-10px); }
                to { opacity: 1; transform: translateY(0); }
            }
        }
    </style>
</head>
<body class="font-inter bg-gray-50 text-gray-800 min-h-screen flex flex-col">
<!-- 顶部导航栏 -->
<header class="bg-white border-b border-gray-200 shadow-sm sticky top-0 z-30">
    <div class="container mx-auto px-4 py-3 flex items-center justify-between">
        <div class="flex items-center gap-4">
            <button id="sidebar-toggle" class="lg:hidden text-gray-500 hover:text-gray-700">
                <i class="fa fa-bars text-xl"></i>
            </button>
            <div class="flex items-center">
                <i class="fa fa-cogs text-primary text-xl mr-2"></i>
                <h1 class="text-xl font-bold text-gray-800">管理后台</h1>
            </div>
        </div>
        <div class="flex items-center">
            <a href="/logout" class="btn btn-secondary flex items-center gap-2">
                <i class="fa fa-sign-out"></i>
                <span>登出</span>
            </a>
        </div>
    </div>
</header>

<div class="flex flex-1 overflow-hidden">
    <!-- 侧边栏导航 -->
    <aside id="sidebar" class="w-64 bg-white border-r border-gray-200 h-[calc(100vh-3.5rem)] sticky top-[3.5rem] transition-all duration-300 transform -translate-x-full lg:translate-x-0 z-20">
        <nav class="p-4 space-y-1">
            <!-- 将active类添加到用户管理项 -->
            <div class="sidebar-item active" data-page="users">
                <i class="fa fa-users text-lg"></i>
                <span>用户管理</span>
            </div>
            <div class="sidebar-item" data-page="chatrooms">
                <i class="fa fa-comments text-lg"></i>
                <span>聊天室管理</span>
            </div>
            <!-- 移除消息记录项的active类 -->
            <div class="sidebar-item" data-page="messages">
                <i class="fa fa-file-text-o text-lg"></i>
                <span>消息记录</span>
            </div>
        </nav>
    </aside>

    <!-- 主内容区 -->
    <main class="flex-1 overflow-y-auto p-6">
        <div class="max-w-7xl mx-auto">
            <!-- 页面标题 -->
            <div class="mb-8">
                <h2 id="pageTitle" class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold text-gray-800">用户管理</h2>
                <p id="pageDescription" class="text-gray-500 mt-1">管理系统中的所有用户信息</p>
            </div>

            <!-- 内容区域切换 -->
            <div id="usersContent" class="hidden">
                <!-- 用户统计卡片和项目介绍 -->
                <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
                    <!-- 总用户数统计 -->
                    <div class="card p-6 lg:col-span-1">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-gray-500 text-sm">总用户数</p>
                                <h3 class="text-3xl font-bold mt-1">${fn:length(users)}</h3>
                            </div>
                            <div class="bg-primary/10 p-3 rounded-lg">
                                <i class="fa fa-users text-primary text-xl"></i>
                            </div>
                        </div>
                    </div>

                    <!-- 项目介绍 -->
                    <div class="card p-6 lg:col-span-2">
                        <div class="flex items-start justify-between">
                            <div>
                                <h3 class="text-lg font-semibold text-gray-900 mb-2">项目介绍</h3>
                                <p class="text-gray-600 leading-relaxed">
                                    本系统是一个功能完善的用户管理平台，旨在帮助管理员高效管理用户信息、聊天室和系统设置。平台提供了直观的用户界面、丰富的统计数据和便捷的操作功能，支持用户信息的增删改查、在线状态监控以及批量操作等功能。
                                </p>
                                <div class="mt-4 flex items-center gap-3">
                                    <span class="text-xs font-medium bg-primary/10 text-primary px-3 py-1 rounded-full">用户管理</span>
                                    <span class="text-xs font-medium bg-secondary/10 text-secondary px-3 py-1 rounded-full">实时监控</span>
                                    <span class="text-xs font-medium bg-warning/10 text-warning px-3 py-1 rounded-full">数据统计</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 用户表格 -->
                <div class="card overflow-hidden">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                            <tr>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">用户信息</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">用户名</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">密码</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                            </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach items="${users}" var="user">
                                <tr class="table-row-hover">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="flex items-center">
                                            <div class="flex-shrink-0 h-10 w-10">
                                                <img class="h-10 w-10 rounded-full object-cover" src="../static/avatar/${user.avatarUrl}" alt="用户头像">
                                            </div>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-gray-900">${user.uname}</div>
                                        <div class="text-xs text-gray-500">${user.email}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">${user.password}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">${user.createdAt}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                        <button onclick="showEditUserModal(${user.id})" class="text-primary hover:text-primary/80 mr-3">
                                            <i class="fa fa-edit mr-1"></i>编辑
                                        </button>
                                        <button onclick="deleteUser(${user.id})" class="text-danger hover:text-danger/80">
                                            <i class="fa fa-trash mr-1"></i>删除
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- 聊天室内容区域 -->
            <div id="chatroomsContent" class="hidden">

                <!-- 聊天室表格 -->
                <div class="card overflow-hidden">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                            <tr>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">聊天室名称</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">描述</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建者</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">成员数</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                            </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach items="${chatrooms}" var="chatroom">
                                <tr class="table-row-hover">
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${chatroom.id}</td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-gray-900">${chatroom.rname}</div>
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="text-sm text-gray-900">${chatroom.description}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">用户${chatroom.creator}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">${chatroom.memberCount}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">${chatroom.createdAt}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                        <button onclick="showEditChatroomModal(${chatroom.id}, '${chatroom.rname}', '${chatroom.description}')" class="text-primary hover:text-primary/80 mr-3">
                                            <i class="fa fa-edit mr-1"></i>编辑
                                        </button>
                                        <button onclick="deleteChatroom(${chatroom.id})" class="text-danger hover:text-danger/80">
                                            <i class="fa fa-trash mr-1"></i>删除
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- 消息记录内容区域 -->
            <div id="messagesContent" class="hidden">
                <!-- 操作栏 -->
                <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
                    <div class="flex items-center gap-3">
                        <button id="deleteSelectedBtn" class="btn btn-danger disabled:opacity-50 disabled:cursor-not-allowed" disabled>
                            <i class="fa fa-trash mr-2"></i>删除选中
                        </button>
                    </div>
                    <div class="relative w-full sm:w-64">
                        <input type="text" id="messageSearch" placeholder="搜索消息..." class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 w-full">
                        <i class="fa fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                    </div>
                </div>

                <!-- 消息记录表格 -->
                <div class="card overflow-hidden">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                            <tr>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    <input type="checkbox" id="selectAll">
                                </th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">用户</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">聊天室ID</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">消息内容</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">时间</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                            </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200" id="messageTableBody">
                            <!-- 消息记录将通过Java代码填充 -->
                            <c:forEach items="${messages}" var="message">
                                <tr class="table-row-hover">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <input type="checkbox" class="message-checkbox" data-id="${message.id}">
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${message.id}</td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="flex items-center">
                                            <div class="text-sm">${message.userId}</div>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm">#${message.chatroomId}</div>
                                    </td>
                                    <td class="px-6 py-4 max-w-xs">
                                        <div class="text-sm line-clamp-2">${message.content}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                                <span class="badge ${message.type == 'text' ? 'badge-text' : 'badge-system'}">
                                                        ${message.type == 'text' ? '文本消息' : '系统消息'}
                                                </span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            ${message.createdAt}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                        <button onclick="deleteMessage(${message.id})" class="text-danger hover:text-danger/80">
                                            <i class="fa fa-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<!-- 编辑用户模态框 -->
<div id="editUserModal" class="fixed inset-0 bg-black bg-opacity-50 z-50 hidden flex items-center justify-center p-4">
    <div class="bg-white rounded-xl shadow-xl w-full max-w-md fade-in">
        <div class="p-6">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-semibold text-gray-900">编辑用户</h3>
                <button onclick="closeEditUserModal()" class="text-gray-400 hover:text-gray-500">
                    <i class="fa fa-times text-xl"></i>
                </button>
            </div>
            <form id="editUserForm" method="post">
                <input type="hidden" id="editUserId" name="id">
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
                    <input type="text" id="editUserName" name="uname" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary">
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
                    <input type="email" id="editUserEmail" name="email" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary">
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">密码</label>
                    <input type="text" id="editUserPassword" name="password" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary">
                </div>
                <div class="flex justify-end space-x-3 mt-6">
                    <button type="button" onclick="closeEditUserModal()" class="btn btn-secondary">取消</button>
                    <button type="submit" class="btn btn-primary">保存</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- 添加聊天室模态框 -->
<div id="addChatroomModal" class="fixed inset-0 bg-black bg-opacity-50 z-50 hidden flex items-center justify-center p-4">
    <div class="bg-white rounded-xl shadow-xl w-full max-w-md fade-in">
        <div class="p-6">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-semibold text-gray-900">添加聊天室</h3>
                <button onclick="closeAddChatroomModal()" class="text-gray-400 hover:text-gray-500">
                    <i class="fa fa-times text-xl"></i>
                </button>
            </div>
            <form id="addChatroomForm" method="post">
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">聊天室名称</label>
                    <input type="text" id="chatroomName" name="rname" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary">
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
                    <textarea id="chatroomDescription" name="description" rows="3" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary"></textarea>
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">创建者ID</label>
                    <input type="number" id="chatroomCreator" name="creator" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary">
                </div>
                <div class="flex justify-end space-x-3 mt-6">
                    <button type="button" onclick="closeAddChatroomModal()" class="btn btn-secondary">取消</button>
                    <button type="submit" class="btn btn-primary">创建</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- 编辑聊天室模态框 -->
<div id="editChatroomModal" class="fixed inset-0 bg-black bg-opacity-50 z-50 hidden flex items-center justify-center p-4">
    <div class="bg-white rounded-xl shadow-xl w-full max-w-md fade-in">
        <div class="p-6">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-semibold text-gray-900">编辑聊天室</h3>
                <button onclick="closeEditChatroomModal()" class="text-gray-400 hover:text-gray-500">
                    <i class="fa fa-times text-xl"></i>
                </button>
            </div>
            <form id="editChatroomForm" method="post">
                <input type="hidden" id="editChatroomId" name="id">
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">聊天室名称</label>
                    <input type="text" id="editChatroomName" name="rname" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary">
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
                    <textarea id="editChatroomDescription" name="description" rows="3" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary"></textarea>
                </div>
                <div class="flex justify-end space-x-3 mt-6">
                    <button type="button" onclick="closeEditChatroomModal()" class="btn btn-secondary">取消</button>
                    <button type="submit" class="btn btn-primary">保存</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="../static/js/admin.js"></script>
</body>
</html>
