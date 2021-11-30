package SeaWolf;

import discord4j.core.object.entity.Message;

import java.util.Locale;


public class CommandService {
    public static void onMessage(Message message){
        //if the message (passed onto this method from Bot.java) starts with cmdChar(also passed from Bot.java),
        //then we do commands and stuff.
        if (message.getContent().toLowerCase(Locale.ROOT).startsWith(String.valueOf(SeaWolf.Bot.cmdChar()))) {
            //removes the command character from the front of the string. In this case, it removes the "!" char
            String command = message.getContent().toString().substring(1);

            //recognizes a command and then does something with it.
            if (command.startsWith("test")){
                SeaWolf.Bot.sendMessage(message, "Bed!");
                return;
            }
            else if(command.startsWith("ping")){
                SeaWolf.Bot.sendMessage(message, "Pong!");
                return;
            }
            else if(command.startsWith("help")){
                SeaWolf.Bot.sendMessage(message, "This is the help menu, which is not yet developed.");
                return;
            }
            else{
                SeaWolf.Bot.sendMessage(message, "Command unrecognized.");
                return;
            }
        }
        else{
            return;
        }
    }
}
