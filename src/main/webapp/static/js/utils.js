// 时间格式化工具函数
function formatTime(createdAt) {
    const msgTime = new Date(createdAt);
    const now = new Date();
    const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const msgDateStart = new Date(msgTime.getFullYear(), msgTime.getMonth(), msgTime.getDate());
    const diffDays = (todayStart - msgDateStart) / (24 * 60 * 60 * 1000);

    if (diffDays >= 2) {
        return `${msgTime.getFullYear()}-${String(msgTime.getMonth() + 1).padStart(2, '0')}-${String(msgTime.getDate()).padStart(2, '0')} ${String(msgTime.getHours()).padStart(2, '0')}:${String(msgTime.getMinutes()).padStart(2, '0')}`;
    } else if (diffDays === 1) {
        return `昨天 ${String(msgTime.getHours()).padStart(2, '0')}:${String(msgTime.getMinutes()).padStart(2, '0')}`;
    } else {
        return `${String(msgTime.getHours()).padStart(2, '0')}:${String(msgTime.getMinutes()).padStart(2, '0')}`;
    }
}

window.formatTime = formatTime;