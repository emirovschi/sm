app.controller("account", function($scope, users)
{
    $scope.isLoading = true;
    $scope.user =
    {
    };

    var updateForm = function()
    {
        $scope.isLoading = false;
        $scope.$emit("userUpdate");
    };

    $scope.update = function()
    {
        $scope.isLoading = false;

        Object.keys($scope.user).forEach(function(key)
        {
            if ($scope.user[key] != null && $scope.user[key].length == 0)
            {
                delete $scope.user[key];
            }
        });

        Object.keys($scope.accountForm).forEach(function(key)
        {
            if (key.indexOf("$") < 0)
            {
                $scope.accountForm[key].$error = {};
                $scope.accountForm[key].$invalid = false;
            }
        });

        users.updateUser($scope.user).then(function(data)
        {
            delete $scope.user.password;
            delete $scope.user.password2;

            updateForm()
        },
        function(response)
        {
            var data = response.data;
            console.log(data);
            data.errors.forEach(function(error)
            {
                error.arguments.forEach(function(argument)
                {
                    if (argument.hasOwnProperty("codes"))
                    {
                        argument.codes.forEach(function(code)
                        {
                            if ($scope.accountForm.hasOwnProperty(code))
                            {
                                $scope.accountForm[code].$setValidity(error.code, false);
                            }
                        });
                    }
                });
            });

            updateForm();
        });
    };

    users.getUser().then(function(user)
    {
        $scope.user.name = user.data.name;
        $scope.isLoading = false;
    });
});