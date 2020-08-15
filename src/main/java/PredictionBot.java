

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class PredictionBot extends TelegramLongPollingBot {

    public String il;
    public String ilce;
    public String oda;
    public int m2;
    Prediction prediction;

    public PredictionBot(Prediction prediction){
        this.prediction=prediction;
    }


    public void onUpdateReceived(Update update) {

        //get message from user:
        String text=update.getMessage().getText();

        try {
            if (!text.equals("/start")) {

                //delete space in message
                String textX = "";
                do {
                    if (text.contains(" ")) {
                        String[] text1 = text.split(" ");
                        for (String t : text1) {
                            textX += t;
                        }
                    }
                } while (textX.contains(" "));


                //split message part
                LinkedList<String> textY = new LinkedList<String>();
                if (text.contains(",")) {
                    String[] text1 = textX.split(",");
                    textY.addAll(Arrays.asList(text1));
                }
                il = textY.get(0).toLowerCase().trim();
                ilce = textY.get(1).toLowerCase().trim();
                m2 = Integer.parseInt(textY.get(2).trim());
                oda = textY.get(3).toLowerCase().trim();

                int estimate = prediction.predict(il, ilce, oda, m2);

                String msg ="Tahmini fiyat "+ estimate +" TL";


                SendMessage message = new SendMessage();
                message.setText(msg);
                message.setChatId(update.getMessage().getChatId());

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


            }
        }catch (Exception e){
            SendMessage errMsg = new SendMessage();
            errMsg.setText("Hatalı veri girdiniz veya veri indexte bulunamadı.\n" +
                    "Örnek kullanım:\n" +
                    "istanbul, avcılar, 120, 2+1   (Enter)");
            errMsg.setChatId(update.getMessage().getChatId());

            try {
                execute(errMsg);
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }

        }

    }


    public String getBotUsername() {
        return "FiyatTahmin_bot";
    }

    public String getBotToken() {
        return "Bot token";
    }
}