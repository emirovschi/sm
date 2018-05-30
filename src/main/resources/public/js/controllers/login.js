app.controller("login", function($scope, $mdDialog, auth)
{
    $scope.request = auth.request;

    $scope.isLoading = function()
    {
        return auth.isLoading();
    }

    $scope.close = function()
    {
        $mdDialog.hide();
    }

    $scope.login = function()
    {
        auth.login(function()
        {
            $scope.close();
        },
        function(data)
        {
            $scope.loginForm["username"].$setValidity("bad", false);
            $scope.loginForm["password"].$setValidity("bad", false);
        });
    }
});
