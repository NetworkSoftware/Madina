package pro.tryme.network;

import java.io.Serializable;

public class FeedbackMainbean implements Serializable {
    String id;
    String feedback;

    public FeedbackMainbean() {
    }

    public FeedbackMainbean(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}