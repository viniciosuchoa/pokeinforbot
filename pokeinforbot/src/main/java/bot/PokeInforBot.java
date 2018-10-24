package bot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PokeInforBot extends TelegramLongPollingBot {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String link) throws IOException, JSONException {
		URL url = new URL(link);
		URLConnection urlConn = url.openConnection();
		urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
		InputStream is = urlConn.getInputStream();

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
	
	//Bloco de código para COMPLETAR A STRING COM ZERO a esquerda;
	public static String completeToLeft(String value, char c, int size) {
		String result = value;
		while (result.length() < size) {
			result = c + result;
		}
		return result;
	}

	
	public void onUpdateReceived(Update update) {
		if(update.hasMessage()) {
			Message msg = update.getMessage();
			
			SendMessage sm1 = new SendMessage();
			sm1.setChatId(update.getMessage().getChatId());
			
			String dado = msg.getText();
			dado = dado.toLowerCase(); //Para a string ficar sempre minuscula;
			
			//Bloco de código para fazer a leitura da API;
			JSONObject json = null;
			try {
				json = readJsonFromUrl("https://pokeapi.co/api/v2/pokemon/" + dado + "/");
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			
			//Bloco de código para EXIBIR A IMAGEM do Pokémon;
			String img = null;

			String numero = json.get("id").toString();
			numero = completeToLeft(numero, '0', 3);
			img = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + numero + ".png";

		
			//Bloco de código para exibir o TIPO do Pokémon;
			String[] arrayTipo = new String[3];
			for(int i = 0; i < json.getJSONArray("types").length(); i++) {
				arrayTipo[i] = json.getJSONArray("types").getJSONObject(i).getJSONObject("type").getString("name");
			}
			String tipo;
			if(arrayTipo[1] == null) {
				tipo = (arrayTipo[0]);
			} else {
				tipo = (arrayTipo[0] + "/" + arrayTipo[1]);
			}
			
			//Bloco de código para exibir as ABILIDADES do Pokémon;
			String[] arrayAbilities = new String[3];
			for(int i = 0; i < json.getJSONArray("types").length(); i++) {
				arrayAbilities[i] = json.getJSONArray("abilities").getJSONObject(i).getJSONObject("ability").getString("name");
			}
			String ability;
			if(arrayAbilities[1] == null) {
				ability = (arrayAbilities[0]);
			} else {
				ability = (arrayAbilities[0] + "/" + arrayAbilities[1]);
			}
					
			//Bloco de código para ENVIAR A MENSAGEM para o usuário;
			sm1.setText("<b>POKÉMON INFO:</b>" + 
					"\n<b>Nome:</b>	            "+json.get("name") + 
					"\n<b>ID:</b>                    " +json.get("id") +
					"\n<b>Tipo:</b>                " + tipo +
					"\n<b>Habilidades:</b>   " + ability +
					"\n<b>Altura:</b>             " + json.getDouble("height")/10 + "m" + 
					"\n<b>Peso:</b>               "+json.getDouble("weight")/10 + "kg" + 
					"\n<b>Imagem:</b> \n" +  img); 
			sm1.enableHtml(true);
			
			try {
				execute(sm1);
			} catch (TelegramApiException e) {
				e.printStackTrace();

			}
		}
	}

	public String getBotUsername() {
		return "pokeinforbot";
	}

	@Override
	public String getBotToken() {
		return "608006201:AAEHR5W8hnyslU07-jt6XjrY6yQdg9p6Cd4";
	}

}
