package org.p2p.solanaj.programs;

import java.util.ArrayList;
import java.util.List;

import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.TransactionInstruction;
import org.p2p.solanaj.core.AccountMeta;

import static org.bitcoinj.core.Utils.*;
import static org.p2p.solanaj.utils.ByteUtils.uint64ToByteArrayLE;

public class SystemProgram {
    public static final PublicKey PROGRAM_ID = new PublicKey("11111111111111111111111111111111");

    public static final int PROGRAM_INDEX_CREATE_ACCOUNT = 0;
    public static final int ASSIGN = 1;
    public static final int PROGRAM_INDEX_TRANSFER = 2;
    public static final int ADVANCE_NONCE_ACCOUNT = 4;
    public static final int INITIALIZE_NONCE_ACCOUNT = 6;
    public static final PublicKey SYSVAR_RECENT_BLOCKHASHES_PUBKEY = new PublicKey("SysvarRecentB1ockHashes11111111111111111111");
    public static final PublicKey SYSVAR_RENT_PUBKEY = new PublicKey("SysvarRent111111111111111111111111111111111");

    public static final Long SPACE = 50L;

    public static TransactionInstruction transfer(PublicKey fromPublicKey, PublicKey toPublickKey, long lamports) {
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(fromPublicKey, true, true));
        keys.add(new AccountMeta(toPublickKey, false, true));

        // 4 byte instruction index + 8 bytes lamports
        byte[] data = new byte[4 + 8];
        uint32ToByteArrayLE(PROGRAM_INDEX_TRANSFER, data, 0);
        uint64ToByteArrayLE(lamports, data, 4);

        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction transfer(String fromPublicKey, String toPublickKey, long lamports) {
        return transfer(new PublicKey(fromPublicKey), new PublicKey(toPublickKey), lamports);
    }

    public static TransactionInstruction createAccount(PublicKey fromPublicKey, PublicKey newAccountPublikkey,
                                                       long lamports, long space, PublicKey programId) {
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(fromPublicKey, true, true));
        keys.add(new AccountMeta(newAccountPublikkey, true, true));

        byte[] data = new byte[4 + 8 + 8 + 32];
        uint32ToByteArrayLE(PROGRAM_INDEX_CREATE_ACCOUNT, data, 0);
        uint64ToByteArrayLE(lamports, data, 4);
        uint64ToByteArrayLE(space, data, 12);
        System.arraycopy(programId.toByteArray(), 0, data, 20, 32);

        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction createAccount(String fromPublicKey, String newAccountPublikkey,
                                                       long lamports, long space, PublicKey programId) {
        return createAccount(new PublicKey(fromPublicKey), new PublicKey(newAccountPublikkey), lamports, space, programId);
    }

    public static TransactionInstruction nonceInitialize(PublicKey noncePubkey, PublicKey authorizedPubkey) {
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(noncePubkey, false, true));
        keys.add(new AccountMeta(SYSVAR_RECENT_BLOCKHASHES_PUBKEY, false, false));
        keys.add(new AccountMeta(SYSVAR_RENT_PUBKEY, false, false));

        byte[] data = new byte[4 + 32];
        uint32ToByteArrayLE(INITIALIZE_NONCE_ACCOUNT, data, 0);
        System.arraycopy(authorizedPubkey.toByteArray(), 0, data, 4, 32);
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction nonceInitialize(String noncePubkey, String authorizedPubkey) {
        return nonceInitialize(new PublicKey(noncePubkey), new PublicKey(authorizedPubkey));
    }


    private static TransactionInstruction nonceAdvance(PublicKey nonceAccountPublicKey, PublicKey authorizedPubkey) {
        List<AccountMeta> keys = new ArrayList<>();
        byte[] data = new byte[4];
        uint32ToByteArrayLE(ADVANCE_NONCE_ACCOUNT, data, 0);
        keys.add(new AccountMeta(nonceAccountPublicKey, false, true));
        keys.add(new AccountMeta(SYSVAR_RECENT_BLOCKHASHES_PUBKEY, false, false));
        keys.add(new AccountMeta(authorizedPubkey, true, false));
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction nonceAdvance(String nonceAccountPublicKey, String authorizedPubkey) {
        return nonceAdvance(new PublicKey(nonceAccountPublicKey), new PublicKey(authorizedPubkey));
    }

    public static TransactionInstruction assign(PublicKey accountPubkey, PublicKey programId) {
        List<AccountMeta> keys = new ArrayList<>();
        byte[] data = new byte[36];
        uint32ToByteArrayLE(ASSIGN, data, 0);
        System.arraycopy(programId.toByteArray(), 0, data, 4, 32);
        keys.add(new AccountMeta(accountPubkey, true, true));
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

}
