/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai.paranormalai;

/**
 *
 * @author antti
 */
public class Key4
{

    int originNodeID;
    int targetNodeID;
    int currentMaxTotalPrice;
    boolean isOnFreeSearoute;

    public Key4(int originNodeID, int targetNodeID, int currentMaxTotalPrice, boolean isOnFreeSearoute)
    {
        this.originNodeID = originNodeID;
        this.targetNodeID = targetNodeID;
        this.currentMaxTotalPrice = currentMaxTotalPrice;
        this.isOnFreeSearoute = isOnFreeSearoute;
    }

    @Override
    public int hashCode()
    {
//        int nodeSpaceInBits = 10;   // 0..1023.
//        int cashSpaceInBits = 13;   // 0..8192.
//        int booleanSpaceInBits = 1; // 0..1.
//        int hash;
//        hash = this.originNodeID;
//        hash = (hash << nodeSpaceInBits) + this.targetNodeID;
//        hash = (hash << cashSpaceInBits) + this.currentMaxTotalPrice;
//        hash = (hash << booleanSpaceInBits) + (this.isOnFreeSearoute ? 1 : 0);
        int hash = this.originNodeID;
        hash = 1009 * hash + this.targetNodeID;
        hash = 101 * hash + this.currentMaxTotalPrice;
        hash = 3 * hash + (this.isOnFreeSearoute ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Key4 other = (Key4) obj;
        if (this.originNodeID != other.originNodeID)
            return false;
        if (this.targetNodeID != other.targetNodeID)
            return false;
        if (this.currentMaxTotalPrice != other.currentMaxTotalPrice)
            return false;
        if (this.isOnFreeSearoute != other.isOnFreeSearoute)
            return false;
        return true;
    }

}
