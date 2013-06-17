package robots;

import java.awt.Color;
import java.awt.Point;

import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.exception.RobotException;

public class ZiplokkMobile extends Robot {
	private boolean hasScannedRobot = false;
	public void run() { 
		this.setRadarColor(Color.pink);
		this.setBodyColor(Color.green);
		this.setAdjustGunForRobotTurn(false);
		this.setAdjustRadarForGunTurn(true);
		this.setAdjustRadarForRobotTurn(true);
		while(true) { 
			if(!hasScannedRobot) {
				this.turnRadarRight(1);
			} else { 
				this.turnRadarRight(90);
				this.turnRadarLeft(90);
			}
		}
	}
	
	@Override
	public void ahead(double i) throws RobotException { 
		super.ahead(i);
	}
	
	@Override
	public void onHitWall(HitWallEvent e) { 
		this.turnLeft(100);
	}
	
	public void scanForWall() { 
		int heading = (int) this.getHeading();
	}
	
	@Override
	public void onHitByBullet(HitByBulletEvent e) { 
		this.ahead(100);
	}
	
	public void onScannedRobot(final ScannedRobotEvent e) { 
		hasScannedRobot = true;
		double bearingFull = -1;
		if(e.getBearing() < 0) { 
			bearingFull = 360 + (e.getBearing());
		} else if(e.getBearing() > 0) { 
			bearingFull = 0 + e.getBearing();
		}
		//out.println(bearingFull);
		//trackRobot(bearingFull);
		//hasScannedRobot = false;
		trackRobot0(e.getBearing());
	}
	
	public static int RIGHT = 0;
	public static int LEFT = 1;
	public static int BEHIND = 2;
	public static int FRONT = 3;
	
	public void trackRobot0(double d) { 
		out.println(d);
		double DEGREES = Math.abs(d);
		int DIRECTION = -1;
		if(d > 10) { 
			DIRECTION = RIGHT;
		} else if (d < -10) { 
			DIRECTION = LEFT;
		} else if (DEGREES < 10) { 
			DIRECTION = BEHIND;
		} else if (DEGREES > 170) {
			DIRECTION = FRONT;
		} else { 
			out.println("opponent dir unspecified");
		}
		if(DEGREES < 10) { 
			DIRECTION = BEHIND;
		}
		if(DEGREES > 170) { 
			DIRECTION = FRONT;
		}
		
		switch (DIRECTION) { 
		case 0: 
			out.println("RIGHT");
			turnRight(DEGREES);
			ahead(10);
			break;
		case 1:
			out.println("LEFT");
			turnLeft(DEGREES);
			ahead(10);
			break;
		case 2:
			out.println("FRONT");
			ahead(10);
			fire(1);
			break;
		case 3:
			out.println("BEHIND");
			turnRight(180);
			ahead(10);
			break;
		}
	}
	
	public void trackRobot(double d) { 
		double robotHeading = this.getHeading();
		int DIRECTION = -1;
		double DEGREE_OF_SEPARATION = 0;
		double OPPONENT_BEARING = 0;
		out.println(robotHeading);
		
		if(d > 180 && robotHeading > 180) { 
			out.println("both in LEFT hemisphere");
			if(robotHeading > d) { 
				DIRECTION = LEFT;
				DEGREE_OF_SEPARATION = (double) robotHeading - d;
			} else if (robotHeading < d) { 
				DIRECTION = RIGHT;
				DEGREE_OF_SEPARATION = d - (double) robotHeading;
			}
		} else if (d > 180 && robotHeading < 180) { 
			OPPONENT_BEARING = (robotHeading - 180) + 360;
			if(robotHeading + 180 < OPPONENT_BEARING) { 
				DIRECTION = LEFT;
				DEGREE_OF_SEPARATION = (double) (robotHeading + 360) - d;
			} else if (robotHeading + 180 > OPPONENT_BEARING) { 
				DIRECTION = RIGHT;
				DEGREE_OF_SEPARATION = d - (double) robotHeading;
			}
		} else if (d < 180 && robotHeading < 180) { 
			out.println("Both in RIGHT hemisphere");
			if(robotHeading < d) { 
				DIRECTION = RIGHT;
				DEGREE_OF_SEPARATION = d - (double) robotHeading;
			} else if (robotHeading > d) { 
				DIRECTION = LEFT;
				DEGREE_OF_SEPARATION = (double) robotHeading - d;
			}
		} else if (d < 180 && robotHeading > 180) { 
			OPPONENT_BEARING = (robotHeading + 180) - 360;
			if(robotHeading - 180 > d) { 
				DIRECTION = RIGHT;
				DEGREE_OF_SEPARATION = (d + 360) - (double) robotHeading;
			} else if (robotHeading - 180 < d) { 
				DIRECTION = LEFT;
				DEGREE_OF_SEPARATION = (double) robotHeading - d;
			}
		} else { 
			out.println("Direction Unknown");
		}
		
		if(DIRECTION == RIGHT) { 
			out.println("Opponent Right");
			this.turnGunRight(DEGREE_OF_SEPARATION);
		} else if (DIRECTION == LEFT) { 
			out.println("Opponent Left");
			this.turnGunLeft(DEGREE_OF_SEPARATION);
		} else { 
			out.println("Opponent Bearing Unknown");
		}
		
		hasScannedRobot = false;
	}
}
