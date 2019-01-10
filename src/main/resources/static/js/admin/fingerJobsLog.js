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

    function into(){
        $http({
            method : 'post',
            url : ctx + "appJson/admin/getJobsAllNo",
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
