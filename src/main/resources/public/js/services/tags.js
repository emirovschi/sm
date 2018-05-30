app.service('tags', function($http)
{
    this.search = function (searchData)
    {
        return $http.post("/tags/search", searchData);
    }
});
