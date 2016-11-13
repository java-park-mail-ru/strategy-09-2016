var MessagingTools = function (Game) {
    var myTestMessage = {};
    myTestMessage.type="ru.mail.park.websocket.TestMessage$Request";
    myTestMessage.content="{}";

    var getPingMessage = {};
    getPingMessage.type="ru.mail.park.pinger.requests.GetPing$Request";
    getPingMessage.content="{}";

    var joinGameMessage = {}
    joinGameMessage.type = "ru.mail.park.websocket.TestMessage$Request";
    joinGameMessage.content="{}";

    this.sendMyTestMessage = function() {
        Game.socket.send(JSON.stringify(myTestMessage));
    };

    this.sendUpdatePingMsg = function () {
        Game.socket.send(JSON.stringify(getPingMessage));
    };

    this.sendJoinGameMsg = function () {
        Game.socket.send(JSON.stringify(joinGameMessage));
    };

    this.sendClientSnap = function(snap) {
        var clientSnapMessage = {};
        clientSnapMessage.type = "ru.mail.park.mechanics.base.ClientSnap";
        clientSnapMessage.content = JSON.stringify(snap);
        Game.socket.send(JSON.stringify(clientSnapMessage));
    }
};
