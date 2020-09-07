package com.porterdustin.zombienuke;

public class PowerUpGenerator {
	
	public PowerUpGenerator () {
		
	}
	
	public int[] getPowerUpList(int level, int choice, int teir) {
		int[] powerUpList = new int[4];
		
		powerUpList[0] = teir;
		powerUpList[1] = 0;
		powerUpList[2] = 0;
		powerUpList[3] = 0;
		
		//-1 - random
		//0 - none
		//1 - pistols
		//2 - axes
		//3 - shotguns
		//4 - machine guns
		//5
		//6 - cure
		//7 - helmets
		//8 - dynamite
		//9 - blast shields
		//10 - speed
		//11 - camouflage
		//12 - bear traps
		//13 - dogs
		//14 - landmines
		
		if (choice == 2) { //random
			powerUpList[1] = -1;
		} else if (teir == 1) {
			if (choice == 0) {		//offensive
				powerUpList[1] = 1;
			} else if (choice == 1) { //defensive
				if (level % 2 == 0) {
					powerUpList[1] = 2;
				} else {
					powerUpList[1] = 7;
				}
			}
		} else if (teir == 2) {
			if (choice == 0) {		//offensive
				if (level % 3 == 0) {
					powerUpList[1] = 3;
					powerUpList[2] = 1;
				} else if (level % 3 == 1) {
					powerUpList[1] = 1;
					powerUpList[2] = 14;
					powerUpList[3] = 0;
				} else {
					powerUpList[1] = 10;
					powerUpList[2] = 4;
					powerUpList[3] = 1;
				}
			} else if (choice == 1) { //defensive
				if (level % 4 == 0) {
					powerUpList[1] = 9;
					powerUpList[2] = 12;
				} else if (level % 4 == 1) {
					powerUpList[1] = 7;
					powerUpList[2] = 2;
				} else if (level % 4 == 2){
					powerUpList[1] = 11;
					powerUpList[2] = 13;
				} else {
					powerUpList[1] = 8;
					powerUpList[2] = 2;
					powerUpList[3] = 12;
				}
			}
		}
		
		
		return powerUpList;
	}
	
}