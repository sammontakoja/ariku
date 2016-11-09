package io.ariku.verification.api;

/**
 * @author Ari Aaltonen
 */
public interface UserVerificationStore {

    void createUserVerification(String userId);

    UserVerification readUserVerification(String userId);

    void deleteUserVerification(String userId);

    void updateUserVerification(String userId, UserVerification userVerification);

}
