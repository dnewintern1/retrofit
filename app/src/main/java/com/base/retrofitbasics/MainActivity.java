package com.base.retrofitbasics;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.insightsonindia.com/wp-json/wp/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();



        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();


                for (Post post : posts) {
                    String content = post.getContent().getRendered();
                    Document doc = Jsoup.parse(content);
                    Elements questionElements = doc.select("p");

                    for (Element questionElement : questionElements) {
                        String questionText = questionElement.text();
                        if (questionText.endsWith("?")) {
                            textViewResult.append(questionText + "\n");
                            Element nextElement = questionElement.nextElementSibling();
                            if (nextElement != null && nextElement.tagName().equals("ul")) {
                                Elements optionElements = nextElement.select("li");
                                for (Element optionElement : optionElements) {
                                    textViewResult.append("Option: " + optionElement.text() + "\n");
                                }
                            }
                        }
                    }

                    Elements solutionElements = doc.select(".wpProQuiz_correct, .wpProQuiz_incorrect");

                    for (Element solutionElement : solutionElements) {
                        String solutionText = solutionElement.text();
                        textViewResult.append("Solution: " + solutionText + "\n");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
            //...
        });



    }
}

///From the JSON snippet you’ve provided, it seems you want to parse the HTML content inside the rendered field of the content object