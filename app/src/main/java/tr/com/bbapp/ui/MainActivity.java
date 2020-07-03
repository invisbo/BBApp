package tr.com.bbapp.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tr.com.bbapp.R;
import tr.com.bbapp.constants.Constants;
import tr.com.bbapp.databinding.ActivityMainBinding;
import tr.com.bbapp.network.Api;
import tr.com.bbapp.network.model.ListItem;
import tr.com.bbapp.network.response.ListResponse;
import tr.com.bbapp.ui.adapters.MainListAdapter;
import tr.com.bbapp.utils.SocketUtils;

import static tr.com.bbapp.constants.Constants.KEY_LOGIN;
import static tr.com.bbapp.constants.Constants.KEY_LOGOUT;
import static tr.com.bbapp.constants.Constants.SOCKET_URL;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainListAdapter adapter = new MainListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        WebSocket ws = SocketUtils.createSocketConnection(SOCKET_URL, new WebSocketListener() {
            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                super.onMessage(webSocket, text);
                runOnUiThread(() -> {
                    switch (text) {
                        case KEY_LOGIN:
                            binding.user.setVisibility(View.VISIBLE);
                            break;
                        case KEY_LOGOUT:
                            binding.user.setVisibility(View.GONE);
                            break;
                        default:
                            String[] values = text.split("-");
                            if (values.length >= 2) {
                                ListItem newItem = null;
                                try {
                                    newItem = new ListItem(Integer.parseInt(values[0]), values[1]);
                                } catch (NumberFormatException ignored) {
                                }
                                if (newItem != null) {
                                    adapter.updateItem(newItem);
                                }
                            }
                    }
                });

            }
        });

        binding.sendButton.setOnClickListener(view -> {
            ws.send(binding.messageText.getText().toString());
            binding.messageText.getText().clear();
        });

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        retrofit.create(Api.class).getList().enqueue(new Callback<ListResponse>() {
            @Override
            public void onResponse(@NotNull Call<ListResponse> call, @NotNull Response<ListResponse> response) {
                if (response.body() != null) {
                    adapter.setItems(response.body().getData());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ListResponse> call, @NotNull Throwable t) {
            }
        });
    }

}