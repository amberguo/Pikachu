import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Send your busters out into the fog to trap ghosts and bring them home!
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int bustersPerPlayer = in.nextInt(); // the amount of busters you control
		int ghostCount = in.nextInt(); // the amount of ghosts on the map
		int myTeamId = in.nextInt(); // if this is 0, your base is on the top left of the map, if it is one, on the bottom right

		// game loop
		while (true) {
			int entities = in.nextInt(); // the number of busters and ghosts visible to you
			for (int i = 0; i < entities; i++) {
				int entityId = in.nextInt(); // buster id or ghost id
				int x = in.nextInt();
				int y = in.nextInt(); // position of this buster / ghost
				int entityType = in.nextInt(); // the team id if it is a buster, -1 if it is a ghost.
				int entityRole = in.nextInt(); // -1 for ghosts, 0 for the HUNTER, 1 for the GHOST CATCHER and 2 for the SUPPORT
				int state = in.nextInt(); // For busters: 0=idle, 1=carrying a ghost. For ghosts: remaining stamina points.
				int value = in.nextInt(); // For busters: Ghost id being carried/busted or number of turns left when stunned. For ghosts: number of busters attempting to trap this ghost.
			}

			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");
			Ghost g;
			Catcher catcher;
			Hunter hunter;
			Supporter sup;

			int hunterMoveX;
			int hunterMoveY;

			// update my busters' position
			if(entityType == myTeamId ) {
				//busters of own team
				if( entityRole == 0){
					//hunter
					hunter.setX(x);
					hunter.setY(y);
				} else if ( entityRole ==1) {
					// Catcher
					catcher.setX(x);
					catcher.setY(y);

				} else if ( entityRole == 2){
					// sup
					sup.setX(x);
					sup.setY(y);
				} else {
					// ghost
					g.setX(x);
					g.setY(y);
				}

			}

			//check hunter
			if (hunter.closestGhost() == null ) {
				hunterMoveX= 8000;
				hunterMoveY= 4500;
			} else {
				// ghost exists
				hunterMoveX= g.getX();
				hunterMoveY= g.getY();
				
			
			}
			
			





			// First the HUNTER : MOVE x y | BUST id
			// Second the GHOST CATCHER: MOVE x y | TRAP id | RELEASE
			// Third the SUPPORT: MOVE x y | STUN id | RADAR
			System.out.println("MOVE 8000 4500");
			System.out.println("MOVE 8000 4500");
			System.out.println("MOVE 8000 4500");
		}
	}
}

class Role {
	int x, y, id ;

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


class Ghost extends Role{
	int stamina, numOfBusters;
	boolean enemy;

	public Ghost(int x, int y, int id, int stamina, int numOfBusters,
			boolean enemy) {
		super(x, y, id);
		this.stamina = stamina;
		this.numOfBusters = numOfBusters;
		this.enemy = enemy;
	}

	public boolean isEnemy() {
		return enemy;
	}

	public void setEnemy(boolean enemy) {
		this.enemy = enemy;
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

class Buster extends Role{

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

class Hunter extends Buster{
	int state;
	boolean busting;
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

}


class Catcher extends Buster{
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

class Support extends Buster{

	public Support(int x, int y, int id, int state) {
		super(x, y, id, state);
		// TODO Auto-generated constructor stub
	}


}


}