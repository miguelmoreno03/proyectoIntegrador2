package com.dh.userservice.repository;

import com.dh.userservice.entities.AppUser;
import com.dh.userservice.entities.AppUserDTO;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.ws.rs.NotFoundException;
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
    public void createUser(AppUser user) {
        UserRepresentation userRepresentation = toUserRepresentation(user);
        keycloakClient.realm(realm).users().create(userRepresentation);
    }

    @Override
    public void patchUser(String username , AppUserDTO user) {
        UserRepresentation userRepresentation = keycloakClient.realm(realm).users().search(username)
                .stream()
                .findFirst()
                .orElseThrow(()->new NotFoundException("User Not Found in keycloak"));


        if (user.getEmail() != null) {

            userRepresentation.setEmail(user.getEmail());
        }
        if (user.getFirst_name() != null) {
            userRepresentation.setFirstName(user.getFirst_name());
        }
        if (user.getLast_name() != null) {
            userRepresentation.setLastName(user.getLast_name());
        }
        if (user.getPassword() != null) {
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(user.getPassword());
            credentialRepresentation.setTemporary(false);

            userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        }
        keycloakClient.realm(realm).users().get(userRepresentation.getId()).update(userRepresentation);

    }

    private UserRepresentation toUserRepresentation(AppUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
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
