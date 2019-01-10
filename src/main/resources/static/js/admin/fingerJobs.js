// 初始化样式
$(function () {
    $(".fingerJobsPage").addClass("active");
})
// admin/fingerJobsController
myapp.controller("fingerJobsController",["$scope","$http","$location",function ($scope, $http, $location) {
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

    // 修改状态
    var lock1 = false; //默认未锁定
    $scope.jobsEditStatus = function (fId,fsId) {
        var index =  layer.load(0, {shade: false});
        if(!lock1) {
            lock1 = true;  // 锁定
            $http({
                method : 'post',
                url : ctx + "appJson/admin/jobsEditStatus",
                params:{"fId": fId,"fsId":fsId}
            }).success(function (data) {
                lock1 = false;
                layer.close(index);
                into();
            })
        }
    }

    // 派单
    $scope.fl = {}
    $scope.alertSet = function(id,name){
        $scope.fl = {}
        $scope.fl.fId = id;
        $scope.fl.f_name = name;
        $("#ladate1").val("")
        $("#ladate2").val("")
        $('#myModal').modal()
    }

    // 保存
    $scope.submitFingerLog = function () {
        var index =  layer.load(0, {shade: false});
        if(!lock1) {
            lock1 = true;  // 锁定
            if(undefined == $scope.fl.no || "" == $scope.fl.no){
               layer.alert( '单号不能为空!', {
                    title:'提示',
                    skin: 'layui-layer-lan'
                    ,closeBtn: 0
                });
                lock1 = false;
                layer.close(index);
            }else{
                $scope.fl.startdate = $("#ladate1").val();
                $scope.fl.enddate = $("#ladate2").val();
                $http({
                    method : 'post',
                    url : ctx + "appJson/admin/addFingerLog",
                    data : JSON.stringify($scope.fl)
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

    // laydate国际版
    laydate.render({
        elem: '#ladate1'
        ,type: 'datetime'
    });
    laydate.render({
        elem: '#ladate2'
        ,type: 'datetime'
    });

    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);
