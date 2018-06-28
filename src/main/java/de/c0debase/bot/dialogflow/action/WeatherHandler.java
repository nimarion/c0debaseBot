package de.c0debase.bot.dialogflow.action;

import ai.api.model.AIResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.dialogflow.ActionHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.query.*;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;

import java.awt.*;

/**
 * @author Biosphere
 * @date 28.06.18
 */
public class WeatherHandler extends ActionHandler {

    private final int warmTemperature = 24;
    private final int coldTemperature = 14;

    public WeatherHandler() {
        super("weather");
    }

    @Override
    public void handle(AIResponse aiResponse, Message message) {
        if (CodebaseBot.getInstance().getDataWeatherClient() == null) {
            return;
        }
        if (aiResponse.getResult().getParameters().containsKey("address")) {
            currentWeather(aiResponse, message);
        }
    }

    private void currentWeather(AIResponse aiResponse, Message message) {
        JsonObject jsonObject = new JsonParser().parse(aiResponse.getResult().getParameters().get("address").toString()).getAsJsonObject();
        if (jsonObject.get("city") == null) {
            message.getChannel().sendMessage("Leider konnte ich das Wetter in diesem Ort nicht herausfinden").queue();
            return;
        }
        CurrentWeatherOneLocationQuery currentWeatherOneLocationQuery = QueryBuilderPicker.pick()
                .currentWeather()
                .oneLocation()
                .byCityName(jsonObject.get("city").getAsString())
                .type(Type.ACCURATE)
                .language(Language.GERMAN)
                .responseFormat(ResponseFormat.JSON)
                .unitFormat(UnitFormat.METRIC)
                .build();
        CurrentWeather currentWeather = CodebaseBot.getInstance().getDataWeatherClient().getCurrentWeather(currentWeatherOneLocationQuery);
        if (aiResponse.getResult().getParameters().containsKey("temperature")) {
            if (currentWeather.getMainParameters().getTemperature() >= warmTemperature) {
                message.getTextChannel().sendMessage("Das Wetter in " + currentWeather.getCityName() + " ist mit " + currentWeather.getMainParameters().getTemperature() + "°C" + " gerade warm :sunglasses:").queue();
            } else if (currentWeather.getMainParameters().getTemperature() < warmTemperature && currentWeather.getMainParameters().getTemperature() > coldTemperature) {
                message.getTextChannel().sendMessage("Die Temperaturen sind mit " + currentWeather.getMainParameters().getTemperature() + "°C in " + currentWeather.getCityName() + " aktuell  gemäßigt").queue();
            } else {
                message.getTextChannel().sendMessage("Mit " + currentWeather.getMainParameters().getTemperature() + "°C ist das Wetter in " + currentWeather.getCityName() + " kalt :wind_blowing_face:").queue();
            }
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setTitle("Wetter in " + currentWeather.getCityName(), "https://openweathermap.org/city/" + currentWeather.getCityId());
        embedBuilder.addField("Temperatur", currentWeather.getMainParameters().getTemperature() + "°C", false);
        embedBuilder.addField("Luftdruck", String.format("%.1f hPa", currentWeather.getMainParameters().getPressure()), false);
        embedBuilder.addField("Windgeschwindigkeit", String.format("%.1f km/h", currentWeather.getWind().getSpeed()), false);
        embedBuilder.addField("Luftfeuchtigkeit", String.format("%.1f", currentWeather.getMainParameters().getHumidity()) + "%\n", false);
        if (!currentWeather.getWeather().isEmpty()) {
            embedBuilder.setFooter(currentWeather.getWeather().get(0).getDescription(), message.getGuild().getIconUrl());
        }
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
