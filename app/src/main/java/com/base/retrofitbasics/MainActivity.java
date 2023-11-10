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
                    String content = post.getContent();

                    // Parse the content with Jsoup
                    Document doc = Jsoup.parse(content);

                    // Get all elements or select specific elements
                    Elements elements = doc.getAllElements();

                    // Define a limit for the number of elements to process
                    int limit = 10;

                    // Initialize a counter
                    int count = 0;

                    for (Element element : elements) {
                        // Do something with each element
                        String elementText = element.text();

                        // Append the text of each element to the TextView
                        textViewResult.append(elementText);

                        // Increment the counter
                        count++;

                        // Break the loop if the limit has been reached
                        if (count >= limit) {
                            break;
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}