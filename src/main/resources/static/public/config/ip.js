// 服务器路径配置
//const ip = 'http://192.168.0.107:9872/zdvsplate';
var ids = [17, 26, 27, 28, 29, 30, 31]
var id = ids[Math.floor((Math.random() * ids.length))];
window.IPTEST = 'http://192.168.20.39:8888/app/getProjectInfo?enterpriseId=' + id;
