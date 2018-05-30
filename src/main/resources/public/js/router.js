app.config(function($routeProvider, $locationProvider)
{
    $routeProvider
    .when("/", {
        title: 'Welcome',
        controller: 'home',
        templateUrl : '/templates/home.html'
    })
    .when("/upload", {
        title: 'Upload',
        controller: 'upload',
        templateUrl : '/templates/upload.html'
    })
    .when("/account", {
        title: 'Account settings',
        controller: 'account',
        templateUrl : '/templates/account.html'
    })
    .otherwise({
        redirectTo: '/'
    });

    $locationProvider.html5Mode(true);
})
.run(['$location', '$rootScope', 'auth', function($location, $rootScope, auth)
{
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous)
    {
        if (current.hasOwnProperty('$$route'))
        {
            if (!auth.isLogged())
            {
                var path = current.$$route.originalPath;
                if (path.startsWith("/account") || path.startsWith("/upload"))
                {
                    $location.url("/");
                }
            }
            document.title = current.$$route.title;
        }
    });
}]);