var myapp = angular.module("myapp",[]);
// fingerIndexController
myapp.controller("fingerIndexController",["$scope","$http","$location","$interval",function ($scope, $http, $location,$interval) {
    // 初始化
    $scope.fingerUsers = {};

    function into(){
        $http({
            method : 'post',
            url : ctx + "appJson/getFingerIndex",
        }).success(function (data) {
            $scope.fingerUsers = data;
        })
    }
    into();

    // 动画判断指纹签到
    var timer=$interval(function(){
        angular.forEach($scope.fingerUsers,function(hero,index,objs){
            var f = objs[index];
            if(f.fsId == 2 ){
                var d = new Date().getTime();
                var fd = new Date(f.jobsUpdateDate).getTime();
                if((d -fd) > 10*60*1000){
                    $scope.AnimationColor1(f.id);
                }
            }
        });
    },1000);  //间隔1秒定时执行

    // 红灯
    $scope.AnimationColor1 = function(id){
        if($("#Animation"+id).css("background-color") == 'rgb(255, 0, 0)'){
            $("#Animation"+id).css("background-color","");
        }else{
            $("#Animation"+id).css("background-color","red");
        }
    }

    //绿灯
    $scope.AnimationColor2 = function(id){
         $("#Animation"+id).css("background-color","green");
    }

    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://"+window.location.host+"/finger/websocket");
    }
    else{
        console.log('Not support websocket')
    }
    //连接发生错误的回调方法
    websocket.onerror = function(){
        console.log('实时更新失败')
    };
    //连接成功建立的回调方法
    websocket.onopen = function(event){
        console.log('连接成功')
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
        console.log('关闭实时更新')
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }
    //关闭连接
    function closeWebSocket(){
        websocket.close();
    }

    /**
     * 指纹
     * @param url
     */
    $scope.user = {};

    // 提交比较
    $scope.editfpComparison = function(){
        $scope.user.fingerTxt = $("#verifyTemplate").val();
        $http({
            method : 'post',
            url : ctx + "appJson/admin/editfpComparison",
            data : JSON.stringify($scope.user),
        }).success(function (data) {
            console.log(data);
            if(data){
                /* 成功*/
                //showImage(globalContext, ctx + "/img/sample/base_fpVerify_clearImage.png", "clearForVerify");
            }
        })
    }


    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);

// fingerSampleIndexController
myapp.controller("fingerSampleIndexController",["$scope","$http","$location",function ($scope, $http, $location) {
   // 启动指纹
    //myfunction();
    var urlImg = ctx + '/img/sample/base_fpVerify.jpg';
    $("#canvasComp").css("background","url("+urlImg+")  rgb(243, 245, 240)");
    $scope.user = {}

    $scope.users = {}

    // 提交比较
    $scope.editfpComparison = function(){
        $scope.user.fingerTxt = $("#verifyTemplate").val();
        $http({
            method : 'post',
            url : ctx + "appJson/admin/editfpComparison",
            data : JSON.stringify($scope.user),
        }).success(function (data) {
            if(data){
                /* 成功*/
                showImage(globalContext, ctx + "/img/sample/base_fpVerify_clearImage.png", "clearForVerify");
            }
        })
    }


    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);



