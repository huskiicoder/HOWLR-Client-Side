package edu.uw.tcss450.howlr.ui.messages.chats;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Encapsulate chat message details.
 */
public final class ChatMessage implements Serializable {

    /** The message ID. */
    private final int mMessageId;

    /** The message content. */
    private final String mMessage;

    /** The message sender. */
    private final String mSender;

    /** The message timestamp. */
    private final String mTimeStamp;

    /** The message sender email. */
    private final String mEmail;

    /**
     * Creates the chat message.
     * @param messageId The new message ID.
     * @param message The new message content.
     * @param sender The new message sender.
     * @param timeStamp The new message timestamp.
     * @param email The new message sender email.
     */
    public ChatMessage(int messageId, String message, String sender, String timeStamp, String email) {
        mMessageId = messageId;
        mMessage = message;
        mSender = sender;
        mTimeStamp = timeStamp;
        mEmail = email;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static ChatMessage createFromJsonString(final String cmAsJson, String firstName,
                                                   String lastName) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);

        return new ChatMessage(msg.getInt("messageid"),
                msg.getString("message"),
                firstName +
                " " + lastName,
                msg.getString("timestamp"),
                msg.getString("email"));
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSender() {
        return mSender;
    }

    public String getEmail() { return mEmail; }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public int getMessageId() {
        return mMessageId;
    }

    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatMessage) {
            result = mMessageId == ((ChatMessage) other).mMessageId;
        }
        return result;
    }
}
