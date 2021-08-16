package pro.network.madina;

public class Category {
    String title;
    String color;
    String subColor;
    int image;

    public Category(String title, String color, String subColor, int image) {
        this.title = title;
        this.color = color;
        this.subColor = subColor;
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSubColor() {
        return subColor;
    }

    public void setSubColor(String subColor) {
        this.subColor = subColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
