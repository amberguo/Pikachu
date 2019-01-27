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

		ArrayList<Ghost> g = new ArrayList<Ghost>();
		Catcher myCatcher = null, yourCatcher = null;
		Hunter myHunter = null, yourHunter = null;
		Support mySup = null, yourSup = null;

		int hunterMoveX = 0;
		int hunterMoveY = 0;
		int supMoveX = 0;
		int supMoveY = 0;
		int supStunId = -10;
		int baseX, baseY;
		if (myTeamId == 0) {
			baseX = 0;
			baseY = 0;
		} else {
			baseX = 16000;
			baseY = 9000;
		}

		// game loop
		while (true) {

			int entities = in.nextInt(); // the number of busters and

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

				// if (myTeamId == 0) {
				// // our base is at upper left corner
				// supMoveX = 14000;
				// supMoveY = 8000;
				// } else {
				// supMoveX = 100;
				// supMoveY = 100;
				// }

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
							myCatcher = new Catcher(x, y, entityId, state,
									false);
						} else {
							// hunter
							myCatcher.setX(x);
							myCatcher.setY(y);
							myCatcher.setState(state);
							if (state == 1) {
								myCatcher.setCatching(true);
							} else {
								myCatcher.setCatching(false);
							}
						}

					} else if (entityRole == 2) {
						if (mySup == null) {
							mySup = new Support(x, y, entityId, state);
						} else {
							mySup.setX(x);
							mySup.setY(y);
							mySup.setState(state);
						}

						if (yourCatcher != null) {
							// follow enemy's catcher
							supMoveX = yourCatcher.getX();
							supMoveY = yourCatcher.getY();
						}

						// has enemy carrying ghost in vision
						if (yourCatcher != null) {
							if (mySup.canStun(yourCatcher)
							/* && yourCatcher.getState() == 1 */) {
								supStunId = yourCatcher.getId();
							}
						}
					}

				} else if (entityType == -1) {
					// ghost
					boolean newGhost = true;
					Ghost oldGhost = null;
					// new ghost

					for (Ghost gh : g) {
						if (gh.getId() == entityId) {
							newGhost = false;
							oldGhost = gh;
						}

					}
					if (newGhost) {
						g.add(new Ghost(x, y, entityId, state, value));
					} else {
						oldGhost.setX(x);
						oldGhost.setY(y);
						oldGhost.setStamina(state);
						oldGhost.setNumOfBusters(value);
						oldGhost.setExist(true);
					}

				} else {
					// enemy

					if (entityRole == 0) {
						if (yourHunter == null) {
							yourHunter = new Hunter(x, y, entityId, state,
									false);
						} else {
							// hunter
							yourHunter.setX(x);
							yourHunter.setY(y);
							yourHunter.setState(state);
						}
					} else if (entityRole == 1) {
						if (yourCatcher == null) {
							System.err.println(
									"INIT yourCATCHER!!" + x + " " + y);
							System.err.println("yourCATCHER's id" + entityId);
							yourCatcher = new Catcher(x, y, entityId, state,
									false);
						} else {
							// hunter
							System.err.println("SET CATCHER!!" + x + " " + y);
							yourCatcher.setX(x);
							yourCatcher.setY(y);
							yourCatcher.setState(state);
						}

					} else if (entityRole == 2) {
						if (yourSup == null) {
							yourSup = new Support(x, y, entityId, state);

						}
					}

				}
			}
			ArrayList<Ghost> ghostToRemove = new ArrayList<Ghost>();

			// cleaning nonexistent ghost
			for (Ghost gh : g) {
				if (!gh.isExist()) {
					ghostToRemove.add(gh);
				}
			}
			for (Ghost gh : ghostToRemove) {
				g.remove(gh);
			}

			/*
			 * // check hunter if (myHunter.getClosestGhost() == null)
			 * { hunterMoveX = 8000; hunterMoveY = 4500; } else { //
			 * ghost exists hunterMoveX =
			 * Util.getX(myHunter.getClosestGhost().getX(),
			 * myHunter.getClosestGhost().getY()); hunterMoveY =
			 * Util.getX(myHunter.getClosestGhost().getX(),
			 * myHunter.getClosestGhost().getY()); }
			 */
			// Ghost ghostToBust = myHunter.GhostToBust();
			// busting

			// First the HUNTER : MOVE x y | BUST id

			// set vision of ghosts
			myHunter.setG(g);

			System.err.println("size of g " + g.size());
			if (myHunter.getGhostToBust() != null
					&& (!myHunter.getGhostToBust().isExist()
							|| myHunter.getGhostToBust().getStamina() == 0)) {
				myHunter.setGhostToBust(null);
			}

			if (myHunter.getGhostToBust() == null) {
				myHunter.setGhostToBust(myHunter.GhostToBust());
			}

			if (myHunter.getGhostToBust() != null
					&& g.contains(myHunter.getGhostToBust())
					&& myHunter.getGhostToBust().getStamina() > 0
					&& Util.getDistance(myHunter.getGhostToBust(),
							myHunter) > 900
					&& Util.getDistance(myHunter.getGhostToBust(),
							myHunter) < 1760) {
				// busting
				System.out.println("BUST " + myHunter.getGhostToBust().getId());
				myHunter.setGhostToBust(null);
				if (myCatcher.getGhostToCatch() == null) {
					myCatcher.setGhostToCatch(myHunter.getGhostToBust());
				}

			} else {
				// move to busting

				if (myHunter.getGhostToBust() != null
						&& g.contains(myHunter.getGhostToBust())) {
					System.out.println("MOVE "
							+ Util.getX(myHunter.getGhostToBust().getX(),
									myHunter.getGhostToBust().getY())
							+ " " + Util.getY(myHunter.getGhostToBust().getX(),
									myHunter.getGhostToBust().getY()));
				} else {
					System.out.println(myHunter.patrol());
				}

			}

			System.err.println("x:" + myHunter.getX() + "y:" + myHunter.getY());

			// Second the GHOST CATCHER: MOVE x y | TRAP id | RELEASE
			for (Ghost gh : g) {
				if (gh.getStamina() == 0)
					myCatcher.setQ(gh);
			}

			if (!g.contains(myCatcher.getGhostToCatch())) {
				myCatcher.setQ(null);
			}

			if (myCatcher.getGhostToCatch() == null
					&& myHunter.getGhostToBust() != null) {
				myCatcher.setGhostToCatch(myHunter.getGhostToBust());
			}

			if (myCatcher.isCatching()) {
				// drop if near base
				if (Util.getDistance(myCatcher, baseX, baseY) < 1600) {
					System.out.println("RELEASE");
				} else {
					System.out.println("MOVE " + baseX + " " + baseY);
					System.err.println("going home");
				}
			} else {
				boolean near = false;
				Ghost target = null;
				for (Ghost gh : g) {
					if (Util.getDistance(gh, myCatcher) > 900
							&& Util.getDistance(gh, myCatcher) < 1760
							&& gh.getStamina() == 0) {
						near = true;
						target = gh;
						break;

					}
				}
				// check ghost on the way

				if (near) {

					System.out.println("TRAP " + target.getId());
				} else if (myCatcher.getGhostToCatch() != null
						&& g.contains(myCatcher.getGhostToCatch())
						&& myCatcher.getGhostToCatch().getStamina() == 0
						&& Util.getDistance(myCatcher.getGhostToCatch(),
								myCatcher) > 900
						&& Util.getDistance(myCatcher.getGhostToCatch(),
								myCatcher) < 1760) {

					System.out.println(
							"TRAP " + myCatcher.getGhostToCatch().getId());
					myCatcher.setGhostToCatch(null);

				} else {
					if (myCatcher.getGhostToCatch() != null) {
						System.out.println("MOVE "
								+ Util.getX(myCatcher.getGhostToCatch().getX(),
										myCatcher.getGhostToCatch().getY())
								+ " "
								+ Util.getY(myCatcher.getGhostToCatch().getX(),
										myCatcher.getGhostToCatch().getY()));
					} else {
						myCatcher.setQ(null);
						System.out.println(myCatcher.patrol());
					}
				}
			}

			// Third the SUPPORT: MOVE x y | STUN id | RADAR
			mySup.turn();
			if (supStunId != -10 && (mySup.getCool() == 0)) {
				// System.err.println("STUN id is" + supStunId);
				System.out.println("STUN " + supStunId);
				mySup.setCool(20);
				if (myCatcher.getGhostToCatch() != null) {
					for (Ghost gh : g) {
						if (gh.getStamina() == 0) {
							myCatcher.setGhostToCatch(gh);
						}
					}
				}
			} else {

				System.out.println(mySup.patrol());
			}

			// reset ghost array
			for (Ghost gh : g) {
				gh.setExist(false);
			}
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
	boolean exist;

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public Ghost(int x, int y, int id, int stamina, int numOfBusters) {
		super(x, y, id);
		this.stamina = stamina;
		this.numOfBusters = numOfBusters;
		exist = true;

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
	int dest = 0;// 0 1 2 3
	String nextMove = "";

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

	public String patrol() {
		System.err.println("PATROLLING");
		int[] xarr = { 3500, 12000, 3500, 12000 };
		int[] yarr = { 1250, 1250, 6500, 6500 };

		if (dest == 0) {
			System.err.println("PATROLLING 0");
			if (x == xarr[dest] && y == yarr[dest]) {
				dest = (dest + 1) % 4;
				return "MOVE " + xarr[dest] + " " + yarr[dest];
			} else {
				return "MOVE " + xarr[dest] + " " + yarr[dest];
			}
		} else if (dest == 1) {
			System.err.println("PATROLLING 1");
			if (x == xarr[dest] && y == yarr[dest]) {
				dest = (dest + 1) % 4;
				return "MOVE " + xarr[dest] + " " + yarr[dest];
			} else {
				return "MOVE " + xarr[dest] + " " + yarr[dest];
			}
		} else if (dest == 2) {
			if (x == xarr[dest] && y == yarr[dest]) {
				dest = (dest + 1) % 4;
				return "MOVE " + xarr[dest] + " " + yarr[dest];
			} else {
				return "MOVE " + xarr[dest] + " " + yarr[dest];
			}
		} else if (dest == 3) {
			if (x == xarr[dest] && y == yarr[dest]) {
				dest = (dest + 1) % 4;
				return "MOVE " + xarr[dest] + " " + yarr[dest];
			} else {
				return "MOVE " + xarr[dest] + " " + yarr[dest];
			}
		}
		System.err.println(dest);
		return "";
	}
	/*
	 * if (nextMove != "") { String move = nextMove; nextMove = "";
	 * return move; }
	 * 
	 * if (x < 8000 && y < 4500) { if (nextMove == "") { nextMove =
	 * "MOVE 15000 1000"; } return "MOVE 15000 1000"; } else if (x <
	 * 8000 && y > 4500) { if (nextMove == "") { nextMove =
	 * "MOVE 1000 1000"; } return "MOVE 1000 1000"; } else if (x >
	 * 8000 && y < 4500) { if (nextMove == "") { nextMove =
	 * "MOVE 15000 8000"; } return "MOVE 15000 8000"; } else { if
	 * (nextMove == "") { nextMove = "MOVE 1000 8000"; } return
	 * "MOVE 1000 8000"; } }
	 */

}

class Hunter extends Buster {
	int state;
	boolean busting;
	Ghost closestGhost;
	ArrayList<Ghost> g = null;
	Ghost GhostToBust;

	public Ghost getGhostToBust() {
		return GhostToBust;
	}

	public void setGhostToBust(Ghost ghostToBust) {
		GhostToBust = ghostToBust;
	}

	public ArrayList<Ghost> getG() {
		return g;
	}

	public void setG(ArrayList<Ghost> g) {
		this.g = g;
	}

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
		System.err.println("Setting closet ghost");
		System.err.println("arr size" + arr.size());

		for (int i = 0; i < arr.size(); i++) {

			if (Util.getDistance(arr.get(i), this) > 0) {
				d = Util.getDistance(arr.get(i), this);
				this.closestGhost = arr.get(i);
				System.err.println("cloest ghost is " + this.closestGhost);
			}
		}

	}

	public Ghost GhostToBust() {
		Ghost result = this.getClosestGhost();
		for (Ghost gh : g) {

			if (900 < Util.getDistance(this, gh)
					&& Util.getDistance(this, gh) < 1760) {

				result = gh;
			}
		}
		return result;

	}

}

class Catcher extends Buster {
	boolean catching;
	Ghost ghostToCatch;
	Queue<Ghost> ghostQ = new LinkedList<Ghost>();

	public void setQ(Ghost ghostToAdd) {
		if (ghostToAdd != null && !(ghostQ.contains(ghostToAdd))
				&& (ghostToCatch != ghostToAdd)) {
			ghostQ.add(ghostToAdd);
		}

		if (ghostToCatch == null) {
			ghostToCatch = ghostQ.poll();
		}
		System.err.println(ghostQ.size());

	}

	public Ghost getGhostToCatch() {
		return ghostToCatch;
	}

	public void setGhostToCatch(Ghost ghostToCatch) {
		this.ghostToCatch = ghostToCatch;
	}

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

	int cool = 0;

	public int getCool() {
		return cool;
	}

	public void setCool(int cool) {
		this.cool = cool;
	}

	public void turn() {
		if (cool != 0) {
			cool--;
		}
	}

	public Support(int x, int y, int id, int state) {
		super(x, y, id, state);
		// TODO Auto-generated constructor stub
	}

	public boolean canStun(Catcher enemy) {
		if (Util.getDistance(this, enemy) <= 1760) {
			return true;
		}
		return false;

	}

}

class Util {
	public static int getDistance(Role r1, Role r2) {
		return (int) Math.sqrt((r1.getX() - r2.getX()) * (r1.getX() - r2.getX())
				+ (r2.getY() - r2.getY()) * (r2.getY() - r2.getY()));

	}

	public static int getDistance(Role r, int x, int y) {
		return (int) Math.sqrt((r.getX() - x) * (r.getX() - x)
				+ (r.getY() - y) * (r.getY() - y));
	}

	public static int getX(int x, int y) {
		if (x < 8000) {
			return x + 950;
		} else {
			return x - 950;
		}
	}

	public static int getY(int x, int y) {
		return y;
	}
}
