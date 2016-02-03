/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbgLecture3lab;

import java.awt.Color;
import java.awt.Graphics2D;
import static pbgLecture3lab.BasicPhysicsEngine.SCREEN_HEIGHT;
import static pbgLecture3lab.BasicPhysicsEngine.SCREEN_WIDTH;

/**
 *
 * @author bernardot
 */
public class BasicParticle_MouseListener extends BasicParticle {
    
    private boolean moving = true;
    
    public BasicParticle_MouseListener(double sx, double sy, double vx, double vy, double radius, boolean improvedEuler, Color col, double mass) {
        super(sx, sy, vx, vy, radius, improvedEuler, col, mass);
    }

    @Override
    public void update() {
        if (BasicMouseListener.isMouseButtonPressed()) {
            if (moving) {
                setPos(BasicMouseListener.getWorldCoordinatesOfMousePointer());
            }
            moving = false;
        } else {
            if (!moving) {
                Vector2D aimVelocity = Vector2D.minus(BasicMouseListener.getWorldCoordinatesOfMousePointer(), getPos());
                aimVelocity.mult(3);
                setVel(aimVelocity);
                moving = true;
            } else {
                super.update();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (BasicMouseListener.isMouseButtonPressed()) {
            int x = 0;
            int y = 0;
            if (!moving) {
                x = BasicPhysicsEngine.convertWorldXtoScreenX(getPos().x);
                y = BasicPhysicsEngine.convertWorldYtoScreenY(getPos().y);
            }
            g.setColor(col);
            g.fillOval(x - SCREEN_RADIUS, y - SCREEN_RADIUS, 2 * SCREEN_RADIUS, 2 * SCREEN_RADIUS);
            g.drawLine(x, y, BasicMouseListener.getMouseX(), BasicMouseListener.getMouseY());
        } else {
            super.draw(g);
        }
    }
    
}
