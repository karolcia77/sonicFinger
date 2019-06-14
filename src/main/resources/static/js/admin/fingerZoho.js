// 初始化样式
$(function () {
    $(".fingerZohoPage").addClass("active");
})

// admin/fingerZohoController
myapp.controller("fingerZohoController",["$scope","$http","$location",function ($scope, $http, $location) {
    // 初始化
    $scope.myZs = {};
    // 分页
    $scope.PageCount = 0; // 总数
    $scope.CurrentPage = 1; // 当前页
    $scope.PageSize = 10; // 显示页数
    // zoholist-fzlid
    $scope.fzlid = Number(getQueryString("fzlid"));

    $http({
        method : 'post',
        url : ctx + "appJson/admin/zoho/getZohos"
    }).success(function (data) {
        if(data){
            /* 成功*/
            $scope.zohoLists = data;
            $scope.zohoLists.unshift({'id':0,'title':'All'})
        }
    })


    function into(CurrentPage,PageSize){
        var start = $("#ladate1").val();
        var end = $("#ladate2").val();
        if(end != ""){
            end = end +" 23:59:59";
        }
        var dataMap = {
            CurrentPage : CurrentPage,
            PageSize : PageSize,
            startTime : start,
            endTime : end,
            fzlid :  $scope.fzlid
        }
        $http({
            method : 'post',
            url : ctx + "appJson/admin/zoho/getZohoPage",
            data : JSON.stringify(dataMap)
        }).success(function (data) {
            console.log(data);
            if(data){
                /* 成功*/
                $scope.myZs = data.result.zohos;
                $scope.PageCount = data.result.PageCount;
                if($scope.PageCount > 0){
                    $scope.Paginator($scope.PageCount,CurrentPage,PageSize);
                }else{
                    // 没有数据时不显示
                    $('#pagination').jqPaginator('destroy');
                };
                $scope.zohoAnalyses = data.result.zohoAnalyses;
                $scope.zoho_maxDate = data.result.zoho_maxDate;
                $scope.zoho_minDate = data.result.zoho_minDate;

                $scope.fingerAnalyses = data.result.fingerAnalyses;
                $scope.finger_maxDate = data.result.finger_maxDate;
                $scope.finger_minDate = data.result.finger_minDate;


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


    // laydate国际版
    laydate.render({
        elem: '#ladate1'
    });
    laydate.render({
        elem: '#ladate2'
    });

    // search
    $scope.getSearch = function(){
        // 初始化
        $scope.myZs = {};
        into($scope.CurrentPage,$scope.PageSize);
    }

    // 上传
    $scope.getUpload = function () {
        var index = layer.load(0, {shade: false});
        if(null == $scope.zoho_title || "" == $scope.zoho_title){
            layer.alert('编号不可为空！！！', {
                skin: 'layui-layer-lan'
                ,closeBtn: 0
                ,anim: 6 //动画类型
            });
            layer.close(index);
            return;
        }
        var formData = new FormData();
        var file = document.getElementById("path").files[0];
        if(undefined != file && file.name){
            var fileName = file.name.substring(file.name.lastIndexOf(".") + 1);
            if(fileName =="xlsx" || fileName =="xls"){
                formData.append('file', file);
                formData.append('title', $scope.zoho_title);
                $http({
                    method:"post",
                    url:ctx + "appJson/admin/zoho/upload",
                    data:formData,
                    headers : {
                        'Content-Type' : undefined
                    },
                    transformRequest : angular.identity
                }).then(function (response) {
                    console.log(response)
                    layer.close(index);
                    if(response.data.code == 200){
                        layer.alert( '文件上传成功！！！', {
                            skin: 'layui-layer-lan'
                            ,closeBtn: 0
                            ,anim: 4 //动画类型
                        },function () {
                            clicked(ctx+"appPage/admin/fingerZoho?fzlid="+response.data.result.fzlid)
                        });
                    }else{
                        layer.alert('文件上传失败！！！', {
                            skin: 'layui-layer-lan'
                            ,closeBtn: 0
                            ,anim: 6 //动画类型
                        });
                    }
                });
            }else{
                layer.close(index);
                layer.alert('文件格式不正确，请上传以.xlsx，.xls 为后缀名的文件。', {
                    skin: 'layui-layer-lan'
                    ,closeBtn: 0
                    ,anim: 6 //动画类型
                });
                $("#path").val("");
            }
        }else{
            layer.close(index);
            layer.alert('文件格式不正确，请上传以.xlsx，.xls 为后缀名的文件。', {
                skin: 'layui-layer-lan'
                ,closeBtn: 0
                ,anim: 6 //动画类型
            });
        }
    }


    // 点击下拉
    $scope.clickSelect = function(){
        clicked(ctx+"appPage/admin/fingerZoho?fzlid="+$scope.fzlid);
    }

    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);
