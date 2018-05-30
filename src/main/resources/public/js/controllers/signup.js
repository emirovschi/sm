app.controller("signup", function($scope, $mdDialog, users, auth)
{
    $scope.registerData = users.registerData;

    $scope.isLoading = function()
    {
        return users.isLoading();
    }

    $scope.close = function()
    {
        $mdDialog.hide();
    }

    $scope.signup = function()
    {
        Object.keys($scope.signupForm).forEach(function(key)
        {
            if (key.indexOf("$") < 0)
            {
                $scope.signupForm[key].$error = {};
                $scope.signupForm[key].$invalid = false;
            }
        });

        users.register(function(data)
        {
            auth.request.username = data.email;
            auth.request.password = data.password;
            auth.login(function()
            {
                $scope.close();
            },
            function()
            {
                $scope.close();
            });
        },
        function(data)
        {
            data.errors.forEach(function(error)
            {
                error.arguments.forEach(function(argument)
                {
                    if (argument.hasOwnProperty("codes"))
                    {
                        argument.codes.forEach(function(code)
                        {
                            if ($scope.signupForm.hasOwnProperty(code))
                            {
                                console.log(code + " " + error.code);
                                $scope.signupForm[code].$setValidity(error.code, false);
                            }
                        });
                    }
                });
            });
        });
    }
});