package de.c0debase.bot.commands.general;

import com.google.gson.Gson;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import okhttp3.*;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Represents the command execution handler of the '!latex' command
 *
 * @author Lukas Schulte Pelkum (Lukaesebrot)
 * @version 1.0
 * @since 1.0
 */
public class LaTeXCommand extends Command {

    // Define the rTeX API URL
    private static final String RTEX_URL = "https://rtex.probablyaweb.site/api/v2";

    // Define the LaTeX template
    private static final String LATEX_TEMPLATE = "\\documentclass[varwidth,border=2pt]{standalone} \\usepackage[utf8]{inputenc} \\usepackage{amsmath} \\usepackage{xcolor} \\definecolor{bgcolor}{HTML}{2F3136} \\begin{document} \\pagecolor{bgcolor} \\color{white} #CONTENT# \\end{document}";

    // Define the OkHttp client
    private final OkHttpClient httpClient;

    /**
     * Creates a new LaTeX command execution handler
     */
    public LaTeXCommand() {
        super("latex", "Rendert eine gegebene LaTeX expression zu einem Bild", Category.GENERAL, "tex");
        this.httpClient = new OkHttpClient();
    }

    @Override
    public void execute(String[] args, Message message) {
        // Define the LaTeX expression
        String expression = String.join(" ", args).trim();

        // Check of the expression is empty
        if (expression.isEmpty()) {
            MessageEmbed embed = getEmbed(message.getGuild(), message.getAuthor())
                    .setTitle("Fehler")
                    .setColor(Color.RED)
                    .setDescription("Die LaTeX expression darf nicht leer sein!")
                    .build();
            message.getTextChannel().sendMessage(embed).queue();
            return;
        }

        // Render the expression
        MessageEmbed embed = getEmbed(message.getGuild(), message.getAuthor())
                .setTitle("Rendert...")
                .setColor(Color.YELLOW)
                .setDescription("Die Expression wird gerendert...")
                .build();
        message.getTextChannel().sendMessage(embed).queue(msg -> {
            // Retrieve the image URL
            renderLaTeX(expression).whenComplete((imageURL, throwable) -> {
                // Check if an error occurred
                if (throwable != null) {
                    MessageEmbed errorEmbed = getEmbed(message.getGuild(), message.getAuthor())
                            .setTitle("Fehler")
                            .setColor(Color.RED)
                            .setDescription("`" + throwable.getMessage() + "`")
                            .build();
                    msg.editMessage(errorEmbed).queue();
                    return;
                }

                // Respond with the image
                MessageEmbed successEmbed = getEmbed(message.getGuild(), message.getAuthor())
                        .setTitle("LaTeX-Expression")
                        .setColor(Color.GREEN)
                        .setImage(imageURL)
                        .build();
                msg.editMessage(successEmbed).queue();
            });
        });
    }

    /**
     * Renders the given LaTeX expression as an image
     *
     * @param expression The expression to render
     * @return A future containing the image URL
     */
    private CompletableFuture<String> renderLaTeX(String expression) {
        // Wrap the expression with the current LaTeX template
        expression = LATEX_TEMPLATE.replace("#CONTENT#", expression);

        // Define the completable future to use during the process
        CompletableFuture<String> future = new CompletableFuture<>();

        // Define the request JSON values
        Map<String, Object> requestValues = Map.of(
                "code", expression,
                "format", "png",
                "quality", 100,
                "density", 400
        );

        // Build a HTTP request
        Request request = new Request.Builder()
                .url(RTEX_URL)
                .method("POST", RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(requestValues)))
                .build();

        // Perform the HTTP request
        httpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException exception) {
                        future.completeExceptionally(exception);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // Parse the response JSON values
                        String body = response.body().string();
                        Map<String, Object> responseValues = new Gson().fromJson(body, Map.class);

                        // Complete the future exceptionally if an error occurred
                        if ("error".equals(responseValues.get("status"))) {
                            future.completeExceptionally(new Exception((String) responseValues.get("description")));
                            return;
                        }

                        // Complete the future with the image URL
                        future.complete(RTEX_URL + "/" + responseValues.get("filename"));
                    }
                });

        // Return the future
        return future;
    }

}
