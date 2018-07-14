package me.weyxin99.instagram.model;

import android.text.format.DateUtils;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject implements Serializable{

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_USER = "user";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_HEARTS = "hearts";
    public static final String KEY_ID = "objectId";
    public static final String KEY_COMMENT = "comments";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

    public String getID() { return getString(KEY_ID); }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public String getHearts() { return getString(KEY_HEARTS); }

    public void setHearts() { put(KEY_HEARTS, Integer.toString(0));}

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public List<String> getComments() { return getList(KEY_COMMENT); }

    public String getTime() {
        Date date = getCreatedAt();
        String dateString = dateFormat.format(date);
        return getRelativeTimeAgo(dateString);
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String instagramFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(instagramFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            Log.d("PostClass", "Got time stamp!");
        }
        catch (java.text.ParseException e) {
            Log.d("PostClass", "Failed to get time stamp.");
            e.printStackTrace();
        }
        return relativeDate;
    }

    public static class Query extends ParseQuery<Post>{
        public Query() {
            super(Post.class);
        }
        public Query getTop() {
            setLimit(20);
            return this;
        }
        public Query withUser() {
            include("user");
            include("comments");
            return this;
        }
    }
}
