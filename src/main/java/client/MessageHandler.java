package client;

import java.io.IOException;
import activities.*;
import core.*;
import event.EventSpecial;
import io.Message;
import io.Session;
import map.Map;
import template.EffTemplate;
import template.ItemBoat;
/**
 *
 * @author Truongbk
 */
public class MessageHandler {

    private Session conn;

    public MessageHandler(Session session) {
        this.conn = session;
    }

    public void process_msg(Message m) throws IOException {
        switch (m.cmd) {
            case -86: {
                if (conn.p != null) {
                    Wanted_Chest.process(conn.p, m);
                }
                break;
            }
            case -85: {
                if (conn.p != null) {
                    Wanted.process(conn.p, m);
                }
                break;
            }
            case -35: {
                if (conn.p != null) {
                    Fight.process(conn.p, m);
                }
                break;
            }
            case -80: {
                if (conn.p != null) {
                    Pet.process(conn.p, m);
                }
                break;
            }
            case 79: {
                if (conn.p != null) {
                    HanhTrinh.process(conn.p, m);
                }
                break;
            }
            case -94: {
                if (conn.p != null) {
                    UpgradeDial.process(conn.p, m);
                }
                break;
            }
            case 81: {
                if (conn.p != null) {
                    Upgrade_Skin.process(conn.p, m);
                }
                break;
            }
            case -53: {
                if (conn.p != null) {
                    Ship.process(conn.p, m);
                }
                break;
            }
            case 74: {
                byte type = m.reader().readByte();
                short id = m.reader().readShort();
                if (conn.p != null && type == 0) {
                    // System.out.println("request skill: " + id);
                    try {
                        Message m2 = new Message(74);
                        m2.writer().writeByte(0);
                        m2.writer().writeShort(id);
                        byte[] data1 = Util
                                .loadfile("data/template/skill/x" + conn.zoomlv + "/data/" + id);
                        byte[] data2 = Util.loadfile(
                                "data/template/skill/x" + conn.zoomlv + "/img/" + id + ".png");
                        m2.writer().writeShort(data1.length);
                        m2.writer().write(data1);
                        m2.writer().write(data2);
                        conn.addmsg(m2);
                        m2.cleanup();
                    } catch (Exception e) {
                    }
                }
                break;
            }
            case 43: {
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    byte value = m.reader().readByte();
                    // System.out.println(type);
                    // System.out.println(value);
                    if (type == 0) {
                        if (value == 1 || value == 0) {
                            conn.p.is_show_hat = !conn.p.is_show_hat;
                            Service.charWearing(conn.p, conn.p, false);
                            Service.update_PK(conn.p, conn.p, false);
                            conn.p.update_info_to_all();
                            Service.send_box_ThongBao_OK(conn.p,
                                    conn.p.is_show_hat ? "Đã bật hiển thị nón"
                                            : "Đã tắt hiển thị nón");
                        }
                    } else if (type == 1) {
                        if (value == 1 || value == 0) {
                            conn.p.is_show_weapon = !conn.p.is_show_weapon;
                            Service.charWearing(conn.p, conn.p, false);
                            Service.update_PK(conn.p, conn.p, false);
                            conn.p.update_info_to_all();
                            Service.send_box_ThongBao_OK(conn.p,
                                    conn.p.is_show_weapon ? "Đã bật hiển thị vũ khí thời trang"
                                            : "Đã tắt hiển thị vũ khí thời trang");
                        }
                    }
                }
                break;
            }
            case -91: {
                if (conn.p != null) { // dau gia
                }
                break;
            }
            case -63: {
                if (conn.p != null) {
                    Pvp.process(conn.p, m);
                }
                break;
            }
            case 68: {
                if (conn.p != null && conn.p.map != null) {
                    conn.p.tocSuper++;
                    if (conn.p.tocSuper > 2) {
                        conn.p.tocSuper = 0;
                    }
                    for (int i = 0; i < conn.p.map.players.size(); i++) {
                        Player p0 = conn.p.map.players.get(i);
                        Service.charWearing(conn.p, p0, false);
                    }
                }
                break;
            }
            case -95: {
                break;
            }
            case -36: {
                int id = m.reader().readInt();
                if (conn.p != null) {
                    Player p0 = null;
                    for (int i = 0; i < conn.p.friend_list.size(); i++) {
                        if (conn.p.friend_list.get(i).id == id) {
                            p0 = Map.get_player_by_name_allmap(conn.p.friend_list.get(i).name);
                            break;
                        }
                    }
                    if (p0 == null) {
                        for (int i = 0; i < conn.p.enemy_list.size(); i++) {
                            if (conn.p.enemy_list.get(i).id == id) {
                                p0 = Map.get_player_by_name_allmap(conn.p.enemy_list.get(i).name);
                                break;
                            }
                        }
                    }
                    boolean check = false;
                    if (p0 != null) {
                        if (Map.map_cant_save_site(p0.map.template.id)) {
                            Service.send_box_ThongBao_OK(conn.p,
                                    "Không thể dịch chuyển đến lúc này");
                            return;
                        }
                        for (int i = 0; i < p0.friend_list.size(); i++) {
                            if (p0.friend_list.get(i).name.equals(conn.p.name)) {
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            for (int i = 0; i < conn.p.enemy_list.size(); i++) {
                                if (conn.p.enemy_list.get(i).name.equals(p0.name)) {
                                    check = true;
                                    break;
                                }
                            }
                        }
                        if (check) {
                            conn.p.data_yesno = new int[]{id};
                            Service.send_box_yesno(conn.p, 43, "Thông báo",
                                    ("Dịch chuyển đến người này mất 5 ruby, xác nhận dịch chuyển?"),
                                    new String[]{"5", "Không"}, new byte[]{7, -1});
                        }
                    }
                    if (!check) {
                        Service.send_box_ThongBao_OK(conn.p,
                                "Đối phương không online hoặc không có trong danh sách");
                    }
                }
                break;
            }
            case -52: {
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    // System.out.println(type);
                    if (conn.p.clan != null || type == 11 || type == 12) { // type 11 xin vao clan,
                        // type 12 dong y moi
                        // vao clan
                        Clan.process(conn.p, m, type);
                    }
                }
                break;
            }
            case 49: {
                if (conn.p != null) {
                    Max_Level.process(conn.p, m);
                }
                break;
            }
            case 80: { // event
                if (conn.p != null) {
                    EventSpecial.process(conn.p, m);
                }
                break;
            }
            case -71: { // auto revive
                if (conn.p != null && conn.p.map != null && conn.p.map.map_pvp == null
                        && conn.p.map.map_little_garden == null) {
                    if (conn.p.type_pk == -1 && conn.p.typePirate == -1 && conn.p.pointPk == 0) {
                        if (m.reader().readByte() == 1) {
                            if (conn.p.item.total_item_bag_by_id(4, 89) > 0) {
                                conn.p.item.remove_item47(4, 89, 1);
                                conn.p.item.update_Inventory(-1, false);
                                conn.p.isdie = false;
                                Service.use_potion(conn.p, 0, conn.p.body.get_hp_max(true));
                                Service.use_potion(conn.p, 1, conn.p.body.get_mp_max(true));
                                //
                                Message m2 = new Message(-71);
                                m2.writer().writeByte(1);
                                m2.writer().writeShort(conn.p.index_map);
                                m2.writer().writeByte(0);
                                m2.writer().writeInt(60 * 30);
                                conn.p.map.send_msg_all_p(m2, conn.p, true);
                                m2.cleanup();
                                EffTemplate eff = conn.p.get_eff(7);
                                if (eff != null) {
                                    eff.time = System.currentTimeMillis() + 60_000L * 15;
                                } else {
                                    conn.p.add_new_eff(7, 1, 60_000L * 15);
                                }
                            }
                        }
                    }
                }
                break;
            }
            case -74: {
                if (conn.p != null) {
                    TableTickOption.process(conn.p, m);
                }
                break;
            }
            case 44: {

                if (conn.p != null) {
                    Market.process(conn.p, m);
                }
                break;
            }
            case -23: {
                if (conn.p != null) {
                    Quest.process(conn.p, m);
                }
                break;
            }
            case -72: {
                if (conn.p != null && conn.p.map != null) {
                    if (conn.p.map.template.id == 64) {
                        Red_Line.process(conn.p, m);
                    } else if (conn.p.map.map_ThuThachVeThan != null
                            && !conn.p.map.map_ThuThachVeThan.isFinish) {
                        Red_Line.process_TTVT(conn.p, m);
                    }
                }
                break;
            }
            case 66: {
                if (conn.p != null) {
                    UpgradeSuperItem.process(conn.p, m);
                }
                break;
            }
//            case -30: {
//                if (conn.p != null) {
//                    BXH.process(conn.p, m);
//                }
//                break;
//            }
            case -50: {
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    if (type == 0) {
                        Split_Item.process(conn.p, m);
                    } else if (type == 1) {
                        Join_Item.process(conn.p, m);
                    }
                }
                break;
            }
            case -62: {
                if (conn.p != null) {
                    ItemBoat.update_part_boat_when_shopping(conn.p);
                }
                break;
            }
            case -28: {
                if (conn.p != null) {
                    Learn_Skill.process(conn.p, m);
                }
                break;
            }
            case 45: {
                if (conn.p != null) {
                    UpgradeDevil.process(conn.p, m);
                }
                break;
            }
            case -25: {
                if (conn.p != null) {
                    Party.process(conn.p, m);
                }
                break;
            }
            case 20: {
                if (conn.p != null && conn.p.map != null && !conn.p.isdie
                        && conn.p.get_eff(201) == null) {
                    Buff.process(conn.p, m);
                }
                break;
            }
            case -32: {
                if (conn.p != null) {
                    PlayerChest.process(conn.p, m);
                }
                break;
            }
            case 54: {
                if (conn.p != null) {
                    VongQuay.process(conn.p, m);
                }
                break;
            }
            case 18: {
                if (conn.p != null) {
                    Chat.process(conn.p, m, 0);
                }
                break;
            }
            case -29: {
                if (conn.p != null) {
                    Friend.process(conn.p, m);
                }
                break;
            }
            case -49: {
                if (conn.status != 1) {
                    Service.send_box_ThongBao_OK(conn.p,
                            "Chưa Kích hoạt không thể giao dịch");
                    return;
                }
                Trade.process(conn.p, m);
                break;
            }
            case -67: {
                if (conn.p != null) {
                    Rebuild_Item.process(conn.p, m);
                }
                break;
            }
            case -77: {
                if (conn.p != null) {
                    ChuyenHoa.process(conn.p, m);
                }
                break;
            }
            case -16: {
                if (conn.p != null) {
                    conn.p.plus_point(m);
                }
                break;
            }
            case -13: { // use potion
                if (conn.p != null) {
                    short id = m.reader().readShort();
                    UseItem.use_item_potion(conn.p, id);
                }
                break;
            }
            case -11: {
                if (conn.p != null) {
                    ClientYesNo.process(conn.p, m);
                }
                break;
            }
            case -58: {
                if (conn.p != null) {
                    ClientInput.process(conn.p, m);
                }
                break;
            }
            case -46: {
                if (conn.status != 1) {
                    Service.send_box_ThongBao_OK(conn.p,
                            "Chưa Kích hoạt không thể chat KTG");
                    return;
                }
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    String text = m.reader().readUTF();
                    if (type == 0) {

                        if (conn.p.get_ngoc() < 5) {
                            Service.send_box_ThongBao_OK(conn.p, "Không đủ 5 ruby để chat KTG");
                            return;
                        }
                        conn.p.update_ngoc(-5);
                        conn.p.update_money();
                        Manager.gI().chatKTG(conn.p, conn.p.name + ": " + text);
                    } else if (type == 1 && conn.p.clan != null) {
                        boolean check = false;
                        for (int i = 0; i < conn.p.clan.members.size(); i++) {
                            if (conn.p.clan.members.get(i).name.equals(conn.p.name)
                                    && (conn.p.clan.members.get(i).levelInclan == 1
                                    || conn.p.clan.members.get(i).levelInclan == 0)) {
                                check = true;
                                break;
                            }
                        }
                        if (check) {
                            if (conn.p.clan.get_ngoc() < 15) {
                                Service.send_box_ThongBao_OK(conn.p,
                                        "Không đủ 15 ruby băng để chat KTG");
                                return;
                            }
                            conn.p.clan.update_ruby(-15);
                            for (int i = 0; i < conn.p.clan.members.size(); i++) {
                                Player p0 = Map
                                        .get_player_by_name_allmap(conn.p.clan.members.get(i).name);
                                if (p0 != null) {
                                    Clan.send_money(p0, false);
                                }
                            }
                            Message m23 = new Message(-31);
                            m23.writer().writeByte(type);
                            m23.writer().writeUTF(conn.p.clan.name + ": " + text);
                            m23.writer().writeByte(0);
                            m23.writer().writeShort(conn.p.clan.icon);
                            for (Map[] mapall : Map.ENTRYS) {
                                for (Map map : mapall) {
                                    for (int i = 0; i < map.players.size(); i++) {
                                        Player p0 = map.players.get(i);
                                        p0.conn.addmsg(m23);
                                    }
                                }
                            }
                            m23.cleanup();
                        }
                    }
                }
                break;
            }
            case -48: {
                if (conn.p != null) {
                    UpgradeItem.process(conn.p, m);
                }
                break;
            }
            case -22: {
                if (conn.p != null) {
                    UseItem.process(conn.p, m);
                }
                break;
            }
            case -105: {
                if (conn.p != null) {
                    Service.request_item4_info(conn.p, m);
                }
                break;
            }
            case -21: {
                if (conn.p != null) {
                    Service.sell_item(conn.p, m);
                }
                break;
            }
            case 12: {
                if (conn.p != null) {
                    conn.p.map.pick_item(conn.p, m);
                }
                break;
            }
            case -42: {
                if (conn.p != null) {
                    String name = m.reader().readUTF();
                    Player p0 = Map.get_player_by_name_allmap(name);
                    if (p0 != null) {
                        Service.send_view_other_player(p0, conn.p);
                    }
                }
                break;
            }
            case 14: {
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    byte act = m.reader().readByte();
                    // System.out.println(type);
                    // System.out.println(act);
                    if (act == 0) {
                        conn.p.map.change_flag(conn.p, type);
                    }
                }
                break;
            }
            case 6: {
                if (conn.p != null) {
                    conn.p.request_live_from_die(m);
                }
                break;
            }
            case -18: {
                if (conn.p != null) {
                    Service.buy_item(conn.p, m);
                }
                break;
            }
            case -5: {
                if (conn.p != null) {
                    short id = m.reader().readShort();
                    conn.p.map.send_char_in4_inmap(conn.p, id);
                }
                break;
            }
            case 46: {
                if (conn.p != null) {
                    Service.checkPlayInMap(conn.p, m);
                }
                break;
            }
            case 0: {
                if (conn.p != null) {
                    // int id_map_change =
                    m.reader().readShort();
                    // byte action_change =
                    m.reader().readByte();
                    // System.out.println(id_map_change);
                    // System.out.println(action_change);
                    while (conn.p.list_msg_cache.size() > 0) {
                        try {
                            Message m_send = conn.p.list_msg_cache.take();
                            conn.addmsg(m_send);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Service.getThanhTich(conn.p, conn.p);
                    conn.p.map.send_in4_obj_inmap(conn.p);
                    conn.p.wait_change_map = false;
                    //
                    if (conn.p.map.template.id == 1000) {// map wait pvp
                        Pvp.show_table(conn.p);
                    }
                    if (conn.p.map.map_pvp != null) {// map pvp
                        Pvp.show_info(conn.p, 0, 0, 0, 3);
                    }
                    conn.p.map.change_flag(conn.p, conn.p.type_pk);
                    Service.update_PK(conn.p, conn.p, false);
                    // weather
                    conn.p.map.send_weather(conn.p);
                    // ship pet
                    if (conn.p.ship_pet != null && conn.p.ship_pet.map == null) {
                        conn.p.ship_pet.map = conn.p.map;
                        conn.p.ship_pet.id_map_save = conn.p.map.template.id;
                        conn.p.ship_pet.x = conn.p.x;
                        conn.p.ship_pet.y = conn.p.y;
                        Message m_local = new Message(1);
                        m_local.writer().writeByte(0);
                        m_local.writer().writeShort(conn.p.ship_pet.index_map);
                        m_local.writer().writeShort(conn.p.ship_pet.x);
                        m_local.writer().writeShort(conn.p.ship_pet.y);
                        for (int j = 0; j < conn.p.map.players.size(); j++) {
                            Player p0 = conn.p.map.players.get(j);
                            p0.conn.addmsg(m_local);
                        }
                        m_local.cleanup();
                    }
                    if (Map.is_map_dungeon(conn.p.map.template.id) && conn.p.dungeon != null) {
                        Service.send_time_cool_down(conn.p, conn.p.dungeon.time, "Thời gian", 2);
                    } else if (conn.p.map.template.id == 9999 && conn.p.map.clan_resource != null) {
                        Service.send_time_cool_down(conn.p, conn.p.map.clan_resource.time,
                                "Thời gian", 2);
                    } else if (conn.p.map.template.id == 81
                            && conn.p.map.map_little_garden != null) { // pho ban khong
                        // lo
                        LittleGarden.send_info(conn.p);
                        Service.send_time_cool_down(conn.p, conn.p.map.map_little_garden.time,
                                "Thời gian", 2);
                    } else if (conn.p.map.template.id == 984) { // pho ban thu thach ve than
                        conn.p.map.update_boat(conn.p, conn.p, false);
                        //
                        Red_Line.init_key_TTVT(conn.p);
                    } else if (conn.p.map.map_pvp != null && conn.p.map.map_pvp.type_map == 1) { // map
                        // sieu
                        // hang
                        conn.p.update_info_to_all();
                    } else if (conn.p.map.template.id == 119) { // phong cho truy na
                        Wanted.show_table(conn.p);
                        Service.Wanted(conn.p, false);
                        Wanted_Chest.send_box(conn.p);
                    }
                }
                break;
            }
            case 23: {
                if (conn.p != null && !conn.p.isdie) {
                    if (conn.p.ship_pet != null) {
                        Service.send_box_ThongBao_OK(conn.p,
                                "Không thể chuyến khu khi đang chuyển hàng");
                    } else {
                        Service.area_select(conn.p, m);
                    }
                }
                break;
            }
            case 17: {
                if (conn.p != null) {
                    conn.p.map.send_chat(conn.p, m);
                }
                break;
            }
            case -20: {
                if (conn.p != null) {
                    MenuController.process_menu(conn.p, m);
                }
                break;
            }
            case -19: {
                if (conn.p != null) {
                    MenuController.send_menu(conn.p, m);
                }
                break;
            }
            case 2: {
                if (conn.p != null && conn.p.map != null) {
                    conn.p.map.use_skill(conn.p, m);
                }
                break;
            }
            case -70: {
                if (conn.p != null) {
                    conn.p.map.update_num_player_in_map(conn.p);
                }
                break;
            }
            case -45: {// update pk point
                if (conn.p != null) {
                    conn.p.update_point_pk(0);
                    Service.CountDown_Ticket(conn.p);
                    //
                    Service.charWearing(conn.p, conn.p, false);
                }
                break;
            }
            case -33: {
                if (conn.p != null) {
                    Service.rms_process(conn.p, m);
                }
                break;
            }
            case 1: {
                if (conn.p != null && conn.p.map != null) {
                    conn.p.map.send_move(conn.p, m);
                }
                break;
            }
            case 4: {
                if (conn.p != null) {
                    Service.request_mob_in4(conn.p, m);
                }
                break;
            }
            case 48: {
                if (conn.p != null) {
                    Service.send_obj_template(conn.p, m);
                }
                break;
            }
            case -9: {
                if (conn.p == null) {
                    login(m);
                }
                break;
            }
            case -8: {
                conn.create_char(m);
                break;
            }
            case -51: {
                Service.send_icon(m, conn);
                break;
            }
            case -82: {
                conn.ReadPartNew(m);
                break;
            }
            case -38: {
                conn.send_data_from_server(m);
                break;
            }
            case -2: {
                if (conn.user == null && conn.pass == null) {
                    conn.login(m);
                }
                break;
            }
            case -6: {
                conn.Check_Data_Ver();
                break;
            }
            case -7: {
                conn.request_data_update(m);
                break;
            }
        }
    }

    public void login(Message m2) throws IOException {
        short id = m2.reader().readShort();
        // byte type =
        m2.reader().readByte();
        // short idsupport =
        m2.reader().readShort();
        login_into_char_select(id);
    }

    public void login_into_char_select(short id) throws IOException {
        if (conn.list_char != null && id < conn.list_char.size()) {
            Player p0 = new Player(conn, conn.list_char.get(id));
            if (!p0.setup()) {
                conn.disconnect();
                return;
            }
            p0.setin4();
            if (Map.get_player_by_name_allmap(p0.name) != null) {
                conn.disconnect();
                return;
            }
            conn.p = p0;
            Message m = new Message(-7); // update clock
            m.writer().writeByte(17);
            m.writer().writeLong(System.currentTimeMillis());
            conn.addmsg(m);
            m.cleanup();
            //
            Service.UpdateInfoMaincharInfo(conn.p);
            Service.Main_char_Info(conn.p);
            Service.UpdatePvpPoint(conn.p);
            Service.update_PK(conn.p, conn.p, false);
            Service.getThanhTich(conn.p, conn.p);
            conn.p.item.send_maxbag_Inventory();
            // send data map
            conn.p.map.goto_map(conn.p);
            //
            Service.update_PK(conn.p, conn.p, true);
            Service.pet(conn.p, conn.p, true);
            conn.p.item.update_Inventory(-1, true);
            conn.p.item.update_assets_Inventory(true);
            Service.ChestWanted(conn.p, true);
            //
            conn.p.item.send_maxbox_Inventory();
            conn.p.item.update_assets_Box(true);
            conn.p.item.update_Inventory_box(-1, true);
            //
            Quest.send_List_Quest(conn.p, true);
            Quest.update_map_have_side_quest(conn.p, true);
            Service.Weapon_fashion(conn.p, conn.p, true);
            UpgradeItem.send_heart_info(conn.p, true);
            Service.charWearing(conn.p, conn.p, true);
            conn.p.map.send_boat(conn.p, true);
            conn.p.map.update_boat(conn.p, conn.p, true);
            Service.login_ok(conn.p, true);
            Service.Wanted(conn.p, true);
            Clan.send_info(conn.p, true);
            conn.p.item.update_assets_Inventory(true);
            //
            Message m2 = new Message(18);
            m2.writer().writeUTF("Tin đến");
            m2.writer().writeUTF(
                    "UPDATE - Chức Năng Tuyệt kỹ Lục Thức\n"
                    + "\n"
                    + "Cùng với Haki, Trái ác quỷ và Karate Người cá thì Lục thức là một trong những sức mạnh độc đáo của One Piece.\n"
                    + "\n"
                    + "Chi Tiết : Tự tìm hiểu \n"
            );
            conn.p.list_msg_cache.add(m2);
            Message m3 = new Message(18);
            m3.writer().writeUTF("Đua Top ");
            m3.writer().writeUTF("\nThời gian: \n"
                    + "Phần thưởng:\n"
                    + "- Top 1: 1 trái ác quỷ bóng tối, 15 khiên, 1 đá Hổ phách - Ruby siêu cấp, 1 thời trang UTA (vĩnh viễn)\n"
                    + "- Top 2-3: 1 trái ác quỷ bóng tối, 10 khiên, 1 đá Hổ phách cấp 6\n"
                    + "- Top 4-10: 1 trái ác quỷ sét, 5 khiên, 1 đá hổ phách cấp 5.\n"
                    + "TOP 10 nhân vật nạp nhiều nhất:\n"
                    + "- TOP 1: Trái ác quỷ Bóng Tối, Thời trang Mihawk Gold, 1 Đá khảm vô cực S, 100 Đá hải thạch cấp 6, 500.000.000 Beri\n"
                    + "- TOP 2: 1 Trang bị Max +12, Thời trang Mihawk Gold, 1 Đá khảm vô cực S, 80 Đá hải thạch cấp 6, 400.000.000 Beri\n"
                    + "- TOP 3: 1 Trang bị Max +12, Thời trang Mihawk, 1 Đá khảm vô cực S, 50 Đá hải thạch cấp 6, 300.000.000 Beri\n"
                    + "- TOP 4-10: 1 Trang bị Max +10, Thời trang Mihawk, 1 Đá khảm vô cực, 30 Đá hải thạch cấp 6, 100.000.000 Beri\n"
                    + "");
            conn.p.list_msg_cache.add(m3);
            Message m4 = new Message(18);
            m4.writer().writeUTF("GiftCode");
            m4.writer().writeUTF(
                     " \n- 1: tanthu"
//                    + "\n- 2: ThanhVien"
//                    + "\n- 3:tambiet2023"
//                    + "\n- 4:htht2024"
//                    + "\n- 5:baotri"
                    );
            conn.p.list_msg_cache.add(m4);
        }
    }
}
