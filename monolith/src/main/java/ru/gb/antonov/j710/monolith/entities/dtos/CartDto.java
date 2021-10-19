package ru.gb.antonov.j710.monolith.entities.dtos;


import java.util.LinkedList;
import java.util.List;

public class CartDto
{
    private List<OrderItemDto> oitems;
    private int titlesCount; //< используется клиентом для проверки, пустая ли корзина.

    //следующ. 2 поля никогда не хранят актуальные значения! При попытке получить их значения, последние
    // вычисляются и возвращаются геттерами.
    private double cost;
    private int load;
//-----------------------------------------------------------------------------
    public CartDto () { oitems = new LinkedList<> (); }

    public static CartDto dummyCartDto (/*Метод готов переварить любые параметры.*/)
    {
        CartDto cdt = new CartDto();
        //cdt.oitems = ;
        cdt.titlesCount = 0;
        cdt.cost = 0.0;
        cdt.load = 0;
        //cdt. = ;
        return cdt;
    }
//--------------------- геттеры и сеттеры -------------------------------------
    public double getCost ()
    {
        double cost = 0.0;
        for (OrderItemDto oitem : oitems)
        {
            cost += oitem.getCost();
        }
        return cost;
    }
    public void setCost (double cost)    {    this.cost = cost;    }

    public int getTitlesCount () { return oitems.size(); }
    public void setTitlesCount (int titlesCount)    {    this.titlesCount = titlesCount;    }

    public List<OrderItemDto> getOitems ()    {    return oitems;    }
    public void setOitems (List<OrderItemDto> oitems)    {    this.oitems = oitems;    }

    public int getLoad ()    {    return load;    }
    public void setLoad (int load)    {    this.load = load;    }

    //-----------------------------------------------------------------------------
    public boolean addItem (OrderItemDto oitem, int quantity /* может быть 0 */)
    {
        boolean ok = false;
        if (oitem != null && quantity >= 0)
        {
            for (OrderItemDto oi : oitems)
            {
                if (oi.getProductId().equals (oitem.getProductId()))
                {

                    if (oi.changeQuantity (quantity))
                        /*recalcCost()*/;
                    ok = true;
                    break;
                }
            }
            if (!ok)
            {
                if (quantity > 0)
                    oitem.setQuantity (quantity);

                ok = oitems.add (oitem);
                load += quantity;
                cost += oitem.getCost();
            }
        }
        return ok;
    }

    public String toString()
    {   return String.format("CartDto:[cst:%.2f, tls:%d, ld:%d, ois:%s]",
                             cost, titlesCount, load, oitems);
    }
}
