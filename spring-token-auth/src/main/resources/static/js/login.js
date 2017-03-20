var TOKEN_KEY = "token";

function getToken() {
    console.log('getting token ... ');
    return localStorage.getItem(TOKEN_KEY);
}

function setToken(token) {
    console.log('setting token ' + token);
    localStorage.setItem(TOKEN_KEY, token);
}

function removeJwtToken() {
    console.log('removing token ' + token);
    localStorage.removeItem(TOKEN_KEY);
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
            setToken(data.token);
            console.log('api/login provided token ' + data.token);
            alert('Successful authentication, your token is ' + data.token);
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