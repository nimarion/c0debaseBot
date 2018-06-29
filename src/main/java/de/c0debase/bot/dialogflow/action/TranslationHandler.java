package de.c0debase.bot.dialogflow.action;

import ai.api.model.AIResponse;
import com.google.gson.JsonParser;
import de.c0debase.bot.dialogflow.ActionHandler;
import de.linus.deepltranslator.DeepLTranslator;
import de.linus.deepltranslator.Language;
import de.linus.deepltranslator.Translation;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 29.06.18
 */
public class TranslationHandler extends ActionHandler {

    public TranslationHandler() {
        super("translate");
    }

    @Override
    public void handle(AIResponse aiResponse, Message message) {
      if(aiResponse.getResult().getParameters().containsKey("text") && aiResponse.getResult().getParameters().containsKey("lang-to")){
          Language language = getLanguage(aiResponse.getResult().getParameters().get("lang-to").getAsString());
          if(language == null){
              message.getTextChannel().sendMessage("Ich kann aktuell nur die Sprachen ```Deutsch, Englisch, Französisch, Spanisch, Polnisch und Niederländisch```übersetzen :smile:").queue();
              return;
          }
          try {
              Translation translation = DeepLTranslator.translate(new JsonParser().parse(aiResponse.getResult().getParameters().get("text").toString()).getAsString(), Language.AUTO_DETECT, language);
              if(!translation.printError() && !translation.getTranslations().isEmpty()) {
                  message.getTextChannel().sendMessage(translation.getTranslations().get(0)).queue();
                  return;
              }
          } catch (Exception exception){
              exception.printStackTrace();
          }
      } else if(aiResponse.getResult().getParameters().containsKey("text") && !aiResponse.getResult().getParameters().containsKey("lang-to")){
          message.getTextChannel().sendMessage("Ich kann aktuell nur die Sprachen ```Deutsch, Englisch, Französisch, Spanisch, Polnisch und Niederländisch```übersetzen :smile:").queue();
          return;
      }
      message.getTextChannel().sendMessage("Ich habe leider keiner passende Übersetzung finden können.").queue();
    }

    public Language getLanguage(String input){
        switch ( input.toLowerCase()){
            case "deutsch":
                return Language.GERMAN;
            case "französisch":
               return Language.FRENCH;
            case "spanisch":
                return Language.SPANISH;
            case "italienisch":
                return Language.ITALIAN;
            case "polnisch":
                return Language.POLISH;
            case "englisch":
                return Language.ENGLISH;
            case "niederländisch":
                return Language.DUTCH;
                default:
                    return null;
        }
    }

}
