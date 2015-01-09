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
public class Key3
{

    int originNodeID;
    int targetNodeID;
    int currentMaxTotalPrice;

    public Key3(int originNodeID, int targetNodeID, int currentMaxTotalPrice)
    {
        this.originNodeID = originNodeID;
        this.targetNodeID = targetNodeID;
        this.currentMaxTotalPrice = currentMaxTotalPrice;
    }

    @Override
    public int hashCode()
    {
        // 79.
        int hash = 7;
        hash = 1009 * hash + this.originNodeID;
        hash = 1009 * hash + this.targetNodeID;
        hash = 23 * hash + this.currentMaxTotalPrice;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Key3 other = (Key3) obj;
        if (this.originNodeID != other.originNodeID)
            return false;
        if (this.targetNodeID != other.targetNodeID)
            return false;
        if (this.currentMaxTotalPrice != other.currentMaxTotalPrice)
            return false;
        return true;
    }

}
