package pbgLecture3lab;

import static pbgLecture3lab.BasicPhysicsEngine.DELTA_T;
import static pbgLecture3lab.BasicPhysicsEngine.GRAVITY;

import java.awt.Color;
import java.awt.Graphics2D;
import static pbgLecture3lab.BasicPhysicsEngine.SCREEN_HEIGHT;
import static pbgLecture3lab.BasicPhysicsEngine.SCREEN_WIDTH;

public class BasicParticle {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-01-28
	 * Significant changes applied:
	 */
	public final int SCREEN_RADIUS;

	private Vector2D pos;
	private Vector2D vel;
	private final double radius;
	private final double mass;
	public final Color col;

	private final boolean improvedEuler;

	

	public BasicParticle(double sx, double sy, double vx, double vy, double radius, boolean improvedEuler, Color col, double mass) {
		setPos(new Vector2D(sx,sy));
		setVel(new Vector2D(vx,vy));
		this.radius=radius;
		this.mass=mass;
		this.improvedEuler=improvedEuler;
		this.SCREEN_RADIUS=Math.max(BasicPhysicsEngine.convertWorldLengthToScreenLength(radius),1);
		this.col=col;
	}

	public void update() {
		Vector2D acc;
		acc=new Vector2D(0,-GRAVITY);
		
		if (improvedEuler) {
			Vector2D vel2=new Vector2D(getVel());
			Vector2D pos2=new Vector2D(getPos());
			pos2.addScaled(getVel(), DELTA_T);
			vel2.addScaled(acc, DELTA_T);
			Vector2D acc2=new Vector2D(acc);//assuming acceleration is constant
			// Note acceleration is NOT CONSTANT for distance dependent forces such as 
			// Hooke's law or newton's law of gravity, so this is BUG  
			// in this Improved Euler implementation.  
			// The whole program structure needs changing to fix this problem properly!
			vel2.add(getVel());
			vel2.mult(0.5);
			acc2.add(acc);
			acc2.mult(0.5);
			getPos().addScaled(vel2, DELTA_T);
			getVel().addScaled(acc2, DELTA_T);
		} else {
			// basic Euler
			getPos().addScaled(getVel(), DELTA_T);
			getVel().addScaled(acc, DELTA_T);
		}
	}


	public void draw(Graphics2D g) {
		int x = BasicPhysicsEngine.convertWorldXtoScreenX(getPos().x);
		int y = BasicPhysicsEngine.convertWorldYtoScreenY(getPos().y);
		g.setColor(col);
		g.fillOval(x - SCREEN_RADIUS, y - SCREEN_RADIUS, 2 * SCREEN_RADIUS, 2 * SCREEN_RADIUS);
	}

	public double getRadius() {
		return radius;
	}

	public Vector2D getPos() {
		return pos;
	}

	public void setPos(Vector2D pos) {
		this.pos = pos;
	}

	public Vector2D getVel() {
		return vel;
	}

	public void setVel(Vector2D vel) {
		this.vel = vel;
	}

	public boolean collidesWith(BasicParticle p2) {
		Vector2D vecFrom1to2 = Vector2D.minus(p2.getPos(), getPos());
		boolean movingTowardsEachOther = Vector2D.minus(p2.getVel(), getVel()).scalarProduct(vecFrom1to2)<0;
		return vecFrom1to2.mag()<getRadius()+p2.getRadius() && movingTowardsEachOther;
	}

	public static void implementElasticCollision(BasicParticle p1, BasicParticle p2, double e) {
		/*if (!p1.collidesWith(p2)) throw new IllegalArgumentException();
		throw new RuntimeException("Not completed");*/
                // Create a normalised vector from p1p2 (vector)
                Vector2D n = Vector2D.minus(p2.getPos(), p1.getPos());
                n.normalise();
                // Get the impulse of p2 (j2)
                double j2 = ((e + 1) * (p1.getVel().scalarProduct(n) - p2.getVel().scalarProduct(n))) / (1/p1.mass + 1/p2.mass);
                // Set new velocity (v2)
                p2.getVel().addScaled(n, j2/p2.mass);
                // Get new velocity (v1)
                Vector2D nModified = n;
                nModified.mult(j2/p1.mass);
                p1.setVel(Vector2D.minus(p1.getVel(), nModified));
	}


}
