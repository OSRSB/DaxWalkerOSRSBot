package net.runelite.client.rsb.walker.dax_api.api_lib.models.exceptions;

import com.allatori.annotations.DoNotRename;

@DoNotRename
public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
}
