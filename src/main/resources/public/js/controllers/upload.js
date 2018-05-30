app.controller("upload", function($scope, $mdConstant, $location, $q, posts, tags)
{
    $scope.isLoading = false;
    $scope.notReady = true;

    $scope.previewUrl = "";
    $scope.previewType = "";
    $scope.files = [];
    $scope.post =
    {
        title: '',
        tags: [],
        fileSelected: false
    };

    $scope.selectedItem = null;
    $scope.searchText = null;
    $scope.keys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA];

    $scope.querySearch = function(tag)
    {
        var deferred = $q.defer();

        var data =
        {
            query: "%" + tag + "%"
        };

        tags.search(data).then(function(data)
        {
            deferred.resolve(data.data.items.map(function(i)
            {
                return i.name
            }));
        });

        return deferred.promise;
    };

    $scope.$watch('files[0].lfDataUrl', function(newVal, oldVal)
    {
        $scope.post.fileSelected = $scope.files.length > 0;

        if ($scope.post.fileSelected)
        {
            console.log($scope.files[0].lfFile);
            $scope.previewUrl = $scope.files[0].lfDataUrl;
            var type = $scope.files[0].lfFile.type;
            $scope.previewType = type.substring(0, type.indexOf('/'));
        }
        else
        {
            $scope.previewUrl = "";
            $scope.previewType = "";
        }
    });

    $scope.postWatcher = $scope.$watch('post', function(newVal, oldVal)
    {
        $scope.notReady = $scope.post.length == 0 || $scope.post.tags.length == 0 || $scope.post.fileSelected == false;
    }, true);

    $scope.upload = function()
    {
        $scope.isLoading = true;
        $scope.postWatcher();
        $scope.post.media = $scope.files[0].lfFile;
        posts.upload($scope.post).then(function(data)
        {
            $location.url("/post/" + data.data.id);
        },
        function(data)
        {
            $location.url("/");
        })
    }
});