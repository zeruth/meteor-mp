package jagex2.dash3d.type;

import jagex2.graphics.Model;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client.client!j")
public class GroundDecor {

	@OriginalMember(owner = "client.client!j", name = "a", descriptor = "I")
	public int y;

	@OriginalMember(owner = "client.client!j", name = "b", descriptor = "I")
	public int x;

	@OriginalMember(owner = "client.client!j", name = "c", descriptor = "I")
	public int z;

	@OriginalMember(owner = "client.client!j", name = "d", descriptor = "Lclient!eb;")
	public Model model;

	@OriginalMember(owner = "client.client!j", name = "e", descriptor = "I")
	public int bitset;

	@OriginalMember(owner = "client.client!j", name = "f", descriptor = "B")
	public byte info;
}
