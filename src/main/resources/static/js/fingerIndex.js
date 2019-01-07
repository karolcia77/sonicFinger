var myapp = angular.module("myapp",[]);
// fingerIndexController
myapp.controller("fingerIndexController",["$scope","$http","$location",function ($scope, $http, $location) {
    // 初始化
    $scope.fingerUsers = {};

    function into(){
        $http({
            method : 'post',
            url : ctx + "appJson/admin/getFingerJobsAll",
        }).success(function (data) {
            if(data){
                /* 成功*/
                $scope.fingerUsers = data;
            }
        })
    }
    into();

    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://localhost:8081/finger/websocket");
    }
    else{
        alert('Not support websocket')
    }
    //连接发生错误的回调方法
    websocket.onerror = function(){
        alert('实时更新失败')
    };
    //连接成功建立的回调方法
    websocket.onopen = function(event){
        alert('实时更新成功')
    }

    //接收到消息的回调方法
    websocket.onmessage = function(event){
        console.log(event.data)
        if("ok" == event.data ){
            into();
        }
    }
    //连接关闭的回调方法
    websocket.onclose = function(){
        alert('关闭实时更新')
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }
    //关闭连接
    function closeWebSocket(){
        websocket.close();
    }


    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);
