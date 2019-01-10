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
                console.log($scope.fingerUsers)
            }
        })
    }
    into();

    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://"+window.location.host+"/finger/websocket");
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

// fingerSampleIndexController
myapp.controller("fingerSampleIndexController",["$scope","$http","$location",function ($scope, $http, $location) {
   // 启动指纹
    myfunction();
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
                //$scope.editfpComparison1(data.result.fingerUsers);
              /*  //清空指纹图像
                clearFPImage(globalContext, "verification");
                //显示框--采集提示
                collectTips(globalContext, "请水平按压手指验证", "verification");*/
            }
        })
    }

    $scope.editfpComparison1 = function(dataArr){
        var reg = $scope.user.fingerTxt;
        for (var i=0;i<dataArr.length;i++)
        {
            var ver = dataArr[i].fingerTxt.replace("[","").replace("]","");
            $.ajax( {
                type : "POST",
                url : "http://127.0.0.1:22001/ZKBIOOnline/fingerprint/verify",
                dataType : "json",
                data:JSON.stringify({'reg':reg ,'ver':  ver}),
                async: false,
                success : function(data)
                {
                    //返回码
                    var ret = null;
                    ret = data.ret;
                    //接口调用成功返回时
                    if(ret == 0)
                    {
                        console.log("score:" + data.score);
                        if(data.score > 10){
                            $scope.editfpComparison2(dataArr[i].id)
                        }
                    }
                    else
                    {
                        alert("模板出错,是不是多了指纹ret:" + data.ret);
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown)
                {
                    alert("请安装指纹驱动或启动该服务!");
                }
            });

        }
    }


    $scope.editfpComparison2 = function(id){
        $scope.userNew = {}
        $scope.userNew.id = id;
        $http({
            method : 'post',
            url : ctx + "appJson/admin/fpComparisonaddLog",
            data : JSON.stringify($scope.userNew),
        }).success(function (data) {

        })
    }


    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);



