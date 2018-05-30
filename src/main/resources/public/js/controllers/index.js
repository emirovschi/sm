app.controller("index", function($scope, $mdDialog, auth, users, $mdSidenav)
{
    $scope.logged = function()
    {
        return auth.isLogged();
    };

    $scope.openLogIn = function()
    {
        $mdDialog.show(
        {
            controller: 'login',
            clickOutsideToClose: true,
            openFrom: '#login',
            closeTo: '#login',
            templateUrl: '/templates/login.html'
        });
    };

    $scope.openSignUp = function()
    {
        $mdDialog.show(
        {
            controller: 'signup',
            clickOutsideToClose: true,
            openFrom: '#signup',
            closeTo: '#signup',
            templateUrl: '/templates/signup.html'
        });
    };

    $scope.openMenu = function($mdOpenMenu, $event)
    {
        $mdOpenMenu($event);
    };

    $scope.logout = function()
    {
        auth.logout();
    }

    var updateUser = function()
    {
        users.getUser().then(function(data)
        {
            $scope.user = data.data;
            $scope.user.random = Math.random();
        });
    }

    auth.listenLogin(updateUser);
    $scope.$on("userUpdate", updateUser);

    auth.checkedLog.then(function()
    {
        if (auth.isLogged())
        {
            updateUser();
        }
    });

    $scope.openSidenav = function()
    {
        $mdSidenav('left').open();
    }
});
