// 初始化样式
$(function () {
    $(".fingerRecodingPage").addClass("active");
})

// admin/fingerRecodingController
myapp.controller("fingerRecodingController",["$scope","$http","$location",function ($scope, $http, $location) {
    // 初始化
    $scope.myRs = {};
    // 分页
    $scope.PageCount = 0; // 总数
    $scope.CurrentPage = 1; // 当前页
    $scope.PageSize = 10; // 显示页数


    function into(CurrentPage,PageSize){
        var dataMap = {
            CurrentPage : CurrentPage,
            PageSize : PageSize
        }
        $http({
            method : 'post',
            url : ctx + "appJson/admin/getFingerRecordingPage",
            data : JSON.stringify(dataMap)
        }).success(function (data) {
            if(data){
                /* 成功*/
                $scope.myRs = data.result.fingerRecordings;
                $scope.PageCount = data.result.PageCount;
                if($scope.PageCount > 0){
                    $scope.Paginator($scope.PageCount,CurrentPage,PageSize);
                }else{
                    // 没有数据时不显示
                    $('#pagination').jqPaginator('destroy');
                };
            }
        })
    }
    into($scope.CurrentPage,$scope.PageSize);


    // 分页
    $scope.Paginator = function(PageCount,CurrentPage,PageSize){
        if(PageCount == 0){
            return;
        }
        var myPageCount = PageCount;
        var myPageSize = PageSize;
        var countindex = myPageCount % myPageSize > 0 ? (myPageCount / myPageSize) + 1 : (myPageCount / myPageSize);
        $.jqPaginator('#pagination', {
            totalPages: parseInt(countindex),
            visiblePages: 7, //显示分页数
            currentPage: CurrentPage,
            first: '<li class="first"><a href="javascript:;">First</a></li>',
            prev: '<li class="prev"><a href="javascript:;"><i class="arrow arrow2"></i>Prev</a></li>',
            next: '<li class="next"><a href="javascript:;">Next<i class="arrow arrow3"></i></a></li>',
            last: '<li class="last"><a href="javascript:;">Last</a></li>',
            page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
            onPageChange: function (num, type) {
                if (type == "change") {
                    $scope.CurrentPage = num;
                    into($scope.CurrentPage,$scope.PageSize);
                }
            }
        });
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
        if("ok" == event.data ){
            into($scope.CurrentPage,$scope.PageSize);
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

    // laydate国际版
    laydate.render({
        elem: '#ladate1'
    });
    laydate.render({
        elem: '#ladate2'
    });

    // 下载报表
    $scope.getReportRecodings = function(){
        var start = $("#ladate1").val();
        var end = $("#ladate2").val();
        if(end != ""){
            end = end +" 23:59:59";
        }
        var dataMap = {
            startTime : start,
            endTime : end
        }
        clicked(ctx + "appJson/admin/getReportRecodings?startTime="+start+"&endTime="+end)
    }



    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);
