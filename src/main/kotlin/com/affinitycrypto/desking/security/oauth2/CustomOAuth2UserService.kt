package com.affinitycrypto.desking.security.oauth2

import com.affinitycrypto.desking.exception.OAuth2AuthenticationProcessingException
import com.affinitycrypto.desking.model.AuthProvider
import com.affinitycrypto.desking.model.User
import com.affinitycrypto.desking.repository.UserRepository
import com.affinitycrypto.desking.security.UserPrincipal
import com.affinitycrypto.desking.security.oauth2.user.OAuth2UserInfo
import com.affinitycrypto.desking.security.oauth2.user.OAuth2UserInfoFactory
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import javax.security.sasl.AuthenticationException


@Service
class CustomOAuth2UserService(
        val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }

    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.clientRegistration.registrationId, oAuth2User.attributes)
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }

        val userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail())
        var user: User
        if (userOptional.isPresent) {
            user = userOptional.get()
            if (!user.authProvider.equals(AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId))) {
                throw OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.authProvider + " account. Please use your " + user.authProvider +
                        " account to login.")
            }
            user = updateExistingUser(user, oAuth2UserInfo)
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
        }

        return UserPrincipal.create(user, oAuth2User.attributes)
    }

    private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): User {
        val user = User(
                authProvider = AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId),
                id = oAuth2UserInfo.getId().toLong(),
                name = oAuth2UserInfo.getName(),
                email = oAuth2UserInfo.getEmail(),
                imageUrl = oAuth2UserInfo.getImageUrl())
        return userRepository.save(user)
    }

    private fun updateExistingUser(existingUser: User, oAuth2UserInfo: OAuth2UserInfo): User {
        val newUser = existingUser.copy(
                name = oAuth2UserInfo.getName(),
                imageUrl = oAuth2UserInfo.getImageUrl())
        return userRepository.save(newUser)
    }

}