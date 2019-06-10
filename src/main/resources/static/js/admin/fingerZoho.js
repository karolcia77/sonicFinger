// 初始化样式
$(function () {
    $(".fingerZohoPage").addClass("active");
})

// admin/fingerZohoController
myapp.controller("fingerZohoController",["$scope","$http","$location",function ($scope, $http, $location) {
    // 初始化
    $scope.myRs = {};
    // 分页
    $scope.PageCount = 0; // 总数
    $scope.CurrentPage = 1; // 当前页
    $scope.PageSize = 10; // 显示页数


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
            endTime : end
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
        $scope.myRs = {};
        into($scope.CurrentPage,$scope.PageSize);
    }




    // 退出
    $scope.goCancel = function(url){
        clicked(url); // 跳url
    }
}]);
