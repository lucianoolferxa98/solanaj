package org.p2p.solanaj.programs;

import org.p2p.solanaj.core.AccountMeta;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.TransactionInstruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.p2p.solanaj.utils.ByteUtils.uint64ToByteArrayLE;
import static org.p2p.solanaj.utils.ByteUtils.uint8ToByteArrayLE;


public class TokenProgram {
    public static PublicKey ASSOCIATED_TOKEN_PROGRAM_ID = new PublicKey("ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL");
    public static PublicKey TOKEN_PROGRAM_ID = new PublicKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA");
    public static PublicKey TOKEN_2022_PROGRAM_ID = new PublicKey("TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb");

    public static String getAssociatedTokenAddress(PublicKey programId, String mint, String owner) throws Exception {
        return getAssociatedTokenAddress(programId,new PublicKey(mint), new PublicKey(owner));
    }

    private static String getAssociatedTokenAddress(PublicKey programId,PublicKey mint, PublicKey owner) throws Exception {
        return PublicKey.findProgramAddress(Arrays.asList(owner.toByteArray(), programId.toByteArray(), mint.toByteArray()), ASSOCIATED_TOKEN_PROGRAM_ID).getAddress().toBase58();
    }

    private static TransactionInstruction createAssociatedTokenAccountInstruction(PublicKey prigramId, PublicKey mint, PublicKey associatedAccount, PublicKey owner, PublicKey payer) {
        byte[] data = new byte[0];
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(payer, true, true));
        keys.add(new AccountMeta(associatedAccount, false, true));
        keys.add(new AccountMeta(owner, false, false));
        keys.add(new AccountMeta(mint, false, false));
        keys.add(new AccountMeta(SystemProgram.PROGRAM_ID, false, false));
        keys.add(new AccountMeta(prigramId, false, false));
        keys.add(new AccountMeta(SystemProgram.SYSVAR_RENT_PUBKEY, false, false));
        return new TransactionInstruction(ASSOCIATED_TOKEN_PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction createAssociatedTokenAccountInstruction(PublicKey programId, String mint, String associatedAccount, String owner, String payer) {
        return createAssociatedTokenAccountInstruction(
                programId,
                new PublicKey(mint),
                new PublicKey(associatedAccount),
                new PublicKey(owner),
                new PublicKey(payer)
        );
    }


    private static TransactionInstruction createTransferCheckedInstruction(PublicKey programId,PublicKey source, PublicKey mint, PublicKey destination, PublicKey owner, List<String> multiSigners, Long amount, Integer decimals) {
        // 1 byte instruction index + 8 bytes amount + 1 bytes decimals
        byte[] data = new byte[1 + 8 + 1];
        uint8ToByteArrayLE(12, data, 0);
        uint64ToByteArrayLE(amount, data, 1);
        uint8ToByteArrayLE(decimals, data, 9);
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(source, false, true));
        keys.add(new AccountMeta(mint, false, false));
        keys.add(new AccountMeta(destination, false, true));
        if (multiSigners == null || multiSigners.size() == 0) {
            keys.add(new AccountMeta(owner, true, false));
        } else {
            keys.add(new AccountMeta(owner, false, false));
            for (String signer : multiSigners) {
                keys.add(new AccountMeta(new PublicKey(signer), true, false));
            }
        }
        return new TransactionInstruction(programId, keys, data);
    }

    public static TransactionInstruction createTransferCheckedInstruction(PublicKey programId,String source, String mint, String destination, String owner, List<String> multiSigners, Long amount, Integer decimals) {
        return createTransferCheckedInstruction(programId,new PublicKey(source), new PublicKey(mint), new PublicKey(destination), new PublicKey(owner), multiSigners, amount, decimals);
    }
}
