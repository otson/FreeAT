/* 
 * Copyright (C) 2017 Otso Nuortimo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
