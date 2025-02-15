/*
 * Copyright (c) 2019, Lucas <https://github.com/Lucwousin>
 * All rights reserved.
 *
 * This code is licensed under GPL3, see the complete license in
 * the LICENSE file in the root directory of this submodule.
 */
package com.openosrs.injector.injectors;

import com.openosrs.injector.injection.InjectData;
import net.runelite.asm.ClassFile;
import net.runelite.asm.Interfaces;
import net.runelite.asm.pool.Class;
import com.openosrs.injector.DeobAnnotations;

import java.util.ArrayList;

import static com.openosrs.injector.rsapi.RSApi.API_BASE;

public class InterfaceInjector extends AbstractInjector
{
	private int implemented = 0;

	public InterfaceInjector(InjectData inject)
	{
		super(inject);
	}

	public void inject()
	{
		ArrayList<ClassFile> injected = new ArrayList<ClassFile>();
		// forEachPair performs actions on a deob-vanilla pair, which is what's needed here
		inject.forEachPair((deobCf, vanillaCf) -> {
			final String fullName = API_BASE + deobCf.getClassName();
			if (injectInterface(deobCf, vanillaCf)) {
				if (!injected.contains(deobCf)) {
					injected.add(deobCf);
					log.debug("Injected interface " + fullName + " to " + deobCf.getName());
				}
			}
		});
	}

	private boolean injectInterface(final ClassFile deobCf, final ClassFile vanillaCf)
	{
		final String fullName = API_BASE + deobCf.getClassName();
		if (!inject.getRsApi().hasClass(fullName)) {
			//log.error("[DEBUG] Class " + deobCf.getName() + " implements nonexistent interface " + fullName + ", skipping interface injection");
			return false;
		}else {
			inject.classMap.put(vanillaCf.getClassName(), vanillaCf.getName());
		}

		final Interfaces interfaces = vanillaCf.getInterfaces();
		interfaces.addInterface(new Class(fullName));
		implemented++;

		inject.addToDeob(fullName, deobCf);
		return true;
	}
}
