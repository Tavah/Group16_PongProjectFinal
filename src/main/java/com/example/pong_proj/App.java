package com.example.pong_proj;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.example.pong_proj.Constants.*;

public class App extends GameApplication {

    @Override
    protected void initSettings(GameSettings gameSettings) {

        gameSettings.setTitle("Group 16 - Pong");
        gameSettings.setAppIcon("favicon.png");


    }

    public static void main(String[] args) {
        launch(args);
    }

    private Entity playerOne, playerTwo, ball; //FXGL Game Objects

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> playerOne.translateY(-P_SPD));
        onKey(KeyCode.S, () -> playerOne.translateY(P_SPD));

        onKey(KeyCode.UP, () -> playerTwo.translateY(-P_SPD));
        onKey(KeyCode.DOWN, () -> playerTwo.translateY(P_SPD));

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        //hash map
        vars.put("playerOneScore", 0);
        vars.put("playerTwoScore", 0);
    }

    public Entity spawnPaddle(double x, double y) {
        return entityBuilder()

                .at(x, y) //create paddles at x, y
                .viewWithBBox(new Rectangle(P_W, P_H, Color.WHITESMOKE))//create the viewable object [rect]
                .buildAndAttach(); //add paddles to game world
    }

    public Entity spawnBall(double x, double y) {
        return entityBuilder()
                .at(x, y) //create ball at x, y
                .viewWithBBox(new Rectangle(B_SIZE, B_SIZE, Color.WHITESMOKE)) //create viewable object
                .with("velocity", new Point2D(B_SPD, B_SPD)) //new entity type "velocity"
                .buildAndAttach(); //add ball to game world


    }

    @Override
    protected void initGame() {
        //spawns paddles and ball into game world
        playerOne = spawnPaddle(0, getAppHeight() / 2 - P_H / 2);
        playerTwo = spawnPaddle(getAppWidth() - P_W, getAppHeight() / 2 - P_H / 2);

        ball = spawnBall(getAppWidth() / 2 - B_SIZE / 2, getAppHeight() / 2 - B_SIZE / 2);
        getGameScene().setBackgroundRepeat("bg.png");

        loopBGM("bgm.mp3");
    }

    @Override
    protected void initUI() {
        Text textplayerOneScore = getUIFactoryService().newText("", Color.WHITESMOKE, 22);
        Text textplayerTwoScore = getUIFactoryService().newText("", Color.WHITESMOKE, 22);

        textplayerOneScore.textProperty().bind(getip("playerOneScore").asString());
        textplayerTwoScore.textProperty().bind(getip("playerTwoScore").asString());


        addUINode(textplayerOneScore, 10, 50);
        addUINode(textplayerTwoScore, getAppWidth() - 30, 50);

    }

    @Override
    protected void onUpdate(double tpf) {
        Point2D velocity = ball.getObject("velocity");
        ball.translate(velocity);

        if (ball.getX() == playerOne.getRightX()
            && ball.getY() < playerOne.getBottomY()
            && ball.getBottomY() >= playerOne.getY()
        ) ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));

        if (ball.getRightX() == playerTwo.getX()
            && ball.getY() < playerTwo.getBottomY()
            && ball.getBottomY() >= playerTwo.getY()
        ) ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));

        if (ball.getX() <= 0) {
            inc("playerTwoScore", +1);
            resetBall();
        }

        if (ball.getRightX() >= getAppWidth()) {
            inc("playerOneScore", +1);
            resetBall();
        }

        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }

        if (ball.getBottomY() >= getAppHeight()) {
            ball.setY(getAppHeight() - B_SIZE);
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }

    }

    public Entity getplayerOne() {
        return playerOne;
    }

    public void setplayerOne(Entity playerOne) {
        this.playerOne = playerOne;
    }

    public Entity getplayerTwo() {
        return playerTwo;
    }

    public void setplayerTwo(Entity playerTwo) {
        this.playerTwo = playerTwo;
    }

    public Entity getBall() {
        return ball;
    }

    public void setBall(Entity ball) {
        this.ball = ball;
    }

    private void resetBall() {
        ball.setPosition(getAppWidth() / 2 - B_SIZE / 2, getAppHeight() / 2 - B_SIZE / 2);
        ball.setProperty("velocity", new Point2D(B_SPD, B_SPD));
    }
}
