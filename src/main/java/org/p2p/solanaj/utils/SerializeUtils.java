package org.p2p.solanaj.utils;


import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.util.List;

import static org.p2p.solanaj.core.Transaction.SIGNATURE_LENGTH;

/**
 * @author dongyuhao
 */
public class SerializeUtils {
    public static byte[] serialize(byte[] serializedMessage,List<byte[]> signatures) {
        int signaturesSize = signatures.size();
        byte[] signaturesLength = ShortvecEncoding.encodeLength(signaturesSize);

        ByteBuffer out = ByteBuffer
                .allocate(signaturesLength.length + signaturesSize * SIGNATURE_LENGTH + serializedMessage.length);

        out.put(signaturesLength);

        for (byte[] signature : signatures) {
            out.put(signature);
        }

        out.put(serializedMessage);

        return out.array();
    }


    public static byte[] serialize(byte[] serializedMessage, byte[]... signatures) {
        int signaturesSize = signatures.length;
        byte[] signaturesLength = ShortvecEncoding.encodeLength(signaturesSize);

        ByteBuffer out = ByteBuffer
                .allocate(signaturesLength.length + signaturesSize * SIGNATURE_LENGTH + serializedMessage.length);

        out.put(signaturesLength);

        for (byte[] signature : signatures) {
            out.put(signature);
        }

        out.put(serializedMessage);

        return out.array();
    }
}
