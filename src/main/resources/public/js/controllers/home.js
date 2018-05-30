app.controller("home", function($scope, $mdDialog, auth, $mdSidenav)
{
    $scope.searchData = {
        query: '',
        tags: [],
        users: []
    };

    $scope.openSidenav = function()
    {
        console.log("open");
        $mdSidenav('left').open();
    }

    $scope.closeSidenav = function()
    {
        console.log("close");
        $mdSidenav('left').close();
    }
});