app.directive('search', function(tags){
    return {
        restrict: 'E',
        scope:
        {
            searchData: "=searchData"
        },
        templateUrl: '/templates/search.html',
        link: function($scope, elem)
        {
            $scope.result = [];

            $scope.$watch("searchData", function(newVal, oldVal)
            {
                $scope.search();
            }, true);

            $scope.search = function()
            {
                tags.search($scope.searchData)
                    .then(function(response)
                    {
                        $scope.result = response.data.items;
                    });
            };

            $scope.add = function(tag)
            {
                addTag(tag, true);
            };

            $scope.exclude = function(tag)
            {
                addTag(tag, false);
            };

            var addTag = function(tag, add)
            {
                $scope.searchData.tags = $scope.searchData.tags.filter(function(item) { return item.name != tag});
                $scope.searchData.tags.push({name: tag, add: add});
            };

            $scope.search();
        }
    }
});
