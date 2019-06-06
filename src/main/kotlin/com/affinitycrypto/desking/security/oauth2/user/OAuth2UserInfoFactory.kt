package com.affinitycrypto.desking.security.oauth2.user

import com.affinitycrypto.desking.exception.OAuth2AuthenticationProcessingException
import com.affinitycrypto.desking.model.AuthProvider


class OAuth2UserInfoFactory {
    companion object {
        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
            return when {
                registrationId.equals(AuthProvider.google.toString(), true) -> GoogleOAuth2UserInfo(attributes)
                registrationId.equals(AuthProvider.github.toString(), true) -> GithubOAuth2UserInfo(attributes)
                else -> throw OAuth2AuthenticationProcessingException("Sorry! Login with $registrationId is not supported yet.")
            }
        }
    }
}