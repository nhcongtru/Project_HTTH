package template;
/**
 *
 * @author Truongbk
 */
public class UpgradeMaterialTemplate {
	public byte type;
	public byte id;
	public int quant;

	public UpgradeMaterialTemplate(byte type, byte id, int quant) {
		this.type = type;
		this.id = id;
		this.quant = quant;
	}
}
