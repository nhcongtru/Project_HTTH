package template;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Truongbk
 */
public class Skill_Template {
    // skill id buff  10 trung doc, 11 bat tu, 12 crit lien tuc
    // 1 choang, 2 chay mau
    public static List<Skill_Template> ENTRYS;
    public int ID;
    public int indexSkillInServer;
    public short idIcon;
    public byte typeSkill;
    public byte typeBuff;
    public String name;
    public short range;
    private short typeEffSkill;
    public List<Option> op;
    public byte idEffSpec;
    public short perEffSpec;
    public short timeEffSpec;
    public byte Lv_RQ;
    public byte nTarget;
    public short rangeLan;
    public int damage;
    public short manaLost;
    public int timeDelay;
    public byte nKick;
    public String info;
    public byte typeDevil;
    public int percentDame = 100;

    public String getInfo(byte level, int clazz) {
        String result = info;
        switch (clazz) {
            case 2: {
                result = result.replace("Quả đấm tốc độ", "Nhất kiếm");
                break;
            }
            case 3: {
                result = result.replace("Quả đấm tốc độ", "Hắc cước");
                break;
            }
            case 4: {
                result = result.replace("Quả đấm tốc độ", "Gậy chong chóng");
                break;
            }
            case 5: {
                result = result.replace("Quả đấm tốc độ", "Double Shot");
                break;
            }
        }
        String percent = "";
        Pattern pattern = Pattern.compile("[\\d]+");
        Matcher matcher = pattern.matcher(info);
        while (matcher.find()) {
            percent = matcher.group();
        }
        if (!percent.isBlank()) {
            int value = Integer.parseInt(percent);
            switch (level) {
                case 1: {
                    value = (value * 11) / 10;
                    break;
                }
                case 2: {
                    value = (value * 125) / 100;
                    break;
                }
                case 3: {
                    value = (value * 145) / 100;
                    break;
                }
                case 4: {
                    value = (value * 17) / 10;
                    break;
                }
                case 5: {
                    value *= 2;
                    break;
                }
            }
            if (!percent.isBlank()) {
                result = result.replace(percent, (value + ""));
                percentDame = value;
            }
        }
        return result;
    }

    public Skill_Template(int Index, int Id, short IdImage, byte type, byte typeBuff, String name, short typeEff,
                          short range) {
        this.indexSkillInServer = Index;
        this.ID = (int) Id;
        this.idIcon = IdImage;
        this.typeSkill = type;
        this.typeBuff = typeBuff;
        this.name = name;
        this.range = range;
        this.typeEffSkill = typeEff;
    }

    public void getData(byte nTarget, short rangeLan, int Damage, short Manacost, int CoolDown, byte nkick,
                        String Description, byte LvCur, byte typeDevil) {
        this.nTarget = nTarget;
        this.rangeLan = rangeLan;
        this.damage = Damage;
        this.manaLost = Manacost;
        this.timeDelay = CoolDown;
        this.nKick = nkick;
        this.info = Description;
        this.Lv_RQ = LvCur;
        this.typeDevil = typeDevil;
    }

    public static Skill_Template get_temp(int index, long exp) {
        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
            Skill_Template temp = Skill_Template.ENTRYS.get(i);
            if (temp.indexSkillInServer == index) {
                if (exp == -1 && temp.Lv_RQ == -1) {
                    return temp;
                } else if (exp > -1 && temp.Lv_RQ > -1) {
                    return temp;
                }
            }
        }
        return null;
    }

    public static boolean upgrade_skill(Skill_info sk_info, byte clazz) {
        Skill_Template result = null;
        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
            Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
            if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                switch (clazz) {
                    case 1: {
                        if (temp_ss.indexSkillInServer >= 0 && temp_ss.indexSkillInServer < 60
                                || temp_ss.indexSkillInServer >= 375 && temp_ss.indexSkillInServer < 395
                                || temp_ss.indexSkillInServer >= 566 && temp_ss.indexSkillInServer <= 583) {
                            result = temp_ss;
                        }
                        break;
                    }
                    case 2: {
                        if (temp_ss.indexSkillInServer >= 60 && temp_ss.indexSkillInServer < 120
                                || temp_ss.indexSkillInServer >= 395 && temp_ss.indexSkillInServer < 415
                                || temp_ss.indexSkillInServer >= 584 && temp_ss.indexSkillInServer <= 601) {
                            result = temp_ss;
                        }
                        break;
                    }
                    case 3: {
                        if (temp_ss.indexSkillInServer >= 120 && temp_ss.indexSkillInServer < 180
                                || temp_ss.indexSkillInServer >= 415 && temp_ss.indexSkillInServer < 435
                                || temp_ss.indexSkillInServer >= 602 && temp_ss.indexSkillInServer <= 619) {
                            result = temp_ss;
                        }
                        break;
                    }
                    case 4: {
                        if (temp_ss.indexSkillInServer >= 180 && temp_ss.indexSkillInServer < 240
                                || temp_ss.indexSkillInServer >= 435 && temp_ss.indexSkillInServer < 455
                                || temp_ss.indexSkillInServer >= 620 && temp_ss.indexSkillInServer <= 637) {
                            result = temp_ss;
                        }
                        break;
                    }
                    case 5: {
                        if (temp_ss.indexSkillInServer >= 240 && temp_ss.indexSkillInServer < 300
                                || temp_ss.indexSkillInServer >= 455 && temp_ss.indexSkillInServer < 475
                                || temp_ss.indexSkillInServer >= 638 && temp_ss.indexSkillInServer <= 655) {
                            result = temp_ss;
                        }
                        break;
                    }
                }
                if (result != null) {
                    break;
                }
            }
        }
        if (result == null && sk_info.temp.indexSkillInServer >=661&&sk_info.temp.indexSkillInServer <=665){ // skill dial
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
                if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                    result=temp_ss;
                    break;
                }
            }
        }
        if (result != null && result.Lv_RQ > 0) {
            if (result.Lv_RQ > 25) {
                return false;
            } else {
                sk_info.temp = result;
                return true;
            }
        }
        return false;
    }

    public static boolean learn_skill(Skill_info sk_info) {
        if (sk_info.temp.Lv_RQ == -1) {
            Skill_Template result = null;
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                if (sk_info.temp.indexSkillInServer == Skill_Template.ENTRYS.get(i).indexSkillInServer
                        && sk_info.temp.ID == Skill_Template.ENTRYS.get(i).ID && Skill_Template.ENTRYS.get(i).Lv_RQ == 1) {
                    result = Skill_Template.ENTRYS.get(i);
                    break;
                }
            }
            if (result != null) {
                sk_info.temp = result;
                sk_info.exp = 0;
                return true;
            }
        } else {
            Skill_Template result = null;
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                if (sk_info.temp.indexSkillInServer == (Skill_Template.ENTRYS.get(i).indexSkillInServer - 1)) {
                    result = Skill_Template.ENTRYS.get(i);
                    break;
                }
            }
            if (result != null) {
                sk_info.temp = result;
                sk_info.exp = 0;
                return true;
            }
        }
        return false;
    }

    public static void reset_skill(Skill_info sk_info) {
        if (sk_info.temp.Lv_RQ == -1) {
            return;
        }
        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
            if (sk_info.temp.ID == Skill_Template.ENTRYS.get(i).ID && Skill_Template.ENTRYS.get(i).Lv_RQ == -1) {
                sk_info.temp = Skill_Template.ENTRYS.get(i);
                sk_info.exp = -1;
                break;
            }
        }
    }

    public short getTypeEffSkill() {
        return typeEffSkill;
    }
}
