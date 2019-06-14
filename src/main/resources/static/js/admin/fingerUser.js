// 初始化样式
$(function () {
    $(".fingerUserPage").addClass("active");
})
// admin/fingerUserController
myapp.controller("fingerUserController",["$scope","$http","$location",function ($scope, $http, $location) {
    // 初始化
    $scope.fingerUsers = {};

    function into(){
        $http({
            method : 'post',
            url : ctx + "appJson/admin/getFingerUserAll",
        }).success(function (data) {
            if(data){
                /* 成功*/
                $scope.fingerUsers = data;
            }
        })
    }

    // 复选框
    $scope.selected = [];
    $scope.updateSelection = function($event, id){
        var checkbox = $event.target;
        var action = (checkbox.checked?'add':'remove');
        if(action == 'add' && $scope.selected.indexOf(id) == -1){
            $scope.selected.push(id);
            var flag = true;
            angular.forEach($("[name='checkboxClien']:checkbox"), function (each) {
                if(!each.checked){
                    flag = false;
                }
            })
            $("[name='checkboxAll']:checkbox").prop("checked", flag);
        }
        if(action == 'remove' && $scope.selected.indexOf(id)!=-1){
            var idx = $scope.selected.indexOf(id);
            $scope.selected.splice(idx,1);
            $("[name='checkboxAll']:checkbox").prop("checked", false);
        }
    }
    $scope.updateSelectionAll = function($event){
        var checkbox = $event.target;
        var action = (checkbox.checked?'add':'remove');
        if(action == 'add'){
            $("[name='checkboxClien']:checkbox").prop("checked", true);
            angular.forEach($("[name='checkboxClien']:checkbox"), function (each) {
                $scope.selected.push(Number(each.value));
            })
        }
        if(action == 'remove'){
            $("[name='checkboxClien']:checkbox").prop("checked", false);
            $scope.selected = [];
        }
    }


    // 编辑
    $scope.getEdit = function(id){
        clicked(ctx + "appPage/admin/fingerUserEdit?fId="+id);
    }

    // 修改状态
    var lock2 = false; //默认未锁定
    $scope.editStatus = function (id,status,type) {
        if(!lock2){
            lock2 = true;  // 锁定
            var delId = id;
            if(id == 0){// 全部
                var txt = "您想要显示以下所有内容吗？";
                if(type){
                    txt = "你想隐藏以下所有内容吗？";
                }
                delId = $scope.selected.join("-");
                var i = 0;
                angular.forEach($("[name='checkboxClien']:checkbox"), function (each) {
                    if(each.checked){
                        i++;
                    }
                })
                if(i==0){
                    layer.alert("请检查勾选");
                    lock2 = false;
                    return;
                }
                var myconfirm = layer.confirm(txt, {
                    title:'提示',
                    btn: ['确定','取消'] //按钮
                }, function(){
                    CanEditStatus(delId,status);
                    layer.close(myconfirm);
                }, function(){
                    lock2 = false;
                    layer.close(myconfirm);
                });
            }else{ // 单个
                CanEditStatus(delId,status);
            }


        };
    }
    // 能编辑状态
    function  CanEditStatus(dlIds,status) {
        $http({
            method : 'post',
            url : ctx + "appJson/admin/fingerUser/editStatus",
            params:{"fIds": dlIds ,"status" : status}
        }).success(function (data) {
            /* 成功*/
            var index = layer.alert( 'Success', {
                title:'Information',
                skin: 'layui-layer-lan'
                ,closeBtn: 0
            },function () {
                lock2 = false;
                into();
                $scope.selected = [];
                $("[name='checkboxAll']:checkbox").prop("checked", false);
                layer.close(index);
            });
        })
    }


    // 删除
    var lock3 = false; //默认未锁定
    $scope.getDelete = function(id){
        if(!lock3) {
            lock3 = true;  // 锁定

            var delId = id;
            var txt = "你确定要删除吗？";
            if (id == 0) {
                delId = $scope.selected.join("-");
                txt = "是否要删除以下内容？";
                var i = 0;
                angular.forEach($("[name='checkboxClien']:checkbox"), function (each) {
                    if (each.checked) {
                        i++;
                    }
                })
                if (i == 0) {
                    layer.alert("请检查勾选");
                    lock3 = false;
                    return;
                }
            }
            var myconfirm = layer.confirm(txt, {
                title: '提示',
                btn: ['确定', '取消'] //按钮
            }, function () {
                Candelete(delId);
                layer.close(myconfirm);
            }, function () {
                lock3 = false;
                layer.close(myconfirm);
            });
        }

    }
    // 能删除
    function Candelete(dlIds) {
        $http({
            method : 'post',
            url : ctx + "appJson/admin/fingerUser/delete",
            params:{"fIds": dlIds}
        }).success(function (data) {
            lock3 = false;
            into();
            $scope.selected = [];
            $("[name='checkboxAll']:checkbox").prop("checked", false);
        })
    }


    /* 搜索框  开始*/
    $scope.getSearchTitleq = function () {
        $http({
            method : 'post',
            url : ctx + "appJson/admin/fingerUser/getSearch",
            params:{"search" : $scope.searchTest}
        }).success(function (data) {
            if(data){
                /* 成功*/
                $scope.fingerUsers = data;
            }
        })
    }
    $scope.searchTest = "";
    $scope.getSearchTitle = function () {
        var q = escape($scope.searchTest);
        var url = ctx + "appPage/admin/fingerUser?q="+q;
        clicked(encodeURI(url));
    }
    $scope.onKeyup = function(event){
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if(e && e.keyCode==13){ // enter 键
            $scope.getSearchTitle();
        }
    }
    var sq = $location.search().q;
    if(undefined != sq && "" != sq){
        $scope.searchTest = unescape(sq);
        $scope.getSearchTitleq();
    }else{
        into();
    }
    /* 搜索框  结束*/

    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);


// admin/fingerUserEditController
myapp.controller("fingerUserEditController",["$scope","$http","$location",function ($scope, $http, $location) {
    $scope.fId = GetUrlParam("fId")==""?0:GetUrlParam("fId");
    $scope.user = {};

    // 初始化
    $scope.info1 = function(){
        $http({
            method : 'post',
            url : ctx + "appJson/admin/getFingerUserEdit",
            params:{"fId": $scope.fId}
        }).success(function (data) {
            $scope.user = data.result.user;
            $("#fingerId").val($scope.user.finger);
            $("#fingerTemplate10").val($scope.user.fingerTxt);

            if($scope.user){
                $scope.editType = "< Edit <" + $scope.user.name;
            }else{
                $scope.editType = "< Add";
            }
        })
    }
    $scope.info1();

    // 启动指纹
    myfunction();



    // update提交
    var lock1 = false; //默认未锁定
    $scope.submitUpdate = function () {
        //判断
        if(!chekFrom()){
            return;
        };
        if(!lock1) {
            var index =  layer.load(0, {shade: false});
            lock1 = true; // 锁定
            $scope.user.finger = $("#fingerId").val();
            $scope.user.fingerTxt = $("#fingerTemplate10").val();
            $http({
                method : 'post',
                url : ctx + 'appJson/admin/fingerUserUpdate',
                data : $scope.user
            }).success(function(resp){
                layer.alert( 'Success', {
                    title:'Information',
                    skin: 'layui-layer-lan'
                    ,closeBtn: 0
                },function () {
                    var url = ctx + "appPage/admin/fingerUser";
                    clicked(url);
                });
                layer.close(index);
            });
        }
    }

    // 判断title是否为空
    function chekFrom() {
        if( "" == $("#inputTitle").val().trim()){
            layer.alert( '名称不能为空.', {
                title:'Information',
                skin: 'layui-layer-lan'
                ,closeBtn: 0
            })
            return false;
        }
        return true;
    }



    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);
