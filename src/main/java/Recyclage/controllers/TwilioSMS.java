package Recyclage.controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioSMS {
    // Remplacez ces valeurs par votre Account SID et Auth Token Twilio
    public static final String ACCOUNT_SID = "AC464e362af2d71aa04eb3ff9e9dec4e33";
    public static final String AUTH_TOKEN = "31dd5958fb26f83ddfe98a76d514cecf";
    public static final String TWILIO_NUMBER = "+12176276204";

    // Initialiser Twilio avec votre Account SID et Auth Token
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // Méthode pour envoyer un SMS
    public static void sendSMS(String recipient, String messageBody) {
        try {
            Message message = Message.creator(
                            new PhoneNumber(recipient), // Numéro du destinataire
                            new PhoneNumber(TWILIO_NUMBER), // Votre numéro Twilio
                            messageBody)
                    .create();

            // Afficher l'ID du message si l'envoi est réussi
            System.out.println("Message SID: " + message.getSid());
        } catch (Exception ex) {
            // Gérer les erreurs d'envoi de SMS
            System.err.println("Erreur lors de l'envoi du SMS: " + ex.getMessage());
        }
    }
}
