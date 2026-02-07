package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize/2);
        screenY = gp.screenHeight / 2 - (gp.tileSize/2);


        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y =16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;


        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        spriteNum = 1;
        spriteCounter = 0;
    }

    private BufferedImage loadImage(String path) {
        try {
            var url = getClass().getResource(path);
            if (url == null) {
                throw new RuntimeException("Image not found: " + path);
            }
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getPlayerImage() {
        up1 = loadImage("/player/boy_up_1.png");
        up2 = loadImage("/player/boy_up_2.png");
        down1 = loadImage("/player/boy_down_1.png");
        down2 = loadImage("/player/boy_down_2.png");
        left1 = loadImage("/player/boy_left_1.png");
        left2 = loadImage("/player/boy_left_2.png");
        right1 = loadImage("/player/boy_right_1.png");
        right2 = loadImage("/player/boy_right_2.png");

        System.out.println("Player images loaded ✅");
    }

    public void update() {

        boolean moving = false;

        if (keyH.upPressed) {
            direction = "up";
            moving = true;
        } else if (keyH.downPressed) {
            direction = "down";
            moving = true;
        } else if (keyH.leftPressed) {
            direction = "left";
            moving = true;
        } else if (keyH.rightPressed) {
            direction = "right";
            moving = true;
        }

        if (moving) {
            // RESET collision flag
            collisionOn = false;

            // CHECK collision ONLY when moving
            gp.cChecker.checkTile(this);
            int objIndex = gp.cChecker.checkObject(this , true);
            pickUpObject(objIndex);

            // MOVE only if no collision
            if (!collisionOn) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            // SPRITE animation
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int i){
        if(i != 999){
            String objectName = gp.obj[i].name;


            switch(objectName){
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i]=null;
                    gp.ui.showMessage("You got a key!");

                    break;
                case "Door":
                    if(hasKey>0){
                        gp.playSE(3);
                        gp.obj[i]=null;
                        hasKey--;
                        gp.ui.showMessage("You opened the door !");
                    }
                    else {
                        gp.ui.showMessage("You need a key !");
                    }

                    break;
                case"Boots":
                    gp.playSE(2);
                    speed += 1;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed up !");
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;


            }
        }
    }


    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up" -> image = (spriteNum == 1) ? up1 : up2;
            case "down" -> image = (spriteNum == 1) ? down1 : down2;
            case "left" -> image = (spriteNum == 1) ? left1 : left2;
            case "right" -> image = (spriteNum == 1) ? right1 : right2;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}

