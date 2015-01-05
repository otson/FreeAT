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
    private final int destination;

    public Key(int distance, int price, int destination)
    {
        this.distance = distance;
        this.price = price;
        this.destination = destination;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + this.distance;
        hash = 89 * hash + this.price;
        hash = 89 * hash + this.destination;
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
