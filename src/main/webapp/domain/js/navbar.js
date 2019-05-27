app.controller("navbarCtrl", function ($scope) {

    $scope.loginInfo = {};

    // 2
    $scope.logout = function (loginName) {
        var url = getRequestPath() + '/sign/logout';

        console.log(url);

        var params = {
            'loginName': loginName
        };

        console.log(params);

        $.ajax({
            type: "POST",
            url: url,
            async: false,
            data: params,
            dataType: 'json',
            error: function (data) {
                alert(data.resultMesg);
            },
            success: function (data) {
                window.location.href = getRootPath() + '/dashboard-data-login.html';
            }
        });
    };

    // 1
    $scope.getLoginInfo = function () {
        var url = getRequestPath() + '/sign/getLoginInfo';

        console.log(url);

        var params = {
            'curTime': '1'
        };

        console.log(params);

        $.ajax({
            type: "POST",
            url: url,
            async: false,
            data: params,
            dataType: 'json',
            error: function (data) {
                alert(data.resultMesg);
            },
            success: function (data) {
                if (data.resultCode == 'success') {
                    $scope.loginInfo = data.loginInfo;
                } else {
                    window.location.href = getRootPath() + '/dashboard-data-login.html';
                }
            }
        });
    };

    // 0
    $scope.refreshPage = function () {
        console.log('refresh page navbar');

        // 获取用户的信息
        $scope.getLoginInfo();

    };

    $scope.refreshPage();


});