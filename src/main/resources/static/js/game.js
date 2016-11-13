var Game = {};
var Messaging = new MessagingTools(Game);
Game.socket = null;
Game.userLogin = null;
Game.initialize = function () {
    Game.connect();
};
Game.connect = (function () {
    Game.socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/game");

    document.getElementById("MyButton").onclick = function() {
        alert('Кто-то нажал на кнопку 0_о')
        Messaging.sendMyTestMessage();
    };

    Game.socket.onopen = function () {
        // Socket open.. start the game loop.
        console.log('Info: WebSocket connection opened.');
        console.log('Info: Waiting for another player...');
    };

    Game.socket.onclose = function () {
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
        } else {
            console.log("not ready");
        }
    };
});

Game.initialize();