/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

/**
 *
 * @author otso
 */
public class Key
{
    private final int distance;
    private final int price;

    public Key(int distance, int price)
    {
        this.distance = distance;
        this.price = price;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + this.distance;
        hash = 89 * hash + this.price;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Key other = (Key) obj;
        return true;
    }

}
