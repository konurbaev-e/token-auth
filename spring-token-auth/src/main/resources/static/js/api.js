var TOKEN_KEY = "token";

function getToken() {
    console.log('getting token ... ');
    return localStorage.getItem(TOKEN_KEY);
}

function setToken(token) {
    console.log('setting token ' + token);
    localStorage.setItem(TOKEN_KEY, token);
}

function removeToken() {
    console.log('removing token ' + token);
    localStorage.removeItem(TOKEN_KEY);
}

function getTokenFromCookie(){
    console.log('getting token from cookie ... ');
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + TOKEN_KEY.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function setTokenInCookie(token){
    console.log('setting token in cookie ' + token);
    document.cookie = TOKEN_KEY + "=" + token;
}

function removeTokenFromCookie(){
    console.log('removing token from cookie ' + token);
    setCookie(TOKEN_KEY, "", {expires: -1})
}

function login(){
    console.log('starting login ... ');

    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    var JSONObject = {"username": username, "password": password};

    console.log(JSONObject);

    var request = $.ajax({
        url: "api/login",
        type: "POST",
        data: JSON.stringify(JSONObject),
        contentType: "application/json",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log('api/login provided token ' + data.token);
            setToken(data.token);
            setTokenInCookie(data.token)
            alert('Successful authentication, your token is ' + data.token);
            window.location.href = "/hello"
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401) {
                alert('Incorrect credentials');
            } else {
                alert('An unexpected error occurred. Status ' + jqXHR.status + ', ' + textStatus + '. Error ' + errorThrown);
            }
        }
    });
}

function reLogin(){
    console.log('starting reLogin ... ');
    window.location.href = "/index"
}

function hello(){
    console.log('starting hello ... ');

    var request = $.ajax({
        url: "api/hello",
        type: "GET",
        contentType: "text/plain",
        dataType: "text",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            alert('Successful hello call: ' + data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('An unexpected error occurred. Status ' + jqXHR.status + ', ' + textStatus + '. Error ' + errorThrown);
        }
    });
}

function createAuthorizationTokenHeader() {
    var token = getToken();
    console.log('get current token ' + token);
    if (token) {
        return {"token": token};
    } else {
        return {};
    }
}