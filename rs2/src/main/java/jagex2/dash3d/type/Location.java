package jagex2.dash3d.type;

import jagex2.dash3d.entity.Entity;
import jagex2.graphics.Model;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client.client!p")
public class Location {

	@OriginalMember(owner = "client.client!p", name = "a", descriptor = "I")
	public int level;

	@OriginalMember(owner = "client.client!p", name = "b", descriptor = "I")
	public int y;

	@OriginalMember(owner = "client.client!p", name = "c", descriptor = "I")
	public int x;

	@OriginalMember(owner = "client.client!p", name = "d", descriptor = "I")
	public int z;

	@OriginalMember(owner = "client.client!p", name = "e", descriptor = "Lclient!eb;")
	public Model model;

	@OriginalMember(owner = "client.client!p", name = "f", descriptor = "Lclient!w;")
	public Entity entity;

	@OriginalMember(owner = "client.client!p", name = "g", descriptor = "I")
	public int yaw;

	@OriginalMember(owner = "client.client!p", name = "h", descriptor = "I")
	public int minSceneTileX;

	@OriginalMember(owner = "client.client!p", name = "i", descriptor = "I")
	public int maxSceneTileX;

	@OriginalMember(owner = "client.client!p", name = "j", descriptor = "I")
	public int minSceneTileZ;

	@OriginalMember(owner = "client.client!p", name = "k", descriptor = "I")
	public int maxSceneTileZ;

	@OriginalMember(owner = "client.client!p", name = "l", descriptor = "I")
	public int distance;

	@OriginalMember(owner = "client.client!p", name = "m", descriptor = "I")
	public int cycle;

	@OriginalMember(owner = "client.client!p", name = "n", descriptor = "I")
	public int bitset;

	@OriginalMember(owner = "client.client!p", name = "o", descriptor = "B")
	public byte info;
}
