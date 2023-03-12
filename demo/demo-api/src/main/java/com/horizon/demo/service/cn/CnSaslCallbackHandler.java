package com.horizon.demo.service.cn;

import javax.security.auth.callback.*;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;

public class CnSaslCallbackHandler implements CallbackHandler {

    public static final String PRINCIPAL = "thrift-test-principal";
    public static final String REALM = "thrift-test-realm";

    private final String password;

    public CnSaslCallbackHandler(String password) {
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
        for (Callback c : callbacks) {
            if (c instanceof NameCallback) {
                ((NameCallback) c).setName(PRINCIPAL);
            } else if (c instanceof PasswordCallback) {
                ((PasswordCallback) c).setPassword(password.toCharArray());
            } else if (c instanceof AuthorizeCallback) {
                ((AuthorizeCallback) c).setAuthorized(true);
            } else if (c instanceof RealmCallback) {
                ((RealmCallback) c).setText(REALM);
            } else {
                throw new UnsupportedCallbackException(c);
            }
        }
    }
}