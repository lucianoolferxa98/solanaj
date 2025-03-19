package org.p2p.solanaj.programs;

import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.TransactionInstruction;

import java.util.Collections;

import static org.p2p.solanaj.utils.ByteUtils.*;


/**
 * @Author melody
 * @Date 2024/3/19 13:41
 * @Version 1.0
 */
public class ComputeBudgetProgram {
    private static final PublicKey PROGRAM_ID = new PublicKey("ComputeBudget111111111111111111111111111111");
    private static final Integer SET_COMPUTE_UNIT_Limit_INDEX = 2;
    private static final Integer SET_COMPUTE_UNIT_PRICE_INDEX = 3;

    public static TransactionInstruction setComputeUnitPrice(long microLamports){
        byte[] data = new byte[1 + 8];
        uint8ToByteArrayLE(SET_COMPUTE_UNIT_PRICE_INDEX, data, 0);
        uint64ToByteArrayLE(microLamports, data, 1);
        return new TransactionInstruction(PROGRAM_ID, Collections.emptyList(), data);
    }

    public static TransactionInstruction setComputeUnitLimit(long limit){
        byte[] data = new byte[1 + 4];
        uint8ToByteArrayLE(SET_COMPUTE_UNIT_Limit_INDEX, data, 0);
        uint32ToByteArrayLE(limit, data, 1);
        return new TransactionInstruction(PROGRAM_ID, Collections.emptyList(), data);
    }
}
