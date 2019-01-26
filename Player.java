import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Send your busters out into the fog to trap ghosts and bring them
 * home!
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int bustersPerPlayer = in.nextInt(); // the amount of busters
												// you control
		int ghostCount = in.nextInt(); // the amount of ghosts on the
										// map
		int myTeamId = in.nextInt(); // if this is 0, your base is on
										// the top left of the map, if
										// it is one, on the bottom
										// right

		// game loop
		while (true) {
			int entities = in.nextInt(); // the number of busters and
			ArrayList<Ghost> g = new ArrayList<Ghost>();
			Catcher myCatcher = null, yourCatcher;
			Hunter myHunter = null, yourHunter;
			Support sup = null;

			// ghosts visible to you
			for (int i = 0; i < entities; i++) {
				int entityId = in.nextInt(); // buster id or ghost id
				int x = in.nextInt();
				int y = in.nextInt(); // position of this buster /
										// ghost
				int entityType = in.nextInt(); // the team id if it is
												// a buster, -1 if it
												// is a ghost.
				int entityRole = in.nextInt(); // -1 for ghosts, 0 for
												// the HUNTER, 1 for
												// the GHOST CATCHER
												// and 2 for the
												// SUPPORT
				int state = in.nextInt(); // For busters: 0=idle,
											// 1=carrying a ghost. For
											// ghosts: remaining
											// stamina points.
				int value = in.nextInt(); // For busters: Ghost id
											// being carried/busted or
											// number of turns left
											// when stunned. For
											// ghosts: number of
											// busters attempting to
											// trap this ghost.

				// update my busters' position
				if (entityType == myTeamId) {
					// busters of own team
					if (entityRole == 0) {
						if (myHunter == null) {
							myHunter = new Hunter(x, y, entityId, state, false);
						} else {
							// hunter
							myHunter.setX(x);
							myHunter.setY(y);
							myHunter.setState(state);
						}
					} else if (entityRole == 1) {
						if (myCatcher == null) {
							myCatcher = new Catcher(x, y, entityId, state, false);
						} else {
							// hunter
							myCatcher.setX(x);
							myCatcher.setY(y);
							myCatcher.setState(state);
						}
					
					} else if (entityRole == 2) {
						// sup
						sup.setX(x);
						sup.setY(y);
					} else {
						// ghost
						boolean newGhost = true;
						Ghost oldGhost = null;
						//new ghost
						for (Ghost gh:g) {
							if (gh.getId() == entityId) {
								newGhost = false;
								oldGhost = gh;
							}
							
						}
						if (newGhost) {
							g.add(new Ghost(x,y,entityId,state,value));
						} else {
							oldGhost.setX(x);
							oldGhost.setY(y);
							oldGhost.setStamina(state);
							oldGhost.setNumOfBusters(value);
						}
						
					}

				}

			}

			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");

			int hunterMoveX;
			int hunterMoveY;

			// set vision of ghosts
			myHunter.setClosestGhost(g);
			// check hunter
			if (myHunter.getClosestGhost() == null) {
				hunterMoveX = 8000;
				hunterMoveY = 4500;
			} else {
				// ghost exists
				hunterMoveX = myHunter.getClosestGhost().getX();
				hunterMoveY = myHunter.getClosestGhost().getY();

			}

			// First the HUNTER : MOVE x y | BUST id
			// Second the GHOST CATCHER: MOVE x y | TRAP id | RELEASE
			// Third the SUPPORT: MOVE x y | STUN id | RADAR
			System.out.println("MOVE " + hunterMoveX + hunterMoveY);
			System.out.println("MOVE " + hunterMoveX + hunterMoveY);
			
			System.out.println("MOVE 8000 4500");
		}
	}
}

class Role {
	int x, y, id;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Role(int x, int y, int id) {
		super();
		this.x = x;
		this.y = y;
		this.id = id;
	}

}

class Ghost extends Role {
	int stamina, numOfBusters;

	public Ghost(int x, int y, int id, int stamina, int numOfBusters) {
		super(x, y, id);
		this.stamina = stamina;
		this.numOfBusters = numOfBusters;

	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getNumOfBusters() {
		return numOfBusters;
	}

	public void setNumOfBusters(int numOfBusters) {
		this.numOfBusters = numOfBusters;
	}

}

class Buster extends Role {

	int state;

	public Buster(int x, int y, int id, int state) {
		super(x, y, id);
		this.state = state;

	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}

class Hunter extends Buster {
	int state;
	boolean busting;
	Ghost closestGhost;

	public Hunter(int x, int y, int id, int state, boolean busting) {
		super(x, y, id, state);
		this.state = state;
		this.busting = busting;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isBusting() {
		return busting;
	}

	public void setBusting(boolean busting) {
		this.busting = busting;
	}

	public Ghost getClosestGhost() {
		return closestGhost;
	}

	public void setClosestGhost(ArrayList<Ghost> arr) {

		int d = Integer.MAX_VALUE;
		for (Ghost g : arr) {
			if (getDistance(g, this) < d) {
				d = getDistance(g, this);
				this.closestGhost = g;
			}
		}

	}

	private int getDistance(Role r1, Role r2) {
		return ((r1.getX() - r2.getX()) ^ 2 + (r2.getY() - r2.getY()) ^ 2)
				^ (1 / 2);

	}

}

class Catcher extends Buster {
	boolean catching;

	public Catcher(int x, int y, int id, int state, boolean catching) {
		super(x, y, id, state);
		this.catching = catching;
	}

	public boolean isCatching() {
		return catching;
	}

	public void setCatching(boolean catching) {
		this.catching = catching;
	}

}

class Support extends Buster {

	public Support(int x, int y, int id, int state) {
		super(x, y, id, state);
		// TODO Auto-generated constructor stub
	}

}
