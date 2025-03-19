package org.p2p.solanaj.core;


/**
 * @author dongyuhao
 */

public class Lockup {
    private Long unixTimestamp;

    private Long epoch;

    private PublicKey custodian;


    public Lockup(Long unixTimestamp, Long epoch, String custodian) {
        this.unixTimestamp = unixTimestamp;
        this.epoch = epoch;
        this.custodian = new PublicKey(custodian);
    }

    public Long getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setUnixTimestamp(Long unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }

    public Long getEpoch() {
        return epoch;
    }

    public void setEpoch(Long epoch) {
        this.epoch = epoch;
    }

    public PublicKey getCustodian() {
        return custodian;
    }

    public void setCustodian(PublicKey custodian) {
        this.custodian = custodian;
    }
}
