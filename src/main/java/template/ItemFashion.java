package template;

import java.util.List;
/**
 *
 * @author Truongbk
 */
public class ItemFashion {
    public static List<ItemFashion> ENTRYS;
    public final int price;
    public final byte ID;
    public final short idIcon;
    public final String name;
    public final String info;
    public final short[] mWearing;
    public final List<Option> op;

    public ItemFashion(byte ID, short IDIcon, String name, String info, short[] wear,
            List<Option> op, int price) {
        this.ID = ID;
        this.idIcon = IDIcon;
        this.name = name;
        this.info = info;
        this.mWearing = wear;
        this.op = op;
        this.price = price;
    }

    public static ItemFashion get_item(int id) {
        for (int i = 0; i < ItemFashion.ENTRYS.size(); i++) {
            ItemFashion temp = ItemFashion.ENTRYS.get(i);
            if (temp.ID == id) {
                return temp;
            }
        }
        return null;
    }
}
