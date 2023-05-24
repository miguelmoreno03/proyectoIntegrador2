package com.dh.userservice.repository;

import com.dh.userservice.entities.AppUser;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class KeyCloakUserRepository implements IUserKeyCloakRepository {
    @Autowired
    private Keycloak keycloakClient;

    @Value("${dh.keycloak.realm}")
    private String realm;

    @Override
    public AppUser createUser(AppUser user) {
        UserRepresentation userRepresentation = toUserRepresentation(user);
        keycloakClient.realm(realm).users().create(userRepresentation);
        return user;
    }

    private UserRepresentation toUserRepresentation(AppUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getEmail());
        userRepresentation.setFirstName(user.getFirst_name());
        userRepresentation.setLastName(user.getLast_name());
        userRepresentation.setEmail(user.getEmail());

        List<String> requiredActions = new ArrayList<>();
        requiredActions.add("VERIFY_EMAIL");
        userRepresentation.setRequiredActions(requiredActions);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setEnabled(true);

        return userRepresentation;
    }
}
