package com.ttv.bean;

public  enum SwapType {

    // constants ------------------------------------------------------------------------------------------------------

    LOGIN((byte) 0x01),
    TEXT((byte) 0x02),
    FILE((byte) 0x03),
    OFFER_BUY((byte) 0x04),
    OFFER_SWAP((byte) 0x05),
    OFFER_BUY_AGREE((byte) 0x06),
    OFFER_BUY_DENY((byte) 0x07),
    OFFER_BUY_CANCEL((byte) 0x08),
    OFFER_SWAP_AGREE((byte) 0x09),
    OFFER_SWAP_DENY((byte) 0x0A),
    OFFER_SWAP_CANCEL((byte) 0x0B),
    TRANSACTION_CREATED((byte) 0x0C),
    TRANSACTION_CANCEL((byte) 0x0D),
    TRANSACTION_COMPELETE((byte) 0x0E),
    REPLY ((byte)0x5A),
    INFORM ((byte)0x62),
    PING ((byte)0x63),
    LOGOUT ((byte)0x64),
    
    // put last since it's the least likely one to be encountered in the fromByte() function
    UNKNOWN((byte) 0x00);

    // internal vars --------------------------------------------------------------------------------------------------

    private final byte b;

    // constructors ---------------------------------------------------------------------------------------------------

    private SwapType(byte b) {
        this.b = b;
    }

    // public static methods ------------------------------------------------------------------------------------------

    public static SwapType fromByte(byte b) {
        for (SwapType code : values()) {
            if (code.b == b) {
                return code;
            }
        }

        return UNKNOWN;
    }

    // getters & setters ----------------------------------------------------------------------------------------------

    public byte getByteValue() {
        return b;
    }
}