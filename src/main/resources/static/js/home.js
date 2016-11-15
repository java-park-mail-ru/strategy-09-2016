$(document).ready(function () {
    $("#form").submit(function(ev){
        var login = $("#login").val();
        var password = $("#password").val();
        if (!login || login == "") {
            return;
        }
        if (!password || password == "") {
            return;
        }
        var request = JSON.stringify({"login" : login, "password": password});
        $.ajax({
            type: 'POST',
            contentType: "application/json",
            url: "/session/",
            data: request,
            success: function(response) {
                if (response.response) {
                    $("#answer").html("Hi, " + login);
                }
                setTimeout(function () {
                    window.location = "/gameMock.html";
                            }, 2000);
            },
            error: function(data) {
                window.console.error("error logining in: " + data);
                //$("#commentList").append($("#name").val() + "<br/>" + $("#body").val());
                // alert("There was an error submitting comment");
            }
        });
        ev.stopPropagation();
        return false;

    });
});


