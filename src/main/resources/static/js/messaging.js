var MessagingTools = function (soket) {
    var myTestMessage = {};
    myTestMessage.type="ru.mail.park.websocket.TestMessage$Request";
    myTestMessage.content="{}";

    var getPingMessage = {};
    getPingMessage.type="ru.mail.park.pinger.requests.GetPing$Request";
    getPingMessage.content="{}";

    var joinGameMessage = {}
    joinGameMessage.type = "ru.mail.park.mechanics.requests.JoinGame$Request";
    joinGameMessage.content="{}";

    this.sendMyTestMessage = function() {
        socket.send(JSON.stringify(myTestMessage));
    };

    this.sendUpdatePingMsg = function () {
        socket.send(JSON.stringify(getPingMessage));
    };

    this.sendJoinGameMsg = function () {
        socket.send(JSON.stringify(joinGameMessage));
    };

    this.sendClientSnap = function(snap) {
        var clientSnapMessage = {};
        clientSnapMessage.type = "ru.mail.park.mechanics.base.ClientSnap";
        clientSnapMessage.content = JSON.stringify(snap);
        socket.send(JSON.stringify(clientSnapMessage));
    }

    this.sendPiratMove = function(piratMove){
        var clientPiratMoveMessage = {};
        clientPiratMoveMessage.type = "ru.mail.park.mechanics.requests.PiratMoveRequest";
        clientPiratMoveMessage.content = JSON.stringify(piratMove);
        socket.send(JSON.stringify(clientPiratMoveMessage));
    }

    this.sendShipMove = function(shipMove){
        var clientShipMoveMessage = {};
        clientShipMoveMessage.type = "ru.mail.park.mechanics.requests.ShipMoveRequest";
        clientShipMoveMessage.content = JSON.stringify(shipMove);
        socket.send(JSON.stringify(clientShipMoveMessage));
    }
};
