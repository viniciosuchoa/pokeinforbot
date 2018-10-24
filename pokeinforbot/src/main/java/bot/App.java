package bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class App {

	public static void main(String[] args) {
		
		ApiContextInitializer.init();
		
		TelegramBotsApi pokeBot = new TelegramBotsApi();
		
		try {
			pokeBot.registerBot(new PokeInforBot());
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}

	}

}
