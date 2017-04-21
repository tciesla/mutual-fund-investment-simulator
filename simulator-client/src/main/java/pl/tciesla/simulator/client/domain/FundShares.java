package pl.tciesla.simulator.client.domain;

import pl.tciesla.simulator.client.constant.ShareType;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "fund_shares")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"fundId", "type"})
public class FundShares {

    @XmlElement(required = true)
    private Long fundId;
    @XmlElement(required = true)
    private ShareType type;


    public FundShares() {}

    public long getFundId() {
        return fundId;
    }

    public void setFundId(long fundId) {
        this.fundId = fundId;
    }

    public ShareType getType() {
        return type;
    }

    public void setType(ShareType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FundShare{" + fundId + ", " + type + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        FundShares otherFundShares = (FundShares) o;
        if (fundId != otherFundShares.fundId) return false;
        if (type != otherFundShares.type) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (fundId ^ (fundId >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
