package io.netty.example.securechat;

public enum DecodingState {

    // constants --------------------------------------------------------------------------------------------------

    VERSION,
    TYPE,
    PAYLOAD_LENGTH,
    PAYLOAD,
}