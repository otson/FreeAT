/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai.normalaiNode;

/**
 *
 * @author otso
 */
public class Key
{

    private int from;
    private int to;

    public Key(int from, int to)
    {
        if (to > from)
        {
            this.to = from;
            this.from = to;
        } else
        {
            this.from = from;
            this.to = to;
        }
    }

    @Override
    public int hashCode()
    {

        int hash = 486187739;
        hash = 92821 * hash + this.from;
        hash = 92821 * hash + this.to;
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
