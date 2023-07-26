// export function formatterTime(value) {
//     var date = new Date(value); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
//     var Y = date.getFullYear() + "-";
//     var M =
//         (date.getMonth() + 1 < 10
//             ? "0" + (date.getMonth() + 1)
//             : date.getMonth() + 1) + "-";
//     var D = date.getDate() + " ";
//     var h = date.getHours() + ":";
//     var m = date.getMinutes() + ":";
//     var s = date.getSeconds();
//     return Y + M + D + h + m + s;
// }

// 金额除以100显示
export function formatBalance(value) {
    return value / 100
}

// input为数字类型，输入框不允许输入'e,E'和'+,-'
export function clearInputFn(e) {
    let key = e.key;
    let keyCode = e.keyCode
    if (key === 'e' || key === 'E' || key === '+' || key === '-' || key === '.' || keyCode === 190 || keyCode === 110) {
        e.returnValue = false;
        return false;
    }
    return true;
}
