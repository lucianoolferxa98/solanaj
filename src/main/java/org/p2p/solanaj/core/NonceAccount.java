package org.p2p.solanaj.core;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Utils;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

/**
 * @author melody
 * @version NonceAccount.java, v 0.1 2022年07月20日
 */
public class NonceAccount extends Account {
    public static Long NONCE_ACCOUNT_LENGTH = 80L;
    private PublicKey authorizedPubkey;
    private String blockHash;
    private BigDecimal feeCalculator;

    public static NonceAccount fromAccountData(List<String> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        if (data.size() != 2) {
            return null;
        }
        if (!data.get(1).equals("base64")) {
            return null;
        }
        NonceAccount nonceAccount = new NonceAccount();
        byte[] decode = Base64.getDecoder().decode(data.get(0));
        byte[] authorityBytes = new byte[32];
        System.arraycopy(decode, 8, authorityBytes, 0, 32);
        nonceAccount.setAuthorizedPubkey(new PublicKey(Base58.encode(authorityBytes)));

        byte[] blockHashBytes = new byte[32];
        System.arraycopy(decode, 40, blockHashBytes, 0, 32);
        nonceAccount.setBlockHash(Base58.encode(blockHashBytes));

        byte[] lamportBytes = new byte[8];
        System.arraycopy(decode, 72, lamportBytes, 0, 8);
        nonceAccount.setFeeCalculator(new BigDecimal(Utils.readInt64(lamportBytes, 0)));
        return nonceAccount;
    }

    @Override
    public String toString() {
        return "NonceAccount{" +
                "authorizedPubkey='" + authorizedPubkey + '\'' +
                ", nonce='" + blockHash + '\'' +
                ", feeCalculator=" + feeCalculator +
                '}';
    }

    public PublicKey getAuthorizedPubkey() {
        return authorizedPubkey;
    }

    public void setAuthorizedPubkey(PublicKey authorizedPubkey) {
        this.authorizedPubkey = authorizedPubkey;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public BigDecimal getFeeCalculator() {
        return feeCalculator;
    }

    public void setFeeCalculator(BigDecimal feeCalculator) {
        this.feeCalculator = feeCalculator;
    }
}
