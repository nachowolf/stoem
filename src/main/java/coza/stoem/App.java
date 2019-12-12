package coza.stoem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import coza.stoem.services.TransactionService;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;


public class App{
    //set static resources directory
    static{
        staticFiles.location("/public");
    }
    public static void main(String[] arguments){
        TransactionService transactionService = new TransactionService();


        get("/get-stats", (request,response) -> {
            List<Transaction> ts = transactionService.getTransactions();
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(ts, new TypeToken<List<Transaction>>() {}.getType());

            JsonArray jsonArray = element.getAsJsonArray();
            return jsonArray;
        });

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new HandlebarsTemplateEngine().render(
                    new ModelAndView(model, "index.handlebars"));
        });

        post("/capture-form",(request,response) -> {

            final HashMap<String, String> queryParams = new HashMap<>();
                request.queryMap().toMap().forEach((k, v) -> {
                queryParams.put(k, v[0]);
            });

            Transaction transaction = new Transaction(queryParams);
                transactionService.capture(transaction);
                return "succsess";
                //end of capture
        });
    }
}