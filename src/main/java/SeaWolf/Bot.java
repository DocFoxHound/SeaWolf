package SeaWolf;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Bot {

    //Execute command??
    public interface Command {
        void execute(MessageCreateEvent event);
    }

    public static void main(String[] args) throws IOException {
        String token;
        //get the token
        try (InputStream input = new FileInputStream("src/main/java/config.properties")){
            Properties prop = new Properties();
            prop.load(input);
            token = prop.getProperty("app.token");
            System.out.println(token);
        }




        //logging into the bot and keeping it logged in
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();

        //prints out to console that the bot logged in/started up. Not needed, but nice to have.
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    System.out.printf(
                            "Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator()
                    );
                });

        //check for the ! command
        client.getEventDispatcher().on(MessageCreateEvent.class)
                // subscribe is like block, in that it will *request* for action
                // to be done, but instead of blocking the thread, waiting for it
                // to finish, it will just execute the results asynchronously.
                .subscribe(event -> {
                    // 3.1 Message.getContent() is a String.
                    //Create the data to send to the external method
                    final String content = event.getMessage().getContent();

                    //capture the MessageCreateEvent as a... Message that we can send to the method.
                    //The reason why is because the Message Data Type contains info such as the discord
                    //server, the message content, and the sender, which can all be useful when
                    //processing commands
                    Message message = event.getMessage();
                    CommandService.onMessage(message); //this is the method we're sending the message to.
                });
        //prevents disconnect. I hear if I have a daemon thread running I wont need to use this,
        //but I'm not sure how to do that. Also I may need to use .subscribe on login if I wanted
        //to use a daemon thread?
        client.onDisconnect().block();
    }

    //this is the command each server can setup to customize their bot, if need be. Right now it is static.
    //I don't actually want to do this because its time consuming and can become burdensome on the bot if not handled
    //correctly, which I don't think I can haha. We can talk about this later.
    public static String cmdChar(){
        return("!");
    }

    //I just decided to make my own method for handling sending messages back to Discord. Its completely unnecessary,
    //but it is shorter than typing it all out. the Return block shows the full way to send a message, just replace
    //text with "strings go here" to send a message yourself.
    public static Message sendMessage(Message message, String text){
        return (message.getChannel().block().createMessage(text).block());
    }
}
