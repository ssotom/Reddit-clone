package ssotom.clone.reddit.demo.model;

public enum VoteType {
    UPVOTE(1),
    DOWNVOTE(-1);

    VoteType(int direction) { }

    public VoteType getInverse() {
        if (ordinal() == 0) {
            return VoteType.DOWNVOTE;
        } else {
            return  VoteType.UPVOTE;
        }
    }

}
