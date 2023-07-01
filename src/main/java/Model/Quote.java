package Model;

public class Quote extends Tweet{
    private String secondBody;

    public Quote(String body, int likes, int retweets, int comments, String secondBody) {
        super(body, likes, retweets, comments);
        this.secondBody = secondBody;
    }
}
