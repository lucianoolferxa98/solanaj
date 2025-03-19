package org.p2p.solanaj.programs;

import org.p2p.solanaj.core.AccountMeta;
import org.p2p.solanaj.core.Lockup;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.TransactionInstruction;

import java.util.ArrayList;
import java.util.List;

import static org.p2p.solanaj.utils.ByteUtils.uint32ToByteArrayLE;
import static org.p2p.solanaj.utils.ByteUtils.uint64ToByteArrayLE;


/**
 * @author dongyuhao
 */
public class StakeProgram {
    public static final Long SPACE = 200L;
    private static final Integer INITIALIZE = 0;
    private static final Integer DELEGATE = 2;
    private static final Integer SPLIT = 3;
    private static final Integer WITHDRAW = 4;
    private static final Integer DEACTIVATE = 5;
    private static final Integer MERGE = 7;

    public static final PublicKey PROGRAM_ID = new PublicKey("Stake11111111111111111111111111111111111111");
    public static final PublicKey SYSVAR_CLOCK_PUBKEY = new PublicKey("SysvarC1ock11111111111111111111111111111111");
    public static final PublicKey SYSVAR_STAKE_HISTORY_PUBKEY = new PublicKey("SysvarStakeHistory1111111111111111111111111");
    public static final PublicKey STAKE_CONFIG_ID = new PublicKey("StakeConfig11111111111111111111111111111111");


    private static TransactionInstruction initialize(PublicKey stakePubkey, PublicKey staker, PublicKey withdrawer, Lockup lockup) {
        byte[] data = new byte[116];
        uint32ToByteArrayLE(INITIALIZE, data, 0);
        System.arraycopy(staker.toByteArray(), 0, data, 4, 32);
        System.arraycopy(withdrawer.toByteArray(), 0, data, 36, 32);
        uint64ToByteArrayLE(lockup.getUnixTimestamp(), data, 68);
        uint64ToByteArrayLE(lockup.getEpoch(), data, 76);
        System.arraycopy(lockup.getCustodian().toByteArray(), 0, data, 84, 32);

        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(stakePubkey, false, true));
        keys.add(new AccountMeta(SystemProgram.SYSVAR_RENT_PUBKEY, false, false));
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction initialize(String stakePubkey, String staker, String withdrawer, Lockup lockup) {
        return initialize(new PublicKey(stakePubkey), new PublicKey(staker), new PublicKey(withdrawer), lockup);
    }

    private static TransactionInstruction delegate(PublicKey stakePubkey, PublicKey authorizedPubkey, PublicKey votePubkey) {
        byte[] data = new byte[4];
        uint32ToByteArrayLE(DELEGATE, data, 0);
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(stakePubkey, false, true));
        keys.add(new AccountMeta(votePubkey, false, false));
        keys.add(new AccountMeta(SYSVAR_CLOCK_PUBKEY, false, false));
        keys.add(new AccountMeta(SYSVAR_STAKE_HISTORY_PUBKEY, false, false));
        keys.add(new AccountMeta(STAKE_CONFIG_ID, false, false));
        keys.add(new AccountMeta(authorizedPubkey, true, false));
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction delegate(String stakePubkey, String authorizedPubkey, String votePubkey) {
        return delegate(new PublicKey(stakePubkey), new PublicKey(authorizedPubkey), new PublicKey(votePubkey));
    }

    private static TransactionInstruction deactivate(PublicKey stakePubkey, PublicKey authorizedPubkey) {
        byte[] data = new byte[4];
        uint32ToByteArrayLE(DEACTIVATE, data, 0);
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(stakePubkey, false, true));
        keys.add(new AccountMeta(SYSVAR_CLOCK_PUBKEY, false, false));
        keys.add(new AccountMeta(authorizedPubkey, true, false));
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction deactivate(String stakePubkey, String authorizedPubkey) {
        return deactivate(new PublicKey(stakePubkey), new PublicKey(authorizedPubkey));
    }

    private static TransactionInstruction withdraw(PublicKey stakePubkey, PublicKey authorizedPubkey, PublicKey to, Long lamports) {
        byte[] data = new byte[4 + 8];
        uint32ToByteArrayLE(WITHDRAW, data, 0);
        uint64ToByteArrayLE(lamports, data, 4);
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(stakePubkey, false, true));
        keys.add(new AccountMeta(to, false, true));
        keys.add(new AccountMeta(SYSVAR_CLOCK_PUBKEY, false, false));
        keys.add(new AccountMeta(SYSVAR_STAKE_HISTORY_PUBKEY, false, false));
        keys.add(new AccountMeta(authorizedPubkey, true, false));
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction withdraw(String stakePubkey, String authorizedPubkey, String to, Long lamports) {
        return withdraw(new PublicKey(stakePubkey), new PublicKey(authorizedPubkey), new PublicKey(to), lamports);
    }

    private static TransactionInstruction split(PublicKey stakePubkey, PublicKey authorizedPubkey, PublicKey splitStakePubkey, Long lamports) {
        byte[] data = new byte[4 + 8];
        uint32ToByteArrayLE(SPLIT, data, 0);
        uint64ToByteArrayLE(lamports, data, 4);
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(stakePubkey, false, true));
        keys.add(new AccountMeta(splitStakePubkey, false, true));
        keys.add(new AccountMeta(authorizedPubkey, true, false));
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction split(String stakePubkey, String authorizedPubkey, String splitStakePubkey, Long lamports) {
        return split(new PublicKey(stakePubkey), new PublicKey(authorizedPubkey), new PublicKey(splitStakePubkey), lamports);
    }

    private static TransactionInstruction merge(PublicKey stakePubkey, PublicKey sourceStakePubKey, PublicKey authorizedPubkey){
        byte[] data = new byte[4];
        uint32ToByteArrayLE(MERGE, data, 0);
        List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(stakePubkey, false, true));
        keys.add(new AccountMeta(sourceStakePubKey, false, true));
        keys.add(new AccountMeta(SYSVAR_CLOCK_PUBKEY, false, false));
        keys.add(new AccountMeta(SYSVAR_STAKE_HISTORY_PUBKEY, false, false));
        keys.add(new AccountMeta(authorizedPubkey, true, false));
        return new TransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction merge(String stakePubkey, String sourceStakePubKey, String authorizedPubkey){
        return merge(new PublicKey(stakePubkey),new PublicKey(sourceStakePubKey),new PublicKey(authorizedPubkey));
    }
}
