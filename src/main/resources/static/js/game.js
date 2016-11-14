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
        document.getElementById("MyButton").style.visibility='hidden';
        var controllBlock = document.getElementById('controllBlock');
        controllBlock.style.visibility = 'visible';
        Messaging.sendJoinGameMsg();
    };

    document.getElementById("piratMoveSubmit").onclick = function() {
        var piratMove = {};
        piratMove.piratId = document.getElementById("piratId").value;
        piratMove.targetCellX = document.getElementById("targetCellX").value;
        piratMove.targetCellY = document.getElementById("targetCellY").value;
        Messaging.sendPiratMove(piratMove);
    }

    document.getElementById("shipMoveSubmit").onclick = function() {
        var shipMove = {};
        shipMove.directionX = document.getElementById("shipDirectionX").value;
        shipMove.directionY = document.getElementById("shipDirectionY").value;
        Messaging.sendShipMove(shipMove);
    }

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
        }
        if ( message.type === "ru.mail.park.mechanics.requests.BoardMapForUsers$Request"){
            console.log("Wow. Seems loke game been started");
            content = JSON.parse(message.content);
            console.log(content);
            var gameBoard = document.getElementById('gameBoard');
            var active = content.active;
            if(!active){
                document.getElementById("piratMoveSubmit").disabled = true;
                document.getElementById("shipMoveSubmit").disabled = true;
            }
            gameBoard.innerHTML = content.gameBoard;
        }
        if(message.type === "ru.mail.park.mechanics.requests.BoardMapAfterTurn$Request") {
            console.log("Someone moved. Handle new boardMap.");
            content = JSON.parse(message.content);
            console.log(content);
            var gameBoard = document.getElementById('gameBoard');
            var active = content.active;
            if(!active){
                document.getElementById("piratMoveSubmit").disabled = true;
                document.getElementById("shipMoveSubmit").disabled = true;
            } else {
                document.getElementById("piratMoveSubmit").disabled = false;
                document.getElementById("shipMoveSubmit").disabled = false;
            }
            gameBoard.innerHTML = content.gameBoard;
        }
        console.log(message.type);

    };
});

Game.initialize();