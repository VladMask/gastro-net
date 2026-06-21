package grsu.by.security;

import lombok.Getter;

@Getter
public class ProfilePrincipal {
    private final Long profileId;
    private final String email;

    public ProfilePrincipal(Long profileId, String email) {
        this.profileId = profileId;
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }
}
