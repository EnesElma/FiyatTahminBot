import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main{
    public static void main(String[] args) {

        Prediction prediction=new Prediction();
        prediction.startAndLoad();

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new PredictionBot(prediction));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
