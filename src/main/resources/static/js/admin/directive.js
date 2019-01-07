var myapp = angular.module("myapp",[]);
// 路由
myapp.config(['$locationProvider', function($locationProvider) {
    // $locationProvider.html5Mode(true);
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);
// 左导航  <left-Directive></left-Directive>
myapp.directive('leftDirective', function() {
  /*  var templateHtml = " <div class=\"col-sm-3 col-md-2 sidebar\">" +
        "                <ul class=\"nav nav-sidebar\">\n" +
        "                <li class=\"fingerUserPage\"><a href=\"javascript:void(0);\" ng-click=\"goCancel('" + ctx + "appPage/admin/fingerUser')\">用户管理</a></li>\n" +
        "                <li class=\"fingerJobsPage\"><a href=\"javascript:void(0);\" ng-click=\"goCancel('" + ctx + "appPage/admin/fingerJobs')\">派单管理</a></li>\n" +
        "                <li class=\"fingerJobsLogsPage\"><a href=\"javascript:void(0);\" ng-click=\"goCancel('" + ctx + "appPage/admin/fingerJobsLogs')\">派单历史</a></li>\n" +

        "            </ul></div>";*/
    var templateHtml = "<nav class=\"navbar navbar-default navbar-static-top\" role=\"navigation\" style=\"margin-bottom: 0\">\n" +
        "            <div class=\"navbar-header\">\n" +
        "                <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">\n" +
        "                    <span class=\"sr-only\">Toggle navigation</span>\n" +
        "                    <span class=\"icon-bar\"></span>\n" +
        "                    <span class=\"icon-bar\"></span>\n" +
        "                    <span class=\"icon-bar\"></span>\n" +
        "                </button>\n" +
        "                <a class=\"navbar-brand\" href=\"index.html\">Sonic派单后台</a>\n" +
        "            </div>\n" +
        "            <!-- /.navbar-header -->\n" +
        "            <ul class=\"nav navbar-top-links navbar-right\">\n" +
        "                <!-- /.1 -->\n" +
        "                <li class=\"dropdown\">\n" +
        "                    <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href='javascript:void(0);' ng-click=\"goCancel('" + ctx + "appPage/fingerIndex')\">前台\n" +
        "                        <i class=\"fa fa-tasks fa-fw\"></i>\n" +
        "                    </a>\n" +
        "                    <!-- /.dropdown-alerts -->\n" +
        "                </li>\n" +
        "                <!-- /.2 -->\n" +
        "                <li class=\"dropdown\">\n" +
        "                    <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n" +
        "                        <i class=\"fa fa-bell fa-fw\"></i> <i class=\"fa fa-caret-down\"></i>\n" +
        "                    </a>\n" +
        "                    <ul class=\"dropdown-menu dropdown-alerts\">\n" +
        "                        <li>\n" +
        "                            <a href=\"#\">\n" +
        "                                <div>\n" +
        "                                    <i class=\"fa fa-comment fa-fw\"></i> New Comment\n" +
        "                                    <span class=\"pull-right text-muted small\">4 minutes ago</span>\n" +
        "                                </div>\n" +
        "                            </a>\n" +
        "                        </li>\n" +
        "                        <li class=\"divider\"></li>\n" +
        "                        <li>\n" +
        "                            <a class=\"text-center\" href=\"#\">\n" +
        "                                <strong>See All Alerts</strong>\n" +
        "                                <i class=\"fa fa-angle-right\"></i>\n" +
        "                            </a>\n" +
        "                        </li>\n" +
        "                    </ul>\n" +
        "                    <!-- /.dropdown-alerts -->\n" +
        "                </li>\n" +
        "                <!-- /.3 -->\n" +
        "                <li class=\"dropdown\">\n" +
        "                    <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n" +
        "                        <i class=\"fa fa-user fa-fw\"></i> <i class=\"fa fa-caret-down\"></i>\n" +
        "                    </a>\n" +
        "                    <ul class=\"dropdown-menu dropdown-user\">\n" +
        "                        <li><a href=\"#\"><i class=\"fa fa-user fa-fw\"></i> User Profile</a>\n" +
        "                        </li>\n" +
        "                        <li><a href=\"#\"><i class=\"fa fa-gear fa-fw\"></i> Settings</a>\n" +
        "                        </li>\n" +
        "                        <li class=\"divider\"></li>\n" +
        "                        <li><a href=\"javascript:void(0);\" ng-click=\"goCancel('" + ctx + "appJson/admin/cleanlogin')\" ><i class=\"fa fa-sign-out fa-fw\"></i> Logout</a>\n" +
        "                        </li>\n" +
        "                    </ul>\n" +
        "                    <!-- /.dropdown-user -->\n" +
        "                </li>\n" +
        "                <!-- /.dropdown -->\n" +
        "            </ul>\n" +
        "            <!-- /.navbar-top-links -->\n" +
        "            <div class=\"navbar-default sidebar\" role=\"navigation\">\n" +
        "                <div class=\"sidebar-nav navbar-collapse\">\n" +
        "                    <ul class=\"nav\" id=\"side-menu\">\n" +
        "                        <li>\n" +
        "                            <a  class=\"fingerUserPage\" href='javascript:void(0);' ng-click=\"goCancel('" + ctx + "appPage/admin/fingerUser')\"><i class=\"fa fa-dashboard fa-fw\"></i> 用户管理</a>\n" +
        "                        </li>\n" +
        "                        <li>\n" +
        "                            <a class=\"fingerJobsPage\" href='javascript:void(0);' ng-click=\"goCancel('" + ctx + "appPage/admin/fingerJobs')\"><i class=\"fa fa-bar-chart-o fa-fw\"></i> 派单管理</a>\n" +
        "                        </li>\n" +
        "                        <li>\n" +
        "                            <a href=\"#\"><i class=\"fa fa-sitemap fa-fw\"></i> 派单历史<span class=\"fa arrow\"></span></a>\n" +
        "                            <ul class=\"nav nav-second-level\">\n" +
        "                                <li>\n" +
        "                                    <a href=\"flot.html\">Flot Charts</a>\n" +
        "                                </li>\n" +
        "                                <li>\n" +
        "                                    <a href=\"morris.html\">Morris.js Charts</a>\n" +
        "                                </li>\n" +
        "                            </ul>\n" +
        "                            <!-- /.nav-second-level -->\n" +
        "                        </li>\n" +
        "                    </ul>\n" +
        "                </div>\n" +
        "                <!-- /.sidebar-collapse -->\n" +
        "            </div>\n" +
        "            <!-- /.navbar-static-side -->\n" +
        "        </nav>\n";

    return {
        restrict: 'E',
        template: templateHtml
    }
});
