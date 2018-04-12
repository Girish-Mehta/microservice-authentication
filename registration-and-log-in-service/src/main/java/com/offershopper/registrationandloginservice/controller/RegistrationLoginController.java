/********************************************************************
* Filename: convert_temperature.c
* Original Author: Jane Doe
* File Creation Date: August 20, 2004
* Description: Contains routines for converting temperatures from Celsius to Fahrenheit.
********************************************************************/

package com.offershopper.registrationandloginservice.controller;

import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.offershopper.registrationandloginservice.bean.LoginInfoBean;
import com.offershopper.registrationandloginservice.bean.RegisterInfoBean;
import com.offershopper.registrationandloginservice.repository.LoginProxyRepository;
import com.offershopper.registrationandloginservice.repository.RegisterProxyRepository;


public class RegistrationLoginController {
	private static final String subject = "OfferShopper:Account Verification";

	@Autowired
	private LoginProxyRepository loginproxy;

	@Value("verificationUrl")
	private String verificationUrl;

	@Autowired
	private RegisterProxyRepository registerproxy;

	@HystrixCommand(fallbackMethod="verifyUserFallback")
	@PostMapping("/login")
	public String verifyUser(@RequestBody LoginInfoBean obj) {
		Optional<LoginInfoBean> logininfo = loginproxy.findById(obj.getUserId());
		// if user not present return Unauthorized
		if (!logininfo.isPresent()) {
			return "Unauthorized";
		}
		LoginInfoBean loginobj = logininfo.get();
		// if password not match return Unauthorized
		if (!loginobj.getPassword().equals(obj.getPassword())) {
			return "Unauthorized";
		}
		// return userid and role if userid and password match
		return loginobj.getUserId() + "," + loginobj.getRole();
	}

	
	public String verifyUserFallback(@RequestBody LoginInfoBean obj) {
		//TODO
		return "asd";
	}
	
	// registration for new user
	@PostMapping("/registration")
	@HystrixCommand(fallbackMethod="newUserFallback")
	public String newUser(@RequestBody RegisterInfoBean obj) {
		String CompactSerialization = null;
		Optional<RegisterInfoBean> registerinfo = registerproxy.findById(obj.getUserId());
		// if user already present return already exists user
		if (registerinfo.isPresent()) {
			return "Already Exists";
		}
		// save the credential for user and return role
		registerproxy.save(obj);

		// generate token and send email to user with the verification link
		// The shared secret or shared symmetric key represented as a octet sequence
		// JSON Web Key (JWK)
		String userId = obj.getUserId();
		String jwkJson = "{\"kty\":\"oct\",\"k\":\"Fdh9u8rINxfivbrianbbVT1u232VQBZYKx1HGAGPt2I\"}";
		JsonWebKey jwk = null;
		try {
			jwk = JsonWebKey.Factory.newJwk(jwkJson);
		} catch (JoseException e) {
			return "Unable to send verification email";
		}

		// Create a new Json Web Encryption object
		JsonWebEncryption senderJwe = new JsonWebEncryption();

		// The plaintext of the JWE is the message that we want to encrypt.
		senderJwe.setPlaintext(userId);

		// Set the "alg" header, which indicates the key management mode for this JWE.
		// In this example we are using the direct key management mode, which means
		// the given key will be used directly as the content encryption key.
		senderJwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);

		// Set the "enc" header, which indicates the content encryption algorithm to be
		// used.
		// This example is using AES_128_CBC_HMAC_SHA_256 which is a composition of AES
		// CBC
		// and HMAC SHA2 that provides authenticated encryption.
		senderJwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);

		// Set the key on the JWE. In this case, using direct mode, the key will used
		// directly as
		// the content encryption key. AES_128_CBC_HMAC_SHA_256, which is being used to
		// encrypt the
		// content requires a 256 bit key.
		senderJwe.setKey(jwk.getKey());

		// Produce the JWE compact serialization, which is where the actual encryption
		// is done.
		// The JWE compact serialization consists of five base64url encoded parts
		// combined with a dot ('.') character in the general format of
		// <header>.<encrypted key>.<initialization vector>.<ciphertext>.<authentication
		// tag>
		// Direct encryption doesn't use an encrypted key so that field will be an empty
		// string
		// in this case.
		try {
			CompactSerialization = senderJwe.getCompactSerialization();
		} catch (JoseException e) {
			return "Already Exists";
		}

		// send link to email
		sendTextMail(CompactSerialization, userId);

		return obj.getUserId() + "," + obj.getRole();
	}

	public String newUserFallback(@RequestBody RegisterInfoBean obj) {
		//TODO
		return "asd";
	}

	
	public void sendTextMail(String content, String userId) throws MessagingException {
		// Recipient's email ID needs to be mentioned.
		String to = userId;

		// Sender's email ID needs to be mentioned
		String from = "offershopper@gmail.com";

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			try {
				// Set From: header field of the header.
				message.setFrom(new InternetAddress(from));
				// Set To: header field of the header.
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

				// Set Subject: header field
				message.setSubject(RegistrationLoginController.subject);

				// Now set the actual message
				message.setText(content);

				// Send message
				Transport.send(message);

			} catch (javax.mail.MessagingException e) {
				e.printStackTrace();
			}

			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
