app.service('auth', function($http, $timeout, $location)
{
    var client_login = "web";
    var client_password = "";
    var loginCallbacks = [];
    var logoutCallbacks = [];
    var authToken = btoa(client_login + ":" + client_password);
    var refresher = null;
    var refreshTimeDelta = 20;

    var isLoading_ = false;
    var isLogged_ = false;

    var request_ =
    {
        grant_type: "password",
        username: "",
        password: ""
    };

    this.request = request_;

    this.isLoading = function()
    {
        return isLoading_;
    };

    this.isLogged = function()
    {
        return isLogged_;
    };

    var config = {
        headers:
        {
            "Authorization": "Basic " + authToken,
            "Content-Type": "application/x-www-form-urlencoded"
        }
    };

    var encode = function(data)
    {
        return Object.keys(data)
                     .map(function(k){ return encodeURIComponent(k) + "=" + encodeURIComponent(data[k])})
                     .join("&");
    };

    var refresh = function(authorization)
    {
        var body = {
            grant_type: "refresh_token",
            refresh_token: authorization.refresh_token
        };

        var promise = $http.post("/oauth/token", encode(body), config);

        promise.then(function(response)
        {
            save(response.data);
        },
        function(er)
        {
            save();
        });

        return promise;
    };

    var startRefresh = function(authorization)
    {
        if(authorization.expires_in < 0)
        {
            return refresh(authorization);
        }
        else
        {
            return $timeout(function()
            {
                refresh(authorization);
            }, (authorization.expires_in - refreshTimeDelta) * 1000);
        }
    };

    var save = function(authorization)
    {
        if (authorization != null && authorization.access_token != null)
        {
            $http.defaults.headers.common["Authorization"] = "Bearer " + authorization.access_token;
            localStorage.refresh_token = authorization.refresh_token;
            refresher = startRefresh(authorization);

            isLogged_ = true;
        }
        else
        {
            $http.defaults.headers.common["Authorization"] = null;
            localStorage.removeItem("refresh_token");
            $timeout.cancel(refresher);

            isLogged_ = false;
            $location.url("/");
        }
    };

    this.login = function(success, error)
    {
        isLoading_ = true;
        $http.post("/oauth/token", encode(request_), config).then(function(response)
        {
            save(response.data);

            loginCallbacks.forEach(function(callback)
            {
                callback();
            });

            success();

            isLoading_ = false;
            request_.username = "";
            request_.password = "";
        },
        function(er)
        {
            error(er);

            logoutCallbacks.forEach(function(callback)
            {
                callback();
            });

            isLoading_ = false;
        });
    };

    this.logout = function()
    {
        save();
    };

    this.listenLogin = function(callback)
    {
        loginCallbacks.push(callback);
    };

    this.listenLogout = function(callback)
    {
        logoutCallbacks.push(callback);
    };

    var checkedLog_ = new Promise(function(resolve)
    {
        if (localStorage.refresh_token != null)
        {
            var authorization =
            {
                refresh_token: localStorage.refresh_token,
                expires_in: -1
            };
            refresh(authorization).then(resolve, resolve);
        }
        else
        {
            resolve();
        }
    });

    this.checkedLog = checkedLog_;
});