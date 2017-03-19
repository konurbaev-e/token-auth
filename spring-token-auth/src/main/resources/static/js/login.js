var attempt = 3; // Variable to count number of attempts.
// Below function Executes on click of login button.
function validate(){
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    var JSONObject = {"username": username, "password": password};

    console.log(JSONObject);

//    var jsonData = JSON.parse(JSONObject);
//
//    console.log(jsonData);

    var request = $.ajax({
        url: "api/login",
        type: "POST",
//        data: jsonData,
        data: JSON.stringify(JSONObject),
//        contentType: "application/json; charset=utf-8",
        contentType: "application/json",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log(data.token);
        }
    });

//    if ( username == "Formget" && password == "formget#123"){
//        alert ("Login successfully");
//        window.location = "success.html"; // Redirecting to other page.
//        return false;
//    }
//    else {
//        attempt --;// Decrementing by one.
//        alert("You have left "+attempt+" attempt;");
//        // Disabling fields after 3 attempts.
//        if( attempt == 0){
//            document.getElementById("username").disabled = true;
//            document.getElementById("password").disabled = true;
//            document.getElementById("submit").disabled = true;
//            return false;
//        }
//    }
}