package org.p2p.solanaj.utils;


import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.util.List;

import static org.p2p.solanaj.core.Transaction.SIGNATURE_LENGTH;

/**
 * @author dongyuhao
 */
public class SerializeUtils {
    public static byte[] serialize(List<String> signatures, byte[] serializedMessage) {
        int signaturesSize = signatures.size();
        byte[] signaturesLength = ShortvecEncoding.encodeLength(signaturesSize);

        ByteBuffer out = ByteBuffer
                .allocate(signaturesLength.length + signaturesSize * SIGNATURE_LENGTH + serializedMessage.length);

        out.put(signaturesLength);

        for (String signature : signatures) {
            byte[] rawSignature = Base58.decode(signature);
            out.put(rawSignature);
        }

        out.put(serializedMessage);

        return out.array();
    }
}
