app.directive('postList', function(posts, auth){
    return {
        restrict: 'E',
        scope:
        {
            searchData: "=searchData"
        },
        templateUrl: '/templates/postList.html',
        link: function($scope, elem)
        {
            var pageSize = 20;
            var totalPages = -1;
            var page = 0;
            var promise;

            var getWidth = function()
            {
                return document.getElementById("post-list-sample").offsetWidth;
            }

            $scope.logged = function()
            {
                return auth.isLogged();
            }

            auth.checkedLog.then(function()
            {
                $scope.$watch("searchData.tags", function(newVal, oldVal)
                {
                    page = 0;
                    totalPages = -1;
                    promise = null;
                    $scope.searchData.firstId = null;
                    $scope.posts = [];
                    $scope.fetch();
                }, true);
            });

            $scope.posts = [];

            $scope.showLoading = function()
            {
                return totalPages != page;
            };

            $scope.fetch = function()
            {
                if (promise == null && (totalPages < 0 || page < totalPages))
                {
                    promise = posts.getPosts(page, pageSize, $scope.searchData);

                    promise.then(function(response)
                    {
                        if (promise == null)
                        {
                            return;
                        }

                        response.data.items.forEach(function(e)
                        {
                            e.size = function ()
                            {
                                return (e.type === 'audio' ? 32 : e.height * getWidth() / e.width) + 67;
                            }
                            $scope.posts.push(e);
                        });

                        if ($scope.searchData.firstId == null && response.data.items.length > 0)
                        {
                            $scope.searchData.firstId = response.data.items[0].id;
                        }

                        if (totalPages < 0)
                        {
                            totalPages = response.data.totalPage;
                        }

                        page++;
                        promise = null;
                    });
                }
            };

            var resetVote = function(item)
            {
                if (item.userVote > 0)
                {
                    item.ups--;
                }
                else if (item.userVote < 0)
                {
                    item.downs--;
                }
            }

            $scope.voteUp = function(item)
            {
                resetVote(item);
                item.ups++;
                item.userVote = 1;
                posts.voteUp(item.id);
            };

            $scope.voteDown = function(item)
            {
                resetVote(item);
                item.downs++;
                item.userVote = -1;
                posts.voteDown(item.id);
            };

            auth.listenLogin(function()
            {
                posts.getVotes(page * pageSize, $scope.searchData).then(function(data)
                {
                    for(i = 0; i < $scope.posts.length; i++)
                    {
                        $scope.posts[i].userVote = data.data.items[i].userVote;
                    }
                });
            });
        }
    }
});
