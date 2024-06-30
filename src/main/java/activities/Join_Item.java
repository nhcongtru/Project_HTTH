package activities;

import java.io.IOException;
import client.Player;
import core.Service;
import io.Message;
import template.ItemTemplate7;
/**
 *
 * @author Truongbk
 */
public class Join_Item {
    public static short[][] ID = new short[][] { //
            new short[] {1, 0}, //
            new short[] {3, 2, 1}, //
            new short[] {9, 8}, //
            new short[] {8, 9}, //
            new short[] {11, 5} //
    };
    public static short[][] NUM = new short[][] { //
            new short[] {1, 5}, //
            new short[] {1, 5, 1}, //
            new short[] {1, 5}, //
            new short[] {4, 1}, //
            new short[] {1, 5} //
    };
    public static String[] NAME = new String[] { //
            "Để ghép 1 nguyên liệu Bột cường hóa bạn cần có\n5 Đá ngũ sắc", //
            "Để ghép 1 nguyên liệu Bột tím bạn cần có\n5 Bột than + 1 Bột cường hóa", //
            "Để ghép 1 nguyên liệu Đá ác quỷ bạn cần có\n5 Tinh thể đá ác quỷ", //
            "Để ghép 4 nguyên liệu Tinh thể đá ác quỷ bạn cần có\n1 Đá ác quỷ", //
            "Để ghép 1 nguyên liệu Thiên thạch may mắn bạn cần có\n5 Ngôi sao may mắn" //
    };

    public static void show_table(Player p, int index) throws IOException {
        p.last_index_join_item = (byte) index;
        Message m = new Message(-50);
        m.writer().writeByte(1);
        m.writer().writeByte(0);
        m.writer().writeUTF(Join_Item.NAME[index]);
        m.writer().writeByte(Join_Item.ID[index].length);
        for (int i = 0; i < Join_Item.ID[index].length; i++) {
            m.writer().writeShort(Join_Item.ID[index][i]);
            m.writer().writeByte(7);
            m.writer().writeShort(Join_Item.NUM[index][i]);
        }
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static void process(Player p, Message m2) throws IOException {
        byte act = m2.reader().readByte();
        short id = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        short num = m2.reader().readShort();
        if (act == 1 && cat == 7 && num > 0) { // ghep item7 id 1
            // check request
            short[] id_request = new short[] {-1, -1, -1};
            short[] num_request = new short[] {0, 0, 0};
            for (int i = 0; i < Join_Item.ID.length; i++) {
                if (Join_Item.ID[i][0] == id) {
                    for (int j = 1; j < Join_Item.NUM[i].length; j++) {
                        int quant = num * Join_Item.NUM[i][j];
                        if (p.item.total_item_bag_by_id(7, Join_Item.ID[i][j]) < quant) {
                            Service.send_box_ThongBao_OK(p, "Không đủ " + quant + " "
                                    + ItemTemplate7.get_it_by_id(Join_Item.ID[i][j]).name);
                            return;
                        }
                        id_request[j] = Join_Item.ID[i][j];
                        num_request[j] = (short) quant;
                    }
                    id_request[0] = Join_Item.ID[i][0];
                    num_request[0] = Join_Item.NUM[i][0];
                }
            }
            // remove request
            for (int i = 1; i < id_request.length; i++) {
                if (num_request[i] > 0) {
                    p.item.remove_item47(7, id_request[i], num_request[i]);
                }
            }
            // add item result
            p.item.add_item_bag47(7, id_request[0], (num * num_request[0]));
            p.item.update_Inventory(-1, false);
            //
            Message m = new Message(-50);
            m.writer().writeByte(1);
            m.writer().writeByte(1);
            m.writer().writeUTF("Thành công");
            m.writer().writeShort(id_request[0]);
            m.writer().writeByte(7);
            m.writer().writeShort((num * num_request[0]));
            p.conn.addmsg(m);
            m.cleanup();
        } else if (act == 4 && id == 0 && cat == 0 && num == 0) { // show table again
            Join_Item.show_table(p, p.last_index_join_item);
        }
    }
}
