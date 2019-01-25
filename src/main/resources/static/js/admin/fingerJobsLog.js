// 初始化样式
$(function () {
    $("#navbartop3").addClass("in");
})
// admin/fingerJobsLogController
myapp.controller("fingerJobsLogController",["$scope","$http","$location",function ($scope, $http, $location) {
    // 初始化
    $(".fingerJobsLogPage").addClass("active");
    $scope.myLogs = {};

    function into(){
        $http({
            method : 'post',
            url : ctx + "appJson/admin/getFingerJobsLogAll",
        }).success(function (data) {
            if(data){
                /* 成功*/
                $scope.myLogs = data;
                console.log($scope.myLogs)
            }
        })
    }
    into();


    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);


// fingerJobsYesController 已派单
myapp.controller("fingerJobsYesController",["$scope","$http","$location",function ($scope, $http, $location) {
    // 初始化
    $(".fingerJobsYesPage").addClass("active");
    $scope.myLogs = {};

    function into(){
        $http({
            method : 'post',
            url : ctx + "appJson/admin/getJobsAllYes",
        }).success(function (data) {
            if(data){
                /* 成功*/
                $scope.myLogs = data;
                console.log($scope.myLogs)
            }
        })
    }
    into();


    // 派单
    $scope.fl = {}
    $scope.alertSet = function(f){
        $scope.fl = {}
        $scope.fl = f;
        if($scope.fl.status == 0){
            $scope.fl.statusText = '未完成';
        }else if($scope.fl.status == 1){
            $scope.fl.statusText = '完成';
        }else if($scope.fl.status == 2){
            $scope.fl.statusText = '待做';
        }
        var date2=new Date($scope.fl.fcrStartdate);
        var date1=new Date($scope.fl.fcCreateDate);
        var date3=date2.getTime()-date1.getTime()  //时间差的毫秒数
        if(date3 > 5*60*1000){
            //计算出相差天数
            var days=Math.floor(date3/(24*3600*1000))
            //计算出小时数
            var leave1=date3%(24*3600*1000)    //计算天数后剩余的毫秒数
            var hours=Math.floor(leave1/(3600*1000))
            //计算相差分钟数
            var leave2=leave1%(3600*1000)        //计算小时数后剩余的毫秒数
            var minutes=Math.floor(leave2/(60*1000))
            //计算相差秒数
            var leave3=leave2%(60*1000)      //计算分钟数后剩余的毫秒数
            var seconds=Math.round(leave3/1000)
            $scope.fl.fingerStartText = " 超时 "+days+"天 "+hours+"小时 "+minutes+" 分钟"+seconds+" 秒";
        }else{
            $scope.fl.fingerStartText = " 未 ";
        }



        $('#myModal').modal()
        $('#myModal input[type="text"]').attr("readonly","readonly")
    }

    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);


// fingerJobsNoController 未派单
myapp.controller("fingerJobsNoController",["$scope","$http","$location",function ($scope, $http, $location) {
    // 初始化
    $(".fingerJobsNoPage").addClass("active");
    $scope.myLogs = {};
    $scope.users = {};
    $scope.fId = 0;

    function into(){
        $http({
            method : 'post',
            url : ctx + "appJson/admin/getJobsAllNo",
        }).success(function (data) {
            if(data){
                /* 成功*/
                $scope.myLogs = data.result.fingerCases;
                $scope.users = data.result.fingerUsers;
                console.log(data)
            }
        })
    }
    into();

    // 派单
    $scope.fl = {}
    $scope.alertSet = function(ob){
        $scope.fl = ob;
        $('#myModal').modal()
    }

    // 保存
    var lock1 = false; //默认未锁定
    $scope.submitFingerLog = function () {
        var index =  layer.load(0, {shade: false});
        if(!lock1) {
            lock1 = true;  // 锁定
            if($scope.fId == 0){
                layer.alert( '请选择跟单人!', {
                    title:'提示',
                    skin: 'layui-layer-lan'
                    ,closeBtn: 0
                });
                lock1 = false;
                layer.close(index);
            }else{
                $http({
                    method : 'post',
                    url : ctx + "appJson/admin/addCaseRelation",
                    params : {"fId":$scope.fId,"fcId":$scope.fl.id}
                }).success(function (data) {
                    lock1 = false;
                    layer.alert( '保存成功', {
                        title:'提示',
                        skin: 'layui-layer-lan'
                        ,closeBtn: 0
                    },function () {
                        location.reload();
                    });
                })
            }

        }
    }


    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);
