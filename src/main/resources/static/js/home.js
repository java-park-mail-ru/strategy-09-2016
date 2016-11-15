$(document).ready(function () {
    var socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/game");
    socket.onopen = function () {
        // Socket open.. start the game loop.
        console.log('Info: WebSocket connection opened.');
        console.log('Info: Waiting for another player...');
    };
    socket.onclose = function () {
        console.log('Info: WebSocket closed.');
    };
    Game.socket.onmessage = function (event) {
        var content = {};
        var responseContent = {};
        var response = {};
        var message = JSON.parse(event.data);


        if (message.type === "ru.mail.park.websocket.MessageToClient$Request") {
            content = JSON.parse(message.content);
            responseContent.myMessage = content.myMessage;
            console.log(responseContent.myMessage);
            return;
        }
    };
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


