package io.ariku.verification;

/**
 * @author Ari Aaltonen
 */
public class UserVerification {
    public String userId = "";
    public SecurityMessage securityMessage = new SecurityMessage();
    public boolean isSignedIn;
    public boolean isSignedInConfirmed;

    public UserVerification() {
    }

    public UserVerification(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserVerification{" +
                "userId='" + userId + '\'' +
                ", securityMessage=" + securityMessage +
                ", isSignedIn=" + isSignedIn +
                ", isSignedInConfirmed=" + isSignedInConfirmed +
                '}';
    }
}
