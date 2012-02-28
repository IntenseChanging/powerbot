package org.powerbot.game.api.methods;

import org.powerbot.game.api.Constants;
import org.powerbot.game.bot.Bot;

public class Game {
	public static int getClientState() {
		Bot bot = Bot.resolve();
		Constants constants = bot.constants;
		int clientState = bot.client.getLoginIndex() * bot.multipliers.GLOBAL_LOGININDEX;
		if (clientState == constants.CLIENTSTATE_3) {
			return 3;
		} else if (clientState == constants.CLIENTSTATE_6) {
			return 6;
		} else if (clientState == constants.CLIENTSTATE_7) {
			return 7;
		} else if (clientState == constants.CLIENTSTATE_9) {
			return 9;
		} else if (clientState == constants.CLIENTSTATE_10) {
			return 10;
		} else if (clientState == constants.CLIENTSTATE_11) {
			return 11;
		} else if (clientState == constants.CLIENTSTATE_12) {
			return 12;
		}
		return -1;
	}
}
