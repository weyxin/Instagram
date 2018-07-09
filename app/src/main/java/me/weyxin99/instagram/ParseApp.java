package me.weyxin99.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.weyxin99.instagram.model.Post;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("weyxin")
                .clientKey("weyxin-ly")
                .server("http://weyxin-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
