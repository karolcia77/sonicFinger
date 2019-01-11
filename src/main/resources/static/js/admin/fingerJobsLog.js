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
