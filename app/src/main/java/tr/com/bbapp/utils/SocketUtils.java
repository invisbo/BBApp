package tr.com.bbapp.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class SocketUtils {

    public static WebSocket createSocketConnection(String url, WebSocketListener webSocketListener) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.build();
        WebSocket ws = client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService().shutdown();
        return ws;
    }
}
