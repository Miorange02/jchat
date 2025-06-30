// 时间格式化工具函数
function formatTime(createdAt) {
    const msgTime = new Date(createdAt);
    const now = new Date();
    const diff = now - msgTime; // 时间差（毫秒）

    // 超过24小时显示完整日期（yyyy-MM-dd HH:mm）
    if (diff > 48 * 60 * 60 * 1000) {
        return `${msgTime.getFullYear()}-${String(msgTime.getMonth() + 1).padStart(2, '0')}-${String(msgTime.getDate()).padStart(2, '0')} ${String(msgTime.getHours()).padStart(2, '0')}:${String(msgTime.getMinutes()).padStart(2, '0')}`;
    } else if (diff > 24 * 60 * 60 * 1000 && diff <= 48 * 60 * 60 * 1000) {
        return `昨天 ${String(msgTime.getHours()).padStart(2, '0')}:${String(msgTime.getMinutes()).padStart(2, '0')}`;
    } else {
        // 当天显示时分（HH:mm）
        return `${String(msgTime.getHours()).padStart(2, '0')}:${String(msgTime.getMinutes()).padStart(2, '0')}`;
    }
}

window.formatTime = formatTime;