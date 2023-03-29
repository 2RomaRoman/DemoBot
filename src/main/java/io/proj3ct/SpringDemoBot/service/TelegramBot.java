package io.proj3ct.SpringDemoBot.service;


import io.proj3ct.SpringDemoBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    public TelegramBot(BotConfig config) {
        this.config = config;
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();
            String path = "https://i.pinimg.com/736x/f4/d2/96/f4d2961b652880be432fb9580891ed62.jpg";
            String caption = "фоточка котика";
            String filePath = "BQACAgIAAx0CdD7q5AADDGQjZLBTtJRKWBQaw69FKR0AAROPcQACojAAAq7hGUm45TCU9hApei8E";
            String videoID = "BAACAgIAAx0CdD7q5AADDWQjaICt-ZgsJYpJik2X1Y5M17RaAAKtMAACruEZSdEmRRi-hrQGLwQ";

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatID, update.getMessage().getChat().getFirstName());
                    break;
                case "/image":
                    sendPhoto(chatID, caption, path);
                    break;

                case "/file":
                    sendFile(chatID, filePath);
                    break;

                case "/video":
                    sendVideo(chatID,videoID);
            }
        }

    }
    private void startCommandReceived(long chatID, String name) {

        String answer = "Приветствую, " + name +", рад знакомству! Это мой первый бот на java spring. Вот список команд: /image, /file, /video";
        log.info("Replied to user " + name + " " + chatID);

        sendMessage(chatID, answer);
    }

    private void sendPhoto(long chatID, String caption, String pathToPhoto){
        SendPhoto request = new SendPhoto();
        request.setChatId(chatID);
        request.setCaption(caption);
        request.setPhoto(new InputFile(pathToPhoto));


        try {
            execute(request);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
        private void sendVideo(long chatID,  String videoID){
        SendVideo request = new SendVideo();
        request.setChatId(chatID);
        request.setVideo(new InputFile(videoID));
        try {
            execute(request);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendFile(long chatID, String FileId){
    SendDocument request = new SendDocument();
    request.setChatId(chatID);
    request.setDocument(new InputFile(FileId));

    try {
        execute(request);
        }
    catch (TelegramApiException e) {
        e.printStackTrace();
    }

    }
    private void sendMessage(long chatID, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch (TelegramApiException e) {
           log.error("Error occurred: " + e.getMessage());
        }
    }
}
