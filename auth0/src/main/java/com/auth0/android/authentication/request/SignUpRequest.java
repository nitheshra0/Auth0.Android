/*
 * SignUpRequest.java
 *
 * Copyright (c) 2015 Auth0 (http://auth0.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.auth0.android.authentication.request;

import com.auth0.android.Auth0Exception;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.ParameterBuilder;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.request.AuthenticationRequest;
import com.auth0.android.request.Request;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.DatabaseUser;

import java.util.Map;

/**
 * Represent a request that creates a user in a Auth0 Database connection and then logs in.
 */
public class SignUpRequest implements Request<Credentials, AuthenticationException>, AuthenticationRequest {

    private final DatabaseConnectionRequest<DatabaseUser, AuthenticationException> signUpRequest;
    private final AuthenticationRequest authenticationRequest;

    public SignUpRequest(DatabaseConnectionRequest<DatabaseUser, AuthenticationException> signUpRequest, AuthenticationRequest authenticationRequest) {
        this.signUpRequest = signUpRequest;
        this.authenticationRequest = authenticationRequest;
    }

    /**
     * Add additional parameters sent when creating a user.
     *
     * @param parameters sent with the request and must be non-null
     * @see ParameterBuilder
     * @return itself
     */
    public SignUpRequest addSignUpParameters(Map<String, Object> parameters) {
        signUpRequest.addParameters(parameters);
        return this;
    }

    /**
     * Add additional parameters sent when logging the user in
     *
     * @param parameters sent with the request and must be non-null
     * @return itself
     * @see ParameterBuilder
     */
    @Override
    public SignUpRequest addAuthenticationParameters(Map<String, Object> parameters) {
        authenticationRequest.addAuthenticationParameters(parameters);
        return this;
    }

    @Override
    public SignUpRequest setScope(String scope) {
        authenticationRequest.setScope(scope);
        return this;
    }

    @Override
    public SignUpRequest setDevice(String device) {
        authenticationRequest.setDevice(device);
        return this;
    }

    @Override
    public SignUpRequest setAudience(String audience) {
        authenticationRequest.setAudience(audience);
        return this;
    }

    @Override
    public SignUpRequest setAccessToken(String accessToken) {
        authenticationRequest.setAccessToken(accessToken);
        return this;
    }

    @Override
    public SignUpRequest setGrantType(String grantType) {
        authenticationRequest.setGrantType(grantType);
        return this;
    }

    @Override
    public SignUpRequest setConnection(String connection) {
        signUpRequest.setConnection(connection);
        authenticationRequest.setConnection(connection);
        return this;
    }

    @Override
    public SignUpRequest setRealm(String realm) {
        signUpRequest.setConnection(realm);
        authenticationRequest.setRealm(realm);
        return this;
    }

    /**
     * Starts to execute create user request and then logs the user in.
     *
     * @param callback called on either success or failure.
     */
    @Override
    public void start(final BaseCallback<Credentials, AuthenticationException> callback) {
        signUpRequest.start(new BaseCallback<DatabaseUser, AuthenticationException>() {
            @Override
            public void onSuccess(final DatabaseUser user) {
                authenticationRequest.start(callback);
            }

            @Override
            public void onFailure(AuthenticationException error) {
                callback.onFailure(error);
            }
        });
    }

    /**
     * Execute the create user request and then logs the user in.
     *
     * @return authentication object on success
     * @throws Auth0Exception on failure
     */
    @Override
    public Credentials execute() throws Auth0Exception {
        signUpRequest.execute();
        return authenticationRequest.execute();
    }
}
