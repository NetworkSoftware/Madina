package pro.network.madina.about;

import android.graphics.drawable.Drawable;

/**
 * Created by navaneeth on 5/11/17.
 */

public class AboutUs {
    private String title;
    private String summary;
    private Drawable image;

    public AboutUs(String title, String summary, Drawable image) {
        this.title = title;
        this.summary = summary;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
