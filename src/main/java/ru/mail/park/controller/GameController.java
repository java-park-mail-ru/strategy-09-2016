package ru.mail.park.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.game.GameBoard;


@RestController
public class GameController {

    @RequestMapping(path = "/test/map", method = RequestMethod.GET)
    public ResponseEntity map() {
        String ResponseString = "";
        GameBoard gameBoard = new GameBoard();
        Integer cx = 1;
        Integer cy = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("<pre>");
        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 13; ++j) {
                String tmpStr = "";
                if(gameBoard.getBoardMapId(i,j)!=null) {
                    tmpStr = Integer.toString(gameBoard.getBoardMapId(i, j));
                } else {
                    tmpStr = "null";
                }
                while (tmpStr.length() < 4) {
                    tmpStr = " " + tmpStr;
                }
                builder.append(tmpStr);
                builder.append(" ");
            }
            ResponseString += "<br>";
            builder.append("<br>");
        }
        builder.append("</pre>");
        ResponseString = "<pre>" + ResponseString + "</pre>";
        return ResponseEntity.ok(builder.toString());
    }
}
