package jagex2.dash3d.type;

import jagex2.datastruct.Linkable;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client.client!cb")
public class Ground extends Linkable {

	@OriginalMember(owner = "client.client!cb", name = "e", descriptor = "I")
	public int level;

	@OriginalMember(owner = "client.client!cb", name = "f", descriptor = "I")
	public final int x;

	@OriginalMember(owner = "client.client!cb", name = "g", descriptor = "I")
	public final int z;

	@OriginalMember(owner = "client.client!cb", name = "h", descriptor = "I")
	public final int occludeLevel;

	@OriginalMember(owner = "client.client!cb", name = "i", descriptor = "Lclient!o;")
	public TileUnderlay underlay;

	@OriginalMember(owner = "client.client!cb", name = "j", descriptor = "Lclient!i;")
	public TileOverlay overlay;

	@OriginalMember(owner = "client.client!cb", name = "k", descriptor = "Lclient!q;")
	public Wall wall;

	@OriginalMember(owner = "client.client!cb", name = "l", descriptor = "Lclient!h;")
	public Decor decor;

	@OriginalMember(owner = "client.client!cb", name = "m", descriptor = "Lclient!j;")
	public GroundDecor groundDecor;

	@OriginalMember(owner = "client.client!cb", name = "n", descriptor = "Lclient!k;")
	public GroundObject groundObj;

	@OriginalMember(owner = "client.client!cb", name = "o", descriptor = "I")
	public int locCount;

	@OriginalMember(owner = "client.client!cb", name = "p", descriptor = "[Lclient!p;")
	public final Location[] locs = new Location[5];

	@OriginalMember(owner = "client.client!cb", name = "q", descriptor = "[I")
	public final int[] locSpan = new int[5];

	@OriginalMember(owner = "client.client!cb", name = "r", descriptor = "I")
	public int locSpans;

	@OriginalMember(owner = "client.client!cb", name = "s", descriptor = "I")
	public int drawLevel;

	@OriginalMember(owner = "client.client!cb", name = "t", descriptor = "Z")
	public boolean visible;

	@OriginalMember(owner = "client.client!cb", name = "u", descriptor = "Z")
	public boolean update;

	@OriginalMember(owner = "client.client!cb", name = "v", descriptor = "Z")
	public boolean containsLocs;

	@OriginalMember(owner = "client.client!cb", name = "w", descriptor = "I")
	public int checkLocSpans;

	@OriginalMember(owner = "client.client!cb", name = "x", descriptor = "I")
	public int blockLocSpans;

	@OriginalMember(owner = "client.client!cb", name = "y", descriptor = "I")
	public int inverseBlockLocSpans;

	@OriginalMember(owner = "client.client!cb", name = "z", descriptor = "I")
	public int backWallTypes;

	@OriginalMember(owner = "client.client!cb", name = "A", descriptor = "Lclient!cb;")
	public Ground bridge;

	@OriginalMember(owner = "client.client!cb", name = "<init>", descriptor = "(III)V")
	public Ground(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
		this.occludeLevel = this.level = level;
		this.x = x;
		this.z = z;
	}
}
