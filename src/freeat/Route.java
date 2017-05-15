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
public class Route
{

    private Node destination;
    private int price;

    Route(Node destination, int price)
    {
        this.destination = destination;
        this.price = price;
    }

    public Node getDestination()
    {
        return destination;
    }

    public int getPrice()
    {
        return price;
    }
    
    public String getString(){
        return "Destination: "+destination.getName()+" price: "+price;
    }

}
